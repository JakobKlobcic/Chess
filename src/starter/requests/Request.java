package requests;

import java.util.HashMap;
import java.util.Map;

/**
 * The Request class represents an HTTP request with a specified method type and an optional request body.
 */
public class Request {

    /**
     * Enumeration representing the HTTP methods: POST, DELETE, GET, and PUT.
     */
    enum Method {
        POST,
        DELETE,
        GET,
        PUT
    }

    /**
     * The HTTP method type for the request.
     */
    Method methodType;

    /**
     * The request body associated with the HTTP request.
     */
    String requestBody;

    Map<String,String> headers = new HashMap<>();

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, String value){
        headers.put(key, value);
    }
    public String getHeader(String key){
        return headers.get(key);
    }


}


