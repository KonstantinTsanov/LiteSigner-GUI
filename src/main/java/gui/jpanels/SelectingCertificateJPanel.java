/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.jpanels;

import callbacks.CertificatePanel;
import callbacks.FrameControls;
import core.LiteSignerManager;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
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
    private Locale locale;

    public SelectingCertificateJPanel(JFrame parent, Locale locale) {
        this.parent = parent;
        this.locale = locale;
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
            ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
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

    public void setComponentText(Locale locale) {
        this.locale = locale;
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
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
                    LiteSignerManager.getInstance().checkIfCertificateHasChain(certificateTable.getSelectedRow());
                } else {
                    ((FrameControls) parent).hideFileAndSignaturePanel();
                }
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (certificateTable.getSelectedRow() != -1) {
                    ((FrameControls) parent).showFileAndSignaturePanel();
                } else {
                    ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
                    JOptionPane.showMessageDialog(parent, r.getString("selectingCertificateJPanel.noCertificateSelectedError"),
                            r.getString("errorMessage.title"), JOptionPane.WARNING_MESSAGE);
                }
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
