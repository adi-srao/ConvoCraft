package com.convocraft;
import java.util.Scanner;

import com.convocraft.chatroomManager.User;
import com.convocraft.commandProcessor.commandProcessor;


public class MessageReceiver implements Runnable {
    private User user;
    private commandProcessor commandProcessor;

    public MessageReceiver(User user) {
        this.user = user;
        this.commandProcessor = new commandProcessor(user.getChatroom(), user);
    }

    @Override
    public void run() {
        System.out.println("-------------------------------------");
        while (true) {
            String message = user.receiveMessage();
            if ((message != null)&&commandProcessor.isActive()) {
                commandProcessor.processReceive(message);
                // System.out.print("\033[10;0H");
                // Print message
                // System.out.print("\033[B");
                // System.out.print("\033[H\033[0E");
                String i = "/leave "+ user.getUserName();
                if (message.equals(i)){
                    break;
                }
            }
        }
    }
}
