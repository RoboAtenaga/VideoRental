package com.rensource.videorental.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.Nullable;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String responseCode;
    private String status;
    private String responseMessage;
    private Object data;
    private Object errors;

    public enum Status {

        SUCCESS("00"),
        FAILURE("99"),
        ERROR("01");

        public final String code;

        private Status(String code) {
            this.code = code;
        }
    }

    public ResponseDto() {
    }

    public ResponseDto(String responseCode, String responseMessage, Object data) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.data = data;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Nullable
    public String getStatus() {
        return status;
    }

    public void setStatus(@Nullable String status) {
        this.status = status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ResponseDto{" +
                "responseCode='" + responseCode + '\'' +
                ", status='" + status + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                ", data=" + data +
                ", errors=" + errors +
                '}';
    }
}
