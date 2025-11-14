package com.convocraft.cmdManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import com.convocraft.MessageReceiver;
import com.convocraft.chatroom.Chatroom;
import com.convocraft.chatroomManager.Admin;

public class newRoom {

    private static boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z0-9_]+$";
        return username.matches(regex);
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\033[H\033[2J"); // move cursor to top and clear screen
        System.out.flush();

        System.out.print("Enter Chatroom name: ");
        String chatroomName = scanner.nextLine();
        String adminName;
        while (true){
            System.out.print("Enter admin name: ");
            adminName = scanner.nextLine();
            if (isValidUsername(adminName)){
                break;
            }else{
                System.out.println("Invalid username only use characters: a-z 0-9 and _");
            }

        }

        String ipAddress = null;
        try {                                                                           // Get the IP address
            ipAddress = InetAddress.getLocalHost().getHostAddress();

            // Create a connection factory using the IP address
        } catch (UnknownHostException e) {
            System.out.println("Unknown host");
        }

        Chatroom chatroom = new Chatroom(chatroomName, adminName, ipAddress);

        Admin admin = new Admin(adminName, chatroom);
        // Start threads for sending and receiving messages
        Thread receiverThread = new Thread(new MessageReceiver(admin));

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("admin.ser"));
        oos.writeObject(admin);
        oos.close();

        Process newTerminalProcess = null;
        try{
            String currentDir = new File(".").getCanonicalPath();
            String command = String.format(
                "cd %s && javac -cp target/classes;target/dependency/* src/main/java/com/convocraft/cmdManager/newRoomSender.java  && java -cp target/classes;target/dependency/*;target/dependency/javax.jms-api-2.0.1.jar com.convocraft.cmdManager.newRoomSender\"",
                currentDir
            );

            // Use ProcessBuilder to execute the command
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", "cmd.exe", "/k", command);

            // Start the process (this opens the new terminal window)
            newTerminalProcess = processBuilder.start();

            System.out.println("New terminal opened to send messages.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // finally {
        //     // Ensure the new terminal process is destroyed when exiting
        //     if (newTerminalProcess != null) {
        //         newTerminalProcess.destroy();
        //         System.out.println("New terminal process terminated.");
        //     }
        // }
        
        // Create a .txt file to store run status of newRoomSender terminal window and write "true" into it
        String statusFilePath = chatroomName+"status.txt";
        try (FileWriter writer = new FileWriter(statusFilePath)) {
            writer.write("true");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Your IP address is: " + ipAddress);
        System.out.println("Your port is: 61616" );

        receiverThread.start();

        try {
            receiverThread.join();
        } catch (InterruptedException e) {
            // Handle the exception
            System.err.println("Thread was interrupted: " + e.getMessage());
        } catch (RuntimeException e) {
            // Handle the exception
            System.err.println("Thread was interrupted: " + e.getMessage());
        }

        // Write "false" to status.txt upon completion
        try (FileWriter writer = new FileWriter(statusFilePath)) {
            writer.write("false");
            System.out.println("Message reciever is closing...");
        } catch (IOException e) {
            System.err.println("Failed to update status.txt: " + e.getMessage());
        }
        System.out.println("Chatroom has closed");
        scanner.close();
    }
}
