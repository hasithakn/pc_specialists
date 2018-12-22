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
public class Warrenty {

    private String wid;
    private String wPeriod;

    public Warrenty(String wid, String wPeriod) {
        this.wid = wid;
        this.wPeriod = wPeriod;

    }

    public String getWid() {
        return wid;
    }

    public String getwPeriod() {
        return wPeriod;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public void setwPeriod(String wPeriod) {
        this.wPeriod = wPeriod;
    }

}
