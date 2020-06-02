package xw.cloud.consul.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author : 夏玮
 * Created on 2019-01-28 15:01
 * 调用生产者服务
 */
@FeignClient("consul-producer")
//@FeignClient("micro-naut")
public interface ProducerRemote {

    @GetMapping("/producer")
    String producer();
}