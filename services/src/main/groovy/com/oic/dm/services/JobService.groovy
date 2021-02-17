package com.oic.dm.services

import com.oic.dm.config.AppConfig
import com.oic.dm.config.UnirestClient
import com.oic.dm.model.*
import groovy.transform.Synchronized
import org.springframework.beans.factory.annotation.Autowired
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
    SimpleDateFormat sdf = new SimpleDateFormat('yyyyMMdd')

    Job initiate(Job job) {
        String endpoint = config.fusionInfo.jobs[job.type]
        if (!endpoint)
            throw new HttpServerErrorException(HttpStatus.NOT_IMPLEMENTED, "Import for job type ${job.type} is currently not implemented")
        ApiResponse<Result> response = client.sendRequest(config.fusionInfo.oicUrl + endpoint, token, [routeParam: [jobId: nextJobNumber]], Result.class)
        job.parentId = response.result.instanceId
        job.status = 'UNKNOWN'
        job.complete = false
        job.running = true
        findJob(job)
    }

    Job findJob(Job job) {
        ApiResponse<FindResult<JobItem>> responseFind = client.sendRequest(config.fusionInfo.oicUrl + '/ic/api/integration/v1/monitoring/instances', token,
                [queryString: [q: "{ tertiaryValue: '${job.parentId}' }"]], FindResult<JobItem>.class)
        def jobItem = responseFind.result.totalResults ? responseFind.result.items.first() : null
        if (jobItem) {
            job.status = jobItem.status
            job.link = jobItem.links.first().href
            job.id = jobItem.id
            if (job.status == 'COMPLETED')
                getESSJobId(job)
            job.complete = job.status in ['FAILED', 'COMPLETED', 'PARTIAL_COMPLETION', 'ABORTED']
            job.running = !job.complete
        }
        job
    }

    Job getJobStatus(Job job) {
        if (job.link) {
            ApiResponse<JobItem> response = client.sendRequest(job.link, token, [:], JobItem.class)
            job.status = response.result.status
            job.complete = job.status in ['FAILED', 'COMPLETED', 'PARTIAL_COMPLETION', 'ABORTED']
            if (job.status == 'COMPLETED')
                getESSJobId(job)
            job.running = !job.complete
        } else {
            findJob(job)
        }
        job
    }

    File getLogs(String essJobId){
        client.sendPostRequest
    }

    Job getESSJobId(job) {
        int attempts = 0
        while (job.essJobId == null && attempts < 5) {
            ApiResponse<FindResult<LogEntry>> responseFind = client.sendRequest(config.fusionInfo.oicUrl + '/ic/api/integration/v1/monitoring/integrationActivityStream', token,
                    [queryString: [q: "{ instanceId: '${job.id}' }"]], FindResult<LogEntry>.class)
            job.essJobId = responseFind.result.items.find { it.actionName == 'logResponse' }?.logMessage
            attempts++
        }
        job
    }

    String getToken() {
        StringWriter sw = new StringWriter()
        sw.write('Basic ')
        "${config.fusionInfo.oicUsername}:${config.fusionInfo.oicPassword}".bytes.encodeBase64().writeTo(sw)
        sw.toString()
    }

    @Synchronized
    String getNextJobNumber() {
        String prefix = 'J' + sdf.format(new Date())
        if (prefix != lastJobPrefix)
            seq = 0
        lastJobPrefix = prefix
        prefix + '-' + "${++seq}".padLeft(4, '0')
    }
}
