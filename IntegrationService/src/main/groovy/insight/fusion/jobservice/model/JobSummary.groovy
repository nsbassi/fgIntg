package insight.fusion.jobservice.model

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
class JobSummary {
    @JsonProperty('JOBS')
    public List<EssJobStatus> jobs = null
    @JsonProperty('SUMMARYSTATUS')
    public String summaryStatus
    @JsonProperty('DOCUMENTID')
    public String documentId
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