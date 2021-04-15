package com.oic.dm.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.oic.dm.config.AppConfig
import com.oic.dm.config.UnirestClient
import com.oic.dm.model.*
import groovy.json.JsonOutput
import groovy.xml.MarkupBuilder
import groovy.xml.XmlSlurper
import kong.unirest.Unirest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpServerErrorException

import java.text.SimpleDateFormat

@Service
class JobService {
    @Autowired
    AppConfig config
    @Autowired
    UnirestClient client
    String lastJobPrefix
    int seq

    @Autowired
    ObjectMapper mapper

    SimpleDateFormat sdf = new SimpleDateFormat('yyyyMMdd')

    @Autowired
    DBService dbService

    Job initiate(Job job) {
        job.essJobId = null
        job.parentId = null
        job.status = 'unknown'
        job.lastRunDate = new Date()
        job.runBy = config.fusionInfo.oicUsername
        try {
            String jobNum = nextJobNumber
            File file = dbService.buildFile(job, jobNum)
            if (file) {
                if (config.useOIC) {
                    String endpoint = config.fusionInfo.intEndPoint
                    ApiResponse<Result> response = client.sendRequest('get', null, config.fusionInfo.oicUrl + endpoint, token,
                            [routeParam: [jobId: jobNum], queryString: [file: file.name]], Result.class) as ApiResponse<Result>
                    job.parentId = response.result.id
                } else {
                    String endpoint = config.fusionInfo.intFusionIntSvc
                    StringWriter sw = new StringWriter()
                    file.bytes.encodeBase64().writeTo(sw)
                    String content = sw.toString()
                    String url = config.fusionInfo.fusionUrl + endpoint
                    def headers = ['Content-Type': 'application/json', Authorization: FAToken]
                    String payload = JsonOutput.toJson([OperationName  : 'importBulkData',
                                                        DocumentContent: content.toString(),
                                                        ContentType    : 'zip',
                                                        FileName       : file.name,
                                                        JobName        : '/oracle/apps/ess/scm/advancedPlanning/collection/configuration,CSVController',
                                                        ParameterList  : 'FGP,1,#NULL,#NULL,#NULL,300000006810154,3,\'0\',#NULL'])
                    def response = Unirest.post(url).body(payload).headers(headers).asString()
                    ImportBulkResult result = mapper.readValue(response.body, ImportBulkResult.class)
                    job.essJobId = result.reqstId as BigInteger
                    job.status = 'success'
                }
                dbService.saveJobLog(job)
                findJob(job)
            } else {
                job.status = 'failed'
                dbService.saveJobLog(job)
            }
        } catch (Exception e) {
            job.status = 'failed'
            dbService.saveJobLog(job)
            throw e
        }
        job
    }

    Job findJob(Job job) {
        if (!job.essJobId) {
            ApiResponse<JobItem> jobItemRes = client.sendRequest('get', null, config.fusionInfo.oicUrl +
                    '/ic/api/integration/v1/monitoring/instances/{id}', token, [routeParam: [id: job.parentId]],
                    JobItem.class) as ApiResponse<JobItem>
            JobItem jobItem = jobItemRes.result
            if (jobItem) {
                if (jobItem.status in ['FAILED', 'PARTIAL_COMPLETION', 'ABORTED']) {
                    job.status = 'failed'
                    job.running = false
                } else if (jobItem.status == 'COMPLETED') {
                    job.status = 'success'
                    job.essJobId = new BigInteger(jobItem.trackings.find { it.name == 'tracking_var_2' }?.value)
                    getEssJobStatus(job)
                }
            }
        } else {
            getEssJobStatus(job)
        }
        dbService.saveJobLog(job)
        job
    }

    Job getEssJobStatus(Job job) {
        def sdfLong = new SimpleDateFormat('yyyy-MM-dd\'T\'hh:mm:ss.SSS\'Z\'')
        sdfLong.timeZone = TimeZone.getTimeZone('GMT')
        StringWriter sw = new StringWriter()

        def xml = new MarkupBuilder(sw)
        xml.'soapenv:Envelope'('xmlns:soapenv': 'http://schemas.xmlsoap.org/soap/envelope/') {
            'soapenv:Header' {
                'wsse:Security'('soapenv:mustUnderstand': '1',
                        'xmlns:wsse': 'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd',
                        'xmlns:wsu':'http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd'
                ) {
                    'wsu:Timestamp'('wsu:Id': 'TS-ABC7436A2216C903F234' + ((int) (Math.random() * 1000000000))) {
                        'wsu:Created'(sdfLong.format(new Date()))
                        'wsu:Expires'(sdfLong.format(new Date(new Date().time + 60000)))
                    }
                    'wsse:UsernameToken'('wsu:Id': 'UsernameToken-ABC7436A2216C903F1' + ((int) (Math.random() * 1000000000))) {
                        'wsse:Username'(config.fusionInfo.fusionUsername)
                        'wsse:Password'(config.fusionInfo.fusionPassword)
                    }
                }
            }
            'soapenv:Body' {
                'sch:getRequestState'('xmlns:sch': 'http://xmlns.oracle.com/scheduler') {
                    'sch:requestId'(job.essJobId)
                }
            }
        }
        String payload = sw.toString()
        String endpoint = config.fusionInfo.essWsEndPoint
        ApiResponse<String> response = client.sendRequest('post', payload, config.fusionInfo.fusionUrl +
                endpoint, token, [headers: ['Content-Type': 'text/xml;charset=UTF-8']])

        if (response.status == 200) {
            def status = new XmlSlurper().parseText(response.result)?.Body?.getRequestStateResponse?.state
            if (status == 'SUCCEEDED') job.status = 'success'
            if (job.status in config.fusionInfo.essFailedStatusList) job.status = 'failed'
        }
        job
    }

    File getLogs(String essJobId) {
        client.sendPostRequest
    }

    String getToken() {
        StringWriter sw = new StringWriter()
        sw.write('Basic ')
        "${config.fusionInfo.oicUsername}:${config.fusionInfo.oicPassword}".bytes.encodeBase64().writeTo(sw)
        sw.toString()
    }

    String getFAToken() {
        StringWriter sw = new StringWriter()
        sw.write('Basic ')
        "${config.fusionInfo.fusionUsername}:${config.fusionInfo.fusionPassword}".bytes.encodeBase64().writeTo(sw)
        sw.toString()
    }

    String getNextJobNumber() {
        String prefix = 'J' + sdf.format(new Date())
        if (prefix != lastJobPrefix)
            seq = 0
        lastJobPrefix = prefix
        prefix + '-' + "${++seq}".padLeft(4, '0')
    }
}
