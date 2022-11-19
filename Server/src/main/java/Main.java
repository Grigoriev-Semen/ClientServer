import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        int port = 0;
        Properties property = new Properties();

        try {
            property.load(new FileInputStream("src/main/resources/settings.properties"));
            port = Integer.parseInt(property.getProperty("port"));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        new Server(port).start();
    }
}
