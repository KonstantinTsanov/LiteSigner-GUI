/* 
 * The MIT License
 *
 * Copyright 2017 Konstantin Tsanov <k.tsanov@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ktsanov.gui.jpanels;

import com.ktsanov.interfaces.FrameControls;
import com.ktsanov.core.LiteSignerManager;
import com.ktsanov.interfaces.SignatureVerificationPanel;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import com.ktsanov.tools.FilesTool;

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

    JScrollPane signatureInformationScrollPane;
    JTextArea signatureInformation;

    private JButton clearAllButton;
    private JButton validateButton;
    private JButton backButton;
    private final JFrame parent;

    public SignatureVerificationJPanel(JFrame parent) {
        this.parent = parent;
        MigLayout layout = new MigLayout("", "[grow][grow]", "[shrink 0][shrink 0][shrink 0][shrink 0][grow][shrink 0][shrink 0]");
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
        signatureInformation.setEditable(false);
        signatureInformationScrollPane = new JScrollPane(signatureInformation);
        clearAllButton = new JButton(new ImageIcon(getClass().getResource("/images/clear-list.png")));
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
        add(signatureInformationScrollPane, "span, growx, growy, wrap,wmin 10");
        add(validateButton, "span, center, wrap, wmin 250");
        add(clearAllButton, "span, center, wrap");
        add(backButton, "south");
    }

    public void setComponentText() {
        ResourceBundle r = ResourceBundle.getBundle("Bundle");
        selectFileLabel.setText(r.getString("fileSignatureVerificationJPanel.selectSignatureOrAttachedFileLabel"));
        selectSignedFileLabel.setText(r.getString("fileSignatureVerificationJPanel.selectSignedFileLabel"));
        validateButton.setText(r.getString("signatureVerificationJPanel.validateButton"));
        backButton.setText(r.getString("fileSignatureVerificationJPanel.backButton"));
        clearAllButton.setToolTipText(r.getString("signatureVerificationJPanel.clearWindow"));
    }

    public void attachListeners() {
        pkcs7Button.addActionListener((ae) -> {
            int result = pkcs7Chooser.showOpenDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                if (FilesTool.checkIfFileExists(pkcs7Chooser.getSelectedFile())) {
                    pkcs7.setText(pkcs7Chooser.getSelectedFile().toString());
                } else {
                    ResourceBundle r = ResourceBundle.getBundle("Bundle");
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
                    ResourceBundle r = ResourceBundle.getBundle("Bundle");
                    JOptionPane.showMessageDialog(parent, r.getString("fileSignatureVerificationJPanel.selectedFileDoesNotExist"),
                            r.getString("errorMessage.title"), JOptionPane.WARNING_MESSAGE);
                }
            } else {
                signedFileChooser.setSelectedFile(null);
            }
        });
        clearAllButton.addActionListener((ae) -> {
            pkcs7.setText(null);
            pkcs7Chooser.setSelectedFile(null);
            signedFile.setText(null);
            signedFileChooser.setSelectedFile(null);
            signatureInformation.setText(null);
        });
        validateButton.addActionListener((ae) -> {
            if (pkcs7Chooser.getSelectedFile() == null) {
                ResourceBundle r = ResourceBundle.getBundle("Bundle");
                JOptionPane.showMessageDialog(parent, r.getString("signatureVerificationJPanel.noPkcs7FileSelected"),
                        r.getString("errorMessage.title"), JOptionPane.WARNING_MESSAGE);
            } else {
                LiteSignerManager.getInstance().validateSignature(pkcs7Chooser.getSelectedFile(), signedFileChooser.getSelectedFile());
            }
        });
        backButton.addActionListener((ae) -> {
            ((FrameControls) parent).showChooseOptionPanel();
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
