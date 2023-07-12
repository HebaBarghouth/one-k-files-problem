package com.tamatem.assessment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DirectoryClassifier {
    private int maxDeep = 1;
    private String directoryPath;
    private int batchSize;

    public DirectoryClassifier(String directoryPath, int batchSize) {
        this.directoryPath = directoryPath;
        this.batchSize = batchSize;
    }

    public void groupDirectoryFiles() throws IOException {
        int batch = 0;
        List<Path> batchFiles;
        Set<String> langs = new HashSet<>();

        do {
            batchFiles = Files.walk(Paths.get(directoryPath), maxDeep)
                    .filter(p -> Files.isRegularFile(p))
                    .limit(batchSize)
                    .collect(Collectors.toList());
            if (!batchFiles.isEmpty()) {
                batch++;
                System.out.println("Handling Batch " + batch + " ##");
            }

            for (Path path : batchFiles) {
                String fileName = path.getFileName().toString();
                String fileLang = extractLang(extractLang(fileName));
                boolean isNewLang = langs.add(fileLang);
                if (isNewLang) {
                    try {
                        Files.createDirectory(Paths.get(directoryPath + "/" + fileLang));
                    } catch (IOException e) {
                        System.err.println("creating directory error: " + e.getMessage());
                    }
                }
                moveFile(path, directoryPath + "/" + fileLang + "/" + fileName);
            }

        }
        while (!batchFiles.isEmpty());
    }

    private String extractLang(String fileName) {
        String[] splitName = fileName.split("-");
        return splitName[0];
    }


    private void moveFile(Path src, String dest) {
        try {
            Files.move(src, Paths.get(dest));
        } catch (IOException e) {
            System.err.println("Moving file error: " + e);
        }
    }

    public long getFilesCount() throws IOException {
        return Files.walk(Paths.get(directoryPath), maxDeep)
                .filter(p -> Files.isRegularFile(p))
                .count();
    }

    public List<String> getFolderNames() throws IOException {
        return Files.walk(Paths.get(directoryPath), maxDeep)
                .filter(p -> Files.isDirectory(p) && p.getParent() != null)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
    }

}
