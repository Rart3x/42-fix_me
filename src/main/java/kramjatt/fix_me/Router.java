package kramjatt.fix_me;

import java.io.*;
import java.net.*;


public class Router {
    public int[]    clientIds;

    public void start() {
        new Thread(() -> listen(8000)).start();
        new Thread(() -> listen(9000)).start();
    }

    private static void listen(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println(Color.BLUE + "Market server started on port " + port + Color.RESET);

            while (true) {
                Socket client = server.accept();
                System.out.println(Color.BLUE + "Client connected: " + Color.RESET);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
