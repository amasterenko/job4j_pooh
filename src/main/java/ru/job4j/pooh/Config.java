package ru.job4j.pooh;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The class parses config file
 */
public class Config {
    private final String path;

    public Config(String path) {
        this.path = path;
    }

    /**
     * Provides a map with the app settings.
     * @return HashMap<String, Integer>, where key - parameter name, value - parameter value.
     */
    public Map<String, Integer> getConfig() {
        Properties cfg = new Properties();
        Map<String, Integer> res = new HashMap<>(3);
        int serverPort = 9000;
        int threadPoolSize = Runtime.getRuntime().availableProcessors();
        int topicSubscrAmount = 10;
        try (InputStream in = new FileInputStream(path)) {
            cfg.load(in);
            serverPort = Integer.parseInt(cfg.getProperty("server.tcp_port", "9000"));
            threadPoolSize = Integer.parseInt(cfg.getProperty("thread_pool.size", "4"));
            topicSubscrAmount = Integer.parseInt(cfg.getProperty("topic_subscribers.amount", "10"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        res.put("serverPort", serverPort);
        res.put("threadPoolSize", threadPoolSize);
        res.put("topicSubscrAmount", topicSubscrAmount);
        return res;
    }
}
