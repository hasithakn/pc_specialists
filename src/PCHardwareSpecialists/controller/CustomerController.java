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
import PCHardwareSpecialists.model.Customer;

/**
 *
 * @author Hasithakn
 */
public class CustomerController {

    public static int addCustomer(Customer customer) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Insert into Customer values(?,?,?,?,?,?)");
        stm.setObject(1, customer.getCustId());
        stm.setObject(2, customer.getCustName());
        stm.setObject(3, customer.getNic());
        stm.setObject(4, customer.getAddress());
        stm.setObject(5, customer.getCustEmail());
        stm.setObject(6, customer.getTel());
        return stm.executeUpdate();
    }

    public static Customer searchCustomer(String custId) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Customer where custId='" + custId + "'";
        ResultSet rst = stm.executeQuery(sql);
        if (rst.next()) {
            Customer customer = new Customer(custId, rst.getString("custName"), rst.getString("nic"), rst.getString("address"), rst.getString("custEmail"), rst.getInt("tel"));
            return customer;
        } else {
            return null;
        }
    }

    public static ArrayList<Customer> searchCustomerByNic(String nic) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Customer where nic='" + nic + "'";
        ResultSet rst = stm.executeQuery(sql);

        ArrayList<Customer> customerList = new ArrayList<>();
        while (rst.next()) {
            Customer customer = new Customer(rst.getString("custId"), rst.getString("custName"), rst.getString("nic"), rst.getString("address"), rst.getString("custEmail"), rst.getInt("tel"));
            customerList.add(customer);
        }
        return customerList;
    }

    public static ArrayList<Customer> searchCustomerByName(String custName) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Customer where custName='" + custName + "'";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Customer> customerList = new ArrayList<>();
        while (rst.next()) {
            Customer customer = new Customer(rst.getString("custId"), rst.getString("custName"), rst.getString("nic"), rst.getString("address"), rst.getString("custEmail"), rst.getInt("tel"));
            customerList.add(customer);
        }
        return customerList;

    }

    public static int updateCustomer(Customer customer) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Update Customer set custName=?, nic=?, address=?,custEmail=? , tel =? where custId=?");
        stm.setObject(1, customer.getCustName());
        stm.setObject(2, customer.getNic());
        stm.setObject(3, customer.getAddress());
        stm.setObject(4, customer.getCustEmail());
        stm.setObject(5, customer.getTel());
        stm.setObject(6, customer.getCustId());
        return stm.executeUpdate();
    }

    public static int deleteCustomer(String custId) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Delete From Customer where custId=?");
        stm.setObject(1, custId);
        return stm.executeUpdate();
    }

    public static ArrayList<Customer> getAllCustomer() throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Customer ";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Customer> customerList = new ArrayList<>();
        while (rst.next()) {
            Customer customer = new Customer(rst.getString("custId"), rst.getString("custName"), rst.getString("nic"), rst.getString("address"), rst.getString("custEmail"), rst.getInt("tel"));
            customerList.add(customer);
        }
        return customerList;
    }

}
