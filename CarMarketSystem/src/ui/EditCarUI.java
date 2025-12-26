package ui;

import ui.theme.Theme;
import ui.theme.ModernButton;
import ui.theme.ModernTextField;
import dao.FullCarDetailsDAO;
import dao.CarDAO;
import models.FullCarDetails;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class EditCarUI extends JFrame {

    private int carId;
    private FullCarDetails details;
    
    private ModernTextField txtPrice;
    private JLabel lblCurrentOption; // Mevcut opsiyonu gösterir
    private JComboBox<String> cmbNewOption; // Yeni opsiyon seçimi
    private ModernButton btnSave, btnAddPhoto;
    private JLabel lblPhotoStatus; // "Fotoğraf seçildi" yazısı için

    public EditCarUI(int carId) {
        this.carId = carId;
        this.details = FullCarDetailsDAO.getCarDetails(carId);

        setTitle("Edit Car Details");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Başlık
        JLabel lblHeader = new JLabel("Edit: " + details.getCar().getBrand() + " " + details.getCar().getModel());
        lblHeader.setFont(Theme.HEADER_FONT);
        lblHeader.setForeground(Theme.PRIMARY_COLOR);
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // Form Alanı
        JPanel formPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        // 1. Fiyat Değiştirme
        formPanel.add(new JLabel("Update Price:"));
        txtPrice = new ModernTextField();
        txtPrice.setText(String.valueOf(details.getCar().getPrice())); // Mevcut fiyatı yaz
        formPanel.add(txtPrice);

        // 2. Opsiyon Gösterme (Basitlik için sadece gösterip yenisini seçtiriyoruz)
        String currentOpt = "None";
        if (!details.getOptionList().isEmpty()) {
            currentOpt = details.getOptionList().get(0).getOptionName() + ": " + details.getOptionList().get(0).getOptionValue();
        }
        formPanel.add(new JLabel("Current Option: " + currentOpt));
        
        // Yeni Opsiyon Seçimi (Örnek veriler)
        cmbNewOption = new JComboBox<>(new String[]{"No Change", "Color: Black", "Color: White", "Package: Sport", "Package: Comfort"});
        formPanel.add(cmbNewOption);

        // 3. Fotoğraf Ekleme
        btnAddPhoto = new ModernButton("Add New Photo...");
        lblPhotoStatus = new JLabel("No file selected");
        lblPhotoStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        
        JPanel pnlPhoto = new JPanel(new BorderLayout(10, 0));
        pnlPhoto.setOpaque(false);
        pnlPhoto.add(btnAddPhoto, BorderLayout.CENTER);
        pnlPhoto.add(lblPhotoStatus, BorderLayout.SOUTH);
        
        btnAddPhoto.addActionListener(e -> choosePhoto());
        formPanel.add(pnlPhoto);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Kaydet Butonu
        btnSave = new ModernButton("Save Changes");
        btnSave.addActionListener(e -> saveChanges());
        mainPanel.add(btnSave, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    // --- FOTOĞRAF SEÇME ---
    private String selectedImagePath = null;
    
    private void choosePhoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png", "jpeg", "webp"));
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            selectedImagePath = f.getAbsolutePath();
            lblPhotoStatus.setText("Selected: " + f.getName());
            lblPhotoStatus.setForeground(new Color(0, 128, 0));
        }
    }

    // --- KAYDETME ---
    private void saveChanges() {
        try {
            // 1. Fiyat Güncelle
            double newPrice = Double.parseDouble(txtPrice.getText());
            CarDAO.updateCar(carId, newPrice, null, null); // Opsiyon güncellemesi DAO'da tam yazılmalı
            
            // 2. Fotoğraf Ekle (Varsa)
            if (selectedImagePath != null) {
                CarDAO.addImageToCar(carId, selectedImagePath);
            }

            JOptionPane.showMessageDialog(this, "Changes Saved Successfully!");
            dispose(); // Pencereyi kapat
            
            // İstersen burada CarListUI'yı yenilemek için bir callback ekleyebilirsin.

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Price Format!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}