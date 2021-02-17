package com.oic.dm.controllers

import com.oic.dm.config.AppConfig
import com.oic.dm.model.Job
import com.oic.dm.services.DBService
import com.oic.dm.services.JobService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping()
@CrossOrigin(origins = "*", maxAge = 3600l)
class MainController {
    @Autowired
    AppConfig config

    @Autowired
    DBService service

    @Autowired
    JobService jobService

    @GetMapping('/fatima')
    def hello() {
        return [msg: 'OK']
    }

    @PostMapping(value = '/initiate')
    Job initiateJob(@RequestBody Job job) {
        jobService.initiate(job)
    }

    @PostMapping(value = '/status')
    Job checkJobStatus(@RequestBody Job job) {
        jobService.getJobStatus(job)
    }

    @RequestMapping(method = [RequestMethod.GET], value = '/fatima/planners')
    ResponseEntity<Resource> getData() {
        File zipFile = service.buildPlannerFile()
        Resource resource = new InputStreamResource(new FileInputStream(zipFile))
        ResponseEntity.ok()
                .contentType(MediaType.parseMediaType('application/zip'))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$zipFile.name\"")
                .body(resource)
    }
}
