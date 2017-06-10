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

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public class SelectingCertificateJPanel extends JPanel {

    private JLabel selectingCertificateJLabel;
    private JButton signButton;
    private DefaultListModel<String> certificateModel = new DefaultListModel<>();
    private JList<String> certificateList;
    private JScrollPane deviceScrollPane;

}
