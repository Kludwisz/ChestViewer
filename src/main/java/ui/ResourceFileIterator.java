package ui;

import com.seedfinding.mccore.util.data.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.List;
import java.util.function.Consumer;

public class ResourceFileIterator {
    public static void forEachImage(List<String> resources, Consumer<Pair<String, BufferedImage>> action) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        for (String resource : resources) {
            try (InputStream inputStream = classLoader.getResourceAsStream(resource)) {
                if (inputStream == null) {
                    System.out.println("File not found!");
                    return;
                }

                action.accept(new Pair<>(resource, ImageIO.read(inputStream)));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
