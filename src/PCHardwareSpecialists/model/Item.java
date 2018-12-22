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
public class Item {
    private String iCode;
    private String description;

    public Item(String iCode, String description) {
        this.iCode = iCode;
        this.description = description;
    }

    public String getiCode() {
        return iCode;
    }

    public String getDescription() {
        return description;
    }

    public void setiCode(String iCode) {
        this.iCode = iCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
