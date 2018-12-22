/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import PCHardwareSpecialists.model.SummarizeItemsDialogModel;
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
public class ViewOrderBrandItemDetailsController {

    public static ArrayList<SummarizeItemsDialogModel> getoQtyBiidPriceForViewOrderBrandItemdetails(String oid) throws ClassNotFoundException, SQLException {
        String sql = "Select oQty,batchId,price from orderbranditemdetails obid, orders o where obid.oid=o.oid and o.oid='" + oid + "'";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<SummarizeItemsDialogModel> List = new ArrayList<>();
        while (rst.next()) {
            SummarizeItemsDialogModel summarizeItemsDialogModel = new SummarizeItemsDialogModel(rst.getInt("oQty"), rst.getDouble("Price"), rst.getString("batchId"));
            List.add(summarizeItemsDialogModel);
        }
        return List;
    }
}
