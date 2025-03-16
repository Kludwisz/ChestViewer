package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Objects;

public class AppGUI {
    public static Font MC_FONT;

    public static final JPanel mainPanel = new JPanel();
    public static final DoubleChestViewer viewer = new DoubleChestViewer();
    public static final JTextField seedField = new JTextField(20);

    // simple gui using swing
    public static void main(String[] args) {
        try {
            MC_FONT = Font.createFont(
                    Font.TRUETYPE_FONT,
                    Objects.requireNonNull(AppGUI.class.getResourceAsStream("/minecraft_font.ttf"))
            ).deriveFont(Font.PLAIN, 20);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

        JFrame frame = new javax.swing.JFrame("Chest Viewer");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setSize(470, 380);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(31, 31, 44));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // listen to text property change, update viewer when text changes
        seedField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSeed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSeed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSeed();
            }

            private void updateSeed() {
                try {
                    long seed = Long.parseLong(seedField.getText());
                    viewer.updateLoot(seed);
                }
                catch (NumberFormatException ignored) {}
            }
        });

        seedField.setMaximumSize(new Dimension(300, 40));
        mainPanel.add(seedField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(viewer);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
