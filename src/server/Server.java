package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    
    static boolean run = true;
    static String ip; 
    static int port;
    static PrintWriter out;
    static BufferedReader in;
    static String input; 
    
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(ip,port));
        
        
        while (run){
        Socket socket = ss.accept();
        out = new PrintWriter(socket.getOutputStream(),true);
        in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
        input = in.readLine();
        }
    }

    

}
