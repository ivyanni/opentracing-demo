package ivyanni.tracingdemo.second.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Ilia Vianni
 * Created on 08.12.2018.
 */
@RestController
public class SecondController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecondController.class);
    private final RabbitTemplate rabbitTemplate;
    private final Exchange exchange;

    @Autowired
    public SecondController(RabbitTemplate rabbitTemplate, Exchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    @RequestMapping("/hello")
    public String sayHello(@RequestHeader HttpHeaders httpHeaders) {
        List<String> traceId = httpHeaders.get("uber-trace-id");
        LOGGER.info("traceId: {}", traceId);
        return "Hello from second service!";
    }

    @RequestMapping("/amqp")
    public String sendAmqpMessage() {
        rabbitTemplate.convertAndSend(exchange.getName(), "hello.world", "Hello from RabbitMQ!");
        LOGGER.info("Message was sent");
        return "Message was sent";
    }
}
