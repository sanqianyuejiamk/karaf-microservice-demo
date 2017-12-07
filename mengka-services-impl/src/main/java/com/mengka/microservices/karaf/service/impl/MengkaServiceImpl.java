package com.mengka.microservices.karaf.service.impl;

import com.mengka.microservices.karaf.service.MengkaService;
import com.mengka.microservices.karaf.service.values.MengkaReq;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author huangyy
 * @date 2017/12/07.
 */
@Component(configurationPid = "com.mengka.microservices.karaf.service",
        property = {"institute=default", "fee=500"},
        configurationPolicy = ConfigurationPolicy.REQUIRE,
        immediate = true)
public class MengkaServiceImpl implements MengkaService {

    private static final Logger LOG = LoggerFactory.getLogger(MengkaServiceImpl.class);

    private String institute;

    private double fee = 500.0;

    @Activate
    void activate(Map<String, ?> properties) {
        LOG.info("Activating " + getClass().getName());
        String institute = (String) properties.get("institute");
        if (StringUtils.isNoneBlank(institute)) {
            this.institute = institute;
        }
        String fee = (String) properties.get("fee");
        if (fee != null) {
            this.fee = Double.valueOf(fee);
        }
    }

    @Deactivate
    void close() {
        LOG.info("Deactivating " + getClass().getName());
    }

    public void calculateRate(MengkaReq mengkaReq) {
        Validate.notNull(mengkaReq.getCreditAmount(), "creditAmount needs to be set to calculate the rate");
        Validate.notNull(mengkaReq.getApplyAmount(), "applyAmount needs to be set to calculate the rate");

        //手续费
        mengkaReq.setFee(this.fee);

        //申请金额判断
        if (mengkaReq.getApplyAmount() > mengkaReq.getCreditAmount()) {
            LOG.info("审批失败:申请金额超过用户信用额度");
            return;
        }

        //计算用户额度
        Double amount = mengkaReq.getApplyAmount() - fee;
        mengkaReq.setAmount(amount);
        LOG.info("审批结果:{}元", amount);
    }

    public String getInstitute() {
        return institute;
    }
}
