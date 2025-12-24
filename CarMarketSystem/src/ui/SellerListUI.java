package ui;

import ui.theme.Theme;
import ui.theme.ModernButton;
import dao.SellerDAO;
import models.Seller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;

public class SellerListUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private ModernButton btnViewPhones;

    public SellerListUI() {
        setTitle("Seller List");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main Container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel lblHeader = new JLabel("Registered Sellers");
        lblHeader.setFont(Theme.HEADER_FONT);
        lblHeader.setForeground(Theme.PRIMARY_COLOR);
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[] { "ID", "Name", "Email" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        Theme.styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Theme.WHITE_COLOR);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        mainPanel.add(scroll, BorderLayout.CENTER);

        // Footer Actions
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnViewPhones = new ModernButton("View Phone Numbers");
        btnViewPhones.addActionListener(e -> showPhones());

        footerPanel.add(btnViewPhones);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        loadSellers();
    }

    private void loadSellers() {
        try {
            ArrayList<Seller> sellers = SellerDAO.getAllSellers();
            model.setRowCount(0);

            for (Seller s : sellers) {
                String fullName = s.getFirstName() + " " + (s.getMiddleName() != null ? s.getMiddleName() + " " : "")
                        + s.getLastName();
                model.addRow(new Object[] {
                        s.getSellerId(),
                        fullName,
                        s.getEmail()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void showPhones() {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select a seller first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(selected, 0);
        String name = (String) model.getValueAt(selected, 1);

        ArrayList<String> phones = SellerDAO.getPhones(id);

        if (phones.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No phones found for " + name, "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JList<String> list = new JList<>(phones.toArray(new String[0]));
            list.setFont(Theme.STANDARD_FONT);
            JOptionPane.showMessageDialog(this, new JScrollPane(list), "Phones for " + name, JOptionPane.PLAIN_MESSAGE);
        }
    }
}
