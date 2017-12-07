package com.mengka.springboot.consumer;

import com.mengka.springboot.util.TimeUtil;
import lombok.extern.log4j.Log4j2;
import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * User: huangyy
 * Date: 2016/11/26
 * Time: 14:03
 */
@Log4j2
public abstract class AbstractConsumer extends Thread {

    @Override
    public void run() {
        while(true){
            log.info("-----consumer----- data = {}",TimeUtil.toDate(new Date(),TimeUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
            consumer(TimeUtil.toDate(new Date(),TimeUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
        }
    }

    abstract void consumer(String data);
}
