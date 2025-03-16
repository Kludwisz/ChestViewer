package ui;

import com.seedfinding.mcfeature.loot.item.ItemStack;
import loot.BastionLootGenerator;

import javax.swing.*;
import java.awt.*;

import java.util.Arrays;
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

    public void clear() {
        currentLoot = null;
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

        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 9; i++) {
                int x = startX + padding + i * (squareSize + padding);
                int y = padding + j * (squareSize + padding);
                g2.setColor(new Color(138, 138, 138));
                g2.fillRect(x, y, squareSize, squareSize);

                if (iterator != null && iterator.hasNext()) {
                    ItemStack is = iterator.next();
                    if (is.getItem() == null)
                        continue;
                    drawItem(g2, x, y, squareSize, is.getItem().getName());

                    // FIXME looks terrible
                    //drawItemCount(g2, x + squareSize - 20, y + squareSize - 5, is.getCount());
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

    @SuppressWarnings("unused")
    private void drawItemCount(Graphics2D g2, int x, int y, int itemCount) {
        String text = String.valueOf(itemCount);
        int offset = text.length() - 1;

        g2.setFont(AppGUI.MC_FONT);
        g2.setColor(Color.BLACK);
        g2.drawString(text, x - offset * 15, y);
    }

    static {
        List<String> items = Arrays.asList(
                "items/ancient_debris.png",
                "items/arrow.png",
                "items/bone_block.png",
                "items/chain.png",
                "items/crossbow.png",
                "items/crying_obsidian.png",
                "items/enchanted_book.png",
                "items/gilded_blackstone.png",
                "items/gold_block.png",
                "items/gold_ingot.png",
                "items/gold_nugget.png",
                "items/golden_boots.png",
                "items/golden_chestplate.png",
                "items/golden_helmet.png",
                "items/golden_leggings.png",
                "items/golden_sword.png",
                "items/iron_ingot.png",
                "items/iron_nugget.png",
                "items/magma_cream.png",
                "items/music_disc_pigstep.png",
                "items/netherite_scrap.png",
                "items/obsidian.png",
                "items/snout_banner_pattern.png",
                "items/spectral_arrow.png",
                "items/string.png"
        );

        ResourceFileIterator.forEachResource(items,
                path -> {
                    String itemName = path.getFileName().toString().replace(".png", "");
                    try {
                        Image img = new ImageIcon(path.toUri().toURL()).getImage();
                        ITEM_IMAGES.put(itemName, img);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
