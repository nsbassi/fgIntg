package com.oic.dm.controllers

import com.oic.dm.model.Job
import com.oic.dm.services.AuthService
import com.oic.dm.services.DBService
import com.oic.dm.services.JobService
import org.jasypt.util.text.BasicTextEncryptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping()
@CrossOrigin(origins = "*", maxAge = 3600l)
class MainController {

    @Autowired
    DBService dbService

    @Autowired
    JobService jobService

    @Autowired
    AuthService authService

    @GetMapping('/status')
    def status() {
        return [msg: 'OK']
    }

    @GetMapping('/encrypt')
    def encrypt(@RequestParam String key, @RequestParam String input) {
        BasicTextEncryptor enc = new BasicTextEncryptor()
        enc.setPasswordCharArray(key.toCharArray())
        enc.encrypt(input)
    }

    @GetMapping('/decrypt')
    def decrypt(@RequestParam String key, @RequestParam String input) {
        BasicTextEncryptor enc = new BasicTextEncryptor()
        enc.setPasswordCharArray(key.toCharArray())
        enc.decrypt(input)
    }

    @GetMapping(value = '/jobs')
    ResponseEntity<List<Job>> listJobs(HttpServletRequest request, @RequestParam String instance) {
        String token = request.getHeader('X-Authorization')
        if (authService.isValid(token)) {
            def jobs = dbService.loadJobInfo(instance)
            ResponseEntity.ok().body(jobs)
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body([])
        }
    }

    @GetMapping(value = '/job/{id}')
    ResponseEntity<Job> getJobInfo(HttpServletRequest request, @RequestParam String instance, @PathVariable int id) {
        String token = request.getHeader('X-Authorization')
        if (authService.isValid(token)) {
            ResponseEntity.ok().body(dbService.loadJobInfo(instance, id))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
    }

    @GetMapping(value = '/job/{id}/logs')
    ResponseEntity<Resource> getJobLogs(HttpServletRequest request, @PathVariable int id) {
        String token = request.getHeader('X-Authorization')
        if (authService.isValid(token)) {
            File zipFile = jobService.getLogs(id, token)
            Resource resource = new InputStreamResource(new FileInputStream(zipFile))
            ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType('application/zip'))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$zipFile.name\"")
                    .body(resource)
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
    }

    @PostMapping(value = '/initiate')
    ResponseEntity<Job> initiateJob(HttpServletRequest request, @RequestBody Job job) {
        String token = request.getHeader('X-Authorization')
        if (authService.isValid(token)) {
            ResponseEntity.ok().body(jobService.initiate(job, token))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
    }

    @PostMapping(value = '/status')
    ResponseEntity<Job> checkJobStatus(HttpServletRequest request, @RequestBody Job job) {
        String token = request.getHeader('X-Authorization')
        if (authService.isValid(token)) {
            ResponseEntity.ok().body(jobService.findJob(job, token))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        }
    }
}
