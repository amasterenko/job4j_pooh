## Pooh JMS  
[![Build Status](https://travis-ci.org/amasterenko/job4j_pooh.svg?branch=master)](https://travis-ci.org/amasterenko/job4j_pooh)
  
The application provides an example of an asynchronous message queue.  

### Technologies  
* Java Core

### Description  
The app has _queue_ and _topic_ modes and uses http-like messaging protocol.      
Clients can be either publishers or subscribers.   

_Queue mode:_  

Publisher sends a message with the queue's name and a text.  
The service has a single queue for all the recipients.    
Subscriber reads the first message in the queue and deletes it.  

_Topic mode:_  

Publisher sends a message with the topic name and a text.  
The service has unique queue for each subscriber of the topic (up to 10 queues by default).  
Subscriber reads the first message in their queue and deletes the message.

### Install  

Build the project with Maven: ```mvn clean package```.    
Run the app: ```java -jar target/job4j_pooh.jar```.  
Server default URL: http://localhost:9000    

### Usage  
Queue-mode POST:  
```
curl -i -X POST -d "temperature=18" http://localhost:9000/queue/weather       
```
_queue_ - mode,  
_weather_ - unique name of the queue,  
_"temperature=18"_ - text message.  

Queue-mode GET:  
```
curl -i http://localhost:9000/queue/weather       
```
_queue_ - mode,  
_weather_ - unique name of the queue.  

Response:
```
HTTP/1.1 200 OK
temperature=18
```
Topic-mode POST:
```
curl -i -X POST -d "temperature=23" http://localhost:9000/topic/weather       
```
_topic_ - mode,  
_weather_ - unique name of the topic,  
"temperature=18" - text message.  

Topic-mode GET:
```
curl -i http://localhost:9000/topic/weather/3       
```
_topic_ - mode,  
_weather_ - unique name of the topic,  
_3_ - subscriber's ID.    

Response:
```
HTTP/1.1 200 OK  
temperature=23  
```

