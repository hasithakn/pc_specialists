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
public class Brand {
    private String bid;
    private String brandName;

    public Brand(String bid, String brandName) {
        this.bid = bid;
        this.brandName = brandName;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBid() {
        return bid;
    }

    public String getBrandName() {
        return brandName;
    }
    
}
