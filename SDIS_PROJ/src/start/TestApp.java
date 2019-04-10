package start;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import utils.RMI;

 
 
public class TestApp {
     
	private static String peer_ap;
	private static String sub_protocol;
	private static String file_path;
	private static double disk_space;
	private static int replication_degree;
    public static void main(String[] args) throws UnknownHostException { //<peer_ap> <sub_protocol> <opnd_1> <opnd_2> 
    	
    	peer_ap = args[0];
        sub_protocol = args[1].toUpperCase();
        System.out.println(args[0]);
        
        try {
         
        	
        	Registry registry = LocateRegistry.getRegistry("localhost");
      	    RMI stub = (RMI) registry.lookup(peer_ap);
              System.out.println(args[0]);
        } catch (Exception e) {
        	System.err.println("TestApp exception: " + e.toString());
       	    e.printStackTrace();
        }
        
        System.out.println(args[0]);
       if(sub_protocol == "BACKUP")
       {
    	   file_path = args[2];
       }
       else if(sub_protocol == "RESTORE")
       {
    	   file_path = args[2];
       }
       else if(sub_protocol == "DELETE")
       {
    	   file_path = args[2];
       }
       else if(sub_protocol == "RECLAIM")
       {
    	   disk_space = Double.parseDouble(args[2]);
    	   replication_degree = Integer.parseInt(args[3]);
       }
       System.out.println(args[0]);
        
    }
}