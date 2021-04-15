package com.oic.dm.config

import groovy.transform.ToString
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.stereotype.Component

import java.time.Duration

@ToString
@Component
@ConfigurationProperties(prefix = 'fatima')
class AppConfig {
    @NestedConfigurationProperty
    DBInfo dbInfo

    FusionInfo fusionInfo

    String dateFormat = 'yyyy/MM/dd', stagingDir = 'staging', fileNameSuffixFormat='yyyy-dd-MM-hh-mm-ss'
    Duration purgeFrequency = Duration.ZERO.plusDays(1)
    int retention = 365

    @NestedConfigurationProperty
    Proxy proxy

    boolean disableSSLHostVerification = false, useOIC = true

    Duration conTimeout = Duration.ZERO.plusSeconds(20), socTimeout = Duration.ZERO.plusSeconds(60)

    boolean devMode = false
}


