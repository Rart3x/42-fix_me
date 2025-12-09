package kramjatt.fix_me;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Random;


public class Router {
    public int[]    clientIds;

    public void start() {
        new Thread(() -> listen(8000)).start();
        new Thread(() -> listen(8001)).start();
    }

    private int generateId() {
        Random random = new Random();
        int tmpId = 0;
        int finalTmpId = tmpId;

        do {
            tmpId = random.nextInt(1_000_000);
        } while (clientIds != null && Arrays.stream(clientIds)
                .filter(id -> id != 0)
                .anyMatch(id -> id == finalTmpId));

        if (clientIds != null) {
            for (int i = 0; i < clientIds.length; i++) {
                if (clientIds[i] == 0) {
                    clientIds[i] = tmpId;
                    break;
                }
            }
        }

        return tmpId;
    }

    private void listen(int port) {
        int clientId = 0;
        Socket client = null;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println(Color.BLUE + "Router server started on port " + port + Color.RESET);

            while (true) {
                client = server.accept();
                clientId = generateId();

                System.out.println(Color.BLUE + "\nClient " + clientId + " connected: " + Color.RESET);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
