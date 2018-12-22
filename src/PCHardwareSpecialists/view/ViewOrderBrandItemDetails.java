/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.view;

import PCHardwareSpecialists.controller.BatchController;
import java.awt.Frame;
import PCHardwareSpecialists.controller.BrandController;
import PCHardwareSpecialists.controller.BrandItemDetailsController;
import PCHardwareSpecialists.controller.CustomerController;
import PCHardwareSpecialists.controller.IdGen;
import PCHardwareSpecialists.controller.ItemController;
import PCHardwareSpecialists.controller.OtherSql;
import PCHardwareSpecialists.controller.WarrentyController;
import PCHardwareSpecialists.controller.OrderBrandItemDetailsController;
import PCHardwareSpecialists.controller.OrdersController;
import PCHardwareSpecialists.controller.ViewOrderBrandItemDetailsController;
import PCHardwareSpecialists.view.NewItem;
import PCHardwareSpecialists.view.ViewCustomer;
//import PCHardwareSpecialists.view.SummarizeItems;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import PCHardwareSpecialists.model.Brand;
import PCHardwareSpecialists.model.BrandItemDetails;
import PCHardwareSpecialists.model.Customer;
import PCHardwareSpecialists.model.Item;
import PCHardwareSpecialists.model.Warrenty;
import PCHardwareSpecialists.model.OrderBrandItemDetails;
import PCHardwareSpecialists.model.Orders;
import PCHardwareSpecialists.model.SummarizeItemsDialogModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Hasithakn
 */
public class ViewOrderBrandItemDetails extends javax.swing.JFrame {

    ViewOrders vo = null;

    /**
     * Creates new form Invoice
     */
    public ViewOrderBrandItemDetails() {

        initComponents();
        //fillordertable
        fillOrderTableByOid();
        fillCustomerCombo();
        //fill itemCombo
        fillItemDesCombo();
        //searchable combos
        AutoCompleteDecorator.decorate(itemDesCombo10);
        AutoCompleteDecorator.decorate(brandCombo10);
        AutoCompleteDecorator.decorate(customerCombo1);
    }

    public ViewOrderBrandItemDetails(ViewOrders vo) {

        initComponents();
        this.vo = vo;
        //fillordertable
        fillOrderTableByOid();
        fillCustomerCombo();
        //fill itemCombo
        fillItemDesCombo();
        //searchable combos
        AutoCompleteDecorator.decorate(itemDesCombo10);
        AutoCompleteDecorator.decorate(brandCombo10);
        AutoCompleteDecorator.decorate(customerCombo1);
    }

