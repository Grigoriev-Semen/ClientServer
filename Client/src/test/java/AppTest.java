import Util.Connection;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    @Test
    public void receive() throws Exception {
        String testMessage = "test";
        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.receive()).thenReturn(testMessage);
        assertEquals(testMessage, connection.receive());
    }
}
