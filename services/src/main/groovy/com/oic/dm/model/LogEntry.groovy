package com.oic.dm.model

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder([
        'actionId',
        'actionName',
        'actionType',
        'code',
        'instanceId',
        'logMessage',
        'operationName',
        'timeStamp',
        'version'
])
class LogEntry {
    @JsonProperty('actionId')
    String actionId
    @JsonProperty('actionName')
    String actionName
    @JsonProperty('actionType')
    String actionType
    @JsonProperty('code')
    String code
    @JsonProperty('instanceId')
    String instanceId
    @JsonProperty('logMessage')
    String logMessage
    @JsonProperty('operationName')
    String operationName
    @JsonProperty('timeStamp')
    String timeStamp
    @JsonProperty('version')
    String version
    @JsonIgnore
    Map<String, Object> additionalProperties = new HashMap<String, Object>()

    @JsonAnyGetter
    Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties
    }

    @JsonAnySetter
    void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value)
    }
}