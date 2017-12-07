package com.mengka.springboot.consumer;

import com.mengka.springboot.manager.MengkaManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA
 * User: huangyy
 * Date: 2016/11/26
 * Time: 14:08
 */
@Log4j2
@Service
public class ETLKafkaConsumer extends AbstractConsumer implements InitializingBean {

    @Value("${kafka.etl.zookeeperconnect}")
    private String zookeeperconnect;

    @Value("${kafka.etl.topic}")
    private String topic;

    @Value("${kafka.etl.consumegroupid}")
    private String consumergrp;

    @Autowired
    private MengkaManager mengkaManager;

    void consumer(String data) {
        log.error("ETLKafkaConsumer data = {}",data);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void afterPropertiesSet() throws Exception {
        log.info("ETLKafkaConsumer start.. broker = "+mengkaManager.getBroker());
        log.info("kafka zookeeperconnect = "+zookeeperconnect+" , topic = "+topic+" , consumergrp = "+consumergrp);
        this.start();
    }
}
