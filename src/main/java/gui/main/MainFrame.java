/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.main;

import gui.jpanels.ChooseAnOptionJPanel;
import gui.jpanels.SelectingDeviceJPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import lombok.extern.java.Log;
import net.miginfocom.swing.MigLayout;
import org.usb4java.LibUsb;
import callbacks.FrameControls;
import enums.Languages;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
@Log
public class MainFrame extends JFrame implements FrameControls {

    private CardLayout deviceLayout, signingLayout;
    private JPanel deviceJPanel, signingJPanel;

    private JSplitPane splitPaneH;

    private SelectingDeviceJPanel deviceSelectionPanel;
    private ChooseAnOptionJPanel chooseAnOptionJPanel;

    private final JMenuBar topMenuBar;
    private JMenu fileJMenu, optionsJMenu, languageJMenu;
    private JMenuItem exitJMenuItem;

    public MainFrame() {
        setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));
        setTitle("LiteSigner");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        topMenuBar = new JMenuBar();
        initMenuBar();
        initOptionsLayout();
        initSigningLayout();
        setLanguage(getLocaleFromPreferences());
        add(chooseAnOptionJPanel);
        pack();
    }

    private void initOptionsLayout() {
        chooseAnOptionJPanel = new ChooseAnOptionJPanel(this);
    }

    private void initSigningLayout() {
        deviceLayout = new CardLayout();
        signingLayout = new CardLayout();
        deviceJPanel = new JPanel();
        signingJPanel = new JPanel();
        splitPaneH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        deviceSelectionPanel = new SelectingDeviceJPanel(this);
        attachSigningListeners();
        deviceJPanel.setLayout(deviceLayout);
        signingJPanel.setLayout(signingLayout);
        signingJPanel.add(deviceSelectionPanel, deviceSelectionPanel.getClass().toString());
        splitPaneH.setDividerLocation(0.5);
        splitPaneH.setResizeWeight(0.5);
        splitPaneH.setLeftComponent(signingJPanel);
        splitPaneH.setRightComponent(deviceJPanel);
    }

    @Override
    public void showSigningLayout() {
        this.getContentPane().remove(chooseAnOptionJPanel);
        add(splitPaneH, BorderLayout.CENTER);
        pack();
        revalidate();
        repaint();
    }

    @Override
    public void showChooseOptionLayout() {
        this.getContentPane().remove(splitPaneH);
        add(chooseAnOptionJPanel, BorderLayout.CENTER);
        pack();
        revalidate();
        repaint();
    }

    private void attachSigningListeners() {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent winEvt) {
                deviceSelectionPanel.cancelDeviceScanner();
                try {
                    LibUsb.exit(null);
                } catch (IllegalStateException ex) {
                    //I dont care, the default context has not been initialized in the first place.
                }
                System.exit(0);
            }
        });
    }

    private void initMenuBar() {
        createInitFileMenu();
        createInitOptionsMenu();
        setJMenuBar(topMenuBar);
    }

    /**
     * creates and initializes the file menu and submenus
     */
    private void createInitFileMenu() {
        fileJMenu = new JMenu();
        fileJMenu.setMnemonic(KeyEvent.VK_F);
        exitJMenuItem = new JMenuItem("Exit");
        exitJMenuItem.setMnemonic(KeyEvent.VK_E);
        exitJMenuItem.setToolTipText("Exit application");
        exitJMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });
        fileJMenu.add(exitJMenuItem);
        topMenuBar.add(fileJMenu);
    }

    /**
     * Creates and initizlies the options menu and submenu
     */
    private void createInitOptionsMenu() {
        optionsJMenu = new JMenu();
        optionsJMenu.setMnemonic(KeyEvent.VK_O);
        languageJMenu = new JMenu();
        for (Languages lang : Languages.values()) {
            JMenuItem language = new JMenuItem(lang.getName());
            language.addActionListener((ActionEvent ActionEvent) -> {
                if (getLocaleFromPreferences() != lang.getLocale()) {
                    setLanguage(lang.getLocale());
                    setLocaleToPreferences(lang);
                    pack();
                }
            });
            languageJMenu.add(language);
        }
        optionsJMenu.add(languageJMenu);
        topMenuBar.add(optionsJMenu);
    }

    private void setLanguage(Locale locale) {
        deviceSelectionPanel.setComponentText(locale);
        chooseAnOptionJPanel.setComponentText(locale);
        setComponentText(locale);
    }

    /**
     * Gets the lastly used language from the registry(windows) or whatever it
     * is in linux
     *
     * @return new locale object, composed of the extracted parameters
     */
    private Locale getLocaleFromPreferences() {
        Preferences prefs = Preferences.userRoot().node(getClass().getName());
        String language = "Language";
        String country = "Country";
        return new Locale(prefs.get(language, "en"), prefs.get(country, "US"));
    }

    /**
     * Sets the lastly used language in the registry(windows) or whatever it is
     * in linux
     *
     * @param lang enum containing the required variables
     */
    private void setLocaleToPreferences(Languages lang) {
        Preferences prefs = Preferences.userRoot().node(getClass().getName());
        String language = "Language";
        String country = "Country";
        prefs.put(language, lang.getShortLanguage());
        prefs.put(country, lang.getShortCountry());
    }

    private void setComponentText(Locale locale) {
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
        fileJMenu.setText(r.getString("MainFrame.optionsMenu.fileJMenu"));
        exitJMenuItem.setText(r.getString("MainFrame.optionsMenu.exitJMenuItem"));
        optionsJMenu.setText(r.getString("MainFrame.optionsMenu.optionsJMenu"));
        languageJMenu.setText(r.getString("MainFrame.optionsMenu.languageJMenu"));
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
