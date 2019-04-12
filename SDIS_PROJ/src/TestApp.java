
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


 
 
public class TestApp {
     
	private static String peer_ap;
	private static String sub_protocol;
	private static String file_path;
	private static double disk_space = 0;
	private static int ReplicationDeg = 0;
	
    public static void main(String[] args) throws UnknownHostException { //<peer_ap> <sub_protocol> <opnd_1> <opnd_2> 
    	
    	peer_ap = args[0];
        sub_protocol = args[1];
        
        for(String item : args) {
        	
        	System.out.println("Arg: "+item);
        }
     
        
        
        try {       	
            
            // Getting the registry                        
            Registry registry = LocateRegistry.getRegistry(null); 
       
            // Looking up the registry for the remote object 
            RMI stub = (RMI) registry.lookup(peer_ap); 
       
            // Calling the remote method using the obtained object 
            //stub.printMsg();
            //stub.operation(sub_protocol, file_path, ReplicationDeg, disk_space);
        	System.out.println("PROTOCOLO "+sub_protocol);
        	
            if(sub_protocol.equals("BACKUP"))
            {
            	System.out.println("in testapp back up");
            	if(args.length != 4) {
            		System.out.println("Invalid arguments for backup Protocol");
            		System.out.println("TestApp backup <file_path> <replication_degree>");
            		return;
            	}
            	ReplicationDeg = Integer.parseInt(args[3]);
                file_path = args[2];
                
                stub.operation(sub_protocol, file_path, ReplicationDeg, disk_space);
            }
            else if(sub_protocol.equals("RESTORE"))
            {
            	if(args.length != 3) {
            		System.out.println("Invalid arguments for restore Protocol");
            		System.out.println("TestApp restore <file_path>");
            		return;
            	}
            	
                file_path = args[2];
                
                stub.operation(sub_protocol, file_path, 0, 0);
            }
            else if(sub_protocol.equals("DELETE"))
            {
            	if(args.length != 3) {
            		System.out.println("Invalid arguments for delete Protocol");
            		System.out.println("TestApp backup <file_path> ");
            		return;
            	}
            	
                file_path = args[2];
                
                stub.operation(sub_protocol, file_path, ReplicationDeg, disk_space);
            }
            else if(sub_protocol.equals("RECLAIM"))
            {
            	if(args.length != 3) {
            		System.out.println("Invalid arguments for reclaim Protocol");
            		System.out.println("TestApp reclaim <space>");
            		return;
            	}
            	
                disk_space = Double.parseDouble(args[2]);
                
                stub.operation(sub_protocol, file_path, ReplicationDeg, disk_space);
            }
            
            
                 
           } catch (Exception e) {
           	System.err.println("TestApp exception: " + e.toString());
          	    e.printStackTrace();
           }
        
        
        

       

        
    }
}