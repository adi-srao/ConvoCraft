package com.convocraft.chatroom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.convocraft.chatroomManager.User;;

public class Poll {
    private String pollID;
    private String question;
    private List<String> options;
    private HashMap<User, String> responses;

    public Poll(String pollID, String question, List<String> options) {
        this.pollID = pollID;
        this.question = question;
        this.options = options;
        this.responses = new HashMap<>();
    }

    public String getQuestion() {
        return question;
    }

    public void addResponse(User user, String response) {
        if (options.contains(response)) {
            if(responses.containsKey(user)) {
                System.out.println("User " + user.getUserName() + "has already responded");
            }
            else{
                responses.put(user, response);
            }
        } else {
            System.out.println("Invalid response!");
        }
    }

    public String[] getPollResults() {
        HashMap<String, Integer> results = new HashMap<>();
        for (String option : options) {
            results.put(option, 0);
        }
        for (String response : responses.values()) {
            results.put(response, results.get(response) + 1);
        }
        String maxResponse = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxResponse = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        return new String[] {maxResponse, String.valueOf(maxCount)};
}

    public String toString() {
        String output = "Poll ID: " + pollID + "\n" + "Question: " + question + "\n" + "Options:\n";
        for(String option : options)
            output += option + "\n";
        
        String[] results = getPollResults();
        output += "Results:\n" + results[0] + ": " + results[1] + "\n";
        return output;    }
}
