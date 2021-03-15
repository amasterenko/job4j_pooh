package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 * Serves queue-mode requests.
 *@author AndrewMs
 *@version 1.0
 */
public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queues = new ConcurrentHashMap<>();

    /**
     * Processes queue-mode request and returns a response object.
     * For creating a new queue the publisher must send a POST request with the queue's name parameter.
     *
     * Response codes:
     * 200 - POST/GET request processed correctly
     * 400 - incorrect POST/GET request
     * @param req Req - Request object
     * @return Resp - Response object
     */
    @Override
    public Resp process(Req req) {
        if ("GET".equals(req.method())) {
            String text = queues.getOrDefault(req.destination(), emptyQueue()).poll();
            return new Resp(text, 200);
        }
        if ("POST".equals(req.method())) {
            queues.putIfAbsent(req.destination(), new ConcurrentLinkedQueue<>());
            queues.get(req.destination()).add(req.text());
            return new Resp(null, 200);
        }
        return new Resp(null, 400);
    }

    /**
     * @return stub queue
     */
    private ConcurrentLinkedQueue<String> emptyQueue() {
        return new ConcurrentLinkedQueue<>();
    }
}