    //////////shit
    public void fillOrderTableByOid() {

        ArrayList<SummarizeItemsDialogModel> sidm = null;
        try {
            sidm = ViewOrderBrandItemDetailsController.getoQtyBiidPriceForViewOrderBrandItemdetails(oidText.getText());
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        }
        try {
            DefaultTableModel dtm = (DefaultTableModel) orderTable.getModel();
            dtm.setRowCount(0);
            for (SummarizeItemsDialogModel summarizeItemsDialogModel : sidm) {
                //Object[] ob = {rst.getString("oQty"), rst.getString("biid"), rst.getString("price")};
                //list.add(ob);                  
                String biid = BatchController.selectBiidFromBatchWhereBatchId(summarizeItemsDialogModel.getBatchId());
                BrandItemDetails BID = null;
                Brand brand = null;
                Warrenty warrenty = null;
                Item item = null;
                //
                BID = BrandItemDetailsController.searchBrandItemDetailsByBIID(biid);
                //get brand warrenty , item names
                item = ItemController.searchItem(BID.getiCode());
                brand = BrandController.searchBrand(BID.getBid());
                warrenty = WarrentyController.searchWarrenty(BID.getWid());
                Double a = summarizeItemsDialogModel.getPrice() * Double.parseDouble(Integer.toString(summarizeItemsDialogModel.getoQty()));
                Object[] rowData = {biid, item.getDescription(), brand.getBrandName(), warrenty.getwPeriod(), summarizeItemsDialogModel.getoQty(), summarizeItemsDialogModel.getPrice(), Double.toString(a), summarizeItemsDialogModel.getBatchId()};
                dtm.addRow(rowData);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        }
    }

    public void oidGen() {
        try {
            oidText.setText(IdGen.getNextId("orders", "oid", "OI"));
        } catch (SQLException ex) {
            Logger.getLogger(ViewOrderBrandItemDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fillbrandComboByItem() {
        String description = itemDesCombo10.getEditor().getItem().toString();
        ArrayList<Item> item2 = null;
        if (!description.isEmpty() || description != null) {
            brandCombo10.removeAllItems();
            try {
                item2 = ItemController.searchItemByname(description);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            }
            if (item2 != null) {
                for (Item item : item2) {
                    ArrayList<Brand> brands = null;
                    try {
                        brands = BrandItemDetailsController.searchBidFromBrandItemDetailsWhereIcode(item.getiCode());
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
                    } catch (NullPointerException ex) {
                        JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage() + "ss");
                    }
                    if (brands != null) {
                        for (Brand brand : brands) {
                            Brand brandByBid = null;
                            try {
                                brandByBid = BrandController.searchBrand(brand.getBid());
                            } catch (ClassNotFoundException ex) {
                                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
                            }
                            if (brandByBid != null) {
                                brandCombo10.addItem(brandByBid.getBrandName());
                            }
                            try {
                                brandCombo10.setSelectedIndex(0);
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
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            }
            brandCombo10.removeAllItems();
            for (Brand brand1 : brand) {
                brandCombo10.addItem(brand1.getBrandName());
            }
            try {
                brandCombo10.setSelectedIndex(0);
            } catch (IllegalArgumentException ex) {

            }
        }

    }

    public void grandTotal() {
        Double a = 0.0;
        for (int i = 0; i < orderTable.getRowCount(); i++) {
            a = a + Double.parseDouble(orderTable.getValueAt(i, 6).toString());
        }
        if (!discountText2.getText().isEmpty()) {
            try {
                Double b = 100.0 - Double.parseDouble(discountText2.getText());
                b = (b * a) / 100;
                grandTotalText1.setText(b.toString());
            } catch (Exception e) {
            }
        } else {
            grandTotalText1.setText(a.toString());
        }

    }

    public boolean checkBeforeAddBiid() {
        String a = biidText1.getText();
        boolean b = false;
        for (int i = 0; i < orderTable.getRowCount(); i++) {
            String a1 = orderTable.getValueAt(i, 0).toString();
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
        if (!customerCombo1.getEditor().getItem().toString().isEmpty()) {
            try {
                customerlist = CustomerController.searchCustomerByName(customerCombo1.getEditor().getItem().toString());
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            }
            Customer[] customer = new Customer[customerlist.size()];
            for (int i = 0; i < customerlist.size(); i++) {
                customer[i] = customerlist.get(i);
            }
            if (customerlist.size() == 1 && customer != null) {
                customerIdCombo1.removeAllItems();
                customerIdCombo1.addItem(customer[0].getCustId());
                nicText1.setText(customer[0].getNic());
                addressText1.setText(customer[0].getAddress());
                emailText1.setText(customer[0].getCustEmail());
                telText1.setText(Integer.toString(customer[0].getTel()));
            } else {
                customerIdCombo1.removeAllItems();
                for (int i = 0; i < customerlist.size(); i++) {
                    customerIdCombo1.addItem(customer[i].getCustId());
                }

            }
        }
    }

    public void fillwarrentyDesCombo() {
        ArrayList<Warrenty> warrenty = null;
        try {
            warrenty = WarrentyController.getAllWarrenty();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        }
        warrentyperiodCombo10.removeAllItems();
        for (Warrenty warrenty1 : warrenty) {
            warrentyperiodCombo10.addItem(warrenty1.getwPeriod());
        }
        try {
            warrentyperiodCombo10.setSelectedIndex(0);
        } catch (Exception e) {
        }

    }

    public void totalAndOrderQtyTextClear() {
        totalText1.setText("");
        orderQtyText1.setText("");
    }

    public void fillwarrentyDesComboByBrand() {
        warrentyperiodCombo10.removeAllItems();
        String iCode = iCodeText10.getText();
        String bid = brandIdText10.getText();
        ArrayList<Warrenty> widList = null;
        if (iCode.isEmpty() || bid.isEmpty()) {
            fillwarrentyDesCombo();
        } else {
            try {
                widList = BrandItemDetailsController.searchWidFromBrandItemDetailsWhereIcodeAndBid(iCode, bid);
                for (Warrenty warrenty : widList) {
                    Warrenty ww = WarrentyController.searchWarrenty(warrenty.getWid());
                    if (ww != null) {
                        warrentyperiodCombo10.addItem(ww.getwPeriod());
                    }
                    try {
                        warrentyperiodCombo10.setSelectedIndex(0);
                    } catch (IllegalArgumentException ex) {
                    }

                }
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            }
        }
    }

    public void fillCustomerDetailsById() {
        Customer customer = null;
        if (!customerIdCombo1.getEditor().getItem().toString().isEmpty()) {
            try {
                customer = CustomerController.searchCustomer(customerIdCombo1.getEditor().getItem().toString());
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            }
            if (customer != null) {
                customerCombo1.getEditor().setItem(customer.getCustName());
                nicText1.setText(customer.getNic());
                addressText1.setText(customer.getAddress());
                emailText1.setText(customer.getCustEmail());
                telText1.setText(Integer.toString(customer.getTel()));
            }
        }
    }

    public void fillItemDesCombo() {
        ArrayList<Item> item = null;
        try {
            item = ItemController.getAllItem();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        }
        itemDesCombo10.removeAllItems();
        for (Item item1 : item) {
            itemDesCombo10.addItem(item1.getDescription());
        }
    }

    public void getBiidDetails() {
        BrandItemDetails brandItemDetails = null;
        if (iCodeText10.getText().isEmpty() || brandIdText10.getText().isEmpty() || widText10.getText().isEmpty()) {
            biidText1.setText("");
            itemPriceText1.setText("");
            qtyOnHandText1.setText("");
            totalAndOrderQtyTextClear();
        } else {
            try {
                brandItemDetails = BrandItemDetailsController.searchBrandItemDetails(iCodeText10.getText(), brandIdText10.getText(), widText10.getText());
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            }
            if (brandItemDetails != null) {
                biidText1.setText(brandItemDetails.getBiid());
                itemPriceText1.setText(Double.toString(brandItemDetails.getSellingPrice()));
                int a = 0;
                String sql = "select qty from batch where biid='" + brandItemDetails.getBiid() + "'";
                ResultSet rst;
                try {
                    rst = OtherSql.otherSql(sql);
                    while (rst.next()) {
                        a = a + rst.getInt("qty");
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ViewItems.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ViewItems.class.getName()).log(Level.SEVERE, null, ex);
                }
                qtyOnHandText1.setText(Integer.toString(a));
                ///                
                totalAndOrderQtyTextClear();
            } else {
                biidText1.setText("");
                itemPriceText1.setText("");
                qtyOnHandText1.setText("");
                totalAndOrderQtyTextClear();
            }

        }
    }

    public void fillCustomerCombo() {
        customerCombo1.removeAllItems();
        ArrayList<Customer> customerList = null;
        try {
            customerList = CustomerController.getAllCustomer();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        }

        for (Customer customer : customerList) {
            customerCombo1.addItem(customer.getCustName());
            AutoCompleteDecorator.decorate(customerCombo1);
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

        jLabel95 = new javax.swing.JLabel();
        discountText1 = new javax.swing.JTextField();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        grandTotalText = new javax.swing.JTextField();
        removeBtn1 = new javax.swing.JButton();
        saveBtn1 = new javax.swing.JButton();
        cancelBtn1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        nicText1 = new javax.swing.JTextField();
        addressText1 = new javax.swing.JTextField();
        emailText1 = new javax.swing.JTextField();
        telText1 = new javax.swing.JTextField();
        customerCombo1 = new javax.swing.JComboBox();
        newCustomerBtn1 = new javax.swing.JButton();
        refreshBtn1 = new javax.swing.JButton();
        customerIdCombo1 = new javax.swing.JComboBox();
        jLabel27 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        biidText1 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        itemPriceText1 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        qtyOnHandText1 = new javax.swing.JTextField();
        orderQtyText1 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        totalText1 = new javax.swing.JTextField();
        addBtn1 = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        itemDesCombo10 = new javax.swing.JComboBox();
        iCodeText10 = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        brandCombo10 = new javax.swing.JComboBox();
        brandIdText10 = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        warrentyperiodCombo10 = new javax.swing.JComboBox();
        widText10 = new javax.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        discountText2 = new javax.swing.JTextField();
        jLabel99 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        grandTotalText1 = new javax.swing.JTextField();
        removeBtn2 = new javax.swing.JButton();
        saveBtn2 = new javax.swing.JButton();
        cancelBtn2 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        oidText = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane2 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        removeBtn3 = new javax.swing.JButton();

        jLabel95.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel95.setText("Discounts");

        discountText1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        discountText1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        discountText1.setText("0");
        discountText1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                discountText1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                discountText1KeyReleased(evt);
            }
        });

        jLabel96.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel96.setText("%");

        jLabel97.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel97.setText("Grand Total");

        grandTotalText.setEditable(false);
        grandTotalText.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        removeBtn1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        removeBtn1.setText("Remove");
        removeBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtn1ActionPerformed(evt);
            }
        });

        saveBtn1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        saveBtn1.setText("Save");
        saveBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtn1ActionPerformed(evt);
            }
        });

        cancelBtn1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cancelBtn1.setText("Cancal");
        cancelBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtn1ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Details"));

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel21.setText("Customer ID");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel22.setText("Customer Name");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel23.setText("Customer Address");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel24.setText("Customer Nic");

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel25.setText("Customer Email");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel26.setText("Customer Contact No.");

        nicText1.setEditable(false);
        nicText1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        nicText1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nicText1ActionPerformed(evt);
            }
        });

        addressText1.setEditable(false);
        addressText1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        emailText1.setEditable(false);
        emailText1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        emailText1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailText1ActionPerformed(evt);
            }
        });

        telText1.setEditable(false);
        telText1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        customerCombo1.setEditable(true);
        customerCombo1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        customerCombo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerCombo1ActionPerformed(evt);
            }
        });

        newCustomerBtn1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        newCustomerBtn1.setText("New Customer");
        newCustomerBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCustomerBtn1ActionPerformed(evt);
            }
        });

        refreshBtn1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        refreshBtn1.setText("Refresh List");
        refreshBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtn1ActionPerformed(evt);
            }
        });

        customerIdCombo1.setEditable(true);
        customerIdCombo1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        customerIdCombo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerIdCombo1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26)
                    .addComponent(jLabel25)
                    .addComponent(jLabel23)
                    .addComponent(jLabel22)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customerCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerIdCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(nicText1)
                            .addComponent(refreshBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(newCustomerBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel24)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(addressText1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(emailText1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(telText1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(customerCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newCustomerBtn1)
                    .addComponent(refreshBtn1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel24)
                    .addComponent(nicText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerIdCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(addressText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(emailText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(telText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Order Details");

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Item Details"));

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel30.setText("BIID");

        biidText1.setEditable(false);
        biidText1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel31.setText("Item Price");

        itemPriceText1.setEditable(false);
        itemPriceText1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel32.setText("Qty On Hand");

        qtyOnHandText1.setEditable(false);
        qtyOnHandText1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        orderQtyText1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        orderQtyText1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderQtyText1ActionPerformed(evt);
            }
        });
        orderQtyText1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                orderQtyText1KeyReleased(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel33.setText("Order Qty .");

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel34.setText("Total");

        totalText1.setEditable(false);
        totalText1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        addBtn1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        addBtn1.setText("ADD");
        addBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel34)
                        .addGap(18, 18, 18)
                        .addComponent(totalText1, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel32)
                                    .addComponent(jLabel33))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(qtyOnHandText1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                                    .addComponent(orderQtyText1)))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31)
                                    .addComponent(jLabel30))
                                .addGap(38, 38, 38)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(biidText1)
                                    .addComponent(itemPriceText1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))))
                        .addGap(0, 68, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(biidText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(itemPriceText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(qtyOnHandText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(orderQtyText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(totalText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel34))
                    .addComponent(addBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Item"));

        jLabel89.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel89.setText("Item Code");

        jLabel90.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel90.setText("Item Description");

        itemDesCombo10.setEditable(true);
        itemDesCombo10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        itemDesCombo10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemDesCombo10ActionPerformed(evt);
            }
        });

        iCodeText10.setEditable(false);
        iCodeText10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel91.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel91.setText("Brand");

        brandCombo10.setEditable(true);
        brandCombo10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        brandCombo10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brandCombo10ActionPerformed(evt);
            }
        });

        brandIdText10.setEditable(false);
        brandIdText10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel92.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel92.setText("Brand ID");

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel93.setText("Warrenty ");

        warrentyperiodCombo10.setEditable(true);
        warrentyperiodCombo10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        warrentyperiodCombo10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                warrentyperiodCombo10ActionPerformed(evt);
            }
        });

        widText10.setEditable(false);
        widText10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel94.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel94.setText("WID");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemDesCombo10, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel93))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(brandCombo10, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(warrentyperiodCombo10, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel94))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(brandIdText10, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                            .addComponent(widText10)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iCodeText10, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel89)
                        .addComponent(iCodeText10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel90)
                        .addComponent(itemDesCombo10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel91)
                    .addComponent(brandCombo10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(brandIdText10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel92))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(widText10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel93)
                        .addComponent(warrentyperiodCombo10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel94)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel98.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel98.setText("Discounts");

        discountText2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        discountText2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        discountText2.setText("0");
        discountText2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                discountText2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                discountText2KeyReleased(evt);
            }
        });

        jLabel99.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel99.setText("%");

        jLabel100.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel100.setText("Grand Total");

        grandTotalText1.setEditable(false);
        grandTotalText1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        removeBtn2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        removeBtn2.setText("Remove");
        removeBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtn2ActionPerformed(evt);
            }
        });

        saveBtn2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        saveBtn2.setText("Save");
        saveBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtn2ActionPerformed(evt);
            }
        });

        cancelBtn2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cancelBtn2.setText("Cancal");
        cancelBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtn2ActionPerformed(evt);
            }
        });

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Order Details"));

        oidText.setEditable(false);
        oidText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel101.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel101.setText("Order ID");

        jLabel102.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel102.setText("Date");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel101)
                    .addComponent(jLabel102))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oidText, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                    .addComponent(datePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oidText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel102)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BIID", "Description", "Brand", "Warrenty", "Order Qty", "Unit Price", "Total", "BatchId"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(orderTable);

        removeBtn3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        removeBtn3.setText("Summarize Items");
        removeBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtn3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel98)
                        .addGap(18, 18, 18)
                        .addComponent(discountText2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel99)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel100)
                        .addGap(18, 18, 18)
                        .addComponent(grandTotalText1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(removeBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 14, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(discountText2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel98)
                    .addComponent(cancelBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grandTotalText1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel100)
                    .addComponent(jLabel99)
                    .addComponent(removeBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nicText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nicText1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nicText1ActionPerformed

    private void emailText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailText1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailText1ActionPerformed

    private void customerCombo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerCombo1ActionPerformed
        fillCustomerDetailsByName();

    }//GEN-LAST:event_customerCombo1ActionPerformed

    private void newCustomerBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCustomerBtn1ActionPerformed
        new ViewCustomerJFrame().setVisible(true);
    }//GEN-LAST:event_newCustomerBtn1ActionPerformed

    private void refreshBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtn1ActionPerformed
        fillCustomerCombo();
        // set last item as selected customer combo
        customerCombo1.setSelectedIndex(customerCombo1.getItemCount() - 1);
    }//GEN-LAST:event_refreshBtn1ActionPerformed

    private void customerIdCombo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerIdCombo1ActionPerformed
        fillCustomerDetailsById();        // TODO add your handling code here:
    }//GEN-LAST:event_customerIdCombo1ActionPerformed

    private void orderQtyText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderQtyText1ActionPerformed
        try {
            Double a = Double.parseDouble(itemPriceText1.getText()) * Double.parseDouble(orderQtyText1.getText());
            totalText1.setText(Double.toString(a));
        } catch (Exception e) {
            totalText1.setText("");
        }
    }//GEN-LAST:event_orderQtyText1ActionPerformed

    private void orderQtyText1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_orderQtyText1KeyReleased
        try {
            Double a = Double.parseDouble(itemPriceText1.getText()) * Double.parseDouble(orderQtyText1.getText());
            totalText1.setText(Double.toString(a));
        } catch (Exception e) {
            totalText1.setText("");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_orderQtyText1KeyReleased

    private void addBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtn1ActionPerformed
        if (!checkBeforeAddBiid()) {
            if (!biidText1.getText().isEmpty() && !iCodeText10.getText().isEmpty() && !brandIdText10.getText().isEmpty() && !widText10.getText().isEmpty() && !orderQtyText1.getText().isEmpty() && !itemPriceText1.getText().isEmpty() && !totalText1.getText().isEmpty()) {
                if (Integer.parseInt(orderQtyText1.getText()) <= Integer.parseInt(qtyOnHandText1.getText())) {
                    if (Integer.parseInt(orderQtyText1.getText()) != 0) {
                        String sql = "select batchId,qty from batch where biid='" + biidText1.getText() + "'";
                        ResultSet rst;
                        int oQty = Integer.parseInt(orderQtyText1.getText());
                        try {
                            rst = OtherSql.otherSql(sql);
                            while (rst.next()) {
                                if (oQty <= Integer.parseInt(rst.getString("qty"))) {
                                    DefaultTableModel dtm = (DefaultTableModel) orderTable.getModel();
                                    Object[] rowData = {biidText1.getText(), itemDesCombo10.getEditor().getItem().toString(), brandCombo10.getEditor().getItem().toString(), warrentyperiodCombo10.getEditor().getItem().toString(), oQty, itemPriceText1.getText(), Double.parseDouble(itemPriceText1.getText()) * Double.parseDouble(Integer.toString(oQty)), rst.getString("batchId")};
                                    dtm.addRow(rowData);
                                    grandTotal();
                                    oQty = 0;
                                    break;
                                } else if (Integer.parseInt(rst.getString("qty")) > 0) {
                                    DefaultTableModel dtm = (DefaultTableModel) orderTable.getModel();
                                    Object[] rowData = {biidText1.getText(), itemDesCombo10.getEditor().getItem().toString(), brandCombo10.getEditor().getItem().toString(), warrentyperiodCombo10.getEditor().getItem().toString(), rst.getString("qty"), itemPriceText1.getText(), Double.parseDouble(itemPriceText1.getText()) * Double.parseDouble(rst.getString("qty")), rst.getString("batchId")};
                                    dtm.addRow(rowData);
                                    grandTotal();
                                    oQty = oQty - Integer.parseInt(rst.getString("qty"));
                                }
                            }
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ViewItems.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(ViewItems.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else {
                        JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "'Order Qty' Cannot Be Zero");
                    }

                } else {
                    JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "'Order Qty' Cannot Be Greater Than 'Qty On Hand'");
                }

            } else {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "Please Enter Valid Details");
            }
        } else {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "Item Already Added");
        }

    }//GEN-LAST:event_addBtn1ActionPerformed

    private void itemDesCombo10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemDesCombo10ActionPerformed
        fillbrandComboByItem();
        fillwarrentyDesComboByBrand();

        String description = itemDesCombo10.getEditor().getItem().toString();
        ArrayList<Item> item = null;
        try {
            item = ItemController.searchItemByname(description);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        }
        try {
            iCodeText10.setText(item.get(0).getiCode());
        } catch (Exception e) {
        }

        getBiidDetails();
    }//GEN-LAST:event_itemDesCombo10ActionPerformed

    private void brandCombo10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brandCombo10ActionPerformed
        fillwarrentyDesComboByBrand();

        String brand = brandCombo10.getEditor().getItem().toString();
        ArrayList<Brand> brandList = null;
        try {
            brandList = BrandController.searchBrandByName(brand);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        }
        try {
            brandIdText10.setText(brandList.get(0).getBid());
        } catch (Exception e) {
        }
        getBiidDetails();
    }//GEN-LAST:event_brandCombo10ActionPerformed

    private void warrentyperiodCombo10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_warrentyperiodCombo10ActionPerformed
        String warrenty = warrentyperiodCombo10.getEditor().getItem().toString();
        ArrayList<Warrenty> warrenty2 = null;
        try {
            warrenty2 = WarrentyController.searchWarrentyByName(warrenty);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
        }
        try {
            if (!warrentyperiodCombo10.getEditor().getItem().toString().isEmpty()) {
                widText10.setText(warrenty2.get(0).getWid());
                //warrentyPeriodText.setText(warrenty2.get(0).getwPeriod());
            }
        } catch (Exception e) {

        }
        getBiidDetails();
    }//GEN-LAST:event_warrentyperiodCombo10ActionPerformed

    private void discountText1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discountText1KeyPressed
        // grandTotal();
    }//GEN-LAST:event_discountText1KeyPressed

    private void discountText1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discountText1KeyReleased
        grandTotal();
    }//GEN-LAST:event_discountText1KeyReleased

    private void removeBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtn1ActionPerformed
        int a = orderTable.getSelectedRow();
        if (a != -1) {
            DefaultTableModel dtm = (DefaultTableModel) orderTable.getModel();
            dtm.removeRow(a);
            grandTotal();
        } else {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "Please select a row to delete");
        }
    }//GEN-LAST:event_removeBtn1ActionPerformed

    private void saveBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtn1ActionPerformed
        /*if (!oidText.getText().isEmpty() && (orderTable.getRowCount() != 0)) {
         String oid = oidText.getText();

         Date date = datePicker.getDate();

         String custId = customerIdCombo1.getEditor().getItem().toString();
         Customer customer = null;
         try {
         customer = CustomerController.searchCustomer(custId);
         } catch (ClassNotFoundException ex) {
         JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
         } catch (SQLException ex) {
         JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
         }

         OrderBrandItemDetails[] obid = new OrderBrandItemDetails[orderTable.getRowCount()];

         for (int i = 0; i < orderTable.getRowCount(); i++) {

         String biid = orderTable.getValueAt(i, 0).toString();
         int oQty = Integer.parseInt(orderTable.getValueAt(i, 4).toString());

         double unitPrice = Double.parseDouble(orderTable.getValueAt(i, 5).toString());

         obid[i] = new OrderBrandItemDetails(biid, oQty, unitPrice, oid);
         }

         if (customer != null) {
         Orders orders = new Orders(oid, custId, date, Double.parseDouble(discountText2.getText().toString()), obid);
         try {
         int res = OrdersController.addOrder(orders);
         if (res > 0) {
         JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "Saved...");
         } else {
         JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "Error...");
         }

         } catch (SQLException | ClassNotFoundException ex) {
         JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
         }
         } else {
         JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "Customer not selected");
         }

         } else {
         JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "Please Add Items To List");
         }*/
    }//GEN-LAST:event_saveBtn1ActionPerformed

    private void cancelBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtn1ActionPerformed
        this.dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelBtn1ActionPerformed

    private void discountText2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discountText2KeyPressed
        // grandTotal();
    }//GEN-LAST:event_discountText2KeyPressed

    private void discountText2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discountText2KeyReleased
        grandTotal();
    }//GEN-LAST:event_discountText2KeyReleased

    private void removeBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtn2ActionPerformed
        int a = orderTable.getSelectedRow();
        if (a != -1) {
            DefaultTableModel dtm = (DefaultTableModel) orderTable.getModel();
            dtm.removeRow(a);
            grandTotal();
        } else {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "Please select a row to delete");
        }
    }//GEN-LAST:event_removeBtn2ActionPerformed

    private void saveBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtn2ActionPerformed
        if (!oidText.getText().isEmpty() && (orderTable.getRowCount() != 0)) {
            String oid = oidText.getText();
            Date date = datePicker.getDate();
            String custId = customerIdCombo1.getEditor().getItem().toString();
            Customer customer = null;
            try {
                customer = CustomerController.searchCustomer(custId);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
            }

            OrderBrandItemDetails[] obid = new OrderBrandItemDetails[orderTable.getRowCount()];

            for (int i = 0; i < orderTable.getRowCount(); i++) {

                String batchId = orderTable.getValueAt(i, 7).toString();
                int oQty = Integer.parseInt(orderTable.getValueAt(i, 4).toString());

                double unitPrice = Double.parseDouble(orderTable.getValueAt(i, 5).toString());

                obid[i] = new OrderBrandItemDetails(batchId, oQty, unitPrice, oid);
            }

            if (customer != null) {
                Orders orders = new Orders(oid, custId, date, Double.parseDouble(discountText2.getText().toString()), obid);
                try {
                    int res = OrdersController.deleteOrderFullyAndAdd(oid, orders);
                    if (res > 0) {

                        JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "Saved...");
                        if (vo != null) {
                            vo.fillOrderTable();
                        }
//                        for (Frame frame : Frame.getFrames()) {
//                            if (frame.getTitle().equals("Orders")) {
//                                /* Cast to ChildJFrame */
//                                ViewOrders frameyo = (ViewOrders) frame;
//                                frameyo.fillOrderTable();
//                            }
//                        }
                    } else {
                        JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "Error...");
                    }

                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "Customer not selected");
            }

        } else {
            JOptionPane.showMessageDialog(ViewOrderBrandItemDetails.this, "Please Add Items To List");
        }
    }//GEN-LAST:event_saveBtn2ActionPerformed

    private void cancelBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtn2ActionPerformed
        this.dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelBtn2ActionPerformed

    private void removeBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtn3ActionPerformed
        SummarizeItemsDialog a = new SummarizeItemsDialog(this);
        a.setVisible(true);

    }//GEN-LAST:event_removeBtn3ActionPerformed

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
            java.util.logging.Logger.getLogger(ViewOrderBrandItemDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewOrderBrandItemDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewOrderBrandItemDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewOrderBrandItemDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewOrderBrandItemDetails().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn1;
    private javax.swing.JTextField addressText1;
    private javax.swing.JTextField biidText1;
    private javax.swing.JComboBox brandCombo10;
    private javax.swing.JTextField brandIdText10;
    private javax.swing.JButton cancelBtn1;
    private javax.swing.JButton cancelBtn2;
    private javax.swing.JComboBox customerCombo1;
    public javax.swing.JComboBox customerIdCombo1;
    public org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JTextField discountText1;
    public javax.swing.JTextField discountText2;
    private javax.swing.JTextField emailText1;
    private javax.swing.JTextField grandTotalText;
    private javax.swing.JTextField grandTotalText1;
    private javax.swing.JTextField iCodeText10;
    private javax.swing.JComboBox itemDesCombo10;
    private javax.swing.JTextField itemPriceText1;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton newCustomerBtn1;
    private javax.swing.JTextField nicText1;
    public javax.swing.JTextField oidText;
    private javax.swing.JTextField orderQtyText1;
    private javax.swing.JTable orderTable;
    private javax.swing.JTextField qtyOnHandText1;
    private javax.swing.JButton refreshBtn1;
    private javax.swing.JButton removeBtn1;
    private javax.swing.JButton removeBtn2;
    private javax.swing.JButton removeBtn3;
    private javax.swing.JButton saveBtn1;
    private javax.swing.JButton saveBtn2;
    private javax.swing.JTextField telText1;
    private javax.swing.JTextField totalText1;
    private javax.swing.JComboBox warrentyperiodCombo10;
    private javax.swing.JTextField widText10;
    // End of variables declaration//GEN-END:variables
}
