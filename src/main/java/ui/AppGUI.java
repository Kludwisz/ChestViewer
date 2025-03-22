package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class AppGUI {
    public static Font MC_FONT;
    public static final Color BG_COLOR = new Color(31, 31, 44);

    private static final ArrayList<Long> seeds = new ArrayList<>();
    private static int currentSeedIndex = 0;

    public static final JPanel mainPanel = new JPanel();
    public static final DoubleChestViewer viewer = new DoubleChestViewer();

    public static final JPanel hboxPanel = new JPanel();
    public static final JTextField seedField = new JTextField(20);
    public static final JLabel seedIDLabel = new JLabel("0/0");
    public static final JButton loadFileButton = new JButton("Load...");
    public static final JButton nextSeedButton = new JButton(">>");
    public static final JButton prevSeedButton = new JButton("<<");

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
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        hboxPanel.setLayout(new BoxLayout(hboxPanel, BoxLayout.X_AXIS));

        loadFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text files", "txt"));
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                seeds.clear();
                currentSeedIndex = 0;
                seedIDLabel.setText("0/0");
                seedField.setText("");
                for (java.io.File file : fileChooser.getSelectedFiles()) {
                    try {
                        java.util.Scanner scanner = new java.util.Scanner(file);
                        while (scanner.hasNextLong()) {
                            seeds.add(scanner.nextLong());
                        }
                    }
                    catch (java.io.FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                if (!seeds.isEmpty()) {
                    seedField.setText(Long.toString(seeds.get(0)));
                    viewer.updateLoot(seeds.get(0));
                    seedIDLabel.setText("1/" + seeds.size());
                }
            }
        });
        hboxPanel.add(loadFileButton);
        hboxPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        prevSeedButton.addActionListener(e -> {
            if (!seeds.isEmpty()) {
                currentSeedIndex = (currentSeedIndex + seeds.size() - 1) % seeds.size();
                seedField.setText(Long.toString(seeds.get(currentSeedIndex)));
                //viewer.updateLoot(seeds.get(currentSeedIndex));
                seedIDLabel.setText((currentSeedIndex + 1) + "/" + seeds.size());
            }
        });
        hboxPanel.add(prevSeedButton);
        hboxPanel.add(Box.createRigidArea(new Dimension(10, 0)));

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
                catch (NumberFormatException ex) {
                    viewer.clear();
                }
            }
        });
        seedField.setMaximumSize(new Dimension(300, 40));
        hboxPanel.add(seedField);

        nextSeedButton.addActionListener(e -> {
            if (!seeds.isEmpty()) {
                currentSeedIndex = (currentSeedIndex + 1) % seeds.size();
                seedField.setText(Long.toString(seeds.get(currentSeedIndex)));
                //viewer.updateLoot(seeds.get(currentSeedIndex));
                seedIDLabel.setText((currentSeedIndex + 1) + "/" + seeds.size());
            }
        });
        hboxPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        hboxPanel.add(nextSeedButton);

        hboxPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        seedIDLabel.setForeground(Color.WHITE);
        hboxPanel.add(seedIDLabel);

        hboxPanel.setBackground(BG_COLOR);

        // --------------------------------------------------------------------

        mainPanel.add(hboxPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(viewer);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
