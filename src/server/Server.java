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
import java.util.List;

public class Server extends Thread {

    static boolean run = true;
    static String ip = "localhost";
    static int port = 9090;
    static PrintWriter out;
    static BufferedReader in;
    static String input;
    static List<ClientHolder> clients = new ArrayList();
    static Server server = new Server();

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(ip, port));
        
        while (run) {
            Socket socket = ss.accept();
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("Please input a username(USER#'name'");
            input = in.readLine();
            String[] split = input.split("#");
            
            ClientHolder ch = new ClientHolder(split[1], socket, server);
            clients.add(ch);
            ch.start();
            out.println("Welcome to the chat " + split[1] + "!");
            
            printClientList();
        }
    }
    
    public static void commandAccept(String input) throws IOException {
        //sendMsgToAll(input);
            System.out.println("Input: " +input);
            List<String> splitted = Arrays.asList(input.split("#"));
            String command = splitted.get(0);

            System.out.println(splitted);
            
            switch (command) {
                case "USER":
                    //Mangler 
                    break;
                case "MSG":
                    distributingSendMethods(input);
                    break;
                case "STOP":
                   // removeClient(ch); //Ret denne linje
                    break;
            }
    }

    public static void sendMsgToAll(String msg) throws IOException {
        for (ClientHolder client : clients) {
            client.sendMsg(msg);
        }
    }

    public static void sendMsgToOne(String msg) throws IOException {
        List<String> splitted = Arrays.asList(msg.split("#"));
        String command = splitted.get(0);
        String users = splitted.get(1);
        String message = splitted.get(2);

        for (int i = 0; i < clients.size(); i++) {
            ClientHolder client = clients.get(i);
            String user = client.getUsername();
            if (users.equals(user)) {
                client.sendMsg(msg);
            }
        }

    }

    public static void sendMsgToMultiplyPeople(String msg) throws IOException {
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
                    client.sendMsg(message);
                }
            }
        }

    }

    public static void distributingSendMethods(String msg) throws IOException {
        if (msg.contains("*")) {
            System.out.println("Contains *");
            sendMsgToAll(msg);
        } else if (msg.contains(",")) {
            sendMsgToMultiplyPeople(msg);
        } else {
            sendMsgToOne(msg);
        }
    }

    public static void removeClient(ClientHolder client) {
        clients.remove(client);
        client.removeClient();
    }
    
    public static void printClientList() {
        List<String> onlineUsers = new ArrayList();
        for (ClientHolder client : clients) {
            onlineUsers.add(client.getUsername());
        }
        out.println("Online: " + onlineUsers);
    }

}
