package com.oic.dm.model

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder([
        'dataFetchTime',
        'items',
        'links',
        'totalResults'
])
class FindResult<T> {
    @JsonProperty('dataFetchTime')
    String dataFetchTime
    @JsonProperty('items')
    List<T> items = null
    @JsonProperty('links')
    List<Link> links = null
    @JsonProperty('totalResults')
    Integer totalResults
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder([
        'creationDate',
        'date',
        'flowType',
        'id',
        'integrationId',
        'integrationName',
        'integrationVersion',
        'isDataAccurate',
        'isLitmusFlow',
        'isLitmusSupported',
        'lastTrackedTime',
        'links',
        'litmusResultStatus',
        'mepType',
        'nonScheduleAsync',
        'processingEndDate',
        'projectFound',
        'receivedDate',
        'requestId',
        'runId',
        'status',
        'trackings'
])
class JobItem {
    @JsonProperty('creationDate')
    String creationDate
    @JsonProperty('date')
    String date
    @JsonProperty('flowType')
    String flowType
    @JsonProperty('id')
    String id
    @JsonProperty('integrationId')
    String integrationId
    @JsonProperty('integrationName')
    String integrationName
    @JsonProperty('integrationVersion')
    String integrationVersion
    @JsonProperty('isDataAccurate')
    Boolean isDataAccurate
    @JsonProperty('isLitmusFlow')
    Boolean isLitmusFlow
    @JsonProperty('isLitmusSupported')
    Boolean isLitmusSupported
    @JsonProperty('lastTrackedTime')
    String lastTrackedTime
    @JsonProperty('links')
    List<Link> links = null
    @JsonProperty('litmusResultStatus')
    String litmusResultStatus
    @JsonProperty('mepType')
    String mepType
    @JsonProperty('nonScheduleAsync')
    Boolean nonScheduleAsync
    @JsonProperty('processingEndDate')
    String processingEndDate
    @JsonProperty('projectFound')
    Boolean projectFound
    @JsonProperty('receivedDate')
    String receivedDate
    @JsonProperty('requestId')
    String requestId
    @JsonProperty('runId')
    String runId
    @JsonProperty('status')
    String status
    @JsonProperty('trackings')
    List<Tracking> trackings = null
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder([
        'href',
        'rel'
])
class Link {
    @JsonProperty('href')
    String href
    @JsonProperty('rel')
    String rel
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder([
        'name',
        'primary',
        'value'
])
class Tracking {
    @JsonProperty('name')
    String name
    @JsonProperty('primary')
    Boolean primary
    @JsonProperty('value')
    String value
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