package ivyanni.tracingdemo.second.rabbit;

import ivyanni.tracingdemo.second.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ilia Vianni
 * Created on 08.12.2018.
 */
@Component
public class RabbitReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitReceiver.class);
    private final RequestService service;

    @Autowired
    public RabbitReceiver(RequestService service) {
        this.service = service;
    }

    @RabbitListener(queues="#{queue.name}")
    public void receive(String message) {
        LOGGER.info("Received message '{}'", message);
        String response = service.sendTracedHttpRequest();
        LOGGER.info("Response: '{}'", response);
    }
}
