package connection;

import com.mysql.cj.jdbc.Driver;
import models.JdbcProperty;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class MySqlJdbcConnectorTest {

    @Test
    void getJdbcUrl() {
        final var jdbcPropertiesMap = Map.of(JdbcProperty.DBNAME, "db", JdbcProperty.USERNAME, "someUser",
                JdbcProperty.PASSWORD, "pw", JdbcProperty.HOST, "localhost", JdbcProperty.PORT, "3306");

        final String jdbcUrl = new MySqlJdbcConnector(jdbcPropertiesMap, Driver.class)
                .getJdbcUrl(jdbcPropertiesMap);

        assertThat(jdbcUrl)
                .isEqualTo("jdbc:mysql://localhost:3306/db?user=someUser&password=pw");
    }

    @Nested
    class IntegrationTests {
        @Container
        private MySQLContainer mySQLContainer = new MySQLContainer()
                .withDatabaseName("db_test")
                .withUsername("someUser")
                .withPassword("pw");

        @Test
        void getConnectionTest() throws SQLException {
            final var jdbcPropertiesMap = Map.of(JdbcProperty.DBNAME, mySQLContainer.getDatabaseName(),
                    JdbcProperty.USERNAME, mySQLContainer.getUsername(), JdbcProperty.PASSWORD, mySQLContainer.getPassword(),
                    JdbcProperty.HOST, "localhost", JdbcProperty.PORT, mySQLContainer.getMappedPort(3306).toString());

            final var jdbcConnector = new MySqlJdbcConnector(jdbcPropertiesMap, Driver.class);

            assertThat(jdbcConnector.getConnection())
                    .isNotNull();
        }
    }
}