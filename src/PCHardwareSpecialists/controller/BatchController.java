/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import static PCHardwareSpecialists.controller.SupplyOrderBrandItemDetailsController.addSupplyOrderBrandItemDetail;
import PCHardwareSpecialists.model.Batch;
import PCHardwareSpecialists.model.Brand;
import PCHardwareSpecialists.model.OrderBrandItemDetails;
import PCHardwareSpecialists.model.SupplyOrderBrandItemDetails;
import db.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Hasithakn
 */
public class BatchController {

    public static int addBatch(Batch b, String soid) throws ClassNotFoundException, SQLException {
        String batchId = IdGen.getNextId("batch", "batchId", "BATCH");
        String sql = "insert into batch values('" + batchId + "','" + b.getBiid() + "'," + b.getQty() + "," + b.getUnitPrice() + ")";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = null;

        try {
            conn.setAutoCommit(false);
            stm = conn.createStatement();
            int res1 = stm.executeUpdate(sql);
            if (res1 > 0) {
                SupplyOrderBrandItemDetails sobid = new SupplyOrderBrandItemDetails(soid, batchId);
                int res2 = SupplyOrderBrandItemDetailsController.addSupplyOrderBrandItemDetail(sobid);
                if (res2 > 0) {
                    String sql2 = "insert into supplyOrderQty values('" + batchId + "'," + b.getQty() + ")";
                    Connection conn2 = DBConnection.getInstance().getConnection();
                    Statement stm2 = conn2.createStatement();
                    conn2.setAutoCommit(false);
                    int a2 = stm2.executeUpdate(sql2);
                    if (a2 > 0) {
                        return 1;
                    } else {
                        conn.rollback();
                        conn2.rollback();
                        return -1;
                    }

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
            // conn.setAutoCommit(true);
        }
    }

    public static int addBatchesWhenAddSupplyOrder(Batch[] b, String soid) throws ClassNotFoundException, SQLException {
        for (Batch so1 : b) {
            int res = addBatch(so1, soid);
            if (res <= 0) {
                return -1;
            }
        }
        return 1;
    }

    public static int zeroQtyInBatchWhenDeleteSupplyOrder(String batchId) throws ClassNotFoundException, SQLException {
        String sql = "Update Batch set qty =0 where batchId='" + batchId + "'";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        return stm.executeUpdate(sql);
    }

    public static int updateQtyInBatchWhenNewOrder(OrderBrandItemDetails obid) throws ClassNotFoundException, SQLException {
        String sql = "Update Batch set qty =qty-" + obid.getoQty() + " where batchId='" + obid.getBatchId() + "'";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        return stm.executeUpdate(sql);
    }

    public static int updateQtyInBatchWhenDeleteOrder(String batchId, String oQty) throws ClassNotFoundException, SQLException {
        String sql = "Update Batch set qty =qty+" + oQty + " where batchId='" + batchId + "'";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        return stm.executeUpdate(sql);
    }

    public static String selectBiidFromBatchWhereBatchId(String batchId) throws ClassNotFoundException, SQLException {
        String sql = "Select biid From batch where batchId='" + batchId + "'";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        ResultSet rst = stm.executeQuery(sql);
        rst.next();
        return rst.getString("biid");
    }

    public static ArrayList<Batch> searchBatchesByBIID(String biid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From batch where biid='" + biid + "'";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Batch> batchList = new ArrayList<>();
        while (rst.next()) {
            Batch batch = new Batch(rst.getString("batchId"), rst.getString("biid"), rst.getInt("qty"), rst.getDouble("unitPrice"));
            batchList.add(batch);
        }
        return batchList;
    }
}
