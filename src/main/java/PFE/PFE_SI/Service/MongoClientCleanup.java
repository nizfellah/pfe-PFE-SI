package PFE.PFE_SI.Service;

import com.mongodb.client.MongoClient;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MongoClientCleanup {

    private final MongoClient mongoClient;

    @PreDestroy
    public void cleanUp() {
        System.out.println("Closing MongoClient...");
        mongoClient.close();
    }
}
