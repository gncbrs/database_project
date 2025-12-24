package ui;

import ui.theme.Theme;
import ui.theme.ModernButton;
import ui.theme.ModernTextField;
import database.DatabaseConnection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class AddCustomerUI extends JFrame {

    private ModernTextField txtFirstName, txtMiddleName, txtLastName, txtEmail, txtPhone;
    private DefaultListModel<String> phoneListModel;
    private JList<String> phoneList;
    private ModernButton btnAddPhone, btnSave, btnDeletePhone;

    public AddCustomerUI() {
        setTitle("Add New Customer");
        setSize(450, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BG_COLOR);

        // Header
        JLabel lblHeader = new JLabel("Add New Customer");
        lblHeader.setFont(Theme.HEADER_FONT);
        lblHeader.setForeground(Theme.PRIMARY_COLOR);
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblHeader.setBorder(new EmptyBorder(20, 0, 20, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // Form Container
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.BG_COLOR);
        formPanel.setBorder(new EmptyBorder(0, 40, 0, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtFirstName = new ModernTextField();
        txtMiddleName = new ModernTextField();
        txtLastName = new ModernTextField();
        txtEmail = new ModernTextField();
        txtPhone = new ModernTextField();

        addFormRow(formPanel, gbc, 0, "First Name:", txtFirstName);
        addFormRow(formPanel, gbc, 1, "Middle Name:", txtMiddleName);
        addFormRow(formPanel, gbc, 2, "Last Name:", txtLastName);
        addFormRow(formPanel, gbc, 3, "Email:", txtEmail);

        // Phone Section
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel lblPhone = new JLabel("Phone Numbers");
        lblPhone.setFont(Theme.SUBHEADER_FONT);
        lblPhone.setForeground(Theme.PRIMARY_COLOR);
        lblPhone.setBorder(new EmptyBorder(10, 0, 5, 0));
        formPanel.add(lblPhone, gbc);

        gbc.gridy = 5;
        gbc.gridwidth = 1;

        JPanel phoneInputPanel = new JPanel(new BorderLayout(10, 0));
        phoneInputPanel.setOpaque(false);
        phoneInputPanel.add(txtPhone, BorderLayout.CENTER);

        btnAddPhone = new ModernButton("+", Theme.SECONDARY_COLOR, Theme.PRIMARY_COLOR);
        btnAddPhone.setPreferredSize(new Dimension(40, 30));
        btnAddPhone.addActionListener(e -> addPhone());
        phoneInputPanel.add(btnAddPhone, BorderLayout.EAST);

        gbc.gridwidth = 2;
        formPanel.add(phoneInputPanel, gbc);

        // Phone List
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        phoneListModel = new DefaultListModel<>();
        phoneList = new JList<>(phoneListModel);
        phoneList.setFont(Theme.STANDARD_FONT);
        JScrollPane scrollPhone = new JScrollPane(phoneList);
        scrollPhone.setPreferredSize(new Dimension(0, 100)); // Fixed height
        formPanel.add(scrollPhone, gbc);

        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 7;
        btnDeletePhone = new ModernButton("Remove Selected Phone", Theme.ERROR_COLOR, new Color(200, 40, 50));
        btnDeletePhone.addActionListener(e -> removePhone());
        formPanel.add(btnDeletePhone, gbc);

        JScrollPane mainScroll = new JScrollPane(formPanel);
        mainScroll.setBorder(null);
        mainPanel.add(mainScroll, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Theme.BG_COLOR);
        footerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        btnSave = new ModernButton("Save Customer");
        btnSave.setPreferredSize(new Dimension(200, 40));
        btnSave.addActionListener(e -> saveCustomer());

        footerPanel.add(btnSave);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        gbc.gridwidth = 1;
        JLabel label = new JLabel(labelText);
        label.setFont(Theme.STANDARD_FONT);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private void addPhone() {
        String phone = txtPhone.getText().trim();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone cannot be empty!");
            return;
        }
        phoneListModel.addElement(phone);
        txtPhone.setText("");
    }

    private void removePhone() {
        int index = phoneList.getSelectedIndex();
        if (index != -1) {
            phoneListModel.remove(index);
        }
    }

    // ============================
    // VERİTABANINA KAYDETME
    // ============================
    private void saveCustomer() {
        String first = txtFirstName.getText().trim();
        String middle = txtMiddleName.getText().trim();
        String last = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();

        if (first.isEmpty() || last.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First, Last, and Email are required!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            // 1) CUSTOMER EKLE
            String sql = "INSERT INTO Customer (first_name, middle_name, last_name, email) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, first);
            ps.setString(2, middle.isEmpty() ? null : middle);
            ps.setString(3, last);
            ps.setString(4, email);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int customerId = rs.getInt(1);

            // 2) TELEFON NUMARALARINI EKLE
            for (int i = 0; i < phoneListModel.size(); i++) {
                String phone = phoneListModel.get(i);
                PreparedStatement psPhone = conn.prepareStatement(
                        "INSERT INTO Customer_Phone (customer_id, phone) VALUES (?, ?)");
                psPhone.setInt(1, customerId);
                psPhone.setString(2, phone);
                psPhone.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Customer added successfully!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
