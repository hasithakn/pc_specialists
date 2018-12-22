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
public class SCheque {
    private String spid;
    private String chequeNo;

    public SCheque(String spid, String chequeNo) {
        this.spid = spid;
        this.chequeNo = chequeNo;
    }

    public String getSpid() {
        return spid;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }
    
}
