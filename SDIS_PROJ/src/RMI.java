
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI extends Remote {
   void operation(String operation, String file_path, int rep_degree, double space) throws RemoteException; //operator is space for reclaim, rep_degree for back up
   void printMsg() throws RemoteException;  
}