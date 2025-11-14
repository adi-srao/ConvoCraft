package com.convocraft.cmdManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import com.convocraft.MessageSender;
import com.convocraft.chatroom.Chatroom;
import com.convocraft.chatroomManager.User;

public class existingRoomSender {
    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            User user;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.ser"))) {
                user = (User) ois.readObject();
            } catch (ClassNotFoundException e) {
                throw new IOException("Failed to deserialize user object", e);
            }

            Chatroom chatroom = new Chatroom(user.getChatroomName(), user.getUserName(), user.getChatroomIp(), user.getChatroomPort());
            String chatroomName = user.getChatroomName();
            user = new User(user.getUserName(), chatroom, user.getChatroomIp(), user.getChatroomPort());
            Thread senderThread = new Thread(new MessageSender(user, scanner));
            senderThread.start();

            String statusFilePath = chatroomName + "status_e.txt"; // Relative path to status.txt

            // Monitor the status file
            boolean keepRunning = true;
            while (keepRunning) {
            try {
                String status = new String(Files.readAllBytes(Paths.get(statusFilePath))).trim();
                if ("false".equalsIgnoreCase(status)) {
                    System.out.println("Message Sender has closed. Terminating program...");
                    keepRunning = false;
                }
                Thread.sleep(1000); // Check the file every second
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    keepRunning = false; // Exit on error
                }
            }

            try {
                senderThread.join();
            } catch (InterruptedException e) {
                // Handle the exception
                System.err.println("Thread was interrupted: " + e.getMessage());
            }
        }
    }
}
