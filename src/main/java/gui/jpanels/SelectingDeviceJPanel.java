/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.jpanels;

import callbacks.FrameControls;
import callbacks.SelectingDeviceJPanelControls;
import java.awt.Color;
import java.awt.Component;
import litesigner.callbacks.impl.PasswordJOptionPane;
import java.io.File;
import java.security.KeyStoreException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import lombok.extern.java.Log;
import net.miginfocom.swing.MigLayout;
import pkcs.Pkcs11;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tools.DeviceManager;
import tools.LiteSignerManager;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
@Log
public class SelectingDeviceJPanel extends JPanel implements SelectingDeviceJPanelControls {
    
    private JLabel selectingDeviceJLabel;
    private JButton logInDeviceJButton;
    private JButton backJButton;
    //Must be able to control both description and the entry containing slotIndex and driver
    Map<String, Map.Entry<Integer, File>> tokensList;
    //Contains ONLY descriptions
    DefaultListModel<String> tokensModel = new DefaultListModel<>();
    private JList<String> deviceList;
    private JScrollPane deviceScrollPane;
    private final JFrame parent;
    
    
    public SelectingDeviceJPanel(JFrame parent) {
        this.parent = parent;
        tokensList = new HashMap<>();
        MigLayout layout = new MigLayout("", "[grow][grow]", "[shrink 0][grow][shrink 0]");
        setLayout(layout);
        initComponents();
        addComponents();
        attachListeners();
        
    }
    
    private void initComponents() {
        selectingDeviceJLabel = new JLabel();
        logInDeviceJButton = new JButton();
        backJButton = new JButton();
        deviceList = new JList<>(tokensModel);
        deviceScrollPane = new JScrollPane(deviceList);
    }
    
    private void addComponents() {
        add(selectingDeviceJLabel, "span");
        add(deviceScrollPane, "grow, span, wrap");
        add(backJButton, "left");
        add(logInDeviceJButton, "span 2, right");
    }
    
    public void setComponentText(Locale locale) {
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
        selectingDeviceJLabel.setText(r.getString("selectingDeviceJPanel.selectDeviceLabel"));
        logInDeviceJButton.setText(r.getString("selectingDeviceJPanel.logInButton"));
        backJButton.setText(r.getString("selectingDeviceJPanel.backJButton"));
    }
    
    private void attachListeners() {
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                //setting the scanner to check for devices every 1 second
                LiteSignerManager.getInstance().runDeviceScanner(1);
            }
            
            @Override
            public void ancestorRemoved(AncestorEvent ae) {
                
            }
            
            @Override
            public void ancestorMoved(AncestorEvent ae) {
            }
        });
        backJButton.addActionListener((al) -> {
            ((FrameControls) parent).showChooseOptionLayout();
        });
        logInDeviceJButton.addActionListener((al) -> {
            String selectedFromTheList = deviceList.getSelectedValue();
            if (selectedFromTheList != null) {
                deviceLogIn(selectedFromTheList);
            }
        });
        
    }

    //TODO work on it
    private void deviceLogIn(String selectedFromTheList) {
        Entry<Integer, File> selectedDevice = tokensList.get(selectedFromTheList);
        Pkcs11 smartcard = new Pkcs11(selectedDevice.getKey(), selectedDevice.getValue());
        smartcard.initGuiHandler(new PasswordJOptionPane(null));
        try {
            smartcard.login();
           // tokensModel.setElementAt(tokensModel.elementAt(tokensModel.indexOf(selectedFromTheList)).concat(loggedIn), tokensModel.indexOf(selectedFromTheList));
        } catch (KeyStoreException ex) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(parent, "There is a problem with the device.");
            });
        }
    }
    
    @Override
    public DefaultListModel<String> getTokensModel() {
        return tokensModel;
    }
    
    @Override
    public JFrame getParent() {
        return parent;
    }
}
