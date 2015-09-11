/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
                try {
                    Server.main(null);
                } catch (IOException ex) {
                    Logger.getLogger(TestChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
    
    
    @AfterClass
    public static void tearDownClass() throws IOException {
       Server.stopServer();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void hello() throws IOException {
         
         Socket socket = new Socket("localhost" , 9090);
         PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
         Scanner input = new Scanner(socket.getInputStream());
         
         // Testet om peter er i userlisten.
         output.println("USER#peter");
         String text = input.nextLine();
         System.out.println(text);
         Assert.assertEquals("UserList#peter" , text);
         
         
     
     }
}
