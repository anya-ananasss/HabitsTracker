package UserTests.serviceTests;


import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import serviceClasses.Config;

import models.User;
import repositories.UserRepository;
import services.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.*;

@DisplayName("Тесты для сервиса, работающего с пользователем")
public class UserServiceTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    Connection connection;
    UserRepository userRepository;
    UserService service;
    @BeforeEach
    void setUp() {
        postgres.start();
        Config config = new Config();
        Object[] connections = config.establishConnections();
        connection = (Connection) connections[0];
        userRepository = new UserRepository(connection);
        service = new UserService(userRepository);
    }

    @AfterEach
    void clear() throws SQLException {
        connection.rollback();
        connection.close();
        postgres.stop();
    }



    @Test
    @DisplayName("Получение всех пользователей")
    public void getAllUsers() {
        User user1 = new User("admin", "admin@admin.ru", "$2a$10$lD8OC2fG77x4rVgNZ5nUBekUJ5HqP8U02TiPd3uw5HVuyBqdfnMKG", 0);
        User user2 = new User("anya", "anya@ya.ru", "$2a$10$Lz/N/PPqZTdHgRQC6Wf6EeU/SZb/KxAEGm.H/MDvW315ygMq3wEwm", 1);

        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);

        List<User> actual = service.getAllUsers();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Проверка пустого имени пользователя")
    public void blankName() {
        String expected = "Имя не может быть пустым! Пожалуйста, введите имя!";
        String actual = service.nameCheck("");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Проверка приемлемого имени")
    public void checkName() {
        String expected = "anya";
        String actual = service.nameCheck("anya");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Проверка неприемлемого email (нет @, .)")
    public void incorrectEmail1() {
        String actual = service.emailCheck("anya");
        String expected = "Пожалуйста, введите корректный email!";

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Проверка неприемлемого email (нет .)")
    public void incorrectEmail2() {
        String actual = service.emailCheck("anya@ya");
        String expected = "Пожалуйста, введите корректный email!";

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Проверка неприемлемого email (кириллицей)")
    public void incorrectEmail3() {
        String actual = service.emailCheck("аня@я.ру");
        String expected = "Пожалуйста, введите корректный email!";

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Проверка неприемлемого email (нет @)")
    public void incorrectEmail4() {
        String actual = service.emailCheck("anya.ru");
        String expected = "Пожалуйста, введите корректный email!";

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Проверка неприемлемого email (пустой email)")
    public void incorrectEmail5() {
        String actual = service.emailCheck("");
        String expected = "Пожалуйста, введите корректный email!";

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Проверка неприемлемого email (такой уже зарегистрирован)")
    public void nonUniqueEmail() {

        String expected = "Пользователь с таким email уже зарегистрирован!";
        String actual = service.emailCheck("anya@ya.ru");

        assertThat(actual).isEqualTo(expected);

    }

    @Test
    @DisplayName("Проверка приемлемого email")
    public void basicCheckEmail() {
        String expected = "anya_anya@ya.ru";
        String actual = service.emailCheck("anya_anya@ya.ru");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Попытка входа в систему зарегистрированного пользователя")
    public void loginUser() {
        Object[] expected = new Object[]{true, "anya"};
        Object[] actual = service.loginUser("anya@ya.ru", "1234");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Попытка входа в систему с незарегистрированным email")
    public void loginUserUnknownEmail() {
        Object[] expected = new Object[]{false, "unknownEmail"};
        Object[] actual = service.loginUser("anyaa@ya.ru", "1234");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Попытка входа в систему с зарегистрированным email, но неподходящим паролем")
    public void loginUserWrongPass() {
        Object[] expected = new Object[]{false, "wrongPass"};
        Object[] actual = service.loginUser("anya@ya.ru", "5678");

        assertThat(actual).isEqualTo(expected);
    }
    @Test
    @DisplayName("Обновление имени пользователя")
        public void updateName (){
       try {
           String expected = "newAnya";
           String actual = service.updateName("newAnya", "anya@ya.ru");

           assertThat(actual).isEqualTo(expected);
       } catch (SQLException e){
           fail();
       }
    }
    @Test
    @DisplayName("Обновление email пользователя")
    public void updateEmail (){
        try {
            String expected = "newanya@ya.ru";
            String actual = service.updateEmail("newanya@ya.ru", "anya@ya.ru");

            assertThat(actual).isEqualTo(expected);
        } catch (SQLException e){
            fail();
        }
    }
    @Test
    @DisplayName("Обновление email пользователя")
    public void updatePassword (){
        try {

            service.updatePassword("5678", "anya@ya.ru");

            assertThat(service.readUserByEmail("anya@ya.ru").getPassword())
                    .isNotEqualTo("$2a$10$Lz/N/PPqZTdHgRQC6Wf6EeU/SZb/KxAEGm.H/MDvW315ygMq3wEwm");

        } catch (SQLException e){
            fail();
        }
    }

    @Test
    @DisplayName("Обновление статуса активности аккаунта пользователя")
    public void updateActive (){
        try {

            service.updateActive(false, "anya@ya.ru");
            assertThat(service.readUserByEmail("anya@ya.ru").isActive()).isEqualTo(false);

        } catch (SQLException e){
            fail();
        }
    }

    @Test
    @DisplayName("Удаление пользователя")
    public void deleteUser (){
        try {

            service.deleteUserByEmail("anya@ya.ru");
            assertThat(service.readUserByEmail("anya@ya.ru")).isNull();

        } catch (SQLException e){
            fail();
        }
    }
    @Test
    @DisplayName("Добавление пользователя")
    public void createUser (){
        try {
            service.createUser("newAnya", "newanya@ya.ru", "5678");

            assertThat(service.readUserByEmail("newanya@ya.ru")).isNotNull();

        } catch (SQLException e){
            fail();
        }
    }

    @Test
    @DisplayName("Получение сессии соединения")
    public void getConnection (){
            Connection connection1 = service.getRepoConnection();
            assertThat(connection1).isEqualTo(this.connection);
    }
}