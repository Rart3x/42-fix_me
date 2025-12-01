package kramjatt.fix_me;

import java.io.*;
import java.net.*;


public class Server {
    static void main(String[] args) {
        int port = 1231;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println(Color.BLUE + "Server started on port " + port + Color.RESET);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(Color.BLUE + "Client connected: " + clientSocket.getInetAddress() + Color.RESET);

                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println(Color.RED + "Error starting server: " + e.getMessage() + Color.RESET);
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        String message;

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            while ((message = in.readLine()) != null) {
                System.out.println(Color.BLUE + "Received from " + clientSocket.getInetAddress() + ": " + message + Color.RESET);
            }
        } catch (IOException e) {
            System.err.println(Color.RED + "Error handling client: " + e.getMessage() + Color.RESET);
        }
    }
}
