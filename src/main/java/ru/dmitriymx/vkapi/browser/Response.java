/*
 * DmitriyMX <mail@dmitriymx.ru>
 * 2017-04-10
 */
package ru.dmitriymx.vkapi.browser;

public interface Response {
    int getStatus();
    String getContentType();
    String getContent();
}
