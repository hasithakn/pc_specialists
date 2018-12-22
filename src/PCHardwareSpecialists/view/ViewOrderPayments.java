/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.view;

import PCHardwareSpecialists.controller.CustomerController;
import PCHardwareSpecialists.controller.OChequeController;
import PCHardwareSpecialists.controller.OrderPaymentsController;
import PCHardwareSpecialists.controller.OrdersController;
import PCHardwareSpecialists.controller.OtherSql;
import PCHardwareSpecialists.model.Customer;
import PCHardwareSpecialists.model.OCheque;
import PCHardwareSpecialists.model.OrderPayments;
import PCHardwareSpecialists.model.Orders;
import PCHardwareSpecialists.view.NewOrderPayment;
import PCHardwareSpecialists.view.ViewOrders;
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
public class ViewOrderPayments extends javax.swing.JInternalFrame {

    /**
     * Creates new form ViewCustomer
     */
    public ViewOrderPayments() {
        initComponents();
        fillTable();
        fillCustomerCombo();
        Customer cstmr = null;
        try {
            cstmr = CustomerController.searchCustomer(customerIdCombo.getEditor().getItem().toString());
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        }
        if (cstmr != null) {
            custNicText.setText(cstmr.getNic());
        } else {
            custNicText.setText("");
        }
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int selectedRow = table.getSelectedRow();
                    String oid = table.getValueAt(selectedRow, 0).toString();
                    String custId = table.getValueAt(selectedRow, 1).toString();
                    String opid = table.getValueAt(selectedRow, 3).toString();
                    String pDate = table.getValueAt(selectedRow, 4).toString();
                    //
                    orderIdCombo.getEditor().setItem(oid);
                    customerIdCombo.getEditor().setItem(custId);
                    datePicker.setDate(Date.valueOf(pDate));
                    opidCombo.getEditor().setItem(opid);
                    //getPaymentAndOrderDetails();
                    //nic fill
                    Customer cstmr = null;
                    try {
                        cstmr = CustomerController.searchCustomer(customerIdCombo.getEditor().getItem().toString());
                    } catch (ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
                    }
                    if (cstmr != null) {
                        custNicText.setText(cstmr.getNic());
                        customerCombo.getEditor().setItem(cstmr.getCustName());
                    } else {
                        custNicText.setText("");
                        customerCombo.getEditor().setItem("");
                    }
                    //grand total & discount
                    getPaymentAndOrderDetails();

                } catch (Exception ex) {
                    customerCombo.getEditor().setItem("");
                    orderIdCombo.getEditor().setItem("");
                    customerIdCombo.getEditor().setItem("");
                    Calendar cal = Calendar.getInstance();
                    datePicker.setDate(cal.getTime());
                    custNicText.setText("");
                    //fillCustomerCombo();
                }
            }
        });
    }

    public void fillTable() {
        ArrayList<OrderPayments> orderPaymentList = null;
        try {
            orderPaymentList = OrderPaymentsController.getAllOrderPayments();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        }
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0);
        for (OrderPayments orderPayment : orderPaymentList) {
            Orders order = null;
            try {
                order = OrdersController.searchOrder(orderPayment.getOid());
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
            }
            if (order != null) {
                OCheque oCheque = null;
                try {
                    oCheque = OChequeController.searchChequePaymentByOPID(orderPayment.getOPID());
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
                }
                if (oCheque != null) {
                    Object[] rowData = {order.getOid(), order.getCustId(), order.getoDate().toString(), orderPayment.getOPID(), orderPayment.getPayDate().toString(), orderPayment.getAmount(), oCheque.getChequeNo()};
                    dtm.addRow(rowData);
                } else {
                    Object[] rowData = {order.getOid(), order.getCustId(), order.getoDate().toString(), orderPayment.getOPID(), orderPayment.getPayDate().toString(), orderPayment.getAmount(), "Cash"};
                    dtm.addRow(rowData);
                }

            }
        }
    }

    public void fillCustomerCombo() {
        customerCombo.removeAllItems();
        ArrayList<Customer> customerList = null;
        try {
            customerList = CustomerController.getAllCustomer();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        }

        for (Customer customer : customerList) {
            customerCombo.addItem(customer.getCustName());
            AutoCompleteDecorator.decorate(customerCombo);
        }
    }

    public void fillALLOid() {
        ArrayList<Orders> orderList = null;
        orderIdCombo.removeAllItems();
        try {
            orderList = OrdersController.getAllOrders();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        }
        for (Orders orders : orderList) {
            orderIdCombo.addItem(orders.getOid());
        }

    }

    public void fillALLPid() {
        ArrayList<OrderPayments> paymentsList = null;
        opidCombo.removeAllItems();
        try {
            paymentsList = OrderPaymentsController.getAllOrderPayments();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        }
        for (OrderPayments orderPayments : paymentsList) {
            opidCombo.addItem(orderPayments.getOPID());
        }
    }

    public void fillPidByOid() {
        opidCombo.removeAllItems();
        ResultSet rst = null;
        ArrayList<OrderPayments> paymentsList = null;
        if (orderIdCombo.getEditor().getItem().toString().isEmpty()) {
            //fillALLPid();
            opidCombo.removeAllItems();
        } else {
            try {
                paymentsList = OrderPaymentsController.searchPaymentByOid(orderIdCombo.getEditor().getItem().toString());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ViewOrders.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ViewOrders.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (OrderPayments orderPayments : paymentsList) {
                opidCombo.addItem(orderPayments.getOPID());
            }
        }
    }

    public void getPaymentAndOrderDetails() {
        OrderPayments o = null;
        try {
            o = OrderPaymentsController.searchOrderPayment(opidCombo.getEditor().getItem().toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ViewOrderPayments.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ViewOrderPayments.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResultSet rst = null;
        String sql = "Select o.oid,OQty,price from orderbranditemdetails obid, orders o where obid.oid=o.oid and o.oid='" + orderIdCombo.getEditor().getItem().toString() + "'";
        try {
            rst = OtherSql.otherSql(sql);
            Double gtot1 = 0.0;
            while (rst.next()) {
                Double oQty = Double.parseDouble(Integer.toString(rst.getInt("oQty")));
                Double price = rst.getDouble("price");
                gtot1 = gtot1 + (oQty * price);
            }
            if (o != null) {
                orderIdCombo.getEditor().setItem(o.getOid());
                amountText.setText(o.getAmount().toString());
                datePicker.setDate(o.getPayDate());
                ///grandTotalText.setText(Double.toString(gtot1));
                String sql2 = "Select discount from orders where oid='" + orderIdCombo.getEditor().getItem().toString() + "'";
                rst = OtherSql.otherSql(sql2);
                rst.next();
                Double gtotwithDiscount = 0.0;
                gtotwithDiscount = (gtot1 * (100 - (Double.parseDouble(rst.getString("discount"))))) / 100;
                grandTotalText.setText(Double.toString(gtotwithDiscount));
                int a1 = table.getRowCount();
                int b = 0;
                String text = opidCombo.getSelectedItem().toString();
                for (int i = 0; i < a1; i++) {
                    if (table.getValueAt(i, 3).toString().equals(text)) {
                        break;
                    } else {
                        b++;
                    }
                }
                Double amount = 0.0;
                ResultSet rst3 = null;
                String sql3 = "Select amount from orderpayments where oid='" + orderIdCombo.getEditor().getItem().toString() + "'";
                rst3 = OtherSql.otherSql(sql3);
                if (rst3 != null) {
                    while (rst3.next()) {
                        amount = amount + rst3.getDouble("amount");
                    }
                }
                totalAmountText.setText(amount.toString());
                if (!opidCombo.getSelectedItem().toString().isEmpty()) {
                    ListSelectionModel selectionModel = table.getSelectionModel();
                    selectionModel.setSelectionInterval(0, b);
                } else {
                    ListSelectionModel selectionModel = table.getSelectionModel();
                    selectionModel.setSelectionInterval(0, table.getRowCount());
                }
            } else {
                String sql2 = "Select discount from orders where oid='" + orderIdCombo.getEditor().getItem().toString() + "'";
                rst = OtherSql.otherSql(sql2);
                rst.next();
                Double gtotwithDiscount = 0.0;
                gtotwithDiscount = (gtot1 * (100 - (Double.parseDouble(rst.getString("discount"))))) / 100;
                grandTotalText.setText(Double.toString(gtotwithDiscount));
                //orderIdCombo.removeAllItems();
                amountText.setText("");
                totalAmountText.setText("");
                //grandTotalText.setText("");
                Calendar cal = Calendar.getInstance();
                datePicker.setDate(cal.getTime());
            }
        } catch (ClassNotFoundException ex) {
            //JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
            //orderIdCombo.removeAllItems();
            amountText.setText("");
            totalAmountText.setText("");
            //grandTotalText.setText("");
            Calendar cal = Calendar.getInstance();
            datePicker.setDate(cal.getTime());
        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
            //orderIdCombo.removeAllItems();
            amountText.setText("");
            totalAmountText.setText("");
            //grandTotalText.setText("");
            Calendar cal = Calendar.getInstance();
            datePicker.setDate(cal.getTime());
        }

    }

    public void fillOidByCustId() {
        orderIdCombo.removeAllItems();
        ResultSet rst = null;
        ArrayList<Orders> orderList = null;
        if (customerIdCombo.getEditor().getItem().toString().isEmpty()) {
            fillALLOid();
        } else {
            try {
                orderList = OrdersController.searchOrderByCustId(customerIdCombo.getEditor().getItem().toString());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ViewOrderPayments.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ViewOrderPayments.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (orderList != null) {
                for (Orders orders : orderList) {
                    orderIdCombo.addItem(orders.getOid());
                }
            } else {
                fillALLOid();
                totalAmountText.setText("");
                amountText.setText("");
            }

        }
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
                JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
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
                JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
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
        table = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        customerCombo = new javax.swing.JComboBox();
        orderIdCombo = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        grandTotalText = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        totalAmountText = new javax.swing.JTextField();
        customerIdCombo = new javax.swing.JComboBox();
        custNicText = new javax.swing.JTextField();
        opidCombo = new javax.swing.JComboBox();
        amountText = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Payments");

        table.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order ID", "Customer ID", "Order Date", "OPID", "Payment Date", "Amount ", "Payment Method"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Details"));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel3.setText("Customer ID");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel4.setText("Customer Name");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setText("Order ID");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel6.setText("Payment Date");

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jButton3.setText("ADD New");
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

        orderIdCombo.setEditable(true);
        orderIdCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        orderIdCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderIdComboActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel9.setText("Payment ID");

        grandTotalText.setEditable(false);
        grandTotalText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel8.setText("GrandTotal");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel10.setText("Total Amount Paid For Order");

        totalAmountText.setEditable(false);
        totalAmountText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        customerIdCombo.setEditable(true);
        customerIdCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        customerIdCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerIdComboActionPerformed(evt);
            }
        });

        custNicText.setEditable(false);
        custNicText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        opidCombo.setEditable(true);
        opidCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        opidCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opidComboActionPerformed(evt);
            }
        });

        amountText.setEditable(false);
        amountText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel11.setText("Amount");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customerCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(orderIdCombo, 0, 215, Short.MAX_VALUE)
                                    .addComponent(customerIdCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(opidCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11)
                                .addComponent(custNicText, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                                .addGap(28, 28, 28)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(totalAmountText, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11)
                                .addGap(25, 25, 25)
                                .addComponent(amountText, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(grandTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(customerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(customerIdCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(custNicText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(orderIdCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(opidCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(grandTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(amountText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(totalAmountText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jButton1.setText("Cancal");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jButton2.setText("Delete Selected");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(19, Short.MAX_VALUE))
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

    private void customerIdComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerIdComboActionPerformed
        fillOidByCustId();
        //nic text fill
        Customer cstmr = null;
        try {
            cstmr = CustomerController.searchCustomer(customerIdCombo.getEditor().getItem().toString());
        } catch (ClassNotFoundException ex) {
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        }
        if (cstmr != null) {
            custNicText.setText(cstmr.getNic());
        } else {
            custNicText.setText("");
        }
        getPaymentAndOrderDetails();
    }//GEN-LAST:event_customerIdComboActionPerformed

    private void customerComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerComboActionPerformed
        grandTotalText.setText("");
        fillCustomerIdComboByName();
        fillOidByCustId();
        Customer cstmr = null;
        try {
            cstmr = CustomerController.searchCustomer(customerIdCombo.getEditor().getItem().toString());
        } catch (ClassNotFoundException ex) {
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        }
        if (cstmr != null) {
            custNicText.setText(cstmr.getNic());
        } else {
            custNicText.setText("");
        }
        getPaymentAndOrderDetails();
    }//GEN-LAST:event_customerComboActionPerformed

    private void orderIdComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderIdComboActionPerformed
        fillPidByOid();
        getPaymentAndOrderDetails();// TODO add your handling code here:
    }//GEN-LAST:event_orderIdComboActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        NewOrderPayment nop = new NewOrderPayment(this);
        nop.setVisible(true);
        nop.oidText.setText(orderIdCombo.getEditor().getItem().toString());

    }//GEN-LAST:event_jButton3ActionPerformed

    private void opidComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opidComboActionPerformed
        getPaymentAndOrderDetails();        // TODO add your handling code here:
    }//GEN-LAST:event_opidComboActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int a = 0;
        try {
            a = OrderPaymentsController.deleteOrderPayment(opidCombo.getEditor().getItem().toString());
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, ex.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, "Please Select Order Payment To Delete");
        }
        if (a > 0) {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, "Deleted");
            fillTable();
        } else {
            JOptionPane.showMessageDialog(ViewOrderPayments.this, "Error");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(ViewOrderPayments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewOrderPayments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewOrderPayments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewOrderPayments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewOrderPayments().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amountText;
    private javax.swing.JTextField custNicText;
    private javax.swing.JComboBox customerCombo;
    private javax.swing.JComboBox customerIdCombo;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JTextField grandTotalText;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox opidCombo;
    private javax.swing.JComboBox orderIdCombo;
    private javax.swing.JTable table;
    private javax.swing.JTextField totalAmountText;
    // End of variables declaration//GEN-END:variables
}
