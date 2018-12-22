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
import PCHardwareSpecialists.model.Item;

/**
 *
 * @author Hasithakn
 */
public class ItemController {

    public static int addItem(Item item) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Insert into Item values(?,?)");
        stm.setObject(1, item.getiCode());
        stm.setObject(2, item.getDescription());
        return stm.executeUpdate();
    }

    public static Item searchItem(String iCode) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Item where iCode='" + iCode + "'";
        ResultSet rst = stm.executeQuery(sql);
        if (rst.next()) {
            Item item = new Item(rst.getString("iCode"), rst.getString("Description"));
            return item;
        } else {
            return null;
        }
    }
    
    public static ArrayList<Item>  searchItemByname(String description) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Item where description='" + description + "'";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Item> itemList = new ArrayList<>();
        while (rst.next()) {
            Item item = new Item(rst.getString("iCode"), rst.getString("Description"));
            itemList.add(item);
        }
        return itemList;
    }

    public static int updateItem(Item item) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Update Item set Description=? where iCode=?");
        stm.setObject(1, item.getDescription());
        stm.setObject(2, item.getiCode());
        return stm.executeUpdate();
    }

    public static int deleteItem(String iCode) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Delete From Item where iCode=?");
        stm.setObject(1, iCode);
        return stm.executeUpdate();
    }

    public static ArrayList<Item> getAllItem() throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Item ";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Item> itemList = new ArrayList<>();
        while (rst.next()) {
            Item item = new Item(rst.getString("iCode"), rst.getString("Description"));
            itemList.add(item);
        }
        return itemList;
    }
}
