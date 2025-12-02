package kramjatt.fix_me;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Random;


public class Broker {
    public int[]    clientIds;

    public void start() {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println(Color.BLUE + "Broker server started on port " + port + Color.RESET);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(Color.BLUE + "Client connected: " + clientSocket.getInetAddress() + Color.RESET);

                new Thread(new BrokerHandler(clientSocket, this.clientIds)).start();
            }
        } catch (IOException e) {
            System.err.println(Color.RED + "Error starting broker server: " + e.getMessage() + Color.RESET);
        }
    }
}

class BrokerHandler implements Runnable {
    private final int       clientId;
    private final Socket    clientSocket;

    public BrokerHandler(Socket socket, int[] clientIds) {
        Random random = new Random();

        this.clientSocket = socket;

        int tmpId = 0;
        int tmpClientId = random.nextInt(999999);

        while (Arrays.stream(clientIds).anyMatch(id -> id == tmpClientId)) {
            tmpId = random.nextInt(999999);
        }

        this.clientId = tmpId;
        clientIds[this.clientId] = this.clientId;
    }

    @Override
    public void run() {
        String message;

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            out.println(Color.BLUE + this.clientId + " " + Color.RESET);
            while ((message = in.readLine()) != null) {
                System.out.println(Color.BLUE + "Received from " + clientSocket.getInetAddress() + ": " + message + Color.RESET);
            }
        } catch (IOException e) {
            System.err.println(Color.RED + "Error handling client: " + e.getMessage() + Color.RESET);
        }
    }
}
