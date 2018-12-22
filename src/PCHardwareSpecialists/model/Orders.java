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
public class Orders {
    private String oid;
    private String custId;
    private Date oDate;
    private double discount;
    private OrderBrandItemDetails[] orderBrandItemDetails;

    public OrderBrandItemDetails[] getOrderBrandItemDetails() {
        return orderBrandItemDetails;
    }

    public void setOrderBrandItemDetails(OrderBrandItemDetails[] orderBrandItemDetails) {
        this.orderBrandItemDetails = orderBrandItemDetails;
    }

    public Orders(String oid, String custId, Date oDate, double discount, OrderBrandItemDetails[] orderBrandItemDetails) {
        this.oid = oid;
        this.custId = custId;
        this.oDate = oDate;
        this.discount = discount;
        this.orderBrandItemDetails = orderBrandItemDetails;
    }

    public Orders(String oid, String custId, Date oDate, double discount) {
        this.oid = oid;
        this.custId = custId;
        this.oDate = oDate;
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
    

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public void setoDate(Date oDate) {
        this.oDate = oDate;
    }

    public String getOid() {
        return oid;
    }

    public String getCustId() {
        return custId;
    }

    public Date getoDate() {
        return oDate;
    }
    
}
