/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.jpanels;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import callbacks.FrameControls;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public final class ChooseAnOptionJPanel extends JPanel {

    private JLabel chooseAnOptionJLabel;
    private JButton signFileJButton;
    private JButton verifyFileJButton;

    private final FrameControls parent;

    public ChooseAnOptionJPanel(FrameControls parent) {
        this.parent = parent;
        MigLayout layout = new MigLayout("", "[grow][grow]", "[grow][grow]");
        setLayout(layout);
        initComponents();
        addComponents();
        attachListeners();
    }

    private void initComponents() {
        chooseAnOptionJLabel = new JLabel();
        signFileJButton = new JButton();
        verifyFileJButton = new JButton();
    }

    private void addComponents() {
        add(chooseAnOptionJLabel, "span");
        add(signFileJButton, "grow,span,wrap");
        add(verifyFileJButton, "grow,span,wrap");
    }

    public void setComponentText(Locale locale) {
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
        chooseAnOptionJLabel.setText(r.getString("chooseAnOptionJPanel.chooseAnOptionJLabel"));
        signFileJButton.setText(r.getString("chooseAnOptionJPanel.signFileJButton"));
        verifyFileJButton.setText(r.getString("chooseAnOptionJPanel.verifyFileJButton"));
    }

    private void attachListeners() {
        signFileJButton.addActionListener((al) -> {
            parent.showSigningLayout();
        });
        verifyFileJButton.addActionListener((al) -> {
        });
    }
}
