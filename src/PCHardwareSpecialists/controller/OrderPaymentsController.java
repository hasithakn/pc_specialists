/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import PCHardwareSpecialists.model.OrderPayments;
import PCHardwareSpecialists.model.Orders;
import PCHardwareSpecialists.model.OCheque;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Hasithakn
 */
public class OrderPaymentsController {

    public static int addOrderPayment(OrderPayments op) throws SQLException, ClassNotFoundException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String a = sdf.format(op.getPayDate());
        String sql = "insert into OrderPayments values('" + op.getOPID() + "','" + op.getOid() + "','" + op.getAmount() + "','" + a + "')";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        return stm.executeUpdate(sql);
    }

    public static int addOrderPaymentCheque(OrderPayments op, OCheque oc) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String a = sdf.format(op.getPayDate());
            String sql = "insert into OrderPayments values('" + op.getOPID() + "','" + op.getOid() + "','" + op.getAmount() + "','" + a + "')";
            conn = DBConnection.getInstance().getConnection();
            Statement stm = conn.createStatement();
            conn.setAutoCommit(false);
            int ab = stm.executeUpdate(sql);
            int b = OChequeController.addChequePaymentFromAddingPayment(oc);
            if (ab > 0 && b > 0) {
                conn.commit();
                return 1;
            } else {
                conn.rollback();
                return -1;

            }
        } finally {
            conn.setAutoCommit(true);
        }

    }

    public static int deleteOrderPayment(String opid) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Delete From OrderPayments where opid=?");
        stm.setObject(1, opid);
        return stm.executeUpdate();
    }

    public static OrderPayments searchOrderPayment(String opid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From OrderPayments where opid='" + opid + "'";
        ResultSet rst = stm.executeQuery(sql);
        if (rst.next()) {
            OrderPayments orders = new OrderPayments(rst.getString("OPID"), rst.getString("oid"), rst.getDouble("amount"), rst.getDate("payDate"));
            return orders;
        } else {
            return null;
        }
    }

    public static ArrayList<OrderPayments> searchPaymentByOid(String oid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From OrderPayments where oid='" + oid + "'";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<OrderPayments> orderList = new ArrayList<>();
        while (rst.next()) {
            OrderPayments orders = new OrderPayments(rst.getString("OPID"), rst.getString("oid"), rst.getDouble("amount"), rst.getDate("payDate"));
            orderList.add(orders);
        }
        return orderList;
    }

    public static ArrayList<OrderPayments> getAllOrderPayments() throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From OrderPayments";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<OrderPayments> orderList = new ArrayList<>();
        while (rst.next()) {
            OrderPayments orders = new OrderPayments(rst.getString("OPID"), rst.getString("oid"), rst.getDouble("amount"), rst.getDate("payDate"));
            orderList.add(orders);
        }
        return orderList;
    }
}
