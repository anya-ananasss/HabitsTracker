//package UserTests.controllerTest;
//
//import org.junit.jupiter.api.*;
//import org.testcontainers.containers.PostgreSQLContainer;
//import repositories.HabitRepository;
//import serviceClasses.Config;
//
//import models.User;
//import repositories.UserRepository;
//import services.UserService;
//import controllers.UserController;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//import java.io.PrintStream;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class UserControllerTests {
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
//            "postgres:latest"
//    );
//
//    Connection connection;
//    UserRepository userRepository;
//    UserService service;
//
//    @BeforeEach
//    void setUp() {
//        postgres.start();
//        Config config = new Config();
//        Object[] connections = config.establishConnection();
//        connection = (Connection) connections[0];
//        userRepository = new UserRepository(connection);
//        service = new UserService(userRepository);
//        System.out.println("пипяу");
//    }
//
//    @AfterEach
//    void clear() throws SQLException {
//        System.out.println("ПИПИПЯУ");
//        connection.rollback();
//        connection.close();
//        postgres.stop();
//    }
//
//    @Test
//    public void showMainPageOption2() {
//        String input = "2\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        UserController controller = spy(new UserController(service));
//
//        doNothing().when(controller).showPersonalAccountSettings();
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        controller.showMainPageUser();
//
//        verify(controller).showPersonalAccountSettings();
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//
//    }
//
//    @Test
//    public void showMainPageOptionMismatch() {
//        String input = "унрдр тро к унрдр уожтфю ёнб дву рйпвщвжф рёпр к фриж\n2\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        UserController controller = spy(new UserController(service));
//
//        doNothing().when(controller).registerNewUser();
//        doNothing().when(controller).showPersonalAccountSettings();
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        controller.showMainPageUser();
//
//        String expectedMessage = "Пожалуйста, нажмите 1, чтобы перейти в меню управления привычками, 2 - чтобы перейти в личный кабинет, 3 - чтобы выйти из программы!";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//
//    }
//
//    @Test
//    public void showPersonalAccountSettingsOption1() {
//        String input = "1\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        UserController controller = spy(new UserController(service));
//
//        doNothing().when(controller).updateUser();
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        controller.showPersonalAccountSettings();
//        verify(controller).updateUser();
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showPersonalAccountSettingsOption2() {
//        String input = "2\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        UserController controller = spy(new UserController(service));
//
//        doNothing().when(controller).logoutUser();
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        controller.showPersonalAccountSettings();
//        verify(controller).logoutUser();
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showPersonalAccountSettingsOption3() {
//        String input = "3\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        UserController controller = spy(new UserController(service));
//
//        doNothing().when(controller).deleteUser();
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        controller.showPersonalAccountSettings();
//        verify(controller).deleteUser();
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showPersonalAccountSettingsOption4() {
//        String input = "4\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        UserController controller = spy(new UserController(service));
//
//        doNothing().when(controller).showMainPageUser();
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        controller.showPersonalAccountSettings();
//        verify(controller).showMainPageUser();
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showPersonalAccountSettingsOptionMismatch() {
//        String input = "ttt\n4\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        UserController controller = spy(new UserController(service));
//
//        doNothing().when(controller).showMainPageUser();
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        controller.showPersonalAccountSettings();
//
//        String expectedMessage = "Пожалуйста, введите 1, чтобы изменить личную информацию, 2 - чтобы выйти из аккаунта, 3 - чтобы удалить аккаунт, 4 - вернуться назад!";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void updateUserOption1() {
//        String input = "1\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        UserController controller = spy(new UserController(service));
//
//        doNothing().when(controller).updateName();
//        doNothing().when(controller).showPersonalAccountSettings();
//
//        controller.updateUser();
//        verify(controller).updateName();
//
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void updateUserOption2() {
//        String input = "2\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        UserController controller = spy(new UserController(service));
//
//        doNothing().when(controller).updateEmail();
//        doNothing().when(controller).showPersonalAccountSettings();
//        controller.updateUser();
//        verify(controller).updateEmail();
//
//
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void updateUserOption3() {
//        String input = "3\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        UserController controller = spy(new UserController(service));
//
//        doNothing().when(controller).updatePass();
//        doNothing().when(controller).showPersonalAccountSettings();
//        controller.updateUser();
//        verify(controller).updatePass();
//
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void updateUserOptionMismatch() {
//        String input = "ttt\n3\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        UserController controller = spy(new UserController(service));
//
//        doNothing().when(controller).updatePass();
//        doNothing().when(controller).showPersonalAccountSettings();
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        controller.updateUser();
//
//        String expectedMessage = ("Пожалуйста, введите цифру 1, чтобы изменить имя пользователя, 2, чтобы изменить email или 3, чтобы изменить пароль!");
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void basicUpdateEmail() {
//        String input1 = "an@ya.ru\n";
//        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
//        System.setIn(in1);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connectionMock);
//
//            controller.setUserEmail("anya@ya.ru");
//            controller.setUserName("anya");
//
//            controller.updateEmail();
//            verify(service).updateEmail("an@ya.ru", "anya@ya.ru");
//        } catch (SQLException e) {
//            fail();
//        }
//
//
//        String expectedMessage = "Введите новый адрес электронной почты\r\nАдрес электронной почты обновлен успешно! Новый адрес: an@ya.ru";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//    @Test
//    public void updatePassPasswordMismatch() {
//        String input1 = "1111\n1234\n5678\n";
//        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
//        System.setIn(in1);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connectionMock);
//
//            controller.setUserEmail("anya@ya.ru");
//            controller.setUserName("anya");
//
//            controller.updatePass();
//            verify(service).updatePassword("5678", "anya@ya.ru");
//        } catch (SQLException e) {
//            fail();
//        }
//
//
//        String expectedMessage1 = "Неправильный пароль! Повторите попытку!\r\nВведите старый пароль";
//        String expectedMessage2 = "Пароль успешно обновлен!";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage1);
//        assertThat(output).contains(expectedMessage2);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void updateEmailIncorrectEmail1() {
//        String input1 = "anya\nan@ya.ru\n";
//        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
//        System.setIn(in1);
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connectionMock);
//
//            controller.setUserEmail("anya@ya.ru");
//            controller.setUserName("anya");
//
//            controller.updateEmail();
//            verify(service).updateEmail("an@ya.ru", "anya@ya.ru");
//        } catch (SQLException e) {
//            fail();
//        }
//
//
//        String expectedMessage = "Пожалуйста, введите корректный email!\r\nВведите новый адрес электронной почты";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void updateEmailIncorrectEmail2() {
//
//        String input1 = "anya@ya\nan@ya.ru\n";
//        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
//        System.setIn(in1);
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connectionMock);
//
//            controller.setUserEmail("anya@ya.ru");
//            controller.setUserName("anya");
//
//            controller.updateEmail();
//            verify(service).updateEmail("an@ya.ru", "anya@ya.ru");
//        } catch (SQLException e) {
//            fail();
//        }
//
//
//        String expectedMessage = "Пожалуйста, введите корректный email!\r\nВведите новый адрес электронной почты";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void updateEmailIncorrectEmail3() {
//        String input1 = "аня@я.ру\nan@ya.ru\n";
//        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
//        System.setIn(in1);
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connectionMock);
//
//            controller.setUserEmail("anya@ya.ru");
//            controller.setUserName("anya");
//
//            controller.updateEmail();
//            verify(service).updateEmail("an@ya.ru", "anya@ya.ru");
//        } catch (SQLException e) {
//            fail();
//        }
//
//
//        String expectedMessage = "Пожалуйста, введите корректный email!\r\nВведите новый адрес электронной почты";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void updateEmail_incorrectEmail4() {
//        String input1 = "anya.ru\nan@ya.ru\n";
//        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
//        System.setIn(in1);
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connectionMock);
//
//            controller.setUserEmail("anya@ya.ru");
//            controller.setUserName("anya");
//
//            controller.updateEmail();
//            verify(service).updateEmail("an@ya.ru", "anya@ya.ru");
//        } catch (SQLException e) {
//            fail();
//        }
//
//
//        String expectedMessage = "Пожалуйста, введите корректный email!\r\nВведите новый адрес электронной почты";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void updateEmailIncorrectEmail5() {
//        String input1 = "\nan@ya.ru\n";
//        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
//        System.setIn(in1);
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connectionMock);
//
//            controller.setUserEmail("anya@ya.ru");
//            controller.setUserName("anya");
//
//            controller.updateEmail();
//            verify(service).updateEmail("an@ya.ru", "anya@ya.ru");
//        } catch (SQLException e) {
//            fail();
//        }
//
//
//        String expectedMessage = "Пожалуйста, введите корректный email!\r\nВведите новый адрес электронной почты";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void updateEmailNonUniqueEmail() {
//        String input1 = "anya@ya.ru\nan@ya.ru\n";
//        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
//        System.setIn(in1);
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connectionMock);
//
//            controller.setUserEmail("anya@ya.ru");
//            controller.setUserName("anya");
//            User user = service.readUserByEmail("anya@ya.ru");
//            System.out.println(user.getEmail());
//            System.out.println(user.getName());
//            System.out.println(user.getPassword());
//            controller.updateEmail();
//            verify(service).updateEmail("an@ya.ru", "anya@ya.ru");
//        } catch (SQLException e) {
//            fail();
//        }
//
//        String expectedMessage = """
//                Пользователь с таким email уже зарегистрирован!\r
//                Введите новый адрес электронной почты\r
//                Адрес электронной почты обновлен успешно! Новый адрес: an@ya.ru""";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void updateNameBlankName() {
//        String input = "\nанечка\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//
//            controller.setConnection(connectionMock);
//
//            controller.setUserEmail("anya@ya.ru");
//            controller.setUserName("anya");
//
//            controller.updateName();
//            verify(service).updateName("анечка", "anya@ya.ru");
//
//            String expectedMessage = "Имя не может быть пустым! Пожалуйста, введите имя!\r\nВведите новое имя\r\nанечка, имя изменено успешно!";
//            String output = outContent.toString();
//            assertThat(output).contains(expectedMessage);
//            System.setOut(System.out);
//            System.setIn(System.in);
//        } catch (SQLException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void logoutUser() {
//        UserController controller = spy(new UserController(service));
//        doNothing().when(controller).showGreetingScreen();
//        controller.setUserEmail("anya@ya.ru");
//        controller.setUserName("anya");
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.logoutUser();
//
//        String expectedEmail = "";
//        String expectedName = "";
//        assertThat(controller.getUserEmail()).isEqualTo(expectedEmail);
//        assertThat(controller.getUserName()).isEqualTo(expectedName);
//    }
//
//    @Test
//    public void deleteUser_unknownResponse() {
//        String input = "ючаь мнцнл\nнет\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        UserController controller = spy(new UserController(service));
//        controller.setUserEmail("anya@ya.ru");
//        controller.setUserName("anya");
//
//        doNothing().when(controller).showPersonalAccountSettings();
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.deleteUser();
//
//        String expectedMessage = "Введите да или нет.";
//        String output = outContent.toString();
//        String expectedEmail = "anya@ya.ru";
//        String expectedName = "anya";
//        assertThat(output).contains(expectedMessage);
//        assertThat(controller.getUserEmail()).isEqualTo(expectedEmail);
//        assertThat(controller.getUserName()).isEqualTo(expectedName);
//    }
//
//    @Test
//    public void deleteUserYes() {
//
//        String input = "да\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connectionMock);
//            controller.setUserEmail("anya@ya.ru");
//            controller.setUserName("anya");
//
//            controller.deleteUser();
//            verify(service).deleteUserByEmail("anya@ya.ru");
//
//            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//            System.setOut(new PrintStream(outContent));
//
//
//        } catch (SQLException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void deleteUserNo() {
//
//        String input = "нет\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//
//        UserController controller = spy(new UserController(service));
//        controller.setUserEmail("anya@ya.ru");
//        controller.setUserName("anya");
//
//        doNothing().when(controller).showPersonalAccountSettings();
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.deleteUser();
//
//        String expectedEmail = "anya@ya.ru";
//        String expectedName = "anya";
//        assertThat(controller.getUserEmail()).isEqualTo(expectedEmail);
//        assertThat(controller.getUserName()).isEqualTo(expectedName);
//    }
//
//    @Test
//    public void enterPasswordInLogin() {
//        String input = "5678\n1\n1234\n";
//
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//
//        UserController controller = new UserController(service);
//
//        controller.setUserEmail("anya@ya.ru");
//        controller.setUserName("anya");
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.enterPasswordInLogin("anya@ya.ru");
//
//        String expectedMessage1 = "Введен неверный пароль! Повторите попытку!";
//        String expectedMessage2 = "Авторизируем...";
//
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage1);
//        assertThat(output).contains(expectedMessage2);
//        System.setOut(System.out);
//        System.setIn(System.in);
//
//
//    }
//
//    @Test
//    public void loginUser() {
//        String input = "anya@ya.ru\n1234\n1234\n";
//
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            doNothing().when(connectionMock).commit();
//
//
//            UserController controller = spy(new UserController(service));
//            doNothing().when(controller).showMainPageUser();
//            doNothing().when(controller).showGreetingScreen();
//            doNothing().when(controller).initHabitsScheduler();
//           HabitRepository habitRepository = new HabitRepository(userRepository);
//            controller.setHabitRepository(habitRepository);
//            controller.setConnection(connectionMock);
//
//
//            controller.loginUser();
//            verify(controller).enterPasswordInLogin("anya@ya.ru");
//        } catch (SQLException e) {
//            fail();
//        }
//
//        String expectedMessage = "Здравствуйте, anya!";
//
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void enterEmailInLogin() {
//        String input = "an@ya.ru\n1\nanya@ya.ru\n1234\n";
//
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//
//        UserController controller = new UserController(service);
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.enterEmailInLogin();
//
//        String expectedMessage = "Пользователь с таким email не найден!";
//
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void adminPage1void() {
//        String input = "1\n5\n";
//
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            service.deleteUserByEmail("anya@ya.ru");
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connectionMock);
//
//            controller.setUserEmail("admin@admin.ru");
//            controller.setUserName("admin");
//
//            controller.showMainPageAdmin();
//            verify(service).getAllUsers();
//        } catch (SQLException e) {
//            fail();
//        }
//
//
//        String expectedMessage = "Ни одного пользователя не создано!";
//
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void adminPage2void() {
//        String input = "2\n5\n";
//
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            service.deleteUserByEmail("anya@ya.ru");
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connectionMock);
//
//            controller.setUserEmail("admin@admin.ru");
//            controller.setUserName("admin");
//
//            controller.showMainPageAdmin();
//            verify(service).getAllUsers();
//        } catch (SQLException e) {
//            fail();
//        }
//
//
//        String expectedMessage = "Ни одного пользователя не создано!";
//
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void adminPage3void() {
//        String input = "3\n5\n";
//
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            service.deleteUserByEmail("anya@ya.ru");
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connectionMock);
//
//            controller.setUserEmail("admin@admin.ru");
//            controller.setUserName("admin");
//
//            controller.showMainPageAdmin();
//            verify(service).getAllUsers();
//        } catch (SQLException e) {
//            fail();
//        }
//
//
//        String expectedMessage = "Ни одного пользователя не создано!";
//
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void adminPage4void() {
//        String input = "4\n5\n";
//
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        try {
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//            service.deleteUserByEmail("anya@ya.ru");
//            doNothing().when(connectionMock).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connectionMock);
//
//            controller.setUserEmail("admin@admin.ru");
//            controller.setUserName("admin");
//
//            controller.showMainPageAdmin();
//            verify(service).getAllUsers();
//        } catch (SQLException e) {
//            fail();
//        }
//
//
//        String expectedMessage = "Ни одного пользователя не создано!";
//
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//
//    @Test
//    public void adminPage1() {
//        String input = "1\n5\n";
//
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//
//        UserController controller = spy(new UserController(service));
//        doNothing().when(controller).showGreetingScreen();
//        controller.setConnection(connection);
//        controller.setUserEmail("anya@ya.ru");
//        controller.setUserName("anya");
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.showMainPageAdmin();
//
//        String expectedMessage = "1. anya, anya@ya.ru, активен=true";
//
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void adminPage2() {
//        String input = "2\nanya@ya.ru\n1\n5\n";
//
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        try {
//
//            Connection connectionMock = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//
//            doNothing().when(connectionMock).commit();
//            UserController controller = spy(new UserController(service));
//            doNothing().when(controller).showGreetingScreen();
//
//            HabitRepository habitRepository = new HabitRepository(userRepository);
//            controller.setHabitRepository(habitRepository);
//            controller.setConnection(connectionMock);
//            controller.showMainPageAdmin();
//
//            String expectedMessage1 = "Успешно";
//            String expectedMessage2 = "1. anya, anya@ya.ru, активен=false";
//
//
//            String output = outContent.toString();
//            assertThat(output).contains(expectedMessage1);
//            assertThat(output).contains(expectedMessage2);
//
//
//            System.setOut(System.out);
//            System.setIn(System.in);
//
//        } catch (SQLException e) {
//            fail();
//        }
//
//    }
//
//    @Test
//    public void adminPage3NoUsers() {
//        String input = "3\nanya@ya.ru\n5\n";
//
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        try {
//            connection = mock(Connection.class);
//            service = spy(new UserService(userRepository));
//            service.deleteUserByEmail("anya@ya.ru");
//            doNothing().when(connection).commit();
//
//            UserController controller = spy(new UserController(service));
//
//            doNothing().when(controller).showGreetingScreen();
//            controller.setConnection(connection);
//
//            controller.setUserEmail("admin@admin.ru");
//            controller.setUserName("admin");
//
//            controller.showMainPageAdmin();
//        } catch (SQLException e) {
//            fail();
//        }
//
//        String expectedMessage = "Ни одного пользователя не создано!";
//
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//
//    @Test
//    public void testBlockedUser() {
//        String input = "test@ya.ru\n1234\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//
//        UserController controller = spy(new UserController(service));
//        doNothing().when(controller).showGreetingScreen();
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        try {
//            service.createUser("test", "test@ya.ru", "1234");
//            service.updateActive(false, "test@ya.ru");
//        } catch (SQLException e) {
//            fail();
//        }
//
//        controller.loginUser();
//
//        String expectedMessage = "К сожалению, ваш аккаунт заблокирован! Обратитесь к администратору.";
//
//
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void registrationRepeatedPassMismatch() {
//        String input = "1234\n1233\n1234\n1234";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//        UserController controller = new UserController(service);
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        controller.enterPasswordInRegistration();
//
//
//        String expectedMessage = "Пароли не совпадают! Пожалуйста, повторите попытку!\r\nВведите пароль";
//        String output = outContent.toString();
//        String expectedNameEmail = "";
//        assertThat(output).contains(expectedMessage);
//        assertThat(controller.getUserEmail()).isEqualTo(expectedNameEmail);
//        assertThat(controller.getUserName()).isEqualTo(expectedNameEmail);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//}
//
