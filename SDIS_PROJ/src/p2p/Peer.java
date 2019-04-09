package p2p;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import utils.RMI;
 
public class Peer implements RMI {
     
    final static String INET_ADDR = "224.0.0.3";
    final static int PORT = 8888;
 
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        // Get the address that we are going to connect to.
        InetAddress addr = InetAddress.getByName(INET_ADDR);
      
        // Open a new DatagramSocket, which will be used to send the data.
        try (DatagramSocket serverSocket = new DatagramSocket()) {
        	
        	  Peer obj = new Peer();
      	    RMI stub = (RMI) UnicastRemoteObject.exportObject(obj, 0);

      	    // Bind the remote object's stub in the registry
      	    Registry registry = LocateRegistry.getRegistry();
      	    registry.bind("Hello", stub);

      	    System.err.println("Server ready");
      	    
            for (int i = 0; i < 5; i++) {
                String msg = "Sent message no " + i;
 
                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
                        msg.getBytes().length, addr, PORT);
                serverSocket.send(msgPacket);
      
                System.out.println("Server sent packet with msg: " + msg);
                Thread.sleep(500);
            }
        } catch (Exception e) {
    	    System.err.println("Server exception: " + e.toString());
    	    e.printStackTrace();
    	}
    }

    public String sayHello() {
    	return "Hello, world!";
        }
}