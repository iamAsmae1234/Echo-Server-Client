package de.fhac.ami.elakhal;
     //*****Programmbasis + Serververwaltung(Optinal)*****

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static DataOutputStream outToServer;
    private static DataInputStream inFromServer;
    private static boolean running;
    private static Socket clientSocket;
    private static Scanner scanner;
    private static Thread receiverThread;

    public Client(Socket socket) throws IOException {
        clientSocket = socket;
        try {
            outToServer = new DataOutputStream(socket.getOutputStream());
            inFromServer = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = true;
        scanner = new Scanner(System.in);
        receiverThread = null;
    }

    public void sendToServer(String message) {
        try {
            outToServer.writeUTF(message);
            outToServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
        receiverThread.interrupt();
        try {
            outToServer.close();
            inFromServer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String promptForNewMessage() {
        System.out.print("Entrez un message (ou \"\\exit\" pour quitter) : ");
        return scanner.nextLine();
    }

    public void startReceiver() {
        if (receiverThread == null) {
            receiverThread = new Thread(() -> {
                try {
                    while (running) {
                        String serverMessage = inFromServer.readUTF();
                        System.out.println("Message reçu du serveur : " + serverMessage);

                        if (serverMessage.equalsIgnoreCase("\\exit")) {
                            running = false;
                        }
                    }

                    stop();
                    System.out.println("Déconnexion du serveur.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiverThread.start();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 5678);
            Client client = new Client(clientSocket);
            client.startReceiver();

            System.out.println("Connecté au serveur");

            String message;
            do {
                message = promptForNewMessage();
                client.sendToServer(message);
            } while (!message.equals("\\exit"));

            System.out.println("Déconnexion du client.");

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
