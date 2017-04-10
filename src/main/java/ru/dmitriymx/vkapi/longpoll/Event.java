/*
 * DmitriyMX <dimon550@gmail.com>
 * 2017-04-11
 */
package ru.dmitriymx.vkapi.longpoll;

import com.google.gson.JsonArray;

public class Event {
    private JsonArray jsonArray;

    Event(JsonArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public JsonArray getRawData() {
        return jsonArray;
    }
}
