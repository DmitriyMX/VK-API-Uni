/*
 * DmitriyMX <mail@dmitriymx.ru>
 * 2017-04-10
 */
package ru.dmitriymx.vkapi;

import java.io.IOException;

public interface Browser {
    Response get(String url) throws IOException;
    Response post(String url, String data) throws IOException;
}
