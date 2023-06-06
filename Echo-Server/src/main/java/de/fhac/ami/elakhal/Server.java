package de.fhac.ami.elakhal;
		//*****Programmbasis + Serververwaltung(Optinal)*****

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private int port;
    private List<Connection> connections;
    private ServerSocket serverSocket;
    private boolean running;
    private static Server instance;
    private static Lock lock = new ReentrantLock();

    private Server() {
        this.port = loadPortFromConfig("config.txt");
        this.connections = new ArrayList<>();
        this.running = false;
    }

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void broadcast(String message) {
        lock.lock();
        try {
            for (Connection client : connections) {
                client.sendToClient(message);
            }
        } finally {
            lock.unlock();
        }
    }

    private int loadPortFromConfig(String path) {
        // Code pour charger le port à partir d'un fichier de configuration
        // ...
        return 5678; // Valeur par défaut
    }

    public void removeConnection(int id) {
        lock.lock();
        try {
            connections.removeIf(connection -> connection.getId() == id);
        } finally {
            lock.unlock();
        }
    }

    public void start() {
        if (!running) {
            running = true;
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Le serveur démarre.");
                System.out.println("Le serveur attend des connexions.");

                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Nouveau client connecté : " + clientSocket.getInetAddress().getHostAddress());

                    Connection connection = new Connection(clientSocket);
                    connections.add(connection);
                    connection.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (running) {
            running = false;
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connections.clear();
        }
    }

    public static void main(String[] args) {
        Server server = Server.getInstance();
        server.start();
    }
}
