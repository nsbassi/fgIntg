package insight.fusion.jobservice

import com.fasterxml.jackson.databind.ObjectMapper
import insight.fusion.jobservice.model.FAResults
import insight.fusion.jobservice.model.IntegrationJob
import kong.unirest.Unirest
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class IntegrationService {
    Logger logger = LoggerFactory.getLogger(IntegrationService.class)

    String epUrl, token
    ObjectMapper mapper

    IntegrationJob importBulkData(File file, String jobName, String params, String token, String callbackURL = '#NULL',
                                  String notificationCode = 30, String jobOptions = null) {
        StringWriter sw = new StringWriter()
        file.bytes.encodeBase64().writeTo(sw)
        String content = sw.toString()

        def job = new IntegrationJob(operationName: 'importBulkData', documentContent: content, contentType: 'zip',
                fileName: file.name, jobName: jobName, parameterList: params, callbackURL: callbackURL,
                notificationCode: notificationCode)
        if (jobOptions) job.jobOptions = jobOptions
        initiateJob(job, 'Bulk Import Job', token)
    }

    IntegrationJob uploadFileToUCM(File file, String account, String token, String contentType = 'zip') {
        StringWriter sw = new StringWriter()
        file.bytes.encodeBase64().writeTo(sw)
        String content = sw.toString()

        def job = new IntegrationJob(operationName: 'uploadFileToUCM', documentContent: content,
                contentType: contentType, fileName: file.name, documentAccount: account)

        initiateJob(job, 'Upload File To UCM', token)
    }

    IntegrationJob submitIntegrationJobRequest(String jobPackage, String jobDef, String essParam, String token) {
        def job = new IntegrationJob(operationName: 'submitIntegrationJobRequest', jobPackageName: jobPackage,
                jobDefName: jobDef, eSSParameters: essParam)
        initiateJob(job, 'Submit ESS Job Request', token)
    }

    IntegrationJob exportBulkData(String jobName, String params, String token,String callbackURL = '#NULL',
                                  String notificationCode = 30, String jobOptions = 'ExtractFileType=ALL') {
        def job = new IntegrationJob(operationName: 'exportBulkData', jobName: jobName, parameterList: params, callbackURL: callbackURL,
                notificationCode: notificationCode, jobOptions: jobOptions)
        initiateJob(job, 'Export Bulk Data Job', token)
    }

    IntegrationJob initiateJob(IntegrationJob job, String jobType, String token) {
        Unirest.post(epUrl)
                .header('authorization', token)
                .header('Content-Type', 'application/json')
                .body( mapper.writeValueAsString(job)).asObject(IntegrationJob.class)
                .ifSuccess(res -> { job = res.body })
                .ifFailure(res -> {
                    String reason = ''
                    res.parsingError.ifPresent(e -> {
                        reason = e.originalBody
                    });
                    logger.error('{} failed with status code {}. {}', jobType, res.status, reason)
                    throw new Exception("$jobType failed with status code $res.status. $reason")
                })
        job
    }

    IntegrationJob downloadIntegrationJobExecutionDetails(int requestId, String token) {
        FAResults results = getJob("ESSJobExecutionDetailsRF;requestId=$requestId,fileType=ALL",
                 token,'Download ESS Job Execution Details')
        results.count > 0 ? results.items.get(0) : null
    }

    IntegrationJob getEssExecutionDetails(String requestId) {
        FAResults results = getJob("ESSExecutionDetailsRF;requestId=$requestId",
                'Get ESS Execution Details')
        results.count > 0 ? results.items.get(0) : null
    }

    IntegrationJob getEssJobStatus(String requestId, String token) {
        FAResults results = getJob("ESSJobStatusRF;requestId=$requestId",
                token, 'Get ESS Job Status')
        results.count > 0 ? results.items.get(0) : null
    }

    FAResults getJob(String finder, String token, String jobType = 'Planned') {
        FAResults results = null

        def req = Unirest.get(epUrl).queryString('finder', finder)
                .header('authorization', token)

        req.asString()
                .ifSuccess(res -> {
                    results = mapper.readValue(res.body, FAResults.class)
                })
                .ifFailure(res -> {
                    logger.error('{} failed with status code {}. {}', jobType, res.status, res.body)
                    throw new Exception("$jobType failed with status code $res.status. $res.body")
                })
        results
    }
}
