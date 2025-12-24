package ui;

import ui.theme.Theme;
import ui.theme.ModernButton;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Car Market System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG_COLOR);

        // --- Header Section ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Theme.PRIMARY_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("Car Market System");
        lblTitle.setFont(Theme.HEADER_FONT);
        lblTitle.setForeground(Theme.WHITE_COLOR);

        JLabel lblSubtitle = new JLabel("Manage your cars, customers, and sales effectively.");
        lblSubtitle.setFont(Theme.STANDARD_FONT);
        lblSubtitle.setForeground(new Color(230, 230, 250)); // Light lavender

        JPanel titleContainer = new JPanel(new GridLayout(2, 1));
        titleContainer.setOpaque(false);
        titleContainer.add(lblTitle);
        titleContainer.add(lblSubtitle);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Content Section (Grid of Cards/Buttons) ---
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(2, 4, 20, 20));
        menuPanel.setBackground(Theme.BG_COLOR);
        menuPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Create buttons using our new ModernButton
        menuPanel.add(createMenuButton("Customer List", e -> new CustomerListUI().setVisible(true)));
        menuPanel.add(createMenuButton("Seller List", e -> new SellerListUI().setVisible(true)));
        menuPanel.add(createMenuButton("Car List", e -> new CarListUI().setVisible(true)));
        menuPanel.add(createMenuButton("Make Sale", e -> new MakeSaleUI().setVisible(true)));

        menuPanel.add(createMenuButton("Add Customer", e -> new AddCustomerUI().setVisible(true)));
        menuPanel.add(createMenuButton("Add Seller", e -> new AddSellerUI().setVisible(true)));
        menuPanel.add(createMenuButton("Add Car", e -> new AddCarUI().setVisible(true)));

        // Exit button with different color
        ModernButton btnExit = new ModernButton("Exit", Theme.ERROR_COLOR, new Color(200, 40, 50));
        btnExit.addActionListener(e -> System.exit(0));
        menuPanel.add(btnExit);

        add(menuPanel, BorderLayout.CENTER);

        // --- Footer ---
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel lblFooter = new JLabel("Car Market System v2.0 - Developed by Antigravity");
        lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblFooter.setForeground(Color.GRAY);
        footerPanel.add(lblFooter);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private ModernButton createMenuButton(String text, java.awt.event.ActionListener action) {
        ModernButton btn = new ModernButton(text);
        if (action != null) {
            btn.addActionListener(action);
        }
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu().setVisible(true);
        });
    }
}
