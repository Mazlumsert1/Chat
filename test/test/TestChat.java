package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import server.Server;

/**
 *
 * @author Lenovo
 */
public class TestChat {

    public TestChat() {
    }

    @BeforeClass
    public static void setUpClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Server.main(null);
            }
        }).start();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        Server.stopServer();
    }

    @Test
    public void hello() throws IOException {

        Socket socket = new Socket("localhost", 9090);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner in = new Scanner(socket.getInputStream());

        out.println("USER#peter");
        String user = in.nextLine();
        System.out.println(user);
        Assert.assertEquals("USERLIST#[peter]", user);
         
        out.println("MSG#*#Java");
        String msg = in.nextLine();
        System.out.println(msg);
        Assert.assertEquals("MSG#*#Java", msg);
       

        Socket socket2 = new Socket("localhost", 9090);
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream(), true);
        Scanner in2 = new Scanner(socket.getInputStream());

        out2.println("USER#hans");
        String user1 = in.nextLine();
        System.out.println(user1);
        Assert.assertEquals("USERLIST#[peter,hans]#", user1);

        out2.println("MSG#*#JavaScript");
        String msg1 = in.nextLine();
        System.out.println(msg1);
        Assert.assertEquals("MSG#[peter,hans]#JavaScript", msg1);

        
//---------------Testes om en specifik bruger modtag beskeden----------
        out.println("MSG#lars#hey");
        String msg2 = in.nextLine();
        System.out.println(msg1);
        Assert.assertEquals("MSG#lars#hey", msg1);


    }
}
