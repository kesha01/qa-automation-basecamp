package apiclient;

import org.json.JSONObject;

public class Response {
    public int statusCode;
    public JSONObject body;

    public Response (int statusCode) {
        this.statusCode = statusCode;
    }

    public Response (int statusCode, JSONObject body) {
        this.statusCode = statusCode;
        this.body = body;
    }
}
