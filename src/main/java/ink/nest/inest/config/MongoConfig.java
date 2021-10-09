package ink.nest.inest.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import ink.nest.inest.properties.MongoProperties;
import org.bson.UuidRepresentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableMongoRepositories(basePackages = "ink.nest.inest.repository")
public class MongoConfig {
    private final MongoProperties mongoProperties;

    public MongoConfig(MongoProperties mongoProperties) {
        this.mongoProperties = mongoProperties;
    }

    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString(
                String.format("mongodb://%s:%s@%s:%s/%s",
                        URLEncoder.encode(mongoProperties.getUsername(), StandardCharsets.UTF_8),
                        URLEncoder.encode(mongoProperties.getPassword(), StandardCharsets.UTF_8),
                        mongoProperties.getHost(),
                        mongoProperties.getPort(),
                        mongoProperties.getDatabase()
                )
        );

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), mongoProperties.getDatabase());
    }
}
