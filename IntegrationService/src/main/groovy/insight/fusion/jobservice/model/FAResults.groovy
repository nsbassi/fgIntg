package insight.fusion.jobservice.model

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class FAResults {
    @JsonProperty('items')
    public List<IntegrationJob> items = null
    @JsonProperty('count')
    public Integer count
    @JsonProperty('hasMore')
    public Boolean hasMore
    @JsonProperty('limit')
    public Integer limit
    @JsonProperty('offset')
    public Integer offset
    @JsonProperty('links')
    public List<Link> links = null
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>()

    @JsonAnyGetter
    Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties
    }

    @JsonAnySetter
    void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value)
    }
}

