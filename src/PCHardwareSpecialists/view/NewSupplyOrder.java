/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.view;

import PCHardwareSpecialists.controller.BrandController;
import PCHardwareSpecialists.controller.BrandItemDetailsController;
import PCHardwareSpecialists.controller.CustomerController;
import PCHardwareSpecialists.controller.IdGen;
import PCHardwareSpecialists.controller.ItemController;
import PCHardwareSpecialists.controller.OrdersController;
import PCHardwareSpecialists.controller.OtherSql;
import PCHardwareSpecialists.controller.SupplierController;
import PCHardwareSpecialists.controller.SupplyOrderController;
import PCHardwareSpecialists.controller.WarrentyController;
import PCHardwareSpecialists.main.Main;
import PCHardwareSpecialists.model.Batch;
import PCHardwareSpecialists.model.Brand;
import PCHardwareSpecialists.model.BrandItemDetails;
import PCHardwareSpecialists.model.Customer;
import PCHardwareSpecialists.model.Item;
import PCHardwareSpecialists.model.OrderBrandItemDetails;
import PCHardwareSpecialists.model.Orders;
import PCHardwareSpecialists.model.Supplier;
import PCHardwareSpecialists.model.SupplyOrder;
import PCHardwareSpecialists.model.SupplyOrderBrandItemDetails;
import PCHardwareSpecialists.model.Warrenty;
import PCHardwareSpecialists.view.Invoice;
import PCHardwareSpecialists.view.NewItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Hasithakn
 */
public class NewSupplyOrder extends javax.swing.JInternalFrame {

    ViewSupplyOrder vso = null;
    Main m = null;

    /**
     * Creates new form Invoice
     */
    public NewSupplyOrder() {
        initComponents();
        setTitle("New Supply Order");
        soidGen();
        fillSupplierCombo();
        fillItemDesCombo();
        AutoCompleteDecorator.decorate(itemDesCombo);
        AutoCompleteDecorator.decorate(brandNameCombo);
        AutoCompleteDecorator.decorate(supplierCombo);
    }

    public NewSupplyOrder(Main mm) {
        initComponents();
        setTitle("New Supply Order");
        this.m = mm;
        soidGen();
        fillSupplierCombo();
        fillItemDesCombo();
        AutoCompleteDecorator.decorate(itemDesCombo);
        AutoCompleteDecorator.decorate(brandNameCombo);
        AutoCompleteDecorator.decorate(supplierCombo);
    }

    public NewSupplyOrder(ViewSupplyOrder vso) {
        this.vso = vso;
        initComponents();
        setTitle("New Supply Order");
        soidGen();
        fillSupplierCombo();
        fillItemDesCombo();
        AutoCompleteDecorator.decorate(itemDesCombo);
        AutoCompleteDecorator.decorate(brandNameCombo);
        AutoCompleteDecorator.decorate(supplierCombo);
    }

    public void soidGen() {
        try {
            SOIDText.setText(IdGen.getNextId("supplyOrder", "soid", "SOID"));
        } catch (SQLException ex) {
            Logger.getLogger(NewItem.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
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
            JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
        }
        for (Supplier supplier : supList) {
            supplierCombo.addItem(supplier.getSupName());
            AutoCompleteDecorator.decorate(supplierCombo);
        }
    }

    public void fillItemDesCombo() {
        ArrayList<Item> item = null;
        try {
            item = ItemController.getAllItem();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
        }
        itemDesCombo.removeAllItems();
        for (Item item1 : item) {
            itemDesCombo.addItem(item1.getDescription());
        }
    }

    public void fillbrandComboByItem() {
        String description = itemDesCombo.getEditor().getItem().toString();
        ArrayList<Item> item2 = null;
        if (!description.isEmpty() || description != null) {
            brandNameCombo.removeAllItems();
            try {
                item2 = ItemController.searchItemByname(description);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
            }
            if (item2 != null) {
                for (Item item : item2) {
                    ArrayList<Brand> brands = null;
                    try {
                        brands = BrandItemDetailsController.searchBidFromBrandItemDetailsWhereIcode(item.getiCode());
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
                    } catch (NullPointerException ex) {
                        JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage() + "ss");
                    }
                    if (brands != null) {
                        for (Brand brand : brands) {
                            Brand brandByBid = null;
                            try {
                                brandByBid = BrandController.searchBrand(brand.getBid());
                            } catch (ClassNotFoundException ex) {
                                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
                            }
                            if (brandByBid != null) {
                                brandNameCombo.addItem(brandByBid.getBrandName());
                            }
                            try {
                                brandNameCombo.setSelectedIndex(0);
                            } catch (IllegalArgumentException ex) {

                            }
                        }
                    }
                }
            }
        } else {
            ArrayList<Brand> brand = null;
            try {
                brand = BrandController.getAllBrand();
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
            }
            brandNameCombo.removeAllItems();
            for (Brand brand1 : brand) {
                brandNameCombo.addItem(brand1.getBrandName());
            }
            try {
                brandNameCombo.setSelectedIndex(0);
            } catch (IllegalArgumentException ex) {

            }
        }

    }

