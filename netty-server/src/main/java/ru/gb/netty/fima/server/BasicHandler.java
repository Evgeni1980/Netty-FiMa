package ru.gb.netty.fima.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.gb.netty.fima.client.autReg.*;

import java.io.File;
import java.io.FileOutputStream;

public class BasicHandler extends ChannelInboundHandlerAdapter {

    private final static String LOGIN_OK = "login_ok";
    private final static String LOGIN_NO = "login_no";
    private final static String REG_OK = "reg_ok";
    private final static String REG_NO = "reg_no";

    private String login;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        BasicRequest request = (BasicRequest) msg;
        System.out.println(request.getType());

        if (request instanceof AuthRequest) {
            if(((AuthRequest) request).checkLoginAndPassword()){
                login = ((AuthRequest) request).getLogin();
                BasicResponse loginOkResponse = new BasicResponse(new File(String.valueOf(((DownloadFileRequest) request).getPathOfFile())), LOGIN_OK, "download_ok");
                channelHandlerContext.writeAndFlush(loginOkResponse);
            } else {
                BasicResponse loginNoResponse = new BasicResponse(new File(String.valueOf(((DownloadFileRequest) request).getPathOfFile())), LOGIN_NO, "download_ok");
                channelHandlerContext.writeAndFlush(loginNoResponse);
            }
        } else if(request instanceof RegRequest){
            if(((RegRequest) request).registration()){
                ((RegRequest) request).hashPassword();
                BasicResponse regOkResponse = new BasicResponse(new File(String.valueOf(((DownloadFileRequest) request).getPathOfFile())), REG_OK, "download_ok");
                channelHandlerContext.writeAndFlush(regOkResponse);
            } else {
                BasicResponse regNoResponse = new BasicResponse(new File(String.valueOf(((DownloadFileRequest) request).getPathOfFile())), REG_NO, "download_ok");
                channelHandlerContext.writeAndFlush(regNoResponse);
            }
        } else if (request instanceof DisconnectRequest) {
            BasicResponse basicResponse = new BasicResponse(new File(String.valueOf(((DownloadFileRequest) request).getPathOfFile())), "file list....", "download_ok");
            channelHandlerContext.writeAndFlush(basicResponse);


        } else if(request instanceof LoadFileRequest){
            String pathOfFile = String.format(((LoadFileRequest) request).getPathForLoad() + "/" + ((LoadFileRequest) request).getFilename());
            FileOutputStream fos = new FileOutputStream(pathOfFile);
            fos.write(((LoadFileRequest) request).getData());
            BasicResponse basicResponse = new BasicResponse(new File(String.valueOf(((DownloadFileRequest) request).getPathOfFile())), "load_ok", "download_ok");
            channelHandlerContext.writeAndFlush(basicResponse);

        } else if(request instanceof DownloadFileRequest){
            BasicResponse basicResponse = new BasicResponse(new File(String.valueOf(((DownloadFileRequest) request).getPathOfFile())),((DownloadFileRequest) request).getFileName(), "download_ok");
            channelHandlerContext.writeAndFlush(basicResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}


