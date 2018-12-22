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
public class Batch {
    private String batchId;
    private String biid;
    private int qty;
    private Double unitPrice;

    public Batch(String batchId, String biid, int qty, Double unitPrice) {
        this.batchId = batchId;
        this.biid = biid;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public String getBatchId() {
        return batchId;
    }

    public String getBiid() {
        return biid;
    }

    public int getQty() {
        return qty;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public void setBiid(String biid) {
        this.biid = biid;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
    
}
