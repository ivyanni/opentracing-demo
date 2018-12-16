package ivyanni.tracingdemo.first;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ProbabilisticSampler;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Ilia Vianni
 * Created on 08.12.2018.
 */
@SpringBootApplication
public class Application {
    @Value("${tracingdemo.service_name}")
    private String serviceName;

    @Value("${tracingdemo.sample_param}")
    private Number sampleParam;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    public Tracer tracer() {
        return new Configuration(serviceName)
                .withSampler(new Configuration.SamplerConfiguration()
                        .withType(ProbabilisticSampler.TYPE).withParam(sampleParam))
                .withReporter(new Configuration.ReporterConfiguration())
                .getTracer();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
