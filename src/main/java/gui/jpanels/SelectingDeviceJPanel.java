/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.jpanels;

import callbacks.FrameControls;
import java.awt.event.ActionEvent;
import java.util.Locale;
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
import core.LiteSignerManager;
import javax.swing.JOptionPane;
import callbacks.DevicePanel;
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
    private Locale locale;

    public SelectingDeviceJPanel(JFrame parent, Locale locale) {
        this.locale = locale;
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

    public void setComponentText(Locale locale) {
        this.locale = locale;
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
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
            ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
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
            ((FrameControls) parent).showChooseOptionLayout();
        });
        //Log in button listener
        logInDeviceJButton.addActionListener((ActionEvent e) -> {
            Object slotDescription = deviceTable.getValueAt(deviceTable.getSelectedRow(), 0);
            if (slotDescription != null) {
                LiteSignerManager.getInstance().deviceLogIn(slotDescription.toString());
            } else {
                ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
                JOptionPane.showMessageDialog(parent, r.getString("selectingDeviceJPanel.noTokenSelectedError"), r.getString("selectingDeviceJPanel.title"), JOptionPane.WARNING_MESSAGE);
            }
        });
        //Table listener
        deviceTable.getSelectionModel().addListSelectionListener((ListSelectionEvent lse) -> {
            if (!lse.getValueIsAdjusting()) {
                LiteSignerManager.getInstance().clearCertificateList();
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
