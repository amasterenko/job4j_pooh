package ru.job4j.pooh;

import java.util.Scanner;
/**
 *The Req class parses and stores request parameters and provides methods for accessing them
 *@author AndrewMs
 *@version 1.0
 */

public class Req {
    private final String method;
    private final String mode;
    private final String destination;
    private final String clientId;
    private final String text;

    /**
     * Constructs a request object
     * @param method GET|POST method
     * @param mode queue|topic mode
     * @param dest the name of queue|topic
     * @param clientId id of topic's subscriber (topic mode)
     * @param text text message (POST requests)
     */
    private Req(String method, String mode, String dest, String clientId, String text) {
        this.method = method;
        this.mode = mode;
        this.destination = dest;
        this.clientId = clientId;
        this.text = text;
    }

    /**
     * Parses a request and build a Req object.
     * @param content request string received from a client
     * @return Req object
     * @throws IllegalArgumentException when the request has wrong format
     */
    public static Req of(String content) throws IllegalArgumentException {
        try {
            Scanner sc = new Scanner(content);
            String firstLine = sc.nextLine();
            String method = firstLine.split(" ")[0];
            String mode = firstLine.split(" ")[1].split("/")[1];
            String destination = firstLine.split(" ")[1].split("/")[2];
            String clientId = "";
            String text = "";
            if ("topic".equals(mode) && "GET".equals(method)) {
                clientId = firstLine.split(" ")[1].split("/")[3];
            }
            if ("POST".equals(method.split(" ")[0])) {
                while (sc.hasNext()) {
                    text = sc.nextLine();
                }
            }
            return new Req(method, mode, destination, clientId, text);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Wrong request format!");
        }
    }

    /**
     * @return method (GET|POST)
     */
    public String method() {
        return method;
    }

    /**
     * @return mode (queue|topic)
     */
    public String mode() {
        return mode;
    }

    /**
     * @return queue's|topic's name
     */
    public String destination() {
        return destination;
    }

    /**
     * @return id of topic's subscriber
     */
    public String clientId() {
        return clientId;
    }

    /**
     * @return text message (POST requests)
     */
    public String text() {
        return text;
    }
}