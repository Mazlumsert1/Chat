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
import java.util.List;


public class Server {
    
    static boolean run = true;
    static String ip = "localhost"; 
    static int port = 9090;
    static PrintWriter out;
    static BufferedReader in;
    static String input; 
    static List<ClientHolder> clients = new ArrayList();
    
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(ip,port));
        
        
        while (run){
        Socket socket = ss.accept();
        out = new PrintWriter(socket.getOutputStream(),true);
        in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
        input = in.readLine();
        ClientHolder ch = new ClientHolder("test", socket);
        clients.add(ch);
            for (ClientHolder client : clients) {
                ch.sendMsg("" +socket);
            }
        
        }
    }
    
    // SPØRGSMÅL:
    //Skal instantiate af ClientHolder ligge i while loop?
    // Skal add ligge i while loop?

}
