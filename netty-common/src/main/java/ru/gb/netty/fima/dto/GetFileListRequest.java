package ru.gb.netty.fima.dto;

public class GetFileListRequest implements BasicRequest {

    @Override
    public String getType() {
        return "getFileList";
    }
}
