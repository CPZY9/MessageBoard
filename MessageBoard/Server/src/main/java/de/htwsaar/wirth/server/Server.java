package de.htwsaar.wirth.server;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.server.service.Services;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.security.Provider.Service;
import java.rmi.registry.LocateRegistry;

/**
 * A Server binds
 */
public class Server {
	
    private static final String BIND_KEY = "server";
    
    private int parentPort;
    private int localPort;
    private String parentHost;
    private MessageBoardImpl messageBoard;
    

    public Server(String groupName, int localPort) throws RemoteException, AlreadyBoundException {
        this.localPort = localPort;
        messageBoard = new MessageBoardImpl(groupName);
        createRegistry();
        checkGroupLeader();
    }

    private void checkGroupLeader() {
		// gibts schon einen gl
    	if (Services.getInstance().getUserService().existsGroupLeader()) {
    		
    		
    		
    	}
    	// wenn nicht dann erstelle neuen user
    	
    	// mit gl-Flag
    	
    	// und speichere in db
	}

	public Server(String groupName, int localPort, String parentHost, int parentPort) throws RemoteException, NotBoundException, AlreadyBoundException {
        this(groupName, localPort);
        this.parentHost = parentHost;
        this.parentPort = parentPort;
        bindToParent();
    }


    private void createRegistry() throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(localPort);
        registry.bind(BIND_KEY, messageBoard);
    }

    public void bindToParent() throws RemoteException, NotBoundException {
        Registry parentRegistry = LocateRegistry.getRegistry(parentHost, parentPort);
        ParentServer parent = (ParentServer) parentRegistry.lookup(BIND_KEY);
        parent.registerServer(messageBoard);
        messageBoard.setParent(parent);
    }

}
