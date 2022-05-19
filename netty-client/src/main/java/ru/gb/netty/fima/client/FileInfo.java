package ru.gb.netty.fima.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileInfo {
    private String fileName;
    private Path path;
    private FileType type;
    private long size;
    private LocalDateTime lastModified;

    public String getFileName() {
        return fileName;
    }

    public enum FileType {
        FILE("F"), DIRECTORY("D");  // Если тип файл: F, если директория: D

        private String name;

        public String getName() {
            return name;
        }

        FileType(String name) {
            this.name = name;
        }

    }


    public Path getPath() {
        return path;
    }

    public void setFilename(String filename) {
        this.fileName = filename;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getFilename() {
        return fileName;
    }

    public FileType getType() {
        return type;
    }

    public long getSize() {
        return size;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public FileInfo(Path path) {
        try {
            this.fileName = path.getFileName().toString();                            // Конкретно имя файла
            this.size = Files.size(path);                                             // Возвращает размер файла в байтах
            this.type = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE; // Если мы смотрим на дерикторию, то тогда типом будет дериктория. Если нет, то тогда тип файл...
            if (this.type == FileType.DIRECTORY) {                                    // Если тип деректория, то размер будет -1L, нужно при сортировке файлов в таблице
                this.size = -1L;
            }
            this.lastModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(0)); // Получение даты последней модификации
        } catch (IOException e) {
            throw new RuntimeException("Unable to create file info from path");       // Если файл битый, бросится исключение. Может уапасть программа.
        }
    }

}
