/*
 * DmitriyMX <dimon550@gmail.com>
 * 2017-04-11
 */
package ru.dmitriymx.vkapi.longpoll;

public interface EventListener {
    void process(Event event);
}
