/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import static PCHardwareSpecialists.controller.BatchController.addBatch;
import PCHardwareSpecialists.model.Batch;
import PCHardwareSpecialists.model.SupplyOrder;
import PCHardwareSpecialists.model.SypplyOrderQty;
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
public class SupplyOrderQtyController {

    public static int addQtyToSypplyOrderQtyTableWhenAddSupplyOrder(String batchId, int qty) throws ClassNotFoundException, SQLException {
        String sql = "insert into supplyOrderQty values('" + batchId + "'," + qty + ")";
        Connection conn2 = DBConnection.getInstance().getConnection();
        Statement stm2 = conn2.createStatement();
        conn2.setAutoCommit(false);
        return stm2.executeUpdate(sql);
    }

    public static SypplyOrderQty searchSupplyOrderQty(String batchId) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From supplyOrderQty where batchId='" + batchId + "'";
        ResultSet rst = stm.executeQuery(sql);
        SypplyOrderQty aoq = null;
        if (rst.next()) {
            aoq = new SypplyOrderQty(batchId, rst.getInt("qty"));
        }
        return aoq;
    }
}
