package filetree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Tester {
    public static List<File> getAll(Directory directory){
        List<File> result = new LinkedList<>(directory.getFiles().stream().filter(File::isRegularFile).toList());
        if(directory.getFiles().stream().anyMatch(x -> !x.isRegularFile())){
            directory.getFiles().stream().filter(x -> !x.isRegularFile()).forEach(x -> {
                result.add(x);
                result.addAll(getAll((Directory) x));
            });
        }
        return result;
    }
        public static void main(String[] args) throws IOException {

        Path path = Paths.get(MiniJava.readString("Enter your path: "));
        File current = FileUtil.toFileRepresentation(path);
        MiniJava.write("Instructions: cd - change directory, height - get height of current file, it - to start iterating over files elements, iterateAll - to check if it iterates over all files, exit - to exit, cls - clear console;");
        String instruction = MiniJava.readString("Enter instruction: ");
        boolean exit = false;   
        while(!exit) {
            switch (instruction) {
                case "iterateAll":
                    int counter = 0;
                    Iterator<File> iterator1 = current.iterator();
                    List<File> iteratedFiles = new LinkedList<>();
                    while(iterator1.hasNext()) {
                        counter++;
                        File prev = iterator1.next();
                        iteratedFiles.add(prev);
                        System.out.println(prev);
                    }
                    List<File> allFiles = new LinkedList<>();
                    if(current.isRegularFile()){
                        RegularFile current1 = (RegularFile) current;
                        allFiles.addAll(Files.list(current1.getPath().getParent()).filter(x -> !Files.isDirectory(x)).map(x -> {
                            try {
                                return new RegularFile(x);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }).toList());
                    }else{
                        Directory current1 = (Directory) current;
                        allFiles.addAll(getAll(current1));
                        System.out.println("------------------------------");
                        allFiles.forEach(x -> System.out.println(x.getPath()));
                    }
                    if(allFiles.stream().map(File::getPath).toList().containsAll(iteratedFiles.stream().map(File::getPath).toList())) System.out.println("Iterator works, iterated over " + counter + " files");
                    else {
                        System.out.println("Iterator didn't work");
                    }
                    break;
                case "cd":
                    path = Paths.get(MiniJava.readString("Enter your path: "));
                    current = FileUtil.toFileRepresentation(path);
                    MiniJava.writeLineConsole("your in file: " + current.getPath());
                    break;
                case "height":
                    MiniJava.write("Height of this file is: " + current.getHeight());
                    break;
                case "exit":
                    exit = true;
                    continue;
                case "it":
                    String instr;
                    MiniJava.write("Iteration mode started: enter instructions: next - moves to next, hasNext = tells if it has more elements to iterate");
                    Iterator<File> iterator = current.iterator();
                    boolean iteration = true;
                    while (iteration) {
                        instr = MiniJava.readString("Enter instruction: ");
                        switch (instr) {
                            case "stop":
                                iteration = false;
                                break;
                            case "next":
                                System.out.println(((File) iterator.next()).getPath());
                                break;
                            case "hasNext":
                                System.out.println(iterator.hasNext());
                                break;
                            default:
                                MiniJava.write("Unknown instruction!");
                        }
                    }
                    MiniJava.write("Iteration ended.");
                    break;
                default:
                    MiniJava.write("Unknown instruction!");
            }
            instruction = MiniJava.readString("Enter instruction: ");
        }
    }
}
