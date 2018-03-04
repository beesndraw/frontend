package com.beesndraw.web;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class FileSystemUtils {

    public static void mkdirs(String dir) throws Exception {
        File file = new File(dir);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new Exception("failed to create directories for " + dir);
            }
        }
    }

    public static void saveFile(String fileName, String body) throws Exception {
        try {
            OutputStream output = null;
            try {
                output = new BufferedOutputStream(new FileOutputStream(fileName));
                output.write(body.getBytes("UTF-8"));
            } finally {
                if (output != null) {
                    output.close();
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static boolean renameFileIfExists(String filename) {
        try {
            File file = new File(filename);
            File newFile = new File(filename + "." + Long.toString(System.currentTimeMillis()));
            if (file.exists()) {
                if (!file.renameTo(newFile)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void deleteFileIfExists(String filePath) {
        if (isFileExists(filePath)) {
            try {
                File f = new File(filePath);
                f.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isFileExists(String fileName) {
        boolean retVal = false;
        try {
            File f = new File(fileName);
            retVal = f.isFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static List<String> readLines(String trcFile) throws Exception, IOException {
        if (trcFile == null || trcFile.isEmpty()) {
            throw new Exception("Input is empty. Read requires a valid path to a file.");
        }
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader br = null;
        try {
            File file = new File(trcFile);
            if (!file.exists()) {
                throw new Exception("File not found " + trcFile);
            }
            br = new BufferedReader(new FileReader(trcFile));
            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
            return lines;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static String readFile(String path) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.getProperty("line.separator"));
                line = br.readLine();
            }
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                System.err.println("Failed to close file reader. Context: " + e.getMessage());
            }
        }
        return sb.toString();
    }

    public static ArrayList<String> readFileAsList(String path, int numLinesToRead)
            throws Exception {
        ArrayList<String> sb = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (line != null) {
                sb.add(line);
                if (sb.size() >= numLinesToRead) {
                    break;
                }
                line = br.readLine();
            }
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                System.err.println("Failed to close file reader. Context: " + e.getMessage());
            }
        }
        return sb;
    }

    public static ArrayList<String> readFileAsList(String path) throws Exception {
        ArrayList<String> sb = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (line != null) {
                sb.add(line);
                line = br.readLine();
            }
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                System.err.println("Failed to close file reader. Context: " + e.getMessage());
            }
        }
        return sb;
    }

    public static List<String> readFileAsArray(String path) throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        String content;
        try {
            content = readFile(path);
        } catch (Exception e) {
            throw new Exception(e);
        }
        String[] lines = content.split(System.getProperty("line.separator"));
        for (String line : lines) {
            list.add(line);
        }
        return list;
    }

    public static String readFile(String path, boolean includeCRLF) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                System.err.println("Failed to close file reader. Context: " + e.getMessage());
            }
        }
        return sb.toString();
    }

    public static void readStream(InputStream is, StringBuffer buffer) throws IOException {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        while ((line = br.readLine()) != null) {
            buffer.append(line);
        }
    }

    public static String zipIt(File source, List<File> listOfFilesToZip, String snapshotName)
            throws Exception {
        if (snapshotName == null || snapshotName.isEmpty()) {
            snapshotName = "snapshot";
        }
        String outputName = source.getAbsolutePath() + "_" + snapshotName + ".zip";
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zippperStream = null;
        try {
            File file = new File(outputName);
            if (file.exists()) {
                if (!file.delete()) {
                    System.err.println("Cannot delete file " + file.getAbsolutePath());
                } else {
                    saveFile(file.getAbsolutePath(), "");
                }
            }
            fileOutputStream = new FileOutputStream(outputName);
            zippperStream = new ZipOutputStream(fileOutputStream);

            for (File currentFile : listOfFilesToZip) {
                zipSingleFile(currentFile, zippperStream, source);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zippperStream != null) {
                try {
                    zippperStream.close();
                } catch (IOException e) {
                    System.err.println("Failed to close Zip Output Stream : " + e.getLocalizedMessage());
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    System.err.println("Failed to close File Output Stream: " + e.getLocalizedMessage());
                }
            }

        }
        return outputName;
    }

    private static void zipSingleFile(File currentFile, ZipOutputStream zippperStream, File source)
            throws IOException {
        int loc = currentFile.getAbsolutePath().indexOf(source.getName());
        String folderName =
                currentFile.getAbsolutePath().substring(loc + source.getName().length() + 1);
        System.out.println("Zipping " + folderName);
        ZipEntry zipEntry = new ZipEntry(folderName);
        zippperStream.putNextEntry(zipEntry);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(currentFile);
            byte[] bytes = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(bytes)) > 0) {
                zippperStream.write(bytes, 0, bytesRead);
            }
            zippperStream.closeEntry();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    System.err.println("Failed to close File Input Stream: " + e.getLocalizedMessage());
                }
            }
        }
    }

    public static void getAllFiles(List<File> listOfFilesToZip, File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    getAllFiles(listOfFilesToZip, file2);
                } else {
                    listOfFilesToZip.add(file2);
                }
            }
        }
    }

    public static void removeDirectory(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    removeDirectory(files[i]);
                    System.out.println("Deleting directory " + files[i].getName());
                    if (!files[i].delete()) {
                        System.err.println("Cannot delete file " + files[i].getAbsolutePath());
                    }
                } else if (!files[i].delete()) {
                    System.err.println("Cannot delete file " + files[i].getAbsolutePath());
                }
            }
        }
        if (!file.delete()) {
            System.err.println("Cannot delete file " + file.getAbsolutePath());
        }
    }

}
