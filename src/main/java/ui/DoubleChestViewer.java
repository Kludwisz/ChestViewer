package ui;

import com.seedfinding.mcfeature.loot.item.ItemStack;
import loot.BastionLootGenerator;

import javax.swing.*;
import java.awt.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class DoubleChestViewer extends JPanel {
    private static final HashMap<String, Image> ITEM_IMAGES = new HashMap<>();

    private final BastionLootGenerator generator = new BastionLootGenerator();
    private List<ItemStack> currentLoot;

    public void updateLoot(long seed) {
        generator.setInternalSeed(seed);
        currentLoot = generator.generateLoot();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // paint a 9x6 grid of squares with padding in between
        int padding = 8;
        int squareSize = Math.min((getWidth() - padding * 10) / 9, (getHeight() - padding * 7) / 6);
        int startX = (getWidth() - (squareSize * 9 + padding * 8)) / 2 - padding;

        Graphics2D g2 = (Graphics2D) g;
        Iterator<ItemStack> iterator = currentLoot == null ? null : currentLoot.iterator();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 6; j++) {
                int x = startX + padding + i * (squareSize + padding);
                int y = padding + j * (squareSize + padding);
                g.setColor(Color.WHITE);
                g.fillRect(x, y, squareSize, squareSize);

                if (iterator != null && iterator.hasNext()) {
                    ItemStack is = iterator.next();
                    if (is.getItem() == null)
                        continue;
                    drawItem(g2, x, y, squareSize, is.getItem().getName());
                    drawItemCount(g2, x + squareSize - 20, y + squareSize - 5, is.getCount());
                }
            }
        }
    }

    private void drawItem(Graphics2D g2, int x, int y, int size, String itemName) {
        Image img = ITEM_IMAGES.get(itemName);
        if (img != null) {
            g2.drawImage(img, x, y, size, size, null);
        }
    }

    private void drawItemCount(Graphics2D g2, int x, int y, int itemCount) {
        String text = String.valueOf(itemCount);
        int offset = text.length() - 1;

        g2.setFont(AppGUI.MC_FONT);
        g2.setColor(Color.BLACK);
        g2.drawString(text, x - offset * 15, y);
    }

    static {
        // TODO load all item images
    }
}
