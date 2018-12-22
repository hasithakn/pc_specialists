/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import PCHardwareSpecialists.model.Brand;
import PCHardwareSpecialists.model.SupplyOrderBrandItemDetails;
import db.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Hasithakn
 */
public class SupplyOrderBrandItemDetailsController {

    public static int addSupplyOrderBrandItemDetail(SupplyOrderBrandItemDetails sobid) throws SQLException, ClassNotFoundException {
        String sql = "insert into SupplyOrderBrandItemDetails values('" + sobid.getSoid() + "','" + sobid.getBatchId() +  "')";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        int res1 = stm.executeUpdate(sql);
        return res1 > 0 ? 1 : -1;
    }
     public static SupplyOrderBrandItemDetails searchSupplyOrderBrandItemDetail(String batchId) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From SupplyOrderBrandItemDetails where batchId='" + batchId + "'";
        ResultSet rst = stm.executeQuery(sql);
        if (rst.next()) {
            SupplyOrderBrandItemDetails sobid = new SupplyOrderBrandItemDetails(rst.getString("soid"), rst.getString("batchId"));
            return sobid;
        } else {
            return null;
        }
    }

}
