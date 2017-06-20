/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.jpanels;

import callbacks.FrameControls;
import callbacks.SignatureVerificationPanel;
import core.LiteSignerManager;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import tools.FilesTool;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public class SignatureVerificationJPanel extends JPanel implements SignatureVerificationPanel {

    private JLabel selectFileLabel;

    private JTextField pkcs7;
    private JFileChooser pkcs7Chooser;
    private JButton pkcs7Button;

    private JLabel selectSignedFileLabel;
    private JTextField signedFile;
    private JFileChooser signedFileChooser;
    private JButton selectSignedFileButton;

    JTextArea signatureInformation;

    private JButton validateButton;
    private JButton backButton;
    private final JFrame parent;
    private Locale locale;

    public SignatureVerificationJPanel(JFrame parent, Locale locale) {
        this.parent = parent;
        this.locale = locale;
        MigLayout layout = new MigLayout("", "[grow][grow]", "[shrink 0][shrink 0][shrink 0][shrink 0][grow][shrink 0]");
        setLayout(layout);
        initComponents();
        addComponents();
        attachListeners();
    }

    private void initComponents() {
        //First field
        selectFileLabel = new JLabel();
        pkcs7 = new JTextField();
        pkcs7.setEditable(false);
        pkcs7Chooser = new JFileChooser();
        pkcs7Chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        pkcs7Button = new JButton("...");
        //Second field
        selectSignedFileLabel = new JLabel();
        signedFile = new JTextField();
        signedFile.setEditable(false);
        signedFileChooser = new JFileChooser();
        signedFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        selectSignedFileButton = new JButton("...");
        //Signature information
        signatureInformation = new JTextArea();
        signatureInformation.setLineWrap(true);
        validateButton = new JButton();
        backButton = new JButton();
    }

    private void addComponents() {
        add(selectFileLabel, "wrap");
        add(pkcs7, "growx");
        add(pkcs7Button, "wrap");
        add(selectSignedFileLabel, "wrap");
        add(signedFile, "growx");
        add(selectSignedFileButton, "wrap");
        add(signatureInformation, "span, growx, growy, wrap,wmin 10");
        add(backButton, "south");
        add(validateButton, "south");
    }

    public void setComponentText(Locale locale) {
        this.locale = locale;
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
        selectFileLabel.setText(r.getString("fileSignatureVerificationJPanel.selectSignatureOrAttachedFileLabel"));
        selectSignedFileLabel.setText(r.getString("fileSignatureVerificationJPanel.selectSignedFileLabel"));
        validateButton.setText(r.getString("signatureVerificationJPanel.validateButton"));
        backButton.setText(r.getString("fileSignatureVerificationJPanel.backButton"));
    }

    public void attachListeners() {
        pkcs7Button.addActionListener((ae) -> {
            int result = pkcs7Chooser.showOpenDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                if (FilesTool.checkIfFileExists(pkcs7Chooser.getSelectedFile())) {
                    pkcs7.setText(pkcs7Chooser.getSelectedFile().toString());
                } else {
                    ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
                    JOptionPane.showMessageDialog(parent, r.getString("fileSignatureVerificationJPanel.selectedFileDoesNotExist"),
                            r.getString("errorMessage.title"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        selectSignedFileButton.addActionListener((ae) -> {
            int result = signedFileChooser.showOpenDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                if (FilesTool.checkIfFileExists(signedFileChooser.getSelectedFile())) {
                    signedFile.setText(signedFileChooser.getSelectedFile().toString());
                } else {
                    ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
                    JOptionPane.showMessageDialog(parent, r.getString("fileSignatureVerificationJPanel.selectedFileDoesNotExist"),
                            r.getString("errorMessage.title"), JOptionPane.WARNING_MESSAGE);
                }
            } else {
                signedFileChooser.setSelectedFile(null);
            }
        });
        backButton.addActionListener((ae) -> {
            ((FrameControls) parent).showChooseOptionPanel();
        });
        validateButton.addActionListener((ae) -> {
            LiteSignerManager.getInstance().validateSignature(pkcs7Chooser.getSelectedFile(), signedFileChooser.getSelectedFile());
        });
    }

    @Override
    public JTextArea getSignatureDetailsJTextArea() {
        return signatureInformation;
    }

    @Override
    public JFrame getPanelParent() {
        return parent;
    }
}
