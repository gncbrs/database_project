package ui;

import ui.theme.Theme;
import ui.theme.ModernButton;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout; // Butonları yan yana dizmek için
import javax.swing.border.EmptyBorder;

public class CarListUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private ModernButton btnViewDetails;
    private ModernButton btnDelete; // YENİ BUTON
    

    public CarListUI() {
        setTitle("Car List & Management");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel lblHeader = new JLabel("Vehicle Inventory");
        lblHeader.setFont(Theme.HEADER_FONT);
        lblHeader.setForeground(Theme.PRIMARY_COLOR);
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // Table Model (Status Sütunu dahil)
        model = new DefaultTableModel(new String[] { "ID", "Brand", "Model", "Price", "Year", "Status" }, 0) {
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

        // Footer (Butonlar Paneli)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Sağa yaslı butonlar
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        // CarListUI.java içinde "footerPanel" kısmına:

        ModernButton btnEdit = new ModernButton("Edit Car", new Color(255, 193, 7), Color.BLACK); // Sarı renk
        btnEdit.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected == -1) {
                JOptionPane.showMessageDialog(this, "Select a car to edit!");
                return;
            }
            int carId = (int) model.getValueAt(selected, 0);
            new EditCarUI(carId).setVisible(true); // Edit ekranını aç
        });

        footerPanel.add(btnEdit);

        // 1. Detay Butonu
        btnViewDetails = new ModernButton("View Details");
        btnViewDetails.addActionListener(e -> showCarDetails());
        
        // 2. Silme Butonu (YENİ)
        // Kırmızımsı bir renk veriyoruz dikkat çeksin diye
        btnDelete = new ModernButton("Delete Car", new Color(220, 53, 69), Color.WHITE);
        btnDelete.addActionListener(e -> deleteSelectedCar());

        footerPanel.add(btnViewDetails);
        footerPanel.add(Box.createHorizontalStrut(10)); // İki buton arasına boşluk
        footerPanel.add(btnDelete);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        loadCars();
    }

    // =======================
    // 🚗 Verileri Yükle
    // =======================
    private void loadCars() {
        try {
            java.util.List<models.Car> cars = dao.CarDAO.getAllCars();
            model.setRowCount(0);

            for (models.Car c : cars) {
                model.addRow(new Object[] {
                        c.getCarId(),
                        c.getBrand(),
                        c.getModel(),
                        c.getPrice(),
                        c.getYear(),
                        c.getStatus()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading cars: " + ex.getMessage());
        }
    }

    // =======================
    // 🔍 Detay Göster
    // =======================
    private void showCarDetails() {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to view details!");
            return;
        }
        int carId = (int) model.getValueAt(selected, 0);
        new CarDetailsUI(carId).setVisible(true);
    }

    // =======================
    // 🗑️ SİLME FONKSİYONU (YENİ)
    // =======================
    private void deleteSelectedCar() {
        int selected = table.getSelectedRow();
        
        // 1. Seçim Kontrolü
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to delete!");
            return;
        }

        // 2. ID ve Bilgi Alma
        int carId = (int) model.getValueAt(selected, 0);
        String brandModel = model.getValueAt(selected, 1) + " " + model.getValueAt(selected, 2);
        String status = (String) model.getValueAt(selected, 5);

        // 3. Kullanıcıya Onay Sorusu (Confirmation)
        String warningMsg = "Are you sure you want to delete: " + brandModel + "?\nThis action cannot be undone.";
        
        // Eğer araç satılmışsa ek uyarı verelim
        if ("Sold".equalsIgnoreCase(status)) {
            warningMsg += "\n\nWARNING: This car is marked as SOLD. Deleting it will remove sales records too!";
        }

        int response = JOptionPane.showConfirmDialog(
                this, 
                warningMsg, 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE
        );

        // 4. Eğer 'YES' dediyse sil
        if (response == JOptionPane.YES_OPTION) {
            boolean success = dao.CarDAO.deleteCar(carId);

            if (success) {
                JOptionPane.showMessageDialog(this, "Car deleted successfully!");
                loadCars(); // Tabloyu yenile ki silinen satır gitsin
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete car. Check database constraints.");
            }
        }
    }
}