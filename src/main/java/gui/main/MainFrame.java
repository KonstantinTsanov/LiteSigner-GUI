/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.main;

import gui.jpanels.SelectingOptionJPanel;
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
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import litesigner.callbacks.impl.PasswordJOptionPane;
import core.LiteSignerManager;
import gui.jpanels.SignatureVerificationJPanel;
import gui.jpanels.SelectingCertificateJPanel;
import gui.jpanels.SelectingFileAndSignatureJPanel;
import java.awt.Dimension;
import signers.Pkcs7;
import tools.CertificateVerifier;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
@Log
public class MainFrame extends JFrame implements FrameControls {

    private JPanel signingJPanel, deviceJPanel;

    private JSplitPane splitPaneH;

    private SelectingDeviceJPanel deviceSelectionPanel;
    private SelectingCertificateJPanel certificateSelectionPanel;
    private SelectingOptionJPanel optionSelectionPanel;
    private SelectingFileAndSignatureJPanel signatureSelectionJPanel;
    private SignatureVerificationJPanel signatureVerificationJPanel;
    private final JMenuBar topMenuBar;
    private JMenu fileJMenu, optionsJMenu, languageJMenu;
    private JMenuItem exitJMenuItem;

    private final Locale locale = getLocaleFromPreferences();

    /**
     * Constructor for the frame.
     */
    public MainFrame() {
        setMinimumSize(new Dimension(300, 250));
        setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));
        setTitle("LiteSigner");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        topMenuBar = new JMenuBar();
        initMenuBar();
        initOptionsPanel();
        initSigningPanels();
        initSignatureSelectionPanel();
        initSignatureVerificationPanel();
        setLanguage(locale);
        LiteSignerManager.getInstance().setComponents(deviceSelectionPanel, certificateSelectionPanel,
                new PasswordJOptionPane(this), signatureVerificationJPanel);
        add(optionSelectionPanel);
        pack();
    }

    /**
     * Initializes the options panel and builds the options layout.
     */
    private void initOptionsPanel() {
        optionSelectionPanel = new SelectingOptionJPanel(this);
    }

    /**
     * Initializes the signing panels and builds the singing layout.
     */
    private void initSigningPanels() {
        deviceJPanel = new JPanel();
        signingJPanel = new JPanel();
        splitPaneH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        deviceSelectionPanel = new SelectingDeviceJPanel(this, locale);
        certificateSelectionPanel = new SelectingCertificateJPanel(this, locale);
        attachListeners();
        deviceJPanel.add(deviceSelectionPanel, deviceSelectionPanel.getClass().toString());
        signingJPanel.add(certificateSelectionPanel, certificateSelectionPanel.getClass().getName());
        splitPaneH.setLeftComponent(deviceJPanel);
        splitPaneH.setRightComponent(signingJPanel);
        splitPaneH.setDividerLocation(0.5);
        splitPaneH.setResizeWeight(0.5);
    }

    private void initSignatureSelectionPanel() {
        signatureSelectionJPanel = new SelectingFileAndSignatureJPanel(this, locale);
    }

    private void initSignatureVerificationPanel() {
        signatureVerificationJPanel = new SignatureVerificationJPanel(this, locale);
    }

    /**
     * Hides the choose option panel and shows the panels related to signing.
     */
    @Override
    public void showSigningPanel() {
        setMinimumSize(new Dimension(650, 450));
        this.getContentPane().removeAll();
        this.getContentPane().add(splitPaneH, BorderLayout.CENTER);
        pack();
        revalidate();
        repaint();
    }

    /**
     * Hides the panels related to signing and shows the choose option panel.
     */
    @Override
    public void showChooseOptionPanel() {
        setMinimumSize(new Dimension(300, 250));
        this.getContentPane().removeAll();
        this.getContentPane().add(optionSelectionPanel, BorderLayout.CENTER);
        pack();
        revalidate();
        repaint();
    }

    @Override
    public void showFileAndSignaturePanel() {
        splitPaneH.remove(signingJPanel);
        splitPaneH.setRightComponent(signatureSelectionJPanel);
        pack();
        revalidate();
        repaint();
    }

    @Override
    public void hideFileAndSignaturePanel() {
        splitPaneH.remove(signatureSelectionJPanel);
        splitPaneH.setRightComponent(signingJPanel);
        pack();
        revalidate();
        repaint();
    }

    @Override
    public void showSignatureVerificationPanel() {
        setMinimumSize(new Dimension(550, 350));
        this.getContentPane().removeAll();
        this.getContentPane().add(signatureVerificationJPanel, BorderLayout.CENTER);
        pack();
        revalidate();
        repaint();
    }

    /**
     * Attaches the frame's listeners.
     */
    private void attachListeners() {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent winEvt) {
                LiteSignerManager.getInstance().cancelDeviceScanner();
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
     * creates and initializes the file menu and submenus.
     */
    private void createInitFileMenu() {
        fileJMenu = new JMenu();
        fileJMenu.setMnemonic(KeyEvent.VK_F);
        exitJMenuItem = new JMenuItem();
        exitJMenuItem.setMnemonic(KeyEvent.VK_E);
        exitJMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });
        fileJMenu.add(exitJMenuItem);
        topMenuBar.add(fileJMenu);
    }

    /**
     * Creates and initializes the options menu and submenu.
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

    /**
     * Sets the language for the whole application.
     *
     * @param locale Language to be used.
     */
    private void setLanguage(Locale locale) {
        deviceSelectionPanel.setComponentText(locale);
        optionSelectionPanel.setComponentText(locale);
        certificateSelectionPanel.setComponentText(locale);
        signatureSelectionJPanel.setComponentText(locale);
        signatureVerificationJPanel.setComponentText(locale);
        LiteSignerManager.getInstance().setLocale(locale);
        setComponentText(locale);
        PasswordJOptionPane.setPaneLocale(locale);
        Pkcs7.setLocale(locale);
        CertificateVerifier.setLocale(locale);
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

    /**
     * Sets the text for the frame's components.
     *
     * @param locale
     */
    private void setComponentText(Locale locale) {
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
        fileJMenu.setText(r.getString("MainFrame.optionsMenu.fileJMenu"));
        exitJMenuItem.setText(r.getString("MainFrame.optionsMenu.exitJMenuItem"));
        optionsJMenu.setText(r.getString("MainFrame.optionsMenu.optionsJMenu"));
        languageJMenu.setText(r.getString("MainFrame.optionsMenu.languageJMenu"));
    }

    /**
     * Entry point for the application.
     *
     * @param args
     */
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
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

}
