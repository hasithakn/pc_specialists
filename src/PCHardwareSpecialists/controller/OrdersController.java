/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import PCHardwareSpecialists.model.BrandItemDetails;
import db.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import PCHardwareSpecialists.model.Orders;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Hasithakn
 */
public class OrdersController {

    public static int addOrder(Orders order) throws SQLException, ClassNotFoundException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String a = sdf.format(order.getoDate());
        String sql = "insert into Orders values('" + order.getOid() + "','" + order.getCustId() + "','" + a + "','" + order.getDiscount() + "')";

        Connection conn = DBConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);
            Statement stm = conn.createStatement();

            int res1 = stm.executeUpdate(sql);

            if (res1 > 0) {

                int res2 = OrderBrandItemDetailsController.addOrderDetails(order.getOrderBrandItemDetails());

                if (res2 > 0) {
                    conn.commit();
                    return 1;
                } else {
                    conn.rollback();
                    return -1;
                }
            } else {

                conn.rollback();
                return -1;
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }

    }

    public static int addOrderForAutoCommitOffAtFinally(Orders order) throws SQLException, ClassNotFoundException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String a = sdf.format(order.getoDate());
        String sql = "insert into Orders values('" + order.getOid() + "','" + order.getCustId() + "','" + a + "','" + order.getDiscount() + "')";

        Connection conn = DBConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);
            Statement stm = conn.createStatement();

            int res1 = stm.executeUpdate(sql);

            if (res1 > 0) {

                int res2 = OrderBrandItemDetailsController.addOrderDetails(order.getOrderBrandItemDetails());

                if (res2 > 0) {
                    return 1;
                } else {
                    conn.rollback();
                    return -1;
                }
            } else {

                conn.rollback();
                return -1;
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }

    }

    public static Orders searchOrder(String oid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Orders where oid='" + oid + "'";
        ResultSet rst = stm.executeQuery(sql);
        if (rst.next()) {
            Orders orders = new Orders(rst.getString("oid"), rst.getString("custId"), rst.getDate("oDate"), rst.getDouble("discount"));
            return orders;
        } else {
            return null;
        }
    }

    public static ArrayList<Orders> searchOrderByCustId(String custId) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Orders where custId='" + custId + "'";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Orders> orderList = new ArrayList<>();
        while (rst.next()) {
            Orders orders = new Orders(rst.getString("oid"), rst.getString("custId"), rst.getDate("oDate"), rst.getDouble("discount"));
            orderList.add(orders);
        }
        return orderList;
    }

    public static int updateOrders(Orders orders) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Update Orders set oDate=?,discount=? where oid=?");
        stm.setObject(1, orders.getoDate());
        stm.setObject(2, orders.getDiscount());
        stm.setObject(3, orders.getOid());
        return stm.executeUpdate();
    }

    public static int deleteOrders(String oid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Delete From orders where oid=?");
        stm.setObject(1, oid);
        return stm.executeUpdate();
    }

    public static int deleteOrderFully(String oid) throws SQLException, ClassNotFoundException {
        int a = 1;
        Connection conn = DBConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);
            ResultSet rst = null;
            String sql2 = "Select o.oid,OQty,batchId from orderbranditemdetails obid, orders o where obid.oid=o.oid and o.oid='" + oid + "'";
            rst = OtherSql.otherSql(sql2);
            while (rst.next()) {
                String batchId = rst.getString("batchId");
                String oQty = rst.getString("oQty");
                int res2 = BatchController.updateQtyInBatchWhenDeleteOrder(batchId, oQty);
                if (res2 > 0) {
                } else {
                    conn.rollback();
                    a = -1;
                    return -1;
                }
            }
            Statement stm = conn.createStatement();
            String sql = "Delete From orders where oid='" + oid + "'";
            int res1 = stm.executeUpdate(sql);
            if (res1 > 0) {
                conn.commit();
                return 1;
            } else {
                conn.rollback();
                a = -1;
                return -1;
            }
        } catch (SQLException e) {
            conn.rollback();
            a = -1;
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public static int deleteOrderFullyAndAdd(String oid, Orders order) throws SQLException, ClassNotFoundException {

        int a = 1;
        Connection conn = DBConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);
            ResultSet rst = null;
            String sql2 = "Select o.oid,OQty,batchId from orderbranditemdetails obid, orders o where obid.oid=o.oid and o.oid='" + oid + "'";

            rst = OtherSql.otherSql(sql2);

            while (rst.next()) {
                String batchId = rst.getString("batchId");
                String oQty = rst.getString("oQty");
                int res2 = BatchController.updateQtyInBatchWhenDeleteOrder(batchId, oQty);
                if (res2 > 0) {
                } else {
                    conn.rollback();
                    a = -1;
                    return -1;
                }
            }
            Statement stm = conn.createStatement();
            String sql = "Delete From orders where oid='" + oid + "'";
            int res1 = stm.executeUpdate(sql);

            if (res1 > 0) {
                int res2 = addOrderForAutoCommitOffAtFinally(order);

                if (res2 > 0) {
                    conn.commit();
                    return 1;
                } else {
                    conn.rollback();
                    a = -1;
                    return -1;
                }

            } else {
                conn.rollback();
                a = -1;
                return -1;
            }
        } catch (SQLException e) {
            conn.rollback();
            a = -1;
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public static ArrayList<Orders> getAllOrders() throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From Orders";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Orders> orderList = new ArrayList<>();
        while (rst.next()) {
            Orders orders = new Orders(rst.getString("oid"), rst.getString("custId"), rst.getDate("oDate"), rst.getDouble("discount"));
            orderList.add(orders);
        }
        return orderList;
    }
}
