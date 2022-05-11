package ru.gb.netty.fima.autReg;

public class GetFileListRequest implements BasicRequest {

    @Override
    public String getType() {
        return "getFileList";
    }
}
