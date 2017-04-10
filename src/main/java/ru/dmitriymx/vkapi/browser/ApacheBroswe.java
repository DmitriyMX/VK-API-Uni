/*
 * DmitriyMX <dimon550@gmail.com>
 * 2017-04-10
 */
package ru.dmitriymx.vkapi.browser;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

public class ApacheBroswe implements Browser {
    private final HttpClient client = HttpClients.createDefault();
    private String userAgent = "Mozilla/5.0 (Linux; Android 4.2.2; GT-I9505 Build/JDQ39) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.59 Mobile Safari/537.36";

    @Override
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public Response get(String url) throws IOException {
        return request(new HttpGet(url));
    }

    @Override
    public Response post(String url, String data) throws IOException {
        HttpPost requestPost = new HttpPost(url);
        requestPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        if (data != null) {
            requestPost.setEntity(new StringEntity(data));
        }

        return request(requestPost);
    }

    private Response request(HttpRequestBase request) throws IOException {
        setup_headers(request);
        HttpResponse response = client.execute(request);

        try (InputStream streamContent = response.getEntity().getContent()) {
            String contentRaw = IOUtils.toString(streamContent, "UTF-8");
            String contentType = "text/plain";
            for (Header header : response.getAllHeaders()) {
                if (header.getName().equalsIgnoreCase("Content-Type")) {
                    contentType = header.getValue();
                    if (contentType.contains(";")) {
                        contentType = contentType.split(";")[0];
                    }
                    break;
                }
            }

            return new ApacheResponse(
                    response.getStatusLine().getStatusCode(),
                    contentType, contentRaw
            );
        }
    }

    private void setup_headers(HttpMessage request) {
        request.addHeader("Connection", "close");
        request.addHeader("Accept-Encoding", "deflate");
        request.addHeader("User-Agent", userAgent);
    }
}
