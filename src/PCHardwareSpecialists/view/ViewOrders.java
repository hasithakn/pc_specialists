/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.view;

import PCHardwareSpecialists.controller.BrandController;
import PCHardwareSpecialists.controller.BrandItemDetailsController;
import PCHardwareSpecialists.controller.CustomerController;
import PCHardwareSpecialists.controller.ItemController;
import PCHardwareSpecialists.controller.OrdersController;
import PCHardwareSpecialists.controller.OtherSql;
import PCHardwareSpecialists.main.Main;
import PCHardwareSpecialists.model.Brand;
import PCHardwareSpecialists.model.BrandItemDetails;
import PCHardwareSpecialists.model.Customer;
import PCHardwareSpecialists.model.Item;
import PCHardwareSpecialists.model.Orders;
import PCHardwareSpecialists.view.Invoice;
import PCHardwareSpecialists.view.ViewItems;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Hasithakn
 */
public class ViewOrders extends javax.swing.JInternalFrame {

    Main mm = null;

    /**
     * Creates new form ViewCustomer
     */
    public ViewOrders() {
        initComponents();
        setTitle("Orders");

        fillCustomerCombo();
        fillOrderTable();

        //table action
        orderTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int selectedRow = orderTable.getSelectedRow();
                    String oid = orderTable.getValueAt(selectedRow, 0).toString();
                    String custId = orderTable.getValueAt(selectedRow, 2).toString();
                    String custName = orderTable.getValueAt(selectedRow, 1).toString();
                    String oDate = orderTable.getValueAt(selectedRow, 3).toString();
                    String discount = orderTable.getValueAt(selectedRow, 4).toString();

                    customerCombo.getEditor().setItem(custName);
                    orderidCombo.getEditor().setItem(oid);
                    customerIdCombo.getEditor().setItem(custId);

                    datePicker.setDate(Date.valueOf(oDate));
                    discounttext.setText(discount);

                    //nic fill
                    Customer cstmr = null;
                    try {
                        cstmr = CustomerController.searchCustomer(customerIdCombo.getEditor().getItem().toString());
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
                    }
                    if (cstmr != null) {
                        custNicText.setText(cstmr.getNic());
                    } else {
                        custNicText.setText("");
                    }
                    // grand total & discount
                    getOrderDetails();

                } catch (Exception ex) {
                    customerCombo.getEditor().setItem("");
                    orderidCombo.getEditor().setItem("");
                    customerIdCombo.getEditor().setItem("");

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    datePicker.setDate(cal.getTime());

                    discounttext.setText("");
                    custNicText.setText("");
                }
            }
        });
        AutoCompleteDecorator.decorate(customerCombo);
        AutoCompleteDecorator.decorate(orderidCombo);
        AutoCompleteDecorator.decorate(customerIdCombo);
    }

    public ViewOrders(Main m) {
        initComponents();
        setTitle("Orders");
        this.mm = m;
        fillCustomerCombo();
        fillOrderTable();

        //table action
        orderTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int selectedRow = orderTable.getSelectedRow();
                    String oid = orderTable.getValueAt(selectedRow, 0).toString();
                    String custId = orderTable.getValueAt(selectedRow, 2).toString();
                    String custName = orderTable.getValueAt(selectedRow, 1).toString();
                    String oDate = orderTable.getValueAt(selectedRow, 3).toString();
                    String discount = orderTable.getValueAt(selectedRow, 4).toString();

                    customerCombo.getEditor().setItem(custName);
                    orderidCombo.getEditor().setItem(oid);
                    customerIdCombo.getEditor().setItem(custId);

                    datePicker.setDate(Date.valueOf(oDate));
                    discounttext.setText(discount);

                    //nic fill
                    Customer cstmr = null;
                    try {
                        cstmr = CustomerController.searchCustomer(customerIdCombo.getEditor().getItem().toString());
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
                    }
                    if (cstmr != null) {
                        custNicText.setText(cstmr.getNic());
                    } else {
                        custNicText.setText("");
                    }
                    // grand total & discount
                    getOrderDetails();

                } catch (Exception ex) {
                    customerCombo.getEditor().setItem("");
                    orderidCombo.getEditor().setItem("");
                    customerIdCombo.getEditor().setItem("");

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    datePicker.setDate(cal.getTime());

                    discounttext.setText("");
                    custNicText.setText("");
                }
            }
        });
        AutoCompleteDecorator.decorate(customerCombo);
        AutoCompleteDecorator.decorate(orderidCombo);
        AutoCompleteDecorator.decorate(customerIdCombo);
    }

    public void fillCustomerIdComboByName() {
        ResultSet rst = null;
        String customerName = customerCombo.getEditor().getItem().toString();

        ArrayList<Customer> customer = null;
        if (!customerName.isEmpty() || customerName != null) {
            customerIdCombo.removeAllItems();
            custNicText.setText("");
            try {
                customer = CustomerController.searchCustomerByName(customerName);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
            }
            //Customer[] customersInArray = new Customer[customer.size()];
            if (customer != null) {
                for (Customer customer2 : customer) {
                    customerIdCombo.addItem(customer2.getCustId());
                }
            } else {
                custNicText.setText("");
                customerIdCombo.getEditor().setItem("No Cusomer Found");
            }

        } else {
            customerIdCombo.removeAllItems();
            custNicText.setText("");
            ArrayList<Customer> customerList = null;
            try {
                customerList = CustomerController.getAllCustomer();
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
            }
            customerIdCombo.removeAllItems();
            for (Customer cusomer : customerList) {
                customerIdCombo.addItem(cusomer.getCustId());
            }
            try {
                customerIdCombo.setSelectedIndex(0);
            } catch (Exception e) {
            }
        }

    }

    public void fillALLOid() {
        ArrayList<Orders> orderList = null;
        orderidCombo.removeAllItems();
        try {
            orderList = OrdersController.getAllOrders();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
        }
        for (Orders orders : orderList) {
            orderidCombo.addItem(orders.getOid());
        }

    }

    public void fillOidByCustId() {
        orderidCombo.removeAllItems();
        ResultSet rst = null;
        ArrayList<Orders> orderList = null;
        if (customerIdCombo.getEditor().getItem().toString().isEmpty()) {
            fillALLOid();
        } else {
            try {
                orderList = OrdersController.searchOrderByCustId(customerIdCombo.getEditor().getItem().toString());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ViewOrders.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ViewOrders.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (Orders orders : orderList) {
                orderidCombo.addItem(orders.getOid());
            }
        }
    }

    public void fillOrderTable() {
        ArrayList<Orders> orderList = null;
        try {
            orderList = OrdersController.getAllOrders();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
        }
        DefaultTableModel dtm = (DefaultTableModel) orderTable.getModel();
        dtm.setRowCount(0);
        for (Orders orders : orderList) {
            Customer customer = null;
            try {
                customer = CustomerController.searchCustomer(orders.getCustId());
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
            }
            if (customer != null) {
                Object[] rowData = {orders.getOid(), customer.getCustName(), orders.getCustId(), orders.getoDate(), orders.getDiscount()};
                dtm.addRow(rowData);
            } else {
                Object[] rowData = {orders.getOid(), "--", orders.getCustId(), orders.getoDate(), orders.getDiscount()};
                dtm.addRow(rowData);
            }
        }
    }

    public void getOrderDetails() {
        Orders orders = null;
        try {
            if (orderidCombo.getSelectedItem().toString().isEmpty()) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                datePicker.setDate(cal.getTime());

                discounttext.setText("");
                grandTotal.setText("");
                grandTotalWithDiscount.setText("");
                ListSelectionModel selectionModel = orderTable.getSelectionModel();
                selectionModel.setSelectionInterval(0, orderTable.getRowCount());
            } else {
                try {
                    orders = OrdersController.searchOrder(orderidCombo.getSelectedItem().toString());
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
                }
                if (orders != null) {
                    ResultSet rst = null;

                    datePicker.setDate(orders.getoDate());
                    discounttext.setText(Double.toString(orders.getDiscount()));
                    String sql = "Select o.oid,OQty,price from orderbranditemdetails obid, orders o where obid.oid=o.oid and o.oid='" + orders.getOid().toString() + "'";
                    try {
                        rst = OtherSql.otherSql(sql);
                        Double gtot1 = 0.0;
                        while (rst.next()) {
                            Double oQty = Double.parseDouble(Integer.toString(rst.getInt("oQty")));
                            Double price = rst.getDouble("price");
                            gtot1 = gtot1 + (oQty * price);
                        }
                        grandTotal.setText(Double.toString(gtot1));
                        Double gtotwithDiscount = 0.0;
                        gtotwithDiscount = (gtot1 * (100 - (Double.parseDouble(discounttext.getText())))) / 100;
                        grandTotalWithDiscount.setText(Double.toString(gtotwithDiscount));
                        int a1 = orderTable.getRowCount();
                        int b = 0;
                        String text = orderidCombo.getSelectedItem().toString();
                        for (int i = 0; i < a1; i++) {
                            if (orderTable.getValueAt(i, 0).toString().equals(text)) {
                                break;
                            } else {
                                b++;
                            }
                        }
                        if (!orderidCombo.getSelectedItem().toString().isEmpty()) {
                            ListSelectionModel selectionModel = orderTable.getSelectionModel();
                            selectionModel.setSelectionInterval(0, b);
                        } else {
                            ListSelectionModel selectionModel = orderTable.getSelectionModel();
                            selectionModel.setSelectionInterval(0, orderTable.getRowCount());
                        }
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
                    }

                    //System.out.println(b);
                } else {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    datePicker.setDate(cal.getTime());

                    discounttext.setText("");
                    grandTotal.setText("");
                    grandTotalWithDiscount.setText("");
                    ListSelectionModel selectionModel = orderTable.getSelectionModel();
                    selectionModel.setSelectionInterval(0, orderTable.getRowCount() - 1);
                }

            }
        } catch (Exception ex) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            datePicker.setDate(cal.getTime());

            discounttext.setText("");
            grandTotal.setText("");
            grandTotalWithDiscount.setText("");
//            ListSelectionModel selectionModel = orderTable.getSelectionModel();
//            selectionModel.setSelectionInterval(0, orderTable.getRowCount());
//            JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
//            Logger.getLogger(ViewOrders.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void fillCustomerCombo() {
        customerCombo.removeAllItems();
        ArrayList<Customer> customerList = null;
        try {
            customerList = CustomerController.getAllCustomer();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
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

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        customerCombo = new javax.swing.JComboBox();
        discounttext = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        grandTotal = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        orderidCombo = new javax.swing.JComboBox();
        customerIdCombo = new javax.swing.JComboBox();
        custNicText = new javax.swing.JTextField();
        grandTotalWithDiscount = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        cancelButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        viewItemsButton = new javax.swing.JButton();
        viewPaymentButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Orders");

        orderTable.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order ID", "Customer Name", "Customer ID", "Date", "Discount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        orderTable.getTableHeader().setReorderingAllowed(false);
        orderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                orderTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(orderTable);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Details"));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel3.setText("Customer ID");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel4.setText("Customer Name");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setText("Order ID");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel6.setText("Order Date");

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jButton3.setText("New Order");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        customerCombo.setEditable(true);
        customerCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        customerCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerComboActionPerformed(evt);
            }
        });

        discounttext.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel7.setText("Discount");

        grandTotal.setEditable(false);
        grandTotal.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel8.setText("GrandTotal");

        orderidCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        orderidCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderidComboActionPerformed(evt);
            }
        });

        customerIdCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        customerIdCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerIdComboActionPerformed(evt);
            }
        });

        custNicText.setEditable(false);
        custNicText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        grandTotalWithDiscount.setEditable(false);
        grandTotalWithDiscount.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("GrandTotal (With Discount)");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5))
                        .addGap(86, 86, 86)
                        .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(grandTotalWithDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customerCombo, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(orderidCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(customerIdCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(custNicText)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6))
                                .addGap(23, 23, 23)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(datePicker, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                                    .addComponent(grandTotal)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(272, 272, 272)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(customerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(customerIdCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(custNicText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(grandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(orderidCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(grandTotalWithDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        cancelButton.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        deleteButton.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        deleteButton.setText("Delete Selected");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        updateButton.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        updateButton.setText("Update Selected");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        viewItemsButton.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        viewItemsButton.setText("View Items");
        viewItemsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewItemsButtonActionPerformed(evt);
            }
        });

        viewPaymentButton.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        viewPaymentButton.setText("View Payments");
        viewPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPaymentButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(viewPaymentButton)
                        .addGap(18, 18, 18)
                        .addComponent(viewItemsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(updateButton)
                        .addGap(18, 18, 18)
                        .addComponent(deleteButton)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 917, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(deleteButton)
                    .addComponent(updateButton)
                    .addComponent(viewItemsButton)
                    .addComponent(viewPaymentButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void customerComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerComboActionPerformed
        fillCustomerIdComboByName();
        fillOidByCustId();
//        System.out.println("getbiidcustomer comoo");
        getOrderDetails();// TODO add your handling code here:
//        System.out.println("getbiidcustomer comoo done");
    }//GEN-LAST:event_customerComboActionPerformed

    private void customerIdComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerIdComboActionPerformed
        fillOidByCustId();
        //nic text fill
        Customer cstmr = null;
        try {
            cstmr = CustomerController.searchCustomer(customerIdCombo.getEditor().getItem().toString());
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
        }
        if (cstmr != null) {
            custNicText.setText(cstmr.getNic());
        } else {
            custNicText.setText("");
        }
        getOrderDetails();
    }//GEN-LAST:event_customerIdComboActionPerformed

    private void orderidComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderidComboActionPerformed
        getOrderDetails();

    }//GEN-LAST:event_orderidComboActionPerformed

    private void orderTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderTableMouseClicked
        try {
            int selectedRow = orderTable.getSelectedRow();
            String oid = orderTable.getValueAt(selectedRow, 0).toString();
            String custId = orderTable.getValueAt(selectedRow, 2).toString();
            String custName = orderTable.getValueAt(selectedRow, 1).toString();
            String oDate = orderTable.getValueAt(selectedRow, 3).toString();
            String discount = orderTable.getValueAt(selectedRow, 4).toString();

            customerCombo.getEditor().setItem(custName);
            orderidCombo.getEditor().setItem(oid);
            customerIdCombo.getEditor().setItem(custId);
            //orderdate.setText(oDate);
            datePicker.setDate(Date.valueOf(oDate));
            discounttext.setText(discount);

            //nic fill
            Customer cstmr = null;
            try {
                cstmr = CustomerController.searchCustomer(customerIdCombo.getEditor().getItem().toString());
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
            }
            if (cstmr != null) {
                custNicText.setText(cstmr.getNic());
            } else {
                custNicText.setText("");
            }
            // grand total & discount
            getOrderDetails();

        } catch (Exception e) {
            customerCombo.getEditor().setItem("");
            orderidCombo.getEditor().setItem("");
            customerIdCombo.getEditor().setItem("");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            datePicker.setDate(cal.getTime());

            discounttext.setText("");
            custNicText.setText("");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_orderTableMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        mm.desktopPane.removeAll();
        Invoice a = new Invoice();
        a.setVisible(true);
        mm.desktopPane.add(a);
        mm.desktopPane.setLayer(a, 1);
        mm.tabPane.setSelectedIndex(1);     // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if (!orderidCombo.getEditor().getItem().toString().isEmpty()) {
            try {
                String oid = orderTable.getValueAt(orderTable.getSelectedRow(), 0).toString();
                int a = OrdersController.deleteOrderFully(oid);
                if (a > 0) {
                    JOptionPane.showMessageDialog(ViewOrders.this, "Delete Success");
                    fillOrderTable();
                    getOrderDetails();
                } else {
                    JOptionPane.showMessageDialog(ViewOrders.this, "Delete Failed");
                }
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ViewOrders.this, "Please select order to delete");
            }

        } else {
            JOptionPane.showMessageDialog(ViewOrders.this, "Please select order to delete");
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        try {
            if (!orderidCombo.getEditor().getItem().toString().isEmpty() || orderidCombo.getSelectedItem().toString().isEmpty()) {
                try {
                    String oid = orderTable.getValueAt(orderTable.getSelectedRow(), 0).toString();

                    java.util.Date oDate = datePicker.getDate();

                    String custid = orderTable.getValueAt(orderTable.getSelectedRow(), 2).toString();
                    Double discount = Double.parseDouble(discounttext.getText());
                    Orders order = new Orders(oid, custid, oDate, discount);
                    int a = OrdersController.updateOrders(order);
                    if (a > 0) {
                        JOptionPane.showMessageDialog(ViewOrders.this, "Update Success");
                        fillOrderTable();
                        getOrderDetails();
                    } else {
                        JOptionPane.showMessageDialog(ViewOrders.this, "Update Failed");
                    }
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(ViewOrders.this, ex.getMessage());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ViewOrders.this, "Please select order to update ");
                    java.util.logging.Logger.getLogger(ViewOrders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }

            } else {
                JOptionPane.showMessageDialog(ViewOrders.this, "Please select order to update");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ViewOrders.this, "Please select order to update");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_updateButtonActionPerformed

    private void viewItemsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewItemsButtonActionPerformed
        if (!orderidCombo.getEditor().getItem().toString().isEmpty()) {
            try {
                ViewOrderBrandItemDetails a = new ViewOrderBrandItemDetails();
                a.oidText.setText(this.orderidCombo.getEditor().getItem().toString());
                a.datePicker.setDate(this.datePicker.getDate());
                a.customerIdCombo1.getEditor().setItem(this.customerIdCombo.getEditor().getItem());
                a.fillCustomerDetailsById();
                a.discountText2.setText(this.discounttext.getText());
                a.fillOrderTableByOid();
                a.grandTotal();
                a.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ViewOrders.this, "Please select order");
            }

        } else {
            JOptionPane.showMessageDialog(ViewOrders.this, "Please select order");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_viewItemsButtonActionPerformed

    private void viewPaymentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPaymentButtonActionPerformed
        ViewOrderPaymentByOid vo = new ViewOrderPaymentByOid(this);
        vo.setVisible(true);
        vo.oidText.setText(orderidCombo.getEditor().getItem().toString());
        vo.grandTotalText.setText(grandTotalWithDiscount.getText());
        vo.fillTable();
        vo.getDetails();
    }//GEN-LAST:event_viewPaymentButtonActionPerformed

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
            java.util.logging.Logger.getLogger(ViewOrders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewOrders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewOrders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewOrders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewOrders().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField custNicText;
    private javax.swing.JComboBox customerCombo;
    private javax.swing.JComboBox customerIdCombo;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JButton deleteButton;
    private javax.swing.JTextField discounttext;
    private javax.swing.JTextField grandTotal;
    private javax.swing.JTextField grandTotalWithDiscount;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable orderTable;
    private javax.swing.JComboBox orderidCombo;
    private javax.swing.JButton updateButton;
    private javax.swing.JButton viewItemsButton;
    private javax.swing.JButton viewPaymentButton;
    // End of variables declaration//GEN-END:variables
}
