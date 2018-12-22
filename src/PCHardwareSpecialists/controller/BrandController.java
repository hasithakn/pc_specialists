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
import PCHardwareSpecialists.model.Brand;

/**
 *
 * @author Hasithakn
 */
public class BrandController {

    public static int addBrand(Brand brand) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Insert into Brand values(?,?)");
        stm.setObject(1, brand.getBid());
        stm.setObject(2, brand.getBrandName());
        return stm.executeUpdate();
    }

    public static Brand searchBrand(String bid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Brand where bid='" + bid + "'";
        ResultSet rst = stm.executeQuery(sql);
        if (rst.next()) {
            Brand brand = new Brand(rst.getString("bid"), rst.getString("brandName"));
            return brand;
        } else {
            return null;
        }
    }
    public static ArrayList<Brand> searchBrandByName(String brandName) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Brand where brandName='" + brandName + "'";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Brand> brandList = new ArrayList<>();
        while (rst.next()) {
            Brand brand = new Brand(rst.getString("bid"), rst.getString("brandName"));
            brandList.add(brand);
        }
        return brandList;
    }

    public static int updateBrand(Brand brand) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Update brand set brandName=? where bid=?");
        stm.setObject(1, brand.getBrandName());
        stm.setObject(2, brand.getBid());
        return stm.executeUpdate();
    }

    public static int deleteBrand(String bid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Delete From Brand where bid=?");
        stm.setObject(1, bid);
        return stm.executeUpdate();
    }

    public static ArrayList<Brand> getAllBrand() throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Brand ";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Brand> brandList = new ArrayList<>();
        while (rst.next()) {
            Brand brand = new Brand(rst.getString("bid"), rst.getString("brandName"));
            brandList.add(brand);
        }
        return brandList;
    }
}
