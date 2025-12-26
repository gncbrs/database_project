package ui;

import ui.theme.Theme;
import ui.theme.ModernButton;
import ui.theme.ModernTextField;
import dao.SaleDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class MakeSaleUI extends JFrame {

    private JComboBox<String> cmbCustomer, cmbSeller, cmbCar;
    private ModernTextField txtSalePrice;
    private ModernButton btnMakeSale;

    private ArrayList<Integer> customerIds = new ArrayList<>();
    private ArrayList<Integer> sellerIds = new ArrayList<>();
    private ArrayList<Integer> carIds = new ArrayList<>();

    public MakeSaleUI() {
        setTitle("Make Sale");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BG_COLOR);

        // Header
        JLabel lblHeader = new JLabel("Process New Sale");
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

        cmbCustomer = new JComboBox<>();
        cmbSeller = new JComboBox<>();
        cmbCar = new JComboBox<>();
        styleComboBox(cmbCustomer);
        styleComboBox(cmbSeller);
        styleComboBox(cmbCar);

        txtSalePrice = new ModernTextField();

        addFormRow(formPanel, gbc, 0, "Select Customer:", cmbCustomer);
        addFormRow(formPanel, gbc, 1, "Select Seller:", cmbSeller);
        addFormRow(formPanel, gbc, 2, "Select Vehicle:", cmbCar);
        addFormRow(formPanel, gbc, 3, "Sale Price ($):", txtSalePrice);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Theme.BG_COLOR);
        footerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        btnMakeSale = new ModernButton("Confirm Sale", Theme.SUCCESS_COLOR, new Color(34, 139, 34));
        btnMakeSale.setPreferredSize(new Dimension(200, 40));
        btnMakeSale.addActionListener(e -> makeSale());

        footerPanel.add(btnMakeSale);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        loadCustomers();
        loadSellers();
        loadCars();
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

    private void styleComboBox(JComboBox<String> box) {
        box.setFont(Theme.STANDARD_FONT);
        box.setBackground(Color.WHITE);
    }

    // ======================================
    // CUSTOMER DROPDOWN DOLDUR
    // ======================================
    private void loadCustomers() {
        try {
            java.util.List<models.Customer> customers = dao.CustomerDAO.getAllCustomers();

            cmbCustomer.removeAllItems();
            customerIds.clear();

            for (models.Customer c : customers) {
                String name = c.getFirstName() + " " + c.getLastName();
                customerIds.add(c.getCustomerId());
                cmbCustomer.addItem(name);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + ex.getMessage());
        }
    }

    // ======================================
    // SELLER DROPDOWN DOLDUR
    // ======================================
    private void loadSellers() {
        try {
            java.util.List<models.Seller> sellers = dao.SellerDAO.getAllSellers();

            cmbSeller.removeAllItems();
            sellerIds.clear();

            for (models.Seller s : sellers) {
                String name = s.getFirstName() + " " + s.getLastName();
                sellerIds.add(s.getSellerId());
                cmbSeller.addItem(name);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading sellers: " + ex.getMessage());
        }
    }

    // ======================================
    // CAR DROPDOWN DOLDUR
    // ======================================
    // ======================================
    // CAR DROPDOWN DOLDUR (GÜNCELLENDİ)
    // ======================================
    private void loadCars() {
        try {
            // ESKİSİ: java.util.List<models.Car> cars = dao.CarDAO.getAllCars();
            // YENİSİ: Sadece müsait araçları çekiyoruz
            java.util.List<models.Car> cars = dao.CarDAO.getAvailableCars();

            cmbCar.removeAllItems();
            carIds.clear();

            for (models.Car c : cars) {
                // Kullanıcıya Marka + Model + Yıl göstermek daha ayırt edici olur
                String label = c.getBrand() + " " + c.getModel() + " (" + c.getYear() + ")";
                
                carIds.add(c.getCarId());
                cmbCar.addItem(label);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading cars: " + ex.getMessage());
        }
    }

    // ======================================
    // MAKE SALE ACTION
    // ======================================
    private void makeSale() {

    // 1. Validasyonlar (Aynen kalıyor)
    if (cmbCustomer.getSelectedIndex() == -1 ||
        cmbSeller.getSelectedIndex() == -1 ||
        cmbCar.getSelectedIndex() == -1) {
        JOptionPane.showMessageDialog(this, "Please select all fields!");
        return;
    }

    if (txtSalePrice.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter sale price!");
        return;
    }

    try {
        int customerId = customerIds.get(cmbCustomer.getSelectedIndex());
        int sellerId = sellerIds.get(cmbSeller.getSelectedIndex());
        int carId = carIds.get(cmbCar.getSelectedIndex());
        
        // Fiyatı parse et
        String priceText = txtSalePrice.getText().replace(",", "."); // Olası format hatası önlemi
        double price = Double.parseDouble(priceText);

        // --- DEĞİŞİKLİK BURADA BAŞLIYOR ---

        // ADIM 1: Satış Kaydını Oluştur (Artık arabayı silmediğimiz için FK hatası almazsın)
        // Not: addSale metodunuzun normal bir insert işlemi olması yeterli.
        boolean saleAdded = SaleDAO.addSale(customerId, sellerId, carId, price);

        if (saleAdded) {
            
            // ADIM 2: Arabanın Durmunu "Sold" Olarak Güncelle
            // Bunun için CarDAO veya StatusDAO içinde yeni bir metoda ihtiyacın var.
            boolean statusUpdated = dao.CarDAO.updateCarStatusToSold(carId);
            
            if (statusUpdated) {
                JOptionPane.showMessageDialog(this, "Sale completed successfully! Car status updated to Sold.");
                dispose(); // Pencereyi kapat
            } else {
                JOptionPane.showMessageDialog(this, "Sale recorded, but failed to update car status!");
            }
            
        } else {
            JOptionPane.showMessageDialog(this, "Sale FAILED! Could not create sale record.");
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid price format!");
    } catch (Exception ex) {
        ex.printStackTrace(); // Konsolda hatayı görmek için
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}

}
