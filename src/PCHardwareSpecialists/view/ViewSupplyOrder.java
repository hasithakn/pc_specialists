/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.view;

import PCHardwareSpecialists.controller.CustomerController;
import PCHardwareSpecialists.controller.OrdersController;
import PCHardwareSpecialists.controller.SupplierController;
import PCHardwareSpecialists.controller.SupplyOrderController;
import PCHardwareSpecialists.main.Main;
import PCHardwareSpecialists.model.Customer;
import PCHardwareSpecialists.model.Orders;
import PCHardwareSpecialists.model.Supplier;
import PCHardwareSpecialists.model.SupplyOrder;
import PCHardwareSpecialists.view.NewSupplyOrder;
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
public class ViewSupplyOrder extends javax.swing.JInternalFrame {

    Main m = null;

    /**
     * Creates new form Invoice
     */
    public ViewSupplyOrder(Main mm) {
        initComponents();
        setTitle("Orders");
        fillSupplierCombo();
        fillOrderTable();
        this.m = mm;
        AutoCompleteDecorator.decorate(supplierCombo);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int selectedRow = table.getSelectedRow();
                    String soid = table.getValueAt(selectedRow, 0).toString();
                    String supId = table.getValueAt(selectedRow, 1).toString();
                    String supName = table.getValueAt(selectedRow, 2).toString();
                    String sODate = table.getValueAt(selectedRow, 3).toString();

                    soidText.setText(soid);
                    datePicker.setDate(Date.valueOf(sODate));
                    supplierCombo.getEditor().setItem(supName);
                    supIdText.setText(supId);
                    soidCombo.getEditor().setItem(soid);
                } catch (Exception ex) {
                    soidText.setText("");        // TODO add your handling code here:
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    datePicker.setDate(cal.getTime());
                    grandTotalText.setText("");
                    fillSupplierCombo();
                    fillSupplierDetailsByName();

                }
            }
        });
    }

    public ViewSupplyOrder() {
        initComponents();
        setTitle("Orders");
        fillSupplierCombo();
        fillOrderTable();

        AutoCompleteDecorator.decorate(supplierCombo);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int selectedRow = table.getSelectedRow();
                    String soid = table.getValueAt(selectedRow, 0).toString();
                    String supId = table.getValueAt(selectedRow, 1).toString();
                    String supName = table.getValueAt(selectedRow, 2).toString();
                    String sODate = table.getValueAt(selectedRow, 3).toString();

                    soidText.setText(soid);
                    datePicker.setDate(Date.valueOf(sODate));
                    supplierCombo.getEditor().setItem(supName);
                    supIdText.setText(supId);
                    soidCombo.getEditor().setItem(soid);
                } catch (Exception ex) {
                    soidText.setText("");        // TODO add your handling code here:
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    datePicker.setDate(cal.getTime());
                    grandTotalText.setText("");
                    fillSupplierCombo();
                    fillSupplierDetailsByName();

                }
            }
        });
    }

    public void fillSupplierCombo() {
        supplierCombo.removeAllItems();
        ArrayList<Supplier> supList = null;
        try {
            supList = SupplierController.getAllSupplier();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
        }

        for (Supplier supplier : supList) {
            supplierCombo.addItem(supplier.getSupName());
            AutoCompleteDecorator.decorate(supplierCombo);
        }
    }

    public void fillGrandTotalText() {

    }

    public void fillALLOid() {
        ArrayList<SupplyOrder> suporderList = null;
        soidCombo.removeAllItems();
        try {
            suporderList = SupplyOrderController.getAllSupplyOrders();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
        }
        for (SupplyOrder orders : suporderList) {
            soidCombo.addItem(orders.getSoid());
        }

    }

    public void fillOidBySupId() {
        soidCombo.removeAllItems();
        ResultSet rst = null;
        ArrayList<SupplyOrder> suporderList = null;
        if (supIdText.getText().isEmpty()) {
            fillALLOid();
        } else {
            try {
                suporderList = SupplyOrderController.searchOrderBySupId(supIdText.getText());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ViewSupplyOrder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ViewSupplyOrder.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (SupplyOrder supOrders : suporderList) {
                soidCombo.addItem(supOrders.getSoid());
            }
        }
    }

    public void fillOrderTable() {
        ArrayList<SupplyOrder> orderList = null;
        try {
            orderList = SupplyOrderController.getAllSupplyOrders();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
        }
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0);
        for (SupplyOrder orders : orderList) {
            Supplier supplier = null;
            try {
                supplier = SupplierController.searchSupplier(orders.getSupId());
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
            }
            if (supplier != null) {
                Object[] rowData = {orders.getSoid(), supplier.getSupId(), supplier.getSupName(), orders.getSupDate()};
                dtm.addRow(rowData);
            } else {
                Object[] rowData = {orders.getSoid(), "--", "--", orders.getSupDate()};
                dtm.addRow(rowData);
            }
        }
    }

    public void fillSupplierDetailsByName() {
        ArrayList<Supplier> supplierlist = null;
        if (!supplierCombo.getEditor().getItem().toString().isEmpty()) {
            try {
                supplierlist = SupplierController.searchSupplierByName(supplierCombo.getEditor().getItem().toString());
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
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
        soidCombo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        grandTotalText = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        soidText = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(" Supply Orders");

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

        soidCombo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        soidCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soidComboActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel4.setText("Supply Order ID");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setText("Grand Total");

        grandTotalText.setEditable(false);
        grandTotalText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(supplierCombo, 0, 327, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(supIdText)
                            .addComponent(soidCombo, 0, 108, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(grandTotalText)
                        .addGap(18, 18, 18)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(supplierCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(supIdText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(grandTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(soidCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))))
        );

        table.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SOID", "Supplier ID", "Supplier Name", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Supply Order Details"));

        soidText.setEditable(false);
        soidText.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel20.setText("Supply Order Date");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel21.setText("Supply Order ID");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(soidText)
                    .addComponent(datePicker, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jLabel21)
                    .addContainerGap(210, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(soidText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(32, Short.MAX_VALUE)))
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

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jButton5.setText("View Items");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jButton7.setText("New Supply Order");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jButton6.setText("View Payments");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addContainerGap())
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
        fillSupplierDetailsByName();
        fillOidBySupId();// TODO add your handling code here:
    }//GEN-LAST:event_supplierComboActionPerformed

    private void soidComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soidComboActionPerformed
        SupplyOrder so = null;
        try {
            so = SupplyOrderController.searchSupplyOrder(soidCombo.getSelectedItem().toString());
            if (so != null) {
                soidText.setText(so.getSoid());        // TODO add your handling code here:
                datePicker.setDate(so.getSupDate());
                int a1 = table.getRowCount();
                int b = 0;
                String text = soidText.getText();
                for (int i = 0; i < a1; i++) {
                    if (table.getValueAt(i, 0).toString().equals(text)) {
                        break;
                    } else {
                        b++;
                    }
                }
                if (!soidText.getText().isEmpty()) {
                    ListSelectionModel selectionModel = table.getSelectionModel();
                    selectionModel.setSelectionInterval(0, b);
                } else {
                    ListSelectionModel selectionModel = table.getSelectionModel();
                    selectionModel.setSelectionInterval(0, table.getRowCount());
                }
            } else {
                soidText.setText("");        // TODO add your handling code here:
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                datePicker.setDate(cal.getTime());
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
            soidText.setText("");        // TODO add your handling code here:
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            datePicker.setDate(cal.getTime());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
            soidText.setText("");        // TODO add your handling code here:
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            datePicker.setDate(cal.getTime());
        } catch (Exception ex) {
            soidText.setText("");        // TODO add your handling code here:
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            datePicker.setDate(cal.getTime());

        }

        // TODO add your handling code here:
    }//GEN-LAST:event_soidComboActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (!soidCombo.getEditor().getItem().toString().isEmpty()) {
            try {
                String soid = table.getValueAt(table.getSelectedRow(), 0).toString();
                int a = SupplyOrderController.deleteSupplyOrderFully(soid);
                if (a > 0) {
                    JOptionPane.showMessageDialog(ViewSupplyOrder.this, "Delete Success");
                    fillOrderTable();
                    fillOidBySupId();
                } else {
                    JOptionPane.showMessageDialog(ViewSupplyOrder.this, "Delete Failed");
                }
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewSupplyOrder.this, ex.getMessage());
            } /*catch (Exception ex) {
             JOptionPane.showMessageDialog(ViewSupplyOrder.this, "Please select Supply Order to delete ");
             System.out.println(ex.getMessage());
             }*/

        } else {
            JOptionPane.showMessageDialog(ViewSupplyOrder.this, "Please select Supply Order to delete");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if (m != null) {
            NewSupplyOrder b = new NewSupplyOrder(m);
            b.setVisible(true);
            m.desktopPane.removeAll();
            m.desktopPane.add(b);
            m.desktopPane.repaint();
            m.tabPane.setSelectedIndex(1);
        } else {
        }
       // new NewSupplyOrder(this).setVisible(true);    // TODO add your handling code here:

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (!table.getValueAt(table.getSelectedRow(), 0).toString().isEmpty() || table.getSelectedRow() != -1) {
            ViewSupplyOrderItems a = new ViewSupplyOrderItems(this);
            a.setVisible(true);
            a.soidText.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
            a.supIdText.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
            a.supplierCombo.getEditor().setItem(table.getValueAt(table.getSelectedRow(), 2).toString());
            a.datePicker.setDate(Date.valueOf(table.getValueAt(table.getSelectedRow(), 3).toString()));
            a.fillTable();
        } else {
            JOptionPane.showMessageDialog(ViewSupplyOrder.this, "Please select Supply Order to view");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

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
            java.util.logging.Logger.getLogger(ViewSupplyOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewSupplyOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewSupplyOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewSupplyOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewSupplyOrder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JTextField grandTotalText;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox soidCombo;
    private javax.swing.JTextField soidText;
    private javax.swing.JTextField supIdText;
    private javax.swing.JComboBox supplierCombo;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
