/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.jpanels;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author KTsan
 */
public class SelectingDeviceJPanelNew extends JPanel {

    private JLabel selectingDeviceJLabel;
    private JButton logInDeviceJButton;
    private JButton nextStepJButton;

    public SelectingDeviceJPanelNew() {
        MigLayout layout = new MigLayout("", "100!", "60!");
        setLayout(layout);
        initComponents();
    }

    private void initComponents() {
        selectingDeviceJLabel = new JLabel();
        logInDeviceJButton = new JButton();
        nextStepJButton = new JButton();
    }
}
