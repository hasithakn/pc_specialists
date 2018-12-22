/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PCHardwareSpecialists.model;

/**
 *
 * @author Hasithakn
 */
public class OrderBrandItemDetails {
    private String batchId;
    private int oQty;
    private double price;
    private String oid;

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public void setoQty(int oQty) {
        this.oQty = oQty;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public OrderBrandItemDetails(String batchId, int oQty, double price, String oid) {
        this.batchId = batchId;
        this.oQty = oQty;
        this.price = price;
        this.oid = oid;
    }

    public String getBatchId() {
        return batchId;
    }

    public int getoQty() {
        return oQty;
    }

    public double getPrice() {
        return price;
    }

    public String getOid() {
        return oid;
    }


    
    
}
