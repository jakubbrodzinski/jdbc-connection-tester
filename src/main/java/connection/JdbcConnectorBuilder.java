package connection;

import models.DbmsEngine;
import models.JdbcProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JdbcConnectorBuilder<T extends JdbcConnector> {
    private Class<? extends JdbcConnector> connectorClazz;
    private String driverClassName;
    private final Map<JdbcProperty, String> propertiesMap;

    private JdbcConnectorBuilder(Map<JdbcProperty, String> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

    public static JdbcConnectorBuilder<?> empty() {
        return new JdbcConnectorBuilder<>(new HashMap<>());
    }

    public JdbcConnectorBuilder<T> with(JdbcProperty jdbcProperty, String propertyValue) {
        propertiesMap.put(jdbcProperty, propertyValue);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <D extends JdbcConnector> JdbcConnectorBuilder<D> with(Class<D> jdbcConnectorClazz) {
        this.connectorClazz = jdbcConnectorClazz;
        return (JdbcConnectorBuilder<D>) this;
    }

    public JdbcConnectorBuilder<T> withDriver(String driver) {
        this.driverClassName = driver;
        return this;
    }

    @SuppressWarnings("unchecked")
    public T build() throws ClassNotFoundException {
        try {
            Constructor<T> mapConstructor = (Constructor<T>) connectorClazz.getConstructor(Map.class, Class.class);
            return mapConstructor.newInstance(this.propertiesMap, getDriverClass());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Implementation without required constructor", e);
        }
    }

    private Class<?> getDriverClass() throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (this.driverClassName == null) {
            return Arrays.stream(DbmsEngine.values())
                    .map(DbmsEngine::getConnectionClazz)
                    .filter(connectionClazz -> connectionClazz.equals(this.connectorClazz))
                    .findFirst()
                    .orElseThrow(ClassNotFoundException::new);
        } else {
            return classLoader.loadClass(this.driverClassName);
        }
    }
}
