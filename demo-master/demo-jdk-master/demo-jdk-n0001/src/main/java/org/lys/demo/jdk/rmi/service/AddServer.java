package org.lys.demo.jdk.rmi.service;


import java.rmi.Remote;
import java.rmi.RemoteException;


public interface AddServer extends Remote { 
	
	public int AddNumbers(int firstnumber,int secondnumber) throws RemoteException; 
}

