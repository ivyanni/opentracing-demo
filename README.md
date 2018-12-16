# opentracing-demo
Demo project that shows how to enable tracing in Spring application using OpenTracing and Jaeger Tracer

![Jaeger UI](https://i.imgur.com/3ZffS4v.png "JaegerUI")

## Prerequisites
* Local Jaeger server with running Collector
* Local RabbitMQ server available on default port

## Application description  
All services are simple Spring Boot applications
* __First service__ receives HTTP requests from user and just sends new HTTP requests to 2nd service. It uses RestController and RestTemplate from Spring. Tracing is implemented with [OpenTracing Spring Web Instrumentation](https://github.com/opentracing-contrib/java-spring-web) and [Java Jaeger Client](https://github.com/jaegertracing/jaeger-client-java).
* __Second service__ receives HTTP request from 1st service and then sends a message to RabbitMQ queue. It uses RabbitTemplate and RabbitListener to send and receive AMQP messages. Tracing is implemented with [OpenTracing Spring RabbitMQ Instrumentation](https://github.com/opentracing-contrib/java-spring-rabbitmq).   
After receiving message from RabbitMQ it sends HTTP request to 3rd service using OkHttp client. Tracing is implemented without special instrumentation (manually creating new span, setting tags and injecting span to request headers).
* __Third service__ just receives HTTP request from 2rd service and trace it without instrumentation (manual span extraction from request header). 

## How to run
1. Start local RabbitMQ server  
2. Start Jaeger  
3. Start all Spring Boot applications  
4. Send GET request to http://localhost:8081/amqp  
5. See the trace on Jaeger UI (http://localhost:16686/)  
