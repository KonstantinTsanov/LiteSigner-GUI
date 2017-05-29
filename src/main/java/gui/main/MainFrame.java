/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.main;

import gui.jpanels.SelectingDeviceJPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author KTsan
 */
public class MainFrame extends JFrame {

    private CardLayout menuLayout, signingLayout;
    private JPanel menuJPanel, signingJPanel;

    private JSplitPane splitPaneH;

    private SelectingDeviceJPanel deviceSelectionPanel;

    public MainFrame() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        initComponents();
        setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));
        menuJPanel.setLayout(menuLayout);
        signingJPanel.setLayout(signingLayout);
        signingJPanel.add(deviceSelectionPanel, deviceSelectionPanel.getClass().toString());
        splitPaneH.setDividerLocation(0.5);
        splitPaneH.setResizeWeight(0.5);
        add(splitPaneH, BorderLayout.CENTER);
        splitPaneH.setLeftComponent(signingJPanel);
        splitPaneH.setRightComponent(menuJPanel);
        pack();
    }

    private void initComponents() {
        menuLayout = new CardLayout();
        signingLayout = new CardLayout();
        menuJPanel = new JPanel();
        signingJPanel = new JPanel();
        splitPaneH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        deviceSelectionPanel = new SelectingDeviceJPanel();
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
