import Util.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private final static Logger log = LoggerFactory.getLogger(Server.class);
    private final int port;
    private static final Map<String, Connection> mapConnection = new ConcurrentHashMap<>();

    public Server(int port) {
        this.port = port;
    }

    public void start(){
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Server startup");

            while (true) {
                Socket socket = serverSocket.accept();
                log.info("New connection accepted");
                new HandlerUser(socket).start();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    static class HandlerUser extends Thread {
        private final Socket socket;

        public HandlerUser(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String name = "null";
            try (Connection connection = new Connection(socket)) {

                connection.send("Enter your name.");
                name = connection.receive();
                mapConnection.put(name, connection);
                log.info("Connected - {}", name);
                sendMessageAll("Connected - " + name);

                String message;
                while (true) {
                    message = name + " --> " + connection.receive();
                    log.info(message);
                    sendMessageAll(message);
                }

            } catch (Exception e) {
                if (e.getMessage().equals("Connection reset")) {
                    log.info("{} - left the chat", name);
                    sendMessageAll(name + " - left the chat");
                    mapConnection.remove(name);
                } else {
                    log.error(e.getMessage());
                }
            }
        }

        private static void sendMessageAll(String message) {
            mapConnection.forEach((k, v) -> {
                try {
                    v.send(message);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public int getSizeConnection(){
        return mapConnection.size();
    }
}

