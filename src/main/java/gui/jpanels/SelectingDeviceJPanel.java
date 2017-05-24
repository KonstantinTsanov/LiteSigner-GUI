/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.jpanels;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import net.miginfocom.swing.MigLayout;
import tools.DeviceSearcher;

/**
 *
 * @author KTsan
 */
public class SelectingDeviceJPanel extends JPanel {

    private JLabel selectingDeviceJLabel;
    private JButton logInDeviceJButton;
    private JButton nextStepJButton;

    DefaultListModel<String> model = new DefaultListModel<>();
    private JList<String> deviceList;
    private JScrollPane deviceScrollPane;

    public SelectingDeviceJPanel() {
        MigLayout layout = new MigLayout("", "[grow][grow]", "[shrink 0][grow][shrink 0]");
        setLayout(layout);
        initComponents();
        addComponents();
        attachListeners();
    }

    private void initComponents() {
        selectingDeviceJLabel = new JLabel(" асдасдасд ");
        logInDeviceJButton = new JButton();
        nextStepJButton = new JButton();
        deviceList = new JList<>(model);
        deviceScrollPane = new JScrollPane(deviceList);
    }

    private void addComponents() {
        add(selectingDeviceJLabel, "span");
        add(deviceScrollPane, "grow, span, wrap");
        add(logInDeviceJButton);
        add(nextStepJButton, "span 2");
    }

    private void attachListeners() {
        addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
                DeviceSearcher.SearchForDevices().forEach((result) -> {
                    model.addElement(result);
                });
            }

            @Override
            public void ancestorRemoved(AncestorEvent ae) {

            }

            @Override
            public void ancestorMoved(AncestorEvent ae) {
            }
        });
    }
}
