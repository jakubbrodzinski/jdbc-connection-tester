import cli.JdbcConnectionTesterCLI;
import connection.JdbcConnector;
import org.apache.commons.cli.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException, ClassNotFoundException {
        JdbcConnectionTesterCLI jdbcConnectionTesterCLI = new JdbcConnectionTesterCLI();
        if (args.length == 0) {
            jdbcConnectionTesterCLI.printHelpFormattedMessage();
        } else {
            JdbcConnector jdbcConnector = jdbcConnectionTesterCLI.parseAndCreateConnector(args);
        }
    }
}
