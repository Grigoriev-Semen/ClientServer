import Util.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Properties;

public class Client {
    private final static Logger log = LoggerFactory.getLogger(Client.class);
    private Connection connection;
    private static final int port;
    private static final String serverAddress;

    static {
        Properties property = new Properties();
        try {
            property.load(new FileInputStream("src/main/resources/settings.properties"));
            port = Integer.parseInt(property.getProperty("port"));
            serverAddress = property.getProperty("server_address");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Client().run();
    }

    public void run() {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            connection = new Connection(new Socket(serverAddress, port));
            SocketThread socketThread = new SocketThread();
            socketThread.setDaemon(true);
            socketThread.start();

            while (true) {
                String text = br.readLine();
                if (text.equalsIgnoreCase("exit")){
                    break;
                }
                log.info(text);
                connection.send(text);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public class SocketThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    String message = connection.receive();
                    log.info("Server: " + message);
                    System.out.println(message);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}