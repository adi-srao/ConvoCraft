package com.convocraft.cmdManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import com.convocraft.MessageReceiver;
import com.convocraft.chatroom.Chatroom;
import com.convocraft.chatroomManager.User;

public class existingRoom {

    private static boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z0-9_]+$";
        return username.matches(regex);
    }
    public static void main(String[] args) throws IOException {
        System.out.print("\033[H\033[2J"); // move cursor to top and clear screen
        System.out.flush();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name of Chatroom to join: ");
        String chatroomName = scanner.nextLine();
        String username;
        while (true){
            System.out.print("Enter your username: ");
            username = scanner.nextLine();
            if (isValidUsername(username)){
                break;
            }else{
                System.out.println("Invalid username only use characters: a-z 0-9 and _");
            }

        }


        System.out.print("Enter IP Address of the chatroom host: ");
        String hostIp = scanner.nextLine();
        System.out.print("Enter port of Chatroom host: ");
        String hostPort = scanner.nextLine();

        Chatroom chatroom = new Chatroom(chatroomName, username, hostIp, hostPort);

        User user = new User(username, chatroom, hostIp, hostPort);
        // Start threads for sending and receiving messages
        Thread receiverThread = new Thread(new MessageReceiver(user));

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.ser"));
        oos.writeObject(user);
        oos.close();

        Process newTerminalProcess = null;
        try{
            String currentDir = new File(".").getCanonicalPath();
            String command = String.format(
                "cd %s && javac -cp target/classes;target/dependency/* src/main/java/com/convocraft/cmdManager/existingRoomSender.java  && java -cp target/classes;target/dependency/*;target/dependency/javax.jms-api-2.0.1.jar com.convocraft.cmdManager.existingRoomSender\"",
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
        String statusFilePath = chatroomName+"status_e.txt";
        try (FileWriter writer = new FileWriter(statusFilePath)) {
            writer.write("true");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
