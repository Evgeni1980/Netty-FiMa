package ru.gb.netty.fima.autReg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LoadFileRequest implements BasicRequest {

    private File file;

    private String fileName;

    private byte[] data;

    public String getFilename() {
        return fileName;
    }

    public LoadFileRequest(File file, String fileName) {
        this.file = file;
        this.fileName = fileName;
    }

    @Override
    public String getType() {
        return "load";
    }

    public byte[] getData() throws IOException {
        byte[] fileInArray = new byte[(int)file.length()];
        FileInputStream f = new FileInputStream(file.getPath());
        f.read(fileInArray);
        return fileInArray;
    }



}
