**job4j_Pooh JMS**
[![Build Status](https://travis-ci.org/amasterenko/job4j_pooh.svg?branch=master)](https://travis-ci.org/amasterenko/job4j_pooh)

The application provides an example of an asynchronous message queue.

It uses a http-like protocol to receive and send messages and has two modes: "queue" and "topic".

Clients can be senders (publishers) or recipients (subscribers).

_1. Queue mode_

The Sender sends a message with the queue's name and a text.

The Recipient reads the first message in the queue and deletes it.

All the recipients read only one unique queue.

Examples (curl):

POST /queue/weather -d "temperature=18"

queue - mode,

weather - unique name of the queue,

"temperature=18" - text.

GET /queue/weather

queue - mode,

weather - unique name of the queue,

response:

HTTP/1.1 200 OK

temperature=18

_2. Topic mode_

The Publisher sends a message with the topic name and a text. 

The Subscriber reads the first message in the queue and deletes it.

There is a unique queue for each subscriber of the topic.

A new topic can be created by a publisher sending a POST request with the topic's name.

To subscribe to a topic client sends a GET request with the topic's name.

Examples (curl):

POST /topic/weather -d "temperature=18"

topic - mode,

weather - unique name of the topic,

"temperature=18" - text.

GET /topic/weather/1

topic - mode,

weather - unique name of the topic,

1 - subscriber's ID.

response:

HTTP/1.1 200 OK

temperature=18