package requests;

import java.util.HashMap;
import java.util.Map;

/**
 * The Request class represents an HTTP request with a specified method type and an optional request body.
 */
public class Request{
	/**
	 * Request headers
	 */
	Map<String, String> headers = new HashMap<>();

	public void addHeader(String key, String value){
		headers.put(key, value);
	}

	public String getHeader(String key){
		return headers.get(key);
	}


}


