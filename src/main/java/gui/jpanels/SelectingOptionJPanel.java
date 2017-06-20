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

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import callbacks.FrameControls;
import javax.swing.JFrame;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public final class SelectingOptionJPanel extends JPanel {

    private JLabel chooseAnOptionJLabel;
    private JButton signFileJButton;
    private JButton verifySignatureJButton;

    private final JFrame parent;

    public SelectingOptionJPanel(JFrame parent) {
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
        verifySignatureJButton = new JButton();
    }

    private void addComponents() {
        add(chooseAnOptionJLabel, "span");
        add(signFileJButton, "grow,span,wrap");
        add(verifySignatureJButton, "grow,span,wrap");
    }

    public void setComponentText(Locale locale) {
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
        chooseAnOptionJLabel.setText(r.getString("chooseAnOptionJPanel.chooseAnOptionJLabel"));
        signFileJButton.setText(r.getString("chooseAnOptionJPanel.signFileJButton"));
        verifySignatureJButton.setText(r.getString("chooseAnOptionJPanel.verifySignatureJButton"));
    }

    private void attachListeners() {
        signFileJButton.addActionListener((al) -> {
            ((FrameControls) parent).showSigningPanel();
        });
        verifySignatureJButton.addActionListener((al) -> {
            ((FrameControls) parent).showSignatureVerificationPanel();
        });
    }
}
