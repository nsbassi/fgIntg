package insight.fusion.jobservice.model

import com.fasterxml.jackson.annotation.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@JsonInclude(JsonInclude.Include.NON_NULL)
class IntegrationJob {
    /**
     * Callback URL
     * <p>
     * The callback URL of the service implemented by a customer to receive the job status of an Enterprise Scheduling Service job upon its completion.
     *
     */
    @Expose
    @JsonProperty('CallbackURL')
    @SerializedName('CallbackURL')
    @JsonPropertyDescription('The callback URL of the service implemented by a customer to receive the job status of an Enterprise Scheduling Service job upon its completion.')
    public String callbackURL
    /**
     * Comments
     * <p>
     * The action that appends the specified comments to a file.
     *
     */
    @Expose
    @JsonProperty('Comments')
    @SerializedName('Comments')
    @JsonPropertyDescription('The action that appends the specified comments to a file.')
    public String comments
    /**
     * Content Type
     * <p>
     * The type of content within a data file to be processed by the ERP Integration Service.
     *
     */
    @Expose
    @JsonProperty('ContentType')
    @SerializedName('ContentType')
    @JsonPropertyDescription('The type of content within a data file to be processed by the ERP Integration Service.')
    public String contentType
    /**
     * Document Account
     * <p>
     * The account parameter to search for files corresponding to a specified security account.
     *
     */
    @Expose
    @JsonProperty('DocumentAccount')
    @SerializedName('DocumentAccount')
    @JsonPropertyDescription('The account parameter to search for files corresponding to a specified security account.')
    public String documentAccount
    /**
     * Document Content
     * <p>
     * The content of the file in Base64 encoded format.
     *
     */
    @Expose
    @JsonProperty('DocumentContent')
    @SerializedName('DocumentContent')
    @JsonPropertyDescription('The content of the file in Base64 encoded format.')
    public String documentContent
    /**
     * Document Identifier
     * <p>
     * The unique identifier of a file associated with the row.
     *
     */
    @Expose
    @JsonProperty('DocumentId')
    @SerializedName('DocumentId')
    @JsonPropertyDescription('The unique identifier of a file associated with the row.')
    public String documentId
    /**
     * Parameters
     * <p>
     * The list of parameters used to run the Enterprise Scheduling Service job. The list determines the order of the parameters. The corresponding entry in the list should be blank when a given parameter is not passed.
     *
     */
    @Expose
    @JsonProperty('ESSParameters')
    @SerializedName('ESSParameters')
    @JsonPropertyDescription('The list of parameters used to run the Enterprise Scheduling Service job. The list determines the order of the parameters. The corresponding entry in the list should be blank when a given parameter is not passed.')
    public String eSSParameters
    /**
     * File Name
     * <p>
     * The name of the data file to be processed by the ERP Integration Service.
     *
     */
    @Expose
    @JsonProperty('FileName')
    @SerializedName('FileName')
    @JsonPropertyDescription('The name of the data file to be processed by the ERP Integration Service.')
    public String fileName
    /**
     * File Type
     * <p>
     * The file type that determines the execution details to be downloaded. Specify LOG as the file type to download the log and output file. Specify OUT as the file type to download only the output file. If no file type is specified, both the log files and output files are downloaded.
     *
     */
    @Expose
    @JsonProperty('FileType')
    @SerializedName('FileType')
    @JsonPropertyDescription('The file type that determines the execution details to be downloaded. Specify LOG as the file type to download the log and output file. Specify OUT as the file type to download only the output file. If no file type is specified, both the log files and output files are downloaded.')
    public String fileType
    /**
     * Job Definition Name
     * <p>
     * The name of the Enterprise Scheduling Service job definition that must be submitted.
     *
     */
    @Expose
    @JsonProperty('JobDefName')
    @SerializedName('JobDefName')
    @JsonPropertyDescription('The name of the Enterprise Scheduling Service job definition that must be submitted.')
    public String jobDefName
    /**
     * Job Name
     * <p>
     * The job name and job package name separated by a comma.
     *
     */
    @Expose
    @JsonProperty('JobName')
    @SerializedName('JobName')
    @JsonPropertyDescription('The job name and job package name separated by a comma.')
    public String jobName
    /**
     * Job Options
     * <p>
     * A web service operation input parameter. It specifies the inbound bulk data processing variation and the output file type that is generated when the data is processed.
     *
     */
    @Expose
    @JsonProperty('JobOptions')
    @SerializedName('JobOptions')
    @JsonPropertyDescription('A web service operation input parameter. It specifies the inbound bulk data processing variation and the output file type that is generated when the data is processed.')
    public String jobOptions
    /**
     * Job Package Name
     * <p>
     * The name of the Enterprise Scheduling Service job package that must be submitted.
     *
     */
    @Expose
    @JsonProperty('JobPackageName')
    @SerializedName('JobPackageName')
    @JsonPropertyDescription('The name of the Enterprise Scheduling Service job package that must be submitted.')
    public String jobPackageName
    /**
     * Load Request Identifier
     * <p>
     * The unique identifier of the load process that populates data in a product interface table.
     *
     */
    @Expose
    @JsonProperty('LoadRequestId')
    @SerializedName('LoadRequestId')
    @JsonPropertyDescription('The unique identifier of the load process that populates data in a product interface table.')
    public String loadRequestId
    /**
     * Notification Code
     * <p>
     * The two-digit code that determines the notification type and when the notification should be sent.
     *
     */
    @Expose
    @JsonProperty('NotificationCode')
    @SerializedName('NotificationCode')
    @JsonPropertyDescription('The two-digit code that determines the notification type and when the notification should be sent.')
    public String notificationCode
    /**
     * Operation Name
     * <p>
     * The name of the ERP Integration Service operations used to manage inbound and outbound data flows.
     *
     */
    @Expose
    @JsonProperty('OperationName')
    @SerializedName('OperationName')
    @JsonPropertyDescription('The name of the ERP Integration Service operations used to manage inbound and outbound data flows.')
    public String operationName
    /**
     * Parameter List
     * <p>
     * The list of parameters used to run the Enterprise Scheduling Service job. The list determines the order of the parameters. The corresponding entry in the list must be blank when a given parameter is not passed.
     *
     */
    @Expose
    @JsonProperty('ParameterList')
    @SerializedName('ParameterList')
    @JsonPropertyDescription('The list of parameters used to run the Enterprise Scheduling Service job. The list determines the order of the parameters. The corresponding entry in the list must be blank when a given parameter is not passed.')
    public String parameterList
    /**
     * Process Name
     * <p>
     * The name of a particular business application's data import process.
     *
     */
    @Expose
    @JsonProperty('ProcessName')
    @SerializedName('ProcessName')
    @JsonPropertyDescription('The name of a particular business application\'s data import process.')
    public String processName
    /**
     * Request Identifier
     * <p>
     * The unique identifier of the submitted Enterprise Scheduling Service job request.
     *
     */
    @Expose
    @JsonProperty('ReqstId')
    @SerializedName('ReqstId')
    @JsonPropertyDescription('The unique identifier of the submitted Enterprise Scheduling Service job request.')
    public String reqstId
    /**
     * Request Status
     * <p>
     * The current status of the Enterprise Scheduling Service job.
     *
     */
    @Expose
    @JsonProperty('RequestStatus')
    @SerializedName('RequestStatus')
    @JsonPropertyDescription('The current status of the Enterprise Scheduling Service job.')
    public String requestStatus
    /**
     * Status Code
     * <p>
     * The code representing the current status of the Enterprise Scheduling Service job.
     *
     */
    @Expose
    @JsonProperty('StatusCode')
    @SerializedName('StatusCode')
    @JsonPropertyDescription('The code representing the current status of the Enterprise Scheduling Service job.')
    public String statusCode
    /**
     * Items
     * <p>
     * Link Relations
     *
     */
    @Expose
    @JsonProperty('links')
    @SerializedName('links')
    @JsonPropertyDescription('Link Relations')
    public List<Link> links = new ArrayList<Link>()

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
