package responses;

/**
 * The Response class represents an HTTP response with attributes indicating success,
 * a message associated with the response, and an HTTP status code.
 */
public class Response {

    /**
     * A boolean indicating whether the operation associated with the HTTP request was successful.
     */
    Boolean success;
    /**
     * A human-readable message providing additional information about the HTTP response.
     */
    String message;

    /**
     * The HTTP status code indicating the outcome of the operation.
     */
    Integer status;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}