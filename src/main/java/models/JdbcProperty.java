package models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum JdbcProperty {
    USERNAME("USERNAME","u", true, "Username"),
    PASSWORD("PASSWORD","pw", true, "Password"),
    SID("SID","sid", false, "Oracle SID"),
    HOST("HOST","h", true, "Hostname"),
    PORT("PORT","p", true, "Port"),
    DBNAME("DBNAME","db", false, "Database name");

    private final String name;
    private final String shortOpt;
    private final boolean required;
    private final String description;

    public static Optional<JdbcProperty> fromName(String name){
        return Arrays.stream(JdbcProperty.values())
                .filter(property -> property.getName().equals(name))
                .findAny();
    }
}
