package models;

import connection.JdbcConnector;
import connection.MariaDbJdbcConnector;
import connection.MySqlJdbcConnector;
import connection.OracleJdbcConnector;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum DbmsEngine {
    MARIADB("mariadb", "org.mariadb.jdbc.Driver", MariaDbJdbcConnector.class),
    MYSQL("mysql", "com.mysql.jdbc.Driver", MySqlJdbcConnector.class),
    ORACLE("oracle", "oracle.jdbc.OracleDriver", OracleJdbcConnector.class);

    private final String key;
    private final String defaultDriverClassName;
    private final Class<? extends JdbcConnector> connectionClazz;

    public static Optional<DbmsEngine> fromKey(String key) {
        return Arrays.stream(DbmsEngine.values())
                .filter(dbmsEngine -> dbmsEngine.getKey().equals(key))
                .findFirst();
    }
}
