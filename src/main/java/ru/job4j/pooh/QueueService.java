package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 * The class handles queue-mode requests.
 *@author AndrewMs
 *@version 1.0
 */
public class QueueService implements Service {
    private final static ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> QUEUES = new ConcurrentHashMap<>();

    /**
     * Handles queue-mode request and returns a response object.
     * For creating a new queue the publisher must send a POST request with the queue's name parameter.
     *
     * Response codes:
     * 200 - POST/GET request has been processed correctly.
     * 400 - POST/GET request has incorrect format.
     * 404 - no results found.
     * @param req Req - Request object
     * @return Resp - Response object
     */
    @Override
    public Resp process(Req req) {
        if ("GET".equals(req.method())) {
            String queueName =  req.destination();
            String text = QUEUES.getOrDefault(queueName, emptyQueue()).poll();
            return  QUEUES.containsKey(queueName)
                    ? new Resp(text, "200 OK") : new Resp("", "404 Not Found");
        }
        if ("POST".equals(req.method())) {
            QUEUES.putIfAbsent(req.destination(), new ConcurrentLinkedQueue<>());
            QUEUES.get(req.destination()).add(req.text());
            return new Resp("", "200 OK");
        }
        return new Resp("", "400 Bad Request");
    }

    /**
     * @return stub queue
     */
    private ConcurrentLinkedQueue<String> emptyQueue() {
        return new ConcurrentLinkedQueue<>();
    }
}
