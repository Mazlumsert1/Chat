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

//    public void hello() throws IOException {
//       test1();
//       test2();
//       test3();
//       test4();
//   }
    @Test
    public void test1() throws IOException {

        Socket socket = new Socket("localhost", 9090);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner in = new Scanner(socket.getInputStream());

        out.println("USER#peter");
        String user = in.nextLine();
        System.out.println(user);
        Assert.assertEquals("USERLIST#{peter}", user);

    }

    @Test
    public void test2() throws IOException {
        Socket socket = new Socket("localhost", 9090);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner in = new Scanner(socket.getInputStream());

        out.println("MSG#*#Java");
        String msg = in.nextLine();
        System.out.println(msg);
        Assert.assertEquals("MSG#*#Java", msg);

    }

    @Test
    public void test3() throws IOException {

        Socket socket2 = new Socket("localhost", 9090);
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream(), true);
        Scanner in2 = new Scanner(socket2.getInputStream());

        out2.println("USER#hans");
        String user1 = in2.nextLine();
        System.out.println(user1);
        Assert.assertEquals("USERLIST#[peter,hans]#", user1);

    }

    @Test
    public void test4() throws IOException {

        Socket socket2 = new Socket("localhost", 9090);
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream(), true);
        Scanner in2 = new Scanner(socket2.getInputStream());

        out2.println("MSG#*#JavaScript");
        String msg1 = in2.nextLine();
        System.out.println(msg1);
        Assert.assertEquals("MSG#[peter,hans]#JavaScript", msg1);

    }
}
