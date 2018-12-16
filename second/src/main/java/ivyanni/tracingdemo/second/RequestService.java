package ivyanni.tracingdemo.second;

import io.opentracing.Scope;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Ilia Vianni
 * Created on 13.12.2018.
 */
@Service
public class RequestService {
    private final Tracer tracer;
    private final OkHttpClient client;

    @Value("${tracingdemo.third_service.host}")
    private String targetHost;

    @Value("${tracingdemo.third_service.port}")
    private Integer targetPort;

    @Autowired
    public RequestService(Tracer tracer) {
        this.tracer = tracer;
        this.client = new OkHttpClient();
    }

    public String sendTracedHttpRequest() {
        try {
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(targetHost)
                    .port(targetPort)
                    .addPathSegment("test")
                    .build();
            Request.Builder requestBuilder = new Request.Builder().url(url);
            try (Scope scope = tracer.buildSpan("sendTracedHttpRequest").startActive(true)) {
                Tags.SPAN_KIND.set(scope.span(), Tags.SPAN_KIND_CLIENT);
                Tags.HTTP_METHOD.set(scope.span(), "GET");
                Tags.HTTP_URL.set(scope.span(), url.toString());
                tracer.inject(scope.span().context(), Format.Builtin.HTTP_HEADERS,
                        new RequestBuilderCarrier(requestBuilder));
                Request request = requestBuilder.build();
                Response response = client.newCall(request).execute();
                Tags.HTTP_STATUS.set(scope.span(), response.code());
                return response.body() != null ? response.body().string() : null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
