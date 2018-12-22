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
public class SupplierPayments {
    private String spid;
    private String soid;
    private double amount;
    private Date payDate;

    public SupplierPayments(String spid, String soid, double amount, Date payDate) {
        this.spid = spid;
        this.soid = soid;
        this.amount = amount;
        this.payDate = payDate;
    }

    public String getSpid() {
        return spid;
    }

    public String getSoid() {
        return soid;
    }

    public double getAmount() {
        return amount;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public void setSoid(String soid) {
        this.soid = soid;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

  
}
