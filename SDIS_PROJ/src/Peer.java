
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;




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
	
	StorageSystem storage;


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
		
		StorageSystem storage = new StorageSystem();

		System.out.println("teste");
		try {

			Peer obj = new Peer();

			RMI stub = (RMI) UnicastRemoteObject.exportObject(obj, 0);  

			// Binding the remote object (stub) in the registry 
			Registry registry = LocateRegistry.getRegistry(); 
			registry.bind("Hello", stub); 

			//registry.bind(args[2], stub);  
			System.err.println("Server ready"); 




		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

	public void operation(String operation, String file_path, int rep_degree, double space) { //operator is space for reclaim, rep_degree for back up

		if(operation == "BACKUP")
		{
			new Thread(new Backup(file_path,rep_degree));
		}
		else if(operation == "RESTORE")
		{
			new Thread(new Restore(file_path));
		}
		else if(operation == "DELETE")
		{
			new Thread(new Delete(file_path));
		}
		else if(operation == "RECLAIM")
		{
			new Thread(new Reclaim(space));
		}


	}
	

	public void printMsg() {  
		System.out.println("This is an example RMI program");  
	}

	/*@Override
	public void backup(String file_path, int ReplicationDeg) {
		new Thread(new protocols.Backup(file_path, ReplicationDeg));
	}

	@Override
	public void restore(String file_path) {
		new Thread(new protocols.Restore(file_path));
	}

	@Override
	public void delete(String file_path) {
		new Thread(new protocols.Delete(file_path));
	}

	@Override
	public void reclaim(double space) {
		new Thread(new protocols.Reclaim(space));
	}*/

	

	

	

	 
}