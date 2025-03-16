package ui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ResourceFileIterator {
    public static void forEachResource(List<String> resources, Consumer<Path> action) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Path dir = Paths.get(Objects.requireNonNull(classLoader.getResource("items")).toURI());

            try (Stream<Path> files = Files.walk(dir)) {
                files.filter(Files::isRegularFile).forEach(action);
            }
        }
        catch (URISyntaxException | IOException | NullPointerException ignored) {}
    }
}
