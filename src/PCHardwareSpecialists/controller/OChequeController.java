/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import PCHardwareSpecialists.model.OrderPayments;
import PCHardwareSpecialists.model.OCheque;
import PCHardwareSpecialists.model.Orders;
import db.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

/**
 *
 * @author Hasithakn
 */
public class OChequeController {

    public static int addChequePayment(OCheque oc) throws SQLException, ClassNotFoundException {
        String sql = "insert into OCheque values('" + oc.getOpid() + "','" + oc.getChequeNo() + "')";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        return stm.executeUpdate(sql);
    }

    public static int addChequePaymentFromAddingPayment(OCheque oc) throws SQLException, ClassNotFoundException {
        String sql = "insert into OCheque values('" + oc.getOpid() + "','" + oc.getChequeNo() + "')";
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        int a = stm.executeUpdate(sql);
        if (a > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public static OCheque searchChequePaymentByOPID(String opid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From OCheque where opid='" + opid + "'";
        ResultSet rst = stm.executeQuery(sql);
        if (rst.next()) {
            OCheque oCheque = new OCheque(rst.getString("opid"), rst.getString("chequeNo"));
            return oCheque;
        } else {
            return null;
        }
    }
}
