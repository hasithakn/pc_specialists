/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import db.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import PCHardwareSpecialists.model.OrderBrandItemDetails;

/**
 *
 * @author Hasithakn
 */
public class OrderBrandItemDetailsController {

    public static int addOrderBrandItemDetail(OrderBrandItemDetails obid) throws SQLException, ClassNotFoundException {
        String sql = "insert into OrderBrandItemDetails values('" + obid.getBatchId() + "'," + obid.getoQty() + "," + obid.getPrice() + ",'" + obid.getOid() + "')";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        int res1 = stm.executeUpdate(sql);
        int res2 = BatchController.updateQtyInBatchWhenNewOrder(obid);
        return res1 > 0 && res2 > 0 ? 1 : -1;
    }

    public static int addOrderDetails(OrderBrandItemDetails[] orderBrandItemDetails) throws SQLException, ClassNotFoundException {
        for (OrderBrandItemDetails obid : orderBrandItemDetails) {
            int res = addOrderBrandItemDetail(obid);
            if (res <= 0) {
                return -1;
            }
        }
        return 1;
    }
}
