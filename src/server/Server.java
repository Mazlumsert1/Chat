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
            input = in.readLine();
            ClientHolder ch = new ClientHolder("test", socket, server);
            clients.add(ch);
            for (ClientHolder client : clients) {
                ch.sendMsg("" + socket);
            }
            ch.start();
            distributingSendMethods("*");
        }
    }

    public static void sendMsgToAll(String msg) throws IOException {
        for (ClientHolder client : clients) {
            client.sendMsg(msg);
        }
    }
    
    public static void SendMessageToOne(String msg) throws IOException{
        List <String> splitted = Arrays.asList(msg.split("#"));
        String command = splitted.get(0);
        String users= splitted.get(1);
        String message = splitted.get(2);
        
        for (int i = 0; i < clients.size(); i++) {
               ClientHolder client = clients.get(i);
               String user = client.getUsername();
               if(users.equals(user)){
                   client.sendMsg(message);
                }
            }
    
    }
    
    public static void sendMsgToMultiplyPeople(String msg) throws IOException{
        List <String> splitted = Arrays.asList(msg.split("#"));
        String command = splitted.get(0);
        String people = splitted.get(1);
        String message = splitted.get(2);
        List <String > splittedPeople = Arrays.asList(people.split(","));
        
        for (String splittedPeople1 : splittedPeople) {
            for (int i = 0; i < clients.size(); i++) {
               ClientHolder client = clients.get(i);
               String user = client.getUsername();
               if(splittedPeople1.equals(user)){
                   client.sendMsg(message);
                }
            }
        }
        
        
    }
        
    public static void distributingSendMethods(String msg) throws IOException {
        if (msg.contains("*")){
            sendMsgToAll(msg);
        }else if(msg.contains(",")){
        // make method to send multiply people
        
        }else {
        // Send to person
        
        }
    }

}
