import java.io.*;

public class MPD {
//9999нh//д авпапoз444444214lдр3332555555555555555555555555555555525533333333322sdf2fdgfdg913
    public static void createMPDArchive(String sourceFile, String archiveFile) {
        try (FileOutputStream fos = new FileOutputStream(archiveFile);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             FileInputStream fis = new FileInputStream(sourceFile);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            String header = sourceFile + ";" + fis.available() + ";";
            bos.write(header.getBytes());
//
            byte[] buffer = new byte[1024];
            int length;
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            
            System.out.println("Архив в формате .mpd создан успешно"); //
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isMPDFormat(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            byte[] headerBytes = new byte[1024];
            int length = bis.read(headerBytes);
            if (length > 0) {
                String header = new String(headerBytes, 0, length).trim();
                return header.contains(";");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void readMPDFile(File file) {
        if (!isMPDFormat(file)) {
            System.out.println("Файл не соответствует формату .mpd");
            return;
        }
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            byte[] headerBytes = new byte[1024];
            int headerLength = bis.read(headerBytes);
            String header = new String(headerBytes, 0, headerLength).trim();
            String[] headerParts = header.split(";");
            String fileName = headerParts[0];
            int fileSize = Integer.parseInt(headerParts[1]);

            byte[] fileContent = new byte[fileSize];
            int contentLength = bis.read(fileContent);
            if (contentLength > 0) {
                System.out.println("Имя файла: " + fileName);
                System.out.println("Размер файла: " + fileSize + " байт");
                System.out.println("Содержимое файла:\n" + new String(fileContent));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unpackMPDArchive(String archiveFile) {
        File file = new File(archiveFile);
        if (!isMPDFormat(file)) {
            System.out.println("Файл не соответствует формату .mpd");
            return;
        }
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            byte[] headerBytes = new byte[1024];
            int headerLength = bis.read(headerBytes);
            String header = new String(headerBytes, 0, headerLength).trim();
            String[] headerParts = header.split(";");
            String fileName = headerParts[0];
            int fileSize = Integer.parseInt(headerParts[1]);

            byte[] fileContent = new byte[fileSize];
            int contentLength = bis.read(fileContent);
            if (contentLength > 0) {
                try (FileOutputStream fos = new FileOutputStream(fileName);
                     BufferedOutputStream bos = new BufferedOutputStream(fos)) {

                    bos.write(fileContent, 0, contentLength);
                    System.out.println("Файл " + fileName + " успешно распакован.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String sourceFile = "file.txt";
        String archiveFile = "archive.mpd";
        createMPDArchive(sourceFile, archiveFile);
        File file = new File(archiveFile);
        readMPDFile(file);
        unpackMPDArchive(archiveFile);
    }
}
