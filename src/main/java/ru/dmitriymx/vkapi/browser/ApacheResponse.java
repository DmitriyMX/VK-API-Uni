/*
 * DmitriyMX <dimon550@gmail.com>
 * 2017-04-10
 */
package ru.dmitriymx.vkapi.browser;

public class ApacheResponse implements Response {
    private final int code;
    private final String contentType;
    private final String content;

    ApacheResponse(int code, String contentType, String content) {
        this.code = code;
        this.contentType = contentType;
        this.content = content;
    }

    @Override
    public int getStatus() {
        return code;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getContent() {
        return content;
    }
}
