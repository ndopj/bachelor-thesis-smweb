package cz.sm.ng.core.rest.responses;

import com.fasterxml.jackson.annotation.JsonRawValue;
import org.springframework.http.HttpStatus;

/**
 * @author Norbert Dopjera ndopjera@gmail.com
 */
public class HttpRestResponse {
    private final int code;
    private final String message;
    @JsonRawValue private final Object data;

    public HttpRestResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public HttpRestResponse(int code) { this(code, null, null); }
    public HttpRestResponse(HttpStatus statusCode) { this(statusCode.value(), statusCode.getReasonPhrase(), null); }

    public HttpRestResponse(HttpStatus statusCode, Object data) {
        this(statusCode.value(), statusCode.getReasonPhrase(), data);
    }

    public HttpRestResponse(HttpStatus statusCode, String message, Object data) {
        this(statusCode.value(), message, data);
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public Object getData() { return data; }
}
