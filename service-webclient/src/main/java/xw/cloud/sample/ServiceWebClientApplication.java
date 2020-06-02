package xw.cloud.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.ReactiveHystrixCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ServiceWebClientApplication {
    @Value("${spring.application.name}")
    private String name;
    @Value("${server.port}")
    private String port;

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @LoadBalanced //通过服务名调用，支持负载
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 响应式的断路器工厂Hystrix
     * @return
     */
    @Bean
    public Customizer<ReactiveHystrixCircuitBreakerFactory> reactiveConfig() {
        return factory -> factory.configureDefault(id -> {
            return HystrixObservableCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(id))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withExecutionTimeoutInMilliseconds(3000));

        });
    }

    /**
     * 非响应式的断路器工厂Hystrix
     * @return
     */
//    @Bean
//    public Customizer<HystrixCircuitBreakerFactory> defaultConfig() {
//        return factory -> factory.configureDefault(id -> HystrixCommand.Setter
//                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(id))
//                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(3000)));
//    }

    /**
     * 非响应式的断路器工厂Resilience4J
     * @return
     */
//    @Bean
//    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
//        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
//                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(3)).build())
//                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
//                .build());
//    }

    /**
     * 响应式的断路器工厂Resilience4J
     * @return
     */
//    @Bean
//    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
//        CircuitBreakerRegistry cbr = CircuitBreakerRegistry.ofDefaults();
//        return factory -> {
//            factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
//                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
//                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(3)).build())
//                    .circuitBreakerConfig(CircuitBreakerConfig.custom().failureRateThreshold(10)
//                            .slowCallRateThreshold(5).slowCallRateThreshold(2).build()).build());
//        };
//    }

    /**
     * 解决jason转换报错的问题
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(ServiceWebClientApplication.class, args);
    }

    @GetMapping("/")
    public String index(){
        return "Hello,"+ name +":"+port+" !";
    }

}

