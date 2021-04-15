package com.oic.dm.model

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder([
    "OperationName",
    "DocumentId",
    "DocumentContent",
    "FileName",
    "ContentType",
    "FileType",
    "DocumentAccount",
    "Comments",
    "ProcessName",
    "LoadRequestId",
    "JobPackageName",
    "JobDefName",
    "ReqstId",
    "RequestStatus",
    "JobName",
    "ParameterList",
    "NotificationCode",
    "CallbackURL",
    "JobOptions",
    "StatusCode",
    "ESSParameters",
    "links"
])
class ImportBulkResult {

    @JsonProperty("OperationName")
    private String operationName;
    @JsonProperty("DocumentId")
    private Object documentId;
    @JsonProperty("DocumentContent")
    private String documentContent;
    @JsonProperty("FileName")
    private String fileName;

    @JsonIgnore
    private String error
    
    @JsonProperty("ContentType")
    private String contentType;
    @JsonProperty("FileType")
    private Object fileType;
    @JsonProperty("DocumentAccount")
    private Object documentAccount;
    @JsonProperty("Comments")
    private Object comments;
    @JsonProperty("ProcessName")
    private Object processName;
    @JsonProperty("LoadRequestId")
    private Object loadRequestId;
    @JsonProperty("JobPackageName")
    private Object jobPackageName;
    @JsonProperty("JobDefName")
    private Object jobDefName;
    @JsonProperty("ReqstId")
    private String reqstId;
    @JsonProperty("RequestStatus")
    private Object requestStatus;
    @JsonProperty("JobName")
    private String jobName;
    @JsonProperty("ParameterList")
    private String parameterList;
    @JsonProperty("NotificationCode")
    private Object notificationCode;
    @JsonProperty("CallbackURL")
    private Object callbackURL;
    @JsonProperty("JobOptions")
    private Object jobOptions;
    @JsonProperty("StatusCode")
    private Object statusCode;
    @JsonProperty("ESSParameters")
    private Object eSSParameters;
    @JsonProperty("links")
    private List<Link> links = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("OperationName")
    public String getOperationName() {
        return operationName;
    }

    @JsonProperty("OperationName")
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    @JsonProperty("DocumentId")
    public Object getDocumentId() {
        return documentId;
    }

    @JsonProperty("DocumentId")
    public void setDocumentId(Object documentId) {
        this.documentId = documentId;
    }

    @JsonProperty("DocumentContent")
    public String getDocumentContent() {
        return documentContent;
    }

    @JsonProperty("DocumentContent")
    public void setDocumentContent(String documentContent) {
        this.documentContent = documentContent;
    }

    @JsonProperty("FileName")
    public String getFileName() {
        return fileName;
    }

    @JsonProperty("FileName")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonProperty("ContentType")
    public String getContentType() {
        return contentType;
    }

    @JsonProperty("ContentType")
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonProperty("FileType")
    public Object getFileType() {
        return fileType;
    }

    @JsonProperty("FileType")
    public void setFileType(Object fileType) {
        this.fileType = fileType;
    }

    @JsonProperty("DocumentAccount")
    public Object getDocumentAccount() {
        return documentAccount;
    }

    @JsonProperty("DocumentAccount")
    public void setDocumentAccount(Object documentAccount) {
        this.documentAccount = documentAccount;
    }

    @JsonProperty("Comments")
    public Object getComments() {
        return comments;
    }

    @JsonProperty("Comments")
    public void setComments(Object comments) {
        this.comments = comments;
    }

    @JsonProperty("ProcessName")
    public Object getProcessName() {
        return processName;
    }

    @JsonProperty("ProcessName")
    public void setProcessName(Object processName) {
        this.processName = processName;
    }

    @JsonProperty("LoadRequestId")
    public Object getLoadRequestId() {
        return loadRequestId;
    }

    @JsonProperty("LoadRequestId")
    public void setLoadRequestId(Object loadRequestId) {
        this.loadRequestId = loadRequestId;
    }

    @JsonProperty("JobPackageName")
    public Object getJobPackageName() {
        return jobPackageName;
    }

    @JsonProperty("JobPackageName")
    public void setJobPackageName(Object jobPackageName) {
        this.jobPackageName = jobPackageName;
    }

    @JsonProperty("JobDefName")
    public Object getJobDefName() {
        return jobDefName;
    }

    @JsonProperty("JobDefName")
    public void setJobDefName(Object jobDefName) {
        this.jobDefName = jobDefName;
    }

    @JsonProperty("ReqstId")
    public String getReqstId() {
        return reqstId;
    }

    @JsonProperty("ReqstId")
    public void setReqstId(String reqstId) {
        this.reqstId = reqstId;
    }

    @JsonProperty("RequestStatus")
    public Object getRequestStatus() {
        return requestStatus;
    }

    @JsonProperty("RequestStatus")
    public void setRequestStatus(Object requestStatus) {
        this.requestStatus = requestStatus;
    }

    @JsonProperty("JobName")
    public String getJobName() {
        return jobName;
    }

    @JsonProperty("JobName")
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @JsonProperty("ParameterList")
    public String getParameterList() {
        return parameterList;
    }

    @JsonProperty("ParameterList")
    public void setParameterList(String parameterList) {
        this.parameterList = parameterList;
    }

    @JsonProperty("NotificationCode")
    public Object getNotificationCode() {
        return notificationCode;
    }

    @JsonProperty("NotificationCode")
    public void setNotificationCode(Object notificationCode) {
        this.notificationCode = notificationCode;
    }

    @JsonProperty("CallbackURL")
    public Object getCallbackURL() {
        return callbackURL;
    }

    @JsonProperty("CallbackURL")
    public void setCallbackURL(Object callbackURL) {
        this.callbackURL = callbackURL;
    }

    @JsonProperty("JobOptions")
    public Object getJobOptions() {
        return jobOptions;
    }

    @JsonProperty("JobOptions")
    public void setJobOptions(Object jobOptions) {
        this.jobOptions = jobOptions;
    }

    @JsonProperty("StatusCode")
    public Object getStatusCode() {
        return statusCode;
    }

    @JsonProperty("StatusCode")
    public void setStatusCode(Object statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("ESSParameters")
    public Object getESSParameters() {
        return eSSParameters;
    }

    @JsonProperty("ESSParameters")
    public void setESSParameters(Object eSSParameters) {
        this.eSSParameters = eSSParameters;
    }

    @JsonProperty("links")
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty("links")
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

