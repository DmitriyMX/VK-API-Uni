/*
 * DmitriyMX <dimon550@gmail.com>
 * 2017-04-11
 */
package ru.dmitriymx.vkapi.longpoll;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dmitriymx.vkapi.VkApi;
import ru.dmitriymx.vkapi.VkApiException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LPRunner implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(LPRunner.class);
    private final VkApi vkApi;
    private final String server;
    private final String key;
    private long ts;
    Map<Class<? extends Event>, List<EventListener>> mapListeners = new HashMap<>();

    LPRunner(VkApi vkApi, String server, String key, long ts) {
        this.vkApi = vkApi;
        this.server = server;
        this.key = key;
        this.ts = ts;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (logger.isDebugEnabled()) {
                logger.debug("next loop...");
            }

            try {
                JsonObject jsonObject = vkApi.longExecApi(
                        this.server, this.key, this.ts,
                        25L
                );

                JsonArray updates = jsonObject.getAsJsonArray("updates");
                if (updates.size() > 0) {
                    final Event event = new Event(updates);
                    mapListeners.getOrDefault(Event.class, Collections.emptyList())
                            .forEach(listener -> listener.process(event));
                }

                this.ts = jsonObject.get("ts").getAsLong();
            } catch (VkApiException e) {
                logger.error("Oops!", e);
            }
        }
    }
}
