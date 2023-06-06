package de.fhac.ami.elakhal;

import java.io.IOException;
import java.net.Socket;
import java.io.DataInputStream;

public class Receiver extends Thread  {
	private Socket socket;
    private Client client;

    public Receiver(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    public void processReceivedMessage(String message) {
    	// Traitez le message reçu du serveur
        if (message.startsWith("Commande1")) {
            // Effectuez des actions spécifiques pour la commande 1
            // ...
        } else if (message.startsWith("Commande2")) {
            // Effectuez des actions spécifiques pour la commande 2
            // ...
        } else {
            // Si le message ne correspond à aucune commande spécifique,
            // vous pouvez simplement l'afficher à l'écran
            System.out.println("Message du serveur : " + message);
        }
    }

    public String waitForNewMessage() throws IOException {
        DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
        return inFromServer.readUTF();
    }

    @Override
    public void run() {
    	try {
            while (client.isRunning()) {
                String serverMessage = waitForNewMessage();
                System.out.println("Message reçu du serveur : " + serverMessage);
                processReceivedMessage(serverMessage);
            }
            socket.close();
            System.out.println("Déconnexion du serveur.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
