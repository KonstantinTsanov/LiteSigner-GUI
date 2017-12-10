/* 
 * The MIT License
 *
 * Copyright 2017 Konstantin Tsanov <k.tsanov@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ktsanov.gui.jpanels;

import com.ktsanov.interfaces.FrameControls;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import lombok.extern.java.Log;
import net.miginfocom.swing.MigLayout;
import com.ktsanov.core.LiteSignerManager;
import com.ktsanov.interfaces.DevicePanel;
import javax.swing.JOptionPane;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
@Log
public class SelectingDeviceJPanel extends JPanel implements DevicePanel {

    private JLabel selectingDeviceJLabel;
    private JButton logInDeviceJButton;
    public JButton backJButton;
    //Contains tokens descriptions and status
    DefaultTableModel tokensModel;
    private JTable deviceTable;
    private JScrollPane deviceScrollPane;
    private final JFrame parent;
    //Used for the error messages.

    public SelectingDeviceJPanel(JFrame parent) {
        this.parent = parent;
        MigLayout layout = new MigLayout("", "[grow][grow]", "[shrink 0][grow][shrink 0]");
        setLayout(layout);
        initTokensModel();
        initComponents();
        addComponents();
        attachListeners();

    }

    private void initComponents() {
        selectingDeviceJLabel = new JLabel();
        logInDeviceJButton = new JButton();
        backJButton = new JButton();
        deviceTable = new JTable(tokensModel) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        deviceTable.setColumnSelectionAllowed(false);
        deviceTable.setRowSelectionAllowed(true);
        deviceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        deviceScrollPane = new JScrollPane(deviceTable);
        deviceScrollPane.getViewport().setBackground(Color.WHITE);
    }

    private void addComponents() {
        add(selectingDeviceJLabel, "span");
        add(deviceScrollPane, "grow, span, wrap");
        add(backJButton, "left");
        add(logInDeviceJButton, "span 2, right");
    }

    public void setComponentText() {
        ResourceBundle r = ResourceBundle.getBundle("Bundle");
        selectingDeviceJLabel.setText(r.getString("selectingDeviceJPanel.selectDeviceLabel"));
        logInDeviceJButton.setText(r.getString("selectingDeviceJPanel.logInButton"));
        backJButton.setText(r.getString("selectingDeviceJPanel.backJButton"));
        TableColumnModel thm = deviceTable.getTableHeader().getColumnModel();
        thm.getColumn(0).setHeaderValue(r.getString("selectingDeviceJPanel.tokenDescription"));
        thm.getColumn(1).setHeaderValue(r.getString("selectingDeviceJPanel.tokenStatus"));
        repaint();
    }

    private void initTokensModel() {
        tokensModel = new DefaultTableModel() {
            ResourceBundle r = ResourceBundle.getBundle("Bundle");
            String[] certProps = {r.getString("selectingDeviceJPanel.tokenDescription"),
                r.getString("selectingDeviceJPanel.tokenStatus")};

            @Override
            public int getColumnCount() {
                return certProps.length;
            }

            @Override
            public String getColumnName(int index) {
                return certProps[index];
            }
        };
    }

    private void attachListeners() {
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                LiteSignerManager.getInstance().runDeviceScanner(1);
            }

            @Override
            public void ancestorRemoved(AncestorEvent ae) {

            }

            @Override
            public void ancestorMoved(AncestorEvent ae) {
            }
        }
        );
        //Back button listener
        backJButton.addActionListener((ActionEvent e) -> {
            ((FrameControls) parent).showChooseOptionPanel();
        });
        //Log in button listener
        logInDeviceJButton.addActionListener((ActionEvent e) -> {
            if (deviceTable.getSelectedRow() != -1) {
                Object slotDescription = deviceTable.getValueAt(deviceTable.getSelectedRow(), 0);
                LiteSignerManager.getInstance().deviceLogIn(slotDescription.toString());
            } else {
                ResourceBundle r = ResourceBundle.getBundle("Bundle");
                JOptionPane.showMessageDialog(parent, r.getString("selectingDeviceJPanel.noTokenSelectedError"),
                        r.getString("errorMessage.title"), JOptionPane.WARNING_MESSAGE);
            }
        });
        //Table listener
        deviceTable.getSelectionModel().addListSelectionListener((ListSelectionEvent lse) -> {
            LiteSignerManager.getInstance().clearCertificateList();
            if (!lse.getValueIsAdjusting()) {
                if (deviceTable.getSelectedRow() != -1) {
                    LiteSignerManager.getInstance().displayCertificates(deviceTable.getValueAt(deviceTable.getSelectedRow(), 0).toString());
                }
            }
        });
    }

    @Override
    public DefaultTableModel getTokensModel() {
        return tokensModel;
    }

    @Override
    public JFrame getPanelParent() {
        return parent;
    }

    @Override
    public JTable getTokensTable() {
        return deviceTable;
    }
}
