/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.jpanels;

import callbacks.FileCallback;
import callbacks.FrameControls;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public class SelectingFileAndSignatureJPanel extends JPanel implements FileCallback {

    private JLabel signatureTypeJLabel;
    private JRadioButton attachedSignature;
    private JRadioButton detachedSignature;
    private JRadioButton pdfFile;
    private ButtonGroup signatureButtonsGroup;

    private JButton backButton;
    private JButton signButton;

    private JLabel inputFileLabel;
    private JLabel outputFileLabel;
    private JFileChooser inputFile;
    private JFileChooser outputLocation;
    private JTextField inputFilePath;
    private JTextField outputFilePath;
    private JButton selectInputFileButton;
    private JButton selectOutputLocationButton;

    private final JFrame parent;
    private Locale locale;

    public SelectingFileAndSignatureJPanel(JFrame parent, Locale locale) {
        this.locale = locale;
        this.parent = parent;
        MigLayout layout = new MigLayout("", "[grow][grow][grow]", "[shrink 0][shrink 0][shrink 0][shrink 0][shrink 0][shrink 0][grow]");
        setLayout(layout);
        initComponents();
        attachListeners();
        addComponents();
    }

    private void initComponents() {
        signatureTypeJLabel = new JLabel();
        attachedSignature = new JRadioButton();
        detachedSignature = new JRadioButton();
        pdfFile = new JRadioButton();
        signatureButtonsGroup = new ButtonGroup();
        signatureButtonsGroup.add(attachedSignature);
        signatureButtonsGroup.add(detachedSignature);
        signatureButtonsGroup.add(pdfFile);

        backButton = new JButton();
        signButton = new JButton();

        inputFile = new JFileChooser();
        inputFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //inputFile.setCurrentDirectory(new File(System.getProperty("user.name")));

        inputFileLabel = new JLabel();
        outputFileLabel = new JLabel();
        outputLocation = new JFileChooser();
        outputLocation.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        inputFilePath = new JTextField();
        inputFilePath.setEditable(false);
        outputFilePath = new JTextField();
        outputFilePath.setEditable(false);
        selectInputFileButton = new JButton("...");
        selectOutputLocationButton = new JButton("...");
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
        add(selectOutputLocationButton, "wrap");
        add(backButton, "south, left");
        add(signButton, "south, right, wrap");
    }

    public void setComponentText(Locale locale) {
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
        inputFileLabel.setText(r.getString("selectingFileAndSignatureJPanel.inputFileLabel"));
        outputFileLabel.setText(r.getString("selectingFileAndSignatureJPanel.outputFileLabel"));
        signatureTypeJLabel.setText(r.getString("selectingFileAndSignatureJPanel.signatureTypeJLabel"));
        attachedSignature.setText(r.getString("selectingFileAndSignatureJPanel.attachedSignature"));
        detachedSignature.setText(r.getString("selectingFileAndSignatureJPanel.detachedSignature"));
        pdfFile.setText(r.getString("selectingFileAndSignatureJPanel.pdfFile"));
        backButton.setText(r.getString("selectingFileAndSignatureJPanel.backButton"));
        signButton.setText(r.getString("selectingFileAndSignatureJPanel.signButton"));
        repaint();
    }

    private void attachListeners() {
        backButton.addActionListener((ActionEvent ae) -> {
            ((FrameControls) parent).hideFileAndSignaturePanel();
        });
        signButton.addActionListener((ActionEvent ae) -> {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        });
        selectInputFileButton.addActionListener((ActionEvent ae) -> {
            int result = inputFile.showOpenDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                inputFilePath.setText(inputFile.getSelectedFile().toString());
                outputLocation.setSelectedFile(new File(inputFile.getCurrentDirectory().getAbsolutePath()));
                outputFilePath.setText(inputFile.getCurrentDirectory().getAbsolutePath());
            } else {
//todo
            }
        });
        selectOutputLocationButton.addActionListener((ActionEvent ae) -> {
            int result = outputLocation.showOpenDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                outputFilePath.setText(outputLocation.getSelectedFile().toString());
            } else {
//todo
            }
        });
    }

    @Override
    public File getInputFile() {
        return inputFile.getSelectedFile();
    }

    @Override
    public File getOutputFile() {
        return outputLocation.getSelectedFile();
    }
}
