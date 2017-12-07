package com.mengka.microservices.karaf.service;

import com.mengka.microservices.karaf.values.MengkaReq;

/**
 * @author huangyy
 * @date 2017/12/07.
 */
public interface MengkaService {

    void calculateRate(MengkaReq mengkaReq);

    String getInstitute();
}
