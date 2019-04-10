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
     

    //final static int PORT = 8888;
	private static double version;
	private static int server_id;
    private static int peer_id;
    private static String mcAddr = "224.0.0.3";
    private static int mcPort = 8888;
    private static String mdbAddr;
    private static int mdbPort;
    private static String mdrAddr;
    private static int mdrPort;
    
	private int peerID;
	
    
    public static void main(String[] args) throws UnknownHostException, InterruptedException { 
    	
    	version = Double.parseDouble(args[0]);
        server_id = Integer.parseInt(args[1]);
    	peer_id = Integer.parseInt(args[2]);
    	mcAddr = args[3];
    	mcPort = Integer.parseInt(args[4]);
    	mdbAddr = args[5];
    	mdbPort = Integer.parseInt(args[6]);
    	mdrAddr = args[7];
    	mdrPort = Integer.parseInt(args[8]);
        
      
        try {
        	
			  Peer obj = new Peer();
			  LocateRegistry.createRegistry()
      	    RMI stub = (RMI) UnicastRemoteObject.exportObject(obj, 0);

      	    Registry registry = LocateRegistry.getRegistry();
      	    registry.bind(Integer.toString(peer_id), stub);

      	    System.err.println("Peer ready");
      	    
          
        } catch (Exception e) {
    	    System.err.println("Server exception: " + e.toString());
    	    e.printStackTrace();
    	}
    }

    public void operation(String operation, String file_path, int operator) { //operator is space for reclaim, rep_degree for back up

		if(operation == "BACKUP")
		{
			
		}
		else if(operation == "RESTORE")
		{
			
		}
		else if(operation == "DELETE")
		{
			
		}
		else if(operation == "RECLAIM")
		{
			
		}
		

        }
}