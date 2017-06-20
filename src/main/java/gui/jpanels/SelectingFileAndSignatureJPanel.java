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
package gui.jpanels;

import callbacks.FrameControls;
import core.LiteSignerManager;
import enums.SignatureType;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.validator.routines.UrlValidator;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public class SelectingFileAndSignatureJPanel extends JPanel {

    private JLabel signatureTypeJLabel;
    private JRadioButton attachedSignature;
    private JRadioButton detachedSignature;
    private JRadioButton pdfFile;
    private ButtonGroup signatureButtonsGroup;

    private JButton backButton;
    private JButton signButton;

    private JLabel inputFileLabel;
    private JLabel outputFileLabel;
    private JFileChooser fileToBeSignedChooser;
    private JFileChooser outputLocationChooser;
    private JTextField inputFilePath;
    private JTextField outputFilePath;
    private JButton selectInputFileButton;
    private JButton selectOutputLocationButton;
    private JTextField outputExtension;
    private JCheckBox timestampCheckBox;
    private JTextField tsaUrlTextField;

    private final JFrame parent;
    private Locale locale;

    public SelectingFileAndSignatureJPanel(JFrame parent, Locale locale) {
        this.locale = locale;
        this.parent = parent;
        MigLayout layout = new MigLayout("", "[grow][grow][grow]", "[shrink 0][shrink 0][shrink 0][shrink 0][shrink 0][shrink 0][shrink 0][grow]");
        setLayout(layout);
        initComponents();
        addComponents();
        attachListeners();
    }

    private void initComponents() {
        signatureTypeJLabel = new JLabel();
        attachedSignature = new JRadioButton();
        attachedSignature.setActionCommand(".p7m");
        detachedSignature = new JRadioButton();
        detachedSignature.setActionCommand(".p7s");
        pdfFile = new JRadioButton();
        pdfFile.setActionCommand("");
        signatureButtonsGroup = new ButtonGroup();
        signatureButtonsGroup.add(attachedSignature);
        signatureButtonsGroup.add(detachedSignature);
        signatureButtonsGroup.add(pdfFile);
        backButton = new JButton();
        signButton = new JButton();
        outputExtension = new JTextField();
        outputExtension.setEditable(false);
        fileToBeSignedChooser = new JFileChooser();
        fileToBeSignedChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //inputFile.setCurrentDirectory(new File(System.getProperty("user.name")));
        inputFileLabel = new JLabel();
        outputFileLabel = new JLabel();
        outputLocationChooser = new JFileChooser();
        outputLocationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        inputFilePath = new JTextField();
        inputFilePath.setEditable(false);
        outputFilePath = new JTextField();
        outputFilePath.setEditable(false);
        selectInputFileButton = new JButton("...");
        selectOutputLocationButton = new JButton("...");
        timestampCheckBox = new JCheckBox();
        tsaUrlTextField = new JTextField();
        tsaUrlTextField.setEnabled(false);
    }

    private void addComponents() {
        add(signatureTypeJLabel, "span 3, wrap");
        add(attachedSignature, "grow");
        add(detachedSignature, "grow");
        add(pdfFile, "grow,wrap");
        add(inputFileLabel, "wrap");
        add(inputFilePath, "growx, span 3");
        add(selectInputFileButton, "wrap");
        add(outputFileLabel, "wrap");
        add(outputFilePath, "growx, span 3");
        add(outputExtension, "growx, wmin 40");
        add(selectOutputLocationButton, "wrap");
        add(timestampCheckBox, "span 1");
        add(tsaUrlTextField, "wrap, span, growx");
        add(backButton, "south, left");
        add(signButton, "south, right, wrap");
    }

    public void setComponentText(Locale locale) {
        this.locale = locale;
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
        inputFileLabel.setText(r.getString("selectingFileAndSignatureJPanel.inputFileLabel"));
        outputFileLabel.setText(r.getString("selectingFileAndSignatureJPanel.outputFileLabel"));
        signatureTypeJLabel.setText(r.getString("selectingFileAndSignatureJPanel.signatureTypeJLabel"));
        attachedSignature.setText(r.getString("selectingFileAndSignatureJPanel.attachedSignature"));
        detachedSignature.setText(r.getString("selectingFileAndSignatureJPanel.detachedSignature"));
        pdfFile.setText(r.getString("selectingFileAndSignatureJPanel.pdfFile"));
        backButton.setText(r.getString("selectingFileAndSignatureJPanel.backButton"));
        signButton.setText(r.getString("selectingFileAndSignatureJPanel.signButton"));
        timestampCheckBox.setText(r.getString("selectingFileAndSignatureJPanel.timestampCheckBox"));
        repaint();
    }

    private void attachListeners() {
        backButton.addActionListener((ActionEvent ae) -> {
            ((FrameControls) parent).hideFileAndSignaturePanel();
        });
        signButton.addActionListener((ActionEvent ae) -> {
            if (signatureButtonsGroup.getSelection() == null) {
                ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
                JOptionPane.showMessageDialog(parent, r.getString("selectingFileAndSignatureJPanel.signatureTypeError"),
                        r.getString("errorMessage.title"), JOptionPane.ERROR_MESSAGE);
            } else if (fileToBeSignedChooser.getSelectedFile() == null) {
                ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
                JOptionPane.showMessageDialog(parent, r.getString("selectingFileAndSignatureJPanel.inputFileNotSelected"),
                        r.getString("errorMessage.title"), JOptionPane.ERROR_MESSAGE);
            } else if (timestampCheckBox.isSelected() && (tsaUrlTextField.getText().isEmpty() || validateURL(tsaUrlTextField.getText()) == false)) {
                ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
                JOptionPane.showMessageDialog(parent, r.getString("selectingFileAndSignatureJPanel.tsaUrlNotPresentError"),
                        r.getString("errorMessage.title"), JOptionPane.ERROR_MESSAGE);
            } else {
                LiteSignerManager.getInstance().signFile(attachedSignature.isSelected() ? SignatureType.Attached : detachedSignature.isSelected() ? SignatureType.Detached : SignatureType.Pdf,
                        fileToBeSignedChooser.getSelectedFile(),
                        new File(outputLocationChooser.getSelectedFile().toString().concat(outputExtension.getText())), timestampCheckBox.isSelected() ? tsaUrlTextField.getText() : null);
            }
        });
        selectInputFileButton.addActionListener((ActionEvent ae) -> {
            int result = fileToBeSignedChooser.showOpenDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                inputFilePath.setText(fileToBeSignedChooser.getSelectedFile().toString());
                outputLocationChooser.setSelectedFile(fileToBeSignedChooser.getSelectedFile());
                outputFilePath.setText(fileToBeSignedChooser.getSelectedFile().toString());
            } else {
                fileToBeSignedChooser.setSelectedFile(null);
            }
        });
        selectOutputLocationButton.addActionListener((ActionEvent ae) -> {
            int result = outputLocationChooser.showOpenDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                outputFilePath.setText(outputLocationChooser.getSelectedFile().toString());
            } else {
                outputLocationChooser.setSelectedFile(null);
            }
        });
        timestampCheckBox.addActionListener((ae) -> {
            if (timestampCheckBox.isSelected() == true) {
                tsaUrlTextField.setEnabled(true);
            } else {
                tsaUrlTextField.setEnabled(false);
            }
        });
        attachedSignature.addActionListener((ae) -> {
            outputExtension.setText(attachedSignature.getActionCommand());
        });
        detachedSignature.addActionListener((ae) -> {
            outputExtension.setText(detachedSignature.getActionCommand());
        });
        pdfFile.addActionListener((ae) -> {
            outputExtension.setText(pdfFile.getActionCommand());
        });
    }

    public File getInputFile() {
        return fileToBeSignedChooser.getSelectedFile();
    }

    public File getOutputFile() {
        return outputLocationChooser.getSelectedFile();
    }

    private boolean validateURL(String url) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }
}
