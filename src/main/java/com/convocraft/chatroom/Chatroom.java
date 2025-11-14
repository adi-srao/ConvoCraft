package com.convocraft.chatroom;

import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.convocraft.chatroomManager.ActiveMQConnectionManager;
import com.convocraft.chatroom.Poll;

public class Chatroom {

    private String name;
    ActiveMQConnection connection;
    HashMap<String, String> userIpMap;
    HashMap<String, Poll> pollMap;
    Session session;
    MessageProducer producer;
    MessageConsumer consumer;
    
    public Chatroom(String chatroomName, String adminName, String adminIp){

        this.userIpMap = new HashMap<>();
        this.name = chatroomName;
        userIpMap.put(adminName, adminIp);

        connection = ActiveMQConnectionManager.createChatroomTopic(chatroomName);
        session = connection.getSession();
        try {
            producer = session.createProducer(connection.getDestination());
            consumer = session.createConsumer(connection.getDestination());
        } catch (JMSException e) {
            System.err.println("JMS error occurred: " + e.getMessage());
        }
    }

    public Chatroom(String chatroomName, String username, String hostIp, String hostPort){

        this.name = chatroomName;
        this.userIpMap = new HashMap<>();
        userIpMap.put(username, hostIp);

        connection = ActiveMQConnectionManager.joinChatroomTopic(chatroomName, hostIp, hostPort);
        session = connection.getSession();
        try {
            producer = session.createProducer(connection.getDestination());
            consumer = session.createConsumer(connection.getDestination());
        } catch (JMSException e) {
            System.err.println("JMS error occurred: " + e.getMessage());
        }
    }

    public void sendMessage(String message){ // send message to topic
        try{
        TextMessage textMessage = session.createTextMessage(message);
        producer.send(textMessage);
        }catch(JMSException e){
            System.err.println("JMS error occurred: " + e.getMessage());
        }
    }

    public String receiveMessage(){ // receive message from topic
        try{
            Message receivedMessage = consumer.receive();
            // Print the received message
            if (receivedMessage instanceof TextMessage) {
                return ((TextMessage) receivedMessage).getText();
            }
        }catch(JMSException e){
            System.err.println("JMS error occurred: " + e.getMessage());
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String> getUserIpMap() {
        return userIpMap;
    }

    public HashMap<String, Poll> getPollMap() {
        return pollMap;
    }

    public void addPoll(String pollID, Poll newPoll){
        pollMap.put(pollID, newPoll); 
    }
    
    public void closeConnection(){
        try {
            session.close();
            producer.close();
            consumer.close();
        } catch (JMSException e) {
            System.err.println("JMS error occurred: " + e.getMessage());
        }
            connection.close(); 
    }                                                                                                                                                                  
}