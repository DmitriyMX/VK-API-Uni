/*
 * DmitriyMX <mail@dmitriymx.ru>
 * 2017-04-10
 */
package ru.dmitriymx.vkapi.browser;

import java.io.IOException;

public interface Browser {
    void setUserAgent(String userAgent);
    Response get(String url) throws IOException;
    Response post(String url, String data) throws IOException;
}
