
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
	private static double disk_space;
	private static int ReplicationDeg;
	
    public static void main(String[] args) throws UnknownHostException { //<peer_ap> <sub_protocol> <opnd_1> <opnd_2> 
    	
    	peer_ap = args[0];
        sub_protocol = args[1].toUpperCase();
        
        System.out.println(args[0]);
        
        
        try {       	
            
            // Getting the registry                        
            Registry registry = LocateRegistry.getRegistry(null); 
       
            // Looking up the registry for the remote object 
            RMI stub = (RMI) registry.lookup("Hello"); 
       
            // Calling the remote method using the obtained object 
            stub.printMsg();
            //stub.operation(sub_protocol, file_path, replication_degree, disk_space);
            
            if(sub_protocol == "BACKUP")
            {
            	if(args.length != 4) {
            		System.out.println("Invalid arguments for backup Protocol");
            		System.out.println("TestApp backup <file_path> <replication_degree>");
            		return;
            	}
            	ReplicationDeg = Integer.parseInt(args[3]);
                file_path = args[2];
                
                stub.operation(sub_protocol, file_path, ReplicationDeg, disk_space);
            }
            else if(sub_protocol == "RESTORE")
            {
            	if(args.length != 3) {
            		System.out.println("Invalid arguments for restore Protocol");
            		System.out.println("TestApp restore <file_path>");
            		return;
            	}
            	
                file_path = args[2];
                
                stub.operation(sub_protocol, file_path, ReplicationDeg, disk_space);
            }
            else if(sub_protocol == "DELETE")
            {
            	if(args.length != 3) {
            		System.out.println("Invalid arguments for delete Protocol");
            		System.out.println("TestApp backup <file_path> ");
            		return;
            	}
            	
                file_path = args[2];
                
                stub.operation(sub_protocol, file_path, ReplicationDeg, disk_space);
            }
            else if(sub_protocol == "RECLAIM")
            {
            	if(args.length != 3) {
            		System.out.println("Invalid arguments for reclaim Protocol");
            		System.out.println("TestApp reclaim <space>");
            		return;
            	}
            	
                disk_space = Double.parseDouble(args[2]);
                
                stub.operation(sub_protocol, file_path, ReplicationDeg, disk_space);
            }
            
            
                 
                 System.out.println(args[0]);
           } catch (Exception e) {
           	System.err.println("TestApp exception: " + e.toString());
          	    e.printStackTrace();
           }
        
        
        
  
               
        System.out.println(args[0]);
     

       

        
    }
}