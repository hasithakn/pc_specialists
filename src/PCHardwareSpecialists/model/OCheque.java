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
public class OCheque {
     private String opid;
    private String chequeNo;

    public OCheque(String opid, String chequeNo) {
        this.opid = opid;
        this.chequeNo = chequeNo;
    }

    public String getOpid() {
        return opid;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setOpid(String opid) {
        this.opid = opid;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }
    
}
