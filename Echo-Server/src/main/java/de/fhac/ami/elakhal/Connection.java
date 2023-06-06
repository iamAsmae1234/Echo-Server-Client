package de.fhac.ami.elakhal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection extends Thread {
	
	private static int nextId = 1;
    private int id;
	private Socket clientSocket;
    private DataInputStream inFromClient;
    private DataOutputStream outToClient;
    
    public Connection(Socket clientSocket) throws IOException {
        this.id = nextId++;
        this.clientSocket = clientSocket;
        try {
            this.inFromClient = new DataInputStream(clientSocket.getInputStream());
            this.outToClient = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public long getId() {
        return id;
    }

    public void run() {
    	try {
            boolean isRunning = true;
            while (isRunning) {
                String clientMessage = inFromClient.readUTF();
                String echoMessage = "echo: " + clientMessage;
                System.out.println("Message reçu du client : " + clientMessage);

                outToClient.writeUTF(echoMessage);
                if (clientMessage.equalsIgnoreCase("\\exit")) {
                    isRunning = false;
                }
            }

            clientSocket.close();
            Server.getInstance().removeConnection(id);
            System.out.println("Déconnexion du client.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

    public void sendToClient(String message) {
        try {
        	outToClient.writeUTF(message);
        	outToClient.flush();
        } catch (IOException e) {
            // Gérer les exceptions appropriées
        	e.printStackTrace();
        }
    }
    
   

    public String waitForMessage() {
        try {
            return inFromClient.readUTF();
        } catch (IOException e) {
            // Gérer les exceptions appropriées
        	e.printStackTrace();
        }
        return null;
    }

    public void terminate() {
        try {
        	if (inFromClient != null) {
                inFromClient.close();
            }
            if (outToClient != null) {
            	outToClient.close();
            }
            clientSocket.close();
        } catch (IOException e) {
            // Gérer les exceptions appropriées
        	e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return clientSocket;
    }

    public void setSocket(Socket socket) {
        this.clientSocket = socket;
        try {
            this.inFromClient = new DataInputStream(socket.getInputStream());
            this.outToClient = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	

}
