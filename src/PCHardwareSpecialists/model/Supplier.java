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
public class Supplier {
    private String supId;
    private String supName;

    public Supplier(String supId, String supName) {
        this.supId = supId;
        this.supName = supName;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getSupId() {
        return supId;
    }

    public String getSupName() {
        return supName;
    }
    
}
