**job4j_Pooh JMS**
[![Build Status](https://travis-ci.org/amasterenko/job4j_pooh.svg?branch=master)](https://travis-ci.org/amasterenko/job4j_pooh)

The application is an analog of asynchronous queue.

It uses http and has two modes, "queue" and "topic".

Clients can be of two types: senders (publishers) and recipients (subscribers).

_1. Queue mode_

Sender sends a message with the queue's name and a text.

Recipient reads the first message in the queue and deletes it.

All the recipients reads only one unique queue.

Request examples (for curl):

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

Publisher sends a message with the topic name and a text. 

Subscriber reads the first message in the queue and deletes it.

There is a unique queue for each subscriber of the topic.

New topic can be created by a publisher sending POST request with the topic's name.

To subscribe to a topic client sends a GET request with the topic's name.

Request examples (for curl):

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