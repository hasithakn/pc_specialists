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
public class Customer {
   private String custId;
   private String custName;
   private String nic;
   private String address;
   private String custEmail;
   private int tel;

    @Override
    public String toString() {
        return  custName ;
    }

    public Customer(String custId, String custName, String nic, String address, String custEmail, int tel) {
        this.custId = custId;
        this.custName = custName;
        this.nic = nic;
        this.address = address;
        this.custEmail = custEmail;
        this.tel = tel;
    }
   
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public String getCustId() {
        return custId;
    }

    public String getCustName() {
        return custName;
    }

    public String getNic() {
        return nic;
    }

    public String getAddress() {
        return address;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public int getTel() {
        return tel;
    }
   
   
   
}
