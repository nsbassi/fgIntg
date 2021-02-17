package com.oic.dm.services

import com.oic.dm.config.AppConfig
import com.oic.dm.config.DBInfo
import com.oic.dm.model.Planner
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import groovy.sql.Sql
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.sql.DataSource
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
class DBService {
    static Logger logger = LoggerFactory.getLogger(DBService.class)

    DataSource dataSource

    @Autowired
    AppConfig appConfig

    DBInfo dbInfo

    @PostConstruct
    void inti() {
        dbInfo = appConfig.dbInfo
        logger.debug("Connecting to database - $dbInfo.url")

        Properties props = new Properties()
        props.putAll([cachePrepStmts: true, prepStmtCacheSize: 250, prepStmtCacheSqlLimit: 2048])
        dataSource = new HikariDataSource(new HikariConfig(jdbcUrl: dbInfo.url, username: dbInfo.username,
                password: dbInfo.password, dataSourceProperties: props))
        logger.info("Connected to database - $dbInfo.url")

        new Sql(dataSource).execute(dbInfo.query.createTbl)
    }

    File buildPlannerFile() {
        File file = getNextFile(new File(appConfig.stagingDir), /^Planners_(\d*).csv/, 'Planners_{idx}.csv')
        file << '"SR_INSTANCE_CODE","PLANNER_CODE","DESCRIPTION","EMPLOYEE_NUMBER","DISABLE_DATE","END"\n'
        def sql = new Sql(dataSource)
        sql.rows(dbInfo.query.planners).each { row -> file << new Planner(row as Map).toString() }
        file
        zipFiles([file], getNextFile(new File(appConfig.stagingDir), /^Planners_(\d*).zip/, 'Planners_{idx}.zip'))
    }

    File getNextFile(File parentDir, String pattern, String baseName) {
        int idx = parentDir.list()
                .findAll { it.matches(pattern) }.collect {
            def matcher = it =~ pattern
            (matcher[0][1]).toInteger()
        }.max() ?: 0

        new File(parentDir, baseName.replace('{idx}', "${idx + 1}"))
    }

    File zipFiles(List<File> files,  File zipFile){
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))
        files.each{file->
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
