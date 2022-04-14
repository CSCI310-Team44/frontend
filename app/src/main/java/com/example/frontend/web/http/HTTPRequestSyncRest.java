package com.example.frontend.web.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Light weight abstraction over synchronous restful HTTP Requests.
 */
public class HTTPRequestSyncRest {

    private URL url;
    private String requestMethod;
    private int responseCode = 0;
    private String responseContent = "";

    public void sendAndAwaitResponse() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(requestMethod);

        responseCode = con.getResponseCode();

        BufferedReader in =
                new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        responseContent = content.toString();

        in.close();
        con.disconnect();
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseContent() {
        return responseContent;
    }
}
