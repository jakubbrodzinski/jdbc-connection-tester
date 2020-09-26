package connection;

import models.JdbcProperty;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.Driver;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class OracleJdbcConnectorTest {

    @Test
    void getJdbcUrl() {
        final var jdbcPropertiesMap = Map.of(JdbcProperty.SID, "ORCL", JdbcProperty.USERNAME, "someUser",
                JdbcProperty.PASSWORD, "pw", JdbcProperty.HOST, "localhost", JdbcProperty.PORT, "1521");

        final String jdbcUrl = new OracleJdbcConnector(jdbcPropertiesMap, Driver.class)
                .getJdbcUrl(jdbcPropertiesMap);

        assertThat(jdbcUrl)
                .isEqualTo("jdbc:oracle:thin:someUser/pw@localhost:1521:ORCL");
    }

    @Nested
    class IntegrationTests {
        @Container
        private OracleContainer oracleContainer = new OracleContainer("wnameless/oracle-xe-11g-r2")
                .withUsername("system")
                .withPassword("oracle");

        @Test
        void getConnectionTest() throws SQLException {
            final var jdbcPropertiesMap = Map.of(JdbcProperty.SID, oracleContainer.getSid(),
                    JdbcProperty.USERNAME, oracleContainer.getUsername(), JdbcProperty.PASSWORD, oracleContainer.getPassword(),
                    JdbcProperty.HOST, "localhost", JdbcProperty.PORT, oracleContainer.getMappedPort(1521).toString());

            final var jdbcConnector = new OracleJdbcConnector(jdbcPropertiesMap, oracle.jdbc.OracleDriver.class);

            assertThat(jdbcConnector.getConnection())
                    .isNotNull();
        }
    }
}