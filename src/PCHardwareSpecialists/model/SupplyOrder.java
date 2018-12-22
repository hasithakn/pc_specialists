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
public class SupplyOrder {
    private String soid;
    private String supId;
    private Date supDate;
    private Batch[] batch;

    public SupplyOrder(String soid, String supId, Date supDate, Batch[] batch) {
        this.soid = soid;
        this.supId = supId;
        this.supDate = supDate;
        this.batch = batch;
    }

    public SupplyOrder(String soid, String supId, Date supDate) {
        this.soid = soid;
        this.supId = supId;
        this.supDate = supDate;
    }

    public Batch[] getBatch() {
        return batch;
    }

    public void setBatch(Batch[] batch) {
        this.batch = batch;
    }

   
    public void setSupDate(Date supDate) {
        this.supDate = supDate;
    }

    public Date getSupDate() {
        return supDate;
    }

    public String getSoid() {
        return soid;
    }

    public void setSoid(String soid) {
        this.soid = soid;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

   

    public String getSupId() {
        return supId;
    }


   
    
}
