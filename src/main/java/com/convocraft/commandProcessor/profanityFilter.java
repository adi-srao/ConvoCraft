package com.convocraft.commandProcessor;

import java.util.HashMap;

public class profanityFilter {
    static 
    {
        System.setProperty("java.library.path", System.getProperty("java.library.path") + ":src/main/resources");
        System.loadLibrary("profanityFilter");    
    }

    public native String filterProfanityRetainingWhitespace(String message, String[] badWords);
}
