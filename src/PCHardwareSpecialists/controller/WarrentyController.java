/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import PCHardwareSpecialists.model.Warrenty;

/**
 *
 * @author Hasithakn
 */
public class WarrentyController {

    public static int addWarrenty(Warrenty warrenty) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Insert into Warrenty values(?,?)");
        stm.setObject(1, warrenty.getWid());
        stm.setObject(2, warrenty.getwPeriod());
        return stm.executeUpdate();
    }

    public static Warrenty searchWarrenty(String wid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Warrenty where wid='" + wid + "'";
        ResultSet rst = stm.executeQuery(sql);
        if (rst.next()) {
            Warrenty warrenty = new Warrenty(rst.getString("wid"), rst.getString("wPeriod"));
            return warrenty;
        } else {
            return null;
        }
    }

    public static ArrayList<Warrenty> searchWarrentyByName(String wPeriod) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Warrenty where wPeriod='" + wPeriod + "'";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Warrenty> warrentyList = new ArrayList<>();
        while (rst.next()) {
            Warrenty warrenty = new Warrenty(rst.getString("wid"), rst.getString("wPeriod"));
            warrentyList.add(warrenty);
        }
        return warrentyList;
    }

    public static int updateWarrenty(Warrenty warrenty) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Update Warrenty set wPeriod=? where wid=?");
        stm.setObject(1, warrenty.getwPeriod());
        stm.setObject(3, warrenty.getWid());
        return stm.executeUpdate();
    }

    public static int deleteWarrenty(String wid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Delete From Warrenty where wid=?");
        stm.setObject(1, wid);
        return stm.executeUpdate();
    }

    public static ArrayList<Warrenty> getAllWarrenty() throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Warrenty ";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Warrenty> warrentyList = new ArrayList<>();
        while (rst.next()) {
            Warrenty warrenty = new Warrenty(rst.getString("wid"), rst.getString("wPeriod"));
            warrentyList.add(warrenty);
        }
        return warrentyList;
    }
}
