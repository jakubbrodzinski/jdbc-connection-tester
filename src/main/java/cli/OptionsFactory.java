package cli;

import models.JdbcProperty;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Arrays;

public class OptionsFactory {
    public static Options create(String engineArgName, String engineArgDescription) {
        return Arrays.stream(JdbcProperty.values())
                .map(OptionsFactory::toOption)
                .reduce(new Options(), Options::addOption, (ops1, ops2) -> {
                    ops2.getOptions().forEach(ops1::addOption);
                    return ops2;
                }).addOption(dbmsEngineOption(engineArgName,engineArgDescription));
    }

    private static Option toOption(JdbcProperty jdbcProperty) {
        return Option.builder(jdbcProperty.getShortOpt())
                .argName(jdbcProperty.name())
                .desc(jdbcProperty.getDescription())
                .required(jdbcProperty.isRequired())
                .hasArg(true)
                .build();
    }

    private static Option dbmsEngineOption(String argName, String description) {
        return Option.builder("e")
                .argName(argName)
                .desc(description)
                .hasArg(true)
                .required(true)
                .build();
    }
}
