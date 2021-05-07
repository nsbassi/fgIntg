package insight.fusion.jobservice.model

import com.fasterxml.jackson.annotation.*


@JsonInclude(JsonInclude.Include.NON_NULL)
class EssJobStatus {
    @JsonProperty('JOBNAME')
    public String jobName
    @JsonProperty('JOBPATH')
    public String jobPath
    @JsonProperty('DOCUMENTNAME')
    public String documentName
    @JsonProperty('REQUESTID')
    public String requestId
    @JsonProperty('STATUS')
    public String status
    @JsonProperty('CHILD')
    public List<EssJobStatus> child = null
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