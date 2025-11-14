package com.convocraft.cmdManager;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TerminalInteraction {

    public void createRoom() {
        Process newTerminalProcess = null;

        try {
            // Get the current working directory
            String currentDir = new File(".").getCanonicalPath();

            // Command to open a new terminal, run the Java file
            String command = String.format(
                "cmd.exe /c start cmd.exe /k \"cd %s && javac -cp target/classes;target/dependency/* src/main/java/com/convocraft/cmdManager/newRoom.java  && java -cp target/classes;target/dependency/*;target/dependency/javax.jms-api-2.0.1.jar com.convocraft.cmdManager.newRoom && exit\"",
                currentDir
            );

            // Use ProcessBuilder to execute the command
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);

            // Start the process (this opens the new terminal window)
            newTerminalProcess = processBuilder.start();

            System.out.println("New terminal opened to run new Chatroom.");

            Scanner scanner = new Scanner(System.in);

            // Main Terminal Input Handling
            while (true) {
                System.out.print("Enter 'CREATE' to create a new chatroom or 'JOIN' to join an existing one: ");
                String input = scanner.nextLine();
                if (input.equals("'CREATE'")|| input.equals("CREATE")){                          //Creating a new chatroom and user
                    // TODO: Make sure chatroom name is unique while extending
                    TerminalInteraction terminalInteraction = new TerminalInteraction();
                    terminalInteraction.createRoom();
                } else if (input.equals("'JOIN'")|| input.equals("JOIN")){            //Joining an existing chatroom and creating user

                    TerminalInteraction terminalInteraction = new TerminalInteraction();
                    terminalInteraction.joinRoom();
                } else if (input.equals("'EXIT'")|| input.equals("EXIT")){            //Joining an existing chatroom and creating user 
                    System.exit(0);
                } else {
                    System.out.println("Invalid input, please type 'CREATE' or 'JOIN'.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Ensure the new terminal process is destroyed when exiting
            if (newTerminalProcess != null) {
                newTerminalProcess.destroy();
                System.out.println("New terminal process terminated.");
            }
        }
    }

    public void joinRoom() {
        Process newTerminalProcess = null;

        try {
            // Get the current working directory
            String currentDir = new File(".").getCanonicalPath();

            // Command to open a new terminal, run the Java file
            String command = String.format(
                "cmd.exe /c start cmd.exe /k \"cd %s && javac -cp target/classes;target/dependency/* src/main/java/com/convocraft/cmdManager/existingRoom.java  && java -cp target/classes;target/dependency/*;target/dependency/javax.jms-api-2.0.1.jar com.convocraft.cmdManager.existingRoom && exit\"",
                currentDir
            );

            // Use ProcessBuilder to execute the command
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);

            // Start the process (this opens the new terminal window)
            newTerminalProcess = processBuilder.start();

            System.out.println("New terminal opened for existing Chatroom.");

            // Main terminal input handling
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Enter 'CREATE' to create a new chatroom or 'JOIN' to join an existing one: ");
                String input = scanner.nextLine();
                if (input.equals("'CREATE'")|| input.equals("CREATE")){                          //Creating a new chatroom and user
                    // TODO: Make sure chatroom name is unique while extending
                    TerminalInteraction terminalInteraction = new TerminalInteraction();
                    terminalInteraction.createRoom();
                } else if (input.equals("'JOIN'")|| input.equals("JOIN")){            //Joining an existing chatroom and creating user

                    TerminalInteraction terminalInteraction = new TerminalInteraction();
                    terminalInteraction.joinRoom();
                } else if (input.equals("'EXIT'")|| input.equals("EXIT")){            //Joining an existing chatroom and creating user 
                    System.exit(0);
                } else {
                    System.out.println("Invalid input, please type 'CREATE' or 'JOIN'.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Ensure the new terminal process is destroyed when exiting
            if (newTerminalProcess != null) {
                newTerminalProcess.destroy();
                System.out.println("New terminal process terminated.");
            }
        }
    }
}
