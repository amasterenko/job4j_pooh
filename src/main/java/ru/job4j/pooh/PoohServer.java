package ru.job4j.pooh;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 *The PoohServer class implements socket server and handles http-like messages.
 *It has ExecutorService to manage client connections in multithreading mode.
 *@author AndrewMs
 *@version 1.0
 */
public class PoohServer {
    private static final HashMap<String, Service> MODES = new HashMap<>();

    /**
     * Gets properties from Config, creates ExecutorService, launches multithreading Socket Server.
     */
    public static void main(String[] args) {
        Config cfg = new Config("app.properties");

        MODES.put("queue", new QueueService());
        MODES.put("topic", new TopicService(cfg.getConfig().get("topicSubscrAmount")));

        ExecutorService pool = Executors.newFixedThreadPool(cfg.getConfig().get("threadPoolSize"));
        try (ServerSocket server = new ServerSocket(cfg.getConfig().get("serverPort"))) {
            System.out.println("Server has been started..");
            while (!server.isClosed()) {
                Socket socket = server.accept();
                pool.execute(() -> {
                    try (OutputStream out = socket.getOutputStream();
                         InputStream input = socket.getInputStream()) {
                        byte[] buff = new byte[1_000_000];
                        var total = input.read(buff);
                        var text = new String(Arrays.copyOfRange(buff, 0, total), StandardCharsets.UTF_8);
                        var req = Req.of(text);
                        var resp = MODES.get(req.mode()).process(req);
                        out.write(("HTTP/1.1 " + resp.status() + "\r\n").getBytes());
                        out.write((resp.text() + "\r\n").getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}