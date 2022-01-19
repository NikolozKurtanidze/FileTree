package filetree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ClientInfoStatus;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

public class RegularFile extends File {



    public RegularFile(Path path) throws IOException {
        super(path);
    }

    @Override
    public Iterator<File> iterator() {
        return new Iterator<File>() {
            private int current = 0;
            private List<File> iterated = new LinkedList<>();

            @Override
            public boolean hasNext() {
                try {
                    return iterated.size() != Files.list(getPath().getParent()).filter(Files::isRegularFile).toList().size();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public File next() {
                if (!hasNext()) throw new NoSuchElementException();
                try {
                    List<File> temp = Files.list(getPath().getParent()).filter(x -> !Files.isDirectory(x)).map(x -> {
                        try {
                            return FileUtil.toFileRepresentation(x);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).toList();
                    iterated.add(temp.get(current));
                    return temp.get(current++);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public boolean isRegularFile() {
        return true;
    }

}
