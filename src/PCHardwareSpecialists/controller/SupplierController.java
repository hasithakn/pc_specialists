/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import PCHardwareSpecialists.model.Supplier;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Hasithakn
 */
public class SupplierController {

    public static int addSupplier(Supplier supplier) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Insert into Supplier values(?,?)");
        stm.setObject(1, supplier.getSupId());
        stm.setObject(2, supplier.getSupName());
        return stm.executeUpdate();
    }

    public static Supplier searchSupplier(String supId) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Supplier where supId='" + supId + "'";
        ResultSet rst = stm.executeQuery(sql);
        if (rst.next()) {
            Supplier supplier = new Supplier(supId, rst.getString("supName"));
            return supplier;
        } else {
            return null;
        }
    }

    public static ArrayList<Supplier> searchSupplierByName(String supName) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Supplier where supName='" + supName + "'";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Supplier> supplierList = new ArrayList<>();
        while (rst.next()) {
            Supplier supplier = new Supplier(rst.getString("supId"), rst.getString("supName"));
            supplierList.add(supplier);
        }
        return supplierList;

    }

    public static int updateSupplier(Supplier supplier) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Update Supplier set supName=? where supId=?");
        stm.setObject(1, supplier.getSupName());
        stm.setObject(2, supplier.getSupId());
        return stm.executeUpdate();
    }

    public static int deleteSupplier(String supId) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Delete From Supplier where supId=?");
        stm.setObject(1, supId);
        return stm.executeUpdate();
    }

    public static ArrayList<Supplier> getAllSupplier() throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Supplier ";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Supplier> supplierList = new ArrayList<>();
        while (rst.next()) {
            Supplier supplier = new Supplier(rst.getString("supId"), rst.getString("supName"));
            supplierList.add(supplier);
        }
        return supplierList;
    }

}
