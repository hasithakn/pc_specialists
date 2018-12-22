/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PCHardwareSpecialists.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author Hasithakn
 */
public class ComboSearch {

    public void setSearchableComboBox(final JComboBox comboBox, final String whenNoData) {
        comboBox.setEditable(true);
        final JTextField textField = (JTextField) comboBox.getEditor().getEditorComponent();
        final Object[] elements = new Object[comboBox.getItemCount()];
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            elements[i] = comboBox.getItemAt(i);
        }
        textField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                comboBox.hidePopup();
                String text = textField.getText();
                comboBox.removeAllItems();
                for (Object object : elements) {
                    if (object.toString().toUpperCase().contains(text.toUpperCase())) {
                        comboBox.addItem(object);
                    }
                }
                comboBox.setSelectedItem(null);
                if (comboBox.getItemCount() == 0) {
                    comboBox.addItem(whenNoData);
                }
                comboBox.showPopup();
                textField.setText(text);
            }

        });
    }
}
