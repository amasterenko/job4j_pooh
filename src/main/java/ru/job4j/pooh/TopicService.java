package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics
            = new ConcurrentHashMap<>();

    /**
     * Process a consumer's request and return a response object.
     * Response codes:
     * 200 - POST/GET request processed correctly
     * 201 - add topic POST request/subscription GET request processed correctly
     * 400 - incorrect POST/GET request
     * @param req Req - Request object
     * @return Resp - Response object
     */
    @Override
    public Resp process(Req req) {
        try {
            String command = req.method().split(" ")[0];
            String topicName = req.method().split(" ")[1].split("/")[2];
            if ("GET".equals(command)) {
                String clientId = req.method().split(" ")[1].split("/")[3];
                /* add new subscriber */
                var subscriberQueue = topics
                        .getOrDefault(topicName, emptyMap())
                        .putIfAbsent(clientId, new ConcurrentLinkedQueue<>());
                if (subscriberQueue == null) {
                    return new Resp(null, 201);
                }
                /* get a text from the client's queue*/
                String text = topics
                        .getOrDefault(topicName, emptyMap())
                        .getOrDefault(clientId, emptyQueue())
                        .poll();
                return new Resp(text, 200);
            }
            if ("POST".equals(command)) {
                /* add a topic if empty */
                var topicMap = topics.putIfAbsent(topicName, new ConcurrentHashMap<>());
                if (topicMap == null) {
                    return new Resp(null, 201);
                }
                /* put a text into all of the topic's queues*/
                topics.get(topicName).values().forEach(q -> q.add(req.text()));
                return new Resp(null, 200);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return new Resp(null, 400);
    }

    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> emptyMap() {
        return new ConcurrentHashMap<>();
    }

    private ConcurrentLinkedQueue<String> emptyQueue() {
        return new ConcurrentLinkedQueue<>();
    }
}