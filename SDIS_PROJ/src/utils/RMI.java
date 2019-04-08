package utils;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI extends Remote {
    String sayHello() throws RemoteException;
}