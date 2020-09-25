package cli;

import connection.JdbcConnector;
import connection.JdbcConnectorBuilder;
import models.DbmsEngine;
import models.JdbcProperty;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class JdbcConnectionTesterCLI {
    private static final String DBMS_ARG = "ENGINE";

    private Optional<Options> cliOptions = Optional.empty();

    private Options getCliOptions() {
        this.cliOptions = cliOptions.or(() -> Optional.of(createOptions()));
        return this.cliOptions.get();
    }

    private Options createOptions() {
        String supportedEngines = Arrays.stream(DbmsEngine.values())
                .map(DbmsEngine::getKey)
                .collect(Collectors.joining("|"));
        return OptionsFactory.create(DBMS_ARG, "DBMS type " + supportedEngines);
    }

    public void printHelpFormattedMessage() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("jdbc-connection-tester", getCliOptions());
    }

    public JdbcConnector parseAndCreateConnector(String[] args) throws ParseException, ClassNotFoundException {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(getCliOptions(), args);
        return createConnector(line);
    }

    private JdbcConnector createConnector(CommandLine commandLine) throws ClassNotFoundException {
        JdbcConnectorBuilder<?> connectorBuilder = JdbcConnectorBuilder.empty();
        Arrays.stream(commandLine.getOptions()).forEach(o ->{
            JdbcProperty.fromName(o.getArgName())
                    .ifPresent(property -> connectorBuilder.with(property,o.getValue()));
        });
        DbmsEngine.fromKey(commandLine.getOptionValue(DBMS_ARG))
                .ifPresent(engine -> {
                    connectorBuilder.withDriver(engine.getDefaultDriverClassName());
                    connectorBuilder.with(engine.getConnectionClazz());
                });

        return connectorBuilder.build();
    }
}
