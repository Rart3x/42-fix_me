package kramjatt.fix_me;

import java.io.*;
import java.net.*;
import java.util.*;


public class Market {
    public int[]    clientIds;

    public void start() {
        int port = 8001;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println(Color.BLUE + "Market server started on port " + port + Color.RESET);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(Color.BLUE + "Client connected: " + clientSocket.getInetAddress() + Color.RESET);

                new Thread(new MarketHandler(clientSocket, this.clientIds)).start();
            }
        } catch (IOException e) {
            System.err.println(Color.RED + "Error starting market server: " + e.getMessage() + Color.RESET);
        }
    }
}

class MarketHandler implements Runnable {
    private final int       clientId;
    private final Socket    clientSocket;

    public MarketHandler(Socket socket, int[] clientIds) {
        Random random = new Random();

        this.clientSocket = socket;

        int tmpClientId = random.nextInt(Utils.MAX_ID);
        int tmpId = 0;

        while (Arrays.stream(clientIds).anyMatch(id -> id == tmpClientId)) {
            tmpId = random.nextInt(Utils.MAX_ID);
        }

        this.clientId = tmpId;
        clientIds[this.clientId] = this.clientId;
    }

    @Override
    public void run() {
        String message;

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            out.println(Color.BLUE + "ID:" + this.clientId + " " + Color.RESET);

            while ((message = in.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                System.out.println(Color.BLUE + "Received from " + clientSocket.getInetAddress() + ": " + message + Color.RESET);

                if (message.equals("exit")) {
                    this.stop();
                }
            }
        } catch (IOException e) {
            System.err.println(Color.RED + "Error handling client: " + e.getMessage() + Color.RESET);
        }
    }

    public void stop() throws IOException {
        this.clientSocket.close();
        Thread.currentThread().interrupt();
    }
}

