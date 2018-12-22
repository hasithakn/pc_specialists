/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import db.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hasithakn
 */
public class IdGen {

    public static String getNextId(String tableName, String columnName, String prefix) throws SQLException {
        try {

            String sql = "select " + columnName + " from " + tableName + "  order by 1 desc limit 1";
            Connection connection = null;

            try {
                connection = DBConnection.getInstance().getConnection();

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(IdGen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(IdGen.class.getName()).log(Level.SEVERE, null, ex);
            }
            Statement stm = connection.createStatement();

            ResultSet rst = stm.executeQuery(sql);

            if (rst.next()) {

                int index = 0;
                String currentId = rst.getString(1);
                for (int i = 0; i < currentId.length(); i++) {
                    char ch = currentId.charAt(i);
                    if (Character.isDigit(ch)) {
                        index = i;
                        break;
                    }
                }
                try {
                    int num = Integer.parseInt(currentId.substring(index));
                    num++;
                    NumberFormat numberFormat = NumberFormat.getNumberInstance();

                    numberFormat.setMinimumIntegerDigits(3);
                    String formattedNum = numberFormat.format(num);

                    String nextId = prefix + formattedNum;
                    return nextId;
                } catch (Exception e) {
                    return currentId + "001";
                }

            } else {

                return prefix + "001";
            }

        } catch (Exception e) {
            return prefix + "001";
        }

    }

}
