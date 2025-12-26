// package ui;

// import ui.theme.Theme;
// import dao.FullCarDetailsDAO;
// import models.*;
// import javax.swing.*;
// import javax.swing.border.EmptyBorder;
// import java.awt.*;

// public class CarDetailsUI extends JFrame {

//     // carId was unused
//     private FullCarDetails details;

//     private JList<String> listImages, listStatus, listOptions;

//     public CarDetailsUI(int carId) {
//         // this.carId = carId; // Removed as field is unused

//         setTitle("Car Details");
//         setSize(800, 700);
//         setLocationRelativeTo(null);
//         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

//         JPanel mainPanel = new JPanel(new BorderLayout());
//         mainPanel.setBackground(Theme.BG_COLOR);
//         mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

//         details = FullCarDetailsDAO.getCarDetails(carId);

//         // Header
//         JLabel lblHeader = new JLabel(details.getCar().getBrand() + " " + details.getCar().getModel());
//         lblHeader.setFont(Theme.HEADER_FONT);
//         lblHeader.setForeground(Theme.PRIMARY_COLOR);
//         lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
//         lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
//         mainPanel.add(lblHeader, BorderLayout.NORTH);

//         // Content
//         JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
//         contentPanel.setOpaque(false);

//         contentPanel.add(buildInfoPanel(), BorderLayout.NORTH);
//         contentPanel.add(buildListsPanel(), BorderLayout.CENTER);

//         mainPanel.add(contentPanel, BorderLayout.CENTER);

//         setContentPane(mainPanel);
//     }

//     // ----------------------------
//     // CAR BASIC INFO
//     // ----------------------------
//     private JPanel buildInfoPanel() {
//         JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
//         p.setBackground(Theme.WHITE_COLOR);
//         p.setBorder(BorderFactory.createCompoundBorder(
//                 BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
//                 new EmptyBorder(15, 15, 15, 15)));

//         Car c = details.getCar();

//         p.add(createInfoLabel("Brand:", c.getBrand()));
//         p.add(createInfoLabel("Model:", c.getModel()));
//         p.add(createInfoLabel("Year:", String.valueOf(c.getYear())));
//         p.add(createInfoLabel("Horse Power:", String.valueOf(c.getHorsePower())));
//         p.add(createInfoLabel("Torque:", String.valueOf(c.getTorque())));
//         p.add(createInfoLabel("Price:", String.valueOf(c.getPrice())));

//         return p;
//     }

//     private JPanel createInfoLabel(String title, String value) {
//         JPanel p = new JPanel(new BorderLayout());
//         p.setOpaque(false);
//         JLabel t = new JLabel(title);
//         t.setFont(Theme.STANDARD_FONT);
//         t.setForeground(Color.GRAY);

//         JLabel v = new JLabel(value);
//         v.setFont(Theme.SUBHEADER_FONT);
//         v.setForeground(Theme.TEXT_COLOR);

//         p.add(t, BorderLayout.NORTH);
//         p.add(v, BorderLayout.CENTER);
//         return p;
//     }

//     // ----------------------------
//     // IMAGES + STATUS + OPTIONS
//     // ----------------------------
//     private JPanel buildListsPanel() {
//         JPanel outer = new JPanel(new GridLayout(1, 3, 20, 0));
//         outer.setOpaque(false);

//         // IMAGES
//         DefaultListModel<String> imgModel = new DefaultListModel<>();
//         for (CarImage img : details.getImageList()) {
//             imgModel.addElement(img.getImagePath());
//         }
//         listImages = new JList<>(imgModel);
//         styleList(listImages);

//         // STATUS
//         DefaultListModel<String> statusModel = new DefaultListModel<>();
//         for (CarStatus st : details.getStatusList()) {
//             statusModel.addElement(st.getStatusName());
//         }
//         listStatus = new JList<>(statusModel);
//         styleList(listStatus);

//         // OPTIONS
//         DefaultListModel<String> optModel = new DefaultListModel<>();
//         for (CarOption op : details.getOptionList()) {
//             optModel.addElement(op.getOptionName() + ": " + op.getOptionValue());
//         }
//         listOptions = new JList<>(optModel);
//         styleList(listOptions);

