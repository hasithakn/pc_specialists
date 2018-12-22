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
import PCHardwareSpecialists.controller.IdGen;
import PCHardwareSpecialists.controller.ItemController;
import PCHardwareSpecialists.controller.OrderBrandItemDetailsController;
import PCHardwareSpecialists.controller.OrdersController;
import PCHardwareSpecialists.controller.OtherSql;
import PCHardwareSpecialists.controller.SupplierController;
import PCHardwareSpecialists.controller.SupplyOrderBrandItemDetailsController;
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
import PCHardwareSpecialists.model.SupplyOrder;
import PCHardwareSpecialists.model.Warrenty;
import PCHardwareSpecialists.view.NewItem;
import PCHardwareSpecialists.view.ViewCustomer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Hasithakn
 */
public class Invoice extends javax.swing.JInternalFrame {

    Main m = null;

    /**
     * Creates new form Invoice
     */
    public Invoice() {

        initComponents();
        setTitle("Invoice");
        //oid genarater
        oidGen();
        //fill cusomercombo
        fillCustomerCombo();
        //fill itemCombo
        fillItemDesCombo();
        batchTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                try {
                    qtyOnHandText.setText(batchTable.getValueAt(batchTable.getSelectedRow(), 1).toString());
                } catch (ArrayIndexOutOfBoundsException ex) {
                }
            }
        }
        );
        //searchable combos
        AutoCompleteDecorator.decorate(itemDesCombo);
        AutoCompleteDecorator.decorate(brandCombo);
        AutoCompleteDecorator.decorate(customerCombo);
    }

    public Invoice(Main m) {
        initComponents();
        this.m = m;
        setTitle("Invoice");
        //oid genarater
        oidGen();
        //fill cusomercombo
        fillCustomerCombo();
        //fill itemCombo
        fillItemDesCombo();
        batchTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                try {
                    qtyOnHandText.setText(batchTable.getValueAt(batchTable.getSelectedRow(), 1).toString());
                } catch (ArrayIndexOutOfBoundsException ex) {
                }

            }
        }
        );
        //searchable combos
        AutoCompleteDecorator.decorate(itemDesCombo);

        AutoCompleteDecorator.decorate(brandCombo);

        AutoCompleteDecorator.decorate(customerCombo);

    }

    public void oidGen() {
        try {
            oidText.setText(IdGen.getNextId("orders", "oid", "OI"));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
        }
    }

    public void fillbrandComboByItem() {
        String description = itemDesCombo.getEditor().getItem().toString();
        ArrayList<Item> item2 = null;
        if (!description.isEmpty() || description != null) {
            brandCombo.removeAllItems();
            try {
                item2 = ItemController.searchItemByname(description);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
            }
            if (item2 != null) {
                for (Item item : item2) {
                    ArrayList<Brand> brands = null;
                    try {
                        brands = BrandItemDetailsController.searchBidFromBrandItemDetailsWhereIcode(item.getiCode());
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
                    } catch (NullPointerException ex) {
                        JOptionPane.showMessageDialog(Invoice.this, ex.getMessage() + "ss");
                    }
                    if (brands != null) {
                        for (Brand brand : brands) {
                            Brand brandByBid = null;
                            try {
                                brandByBid = BrandController.searchBrand(brand.getBid());
                            } catch (ClassNotFoundException ex) {
                                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
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
                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
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

    public void grandTotal() {
        Double a = 0.0;
        for (int i = 0; i < table.getRowCount(); i++) {
            a = a + Double.parseDouble(table.getValueAt(i, 6).toString());
        }
        if (!discountText.getText().isEmpty()) {
            try {
                Double b = 100.0 - Double.parseDouble(discountText.getText());
                b = (b * a) / 100;
                grandTotalText.setText(b.toString());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage() + " Wrong Input");
            }
        } else {
            grandTotalText.setText(a.toString());
        }

    }

    public boolean checkBeforeAddBatch() {
        String a = batchTable.getValueAt(batchTable.getSelectedRow(), 0).toString();
        boolean b = false;
        for (int i = 0; i < table.getRowCount(); i++) {
            String a1 = table.getValueAt(i, 7).toString();
            if (a1.equals(a)) {
                b = true;
                break;
            } else {
                b = false;
            }
        }
        return b;
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

    public void fillCustomerDetailsByName() {
        ArrayList<Customer> customerlist = null;
        if (!customerCombo.getEditor().getItem().toString().isEmpty()) {
            try {
                customerlist = CustomerController.searchCustomerByName(customerCombo.getEditor().getItem().toString());
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
            }
            Customer[] customer = new Customer[customerlist.size()];
            for (int i = 0; i < customerlist.size(); i++) {
                customer[i] = customerlist.get(i);
            }
            if (customerlist.size() == 1 && customer != null) {
                customerIdCombo.removeAllItems();
                customerIdCombo.addItem(customer[0].getCustId());
                nicText.setText(customer[0].getNic());
                addressText.setText(customer[0].getAddress());
                emailText.setText(customer[0].getCustEmail());
                telText.setText(Integer.toString(customer[0].getTel()));
            } else {
                customerIdCombo.removeAllItems();
                for (int i = 0; i < customerlist.size(); i++) {
                    customerIdCombo.addItem(customer[i].getCustId());
                }

            }
        }
    }

    public void fillwarrentyDesCombo() {
        ArrayList<Warrenty> warrenty = null;
        try {
            warrenty = WarrentyController.getAllWarrenty();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
        }
        warrentyperiodCombo.removeAllItems();
        for (Warrenty warrenty1 : warrenty) {
            warrentyperiodCombo.addItem(warrenty1.getwPeriod());
        }
        try {
            warrentyperiodCombo.setSelectedIndex(0);
        } catch (IllegalArgumentException ex) {
        }
    }

    public void totalAndOrderQtyTextClear() {
        totalText.setText("");
        orderQtyText.setText("");
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
                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
            }
        }
    }

    public void fillCustomerDetailsById() {
        Customer customer = null;
        if (!customerIdCombo.getEditor().getItem().toString().isEmpty()) {
            try {
                customer = CustomerController.searchCustomer(customerIdCombo.getEditor().getItem().toString());
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
            }
            if (customer != null) {
                customerCombo.getEditor().setItem(customer.getCustName());
                nicText.setText(customer.getNic());
                addressText.setText(customer.getAddress());
                emailText.setText(customer.getCustEmail());
                telText.setText(Integer.toString(customer.getTel()));
            }
        }
    }

    public void fillItemDesCombo() {
        ArrayList<Item> item = null;
        try {
            item = ItemController.getAllItem();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
        }
        itemDesCombo.removeAllItems();
        for (Item item1 : item) {
            itemDesCombo.addItem(item1.getDescription());
        }
    }

    public void getBiidDetails() {
        BrandItemDetails brandItemDetails = null;
        if (iCodeText.getText().isEmpty() || brandIdText.getText().isEmpty() || widText.getText().isEmpty()) {
            biidText.setText("");
            itemPriceText.setText("");
            qtyOnHandText.setText("");
            totalAndOrderQtyTextClear();
        } else {
            try {
                brandItemDetails = BrandItemDetailsController.searchBrandItemDetails(iCodeText.getText(), brandIdText.getText(), widText.getText());
            } catch (ClassNotFoundException | SQLException ex) {
                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
            }
            if (brandItemDetails != null) {
                biidText.setText(brandItemDetails.getBiid());
                itemPriceText.setText(Double.toString(brandItemDetails.getSellingPrice()));
                //batch table
                ArrayList<Batch> batchList = null;
                try {
                    batchList = BatchController.searchBatchesByBIID(biidText.getText());
                } catch (ClassNotFoundException | SQLException ex) {
                    JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
                }
                DefaultTableModel dtm = (DefaultTableModel) batchTable.getModel();
                dtm.setRowCount(0);
                if (batchList != null) {
                    for (Batch batch : batchList) {
                        try {
                            String soid = SupplyOrderBrandItemDetailsController.searchSupplyOrderBrandItemDetail(batch.getBatchId()).getSoid();
                            SupplyOrder so = SupplyOrderController.searchSupplyOrder(soid);
                            String supName = SupplierController.searchSupplier(so.getSupId()).getSupName();
                            Object[] rowData = {batch.getBatchId(), batch.getQty(), supName};
                            dtm.addRow(rowData);
                        } catch (ClassNotFoundException | SQLException ex) {
                            Object[] rowData = {batch.getBatchId(), batch.getQty(), "--"};
                            dtm.addRow(rowData);
                        } catch (NullPointerException ex) {
                            Object[] rowData = {batch.getBatchId(), batch.getQty(), "--"};
                            dtm.addRow(rowData);
                        }
                    }
                }

                int a = 0;
                ArrayList<Batch> batches = null;
                try {
                    batches = BatchController.searchBatchesByBIID(brandItemDetails.getBiid());
                } catch (ClassNotFoundException | SQLException ex) {
                    JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
                }
                if (batches != null) {
                    for (Batch batch : batches) {
                        a = a + batch.getQty();
                    }
                }
                qtyOnHandText.setText(Integer.toString(a));
                totalAndOrderQtyTextClear();
            } else {
                biidText.setText("");
                itemPriceText.setText("");
                qtyOnHandText.setText("");
                totalAndOrderQtyTextClear();
            }

        }
    }

    public void fillCustomerCombo() {
        customerCombo.removeAllItems();
        ArrayList<Customer> customerList = null;
        try {
            customerList = CustomerController.getAllCustomer();

        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
        }
        for (Customer customer : customerList) {
            customerCombo.addItem(customer.getCustName());
            AutoCompleteDecorator.decorate(customerCombo);
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        nicText = new javax.swing.JTextField();
        addressText = new javax.swing.JTextField();
        emailText = new javax.swing.JTextField();
        telText = new javax.swing.JTextField();
        customerCombo = new javax.swing.JComboBox();
        newCustomerBtn = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        customerIdCombo = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        itemDesCombo = new javax.swing.JComboBox();
        iCodeText = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        brandCombo = new javax.swing.JComboBox();
        brandIdText = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        warrentyperiodCombo = new javax.swing.JComboBox();
        widText = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        biidText = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        itemPriceText = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        qtyOnHandText = new javax.swing.JTextField();
        orderQtyText = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        totalText = new javax.swing.JTextField();
        addBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        batchTable = new javax.swing.JTable();
        jCheckBox1 = new javax.swing.JCheckBox();
        removeBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        discountText = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        jLabel21 = new javax.swing.JLabel();
        oidText = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        grandTotalText = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();

        setTitle("Invoice");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Invoice");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Details"));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel2.setText("Customer ID");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel3.setText("Customer Name");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel4.setText("Customer Address");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setText("Customer Nic");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel6.setText("Customer Email");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel7.setText("Customer Contact No.");

        nicText.setEditable(false);
        nicText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        nicText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nicTextActionPerformed(evt);
            }
        });

        addressText.setEditable(false);
        addressText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        emailText.setEditable(false);
        emailText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        emailText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextActionPerformed(evt);
            }
        });

        telText.setEditable(false);
        telText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        customerCombo.setEditable(true);
        customerCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        customerCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerComboActionPerformed(evt);
            }
        });

        newCustomerBtn.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        newCustomerBtn.setText("New Customer");
        newCustomerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCustomerBtnActionPerformed(evt);
            }
        });

        refreshBtn.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        refreshBtn.setText("Refresh List");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        customerIdCombo.setEditable(true);
        customerIdCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        customerIdCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerIdComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerIdCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(nicText)
                            .addComponent(refreshBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(newCustomerBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(addressText, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(emailText, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(telText, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 292, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(customerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newCustomerBtn)
                    .addComponent(refreshBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(nicText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerIdCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(addressText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(emailText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(telText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BIID", "Description", "Brand", "Warrenty ", "Order Qty", "Unit Price", "Total", "Batch ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

        brandCombo.setEditable(true);
        brandCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        brandCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brandComboActionPerformed(evt);
            }
        });

        brandIdText.setEditable(false);
        brandIdText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel12.setText("Brand ID");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel16.setText("Warrenty ");

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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemDesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(brandCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(warrentyperiodCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(brandIdText, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                            .addComponent(widText)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iCodeText, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(iCodeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(itemDesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(brandCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(brandIdText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(widText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(warrentyperiodCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Item Details"));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel10.setText("BIID");

        biidText.setEditable(false);
        biidText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel13.setText("Item Price");

        itemPriceText.setEditable(false);
        itemPriceText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel14.setText("Qty On Hand");

        qtyOnHandText.setEditable(false);
        qtyOnHandText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        orderQtyText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        orderQtyText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderQtyTextActionPerformed(evt);
            }
        });
        orderQtyText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                orderQtyTextKeyReleased(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel17.setText("Order Qty .");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setText("Total");

        totalText.setEditable(false);
        totalText.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        addBtn.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        addBtn.setText("ADD");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        batchTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BatchID", "Qty Left", "Supplier"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        batchTable.setEnabled(false);
        batchTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        batchTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(batchTable);

        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jCheckBox1.setText("ADD By Manually");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel10))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(biidText)
                            .addComponent(itemPriceText, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(qtyOnHandText, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                                .addComponent(orderQtyText)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox1))
                        .addGap(0, 12, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(biidText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(itemPriceText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(qtyOnHandText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(orderQtyText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        removeBtn.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        removeBtn.setText("Remove");
        removeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtnActionPerformed(evt);
            }
        });

        saveBtn.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        cancelBtn.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cancelBtn.setText("Cancal");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        discountText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        discountText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        discountText.setText("0");
        discountText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                discountTextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                discountTextKeyReleased(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel19.setText("Discounts");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Order Details"));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel20.setText("Order ID");

        datePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datePickerActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel21.setText("Date");

        oidText.setEditable(false);
        oidText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oidText)
                    .addComponent(datePicker, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oidText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel22.setText("Grand Total");

        grandTotalText.setEditable(false);
        grandTotalText.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel23.setText("%");

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
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(discountText, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23)
                        .addGap(53, 53, 53)
                        .addComponent(jLabel22)
                        .addGap(18, 18, 18)
                        .addComponent(grandTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(discountText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grandTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
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

    private void emailTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextActionPerformed

    private void newCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCustomerBtnActionPerformed
        if (m != null) {
            ViewCustomer b = new ViewCustomer(this);
            b.setVisible(true);
            m.desktopPane.add(b);
            m.desktopPane.setLayer(b, 1);
            m.tabPane.setSelectedIndex(1);
        }
    }//GEN-LAST:event_newCustomerBtnActionPerformed

    private void customerComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerComboActionPerformed
        fillCustomerDetailsByName();
    }//GEN-LAST:event_customerComboActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        fillCustomerCombo();
        // set last item as selected customer combo
        customerCombo.setSelectedIndex(customerCombo.getItemCount() - 1);
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void customerIdComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerIdComboActionPerformed
        fillCustomerDetailsById();        // TODO add your handling code here:
    }//GEN-LAST:event_customerIdComboActionPerformed

    private void nicTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nicTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nicTextActionPerformed

    private void itemDesComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemDesComboActionPerformed
        fillbrandComboByItem();
        fillwarrentyDesComboByBrand();

        String description = itemDesCombo.getEditor().getItem().toString();
        ArrayList<Item> item = null;
        try {
            item = ItemController.searchItemByname(description);
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
        }
        try {
            iCodeText.setText(item.get(0).getiCode());
        } catch (IndexOutOfBoundsException ex) {
        }

        getBiidDetails();
    }//GEN-LAST:event_itemDesComboActionPerformed

    private void brandComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brandComboActionPerformed
        fillwarrentyDesComboByBrand();
        String brand = brandCombo.getEditor().getItem().toString();
        ArrayList<Brand> brandList = null;
        try {
            brandList = BrandController.searchBrandByName(brand);
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
        }
        try {
            brandIdText.setText(brandList.get(0).getBid());
        } catch (IndexOutOfBoundsException e) {
        }
        getBiidDetails();
    }//GEN-LAST:event_brandComboActionPerformed

    private void warrentyperiodComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_warrentyperiodComboActionPerformed
        String warrenty = warrentyperiodCombo.getEditor().getItem().toString();
        ArrayList<Warrenty> warrenty2 = null;
        try {
            warrenty2 = WarrentyController.searchWarrentyByName(warrenty);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
        }
        try {
            if (!warrentyperiodCombo.getEditor().getItem().toString().isEmpty()) {
                widText.setText(warrenty2.get(0).getWid());
            }
        } catch (IndexOutOfBoundsException e) {

        }
        getBiidDetails();
    }//GEN-LAST:event_warrentyperiodComboActionPerformed

    private void orderQtyTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderQtyTextActionPerformed
        try {
            Double a = Double.parseDouble(itemPriceText.getText()) * Double.parseDouble(orderQtyText.getText());
            totalText.setText(Double.toString(a));
        } catch (NumberFormatException e) {
            totalText.setText("");
        }

    }//GEN-LAST:event_orderQtyTextActionPerformed

    private void orderQtyTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_orderQtyTextKeyReleased
        try {
            Double a = Double.parseDouble(itemPriceText.getText()) * Double.parseDouble(orderQtyText.getText());
            totalText.setText(Double.toString(a));
        } catch (NumberFormatException e) {
            totalText.setText("");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_orderQtyTextKeyReleased

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        if (!jCheckBox1.isSelected()) {
            if (!checkBeforeAddBiid()) {
                if (!biidText.getText().isEmpty() && !iCodeText.getText().isEmpty() && !brandIdText.getText().isEmpty() && !widText.getText().isEmpty() && !orderQtyText.getText().isEmpty() && !itemPriceText.getText().isEmpty() && !totalText.getText().isEmpty()) {
                    if (Integer.parseInt(orderQtyText.getText()) <= Integer.parseInt(qtyOnHandText.getText())) {
                        if (Integer.parseInt(orderQtyText.getText()) != 0) {
                            //
                            String sql = "select batchId,qty from batch where biid='" + biidText.getText() + "'";
                            ResultSet rst;
                            ArrayList<Batch> batchList = null;
                            try {
                                batchList = BatchController.searchBatchesByBIID(biidText.getText());
                            } catch (ClassNotFoundException ex) {
                                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
                            }
                            int oQty = Integer.parseInt(orderQtyText.getText());
                            for (Batch batch : batchList) {
                                if (oQty <= batch.getQty()) {
                                    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                                    Object[] rowData = {biidText.getText(), itemDesCombo.getEditor().getItem().toString(), brandCombo.getEditor().getItem().toString(), warrentyperiodCombo.getEditor().getItem().toString(), oQty, itemPriceText.getText(), Double.parseDouble(itemPriceText.getText()) * Double.parseDouble(Integer.toString(oQty)), batch.getBatchId()};
                                    dtm.addRow(rowData);
                                    grandTotal();
                                    oQty = 0;
                                    break;
                                } else if (batch.getQty() > 0) {
                                    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                                    Object[] rowData = {biidText.getText(), itemDesCombo.getEditor().getItem().toString(), brandCombo.getEditor().getItem().toString(), warrentyperiodCombo.getEditor().getItem().toString(),batch.getQty(), itemPriceText.getText(), Double.parseDouble(itemPriceText.getText()) * batch.getQty(), batch.getBatchId()};
                                    dtm.addRow(rowData);
                                    grandTotal();
                                    oQty = oQty - batch.getQty();

                                }
                            }

                            //
                        } else {
                            JOptionPane.showMessageDialog(Invoice.this, "'Order Qty' Cannot Be Zero");
                        }

                    } else {
                        JOptionPane.showMessageDialog(Invoice.this, "'Order Qty' Cannot Be Greater Than 'Qty On Hand'");
                    }

                } else {
                    JOptionPane.showMessageDialog(Invoice.this, "Please Enter Valid Details");
                }
            } else {
                JOptionPane.showMessageDialog(Invoice.this, "Item Already Added");
            }

        } else {
            try {
                if (!checkBeforeAddBatch()) {
                    if (!biidText.getText().isEmpty() && !iCodeText.getText().isEmpty() && !brandIdText.getText().isEmpty() && !widText.getText().isEmpty() && !orderQtyText.getText().isEmpty() && !itemPriceText.getText().isEmpty() && !totalText.getText().isEmpty()) {
                        if (Integer.parseInt(orderQtyText.getText()) <= Integer.parseInt(qtyOnHandText.getText())) {
                            if (Integer.parseInt(orderQtyText.getText()) != 0) {
                                int oQty = Integer.parseInt(orderQtyText.getText());
                                try {
                                    if (oQty <= Integer.parseInt(batchTable.getValueAt(batchTable.getSelectedRow(), 1).toString())) {
                                        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                                        Object[] rowData = {biidText.getText(), itemDesCombo.getEditor().getItem().toString(), brandCombo.getEditor().getItem().toString(), warrentyperiodCombo.getEditor().getItem().toString(), oQty, itemPriceText.getText(), Double.parseDouble(itemPriceText.getText()) * Double.parseDouble(Integer.toString(oQty)), batchTable.getValueAt(batchTable.getSelectedRow(), 0).toString()};
                                        dtm.addRow(rowData);
                                        grandTotal();
                                    }

                                } catch (Exception ex) {
                                    Logger.getLogger(ViewItems.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                //
                            } else {
                                JOptionPane.showMessageDialog(Invoice.this, "'Order Qty' Cannot Be Zero");
                            }

                        } else {
                            JOptionPane.showMessageDialog(Invoice.this, "'Order Qty' Cannot Be Greater Than 'Qty On Hand'");
                        }

                    } else {
                        JOptionPane.showMessageDialog(Invoice.this, "Please Enter Valid Details");
                    }
                } else {
                    JOptionPane.showMessageDialog(Invoice.this, "Item Already Added");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(Invoice.this, "Please Select Batch To Add");
            }
        }


    }//GEN-LAST:event_addBtnActionPerformed

    private void discountTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discountTextKeyPressed
        // grandTotal();
    }//GEN-LAST:event_discountTextKeyPressed

    private void discountTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discountTextKeyReleased
        grandTotal();
    }//GEN-LAST:event_discountTextKeyReleased

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtnActionPerformed
        int a = table.getSelectedRow();
        if (a != -1) {
            DefaultTableModel dtm = (DefaultTableModel) table.getModel();
            dtm.removeRow(a);
            grandTotal();
        } else {
            JOptionPane.showMessageDialog(Invoice.this, "Please select a row to delete");
        }

    }//GEN-LAST:event_removeBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        if (!oidText.getText().isEmpty() && (table.getRowCount() != 0)) {
            String oid = oidText.getText();
            Date date = datePicker.getDate();
            String custId = customerIdCombo.getEditor().getItem().toString();
            Customer customer = null;
            try {
                customer = CustomerController.searchCustomer(custId);
            } catch (ClassNotFoundException | SQLException ex) {
                JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
            }

            OrderBrandItemDetails[] obid = new OrderBrandItemDetails[table.getRowCount()];

            for (int i = 0; i < table.getRowCount(); i++) {

                String batchId = table.getValueAt(i, 7).toString();
                int oQty = Integer.parseInt(table.getValueAt(i, 4).toString());

                double unitPrice = Double.parseDouble(table.getValueAt(i, 5).toString());

                obid[i] = new OrderBrandItemDetails(batchId, oQty, unitPrice, oid);
            }

            if (customer != null) {
                Orders orders = new Orders(oid, custId, date, Double.parseDouble(discountText.getText().toString()), obid);
                try {
                    int res = OrdersController.addOrder(orders);
                    if (res > 0) {
                        JOptionPane.showMessageDialog(Invoice.this, "Saved...");
                    } else {
                        JOptionPane.showMessageDialog(Invoice.this, "Error...");
                    }

                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(Invoice.this, ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(Invoice.this, "Customer not selected");
            }

        } else {
            JOptionPane.showMessageDialog(Invoice.this, "Please Add Items To List");
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void datePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datePickerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_datePickerActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (jCheckBox1.isSelected()) {
            batchTable.setEnabled(true);
        } else {
            batchTable.setEnabled(false);
            getBiidDetails();
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

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
            java.util.logging.Logger.getLogger(Invoice.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Invoice.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Invoice.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Invoice.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Invoice().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JTextField addressText;
    private javax.swing.JTable batchTable;
    private javax.swing.JTextField biidText;
    private javax.swing.JComboBox brandCombo;
    private javax.swing.JTextField brandIdText;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox customerCombo;
    public javax.swing.JComboBox customerIdCombo;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JTextField discountText;
    private javax.swing.JTextField emailText;
    private javax.swing.JTextField grandTotalText;
    private javax.swing.JTextField iCodeText;
    private javax.swing.JComboBox itemDesCombo;
    private javax.swing.JTextField itemPriceText;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton newCustomerBtn;
    private javax.swing.JTextField nicText;
    private javax.swing.JTextField oidText;
    private javax.swing.JTextField orderQtyText;
    private javax.swing.JTextField qtyOnHandText;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JButton removeBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTable table;
    private javax.swing.JTextField telText;
    private javax.swing.JTextField totalText;
    private javax.swing.JComboBox warrentyperiodCombo;
    private javax.swing.JTextField widText;
    // End of variables declaration//GEN-END:variables
}
