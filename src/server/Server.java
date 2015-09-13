package server;

import clientHolder.ClientHolder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;

public class Server extends Thread {

    static boolean run = true;
    static String ip = "100.86.74.7";
    static int port = 9090;
    static PrintWriter out;
    static BufferedReader in;
    static String input;

    static List<ClientHolder> clients = Collections.synchronizedList(new ArrayList<ClientHolder>());
//     static List<ClientHolder> clients = new ArrayList();

    static Server server = new Server();
    static String usr = null;
    private static final Properties properties = Utils.initProperties("server.properties");

    public static void main(String[] args) {
        String logFile = properties.getProperty("logFile");
        Utils.setLogFile(logFile, Server.class.getName());
        
        try {
            ServerSocket ss = new ServerSocket();
            ss.bind(new InetSocketAddress(ip, port));

            while (run) {
                Socket socket = ss.accept();
                Logger.getLogger(Server.class.getName()).log(Level.INFO, "Connected to a client");
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                out.println("Please input a username(USER#'name'");

                input = in.readLine();

                String[] split = input.split("#");

                ClientHolder ch = new ClientHolder(split[1], socket, server);
                clients.add(ch);
                ch.start();
//                out.println("Welcome to the chat " + split[1] + "!");

                printClientList();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            Utils.closeLogger(Server.class.getName());
        }

    }

    public void accept(String username) {
        String test = "hej";
        usr = test;
    }

    public static void commandAccept(String input, String username) throws IOException {
        //sendMsgToAll(input);
        System.out.println("Input: " + input);
        List<String> splitted = Arrays.asList(input.split("#"));
        String command = splitted.get(0);

        System.out.println(splitted);

        switch (command) {
            case "USER":
                //Mangler 
                break;
            case "MSG":
                distributingSendMethods(input, username);
                break;
        }
    }

    public static void sendMsgToAll(String msg, String username) throws IOException {
        System.out.println(msg);
        List<String> splitted = Arrays.asList(msg.split("#"));
        String command = splitted.get(0);
        String users = splitted.get(1);
        String message = splitted.get(2);

        for (ClientHolder client : clients) {
            client.sendMsg(command + "#" + username + "#" + message);
        }
    }

    public static void sendMsgToOne(String msg, String username) throws IOException {
        List<String> splitted = Arrays.asList(msg.split("#"));
        String command = splitted.get(0);
        String users = splitted.get(1);
        String message = splitted.get(2);

        for (int i = 0; i < clients.size(); i++) {
            ClientHolder client = clients.get(i);
            String user = client.getUsername();
            if (users.equals(user)) {
                client.sendMsg(command + "#" + username + "#" + message);
            }
        }

    }

    public static void sendMsgToMultiplyPeople(String msg, String username) throws IOException {
        List<String> splitted = Arrays.asList(msg.split("#"));
        String command = splitted.get(0);
        String people = splitted.get(1);
        String message = splitted.get(2);
        List<String> splittedPeople = Arrays.asList(people.split(","));

        for (String splittedPeople1 : splittedPeople) {
            for (int i = 0; i < clients.size(); i++) {
                ClientHolder client = clients.get(i);
                String user = client.getUsername();
                if (splittedPeople1.equals(user)) {
                    client.sendMsg(command + "#" + username + "#" + message);
                }
            }
        }

    }

    public static void distributingSendMethods(String msg, String username) throws IOException {
        System.out.println(username);
        if (msg.contains("*")) {
            sendMsgToAll(msg, username);
        } else if (msg.contains(",")) {
            sendMsgToMultiplyPeople(msg, username);
        } else {
            sendMsgToOne(msg, username);
        }
    }

    public static void removeClient(ClientHolder client) {
        clients.remove(client);

    }

    public static void printClientList() {
        List<String> onlineUsers = new ArrayList();
        
        for (ClientHolder client : clients) {
            onlineUsers.add(client.getUsername());
           
        }
        String usersOnline = onlineUsers.toString();
        String output;
        
        output = usersOnline.replace("[", "{");
        output = output.replace("]", "}");
        
//        out.println("USERLIST#" + output);
    }

    public static void stopServer() throws IOException {
        ServerSocket ss = null;
//        ss.close();


    }

}
