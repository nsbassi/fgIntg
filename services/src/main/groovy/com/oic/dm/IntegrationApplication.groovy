package com.oic.dm

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class IntegrationApplication {

	static void main(String[] args) {
		System.setProperty('jasypt.encryptor.password', System.getProperty('instanceKey', 'fatima-oic-backend'))
		SpringApplication.run(IntegrationApplication, args)
	}
}
