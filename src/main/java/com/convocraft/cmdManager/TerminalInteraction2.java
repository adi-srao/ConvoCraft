package com.convocraft.cmdManager;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TerminalInteraction2 {

    private Process newTerminalProcess;
    private Scanner scanner;   

    public TerminalInteraction2(){
        newTerminalProcess = null;
        scanner = new Scanner(System.in);
    }
    public void loop(){            
        try{
            while (true) {
                System.out.print("Enter 'CREATE' to create a new chatroom or 'JOIN' to join an existing one or 'EXIT' to exit: ");
                String input = scanner.nextLine();
                if (input.equals("'CREATE'")|| input.equals("CREATE")){                          //Creating a new chatroom and user
                    // TODO: Make sure chatroom name is unique while extending
                    this.createRoom();

                } else if (input.equals("'JOIN'")|| input.equals("JOIN")){            //Joining an existing chatroom and creating user
                    this.joinRoom();

                } else if (input.equals("'EXIT'")|| input.equals("EXIT")){            //Joining an existing chatroom and creating user 
                    System.exit(0);

                } else {
                    System.out.println("Invalid input, please type 'CREATE' or 'JOIN'.");
                }
            }
        }finally {
                // Ensure the new terminal process is destroyed when exiting
                if (newTerminalProcess != null) {
                    newTerminalProcess.destroy();
                    System.out.println("New terminal process terminated.");
                }
            }
        }

    public void createRoom() {

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

            loop();

        }catch (IOException e) {
            e.printStackTrace();
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
            loop();

        } catch (IOException e) {
            e.printStackTrace();
        }    
    }
}
