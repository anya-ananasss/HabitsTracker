import controllers.UserController;
import liquibase.Liquibase;
import repositories.UserRepository;
import serviceClasses.Config;
import services.UserService;
import liquibase.exception.LiquibaseException;

import java.sql.Connection;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException, LiquibaseException {
        Config config = new Config();
        Object[] connections = config.establishConnections();
        Connection con = (Connection) connections[0];
        Liquibase liquibase = (Liquibase) connections[1];
        try {
            if (con != null) {
                UserRepository userRepository = new UserRepository(con);
                UserService userService = new UserService(userRepository);
                UserController userController = new UserController(userService);

                userController.showGreetingScreen();
            } else {
                System.out.println("Ошибка! Не удалось подключиться к базе данных!");
            }
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
                if (liquibase != null) {
                    liquibase.close();
                }
            } catch (SQLException | LiquibaseException e) {
                System.out.println("Ошибка! " + e.getMessage());
            }
        }

    }
}