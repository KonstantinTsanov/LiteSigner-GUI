/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.jpanels;

import callbacks.FrameControls;
import litesigner.callbacks.impl.PasswordJOptionPane;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
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

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
@Log
public class SelectingDeviceJPanel extends JPanel {

    private JLabel selectingDeviceJLabel;
    private JButton logInDeviceJButton;
    private JButton backJButton;
    Map<String, Map.Entry<Integer, File>> tokensDescription;
    DefaultListModel<String> model = new DefaultListModel<>();
    private JList<String> deviceList;
    private JScrollPane deviceScrollPane;
    private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    private final JFrame parent;

    public SelectingDeviceJPanel(JFrame parent) {
        this.parent = parent;
        tokensDescription = new HashMap<>();
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
        deviceList = new JList<>(model);
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
                displayDevices();
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
                Map.Entry<Integer, File> selectedDevice = tokensDescription.get(selectedFromTheList);
                System.out.println(selectedFromTheList);
                //[slotListIndex][driver]
                Pkcs11 smartcard = new Pkcs11(selectedDevice.getKey(), selectedDevice.getValue());
                Thread thread1 = new Thread(() -> {
                    smartcard.initGuiHandler(new PasswordJOptionPane(null));
                    smartcard.login();
                });
                thread1.start();
            }
        });

    }

    private void displayDevices() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Map<String, Map.Entry<Integer, File>> devices = DeviceManager.getInstance().scanForUSBDevices();
                    if (devices != null) {
                        SwingUtilities.invokeLater(() -> {
                            devices.forEach((description, indexAndDriver) -> {
                                if (tokensDescription.containsKey(description) == false) {
                                    tokensDescription.put(description, indexAndDriver);
                                    model.addElement(description);
                                }
                            });
                            for (Iterator<Map.Entry<String, Map.Entry<Integer, File>>> it = tokensDescription.entrySet().iterator(); it.hasNext();) {
                                String description = it.next().getKey();
                                if (devices.containsKey(description) == false) {
                                    it.remove();
                                    model.removeElement(description);
                                }
                            }
                        });
                    }
                } catch (PKCS11Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "There was a problem with the device.");
                    });
                }
            }
        };
        exec.scheduleAtFixedRate(t, 0, 1, TimeUnit.SECONDS);
        t.start();
    }

    public boolean cancelDeviceScanner() {
        exec.shutdown();
        try {
            return exec.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            log.log(Level.SEVERE, "Failed to clean things up before closing the application!", ex);
            return false;
        }
    }
}