//         outer.add(titledPanel("Images", new JScrollPane(listImages)));
//         // outer.add(titledPanel("Status", new JScrollPane(listStatus)));
//         // outer.add(titledPanel("Options", new JScrollPane(listOptions)));

//         return outer;
//     }

//     private void styleList(JList<String> list) {
//         list.setFont(Theme.STANDARD_FONT);
//         list.setSelectionBackground(Theme.SECONDARY_COLOR);
//         list.setSelectionForeground(Color.WHITE);
//         list.setBorder(new EmptyBorder(5, 5, 5, 5));
//     }

//     // ----------------------------
//     // UI Helper
//     // ----------------------------
//     private JPanel titledPanel(String title, JComponent c) {
//         JPanel p = new JPanel(new BorderLayout());
//         p.setBackground(Theme.WHITE_COLOR);
//         p.setBorder(BorderFactory.createTitledBorder(
//                 BorderFactory.createLineBorder(new Color(200, 200, 200)),
//                 title,
//                 javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
//                 javax.swing.border.TitledBorder.DEFAULT_POSITION,
//                 Theme.BUTTON_FONT,
//                 Theme.PRIMARY_COLOR));
//         p.add(c);

//         if (c instanceof JScrollPane) {
//             ((JScrollPane) c).setBorder(null);
//             ((JScrollPane) c).getViewport().setBackground(Color.WHITE);
//         }

//         return p;
//     }
// }
package ui;

