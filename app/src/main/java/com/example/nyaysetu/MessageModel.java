package com.example.nyaysetu;

public class MessageModel {
    public static String SENT_BY_USER = " User";
    public static String SENT_BY_ASSISTANT = "Our App";
    String msg , sendBy;

    public MessageModel(String msg, String sendBy) {
        this.msg = msg;
        this.sendBy = sendBy;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }
}
