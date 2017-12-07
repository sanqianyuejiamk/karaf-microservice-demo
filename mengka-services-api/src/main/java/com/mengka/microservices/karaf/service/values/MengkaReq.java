package com.mengka.microservices.karaf.service.values;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author huangyy
 * @date 2017/12/07.
 */
@XmlRootElement
public class MengkaReq {

    private Double creditAmount;//用户额度

    private Double applyAmount;//申请金额

    private Double fee;//手续费

    private Double amount;//用户额度（审批后金额）

    public Double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Double applyAmount) {
        this.applyAmount = applyAmount;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
