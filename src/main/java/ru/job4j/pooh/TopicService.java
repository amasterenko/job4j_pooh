package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The class handles topic-mode requests.
 *@author AndrewMs
 *@version 1.0
 */
public class TopicService implements Service {
    private final static Map<String, Map<String, ConcurrentLinkedQueue<String>>> TOPICS = new ConcurrentHashMap<>();
    private final int numOfSubscribers;

    public TopicService(int numOfSubscribers) {
        this.numOfSubscribers = numOfSubscribers;
    }

    /**
     * Handles topic-mode request and returns a response object.
     * For creating a new topic the publisher must send a POST request with the topic's name parameter.
     * The service create a separate queue with the topic's messages for each subscriber.
     * Maximum number of subscribers is set by the
     *
     * Response codes:
     * 200 - POST/GET request has been processed correctly.
     * 400 - POST/GET request has incorrect format.
     * 404 - no results found.
     * @param req Req - request object.
     * @return Resp - response object.
     */
    @Override
    public Resp process(Req req) {
        if ("GET".equals(req.method())) {
            var topic = TOPICS.get(req.destination());
            if (topic == null || topic.get(req.clientId()) == null) {
                return new Resp("", "404 Not Found");
            }
            String text = topic.get(req.clientId()).poll();
            return new Resp(text, "200 OK");
        }
        if ("POST".equals(req.method())) {
            if (TOPICS.get(req.destination()) == null) {
                Map<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();
                for (int i = 1; i <= numOfSubscribers; i++) {
                    map.put(String.valueOf(i), new ConcurrentLinkedQueue<>());
                }
                TOPICS.put(req.destination(), map);
            }
            TOPICS.get(req.destination()).values().forEach(q -> q.add(req.text()));
            return new Resp("", "200 OK");
        }
        return new Resp("", "400 Bad Request");
    }

    /**
     * @return stub map.
     */
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> emptyMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * @return stub queue.
     */
    private ConcurrentLinkedQueue<String> emptyQueue() {
        return new ConcurrentLinkedQueue<>();
    }
}