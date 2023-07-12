package com.tamatem.assessment;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int batchSize = Integer.parseInt(args.length > 0 ? args[0] : "0");
        batchSize = batchSize > 0 ? batchSize : 100;

        DirectoryClassifier classifier = new DirectoryClassifier("files", batchSize);
        System.out.println("Files count before grouping: " + classifier.getFilesCount() + "\n");
        classifier.groupDirectoryFiles();
        System.out.println("Files count after grouping: " + classifier.getFilesCount() + "\n");
        System.out.println("Directory Folders:");
        classifier.getFolderNames().forEach(name -> System.out.println(name));
    }

}
