package connection;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public abstract class JdbcConnector {
    private final String jdbcUrl;
    private final Class<? extends Driver> driverClazz;

    protected JdbcConnector(Map<JdbcProperty, String> propertiesMap, Class<? extends Driver> driverClazz) {
        this.jdbcUrl = getJdbcUrl(propertiesMap);
        this.driverClazz = driverClazz;
    }

    protected abstract String getJdbcUrl(Map<JdbcProperty, String> propertiesMap);

    protected abstract Properties getProperties();

    public final Connection getConnection() throws SQLException {
        try {
            Driver jdbcDriver = driverClazz.getConstructor().newInstance();
            return jdbcDriver.connect(this.jdbcUrl, getProperties());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Invalid driver class...", e);
        }
    }
}
