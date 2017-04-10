/*
 * DmitriyMX <mail@dmitriymx.ru>
 * 2017-04-10
 */
package ru.dmitriymx.vkapi;

import com.google.gson.JsonObject;

public class VkApiException extends Exception {
    public VkApiException(String message) {
        super(message);
    }

    public VkApiException(JsonObject jsonObject) {
        this(jsonObject.getAsJsonObject("error").get("error_msg").getAsString());
    }
}
