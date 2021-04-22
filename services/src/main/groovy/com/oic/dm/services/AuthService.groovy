package com.oic.dm.services

import com.oic.dm.config.AppConfig
import com.oic.dm.config.UnirestClient
import com.oic.dm.model.ApiResponse
import kong.unirest.HttpResponse
import kong.unirest.Unirest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthService {
    @Autowired
    AppConfig config

    @Autowired
    UnirestClient client

    boolean isValid(String authToken){
        def payload = '''<Envelope xmlns="http://schemas.xmlsoap.org/soap/envelope/">
           <Body>
              <findSelfUserDetails xmlns="http://xmlns.oracle.com/apps/hcm/people/roles/userDetailsServiceV2/types/" />
           </Body>
        </Envelope>'''
        HttpResponse<String> response = Unirest.post(config.fusionInfo.fusionUrl + '/hcmService/UserDetailsServiceV2')
                .header("Content-Type", "text/xml;charset=UTF-8")
                .header("Authorization", authToken)
                .body(payload)
                .asString()
        response.status == 200
    }
}
