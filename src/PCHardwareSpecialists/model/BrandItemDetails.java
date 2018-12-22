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
public class BrandItemDetails {
    private String biid;
    private String wid;
    private String bid;
    private String iCode;
    private Double sellingPrice;
    private Item item;
    private Brand brand;
    private Warrenty warrenty; 

    public BrandItemDetails(String biid, String wid, String bid, String iCode, Double sellingPrice) {
        this.biid = biid;
        this.wid = wid;
        this.bid = bid;
        this.iCode = iCode;
        this.sellingPrice = sellingPrice;
    }

    public BrandItemDetails(String biid, String wid, String bid, String iCode, Double sellingPrice, Item item, Brand brand, Warrenty warrenty) {
        this.biid = biid;
        this.wid = wid;
        this.bid = bid;
        this.iCode = iCode;
        this.sellingPrice = sellingPrice;
        this.item = item;
        this.brand = brand;
        this.warrenty = warrenty;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

   
    

    public Warrenty getWarrenty() {
        return warrenty;
    }

    public void setWarrenty(Warrenty warrenty) {
        this.warrenty = warrenty;
    }

    public Item getItem() {
        return item;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }


    public void setBiid(String biid) {
        this.biid = biid;
    }


    public void setWid(String wid) {
        this.wid = wid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public void setiCode(String iCode) {
        this.iCode = iCode;
    }

  
    public String getBiid() {
        return biid;
    }

 

    public String getWid() {
        return wid;
    }

    public String getBid() {
        return bid;
    }

    public String getiCode() {
        return iCode;
    }
   
}
