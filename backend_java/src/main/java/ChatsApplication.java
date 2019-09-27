import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.chats.chats"})
public class ChatsApplication {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");

        SpringApplication.run(ChatsApplication.class, args);
    }
}