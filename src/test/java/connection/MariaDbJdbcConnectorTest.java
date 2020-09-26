package connection;

import models.JdbcProperty;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.Driver;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class MariaDbJdbcConnectorTest {

    @Test
    void getJdbcUrl() {
        final var jdbcPropertiesMap = Map.of(JdbcProperty.DBNAME,"db",JdbcProperty.USERNAME,"someUser",
                JdbcProperty.PASSWORD,"pw",JdbcProperty.HOST,"localhost", JdbcProperty.PORT,"3306");

        final String jdbcUrl = new MariaDbJdbcConnector(jdbcPropertiesMap, Driver.class)
                .getJdbcUrl(jdbcPropertiesMap);

        assertThat(jdbcUrl)
                .isEqualTo("jdbc:mariadb://localhost:3306/db?user=someUser&password=pw");
    }

    @Nested
    class IntegrationTests {
        @Container
        private MariaDBContainer mariaDBContainer = new MariaDBContainer<>()
                .withDatabaseName("db_test")
                .withUsername("someUser")
                .withPassword("pw");

        @Test
        void getConnectionTest() throws SQLException {
            final var jdbcPropertiesMap = Map.of(JdbcProperty.DBNAME, mariaDBContainer.getDatabaseName(),
                    JdbcProperty.USERNAME, mariaDBContainer.getUsername(), JdbcProperty.PASSWORD, mariaDBContainer.getPassword(),
                    JdbcProperty.HOST, "localhost", JdbcProperty.PORT, mariaDBContainer.getMappedPort(3306).toString());

            final var jdbcConnector = new MariaDbJdbcConnector(jdbcPropertiesMap, Driver.class);

            assertThat(jdbcConnector.getConnection())
                    .isNotNull();
        }
    }
}