import ui.theme.Theme;
import dao.FullCarDetailsDAO;
import models.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CarDetailsUI extends JFrame {

    private FullCarDetails details;
    // listImages kaldırıldı, yerine panel kullanılacak
    private JList<String> listStatus, listOptions;

    public CarDetailsUI(int carId) {
        setTitle("Car Details");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Veriyi çek
        details = FullCarDetailsDAO.getCarDetails(carId);

        // Header
        JLabel lblHeader = new JLabel(details.getCar().getBrand() + " " + details.getCar().getModel());
        lblHeader.setFont(Theme.HEADER_FONT);
        lblHeader.setForeground(Theme.PRIMARY_COLOR);
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setOpaque(false);

        contentPanel.add(buildInfoPanel(), BorderLayout.NORTH);
        contentPanel.add(buildListsPanel(), BorderLayout.CENTER); // <-- Düzeltilen metod burada çağrılıyor

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    // ----------------------------
    // CAR BASIC INFO
    // ----------------------------
    private JPanel buildInfoPanel() {
        JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
        p.setBackground(Theme.WHITE_COLOR);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 15, 15)));

        Car c = details.getCar();

        p.add(createInfoLabel("Brand:", c.getBrand()));
        p.add(createInfoLabel("Model:", c.getModel()));
        p.add(createInfoLabel("Year:", String.valueOf(c.getYear())));
        p.add(createInfoLabel("Horse Power:", String.valueOf(c.getHorsePower())));
        p.add(createInfoLabel("Torque:", String.valueOf(c.getTorque())));
        p.add(createInfoLabel("Price:", String.valueOf(c.getPrice())));

        return p;
    }

    private JPanel createInfoLabel(String title, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(Theme.STANDARD_FONT);
        t.setForeground(Color.GRAY);

        JLabel v = new JLabel(value);
        v.setFont(Theme.SUBHEADER_FONT);
        v.setForeground(Theme.TEXT_COLOR);

        p.add(t, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);
        return p;
    }

    // ----------------------------
    // IMAGES + STATUS + OPTIONS (BURASI DÜZELTİLDİ)
    // ----------------------------
    // ----------------------------
    // IMAGES + STATUS + OPTIONS (DEBUG VERSİYONU)
    // ----------------------------
    private JPanel buildListsPanel() {
        JPanel outer = new JPanel(new GridLayout(1, 3, 20, 0));
        outer.setOpaque(false);

        // --- 1. IMAGES ---
        JPanel pnlImagesContainer = new JPanel();
        pnlImagesContainer.setLayout(new BoxLayout(pnlImagesContainer, BoxLayout.Y_AXIS)); 
        pnlImagesContainer.setBackground(Color.WHITE);

        System.out.println("--- RESİM YÜKLEME BAŞLADI ---"); // Konsol çıktısı

        java.util.List<CarImage> images = details.getImageList();
        
        // 1. Durum: Liste tamamen boş mu?
        if (images == null || images.isEmpty()) {
            System.out.println("HATA: Resim listesi boş veya null geldi!");
            JLabel lblNoImg = new JLabel("Veritabanında kayıtlı resim yok.");
            lblNoImg.setForeground(Color.RED);
            pnlImagesContainer.add(lblNoImg);
        } else {
            for (CarImage imgObj : images) {
                String path = imgObj.getImagePath();
                
                System.out.println("İncelenen Resim Yolu: " + path); // Konsola yazdır

                // 2. Durum: Yol (Path) null veya boş mu?
                if (path == null || path.trim().isEmpty()) {
                     System.out.println("HATA: Dosya yolu (String) boş!");
                     continue;
                }

                java.io.File imgFile = new java.io.File(path);
                
                // 3. Durum: Dosya bilgisayarda var mı?
                if (imgFile.exists()) {
                    System.out.println("BAŞARILI: Dosya bulundu, yükleniyor...");
                    try {
                        ImageIcon originalIcon = new ImageIcon(path);
                        
                        // Resim gerçekten yüklendi mi kontrol et
                        if (originalIcon.getIconWidth() <= 0) {
                             System.out.println("HATA: Dosya var ama resim okunamadı (Format bozuk olabilir).");
                             pnlImagesContainer.add(new JLabel("Format Hatası: " + imgFile.getName()));
                        } else {
                             // Ölçekleme
                             Image scaledImg = originalIcon.getImage().getScaledInstance(220, -1, Image.SCALE_SMOOTH);
                             JLabel lblImg = new JLabel(new ImageIcon(scaledImg));
                             lblImg.setBorder(new EmptyBorder(0, 0, 10, 0));
                             lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);
                             pnlImagesContainer.add(lblImg);
                             System.out.println("OK: Resim panele eklendi.");
                        }
                    } catch (Exception e) {
                        System.out.println("HATA: Resim yüklenirken exception oluştu: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    // 4. Durum: Dosya bulunamadı
                    System.out.println("HATA: Dosya bu yolda bulunamadı! (Path yanlış)");
                    JLabel lblError = new JLabel("<html>Dosya Bulunamadı:<br/>" + path + "</html>");
                    lblError.setForeground(Color.RED);
                    lblError.setBorder(BorderFactory.createLineBorder(Color.RED));
                    pnlImagesContainer.add(lblError);
                }
            }
        }
        System.out.println("--- RESİM YÜKLEME BİTTİ ---");

        outer.add(titledPanel("Images", new JScrollPane(pnlImagesContainer)));

        // --- STATUS & OPTIONS (Aynı Kalıyor) ---
        DefaultListModel<String> statusModel = new DefaultListModel<>();
        for (CarStatus st : details.getStatusList()) {
            statusModel.addElement(st.getStatusName());
        }
        listStatus = new JList<>(statusModel);
        styleList(listStatus);
        
        DefaultListModel<String> optModel = new DefaultListModel<>();
        for (CarOption op : details.getOptionList()) {
            optModel.addElement(op.getOptionName() + ": " + op.getOptionValue());
        }
        listOptions = new JList<>(optModel);
        styleList(listOptions);
        
        outer.add(titledPanel("Options", new JScrollPane(listOptions)));

        return outer;
    }

    private void styleList(JList<String> list) {
        list.setFont(Theme.STANDARD_FONT);
        list.setSelectionBackground(Theme.SECONDARY_COLOR);
        list.setSelectionForeground(Color.WHITE);
        list.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    private JPanel titledPanel(String title, JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.WHITE_COLOR);
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                title,
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                Theme.BUTTON_FONT,
                Theme.PRIMARY_COLOR));
        p.add(c);

        if (c instanceof JScrollPane) {
            ((JScrollPane) c).setBorder(null);
            ((JScrollPane) c).getViewport().setBackground(Color.WHITE);
        }

        return p;
    }
}