package ru.gb.netty.fima.client.autReg;
import ru.gb.netty.fima.client.autReg.BasicRequest;

public class DeleteFileRequest implements BasicRequest {
    private String pathOfFile;

    public DeleteFileRequest(String pathOfFile) {
        this.pathOfFile = pathOfFile;

    }

    @Override
    public String getType() {
        return "delete_file";
    }

    public String getPathOfFile() {
        return pathOfFile;
    }
}
