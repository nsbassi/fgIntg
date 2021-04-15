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




