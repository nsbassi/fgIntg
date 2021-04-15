package com.oic.dm.services

import com.oic.dm.config.AppConfig
import com.oic.dm.config.DBInfo
import com.oic.dm.config.JobInfo
import com.oic.dm.model.Job
import com.oic.dm.model.JobDetail
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.sql.DataSource
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
class DBService {
    static Logger logger = LoggerFactory.getLogger(DBService.class)

    DataSource dataSource

    @Autowired
    AppConfig appConfig

    DBInfo dbInfo

    DateTimeFormatter formatter

    @PostConstruct
    void init() {
        dbInfo = appConfig.dbInfo
        logger.debug("Connecting to database - $dbInfo.url")

        Properties props = new Properties()
        props.putAll([cachePrepStmts: true, prepStmtCacheSize: 250, prepStmtCacheSqlLimit: 2048])
        dataSource = new HikariDataSource(new HikariConfig(jdbcUrl: dbInfo.url, username: dbInfo.username,
                password: dbInfo.password, dataSourceProperties: props))
        logger.info("Connected to database - $dbInfo.url")
        formatter = DateTimeFormatter.ofPattern(appConfig.dateFormat)
    }

    Job saveJobLog(Job job, boolean update = false) {
        def sql = new Sql(dataSource)
        if (update) {
            sql.executeUpdate(appConfig.dbInfo.query.jobLogUpd, job)
        } else {
            def res = sql.executeInsert(appConfig.dbInfo.query.jobLog, job)
            println(res)
        }
        sql.close()
        job
    }

    List<Job> loadJobInfo(String instance) {
        def sql = new Sql(dataSource)
        List<Job> jobs = []
        sql.eachRow(appConfig.dbInfo.query.jobDef, [instance: instance]) { row ->
            jobs << toJob(row.toRowResult())
        }
        sql.close()
        jobs
    }

    Job loadJobInfo(String instance, int id) {
        def sql = new Sql(dataSource)
        def job = toJob(sql.firstRow(appConfig.dbInfo.query.jobDef + ' and id=:id', [instance: instance, id: id]))
        sql.close()
        job
    }


    static Job toJob(GroovyRowResult row) {
        def job = new Job()
        job.id = row.id ? new BigInteger(row.id.toString()) : null
        job.type = row.job_type
        job.label = row.label
        job.supportsNet = row.supports_net
        job.supportsTarget = row.supports_target
        job.option = row.default_mode
        job.filePrefix = row.file_prefix
        job.status = row.status
        job.hasLogs = (row.has_logs == 'true')
        job.essJobId = row.ess_id ? new BigInteger(row.ess_id.toString()) : null
        job.logId = row.log_id ? new BigInteger(row.log_id.toString()) : null
        job.group = row.category
        job.icon = row.icon
        job.runBy = row.run_by
        try {
            if (row.last_run != null) {
                SimpleDateFormat sdf = new SimpleDateFormat('dd-MMM-yyyy hh:mm:ss XXX')
                SimpleDateFormat sdf1 = new SimpleDateFormat('dd-MMM-yy HH:mm:ss')
                job.lastRunDate = sdf1.format(sdf.parse(row.last_run))
            }
        } catch (Exception ex) {
        }
        job
    }

    File buildFile(Job job, String jobNum) {
        def sql = new Sql(dataSource)
        List<File> files = []
        def dir = new File(appConfig.stagingDir, jobNum)
        dir.mkdirs()

        SimpleDateFormat sdf = new SimpleDateFormat(appConfig.fileNameSuffixFormat)
        String suffix = sdf.format(new Date())
        getDetails(job.id as Integer).each { jd ->
            File file = getNextFile(dir, jd.interfaceTbl, 'csv', suffix)

            file << "\"${jd.headers.join('","')}\"\n"

            sql.rows(['select', jd.headers.join(','), 'from', jd.stageTbl].join(' ')).each { row ->
                file << ('"' + jd.headers.collect { col ->
                    def val = row."${col.trim()}" ?: ''
                    val instanceof Timestamp ? val.toLocalDateTime().format(formatter) : val
                }.join('","') + '"\n')
            }
            files << file
        }

        File file = new File(dir, 'CSVController.properties')
        file.write('"/oracle/apps/ess/scm/advancedPlanning/collection/configuration/","CSVController","planningImp",' +
                '"' + job.instance + '",' + (job.option == 'Target' ? 1 : 2) + ',#NULL,#NULL,#NULL,300000006810154,3,\'0\',#NULL,#NULL,#NULL,#NULL')
        files << file

        sql.close()
        zipFiles(files, getNextFile(dir, job.filePrefix, 'zip', suffix))
    }

    List<JobDetail> getDetails(Integer id) {
        List<JobDetail> list = []
        def sql = new Sql(dataSource)
        sql.rows(appConfig.dbInfo.query.jobDetails, [id: id]).each { row ->
            def jd = new JobDetail()
            jd.id = row.id
            jd.jobId = row.job_id
            jd.stageTbl = row.stg_tbl
            jd.interfaceTbl = row.interface_tbl
            jd.headers = row.headers.toString().split(',')
            list << jd
        }
        sql.close()
        list
    }

    static File getNextFile(File parentDir, String name, String ext, String suffix) {
        File file = new File(parentDir, "${name}_${suffix}.$ext")
        if(file.exists()) {
            int idx = parentDir.list()
                    .findAll { it.matches(~"^${name}_$suffix-\\d*\\.${ext}\$") }.collect {
                def matcher = it =~ "^(${name}_)(\\d*)(${ext})\$"
                (matcher[0][2]).toInteger()
            }.max() ?: 0
            file = new File(parentDir, "${name}_${suffix}-${++idx}.$ext")
        }
        file
    }

    static File zipFiles(List<File> files, File zipFile) {
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))
        files.each { file ->
            zipOutputStream.putNextEntry(new ZipEntry(file.name))
            def buffer = new byte[file.size()]
            file.withInputStream {
                zipOutputStream.write(buffer, 0, it.read(buffer))
            }
            zipOutputStream.closeEntry()
        }
        zipOutputStream.close()
        zipFile
    }
}