    public boolean checkBeforeAddBiid() {
        String a = biidText.getText();
        boolean b = false;
        for (int i = 0; i < table.getRowCount(); i++) {
            String a1 = table.getValueAt(i, 0).toString();
            if (a1.equals(a)) {
                b = true;
                break;
            } else {
                b = false;
            }
        }
        return b;
    }

    public void fillwarrentyDesCombo() {
        ArrayList<Warrenty> warrenty = null;
        try {
            warrenty = WarrentyController.getAllWarrenty();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
        }
        warrentyperiodCombo.removeAllItems();
        for (Warrenty warrenty1 : warrenty) {
            warrentyperiodCombo.addItem(warrenty1.getwPeriod());
        }
        try {
            warrentyperiodCombo.setSelectedIndex(0);
        } catch (IllegalArgumentException e) {
        }

    }

    public void fillwarrentyDesComboByBrand() {
        warrentyperiodCombo.removeAllItems();
        String iCode = iCodeText.getText();
        String bid = brandIdText.getText();
        ArrayList<Warrenty> widList = null;
        if (iCode.isEmpty() || bid.isEmpty()) {
            fillwarrentyDesCombo();
        } else {
            try {
                widList = BrandItemDetailsController.searchWidFromBrandItemDetailsWhereIcodeAndBid(iCode, bid);
                for (Warrenty warrenty : widList) {
                    Warrenty ww = WarrentyController.searchWarrenty(warrenty.getWid());
                    if (ww != null) {
                        warrentyperiodCombo.addItem(ww.getwPeriod());
                    }
                    try {
                        warrentyperiodCombo.setSelectedIndex(0);
                    } catch (IllegalArgumentException ex) {
                    }

                }
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
            }
        }
    }

    public void totalAndSupQtyTextClear() {
        totalText.setText("");
        supplyQty.setText("");
    }

