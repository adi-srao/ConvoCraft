package com.convocraft.chatroomManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.convocraft.chatroom.Chatroom;

public class Admin extends User {

    public Admin(String username, Chatroom chatroom) throws UnknownHostException{
        super(username,chatroom, InetAddress.getLocalHost().getHostAddress(), "61616");

    }
}
