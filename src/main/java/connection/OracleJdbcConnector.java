package connection;

import models.JdbcProperty;
import lombok.ToString;

import java.sql.Driver;
import java.util.Map;
import java.util.Properties;

@ToString(onlyExplicitlyIncluded = true,callSuper = true)
public class OracleJdbcConnector extends JdbcConnector {
    private static final String JDBC_URL_FORMAT = "jdbc:oracle:thin:%s/%s@%s:%s:%s";
    private final String userName;
    private final String password;

    OracleJdbcConnector(Map<JdbcProperty, String> propertiesMap, Class<? extends Driver> jdbcDriver) {
        super(propertiesMap, jdbcDriver);
        this.userName = propertiesMap.get(JdbcProperty.USERNAME);
        this.password = propertiesMap.get(JdbcProperty.PASSWORD);
    }

    @Override
    protected final String getJdbcUrl(Map<JdbcProperty, String> propertiesMap) {
        return String.format(JDBC_URL_FORMAT, this.userName, this.password, propertiesMap.get(JdbcProperty.HOST), propertiesMap.get(JdbcProperty.PORT),
                propertiesMap.get(JdbcProperty.SID), propertiesMap.get(JdbcProperty.DBNAME));
    }

    @Override
    protected final Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("user", userName);
        properties.setProperty("password", password);
        return properties;
    }
}
