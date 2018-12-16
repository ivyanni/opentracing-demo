package ivyanni.tracingdemo.first;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Ilia Vianni
 * Created on 08.12.2018.
 */
@RestController
public class FirstController {
    private final RestTemplate restTemplate;

    @Value("${tracingdemo.second_service.host}")
    private String targetHost;

    @Value("${tracingdemo.second_service.port}")
    private String targetPort;

    @Autowired
    public FirstController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/amqp")
    public String sendAmqpMessageRequest() throws URISyntaxException {
        ResponseEntity<String> response = restTemplate
                .getForEntity(new URI("http://" + targetHost + ":" + targetPort + "/amqp"), String.class);
        return "Response: " + response.getBody();
    }

    @RequestMapping("/hello")
    public String sayHello() throws URISyntaxException {
        ResponseEntity<String> response = restTemplate
            .getForEntity(new URI("http://" + targetHost + ":" + targetPort + "/hello"), String.class);
        return "Response: " + response.getBody();
    }
}
