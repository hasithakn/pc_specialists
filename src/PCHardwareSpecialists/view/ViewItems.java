/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.view;

import PCHardwareSpecialists.controller.BatchController;
import PCHardwareSpecialists.controller.BrandController;
import PCHardwareSpecialists.controller.BrandItemDetailsController;
import PCHardwareSpecialists.controller.CustomerController;
import PCHardwareSpecialists.controller.ItemController;
import PCHardwareSpecialists.controller.OtherSql;
import PCHardwareSpecialists.controller.SupplierController;
import PCHardwareSpecialists.controller.SupplyOrderBrandItemDetailsController;
import PCHardwareSpecialists.controller.SupplyOrderController;
import PCHardwareSpecialists.controller.WarrentyController;
import PCHardwareSpecialists.main.Main;
import PCHardwareSpecialists.model.Batch;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import PCHardwareSpecialists.model.Brand;
import PCHardwareSpecialists.model.BrandItemDetails;
import PCHardwareSpecialists.model.Customer;
import PCHardwareSpecialists.model.Item;
import PCHardwareSpecialists.model.SupplyOrder;
import PCHardwareSpecialists.model.Warrenty;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JInternalFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Hasithakn
 */
public class ViewItems extends javax.swing.JInternalFrame {

    Main m = null;

    /**
     * Creates new form ViewCustomer
     */
    public ViewItems() {
        initComponents();

        ///item des combo fill
        fillitemDesCombo();

        ///table fill
        fillItemTable();
        /* itemTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {

         try {
         int selectedRow = itemTable.getSelectedRow();
         String biid = itemTable.getValueAt(selectedRow, 1).toString();
         String bid = itemTable.getValueAt(selectedRow, 2).toString();
         String iCode = itemTable.getValueAt(selectedRow, 3).toString();
         String wid = itemTable.getValueAt(selectedRow, 4).toString();
         String qtyOnHand = itemTable.getValueAt(selectedRow, 5).toString();
         String itemPrice = itemTable.getValueAt(selectedRow, 0).toString();

         itemPriceText.setText(itemPrice);
         qtyOnHandText.setText(qtyOnHand);
         biidText.setText(biid);
         brandIdText.setText(bid);
         widText.setText(wid);
         itemCodeText.setText(iCode);

         //set selected combo box
         Warrenty warrenty = null;
         Brand brand = null;
         Item item = null;
         try {
         warrenty = WarrentyController.searchWarrenty(wid);
         brand = BrandController.searchBrand(bid);
         item = ItemController.searchItem(iCode);

         itemDescriptionCombo.getEditor().setItem(item.getDescription().toString());
         brandCombo.getEditor().setItem(brand.getBrandName().toString());
         warrentyPeriodCombo.getEditor().setItem(warrenty.getwPeriod().toString());

         } catch (ClassNotFoundException ex) {
         JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
         } catch (SQLException ex) {
         JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
         }

         } catch (Exception ex) {
         JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
         }
         }
         });*/
        //searchable combos

        AutoCompleteDecorator.decorate(itemDescriptionCombo);
        AutoCompleteDecorator.decorate(brandCombo);
        AutoCompleteDecorator.decorate(warrentyPeriodCombo);
    }

    public ViewItems(Main m) {
        initComponents();
        this.m = m;
        ///item des combo fill
        fillitemDesCombo();

        ///table fill
        fillItemTable();
        AutoCompleteDecorator.decorate(itemDescriptionCombo);
        AutoCompleteDecorator.decorate(brandCombo);
        AutoCompleteDecorator.decorate(warrentyPeriodCombo);
    }

    public void fillitemDesCombo() {
        ArrayList<Item> item = null;
        try {
            item = ItemController.getAllItem();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        }
        itemDescriptionCombo.removeAllItems();
        for (Item item1 : item) {
            itemDescriptionCombo.addItem(item1.getDescription());
        }
    }

    public void fillbrandCombo() {
        ArrayList<Brand> brand = null;
        try {
            brand = BrandController.getAllBrand();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        }
        brandCombo.removeAllItems();
        for (Brand brand1 : brand) {
            brandCombo.addItem(brand1.getBrandName());
        }
    }

