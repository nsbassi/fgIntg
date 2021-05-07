package com.oic.dm.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.oic.dm.config.AppConfig
import com.oic.dm.config.UnirestClient
import com.oic.dm.model.ApiResponse
import com.oic.dm.model.Job
import com.oic.dm.model.JobItem
import com.oic.dm.model.Result
import insight.fusion.jobservice.IntegrationService
import insight.fusion.jobservice.model.IntegrationJob
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import java.text.SimpleDateFormat

@Service
class JobService {
    static final String jobName = '/oracle/apps/ess/scm/advancedPlanning/collection/configuration,CSVController'

    @Autowired
    AppConfig config
    @Autowired
    UnirestClient client
    String lastJobPrefix
    int seq

    IntegrationService intService

    @Autowired
    ObjectMapper mapper

    SimpleDateFormat sdf = new SimpleDateFormat('yyyyMMdd')

    @Autowired
    DBService dbService

    @PostConstruct
    void init() {
        intService = new IntegrationService(epUrl: config.fusionInfo.fusionUrl + config.fusionInfo.intFusionIntSvc,
                mapper: mapper)
    }

    Job initiate(Job job, String faTokenIn) {
        job.with {
            essJobId = null
            parentId = null
            status = 'unknown'
            lastRunDate = new Date()
            runBy = config.fusionInfo.oicUsername
        }
        try {
            String jobNum = nextJobNumber
            File file = dbService.buildFile(job, jobNum)
            if (file) {
                if (config.useOIC) {
                    String endpoint = config.fusionInfo.intEndPoint
                    ApiResponse<Result> response = client.sendRequest('get', null, config.fusionInfo.oicUrl + endpoint, token,
                            [routeParam: [jobId: jobNum], queryString: [file: file.name]], Result.class) as ApiResponse<Result>
                    job.parentId = response.result.id
                    faTokenIn = FAToken
                } else {
                    String params = ''
                    IntegrationJob intJob = intService.importBulkData(file, jobName, params, faTokenIn)
                    if (intJob.reqstId) {
                        job.parentId = intJob.reqstId
                        job.essJobId = intJob.reqstId.toBigInteger()
                        job.status = 'initiated'
                        job.running = true
                    } else {
                        job.status = 'failed'
                    }
                }
                dbService.saveJobLog(job)
                findJob(job, faTokenIn)
            } else {
                job.status = 'failed'
                dbService.saveJobLog(job)
                throw new Exception("Input file to initiate import not found at $file.absolutePath")
            }
        } catch (Exception e) {
            job.status = 'failed'
            dbService.saveJobLog(job)
            throw e
        }
        job
    }

    Job findJob(Job job, String faTokenIn) {
        if (config.useOIC && !job.essJobId) {
            ApiResponse<JobItem> jobItemRes = client.sendRequest('get', null, config.fusionInfo.oicUrl +
                    '/ic/api/integration/v1/monitoring/instances/{id}', token, [routeParam: [id: job.parentId]],
                    JobItem.class) as ApiResponse<JobItem>
            JobItem jobItem = jobItemRes.result
            if (jobItem) {
                if (jobItem.status in ['FAILED', 'PARTIAL_COMPLETION', 'ABORTED']) {
                    job.status = 'failed'
                    job.running = false
                } else if (jobItem.status == 'COMPLETED') {
                    job.status = 'initiated'
                    job.essJobId = new BigInteger(jobItem.trackings.find { it.name == 'tracking_var_2' }?.value)
                    getEssJobStatus(job, faTokenIn)
                }
            }
        } else {
            getEssJobStatus(job, faTokenIn)
        }
        dbService.saveJobLog(job)
        job
    }

    Job getEssJobStatus(Job job, String faTokenIn) {
        IntegrationJob intJob = intService.getEssJobStatus(job.essJobId.toString(), faTokenIn)
        job.with {
            if (intJob.requestStatus == 'SUCCEEDED') {
                status = 'success'
                running = false
            }
            if (intJob.requestStatus in config.fusionInfo.essFailedStatusList) {
                status = 'failed'
                running = false
            }
            status = intJob.requestStatus
        }
        job
    }

    File getLogs(int essJobId, String faTokenIn) {
        IntegrationJob intJob = intService.downloadIntegrationJobExecutionDetails(essJobId, faTokenIn)
        File file = new File("${essJobId}.zip")
        file.bytes = intJob.documentContent.decodeBase64()
        file
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
        "${config.fusionInfo.oicUsername}:${config.fusionInfo.oicPassword}".bytes.encodeBase64().writeTo(sw)
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
