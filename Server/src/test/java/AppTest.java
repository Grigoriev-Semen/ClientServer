import Util.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AppTest {
    Server server;
    int port = 8090;
    String host = "localhost";

    @BeforeEach
    void init() {
        server = new Server(port);
    }

    @Test
    void start() throws IOException {
        //given
        ServerSocket serverSocket = new ServerSocket(port);
        //when
        server.start();
        Socket clientSocket = new Socket(host, port);
        // then
        assertFalse(clientSocket.isClosed());
        clientSocket.close();
    }

    @Test
    public void receive() throws Exception {
        String testMessage = "test";
        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.receive()).thenReturn(testMessage);
        assertEquals(testMessage, connection.receive());
    }
}
