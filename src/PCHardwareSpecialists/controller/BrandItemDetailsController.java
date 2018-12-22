/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

/**
 *
 * @author Hasithakn
 */
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import PCHardwareSpecialists.model.Brand;
import PCHardwareSpecialists.model.BrandItemDetails;
import PCHardwareSpecialists.model.SupplyOrderBrandItemDetails;
import PCHardwareSpecialists.model.SupplyOrder;
import PCHardwareSpecialists.model.Item;
import PCHardwareSpecialists.model.OrderBrandItemDetails;
import PCHardwareSpecialists.model.Warrenty;

public class BrandItemDetailsController {

    public static int addBrandItemDetailsAll(BrandItemDetails brandItemDetails) throws ClassNotFoundException, SQLException {
        int a = -1;
        Connection conn = DBConnection.getInstance().getConnection();
        Item i = ItemController.searchItem(brandItemDetails.getiCode());
        Brand b = BrandController.searchBrand(brandItemDetails.getBid());
        Warrenty w = WarrentyController.searchWarrenty(brandItemDetails.getWid());
        boolean wi = false, wb = false, ww = false;
        conn.setAutoCommit(false);
        if (i == null) {
            try {
                int i2 = ItemController.addItem(brandItemDetails.getItem());
                if (i2 > 0) {
                    wi = true;
                } else {
                    wi = false;
                    conn.rollback();
                    return -1;
                }
            } catch (Exception e) {
                throw e;
            }
        } else {
            wi = true;
        }
        if (b == null) {
            try {
                int b2 = BrandController.addBrand(brandItemDetails.getBrand());
                if (b2 > 0) {
                    wb = true;
                } else {
                    wb = false;
                    conn.rollback();
                    return -1;
                }
            } catch (Exception e) {
                throw e;
            }
        } else {
            wb = true;
        }
        if (w == null) {
            try {
                int w2 = WarrentyController.addWarrenty(brandItemDetails.getWarrenty());
                if (w2 > 0) {
                    ww = true;
                } else {
                    ww = false;
                    conn.rollback();
                    return -1;
                }
            } catch (Exception e) {
                throw e;
            }
        } else {
            ww = true;
        }
        if (wi == true && wb == true && ww == true) {
            PreparedStatement stm = conn.prepareStatement("Insert into BrandItemDetails values(?,?,?,?,?)");
            stm.setObject(1, brandItemDetails.getBiid());
            stm.setObject(2, brandItemDetails.getWid());
            stm.setObject(3, brandItemDetails.getBid());
            stm.setObject(4, brandItemDetails.getiCode());
            stm.setObject(5, brandItemDetails.getSellingPrice());
            try {
                int res1 = stm.executeUpdate();
                if (res1 > 0) {
                    conn.commit();
                    a = 1;
                } else {
                    conn.rollback();
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
        return a;
    }
    /*
     public static int getQtyFromBIID(String biid) throws ClassNotFoundException, SQLException {
     Connection conn = DBConnection.getInstance().getConnection();
     PreparedStatement stm = conn.prepareStatement("Insert into BrandItemDetails values(?,?,?,?,?,?)");

     return stm.executeUpdate();

     }*/

    /* public static int addBrandItemDetails(BrandItemDetails brandItemDetails) throws ClassNotFoundException, SQLException {
     Connection conn = DBConnection.getInstance().getConnection();
     PreparedStatement stm = conn.prepareStatement("Insert into BrandItemDetails values(?,?,?,?,?,?)");
     stm.setObject(1, brandItemDetails.getItemPrice());
     stm.setObject(2, brandItemDetails.getBiid());
     stm.setObject(3, brandItemDetails.getQtyOnHand());
     stm.setObject(4, brandItemDetails.getWid());
     stm.setObject(5, brandItemDetails.getBid());
     stm.setObject(6, brandItemDetails.getiCode());

     return stm.executeUpdate();

     }*/
    public static int updateBrandItemDetails(BrandItemDetails brandItemDetails) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Update BrandItemDetails set sellingPrice=? where biid=?");
        stm.setObject(1, brandItemDetails.getSellingPrice());
        stm.setObject(2, brandItemDetails.getBiid());
        return stm.executeUpdate();
    }

    public static BrandItemDetails searchBrandItemDetailsByBIID(String biid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From brandItemDetails where biid='" + biid + "'";
        ResultSet rst = stm.executeQuery(sql);
        if (rst.next()) {
            BrandItemDetails brandItemDetails = new BrandItemDetails(rst.getString("biid"), rst.getString("wid"), rst.getString("bid"), rst.getString("icode"), Double.parseDouble(rst.getString("sellingPrice")));
            return brandItemDetails;
        } else {
            return null;
        }
    }

    public static ArrayList<Brand> searchBidFromBrandItemDetailsWhereIcode(String iCode) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From brandItemDetails where iCode='" + iCode + "'";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Brand> brandList = new ArrayList<>();
        while (rst.next()) {
            Brand brand = new Brand(rst.getString("bid"), "");
            brandList.add(brand);
        }
        return brandList;
    }

    public static ArrayList<Warrenty> searchWidFromBrandItemDetailsWhereIcodeAndBid(String iCode, String bid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "select * from branditemdetails where icode='" + iCode + "' and bid='" + bid + "'";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<Warrenty> warrentyList = new ArrayList<>();
        while (rst.next()) {
            Warrenty warrenty = new Warrenty(rst.getString("wid"), "");
            warrentyList.add(warrenty);
        }
        return warrentyList;
    }

    public static BrandItemDetails searchBrandItemDetails(String iCode, String bid, String wid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From brandItemDetails where iCode='" + iCode + "' and bid='" + bid + "' and wid='" + wid + "'";
        ResultSet rst = stm.executeQuery(sql);
        if (rst.next()) {
            BrandItemDetails brandItemDetails = new BrandItemDetails(rst.getString("biid"), rst.getString("wid"), rst.getString("bid"), rst.getString("icode"), Double.parseDouble(rst.getString("sellingPrice")));
            return brandItemDetails;
        } else {
            return null;
        }
    }

    public static int deleteBrandItemDetails(String biid) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement stm = conn.prepareStatement("Delete From brandItemDetails where biid=?");
        stm.setObject(1, biid);
        return stm.executeUpdate();
    }

