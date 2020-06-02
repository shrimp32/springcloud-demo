package xw.cloud.hello.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xw.cloud.hello.model.Tweet;

import java.util.Arrays;
import java.util.List;


/**
 * @author : 夏玮
 * Created on 2019-07-24 15:57
 */
@RestController
public class SlowRestController {
    @GetMapping("/slow")
    private List<Tweet> getAllTweets() throws InterruptedException {
        Thread.sleep(2000); // delay
        return Arrays.asList(
                new Tweet("RestTemplate rules", "@user1"),
                new Tweet("WebClient is better", "@user2"),
                new Tweet("OK, both are useful", "@user1"));
    }
}
