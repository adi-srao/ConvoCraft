package com.convocraft.chatroom;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMQConnection{

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;

    public ActiveMQConnection(String topicName){
        try{
            connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            // Create a connection
            connection = connectionFactory.createConnection();
            connection.start(); System.out.println("Connection started successfully");


            // Create a session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); System.out.println("Session created successfully");


            // Create a destination (topic for broadcasts)
            destination = session.createTopic(topicName); System.out.println("Destination created successfully");
        }catch (JMSException e) {
            System.out.println("Error connecting to ActiveMQ: " + e.getMessage());
        }
    }
    
    public ActiveMQConnection(String topicName, String hostIp, String hostPort){
        try{
            connectionFactory = new ActiveMQConnectionFactory("tcp://"+hostIp+":"+hostPort);

            // Create a connection
            connection = connectionFactory.createConnection();
            connection.start(); System.out.println("Connection started successfully");

            // Create a session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); System.out.println("Session created successfully");

            // Create a destination (topic for broadcasts)
            destination = session.createTopic(topicName); System.out.println("Destination created successfully");
        } catch (JMSException e) {
            System.out.println("Error connecting to ActiveMQ: " + e.getMessage());
        }
    }

    public Session getSession(){
        return session;
    }
    public Destination getDestination(){
        return destination;
    }
    public void close(){
        try{
            connection.close();
        }catch(JMSException e){
            System.err.println("JMS error occurred: " + e.getMessage());   
        }
    }
}

