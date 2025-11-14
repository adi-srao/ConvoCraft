package com.convocraft.chatroomManager;

import java.io.Serializable;

import com.convocraft.chatroom.Chatroom;

public class User implements Serializable {
    String username;
    transient Chatroom chatroom;
    private String chatroomName;
    private String chatroomIp;
    private String chatroomPort;

    public User(String username, Chatroom chatroom, String ipAddress, String port) {
        this.username = username;
        this.chatroom = chatroom;
        this.chatroomName = chatroom.getName();
        this.chatroomIp = ipAddress;
        this.chatroomPort = port;
    }

    public String receiveMessage(){
        return chatroom.receiveMessage();
    }
    public void sendMessage(String message){
        chatroom.sendMessage(message);
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public String getUserName() {
        return username;
    }

    public String getChatroomIp() {        
        return chatroomIp;
    }

    public String getChatroomPort() {
        return chatroomPort;
    }
    public Chatroom getChatroom() {
        return chatroom;
    }
}

