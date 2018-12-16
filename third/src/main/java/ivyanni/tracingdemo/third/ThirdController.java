package ivyanni.tracingdemo.third;

import io.opentracing.Scope;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapExtractAdapter;
import io.opentracing.tag.Tags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Ilia Vianni
 * Created on 13.12.2018.
 */
@RestController
public class ThirdController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdController.class);
    private Tracer tracer;

    @Autowired
    public ThirdController(Tracer tracer) {
        this.tracer = tracer;
    }

    @RequestMapping("/test")
    public String test(@RequestHeader HttpHeaders httpHeaders, HttpServletRequest request) {
        try (Scope scope = startServerSpan(tracer, httpHeaders, "test")) {
            Tags.HTTP_METHOD.set(scope.span(), request.getMethod());
            Tags.HTTP_URL.set(scope.span(), request.getRequestURL().toString());
            List<String> traceId = httpHeaders.get("uber-trace-id");
            LOGGER.info("traceId: {}", traceId);
            return "Hello from third service!";
        }
    }

    private static Scope startServerSpan(Tracer tracer, HttpHeaders httpHeaders, String operationName) {
        SpanContext parentSpan = tracer.extract(Format.Builtin.HTTP_HEADERS,
                new TextMapExtractAdapter(httpHeaders.toSingleValueMap()));
        Tracer.SpanBuilder spanBuilder;
        if (parentSpan == null) {
            spanBuilder = tracer.buildSpan(operationName);
        } else {
            spanBuilder = tracer.buildSpan(operationName).asChildOf(parentSpan);
        }
        return spanBuilder.withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER).startActive(true);
    }
}
