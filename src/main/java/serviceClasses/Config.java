package serviceClasses;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/**
 * Класс для установки сессии соединения с базой данных и Liquibase
 * <p>
 * * @author Gureva Anna
 * * @version 1.0
 * * @since 19.10.2024
 * </p>
 */
public class Config {

    private final Properties property = new Properties();

    /**
     * Подключается к базе данных в соответствии с данными, прописанными в config.properties,
     * @return массив Object, где первый элемент - соединение с базой данных, второй - экземпляр класса Liquibase,
     * выполняющий миграции схем баз данных
     */
    public Object [] establishConnections() {
        try {
            FileInputStream inputStream = new FileInputStream("src/main/resources/config.properties");
            property.load(inputStream);

            String username = property.getProperty("username");
            String password = property.getProperty("password");
            String url = property.getProperty("url");

            Connection con = DriverManager.getConnection(url, username, password);

            String schemaName = "app";

            con.setAutoCommit(false);
            con.createStatement().execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
            con.createStatement().execute("SET search_path TO " + schemaName);



            JdbcConnection jdbcConnection = new JdbcConnection(con);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);
            Liquibase liquibase = new Liquibase("db-changelog/main-changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();

            con.commit();

            return new Object[] {con, liquibase};

        } catch (IOException | SQLException | LiquibaseException e) {
            System.err.println("Ошибка!" + e);
        }
        return null;
    }
}
