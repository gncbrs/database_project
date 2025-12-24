package ui;

import ui.theme.Theme;
import ui.theme.ModernButton;
import ui.theme.ModernTextField;
import service.CarService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class AddCarWizard extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Step Info Variables
    private ModernTextField txtBrand, txtModel, txtYear, txtHP, txtTorque, txtPrice;

    private JComboBox<String> cmbStatus;
    private JComboBox<String> cmbOptionName, cmbOptionValue;
    private DefaultListModel<String> optionList;

    private DefaultListModel<String> imageList;
    private JList<String> listImages;

    public AddCarWizard() {
        setTitle("Add Car Wizard");
        setSize(550, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Theme.BG_COLOR);

        mainPanel.add(step1Panel(), "STEP1");
        mainPanel.add(step2Panel(), "STEP2");
        mainPanel.add(step3Panel(), "STEP3");
        mainPanel.add(step4Panel(), "STEP4");

        add(mainPanel);
        cardLayout.show(mainPanel, "STEP1");
    }

    // ------------------------------------
    // STEP 1 — BASIC INFO
    // ------------------------------------
    private JPanel step1Panel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Theme.BG_COLOR);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel lblTitle = new JLabel("Step 1: Basic Information");
        lblTitle.setFont(Theme.HEADER_FONT);
        lblTitle.setForeground(Theme.PRIMARY_COLOR);
        p.add(lblTitle, gbc);

        // Fields
        txtBrand = new ModernTextField();
        txtModel = new ModernTextField();
        txtYear = new ModernTextField();
        txtHP = new ModernTextField();
        txtTorque = new ModernTextField();
        txtPrice = new ModernTextField();

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(p, gbc, 1, "Brand:", txtBrand);
        addFormRow(p, gbc, 2, "Model:", txtModel);
        addFormRow(p, gbc, 3, "Year:", txtYear);
        addFormRow(p, gbc, 4, "HP:", txtHP);
        addFormRow(p, gbc, 5, "Torque:", txtTorque);
        addFormRow(p, gbc, 6, "Price:", txtPrice);

        // Next Button
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        ModernButton next = new ModernButton("Next →");
        next.addActionListener(e -> cardLayout.show(mainPanel, "STEP2"));
        p.add(next, gbc);

        return p;
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.STANDARD_FONT);
        p.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        p.add(field, gbc);
    }

    // ------------------------------------
    // STEP 2 — STATUS
    // ------------------------------------
    private JPanel step2Panel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Theme.BG_COLOR);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);

        JLabel lblTitle = new JLabel("Step 2: Status");
        lblTitle.setFont(Theme.HEADER_FONT);
        lblTitle.setForeground(Theme.PRIMARY_COLOR);
        p.add(lblTitle, gbc);

        cmbStatus = new JComboBox<>(new String[] { "Available", "Sold", "Pending", "Reserved" });
        cmbStatus.setFont(Theme.STANDARD_FONT);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblStatus = new JLabel("Select Status:");
        lblStatus.setFont(Theme.STANDARD_FONT);
        p.add(lblStatus, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        p.add(cmbStatus, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        btnPanel.setOpaque(false);

        ModernButton back = new ModernButton("← Back", Theme.SECONDARY_COLOR, Theme.PRIMARY_COLOR);
        ModernButton next = new ModernButton("Next →");

        back.addActionListener(e -> cardLayout.show(mainPanel, "STEP1"));
        next.addActionListener(e -> cardLayout.show(mainPanel, "STEP3"));

        btnPanel.add(back);
        btnPanel.add(next);
        p.add(btnPanel, gbc);

        return p;
    }

    // ------------------------------------
    // STEP 3 — OPTIONS
    // ------------------------------------
    private JPanel step3Panel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(Theme.BG_COLOR);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel lblTitle = new JLabel("Step 3: Options");
        lblTitle.setFont(Theme.HEADER_FONT);
        lblTitle.setForeground(Theme.PRIMARY_COLOR);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lblTitle, BorderLayout.NORTH);

        // Input Area
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbOptionName = new JComboBox<>(new String[] { "Color", "Package" });
        cmbOptionValue = new JComboBox<>(new String[] { "Red", "Blue", "Black", "Sport", "Comfort" });
        styleComboBox(cmbOptionName);
        styleComboBox(cmbOptionValue);

        addFormRow(inputPanel, gbc, 0, "Option Type:", cmbOptionName);
        addFormRow(inputPanel, gbc, 1, "Option Value:", cmbOptionValue);

        ModernButton addOption = new ModernButton("+ Add Selection", Theme.SECONDARY_COLOR, Theme.PRIMARY_COLOR);
        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(addOption, gbc);

        p.add(inputPanel, BorderLayout.CENTER);

        // List
        optionList = new DefaultListModel<>();
        JList<String> optionJList = new JList<>(optionList);
        optionJList.setFont(Theme.STANDARD_FONT);
        JScrollPane scroll = new JScrollPane(optionJList);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scroll.setPreferredSize(new Dimension(0, 150));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(inputPanel, BorderLayout.NORTH);
        centerContainer.add(scroll, BorderLayout.CENTER);
        p.add(centerContainer, BorderLayout.CENTER);

        addOption.addActionListener(e -> {
            String name = cmbOptionName.getSelectedItem().toString();
            String value = cmbOptionValue.getSelectedItem().toString();
            optionList.addElement(name + ": " + value);
        });

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        btnPanel.setOpaque(false);
        ModernButton back = new ModernButton("← Back", Theme.SECONDARY_COLOR, Theme.PRIMARY_COLOR);
        ModernButton next = new ModernButton("Next →");

        back.addActionListener(e -> cardLayout.show(mainPanel, "STEP2"));
        next.addActionListener(e -> cardLayout.show(mainPanel, "STEP4"));

        btnPanel.add(back);
        btnPanel.add(next);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setFont(Theme.STANDARD_FONT);
        box.setBackground(Color.WHITE);
    }

    // ------------------------------------
    // STEP 4 — IMAGES
    // ------------------------------------
    private JPanel step4Panel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(Theme.BG_COLOR);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Step 4: Images");
        lblTitle.setFont(Theme.HEADER_FONT);
        lblTitle.setForeground(Theme.PRIMARY_COLOR);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lblTitle, BorderLayout.NORTH);

        imageList = new DefaultListModel<>();
        listImages = new JList<>(imageList);
        listImages.setFont(Theme.STANDARD_FONT);

        JScrollPane scroll = new JScrollPane(listImages);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        p.add(scroll, BorderLayout.CENTER);

        ModernButton choose = new ModernButton("Select Image File...", Theme.SECONDARY_COLOR, Theme.PRIMARY_COLOR);
        choose.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imageList.addElement(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        p.add(choose, BorderLayout.EAST);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        btnPanel.setOpaque(false);
        ModernButton back = new ModernButton("← Back", Theme.SECONDARY_COLOR, Theme.PRIMARY_COLOR);
        ModernButton finish = new ModernButton("Finish & Save");

        back.addActionListener(e -> cardLayout.show(mainPanel, "STEP3"));
        finish.addActionListener(e -> saveToDatabase());

        btnPanel.add(back);
        btnPanel.add(finish);
        p.add(btnPanel, BorderLayout.SOUTH);

        return p;
    }

    // ------------------------------------
    // SAVE TO DATABASE
    // ------------------------------------
    private void saveToDatabase() {
        // Collect data
        String brand = txtBrand.getText();
        String model = txtModel.getText();
        String priceStr = txtPrice.getText();
        String hpStr = txtHP.getText();
        String yearStr = txtYear.getText();
        String torqueStr = txtTorque.getText();
        String status = cmbStatus.getSelectedItem().toString();

        java.util.List<String> options = new ArrayList<>();
        for (int i = 0; i < optionList.size(); i++) {
            options.add(optionList.get(i));
        }

        java.util.List<String> images = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            images.add(imageList.get(i));
        }

        // Validate
        try {
            if (brand.isEmpty() || model.isEmpty() || priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields!");
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Validation Error: " + e.getMessage());
            return;
        }

        // Run db operation in background
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                java.math.BigDecimal price = new java.math.BigDecimal(priceStr);
                int hp = Integer.parseInt(hpStr);
                int year = Integer.parseInt(yearStr);
                int torque = Integer.parseInt(torqueStr);

                CarService carService = new CarService();
                carService.addCarTransaction(brand, model, price, hp, year, torque, status, options, images);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Check for exceptions
                    JOptionPane.showMessageDialog(AddCarWizard.this, "Car Successfully Added!");
                    dispose();
                } catch (Exception ex) {
                    Throwable cause = ex.getCause();
                    String msg = cause != null ? cause.getMessage() : ex.getMessage();
                    JOptionPane.showMessageDialog(AddCarWizard.this, "Error: " + msg);
                    ex.printStackTrace();
                }
            }
        }.execute();
    }
}
