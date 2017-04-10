/*
 * DmitriyMX <dimon550@gmail.com>
 * 2017-04-11
 */
package ru.dmitriymx.vkapi.longpoll;

import com.google.gson.JsonObject;
import ru.dmitriymx.vkapi.VkApi;
import ru.dmitriymx.vkapi.VkApiException;

import java.util.*;

public class LongPollListener {
    private Thread thread;
    private LPRunner lpRunner;

    public static LongPollListener create(VkApi vkApi) throws VkApiException {
        JsonObject jsonObject = vkApi.execApi("messages.getLongPollServer",
                Collections.singletonMap("need_pts", "1"));
        jsonObject = jsonObject.getAsJsonObject("response");

        return new LongPollListener(
                vkApi,
                jsonObject.get("server").getAsString(),
                jsonObject.get("key").getAsString(),
                jsonObject.get("ts").getAsLong()
        );
    }

    private LongPollListener(VkApi vkApi, String server, String key, long ts) {
        this.lpRunner = new LPRunner(vkApi, server, key, ts);
        this.thread = new Thread(this.lpRunner, "Long poll listener");
    }

    public void start() {
        start(false);
    }

    public void start(boolean join) {
        thread.start();
        if (join) {
            try {
                thread.join();
            } catch (InterruptedException ignore) {
                // ignore
            }
        }
    }

    public void register(Class<? extends Event> clazz, EventListener listener) {
        List<EventListener> eventListeners = lpRunner.mapListeners.computeIfAbsent(clazz, list -> new ArrayList<>());
        eventListeners.add(listener);
    }
}
