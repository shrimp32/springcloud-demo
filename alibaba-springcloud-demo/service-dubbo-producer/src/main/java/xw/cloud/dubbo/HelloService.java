package xw.cloud.dubbo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author : 夏玮
 * Created on 2019-05-20 17:42
 */
@Service
public class HelloService {
    @Value("${dubbo.application.name}")
    private String serviceName;

//    @Override
    public String sayHello(String name) {
        return String.format("[%s] : Hello, %s", serviceName, name);
    }
}
