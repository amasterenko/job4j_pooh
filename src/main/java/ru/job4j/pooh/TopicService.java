package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Serves topic-mode requests.
 *@author AndrewMs
 *@version 1.0
 */
public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics
            = new ConcurrentHashMap<>();

    /**
     * Processes topic-mode request and returns a response object.
     * For creating a new topic the publisher must send a POST request with the topic's name parameter.
     * For subscribing to a topic the subscriber must send a GET request with the topic's name and clientId parameters.
     *
     * Response codes:
     * 200 - POST/GET request processed correctly
     * 201 - POST topic request/GET topic subscription request processed correctly
     * 400 - POST/GET request has incorrect format
     * @param req Req - request object
     * @return Resp - response object
     */
    @Override
    public Resp process(Req req) {
        if ("GET".equals(req.method())) {
            var subscriberQueue = topics
                    .getOrDefault(req.destination(), emptyMap())
                    .putIfAbsent(req.clientId(), new ConcurrentLinkedQueue<>());
            if (subscriberQueue == null) {
                return new Resp(null, 201);
            }
            String text = topics
                    .getOrDefault(req.destination(), emptyMap())
                    .getOrDefault(req.clientId(), emptyQueue())
                    .poll();
            return new Resp(text, 200);
        }
        if ("POST".equals(req.method())) {
            var topicMap = topics.putIfAbsent(req.destination(), new ConcurrentHashMap<>());
            if (topicMap == null) {
                return new Resp(null, 201);
            }
            topics.get(req.destination()).values().forEach(q -> q.add(req.text()));
            return new Resp(null, 200);
        }
        return new Resp(null, 400);
    }

    /**
     * @return stub map
     */
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> emptyMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * @return stub queue
     */
    private ConcurrentLinkedQueue<String> emptyQueue() {
        return new ConcurrentLinkedQueue<>();
    }
}