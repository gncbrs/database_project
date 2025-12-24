package ui;

import ui.theme.Theme;
import ui.theme.ModernButton;
import ui.theme.ModernTextField;
import database.DatabaseConnection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class AddSellerUI extends JFrame {

    private ModernTextField txtFirstName, txtMiddleName, txtLastName, txtEmail, txtPhone;
    private ModernButton btnSave;

    public AddSellerUI() {
        setTitle("Add New Seller");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BG_COLOR);

        // Header
        JLabel lblHeader = new JLabel("Add New Seller");
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
        addFormRow(formPanel, gbc, 4, "Phone:", txtPhone);

        JScrollPane mainScroll = new JScrollPane(formPanel);
        mainScroll.setBorder(null);
        mainPanel.add(mainScroll, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Theme.BG_COLOR);
        footerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        btnSave = new ModernButton("Save Seller");
        btnSave.setPreferredSize(new Dimension(200, 40));
        btnSave.addActionListener(e -> saveSeller());

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

    // ================================
    // SELLER KAYDETME METODU
    // ================================
    private void saveSeller() {

        String first = txtFirstName.getText().trim();
        String middle = txtMiddleName.getText().trim();
        String last = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        if (first.isEmpty() || last.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields except Middle Name are required!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            // 1) SELLER EKLE
            String sql = """
                    INSERT INTO Seller (first_name, middle_name, last_name, email)
                    VALUES (?, ?, ?, ?)
                    """;

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, first);
            ps.setString(2, middle.isEmpty() ? null : middle);
            ps.setString(3, last);
            ps.setString(4, email);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int sellerId = rs.getInt(1);

            // 2) TELEFONU EKLE
            PreparedStatement psPhone = conn.prepareStatement(
                    "INSERT INTO Seller_Phone (seller_id, phone) VALUES (?, ?)");
            psPhone.setInt(1, sellerId);
            psPhone.setString(2, phone);
            psPhone.executeUpdate();

            JOptionPane.showMessageDialog(this, "Seller added successfully!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
