package ui.theme;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class Theme {
    // Colors
    public static final Color PRIMARY_COLOR = new Color(78, 84, 200); // Deep Blue/Violet
    public static final Color SECONDARY_COLOR = new Color(143, 148, 251); // Lighter Accent
    public static final Color BG_COLOR = new Color(244, 247, 246); // Soft White/Grey
    public static final Color TEXT_COLOR = new Color(51, 51, 51); // Dark Grey
    public static final Color WHITE_COLOR = Color.WHITE;
    public static final Color ERROR_COLOR = new Color(220, 53, 69);
    public static final Color SUCCESS_COLOR = new Color(40, 167, 69);

    // Fonts
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font STANDARD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    // Spacing
    public static final int PADDING_SMALL = 5;
    public static final int PADDING_MEDIUM = 10;
    public static final int PADDING_LARGE = 20;

    public static void applyGlobalStyle() {
        // Could act as a simplified look and feel setter if needed
        // For now, we manually apply styles
    }

    public static void stylePanel(JComponent panel) {
        panel.setBackground(BG_COLOR);
    }

    public static void styleTable(JTable table) {
        table.setFont(TABLE_FONT);
        table.setRowHeight(30);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(SECONDARY_COLOR);
        table.setSelectionForeground(WHITE_COLOR);

        JTableHeader header = table.getTableHeader();
        header.setFont(BUTTON_FONT);
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(WHITE_COLOR);
        header.setOpaque(true);

        // Center alignment for cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }
}
