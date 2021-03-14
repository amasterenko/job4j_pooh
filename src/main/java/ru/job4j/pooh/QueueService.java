package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queues = new ConcurrentHashMap<>();

    /**
     * Process a consumer's request and return a response object.
     * Response codes:
     * 200 - POST/GET request processed correctly
     * 400 - incorrect POST/GET request
     * @param req Req - Request object
     * @return Resp - Response object
     */
    @Override
    public Resp process(Req req) {
        try {
            String command = req.method().split(" ")[0];
            String queueName = req.method().split(" ")[1].split("/")[2];
            if ("GET".equals(command)) {
                String text = queues.getOrDefault(queueName, emptyQueue()).poll();
                return new Resp(text, 200);
            }
            if ("POST".equals(command)) {
                /* add a queue if empty */
                queues.putIfAbsent(queueName, new ConcurrentLinkedQueue<>());
                /* put a text into the queue*/
                queues.get(queueName).add(req.text());
                return new Resp(null, 200);
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
        return new Resp(null, 400);
    }

    private ConcurrentLinkedQueue<String> emptyQueue() {
        return new ConcurrentLinkedQueue<>();
    }
}
