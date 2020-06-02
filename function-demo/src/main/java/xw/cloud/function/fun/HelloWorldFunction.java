package xw.cloud.function.fun;

import java.util.function.Function;

/**
 * @author : 夏玮
 * Created on 2019/12/4 17:46
 * 实现函数接口
 */
public class HelloWorldFunction implements Function<String,String> {

    @Override
    public String apply(String s) {
        return "Hello "+(s.isEmpty()?"World":s)+",Welcome to Function World!";
    }

}
