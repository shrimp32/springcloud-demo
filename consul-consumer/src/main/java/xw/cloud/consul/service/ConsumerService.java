package xw.cloud.consul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xw.cloud.consul.remote.ProducerRemote;

/**
 * @author : 夏玮
 * Created on 2019-01-28 15:05
 */
@Service
public class ConsumerService {

    @Autowired
    private ProducerRemote producerRemote;

    public String consumer(){

        return producerRemote.producer();
    }


}