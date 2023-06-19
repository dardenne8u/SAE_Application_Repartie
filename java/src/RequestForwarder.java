import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class RequestForwarder extends HttpClient implements ForwarderInterface {

    private HttpClient forwarder;

    public RequestForwarder() {
        this.forwarder = HttpClient.newHttpClient();
    }

    @Override
    public Optional<CookieHandler> cookieHandler() {
        return forwarder.cookieHandler();
    }

    @Override
    public Optional<Duration> connectTimeout() {
        return Optional.empty();
    }

    @Override
    public Redirect followRedirects() {
        return forwarder.followRedirects();
    }

    @Override
    public Optional<ProxySelector> proxy() {
        return forwarder.proxy();
    }

    @Override
    public SSLContext sslContext() {
        return forwarder.sslContext();
    }

    @Override
    public SSLParameters sslParameters() {
        return forwarder.sslParameters();
    }

    @Override
    public Optional<Authenticator> authenticator() {
        return forwarder.authenticator();
    }

    @Override
    public Version version() {
        return forwarder.version();
    }

    @Override
    public Optional<Executor> executor() {
        return forwarder.executor();
    }

    @Override
    public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) throws IOException, InterruptedException {
        return forwarder.send(request, responseBodyHandler);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) {
        return forwarder.sendAsync(request, responseBodyHandler);
    }

    @Override
    public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler, HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
        return forwarder.sendAsync(request, responseBodyHandler, pushPromiseHandler);
    }

    @Override
    public String forwardRequest(String url, String method, HttpExchange request) {
        String l = null;
        try {
            l = new JSONObject(this.send(
                    HttpRequest.newBuilder(
                            URI.create("https://www.infoclimat.fr/public-api/gfs/json?_ll=48.6822,6.1862&_auth=ARsDFFIsBCZRfFtsD3lSe1Q8ADUPeVRzBHgFZgtuAH1UMQNgUTNcPlU5VClSfVZkUn8AYVxmVW0Eb1I2WylSLgFgA25SNwRuUT1bPw83UnlUeAB9DzFUcwR4BWMLYwBhVCkDb1EzXCBVOFQoUmNWZlJnAH9cfFVsBGRSPVs1UjEBZwNkUjIEYVE6WyYPIFJjVGUAZg9mVD4EbwVhCzMAMFQzA2JRMlw5VThUKFJiVmtSZQBpXGtVbwRlUjVbKVIuARsDFFIsBCZRfFtsD3lSe1QyAD4PZA%3D%3D&_c=19f3aa7d766b6ba91191c8be71dd1ab2")
                    ).build(),
                    HttpResponse.BodyHandlers.ofString()
            )).toString();
        } catch (Exception ignored) {
        }
        return l;
    }
}
