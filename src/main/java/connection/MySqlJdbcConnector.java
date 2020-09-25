package connection;

import models.JdbcProperty;
import lombok.ToString;

import java.sql.Driver;
import java.util.Map;
import java.util.Properties;

@ToString(onlyExplicitlyIncluded = true,callSuper = true)
public class MySqlJdbcConnector extends JdbcConnector {
    private static final String JDBC_URL_FORMAT="jdbc:mysql://%s:%s/%s?user=%s&password=%s";
    private final String userName;
    private final String password;

    MySqlJdbcConnector(Map<JdbcProperty, String> propertiesMap, Class<? extends Driver> jdbcDriver) {
        super(propertiesMap, jdbcDriver);
        this.userName = propertiesMap.get(JdbcProperty.USERNAME);
        this.password = propertiesMap.get(JdbcProperty.PASSWORD);
    }

    @Override
    protected final String getJdbcUrl(Map<JdbcProperty, String> propertiesMap) {
        return String.format(JDBC_URL_FORMAT,propertiesMap.get(JdbcProperty.HOST), propertiesMap.get(JdbcProperty.PORT),
                propertiesMap.get(JdbcProperty.DBNAME),this.userName,this.password);
    }

    @Override
    protected final Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("user",userName);
        properties.setProperty("password",password);
        return properties;
    }
}
