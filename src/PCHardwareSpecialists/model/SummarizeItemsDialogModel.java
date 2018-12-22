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
public class SummarizeItemsDialogModel {

    private String biid;
    private int oQty;
    private double price;
    private String batchId;

    public SummarizeItemsDialogModel(int oQty, double price, String batchId) {
        this.oQty = oQty;
        this.price = price;
        this.batchId = batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchId() {
        return batchId;
    }

    public SummarizeItemsDialogModel(String biid, int oQty, double price, String batchId) {
        this.biid = biid;
        this.oQty = oQty;
        this.price = price;
        this.batchId = batchId;
    }

    public SummarizeItemsDialogModel(String biid, int oQty, double price) {
        this.biid = biid;
        this.oQty = oQty;
        this.price = price;
    }

    public String getBiid() {
        return biid;
    }

    public int getoQty() {
        return oQty;
    }

    public double getPrice() {
        return price;
    }

    public void setBiid(String biid) {
        this.biid = biid;
    }

    public void setoQty(int oQty) {
        this.oQty = oQty;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
