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
public class SypplyOrderQty {
    private String batchId;
    private int qty;

    public SypplyOrderQty(String batchId, int qty) {
        this.batchId = batchId;
        this.qty = qty;
    }

    public String getBatchId() {
        return batchId;
    }

    public int getQty() {
        return qty;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
    
}
