package ui;

import ui.theme.Theme;
import ui.theme.ModernButton;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;

public class CarListUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private ModernButton btnViewDetails;

    public CarListUI() {
        setTitle("Car List");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main Container styling
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel lblHeader = new JLabel("Available Cars");
        lblHeader.setFont(Theme.HEADER_FONT);
        lblHeader.setForeground(Theme.PRIMARY_COLOR);
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // === Table Model ===
        model = new DefaultTableModel(new String[] { "ID", "Brand", "Model", "Price", "Year" }, 0) {
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

        // Footer / Actions
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnViewDetails = new ModernButton("View Details");
        btnViewDetails.addActionListener(e -> showCarDetails());

        footerPanel.add(btnViewDetails);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        loadCars();
    }

    // =======================
    // 🚗 Veritabanından araçları çek
    // =======================
    private void loadCars() {
        try {
            java.util.List<models.Car> cars = dao.CarDAO.getAllCars();

            model.setRowCount(0); // tabloyu temizle

            for (models.Car c : cars) {
                model.addRow(new Object[] {
                        c.getCarId(),
                        c.getBrand(),
                        c.getModel(),
                        c.getPrice(),
                        c.getYear()
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading cars: " + ex.getMessage());
        }
    }

    // =======================
    // 🔍 Seçili aracın detaylarını göster
    // =======================
    private void showCarDetails() {
        int selected = table.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select a car first!");
            return;
        }

        int carId = (int) model.getValueAt(selected, 0);
        new CarDetailsUI(carId).setVisible(true);
    }
}
