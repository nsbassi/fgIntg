package com.oic.dm.model

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

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