package ui;

import ui.theme.Theme;
import ui.theme.ModernButton;
import dao.CustomerDAO;
import models.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;

public class CustomerListUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private ModernButton btnViewPhones;

    public CustomerListUI() {
        setTitle("Customer List");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main Container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel lblHeader = new JLabel("Registered Customers");
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

        loadCustomers();
    }

    private void loadCustomers() {
        try {
            ArrayList<Customer> customers = CustomerDAO.getAllCustomers();
            model.setRowCount(0);

            for (Customer c : customers) {
                String fullName = c.getFirstName() + " " + (c.getMiddleName() != null ? c.getMiddleName() + " " : "")
                        + c.getLastName();
                model.addRow(new Object[] {
                        c.getCustomerId(),
                        fullName,
                        c.getEmail()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void showPhones() {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select a customer first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) model.getValueAt(selected, 0);
        String name = (String) model.getValueAt(selected, 1);

        ArrayList<String> phones = CustomerDAO.getPhones(id);

        if (phones.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No phones found for " + name, "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JList<String> list = new JList<>(phones.toArray(new String[0]));
            list.setFont(Theme.STANDARD_FONT);
            JOptionPane.showMessageDialog(this, new JScrollPane(list), "Phones for " + name, JOptionPane.PLAIN_MESSAGE);
        }
    }
}
