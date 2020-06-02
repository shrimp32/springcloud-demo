package xw.cloud.sample.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.function.Supplier;

/**
 * RestTemplate服务调用及熔断示例
 * https://httpbin.org 测试示例
 * 这里用的rest不支持@Loadbalance
 */
@Service
public class HttpbinService {
    private static final Logger log = LoggerFactory.getLogger(HttpbinService.class);
    @Autowired
    private RestTemplate rest;

    public Map get() {
        return rest.getForObject("https://httpbin.org/get", Map.class);

    }

    public Map delay(int seconds) {
        return rest.getForObject("https://httpbin.org/delay/" + seconds, Map.class);
    }

    public Supplier<Map> delaySuppplier(int seconds) {
        return () -> this.delay(seconds);
    }
}
