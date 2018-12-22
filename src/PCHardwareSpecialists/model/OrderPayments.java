/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PCHardwareSpecialists.model;

import java.util.Date;

/**
 *
 * @author Hasithakn
 */
public class OrderPayments {
    private String OPID;
    private String oid;
    private Double amount;
    private Date payDate;

    public void setOPID(String OPID) {
        this.OPID = OPID;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public OrderPayments(String OPID, String oid, Double amount, Date payDate) {
        this.OPID = OPID;
        this.oid = oid;
        this.amount = amount;
        this.payDate = payDate;
    }

    public String getOPID() {
        return OPID;
    }

    public String getOid() {
        return oid;
    }

    public Double getAmount() {
        return amount;
    }

    public Date getPayDate() {
        return payDate;
    }
    
}
