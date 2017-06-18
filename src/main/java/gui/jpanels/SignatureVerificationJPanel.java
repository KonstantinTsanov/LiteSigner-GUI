/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.jpanels;

import callbacks.FrameControls;
import java.awt.Dimension;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public class SignatureVerificationJPanel extends JPanel {

    private JLabel selectFileLabel;
    private JTextField inputFileLocation;
    private JFileChooser inputFile;
    private JButton selectFileButton;

    JTextArea signatureInformation;

    private JButton backButton;
    private final JFrame parent;
    private Locale locale;

    public SignatureVerificationJPanel(JFrame parent, Locale locale) {
        this.parent = parent;
        this.locale = locale;
        MigLayout layout = new MigLayout("", "[grow][shrink 0]", "[shrink 0][shrink 0][grow][shrink 0]");
        setLayout(layout);
        initComponents();
        addComponents();
        attachListeners();
    }

    private void initComponents() {
        selectFileLabel = new JLabel();
        inputFileLocation = new JTextField();
        inputFileLocation.setEditable(false);
        inputFile = new JFileChooser();
        selectFileButton = new JButton("...");

        signatureInformation = new JTextArea();

        backButton = new JButton();
    }

    private void addComponents() {
        add(selectFileLabel, "wrap");
        add(inputFileLocation, "growx");
        add(selectFileButton, "wrap");
        add(signatureInformation, "span, growx, growy, wrap");
        add(backButton, "dock south");
    }

    public void setComponentText(Locale locale) {
        this.locale = locale;
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
        selectFileLabel.setText(r.getString("fileSignatureVerificationJPanel.selectFileLabel"));
        backButton.setText(r.getString("fileSignatureVerificationJPanel.backButton"));
    }

    public void attachListeners() {
        selectFileButton.addActionListener((ae) -> {
            int result = inputFile.showOpenDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                inputFileLocation.setText(inputFile.getSelectedFile().toString());
            }
        });
        backButton.addActionListener((ae) -> {
            ((FrameControls) parent).showChooseOptionPanel();
        });
    }
}
