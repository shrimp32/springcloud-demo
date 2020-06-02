package xw.cloud.hello.model;

import java.io.Serializable;

/**
 * @author : 夏玮
 * Created on 2019-07-24 16:03
 */
public class Tweet implements Serializable {

    private static final long serialVersionUID = -8680685965085517021L;
    private String msg;
    private String to;

    @Override
    public String toString() {
        return "Tweet{" +
                "msg='" + msg + '\'' +
                ", to='" + to + '\'' +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Tweet(String msg, String to) {
        this.msg = msg;
        this.to = to;
    }

    public Tweet(){

    }
}
