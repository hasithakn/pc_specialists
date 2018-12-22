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
public class SupplyOrderBrandItemDetails {
    private String soid;
    private String batchId;
   // private Batch batch;
    
    public void setSoid(String soid) {
        this.soid = soid;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getSoid() {
        return soid;
    }

    public String getBatchId() {
        return batchId;
    }

    public SupplyOrderBrandItemDetails(String soid, String batchId) {
        this.soid = soid;
        this.batchId = batchId;
    }

    
    
}
