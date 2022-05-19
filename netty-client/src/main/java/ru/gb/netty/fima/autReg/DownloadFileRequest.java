package ru.gb.netty.fima.autReg;

import java.nio.file.Path;

public class DownloadFileRequest implements BasicRequest{

    private Path pathOfFile;

    public Path getPathOfFile() {
        return pathOfFile;
    }

    public DownloadFileRequest(Path pathOfFile) {
        this.pathOfFile = pathOfFile;
    }

    @Override
    public String getType() {
        return "download";
    }
}
