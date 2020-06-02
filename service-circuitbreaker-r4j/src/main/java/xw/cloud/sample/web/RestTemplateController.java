package xw.cloud.sample.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import xw.cloud.sample.model.Tweet;
import xw.cloud.sample.service.HttpbinService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 夏玮
 * Created on 2019-07-24 15:56
 */
@RestController
@RequestMapping("/block")
public class RestTemplateController {
    private static final Logger log = LoggerFactory.getLogger(RestTemplateController.class);

    @Autowired
    HttpbinService service;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CircuitBreakerFactory circuitBreakerFactory;

    @GetMapping("/tweets")
    public List<Tweet> getTweetsBlocking() {
        final String uri = "http://service-hello/slow";

        ResponseEntity<List<Tweet>> response = restTemplate.exchange(
                uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Tweet>>() {
                });
        List<Tweet> result = response.getBody();
        result.forEach(tweet -> log.info(tweet.toString()));

        return result;
    }

    @GetMapping("/slow")
    public String slow() {
        return circuitBreakerFactory.create("slow").run(() -> restTemplate.getForObject("http://service-r4j/slow", String.class), throwable -> "fallback");
    }

    //-------------https://httpbin.org 测试示例----------------------
    @GetMapping("/get")
    public Map get(){
        return service.get();
    }

    @GetMapping("/delay/{seconds}")
    public Map delay(@PathVariable int seconds) {
        return circuitBreakerFactory.create("delay").run(service.delaySuppplier(seconds), t -> {
            log.warn("delay call failed error", t);
            Map<String, String> fallback = new HashMap<>();
            fallback.put("hello", "world");
            return fallback;
        });
    }
}
