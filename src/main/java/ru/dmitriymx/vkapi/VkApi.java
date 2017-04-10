/*
 * DmitriyMX <mail@dmitriymx.ru>
 * 2017-04-10
 */
package ru.dmitriymx.vkapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dmitriymx.vkapi.browser.Browser;
import ru.dmitriymx.vkapi.browser.Response;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class VkApi {
    private static final String VKAPI_URL = "https://api.vk.com/method/";
    private final Logger logger = LoggerFactory.getLogger(VkApi.class);
    private final String accessToken;
    private final Browser browser;
    private final Gson gson;
    private String apiVersion = "5.62";
    private long callPause = 1200L;
    private long lastTime;
    private int call = 0;

    public VkApi(String accessToken, Browser browser) {
        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new RuntimeException("Access token don't be NULL or EMPTY!");
        }
        this.accessToken = accessToken;
        this.browser = browser;
        this.gson = new Gson();
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public long getCallPause() {
        return callPause;
    }

    public void setCallPause(long callPause) {
        this.callPause = callPause;
    }

    public JsonObject execApi(String methodApi, Map<String, String> params) throws VkApiException {
        checkCalls();
        String url = VKAPI_URL + methodApi;
        String postData = paramsToString(params);

        Response response;
        try {
            response = browser.post(url, postData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        chechResponse(response);

        JsonObject jsonObject = gson.fromJson(response.getContent(), JsonObject.class);
        if (jsonObject.has("error")) {
            throw new VkApiException(jsonObject);
        }

        return jsonObject;
    }

    public JsonObject longExecApi(String server, String key, long ts, long wait) throws VkApiException {
        String url = String.format("https://%s?act=a_check&key=%s&ts=%d&wait=%d&mode=2&version=1",
                server, key, ts, wait
        );

        Response response;
        try {
            response = browser.get(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        chechResponse(response);

        JsonObject jsonObject = gson.fromJson(response.getContent(), JsonObject.class);
        if (jsonObject.has("error")) {
            throw new VkApiException(jsonObject);
        }

        return jsonObject;
    }

    private String paramsToString(Map<String, String> params) {
        String collect = "";
        if (params != null && params.size() > 0) {
            collect = "&" + params.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));
        }
        return "access_token=" + accessToken + "&v=" + apiVersion + collect;
    }

    private void checkCalls() {
        if (call >= 3) {
            long currTime = System.currentTimeMillis();
            long diff = currTime - lastTime;

            if (diff <= callPause) {
                safeSleep(callPause);
            }

            lastTime = System.currentTimeMillis();
            call = 0;
        } else {
            safeSleep(callPause/3);
            call++;
        }
    }

    private void chechResponse(Response response) {
        if (response.getStatus() != 200) {
            throw new IllegalStateException(String.format("code != 200 (%d)", response.getStatus()));
        }

        if (!response.getContentType().equalsIgnoreCase("application/json") &&
                !response.getContentType().equalsIgnoreCase("text/javascript")) {
            throw new IllegalStateException(String.format("content type is not JSON/JavaScript (%s)", response.getContentType()));
        }

        if (response.getContent().isEmpty()) {
            throw new IllegalStateException("content is empty");
        }

        if (logger.isDebugEnabled()) {
            logger.debug(response.getContent());
        }
    }

    private void safeSleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignore) {
            // ignore
        }
    }
}