    public void fillbrandComboByItem() {
        String description = itemDescriptionCombo.getEditor().getItem().toString();
        ArrayList<Item> item2 = null;
        if (!description.isEmpty() || description != null) {
            brandCombo.removeAllItems();
            try {
                item2 = ItemController.searchItemByname(description);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
            }
            if (item2 != null) {
                for (Item item : item2) {
                    ArrayList<Brand> brands = null;
                    try {
                        brands = BrandItemDetailsController.searchBidFromBrandItemDetailsWhereIcode(item.getiCode());
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
                    } catch (NullPointerException ex) {
                        JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage() + "ss");
                    }
                    if (brands != null) {
                        for (Brand brand : brands) {
                            Brand brandByBid = null;
                            try {
                                brandByBid = BrandController.searchBrand(brand.getBid());
                            } catch (ClassNotFoundException ex) {
                                JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
                            }
                            if (brandByBid != null) {
                                brandCombo.addItem(brandByBid.getBrandName());
                            }
                            try {
                                brandCombo.setSelectedIndex(0);
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
                JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
            }
            brandCombo.removeAllItems();
            for (Brand brand1 : brand) {
                brandCombo.addItem(brand1.getBrandName());
            }
            try {
                brandCombo.setSelectedIndex(0);
            } catch (IllegalArgumentException ex) {

            }
        }

    }

    public void fillwarrentyDesComboByBrand() {
        warrentyPeriodCombo.removeAllItems();
        String iCode = itemCodeText.getText();
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
                        warrentyPeriodCombo.addItem(ww.getwPeriod());
                    }
                    try {
                        warrentyPeriodCombo.setSelectedIndex(0);
                    } catch (IllegalArgumentException ex) {
                    }

                }
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
            }
        }
    }

    public void fillwarrentyDesCombo() {
        ArrayList<Warrenty> warrenty = null;
        try {
            warrenty = WarrentyController.getAllWarrenty();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        }
        warrentyPeriodCombo.removeAllItems();
        for (Warrenty warrenty1 : warrenty) {
            warrentyPeriodCombo.addItem(warrenty1.getwPeriod());
        }
        try {
            warrentyPeriodCombo.setSelectedIndex(0);
        } catch (IllegalArgumentException ex) {
        }

    }

    public void getBiidDetails() {
        BrandItemDetails brandItemDetails = null;
        if (itemCodeText.getText().isEmpty() || brandIdText.getText().isEmpty() || widText.getText().isEmpty()) {
            biidText.setText("");
            itemPriceText.setText("");
            qtyOnHandText.setText("");
        } else {
            try {
                brandItemDetails = BrandItemDetailsController.searchBrandItemDetails(itemCodeText.getText(), brandIdText.getText(), widText.getText());
            } catch (ClassNotFoundException | SQLException ex) {
                JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
            }
            if (brandItemDetails != null) {
                biidText.setText(brandItemDetails.getBiid());
                itemPriceText.setText(Double.toString(brandItemDetails.getSellingPrice()));
                //batch table
                ArrayList<Batch> batchList = null;
                try {
                    batchList = BatchController.searchBatchesByBIID(biidText.getText());
                } catch (ClassNotFoundException | SQLException ex) {
                    JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
                }
                int a = 0;
                ArrayList<Batch> batches = null;
                try {
                    batches = BatchController.searchBatchesByBIID(brandItemDetails.getBiid());
                } catch (ClassNotFoundException | SQLException ex) {
                    JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
                }
                if (batches != null) {
                    for (Batch batch : batches) {
                        a = a + batch.getQty();
                    }
                }
                qtyOnHandText.setText(Integer.toString(a));
                int a1 = itemTable.getRowCount();
                int b = 0;
                String text = biidText.getText();
                for (int i = 0; i < a1; i++) {
                    if (itemTable.getValueAt(i, 1).toString().equals(text)) {
                        break;
                    } else {
                        b++;
                    }
                }
                if (!biidText.getText().isEmpty()) {
                    ListSelectionModel selectionModel = itemTable.getSelectionModel();
                    selectionModel.setSelectionInterval(0, b);
                } else {
                    ListSelectionModel selectionModel = itemTable.getSelectionModel();
                    selectionModel.setSelectionInterval(0, itemTable.getRowCount());
                }

                //totalAndOrderQtyTextClear();
            } else {
                biidText.setText("");
                itemPriceText.setText("");
                qtyOnHandText.setText("");
                //totalAndOrderQtyTextClear();
            }

        }
    }

    public void fillItemTable() {
        ArrayList<BrandItemDetails> brandItemDetailsList = null;
        try {
            brandItemDetailsList = BrandItemDetailsController.getAllBrandItemDetails();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        }
        DefaultTableModel dtm = (DefaultTableModel) itemTable.getModel();
        dtm.setRowCount(0);
        try {
            for (BrandItemDetails brandItemDetails : brandItemDetailsList) {
                int a = 0;
                ArrayList<Batch> batches = null;
                try {
                    batches = BatchController.searchBatchesByBIID(brandItemDetails.getBiid());
                } catch (ClassNotFoundException | SQLException ex) {
                    JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
                }
                if (batches != null) {
                    for (Batch batch : batches) {
                        a = a + batch.getQty();
                    }
                }

                String itemDes = ItemController.searchItem(brandItemDetails.getiCode()).getDescription();
                String brandName = BrandController.searchBrand(brandItemDetails.getBid()).getBrandName();
                String warrentyPeriod = WarrentyController.searchWarrenty(brandItemDetails.getWid()).getwPeriod();

                Object[] rowData = {brandItemDetails.getSellingPrice(), brandItemDetails.getBiid(), brandItemDetails.getBid(), brandItemDetails.getiCode(), brandItemDetails.getWid(), a, itemDes, brandName, warrentyPeriod};
                dtm.addRow(rowData);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        }
        ListSelectionModel selectionModel = itemTable.getSelectionModel();

        selectionModel.setSelectionInterval(0, 0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        cancelButton = new javax.swing.JButton();
        saveChangesButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        itemDescriptionCombo = new javax.swing.JComboBox();
        itemCodeText = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        brandCombo = new javax.swing.JComboBox();
        brandIdText = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        warrentyPeriodCombo = new javax.swing.JComboBox();
        widText = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        biidText = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        itemPriceText = new javax.swing.JTextField();
        qtyOnHandText = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        itemPriceChange = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        saveChangesButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Items");

        itemTable.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Price", "BIID", "BID", "Item Code", "WID", "Qty On Hand", "Item Des.", "Brand", "Warrenty"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        itemTable.getTableHeader().setReorderingAllowed(false);
        itemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemTableMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                itemTableMouseReleased(evt);
            }
        });
        itemTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                itemTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(itemTable);

        cancelButton.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        saveChangesButton.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        saveChangesButton.setText("Save Changes");
        saveChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveChangesButtonActionPerformed(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Item"));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel20.setText("Item Code");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel21.setText("Item Description");

        itemDescriptionCombo.setEditable(true);
        itemDescriptionCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        itemDescriptionCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemDescriptionComboActionPerformed(evt);
            }
        });

        itemCodeText.setEditable(false);
        itemCodeText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel22.setText("Brand");

        brandCombo.setEditable(true);
        brandCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        brandCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brandComboActionPerformed(evt);
            }
        });

        brandIdText.setEditable(false);
        brandIdText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel23.setText("Brand ID");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel24.setText("Warrenty Period");

        warrentyPeriodCombo.setEditable(true);
        warrentyPeriodCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        warrentyPeriodCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                warrentyPeriodComboActionPerformed(evt);
            }
        });

        widText.setEditable(false);
        widText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel25.setText("WID");

        biidText.setEditable(false);
        biidText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel26.setText("BIID");

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel27.setText("Selling Price");

        itemPriceText.setEditable(false);
        itemPriceText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        qtyOnHandText.setEditable(false);
        qtyOnHandText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel28.setText("Qty On Hand");

        addButton.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        addButton.setText("ADD New");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        itemPriceChange.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        itemPriceChange.setText("Change");
        itemPriceChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemPriceChangeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addGap(40, 40, 40)
                                .addComponent(qtyOnHandText, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(itemCodeText, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(brandIdText, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                                .addComponent(widText))
                            .addComponent(addButton)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(warrentyPeriodCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(brandCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(itemDescriptionCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addGap(54, 54, 54)
                                .addComponent(itemPriceText, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(itemPriceChange)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(biidText, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(21, 21, 21))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(itemDescriptionCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(itemCodeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(brandCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(brandIdText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(warrentyPeriodCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(widText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemPriceText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(biidText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(itemPriceChange))
                .addGap(16, 16, 16)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton)
                    .addComponent(qtyOnHandText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        refreshButton.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        saveChangesButton1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        saveChangesButton1.setText("View Batches");
        saveChangesButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveChangesButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(refreshButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saveChangesButton1)
                        .addGap(18, 18, 18)
                        .addComponent(saveChangesButton)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton))
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(saveChangesButton)
                    .addComponent(refreshButton)
                    .addComponent(saveChangesButton1))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemDescriptionComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemDescriptionComboActionPerformed
        fillbrandComboByItem();
        fillwarrentyDesComboByBrand();
        //remove itmes in warrenty combo
        //warrentyPeriodCombo.removeAllItems();
        //fill icode text
        String description = itemDescriptionCombo.getEditor().getItem().toString();
        ArrayList<Item> item = null;
        try {
            item = ItemController.searchItemByname(description);
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        }
        try {
            itemCodeText.setText(item.get(0).getiCode());
        } catch (IndexOutOfBoundsException e) {
        }
        //chech & get biid details
        getBiidDetails();

    }//GEN-LAST:event_itemDescriptionComboActionPerformed

    private void brandComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brandComboActionPerformed
        fillwarrentyDesComboByBrand();

        //fill bid text
        String brand = brandCombo.getEditor().getItem().toString();
        ArrayList<Brand> brand2 = null;
        try {
            brand2 = BrandController.searchBrandByName(brand);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        }
        try {
            if (!brandCombo.getEditor().getItem().toString().isEmpty()) {
                brandIdText.setText(brand2.get(0).getBid());
            }

        } catch (IndexOutOfBoundsException e) {
        }
        //chech & get biid details
        getBiidDetails();

    }//GEN-LAST:event_brandComboActionPerformed

    private void warrentyPeriodComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_warrentyPeriodComboActionPerformed
        //fill wid text

        String warrenty = warrentyPeriodCombo.getEditor().getItem().toString();
        ArrayList<Warrenty> warrenty2 = null;
        try {
            warrenty2 = WarrentyController.searchWarrentyByName(warrenty);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
        }
        try {
            if (!warrentyPeriodCombo.getEditor().getItem().toString().isEmpty()) {
                widText.setText(warrenty2.get(0).getWid());
                //warrentyPeriodText.setText(warrenty2.get(0).getwPeriod());
            }
        } catch (IndexOutOfBoundsException e) {

        }
        //chech & get biid details
        getBiidDetails();
    }//GEN-LAST:event_warrentyPeriodComboActionPerformed

    private void itemTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemTableMouseReleased

    }//GEN-LAST:event_itemTableMouseReleased

    private void itemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemTableMouseClicked
        //show text fields
        try {
            int selectedRow = itemTable.getSelectedRow();
            String biid = itemTable.getValueAt(selectedRow, 1).toString();
            String bid = itemTable.getValueAt(selectedRow, 2).toString();
            String iCode = itemTable.getValueAt(selectedRow, 3).toString();
            String wid = itemTable.getValueAt(selectedRow, 4).toString();
            String qtyOnHand = itemTable.getValueAt(selectedRow, 5).toString();
            String itemPrice = itemTable.getValueAt(selectedRow, 0).toString();

            itemPriceText.setText(itemPrice);
            qtyOnHandText.setText(qtyOnHand);
            biidText.setText(biid);
            brandIdText.setText(bid);
            widText.setText(wid);
            itemCodeText.setText(iCode);

            //set selected combo box
            Warrenty warrenty = null;
            Brand brand = null;
            Item item = null;
            try {
                warrenty = WarrentyController.searchWarrenty(wid);
                brand = BrandController.searchBrand(bid);
                item = ItemController.searchItem(iCode);

                itemDescriptionCombo.getEditor().setItem(item.getDescription().toString());
                brandCombo.getEditor().setItem(brand.getBrandName().toString());
                warrentyPeriodCombo.getEditor().setItem(warrenty.getwPeriod().toString());

            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_itemTableMouseClicked

    private void itemTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemTableKeyPressed

    }//GEN-LAST:event_itemTableKeyPressed

    private void itemTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemTableKeyReleased

    }//GEN-LAST:event_itemTableKeyReleased

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // combo fill
        fillitemDesCombo();
        //clear txt fields
        itemCodeText.setText("");
        brandIdText.setText("");
        widText.setText("");
        itemPriceText.setText("");
        qtyOnHandText.setText("");
        ///table fill
        fillItemTable();
        ListSelectionModel selectionModel = itemTable.getSelectionModel();
        selectionModel.setSelectionInterval(0, itemTable.getRowCount());

    }//GEN-LAST:event_refreshButtonActionPerformed

    private void itemPriceChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemPriceChangeActionPerformed

        String a = JOptionPane.showInputDialog(ViewItems.this, "New Item Price");
        if (!a.isEmpty()) {
            itemPriceText.setText(a);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_itemPriceChangeActionPerformed

    private void saveChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveChangesButtonActionPerformed

        if (!biidText.getText().isEmpty() && !itemPriceText.getText().isEmpty()) {
            //BrandItemDetails brandItemDetail = new BrandItemDetails(Double.parseDouble(itemPriceText.getText()), biidText.getText(), Integer.parseInt(qtyOnHandText.getText()), widText.getText(), brandIdText.getText(), itemCodeText.getText());
            BrandItemDetails brandItemDetail = new BrandItemDetails(biidText.getText(), widText.getText(), brandIdText.getText(), itemCodeText.getText(), Double.parseDouble(itemPriceText.getText()));
            int a = 0;
            try {
                a = BrandItemDetailsController.updateBrandItemDetails(brandItemDetail);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewItems.this, ex.getMessage());
            }
            if (a > 0) {
                JOptionPane.showMessageDialog(ViewItems.this, "Changes Saved");
                int aa = itemTable.getSelectedRow();
                //fable fill
                fillItemTable();
                ListSelectionModel selectionModel = itemTable.getSelectionModel();
                selectionModel.setSelectionInterval(aa - 1, aa);

            } else {
                JOptionPane.showMessageDialog(ViewItems.this, "Changes Updating Failed");
            }
        } else {
            JOptionPane.showMessageDialog(ViewItems.this, "Fields Are Empty");
        }


    }//GEN-LAST:event_saveChangesButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if (m != null) {
            //this.setEnabled(false);
            NewItem b = new NewItem();
            b.setVisible(true);
            //m.desktopPane.removeAll();
            m.desktopPane.add(b);
            m.desktopPane.setLayer(b, 1);
            m.tabPane.setSelectedIndex(1);

        }

    }//GEN-LAST:event_addButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void saveChangesButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveChangesButton1ActionPerformed
        ViewBatches a = new ViewBatches(this);
        a.setVisible(true);// TODO add your handling code here:
    }//GEN-LAST:event_saveChangesButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(ViewItems.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewItems.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewItems.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewItems.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewItems().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    public javax.swing.JTextField biidText;
    private javax.swing.JComboBox brandCombo;
    private javax.swing.JTextField brandIdText;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField itemCodeText;
    private javax.swing.JComboBox itemDescriptionCombo;
    private javax.swing.JButton itemPriceChange;
    private javax.swing.JTextField itemPriceText;
    public javax.swing.JTable itemTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField qtyOnHandText;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton saveChangesButton;
    private javax.swing.JButton saveChangesButton1;
    private javax.swing.JComboBox warrentyPeriodCombo;
    private javax.swing.JTextField widText;
    // End of variables declaration//GEN-END:variables
}
