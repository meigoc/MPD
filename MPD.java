import java.io.*;

public class MPD {

    public static void createMPDArchive(String sourceFile, String archiveFile) {
        try (FileOutputStream fos = new FileOutputStream(archiveFile);
             FileInputStream fis = new FileInputStream(sourceFile)) {
            String header = sourceFile + ";" + fis.available() + ";";
            fos.write(header.getBytes());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            System.out.println("Архив в формате .mpd создан успешно!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isMPDFormat(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] headerBytes = new byte[1024];
            int length = fis.read(headerBytes);
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
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] headerBytes = new byte[1024];
            int headerLength = fis.read(headerBytes);
            String header = new String(headerBytes, 0, headerLength).trim();
            String[] headerParts = header.split(";");
            String fileName = headerParts[0];
            int fileSize = Integer.parseInt(headerParts[1]);
            byte[] fileContent = new byte[fileSize];
            int contentLength = fis.read(fileContent);
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
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] headerBytes = new byte[1024];
            int headerLength = fis.read(headerBytes);
            String header = new String(headerBytes, 0, headerLength).trim();
            String[] headerParts = header.split(";");
            String fileName = headerParts[0];
            int fileSize = Integer.parseInt(headerParts[1]);
            byte[] fileContent = new byte[fileSize];
            int contentLength = fis.read(fileContent);
            if (contentLength > 0) {
                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                    fos.write(fileContent, 0, contentLength);
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