    public void getBiidDetails() {
        BrandItemDetails brandItemDetails = null;
        if (iCodeText.getText().isEmpty() || brandIdText.getText().isEmpty() || widText.getText().isEmpty()) {
            biidText.setText("");

            totalAndSupQtyTextClear();
        } else {
            try {
                brandItemDetails = BrandItemDetailsController.searchBrandItemDetails(iCodeText.getText(), brandIdText.getText(), widText.getText());
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
            }
            if (brandItemDetails != null) {
                biidText.setText(brandItemDetails.getBiid());

                totalAndSupQtyTextClear();
            } else {
                biidText.setText("");

                totalAndSupQtyTextClear();
            }

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
        newSupplierButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        itemDesCombo = new javax.swing.JComboBox();
        iCodeText = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        brandNameCombo = new javax.swing.JComboBox();
        brandIdText = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        warrentyperiodCombo = new javax.swing.JComboBox();
        widText = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        biidText = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        supItemPriceText = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        supplyQty = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        totalText = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        SOIDText = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        grandTotalText = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("New Supply Order");

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

        newSupplierButton.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        newSupplierButton.setText("New Supplier");
        newSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newSupplierButtonActionPerformed(evt);
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
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(supplierCombo, 0, 345, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(newSupplierButton, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(supIdText, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(supplierCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newSupplierButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(supIdText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        table.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BIID", "Item Des.", "Brand", "Warrenty", "Supply Qty", "Supply Item Price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Item"));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel9.setText("Item Code");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel8.setText("Item Description");

        itemDesCombo.setEditable(true);
        itemDesCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        itemDesCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemDesComboActionPerformed(evt);
            }
        });

        iCodeText.setEditable(false);
        iCodeText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel11.setText("Brand");

        brandNameCombo.setEditable(true);
        brandNameCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        brandNameCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brandNameComboActionPerformed(evt);
            }
        });

        brandIdText.setEditable(false);
        brandIdText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel12.setText("Brand ID");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel16.setText("Warrenty Des.");

        warrentyperiodCombo.setEditable(true);
        warrentyperiodCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        warrentyperiodCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                warrentyperiodComboActionPerformed(evt);
            }
        });

        widText.setEditable(false);
        widText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel15.setText("WID");

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jButton1.setText("New Item");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(iCodeText, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(itemDesCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(brandIdText, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(brandNameCombo, 0, 355, Short.MAX_VALUE)
                                    .addComponent(warrentyperiodCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(widText, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(itemDesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(iCodeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(brandNameCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(brandIdText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(warrentyperiodCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(widText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Item Details"));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel10.setText("BIID");

        biidText.setEditable(false);
        biidText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel13.setText("Supply Item Price");

        supItemPriceText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        supItemPriceText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supItemPriceTextActionPerformed(evt);
            }
        });
        supItemPriceText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                supItemPriceTextKeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel14.setText("Supply Qty");

        supplyQty.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        supplyQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplyQtyActionPerformed(evt);
            }
        });
        supplyQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                supplyQtyKeyReleased(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel18.setText("Total");

        totalText.setEditable(false);
        totalText.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N

        addButton.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        addButton.setText("ADD");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel10)
                    .addComponent(jLabel14))
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(supItemPriceText, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(supplyQty)
                    .addComponent(biidText, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(104, 104, 104)
                .addComponent(jLabel18)
                .addGap(35, 35, 35)
                .addComponent(totalText)
                .addGap(30, 30, 30)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(biidText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(supItemPriceText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplyQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)))
        );

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jButton5.setText("Remove");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jButton6.setText("Save");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jButton4.setText("Cancal");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Supply Order Details"));

        SOIDText.setEditable(false);
        SOIDText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

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
                    .addComponent(SOIDText)
                    .addComponent(datePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SOIDText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        grandTotalText.setEditable(false);
        grandTotalText.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        jLabel19.setText("Grand Total");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(433, 433, 433)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(grandTotalText)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(grandTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void supplierComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierComboActionPerformed
        fillSupplierDetailsByName();        // TODO add your handling code here:
    }//GEN-LAST:event_supplierComboActionPerformed

    private void itemDesComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemDesComboActionPerformed
        fillbrandComboByItem();
        fillwarrentyDesComboByBrand();

        String description = itemDesCombo.getEditor().getItem().toString();
        ArrayList<Item> item = null;
        try {
            item = ItemController.searchItemByname(description);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
        }
        try {
            iCodeText.setText(item.get(0).getiCode());
        } catch (IndexOutOfBoundsException e) {
        }

        getBiidDetails();        // TODO add your handling code here:
    }//GEN-LAST:event_itemDesComboActionPerformed

    private void brandNameComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brandNameComboActionPerformed
        fillwarrentyDesComboByBrand();

        String brand = brandNameCombo.getEditor().getItem().toString();
        ArrayList<Brand> brandList = null;
        try {
            brandList = BrandController.searchBrandByName(brand);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
        }
        try {
            brandIdText.setText(brandList.get(0).getBid());
        } catch (IndexOutOfBoundsException e) {
        }
        getBiidDetails();        // TODO add your handling code here:
    }//GEN-LAST:event_brandNameComboActionPerformed

    private void warrentyperiodComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_warrentyperiodComboActionPerformed
        String warrenty = warrentyperiodCombo.getEditor().getItem().toString();
        ArrayList<Warrenty> warrenty2 = null;
        try {
            warrenty2 = WarrentyController.searchWarrentyByName(warrenty);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
        }
        try {
            if (!warrentyperiodCombo.getEditor().getItem().toString().isEmpty()) {
                widText.setText(warrenty2.get(0).getWid());
                //warrentyPeriodText.setText(warrenty2.get(0).getwPeriod());
            }
        } catch (IndexOutOfBoundsException e) {

        }
        getBiidDetails();        // TODO add your handling code here:
    }//GEN-LAST:event_warrentyperiodComboActionPerformed

    private void supplyQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplyQtyActionPerformed
        try {
            Double a = Double.parseDouble(supItemPriceText.getText()) * Double.parseDouble(supplyQty.getText());
            totalText.setText(Double.toString(a));
        } catch (NumberFormatException e) {
            totalText.setText("");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_supplyQtyActionPerformed

    private void supplyQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supplyQtyKeyReleased
        try {
            Double a = Double.parseDouble(supItemPriceText.getText()) * Double.parseDouble(supplyQty.getText());
            totalText.setText(Double.toString(a));
        } catch (NumberFormatException e) {
            totalText.setText("");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_supplyQtyKeyReleased

    private void supItemPriceTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supItemPriceTextKeyReleased
        try {
            Double a = Double.parseDouble(supItemPriceText.getText()) * Double.parseDouble(supplyQty.getText());
            totalText.setText(Double.toString(a));
        } catch (NumberFormatException e) {
            totalText.setText("");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_supItemPriceTextKeyReleased

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if (!checkBeforeAddBiid()) {
            if (!biidText.getText().isEmpty() && !iCodeText.getText().isEmpty() && !brandIdText.getText().isEmpty() && !widText.getText().isEmpty() && !supplyQty.getText().isEmpty() && !supItemPriceText.getText().isEmpty() && !totalText.getText().isEmpty()) {
                if (Integer.parseInt(supplyQty.getText()) != 0) {
                    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                    Object[] rowData = {biidText.getText(), itemDesCombo.getEditor().getItem().toString(), brandNameCombo.getEditor().getItem().toString(), warrentyperiodCombo.getEditor().getItem().toString(), supplyQty.getText(), supItemPriceText.getText(), totalText.getText()};
                    dtm.addRow(rowData);
                    grandTotal();
                } else {
                    JOptionPane.showMessageDialog(NewSupplyOrder.this, "'Supply Order Qty' Cannot Be Zero");
                }

            } else {
                JOptionPane.showMessageDialog(NewSupplyOrder.this, "Please Enter Valid Details");
            }
        } else {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, "Item Already Added");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_addButtonActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int a = table.getSelectedRow();
        if (a != -1) {
            DefaultTableModel dtm = (DefaultTableModel) table.getModel();
            dtm.removeRow(a);
            grandTotal();
        } else {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, "Please select a row to delete");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (!SOIDText.getText().isEmpty() && (table.getRowCount() != 0) && !supIdText.getText().isEmpty()) {
            String soid = SOIDText.getText();
            String supId = supIdText.getText();
            Date supDate = datePicker.getDate();
            Supplier sup = null;
            try {
                sup = SupplierController.searchSupplier(supId);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
            }

            Batch[] batch = new Batch[table.getRowCount()];

            for (int i = 0; i < table.getRowCount(); i++) {

                String biid = table.getValueAt(i, 0).toString();
                int supQty = Integer.parseInt(table.getValueAt(i, 4).toString());
                double supPrice = Double.parseDouble(table.getValueAt(i, 5).toString());
                batch[i] = new Batch("does not care auto genId adding", biid, supQty, supPrice);
            }

            if (sup != null) {
                SupplyOrder so = new SupplyOrder(soid, supId, supDate, batch);
                try {
                    int res = SupplyOrderController.addSupplyOrder(so);
                    if (res > 0) {
                        JOptionPane.showMessageDialog(NewSupplyOrder.this, "Saved...");
                        if (vso != null) {
                            vso.fillOrderTable();
                            vso.fillSupplierCombo();
                            vso.fillOidBySupId();
                        }
                    } else {
                        JOptionPane.showMessageDialog(NewSupplyOrder.this, "Error...");
                    }

                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
                    //System.out.println(ex.toString());
                }
            } else {
                JOptionPane.showMessageDialog(NewSupplyOrder.this, "Supplier not selected");
            }

        } else {
            JOptionPane.showMessageDialog(NewSupplyOrder.this, "Please Add Items To List");
        }

        /*        if (!SOIDText.getText().isEmpty() && (table.getRowCount() != 0) && !supIdText.getText().isEmpty()) {
            
         String soid = SOIDText.getText();
         String supId = supIdText.getText();
         SupplyOrder[] so = new SupplyOrder[table.getRowCount()];

         for (int i = 0; i < table.getRowCount(); i++) {

         String biid = table.getValueAt(i, 0).toString();
                
         String soid2 = SOIDText.getText();
         String supId2 = supIdText.getText();
         int supQty = Integer.parseInt(table.getValueAt(i, 4).toString());

         double supPrice = Double.parseDouble(table.getValueAt(i, 5).toString());

         so[i] = new SupplyOrder(soid2, supQty, biid, supPrice, supId2);
         }

         try {
         int res = SupplyOrderController.addSupplyOrderDetails(so);
         if (res > 0) {
         JOptionPane.showMessageDialog(NewSupplyOrder.this, "Saved...");
         } else {
         JOptionPane.showMessageDialog(NewSupplyOrder.this, "Error...");
         }

         } catch (SQLException | ClassNotFoundException ex) {
         JOptionPane.showMessageDialog(NewSupplyOrder.this, ex.getMessage());
         }

         } else {
         JOptionPane.showMessageDialog(NewSupplyOrder.this, "Please Add Items To List");
         }*/
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       
        if (m != null) {
            NewItem b = new NewItem(this);
            b.setVisible(true);
            m.desktopPane.add(b);
            m.desktopPane.setLayer(b, 1);
            m.desktopPane.repaint();
            m.tabPane.setSelectedIndex(1);
        } else {
        }      // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void newSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newSupplierButtonActionPerformed
        if (m != null) {
            ViewSupplier b = new ViewSupplier(this);
            b.setVisible(true);
            m.desktopPane.add(b);
            m.desktopPane.setLayer(b, 1);
            m.desktopPane.repaint();
            m.tabPane.setSelectedIndex(1);
        } else {
        }
// TODO add your handling code here:
    }//GEN-LAST:event_newSupplierButtonActionPerformed

    private void supItemPriceTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supItemPriceTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supItemPriceTextActionPerformed

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
            java.util.logging.Logger.getLogger(NewSupplyOrder.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewSupplyOrder.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewSupplyOrder.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewSupplyOrder.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewSupplyOrder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField SOIDText;
    private javax.swing.JButton addButton;
    private javax.swing.JTextField biidText;
    public javax.swing.JTextField brandIdText;
    public javax.swing.JComboBox brandNameCombo;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JTextField grandTotalText;
    public javax.swing.JTextField iCodeText;
    public javax.swing.JComboBox itemDesCombo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton newSupplierButton;
    public javax.swing.JTextField supIdText;
    private javax.swing.JTextField supItemPriceText;
    public javax.swing.JComboBox supplierCombo;
    private javax.swing.JTextField supplyQty;
    private javax.swing.JTable table;
    private javax.swing.JTextField totalText;
    public javax.swing.JComboBox warrentyperiodCombo;
    public javax.swing.JTextField widText;
    // End of variables declaration//GEN-END:variables
}
