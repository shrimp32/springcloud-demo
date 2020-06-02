package xw.cloud.sample.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xw.cloud.sample.model.Tweet;
import xw.cloud.sample.service.HttpBinService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/noblock")
public class WebClientDemoController {
    private static final Logger log = LoggerFactory.getLogger(WebClientDemoController.class);
    @Value("${spring.application.name}")
    private String app;

	private final WebClient.Builder webClientBuilder;
	private final ReactiveCircuitBreakerFactory circuitBreakerFactory;

    HttpBinService httpBin;

    public WebClientDemoController(@Qualifier(value = "loadBalancedWebClientBuilder") WebClient.Builder webClientBuilder, ReactiveCircuitBreakerFactory circuitBreakerFactory, HttpBinService httpBin) {
        this.webClientBuilder = webClientBuilder;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.httpBin = httpBin;
    }

    @GetMapping("/test")
    public Mono<String> test() {
        //使用DefaultWebClientBuilder类构建客户端
        WebClient client = webClientBuilder.build();
        Mono<String> result = client
                .get()
                .uri("http://service-hello/hello?name=shirley")
                .retrieve()
                .bodyToMono(String.class);
        return result;
    }

    @GetMapping(value = "/tweets",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tweet> getTweetsNonBlocking() {
        log.info("Starting NON-BLOCKING Controller!");
        Flux<Tweet> tweetFlux = webClientBuilder.build()
                .get()
                .uri("http://service-hello/slow")
                .retrieve()
                .bodyToFlux(Tweet.class);

        tweetFlux.subscribe(tweet -> log.info(tweet.toString()));
        log.info("Exiting NON-BLOCKING Controller!");
        return tweetFlux;
    }

    @GetMapping("/slow")
    public Mono<String> slow() {
        return webClientBuilder.build().get().uri("http://"+app+"/slow").retrieve().bodyToMono(String.class).transform(
                it -> circuitBreakerFactory.create("slow").run(it, throwable -> Mono.just("fallback")));
    }

    //-------------https://httpbin.org 测试示例----------------------
    @GetMapping("/get")
    public Mono<Map> get() {
        return httpBin.get();
    }

    @GetMapping("/delay/{seconds}")
    public Mono<Map> delay(@PathVariable int seconds) {
        return circuitBreakerFactory.create("delay").run(httpBin.delay(seconds), t -> {
            log.warn("delay call failed error", t);
            Map<String, String> fallback = new HashMap<>();
            fallback.put("hello", "world");
            return Mono.just(fallback);
        });
    }

    @GetMapping("/fluxdelay/{seconds}")
    public Flux<String> fluxDelay(@PathVariable int seconds) {
        return circuitBreakerFactory.create("delay").run(httpBin.fluxDelay(seconds), t -> {
            log.warn("delay call failed error", t);
            return Flux.just("hello", "world");
        });
    }

}
