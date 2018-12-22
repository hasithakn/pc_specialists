/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.view;

import PCHardwareSpecialists.controller.BrandController;
import PCHardwareSpecialists.controller.BrandItemDetailsController;
import PCHardwareSpecialists.controller.ItemController;
import PCHardwareSpecialists.controller.OtherSql;
import PCHardwareSpecialists.controller.SupplierController;
import PCHardwareSpecialists.controller.SupplyOrderController;
import PCHardwareSpecialists.controller.SupplyOrderQtyController;
import PCHardwareSpecialists.controller.WarrentyController;
import PCHardwareSpecialists.model.BrandItemDetails;
import PCHardwareSpecialists.model.Supplier;
import PCHardwareSpecialists.model.SupplyOrder;
import PCHardwareSpecialists.model.SypplyOrderQty;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Hasithakn
 */
public class ViewSupplyOrderItems extends javax.swing.JFrame {

    ViewSupplyOrder vso = null;

    /**
     * Creates new form Invoice
     */
    public ViewSupplyOrderItems() {
        initComponents();
        setTitle("Supply Order Details");
        fillSupplierCombo();
        fillTable();
        grandTotal();
        AutoCompleteDecorator.decorate(supplierCombo);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    if (table.getSelectedRow() != -1) {
                        biidText.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
                        itemDestext.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
                        brandNameText.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
                        warrentyperiodText.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
                        supplyQty.setText(table.getValueAt(table.getSelectedRow(), 4).toString());
                        supItemPriceText.setText(table.getValueAt(table.getSelectedRow(), 5).toString());
                        totalText.setText(table.getValueAt(table.getSelectedRow(), 6).toString());

                        BrandItemDetails bid = BrandItemDetailsController.searchBrandItemDetailsByBIID(table.getValueAt(table.getSelectedRow(), 0).toString());
                        if (bid != null) {
                            widText.setText(bid.getWid());
                            brandIdText.setText(bid.getBid());
                            iCodeText.setText(bid.getiCode());
                        } else {
                            widText.setText("");
                            brandIdText.setText("");
                            iCodeText.setText("");
                        }

                    } else {
                        biidText.setText("");
                        itemDestext.setText("");
                        brandNameText.setText("");
                        warrentyperiodText.setText("");
                        supplyQty.setText("");
                        supItemPriceText.setText("");
                        totalText.setText("");
                        widText.setText("");
                        brandIdText.setText("");
                        iCodeText.setText("");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ViewSupplyOrderItems.this, ex.getMessage());
                    biidText.setText("");
                    itemDestext.setText("");
                    brandNameText.setText("");
                    warrentyperiodText.setText("");
                    supplyQty.setText("");
                    supItemPriceText.setText("");
                    totalText.setText("");
                    widText.setText("");
                    brandIdText.setText("");
                    iCodeText.setText("");
                }

            }
        });
    }

    public ViewSupplyOrderItems(ViewSupplyOrder vso) {
        initComponents();
        this.vso = vso;
        setTitle("Supply Order Details");
        fillSupplierCombo();
        fillTable();
        grandTotal();
        AutoCompleteDecorator.decorate(supplierCombo);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    if (table.getSelectedRow() != -1) {
                        biidText.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
                        itemDestext.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
                        brandNameText.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
                        warrentyperiodText.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
                        supplyQty.setText(table.getValueAt(table.getSelectedRow(), 4).toString());
                        supItemPriceText.setText(table.getValueAt(table.getSelectedRow(), 5).toString());
                        totalText.setText(table.getValueAt(table.getSelectedRow(), 6).toString());

                        BrandItemDetails bid = BrandItemDetailsController.searchBrandItemDetailsByBIID(table.getValueAt(table.getSelectedRow(), 0).toString());
                        if (bid != null) {
                            widText.setText(bid.getWid());
                            brandIdText.setText(bid.getBid());
                            iCodeText.setText(bid.getiCode());
                        } else {
                            widText.setText("");
                            brandIdText.setText("");
                            iCodeText.setText("");
                        }

                    } else {
                        biidText.setText("");
                        itemDestext.setText("");
                        brandNameText.setText("");
                        warrentyperiodText.setText("");
                        supplyQty.setText("");
                        supItemPriceText.setText("");
                        totalText.setText("");
                        widText.setText("");
                        brandIdText.setText("");
                        iCodeText.setText("");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ViewSupplyOrderItems.this, ex.getMessage());
                    biidText.setText("");
                    itemDestext.setText("");
                    brandNameText.setText("");
                    warrentyperiodText.setText("");
                    supplyQty.setText("");
                    supItemPriceText.setText("");
                    totalText.setText("");
                    widText.setText("");
                    brandIdText.setText("");
                    iCodeText.setText("");
                }

            }
        });
    }

    public void grandTotal() {
        Double a = 0.0;
        for (int i = 0; i < table.getRowCount(); i++) {
            a = a + Double.parseDouble(table.getValueAt(i, 6).toString());
        }
        grandTotalText.setText(a.toString());

    }

    public void fillSupplierDetailsByName() {
        ArrayList<Supplier> supplierlist = null;
        if (!supplierCombo.getEditor().getItem().toString().isEmpty()) {
            try {
                supplierlist = SupplierController.searchSupplierByName(supplierCombo.getEditor().getItem().toString());
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewSupplyOrderItems.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewSupplyOrderItems.this, ex.getMessage());
            }
            Supplier[] sup = new Supplier[supplierlist.size()];
            for (int i = 0; i < supplierlist.size(); i++) {
                sup[i] = supplierlist.get(i);
            }
            if (supplierlist.size() == 1 && sup != null) {
                supIdText.setText(sup[0].getSupId());
            } else {
                supIdText.setText("");

            }
        }
    }

    public void fillSupplierCombo() {
        supplierCombo.removeAllItems();
        ArrayList<Supplier> supList = null;
        try {
            supList = SupplierController.getAllSupplier();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewSupplyOrderItems.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewSupplyOrderItems.this, ex.getMessage());
        }

        for (Supplier supplier : supList) {
            supplierCombo.addItem(supplier.getSupName());
            AutoCompleteDecorator.decorate(supplierCombo);
        }
    }

    public void fillTable() {
        ResultSet rstBatches = null;
        ResultSet rstBiids = null;
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0);
        try {
            String sql = "select batchId from supplyorderbranditemdetails where soid='" + soidText.getText() + "'";

            rstBatches = OtherSql.otherSql(sql);
            while (rstBatches.next()) {
                String batchId = rstBatches.getString("batchId");
                String sql2 = "select * from batch where batchId='" + rstBatches.getString("batchId") + "'";

                rstBiids = OtherSql.otherSql(sql2);

                rstBiids.next();

                String biid = rstBiids.getString("biid");
                int qty = rstBiids.getInt("qty");
                double unitPrice = rstBiids.getDouble("unitPrice");
                String iCode = "";
                String bid = "";
                String wid = "";
                String itemDes = "";
                String brand = "";
                String warrenty = "";

                BrandItemDetails brandItemDetails = BrandItemDetailsController.searchBrandItemDetailsByBIID(biid);
                if (brandItemDetails != null) {
                    iCode = brandItemDetails.getiCode();
                    bid = brandItemDetails.getBid();
                    wid = brandItemDetails.getWid();
                    itemDes = ItemController.searchItem(iCode).getDescription();
                    brand = BrandController.searchBrand(bid).getBrandName();
                    warrenty = WarrentyController.searchWarrenty(wid).getwPeriod();

                }
                SypplyOrderQty soq = null;
                soq = SupplyOrderQtyController.searchSupplyOrderQty(batchId);
                if (soq != null) {
                    Double tot = unitPrice * Double.parseDouble(Integer.toString(soq.getQty()));
                    Object[] rowData = {biid, itemDes, brand, warrenty, qty+"/"+soq.getQty(), unitPrice, tot, batchId};
                    dtm.addRow(rowData);
                } else {
                    Double tot = unitPrice * Double.parseDouble(Integer.toString(qty));
                    Object[] rowData = {biid, itemDes, brand, warrenty, qty, unitPrice, tot, batchId};
                    dtm.addRow(rowData);
                }

            }
            grandTotal();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewSupplyOrderItems.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewSupplyOrderItems.this, ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        supIdText = new javax.swing.JTextField();
        supplierCombo = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        biidText = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        supItemPriceText = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        supplyQty = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        totalText = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        brandNameText = new javax.swing.JTextField();
        itemDestext = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        warrentyperiodText = new javax.swing.JTextField();
        iCodeText = new javax.swing.JTextField();
        brandIdText = new javax.swing.JTextField();
        widText = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        soidText = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        grandTotalText = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(" Supply Order Details");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Supplier Details"));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel2.setText("Supplier ID");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel3.setText("Supplier Name");

        supIdText.setEditable(false);
        supIdText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        supplierCombo.setEditable(true);
        supplierCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        supplierCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(supplierCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(supIdText, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(supplierCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(supIdText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Item Details"));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel10.setText("BIID");

        biidText.setEditable(false);
        biidText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel13.setText("Supply Item Price");

        supItemPriceText.setEditable(false);
        supItemPriceText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel14.setText("Qty ");

        supplyQty.setEditable(false);
        supplyQty.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setText("Total");

        totalText.setEditable(false);
        totalText.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel11.setText("Item Des.");

        brandNameText.setEditable(false);
        brandNameText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        itemDestext.setEditable(false);
        itemDestext.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel15.setText("Brand");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel16.setText("Warrenty");

        warrentyperiodText.setEditable(false);
        warrentyperiodText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        iCodeText.setEditable(false);
        iCodeText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        brandIdText.setEditable(false);
        brandIdText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        widText.setEditable(false);
        widText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel10)
                            .addComponent(jLabel14))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(supItemPriceText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                            .addComponent(biidText, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(supplyQty)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(107, 107, 107)
                        .addComponent(totalText, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel11)
                            .addComponent(jLabel16))
                        .addGap(80, 80, 80)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(brandNameText)
                            .addComponent(warrentyperiodText, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                            .addComponent(itemDestext))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(iCodeText, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                            .addComponent(brandIdText)
                            .addComponent(widText))))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(itemDestext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iCodeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(brandNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(brandIdText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(warrentyperiodText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(widText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(biidText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(supItemPriceText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(supplyQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(totalText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jButton4.setText("Cancal");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Supply Order Details"));

        soidText.setEditable(false);
        soidText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel20.setText("Supply Order ID");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel21.setText("Supply Order Date");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(soidText))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(soidText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        table.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BIID", "Item Des.", "Brand", "Warrenty", "Qty ", "Supply Item Price", "Total", "Batch ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel19.setText("Grand Total");

        grandTotalText.setEditable(false);
        grandTotalText.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jButton5.setText("Save");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(561, 561, 561)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grandTotalText)
                        .addGap(26, 26, 26)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(grandTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void supplierComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierComboActionPerformed
        fillSupplierDetailsByName();        // TODO add your handling code here:
    }//GEN-LAST:event_supplierComboActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (!supIdText.getText().isEmpty()) {
            String supId = supIdText.getText();
            Date supDate = datePicker.getDate();
            SupplyOrder so = new SupplyOrder(soidText.getText(), supId, supDate);
            int a = 0;
            try {
                a = SupplyOrderController.updateSupplyOrders(so);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ViewSupplyOrderItems.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ViewSupplyOrderItems.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (a > 0) {
                JOptionPane.showMessageDialog(ViewSupplyOrderItems.this, "Supply Order Saved");
                if (vso != null) {
                    vso.fillOrderTable();
                    vso.fillSupplierCombo();
                    vso.fillOidBySupId();
                }
            } else {
                JOptionPane.showMessageDialog(ViewSupplyOrderItems.this, "Supply Order Not Saved");
            }
        } else {
            JOptionPane.showMessageDialog(ViewSupplyOrderItems.this, "Supplier Not Selected");
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewSupplyOrderItems.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewSupplyOrderItems.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewSupplyOrderItems.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewSupplyOrderItems.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewSupplyOrderItems().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField biidText;
    private javax.swing.JTextField brandIdText;
    private javax.swing.JTextField brandNameText;
    public org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JTextField grandTotalText;
    private javax.swing.JTextField iCodeText;
    private javax.swing.JTextField itemDestext;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextField soidText;
    public javax.swing.JTextField supIdText;
    private javax.swing.JTextField supItemPriceText;
    public javax.swing.JComboBox supplierCombo;
    private javax.swing.JTextField supplyQty;
    private javax.swing.JTable table;
    private javax.swing.JTextField totalText;
    private javax.swing.JTextField warrentyperiodText;
    private javax.swing.JTextField widText;
    // End of variables declaration//GEN-END:variables
}
