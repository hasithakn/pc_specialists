/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import PCHardwareSpecialists.model.Batch;
import PCHardwareSpecialists.model.SummarizeItemsDialogModel;
import PCHardwareSpecialists.view.SummarizeItemsDialog;
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
public class SummarizeItemsDialogController {

    public static ArrayList<SummarizeItemsDialogModel> getoQtyBiidPriceForSummarizeItemsDialog(String oid) throws ClassNotFoundException, SQLException {
        String sql = "Select biid,sum(oQty) as oQty,price from orderbranditemdetails obid, orders o,batch b where obid.oid=o.oid and o.oid='" + oid + "' and b.batchId=obid.batchId group by biid";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<SummarizeItemsDialogModel> List = new ArrayList<>();
        while (rst.next()) {
            SummarizeItemsDialogModel summarizeItemsDialogModel = new SummarizeItemsDialogModel(rst.getString("biid"), rst.getInt("oQty"),rst.getDouble("Price"));
            List.add(summarizeItemsDialogModel);
        }
        return List;
    }
}
