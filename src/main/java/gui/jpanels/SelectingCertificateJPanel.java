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

import callbacks.CertificatePanel;
import callbacks.FrameControls;
import core.LiteSignerManager;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public class SelectingCertificateJPanel extends JPanel implements CertificatePanel {

    private JLabel selectingCertificateJLabel;
    private DefaultTableModel certificateModel;
    private JTable certificateTable;
    private JScrollPane certificateScrollPane;
    private JButton nextButton;
    private final JFrame parent;

    public SelectingCertificateJPanel(JFrame parent) {
        this.parent = parent;
        MigLayout layout = new MigLayout("", "[grow]", "[shrink 0][grow][shrink 0]");
        setLayout(layout);
        initCertificateModel();
        initComponents();
        addComponents();
        attachListeners();
    }

    private void initComponents() {
        selectingCertificateJLabel = new JLabel();
        nextButton = new JButton();
        certificateTable = new JTable(certificateModel) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        certificateTable.setColumnSelectionAllowed(false);
        certificateTable.setRowSelectionAllowed(true);
        certificateTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        certificateScrollPane = new JScrollPane(certificateTable);
        certificateScrollPane.getViewport().setBackground(Color.WHITE);
    }

    private void addComponents() {
        add(selectingCertificateJLabel, "span");
        add(certificateScrollPane, "grow, span, wrap");
        add(nextButton, "right, span");
    }

    private void initCertificateModel() {
        certificateModel = new DefaultTableModel() {
            ResourceBundle r = ResourceBundle.getBundle("Bundle");
            String[] certProps = {r.getString("selectingCertificateJPanel.owner"),
                r.getString("selectingCertificateJPanel.publisher"),
                r.getString("selectingCertificateJPanel.validFrom")};

            @Override
            public int getColumnCount() {
                return certProps.length;
            }

            @Override
            public String getColumnName(int index) {
                return certProps[index];
            }
        };
        revalidate();
        repaint();
    }

    public void setComponentText() {
        ResourceBundle r = ResourceBundle.getBundle("Bundle");
        selectingCertificateJLabel.setText(r.getString("selectingCertificateJPanel.label"));
        nextButton.setText(r.getString("selectingCertificateJPanel.nextButton"));
        TableColumnModel thm = certificateTable.getTableHeader().getColumnModel();
        thm.getColumn(0).setHeaderValue(r.getString("selectingCertificateJPanel.owner"));
        thm.getColumn(1).setHeaderValue(r.getString("selectingCertificateJPanel.publisher"));
        thm.getColumn(2).setHeaderValue(r.getString("selectingCertificateJPanel.validFrom"));
        repaint();
    }

    private void attachListeners() {
        certificateTable.getSelectionModel().addListSelectionListener((ListSelectionEvent lse) -> {
            if (!lse.getValueIsAdjusting()) {
                if (certificateTable.getSelectedRow() != -1) {
                    LiteSignerManager.getInstance().verifySelectedCertificateFromTable(certificateTable.getSelectedRow());
                } else {
                    ((FrameControls) parent).hideFileAndSignaturePanel();
                }
            }
        });
        nextButton.addActionListener((ActionEvent ae) -> {
            if (certificateTable.getSelectedRow() != -1) {
                ((FrameControls) parent).showFileAndSignaturePanel();
            } else {
                ResourceBundle r = ResourceBundle.getBundle("Bundle");
                JOptionPane.showMessageDialog(parent, r.getString("selectingCertificateJPanel.noCertificateSelectedError"),
                        r.getString("errorMessage.title"), JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    @Override
    public DefaultTableModel getTableModel() {
        return certificateModel;
    }

    @Override
    public JFrame getPanelParent() {
        return parent;
    }

    @Override
    public JTable getCertificateTable() {
        return certificateTable;
    }
}
