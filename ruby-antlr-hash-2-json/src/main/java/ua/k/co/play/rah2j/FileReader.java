package ua.k.co.play.rah2j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by vasyl.khrystiuk on 06/27/2019.
 */
public class FileReader {

    public static String read (String name) {
        try {
            return new String(Files.readAllBytes(Paths.get(".").toAbsolutePath().resolve(Paths.get("ruby-antlr-hash-2-json", "src", "main", "resources", name))), "UTF-8");
        } catch (IOException e) {
            return null;
        }
    }
}
