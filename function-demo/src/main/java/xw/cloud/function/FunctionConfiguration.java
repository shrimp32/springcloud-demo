package xw.cloud.function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import xw.cloud.function.fun.HelloWorldFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author : 夏玮
 * Created on 2019/12/5 16:09
 * 通过配置类装配function
 */
@Configuration
public class FunctionConfiguration {

    @Bean
    public Function<String, String> uppercase() {
        return value -> value.toUpperCase();
    }

    @Bean
    public Function<Flux<String>, Flux<String>> lowercase() {
        return flux -> flux.map(value -> value.toLowerCase());
    }

    @Bean
    public Supplier<String> hello() {
        return () -> "hello";
    }

    @Bean
    public HelloWorldFunction say(){
        return new HelloWorldFunction();
    }

//    @Bean
//    public Function<Flux<String>, Flux<String>> uppercase() {
//        return flux -> flux.map(value -> value.toUpperCase());
//    }

    @Bean
    public Function<MultiValueMap<String, String>, Map<String, Integer>> sum() {
        return multiValueMap -> {
            Map<String, Integer> result = new HashMap<>();
            multiValueMap.forEach((s, strings) -> result.put(s,
                    strings.stream().mapToInt(Integer::parseInt).sum()));
            return result;
        };
    }



}
