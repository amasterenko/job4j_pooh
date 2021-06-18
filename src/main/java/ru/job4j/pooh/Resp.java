package ru.job4j.pooh;

/**
 *The Resp class stores response parameters and provides methods to access them.
 *@author AndrewMs
 *@version 1.0
 */
public class Resp {
    private final String text;
    private final String status;

    public Resp(String text, String status) {
        this.text = text;
        this.status = status;
    }

    /**
     * @return text for a response's body
     */
    public String text() {
        return text;
    }

    /**
     * @return status code for a response's header
     */
    public String status() {
        return status;
    }
}
