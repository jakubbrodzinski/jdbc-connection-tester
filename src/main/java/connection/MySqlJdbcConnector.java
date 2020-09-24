package connection;

import java.sql.Driver;
import java.util.Map;
import java.util.Properties;

public class MySqlJdbcConnector extends JdbcConnector {

    MySqlJdbcConnector(Map<JdbcProperty, String> propertiesMap, Class<? extends Driver> jdbcDriver) {
        super(propertiesMap, jdbcDriver);
    }

    @Override
    protected final String getJdbcUrl(Map<JdbcProperty, String> propertiesMap) {
        return null;
    }

    @Override
    protected final Properties getProperties() {
        return null;
    }
}
