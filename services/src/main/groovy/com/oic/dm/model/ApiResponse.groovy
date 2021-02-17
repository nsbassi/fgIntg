package com.oic.dm.model

class ApiResponse<T> {
    int status
    String message
    T result

    ApiResponse(int status, String message, T result){
        this.status = status
        this.message = message
        this.result = result
    }
}

