/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.jpanels;

import java.awt.Color;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Konstantin Tsanov <k.tsanov@gmail.com>
 */
public class SelectingCertificateJPanel extends JPanel {

    private JLabel selectingCertificateJLabel;
    private DefaultTableModel certificateModel;
    private JTable certificateTable;
    private JScrollPane certificateScrollPane;
    private JButton signButton;
    private Locale locale;

    public SelectingCertificateJPanel(Locale locale) {
        this.locale = locale;
        MigLayout layout = new MigLayout("", "[grow]", "[shrink 0][grow][shrink 0]");
        setLayout(layout);
        initCertificateModel();
        initComponents();
        addComponents();
    }

    private void initComponents() {
        selectingCertificateJLabel = new JLabel();
        signButton = new JButton();
        certificateTable = new JTable(certificateModel);
        certificateScrollPane = new JScrollPane(certificateTable);
        certificateScrollPane.getViewport().setBackground(Color.WHITE);
    }

    private void addComponents() {
        add(selectingCertificateJLabel, "span");
        add(certificateScrollPane, "grow, span, wrap");
        add(signButton, "left, span");
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
        ResourceBundle r = ResourceBundle.getBundle("Bundle", locale);
        TableColumnModel thm = certificateTable.getTableHeader().getColumnModel();
        thm.getColumn(0).setHeaderValue(r.getString("selectingCertificateJPanel.owner"));
        thm.getColumn(1).setHeaderValue(r.getString("selectingCertificateJPanel.publisher"));
        thm.getColumn(2).setHeaderValue(r.getString("selectingCertificateJPanel.validFrom"));
        repaint();
    }

    private void attachListeners() {

    }
}
