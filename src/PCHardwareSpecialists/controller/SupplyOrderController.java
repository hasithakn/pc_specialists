/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import PCHardwareSpecialists.model.SupplyOrder;
import PCHardwareSpecialists.model.SupplyOrderBrandItemDetails;
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
public class SupplyOrderController {

    public static int addSupplyOrder(SupplyOrder so) throws SQLException, ClassNotFoundException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String a = sdf.format(so.getSupDate());
        String sql = "insert into SupplyOrder values('" + so.getSoid() + "','" + so.getSupId() + "','" + a + "')";
        Connection conn = DBConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);
            Statement stm = conn.createStatement();
            int res1 = stm.executeUpdate(sql);
            if (res1 > 0) {
                int res2 = BatchController.addBatchesWhenAddSupplyOrder(so.getBatch(), so.getSoid());
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

    /* public static int addOrderForAutoCommitOffAtFinally(Orders order) throws SQLException, ClassNotFoundException {
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
     */
    public static SupplyOrder searchSupplyOrder(String soid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From SupplyOrder where soid='" + soid + "'";
        ResultSet rst = stm.executeQuery(sql);
        if (rst.next()) {
            SupplyOrder orders = new SupplyOrder(rst.getString("soid"), rst.getString("supId"), rst.getDate("sODate"));
            return orders;
        } else {
            return null;
        }
    }

    public static ArrayList<SupplyOrder> searchOrderBySupId(String supId) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From SupplyOrder where supId='" + supId + "'";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<SupplyOrder> supplyOrderList = new ArrayList<>();
        while (rst.next()) {
            SupplyOrder supOrders = new SupplyOrder(rst.getString("soid"), rst.getString("supId"), rst.getDate("sODate"));
            supplyOrderList.add(supOrders);
        }
        return supplyOrderList;
    }

    public static int updateSupplyOrders(SupplyOrder supOrders) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Update SupplyOrder set sODate=?,supId=? where soid=?");
        stm.setObject(1, supOrders.getSupDate());
        stm.setObject(2, supOrders.getSupId());
        stm.setObject(3, supOrders.getSoid());
        return stm.executeUpdate();
    }
    /*
     public static int deleteOrders(String oid) throws ClassNotFoundException, SQLException {
     Connection conn = DBConnection.getInstance().getConnection();
     PreparedStatement stm = conn.prepareStatement("Delete From orders where oid=?");
     stm.setObject(1, oid);
     return stm.executeUpdate();
     }
     */

    public static int deleteSupplyOrderFully(String soid) throws SQLException, ClassNotFoundException {
        int a = 1;
        Connection conn = DBConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);
            ResultSet rst = null;
            String sql2 = "Select batchId from supplyorderbranditemdetails where soid='" + soid + "'";
            rst = OtherSql.otherSql(sql2);
            while (rst.next()) {
                String batchId = rst.getString("batchId");
                int res2 = BatchController.zeroQtyInBatchWhenDeleteSupplyOrder(batchId);
                if (res2 > 0) {
                } else {
                    conn.rollback();
                    a = -1;
                    return -1;
                }
            }
            Statement stm = conn.createStatement();
            String sql = "Delete From supplyOrder where soid='" + soid + "'";
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
    /*
     public static int deleteSupplyOrderFullyAndAdd(String oid, Orders order) throws SQLException, ClassNotFoundException {

     int a = 1;
     Connection conn = DBConnection.getInstance().getConnection();
     try {
     conn.setAutoCommit(false);
     ResultSet rst = null;
     String sql2 = "Select o.oid,OQty,biid from orderbranditemdetails obid, orders o where obid.oid=o.oid and o.oid='" + oid + "'";

     rst = OtherSql.otherSql(sql2);

     while (rst.next()) {

     String biid = rst.getString("biid");
     String oQty = rst.getString("oQty");
     int res2 = BrandItemDetailsController.updateItemQtyWhenDeleteOrder(biid, oQty);
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
     */

    public static ArrayList<SupplyOrder> getAllSupplyOrders() throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From SupplyOrder";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<SupplyOrder> orderList = new ArrayList<>();
        while (rst.next()) {
            SupplyOrder orders = new SupplyOrder(rst.getString("soid"), rst.getString("supId"), rst.getDate("sODate"));
            orderList.add(orders);
        }
        return orderList;
    }
}
