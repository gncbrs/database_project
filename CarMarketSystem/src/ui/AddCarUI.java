package ui;

import ui.theme.Theme;
import ui.theme.ModernButton;
import ui.theme.ModernTextField;
import database.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddCarUI extends JFrame {

    private ModernTextField txtBrand, txtModel, txtPrice, txtHP, txtYear, txtTorque, txtImagePath;
    private JComboBox<String> cmbStatus;
    private JComboBox<String> cmbOption;
    private ModernButton btnChooseImage, btnAddCar;
    // NEW ÖZELLİKLER
    private JLabel lblImagePreview;
    private java.util.ArrayList<String> selectedPaths = new java.util.ArrayList<>();

    public AddCarUI() {
        setTitle("Add New Car");
        setSize(450, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BG_COLOR);

        // Header
        JLabel lblHeader = new JLabel("Add New Car");
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
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fields initialization
        txtBrand = new ModernTextField();
        txtModel = new ModernTextField();
        txtPrice = new ModernTextField();
        txtHP = new ModernTextField();
        txtYear = new ModernTextField();
        txtTorque = new ModernTextField();
        txtImagePath = new ModernTextField();
        txtImagePath.setEditable(false);

        cmbStatus = new JComboBox<>(new String[] { "Available", "Sold", "Pending", "Reserved" });
        cmbOption = new JComboBox<>(
                new String[] { "Color: White", "Color: Black", "Package: Comfort", "Package: Sport" });
        styleComboBox(cmbStatus);
        styleComboBox(cmbOption);

        // Adding rows
        addFormRow(formPanel, gbc, 0, "Brand:", txtBrand);
        addFormRow(formPanel, gbc, 1, "Model:", txtModel);
        addFormRow(formPanel, gbc, 2, "Price:", txtPrice);
        addFormRow(formPanel, gbc, 3, "Horse Power:", txtHP);
        addFormRow(formPanel, gbc, 4, "Year:", txtYear);
        addFormRow(formPanel, gbc, 5, "Torque:", txtTorque);
        addFormRow(formPanel, gbc, 6, "Status:", cmbStatus);
        addFormRow(formPanel, gbc, 7, "Option:", cmbOption);

        // Image Row
        gbc.gridx = 0;
        gbc.gridy = 8;
        // ... (Mevcut kodundaki Image Path satırları) ...

// --- YENİ EKLENECEK KISIM: RESİM ÖNİZLEME ALANI ---
gbc.gridx = 1;
gbc.gridy = 9; // Bir alt satıra geç
gbc.weighty = 1.0; // Dikeyde yer kaplasın
gbc.fill = GridBagConstraints.BOTH;

lblImagePreview = new JLabel();
lblImagePreview.setHorizontalAlignment(JLabel.CENTER);
lblImagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Çerçeve olsun ki yerini görelim
lblImagePreview.setPreferredSize(new Dimension(200, 150)); // Varsayılan boyut

formPanel.add(lblImagePreview, gbc);
        JLabel lblImg = new JLabel("Image Path:");
        lblImg.setFont(Theme.STANDARD_FONT);
        formPanel.add(lblImg, gbc);

        gbc.gridx = 1;
        JPanel imgPanel = new JPanel(new BorderLayout(10, 0));
        imgPanel.setOpaque(false);
        imgPanel.add(txtImagePath, BorderLayout.CENTER);

        btnChooseImage = new ModernButton("...", Theme.SECONDARY_COLOR, Theme.PRIMARY_COLOR);
        btnChooseImage.setPreferredSize(new Dimension(50, 30));
        btnChooseImage.addActionListener(e -> chooseImage());
        imgPanel.add(btnChooseImage, BorderLayout.EAST);

        formPanel.add(imgPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Theme.BG_COLOR);
        footerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        btnAddCar = new ModernButton("Save Car");
        btnAddCar.setPreferredSize(new Dimension(200, 40));
        btnAddCar.addActionListener(e -> addCarToDatabase());

        footerPanel.add(btnAddCar);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(Theme.STANDARD_FONT);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setFont(Theme.STANDARD_FONT);
        box.setBackground(Color.WHITE);
        // Basic styling, specialized UI delegate would be too complex for now
    }

    // =========================
    // FOTOĞRAF SEÇME
    // =========================
    private void chooseImage() {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png", "jpeg", "webp"));
    
    // --- ÇOKLU SEÇİMİ AKTİF ET ---
    chooser.setMultiSelectionEnabled(true); 

    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        // Seçilen dosyaları al
        java.io.File[] files = chooser.getSelectedFiles();
        
        selectedPaths.clear(); // Önceki seçimleri temizle (İstersen silmeyebilirsin)
        
        for (java.io.File file : files) {
            selectedPaths.add(file.getAbsolutePath());
        }

        // Kullanıcıya kaç dosya seçtiğini göster
        if (selectedPaths.size() > 0) {
            txtImagePath.setText(selectedPaths.size() + " images selected");
            
            // İlk resmin önizlemesini gösterelim
            ImageIcon originalIcon = new ImageIcon(selectedPaths.get(0));
            Image img = originalIcon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            lblImagePreview.setIcon(new ImageIcon(img));
        }
    }
}

    // =========================
    // VERİTABANINA EKLEME
    // =========================
    private void addCarToDatabase() {
        String brand = txtBrand.getText();
        String model = txtModel.getText();
        String price = txtPrice.getText();
        String hp = txtHP.getText();
        String year = txtYear.getText();
        String torque = txtTorque.getText();
        String status = cmbStatus.getSelectedItem().toString();
        String option = cmbOption.getSelectedItem().toString();
        String imagePath = txtImagePath.getText();

        if (brand.isEmpty() || model.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Brand and Model cannot be empty!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            // 1) INSERT CAR
            String insertCar = "INSERT INTO Car (brand, model, price, horse_power, year, torque) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psCar = conn.prepareStatement(insertCar, Statement.RETURN_GENERATED_KEYS);
            psCar.setString(1, brand);
            psCar.setString(2, model);
            psCar.setBigDecimal(3, new java.math.BigDecimal(price));
            psCar.setInt(4, Integer.parseInt(hp));
            psCar.setInt(5, Integer.parseInt(year));
            psCar.setInt(6, Integer.parseInt(torque));
            psCar.executeUpdate();

            // Yeni eklenen araç ID
            ResultSet rs = psCar.getGeneratedKeys();
            rs.next();
            int carId = rs.getInt(1);

            // 2) STATUS ID öğren
            PreparedStatement psStatus = conn
                    .prepareStatement("SELECT status_id FROM Car_Status WHERE status_name = ?");
            psStatus.setString(1, status);
            ResultSet rsStatus = psStatus.executeQuery();
            rsStatus.next();
            int statusId = rsStatus.getInt("status_id");

            // Car_Has_Status
            PreparedStatement psCarStatus = conn.prepareStatement(
                    "INSERT INTO Car_Has_Status (car_id, status_id) VALUES (?, ?)");
            psCarStatus.setInt(1, carId);
            psCarStatus.setInt(2, statusId);
            psCarStatus.executeUpdate();

            // 3) OPTION ID öğren
            String[] optionSplit = option.split(":");
            PreparedStatement psOption = conn.prepareStatement(
                    "SELECT option_id FROM Car_Option WHERE option_name = ? AND option_value = ?");
            psOption.setString(1, optionSplit[0].trim());
            psOption.setString(2, optionSplit[1].trim());
            ResultSet rsOption = psOption.executeQuery();
            rsOption.next();
            int optionId = rsOption.getInt("option_id");

            // Car_Has_Option
            PreparedStatement psCarOption = conn.prepareStatement(
                    "INSERT INTO Car_Has_Option (car_id, option_id) VALUES (?, ?)");
            psCarOption.setInt(1, carId);
            psCarOption.setInt(2, optionId);
            psCarOption.executeUpdate();

            // 4) IMAGE INSERT
            PreparedStatement psImage = conn.prepareStatement(
                    "INSERT INTO Car_Image (image_path) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            psImage.setString(1, imagePath);
            psImage.executeUpdate();

            ResultSet rsImg = psImage.getGeneratedKeys();
            rsImg.next();
            int imgId = rsImg.getInt(1);

            PreparedStatement psCarImg = conn.prepareStatement(
                    "INSERT INTO Car_Has_Image (car_id, image_id) VALUES (?, ?)");
            psCarImg.setInt(1, carId);
            psCarImg.setInt(2, imgId);
            psCarImg.executeUpdate();

            JOptionPane.showMessageDialog(this, "Car added successfully!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
