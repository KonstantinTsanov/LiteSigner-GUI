/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.jpanels;

import callbacks.impl.PasswordJOptionPane;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import net.miginfocom.swing.MigLayout;
import pkcs.Pkcs11;
import tools.DeviceManager;

/**
 *
 * @author KTsan
 */
public class SelectingDeviceJPanel extends JPanel {

    private JLabel selectingDeviceJLabel;
    private JButton logInDeviceJButton;
    private JButton nextStepJButton;
    private JButton refreshButton;
    Map<String, Map.Entry<Integer, File>> tokensDescription;
    DefaultListModel<String> model = new DefaultListModel<>();
    private JList<String> deviceList;
    private JScrollPane deviceScrollPane;

    public SelectingDeviceJPanel() {
        tokensDescription = new HashMap<>();
        MigLayout layout = new MigLayout("", "[grow][grow]", "[shrink 0][grow][shrink 0]");
        setLayout(layout);
        initComponents();
        addComponents();
        setComponentText(new Locale("bg", "BG"));
        attachListeners();
    }

    private void initComponents() {
        selectingDeviceJLabel = new JLabel();
        logInDeviceJButton = new JButton();
        nextStepJButton = new JButton();
        refreshButton = new JButton(new ImageIcon("src/main/resources/images/refresh.png"));
        deviceList = new JList<>(model);
        deviceScrollPane = new JScrollPane(deviceList);
    }

    private void addComponents() {
        add(selectingDeviceJLabel, "span");
        add(deviceScrollPane, "grow, span, wrap");
        add(logInDeviceJButton);
        add(refreshButton);
        add(nextStepJButton, "span 3");
    }

    private void setComponentText(Locale locale) {
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
        selectingDeviceJLabel.setText(r.getString("selectingDeviceJPanel.selectDeviceLabel"));
        logInDeviceJButton.setText(r.getString("selectingDeviceJPanel.logInButton"));
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
        refreshButton.addActionListener((ae) -> {
            displayDevices();
        });
        logInDeviceJButton.addActionListener((ae) -> {
            String selectedFromTheList = deviceList.getSelectedValue();
            Map.Entry<Integer, File> selectedDevice = tokensDescription.get(selectedFromTheList);
            System.out.println(selectedFromTheList);
            //[slotListIndex][driver]
            Pkcs11 smartcard = new Pkcs11(selectedDevice.getKey(), selectedDevice.getValue());
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    smartcard.initGuiHandler(new PasswordJOptionPane(null));
                    smartcard.login();
                }
            });
            thread1.start();
        });
    }

    private void displayDevices() {
        Thread t = new Thread() {
            @Override
            public  void run() {
                Map<String, Map.Entry<Integer, File>> devices = DeviceManager.getInstance().SearchForDevices();
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            devices.forEach((description, indexAndDriver) -> {
                                if (tokensDescription.containsKey(description) == false) {
                                    tokensDescription.put(description, indexAndDriver);
                                    model.addElement(description);
                                }
                            });
                            tokensDescription.forEach((description, indexAndDriver) -> {
                                if (devices.containsKey(description) == false) {
                                    tokensDescription.remove(description);
                                    model.removeElement(description);
                                }
                            });
                        }
                    });
                } catch (InterruptedException ex) {
                    Logger.getLogger(SelectingDeviceJPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(SelectingDeviceJPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(t, 0, 1, TimeUnit.SECONDS);
        t.start();
    }
}
