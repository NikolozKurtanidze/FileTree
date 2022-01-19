package filetree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class FileUtil {

    public static File toFileRepresentation(Path path) throws IOException {
        if(!Files.isDirectory(path) && !Files.isRegularFile(path)) throw new IOException("IO exception");
        if(!Files.isDirectory(path))
            return new RegularFile(path);
        return new Directory(path, Files.list(path).map(x -> {
            try {
                return toFileRepresentation(x);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }). toList());
    }
}
