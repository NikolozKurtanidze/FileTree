package filetree;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Directory extends File {

	private final List<File> files;

	public Directory(Path path, List<File> files) {
		super(path);
		this.files = files;
	}

	private List<File> allFiles(){
		List<File> result = new LinkedList<>();
		files.forEach(x -> {
			result.add(x);
			if(!x.isRegularFile()){
				Directory temp = (Directory) x;
				result.addAll(temp.allFiles());
			}
		});
		return result;
	}

	@Override
	public Iterator<File> iterator() {
		return new Iterator<>() {
			private int current = 0;
			private List<File> iterated = new LinkedList<>();
			private final List<File> all = allFiles();

			@Override
			public boolean hasNext() {
				return iterated.size() != all.size();
			}

			@Override
			public File next() {
				File result = all.get(current);
				iterated.add(result);
				current++;
				return result;
			}
		};
	}

	@Override
	public int getHeight() {
		if(files.isEmpty()) return 1;
		return 1 + files.stream().mapToInt(File::getHeight).max().getAsInt();
	}

	@Override
	public boolean isRegularFile() {
		return false;
	}

	public List<File> getFiles() {
		return files;
	}
}
