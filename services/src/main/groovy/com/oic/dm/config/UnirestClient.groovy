package com.oic.dm.config

import com.oic.dm.model.ApiResponse
import kong.unirest.HttpMethod
import kong.unirest.HttpResponse
import kong.unirest.MultipartBody
import kong.unirest.Unirest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

import javax.annotation.PostConstruct

@Configuration
class UnirestClient {

    @Autowired
    AppConfig config

    FusionInfo info

    static Logger logger = LoggerFactory.getLogger(UnirestClient.class)

    @PostConstruct
    void init() {
        info = config.fusionInfo
        Unirest.config().socketTimeout(config.socTimeout.toMillis().toInteger())
                .connectTimeout(config.conTimeout.toMillis().toInteger())
                .concurrency(300, 60)
    }

    public <T> ApiResponse<T> sendRequest(String method, def body, String path, String token, Map<String, Map<String, ?>> params, Class<T> clazz) {
        ApiResponse<T> respOut = null
        def headers = [Authorization: token]
        if (params?.headers) headers.putAll(params.headers)

        def req = Unirest.request(method, path).headers(headers)
        if (params?.routeParam) req.routeParam(params.routeParam)
        if (params?.queryString) req.queryString(params.queryString)
        if (body) req.body(body)
        req.asObject(clazz)
                .ifSuccess({ response ->
                    logger.debugEnabled && logger.debug('Response-> {}', response.body)
                    respOut = new ApiResponse<T>(response.status, response.statusText, response.body)
                })
                .ifFailure({ response ->
                    logger.error('Request-> {}', [method: method, url: path, headers: headers])
                    logger.error('Response-> headers:{}, body:{}', response.headers, response.body)
                    T result = response.body
                    response.parsingError.ifPresent({ e ->
                        result = clazz.newInstance()
                        result.error = e.originalBody
                    })
                    respOut = new ApiResponse<T>(response.status, response.statusText, result)
                })
        respOut
    }

    static ApiResponse<String> sendRequest(String method, def body, String path, String token, Map<String, Map<String, ?>> params) {
        def headers = [Authorization: token]
        if (params?.headers) headers.putAll(params.headers)

        def req = Unirest.request(method, path).headers(headers)
        if (params?.routeParam) req.routeParam(params.routeParam)
        if (params?.queryString) req.queryString(params.queryString)
        if (body) req.body(body)

        ApiResponse<String> respOut = null
        req.asString().ifSuccess({ response ->
            logger.debugEnabled && logger.debug('Response-> {}', response.body)
            respOut = new ApiResponse<String>(response.status, response.statusText, response.body)
        }).ifFailure({ response ->
            logger.error('Request-> {}', [method: method, url: path, headers: headers])
            logger.error('Response-> headers:{}, body:{}', response.headers, response.body)
            respOut = new ApiResponse<String>(response.status, response.statusText, response.body)
        })
        respOut
    }

    static def downloadFiles(HttpMethod method, String url, String token, Map<String, Map<String, ?>> params) {

        def headers = [Authorization: token]
        if (params?.headers) headers.putAll(params.headers)

        def req = Unirest.request(method.name(), url).headers(headers)
        if (params?.routeParam) req.routeParam(params.routeParam)
        if (params?.queryString) req.queryString(params.queryString)

        HttpResponse<MultipartBody> response = req.asObject(MultipartBody.class).ifSuccess({
            r -> r.body
        })

    }
}