    /*  public static int updateItemQty(OrderBrandItemDetails obid) throws SQLException, ClassNotFoundException {
     String sql = "update brandItemDetails set qtyOnHand = qtyOnHand - " + obid.getoQty() + " where biid = '" + obid.getBiid() + "'";
     Connection conn = DBConnection.getInstance().getConnection();
     Statement stm = conn.createStatement();
     return stm.executeUpdate(sql);
     }*/
    /*
     public static int updateItemQtySupplyOrder(SupplyOrderBrandItemDetails so) throws SQLException, ClassNotFoundException {
     String sql = "update brandItemDetails set qtyOnHand = qtyOnHand + " + so.getSupQty() + " where biid = '" + so.getBiid() + "'";
     Connection conn = DBConnection.getInstance().getConnection();
     Statement stm = conn.createStatement();
     return stm.executeUpdate(sql);
     }

     public static int updateItemQtyWhenDeleteOrder(String biid, String increseByQty) throws SQLException, ClassNotFoundException {
     String sql = "update brandItemDetails set qtyOnHand = qtyOnHand + " + increseByQty + " where biid = '" + biid + "'";
     Connection conn = DBConnection.getInstance().getConnection();
     Statement stm = conn.createStatement();
     return stm.executeUpdate(sql);
     }*/
    /*
     public static Item searchItem(String iCode) throws ClassNotFoundException, SQLException {
     Connection conn = DBConnection.getInstance().getConnection();
     Statement stm = conn.createStatement();
     String sql = "Select * From Item where bid='" + iCode + "'";
     ResultSet rst = stm.executeQuery(sql);
     if (rst.next()) {
     Item item = new Item(rst.getString("iCode"), rst.getString("Description"));
     return item;
     } else {
     return null;
     }
     }*/
    public static ArrayList<BrandItemDetails> getAllBrandItemDetails() throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        Statement stm = conn.createStatement();
        String sql = "Select * From brandItemDetails";
        ResultSet rst = stm.executeQuery(sql);
        ArrayList<BrandItemDetails> brandItemDetailsList = new ArrayList<>();
        while (rst.next()) {
            BrandItemDetails brandItemDetails = new BrandItemDetails(rst.getString("biid"), rst.getString("wid"), rst.getString("bid"), rst.getString("icode"), Double.parseDouble(rst.getString("sellingPrice")));
            brandItemDetailsList.add(brandItemDetails);
        }
        return brandItemDetailsList;
    }
}
