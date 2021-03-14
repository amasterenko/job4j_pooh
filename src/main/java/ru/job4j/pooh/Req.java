package ru.job4j.pooh;

import java.util.Scanner;

public class Req {
    private final String method;
    private final String mode;
    private final String text;

    private Req(String method, String mode, String text) {
        this.method = method; //GET|POST + PATH
        this.mode = mode; //queue|topic
        this.text = text; //text for POST requests
    }

    public static Req of(String content) {
        /* parse a content */
        try {
            Scanner sc = new Scanner(content);
            String firstLine = sc.nextLine();
            String method = firstLine.split(" ")[0] + " " + firstLine.split(" ")[1];
            String mode = firstLine.split(" ")[1].split("/")[1];
            String text = "";
            if ("POST".equals(method.split(" ")[0])) {
                while (sc.hasNext()) {
                    text = sc.nextLine();
                }
            }
            return new Req(method, mode, text);
        } catch (ArrayIndexOutOfBoundsException e) {
            return new Req("", "", "");
        }
    }

    public String method() {
        return method;
    }

    public String mode() {
        return mode;
    }

    public String text() {
        return text;
    }
}