package com.convocraft;

import com.convocraft.cmdManager.TerminalInteraction2;

public class App
{
    
    public static void main( String[] args )
    {       
        TerminalInteraction2 terminalInteraction = new TerminalInteraction2();
        terminalInteraction.loop();
    }
}
