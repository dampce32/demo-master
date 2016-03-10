package org.lys.demo.jdk.rmi.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AddServerImpl extends UnicastRemoteObject implements AddServer {

	private static final long serialVersionUID = 1L;

	public AddServerImpl() throws RemoteException {
		super();
	}

	public int AddNumbers(int firstnumber, int secondnumber) throws RemoteException {
		return firstnumber + secondnumber;
	}
}
