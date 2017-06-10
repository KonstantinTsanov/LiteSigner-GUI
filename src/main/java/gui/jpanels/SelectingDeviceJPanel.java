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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import lombok.extern.java.Log;
import net.miginfocom.swing.MigLayout;
import core.LiteSignerManager;
import javax.swing.JOptionPane;
import callbacks.SelectingDeviceLayout;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
@Log
public class SelectingDeviceJPanel extends JPanel implements SelectingDeviceLayout {

    private JLabel selectingDeviceJLabel;
    private JButton logInDeviceJButton;
    public JButton backJButton;
    //Contains ONLY tokenDescriptions
    DefaultListModel<String> tokensModel = new DefaultListModel<>();
    private JList<String> deviceList;
    private JScrollPane deviceScrollPane;
    private final JFrame parent;
    //Used for the error messages.
    private Locale currentLocale;

    public SelectingDeviceJPanel(JFrame parent) {
        this.parent = parent;
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
        currentLocale = locale;
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
        selectingDeviceJLabel.setText(r.getString("selectingDeviceJPanel.selectDeviceLabel"));
        logInDeviceJButton.setText(r.getString("selectingDeviceJPanel.logInButton"));
        backJButton.setText(r.getString("selectingDeviceJPanel.backJButton"));
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
        backJButton.addActionListener((ActionEvent e) -> {
            ((FrameControls) parent).showChooseOptionLayout();
        });
        logInDeviceJButton.addActionListener((ActionEvent e) -> {
            String slotDescription = deviceList.getSelectedValue();
            if (slotDescription != null) {
                LiteSignerManager.getInstance().deviceLogIn(slotDescription);
            } else {
                ResourceBundle r = ResourceBundle.getBundle("Bundle", currentLocale);
                JOptionPane.showMessageDialog(parent, r.getString("selectingDeviceJPanel.noTokenSelectedError"), r.getString("selectingDeviceJPanel.title"), JOptionPane.WARNING_MESSAGE);
            }
        });

    }

    @Override
    public DefaultListModel<String> getTokensModel() {
        return tokensModel;
    }

    @Override
    public JFrame getLayoutParent() {
        return parent;
    }
}
