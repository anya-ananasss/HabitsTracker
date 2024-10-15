package UserTests.controllerTest;

import User.controller.UserController;
import User.repository.UserRepository;
import User.service.UserService;
import User.model.User;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserControllerTests {
    UserRepository repo = new UserRepository();
    UserService service = new UserService(repo);

    private void newUserRegistration() {
        String input = "anya\nanya@ya.ru\n1234\n1234\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));

        doNothing().when(controller).showGreetingScreen(); //не заходить в showGreetingScreen
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        controller.registerNewUser();
        System.setIn(System.in);

    }


    @Test
    public void showMainPage_option2() {
        String input = "2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));

        doNothing().when(controller).showPersonalAccountSettings();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        controller.showMainPage_user();

        verify(controller).showPersonalAccountSettings();

        System.setOut(System.out);
        System.setIn(System.in);

    }

    @Test
    public void showMainPage_optionMismatch() {
        String input = "унрдр тро к унрдр уожтфю ёнб дву рйпвщвжф рёпр к фриж\n2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));

        doNothing().when(controller).registerNewUser();
        doNothing().when(controller).showPersonalAccountSettings();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        controller.showMainPage_user();

        String expectedMessage = "Пожалуйста, нажмите 1, чтобы перейти в меню управления привычками, 2 - чтобы перейти в личный кабинет!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);

    }

    @Test
    public void showPersonalAccountSettings_option1() {
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));

        doNothing().when(controller).updateUser();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        controller.showPersonalAccountSettings();
        verify(controller).updateUser();

        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showPersonalAccountSettings_option2() {
        String input = "2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));

        doNothing().when(controller).logoutUser();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        controller.showPersonalAccountSettings();
        verify(controller).logoutUser();

        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showPersonalAccountSettings_option3() {
        String input = "3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));

        doNothing().when(controller).deleteUser();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        controller.showPersonalAccountSettings();
        verify(controller).deleteUser();

        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showPersonalAccountSettings_option4() {
        String input = "4\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));

        doNothing().when(controller).showMainPage_user();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        controller.showPersonalAccountSettings();
        verify(controller).showMainPage_user();

        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showPersonalAccountSettings_optionMismatch() {
        String input = "ttt\n4\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));

        doNothing().when(controller).showMainPage_user();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        controller.showPersonalAccountSettings();

        String expectedMessage = "Пожалуйста, введите 1, чтобы изменить личную информацию, 2 - чтобы выйти из аккаунта, 3 - чтобы удалить аккаунт, 4 - вернуться назад!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void updateUser_option1() {
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));

        doNothing().when(controller).updateName();
        doNothing().when(controller).showPersonalAccountSettings();

        controller.updateUser();
        verify(controller).updateName();

        System.setIn(System.in);
    }

    @Test
    public void updateUser_option2() {
        String input = "2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));

        doNothing().when(controller).updateEmail();
        doNothing().when(controller).showPersonalAccountSettings();
        controller.updateUser();
        verify(controller).updateEmail();


        System.setIn(System.in);
    }

    @Test
    public void updateUser_option3() {
        String input = "3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));

        doNothing().when(controller).updatePass();
        doNothing().when(controller).showPersonalAccountSettings();
        controller.updateUser();
        verify(controller).updatePass();

        System.setIn(System.in);
    }

    @Test
    public void updateUser_optionMismatch() {
        String input = "ttt\n3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));

        doNothing().when(controller).updatePass();
        doNothing().when(controller).showPersonalAccountSettings();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        controller.updateUser();

        String expectedMessage = ("Пожалуйста, введите цифру 1, чтобы изменить имя пользователя, 2, чтобы изменить email или 3, чтобы изменить пароль!");
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void basicUpdatePass() {
        newUserRegistration();


        String input1 = "1234\n5678\n";
        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
        System.setIn(in1);

        UserController controller = new UserController(service);
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.updatePass();


        String expectedMessage = "Пароль успешно обновлен!";
        String output = outContent.toString();
        String expectedEmail = "anya@ya.ru";
        String expectedName = "anya";
        assertTrue(output.contains(expectedMessage));
        assertTrue(controller.getUserEmail().contentEquals(expectedEmail));
        assertTrue(controller.getUserName().contentEquals(expectedName));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void basicUpdateEmail() {
        newUserRegistration();


        String input1 = "an@ya.ru\n";
        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
        System.setIn(in1);


        UserController controller = new UserController(service);
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.updateEmail();


        String expectedMessage = "Введите новый адрес электронной почты\r\nАдрес электронной почты обновлен успешно! Новый адрес: an@ya.ru";
        String output = outContent.toString();
        String expectedEmail = "an@ya.ru";
        String expectedName = "anya";
        assertTrue(output.contains(expectedMessage));
        assertTrue(controller.getUserEmail().contentEquals(expectedEmail));
        assertTrue(controller.getUserName().contentEquals(expectedName));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void basicUpdateName() {
        newUserRegistration();


        String input = "анечка\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        doNothing().when(controller).showGreetingScreen(); //не заходить в showGreetingScreen

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        controller.updateName();


        String expectedMessage = "Введите новое имя\r\nанечка, имя изменено успешно!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void updatePass_passwordMismatch() {
        newUserRegistration();


        String input1 = "1111\n1234\n5678\n";
        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
        System.setIn(in1);

        UserController controller = new UserController(service);
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.updatePass();


        String expectedMessage = "Неправильный пароль! Повторите попытку!\r\nВведите старый пароль";
        String output = outContent.toString();
        String expectedEmail = "anya@ya.ru";
        String expectedName = "anya";
        assertTrue(output.contains(expectedMessage));
        assertTrue(controller.getUserEmail().contentEquals(expectedEmail));
        assertTrue(controller.getUserName().contentEquals(expectedName));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void updateEmail_incorrectEmail1() {
        newUserRegistration();


        String input1 = "anya\nan@ya.ru\n";
        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
        System.setIn(in1);


        UserController controller = new UserController(service);
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.updateEmail();


        String expectedMessage = "Пожалуйста, введите корректный email!\r\nВведите новый адрес электронной почты";
        String output = outContent.toString();
        String expectedEmail = "an@ya.ru";
        String expectedName = "anya";
        assertTrue(output.contains(expectedMessage));
        assertTrue(controller.getUserEmail().contentEquals(expectedEmail));
        assertTrue(controller.getUserName().contentEquals(expectedName));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void updateEmail_incorrectEmail2() {
        newUserRegistration();


        String input1 = "anya@ya\nan@ya.ru\n";
        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
        System.setIn(in1);


        UserController controller = new UserController(service);
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.updateEmail();


        String expectedMessage = "Пожалуйста, введите корректный email!\r\nВведите новый адрес электронной почты";
        String output = outContent.toString();
        String expectedEmail = "an@ya.ru";
        String expectedName = "anya";
        assertTrue(output.contains(expectedMessage));
        assertTrue(controller.getUserEmail().contentEquals(expectedEmail));
        assertTrue(controller.getUserName().contentEquals(expectedName));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void updateEmail_incorrectEmail3() {
        newUserRegistration();


        String input1 = "аня@я.ру\nan@ya.ru\n";
        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
        System.setIn(in1);


        UserController controller = new UserController(service);
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.updateEmail();


        String expectedMessage = "Пожалуйста, введите корректный email!\r\nВведите новый адрес электронной почты";
        String output = outContent.toString();
        String expectedEmail = "an@ya.ru";
        String expectedName = "anya";
        assertTrue(output.contains(expectedMessage));
        assertTrue(controller.getUserEmail().contentEquals(expectedEmail));
        assertTrue(controller.getUserName().contentEquals(expectedName));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void updateEmail_incorrectEmail4() {
        newUserRegistration();


        String input1 = "anya.ru\nan@ya.ru\n";
        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
        System.setIn(in1);


        UserController controller = new UserController(service);
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.updateEmail();


        String expectedMessage = "Пожалуйста, введите корректный email!\r\nВведите новый адрес электронной почты";
        String output = outContent.toString();
        String expectedEmail = "an@ya.ru";
        String expectedName = "anya";
        assertTrue(output.contains(expectedMessage));
        assertTrue(controller.getUserEmail().contentEquals(expectedEmail));
        assertTrue(controller.getUserName().contentEquals(expectedName));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void updateEmail_incorrectEmail5() {
        newUserRegistration();


        String input1 = "\nan@ya.ru\n";
        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
        System.setIn(in1);


        UserController controller = new UserController(service);
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.updateEmail();


        String expectedMessage = "Пожалуйста, введите корректный email!\r\nВведите новый адрес электронной почты";
        String output = outContent.toString();
        String expectedEmail = "an@ya.ru";
        String expectedName = "anya";
        assertTrue(output.contains(expectedMessage));
        assertTrue(controller.getUserEmail().contentEquals(expectedEmail));
        assertTrue(controller.getUserName().contentEquals(expectedName));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void updateEmail_nonUniqueEmail() {
        newUserRegistration();


        String input1 = "anya@ya.ru\nan@ya.ru\n";
        InputStream in1 = new ByteArrayInputStream(input1.getBytes());
        System.setIn(in1);


        UserController controller = new UserController(service);
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.updateEmail();


        String expectedMessage = """
                Пользователь с таким email уже зарегистрирован!\r
                Введите новый адрес электронной почты\r
                Адрес электронной почты обновлен успешно! Новый адрес: an@ya.ru""";
        String output = outContent.toString();
        String expectedEmail = "an@ya.ru";
        String expectedName = "anya";
        assertTrue(output.contains(expectedMessage));
        assertTrue(controller.getUserEmail().contentEquals(expectedEmail));
        assertTrue(controller.getUserName().contentEquals(expectedName));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void updateName_blankName() {
        newUserRegistration();


        String input = "\nанечка\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        doNothing().when(controller).showGreetingScreen(); //не заходить в showGreetingScreen

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        controller.updateName();


        String expectedMessage = "Имя не может быть пустым! Пожалуйста, введите имя!\r\nВведите новое имя\r\nанечка, имя изменено успешно!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void logoutUser() {
        newUserRegistration();


        UserController controller = spy(new UserController(service));
        doNothing().when(controller).showGreetingScreen();
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.logoutUser();

        String expectedEmail = "";
        String expectedName = "";
        assertTrue(controller.getUserEmail().contentEquals(expectedEmail));
        assertTrue(controller.getUserName().contentEquals(expectedName));
    }

    @Test
    public void deleteUser_unknownResponse() {
        newUserRegistration();


        String input = "ючаь мнцнл\nнет\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        doNothing().when(controller).showPersonalAccountSettings();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.deleteUser();

        String expectedMessage = "Введите \"да\" или \"нет\".";
        String output = outContent.toString();
        String expectedEmail = "anya@ya.ru";
        String expectedName = "anya";
        assertTrue(output.contains(expectedMessage));
        assertTrue(controller.getUserEmail().contentEquals(expectedEmail));
        assertTrue(controller.getUserName().contentEquals(expectedName));
    }

    @Test
    public void deleteUser_yes() {
        newUserRegistration();


        String input = "да\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        doNothing().when(controller).showGreetingScreen();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.deleteUser();

        User found = service.readUser_byEmail("anya@ya.ru");

        assertNull(found);
    }

    @Test
    public void deleteUser_yes_checkUser() {
        newUserRegistration();


        String input = "да\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        UserController controller = spy(new UserController(service));
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        doNothing().when(controller).showGreetingScreen();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.deleteUser();

        String expectedMessage = "Аккаунт успешно удален.";
        String output = outContent.toString();
        String expectedEmail = "";
        String expectedName = "";
        assertTrue(output.contains(expectedMessage));
        assertTrue(controller.getUserEmail().contentEquals(expectedEmail));
        assertTrue(controller.getUserName().contentEquals(expectedName));
    }

    @Test
    public void deleteUser_no() {
        newUserRegistration();


        String input = "нет\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        UserController controller = spy(new UserController(service));
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");

        doNothing().when(controller).showPersonalAccountSettings();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.deleteUser();

        String expectedEmail = "anya@ya.ru";
        String expectedName = "anya";
        assertTrue(controller.getUserEmail().contentEquals(expectedEmail));
        assertTrue(controller.getUserName().contentEquals(expectedName));
    }

    @Test
    public void deleteUser_no_checkUser() {
        newUserRegistration();


        String input = "нет\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        UserController controller = spy(new UserController(service));
        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");


        doNothing().when(controller).showPersonalAccountSettings();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.deleteUser();

        User expected = new User("anya", "anya@ya.ru", service.encrypt("1234"), 1);
        User found = service.readUser_byEmail("anya@ya.ru");
        assertEquals(expected, found);
    }

    @Test
    public void enterPass() {
        newUserRegistration();
        String input = "5678\n1234\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        UserController controller = new UserController(service);

        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.enterPassword_login("anya@ya.ru");

        String expectedMessage1 = "Введен неверный пароль! Повторите попытку!";
        String expectedMessage2 = "Авторизируем...";

        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage1));
        assertTrue(output.contains(expectedMessage2));
        System.setOut(System.out);
        System.setIn(System.in);


    }

    @Test
    public void loginUser() {
        newUserRegistration();
        String input = "anya@ya.ru\n1234\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        UserController controller = spy(new UserController(service));
        doNothing().when(controller).showMainPage_user();

        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.loginUser();

        String expectedMessage = "Здравствуйте, anya!";

        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void enterEmail_login() {
        newUserRegistration();
        String input = "an@ya.ru\n1\nanya@ya.ru\n1234\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        UserController controller = new UserController(service);

        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.enterEmail_login();

        String expectedMessage = "Пользователь с таким email не найден!";

        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void adminPage_1_void() {
        String input = "1\n4\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        UserController controller = spy(new UserController(service));
        controller.CREATE_ADMIN();
        doNothing().when(controller).showGreetingScreen();

        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.showMainPage_admin();

        String expectedMessage = "Ни одного пользователя не создано!";

        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void adminPage_2_void() {
        String input = "2\n4\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        UserController controller = spy(new UserController(service));
        controller.CREATE_ADMIN();
        doNothing().when(controller).showGreetingScreen();

        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.showMainPage_admin();

        String expectedMessage = "Ни одного пользователя не создано!";

        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void adminPage_3_void() {
        String input = "3\n4\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        UserController controller = spy(new UserController(service));
        controller.CREATE_ADMIN();
        doNothing().when(controller).showGreetingScreen();

        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.showMainPage_admin();

        String expectedMessage = "Ни одного пользователя не создано!";

        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void adminPage_1() {
        String input = "1\n4\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        UserController controller = spy(new UserController(service));
        controller.CREATE_ADMIN();
        newUserRegistration();
        doNothing().when(controller).showGreetingScreen();

        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.showMainPage_admin();

        String expectedMessage = "1. anya, anya@ya.ru, активен=true";

        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void adminPage_2() {
        String input = "2\nanya@ya.ru\n1\n4\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        UserController controller = spy(new UserController(service));
        controller.CREATE_ADMIN();
        newUserRegistration();
        doNothing().when(controller).showGreetingScreen();

        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.showMainPage_admin();

        String expectedMessage1 = "Успешно";
        String expectedMessage2 = "1. anya, anya@ya.ru, активен=false";


        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage1));
        assertTrue(output.contains(expectedMessage2));


        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void adminPage_3() {
        String input = "3\nanya@ya.ru\n1\n4\n";

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        UserController controller = spy(new UserController(service));
        controller.CREATE_ADMIN();
        newUserRegistration();
        doNothing().when(controller).showGreetingScreen();

        controller.setUserEmail("anya@ya.ru");
        controller.setUserName("anya");


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.showMainPage_admin();

        String expectedMessage1 = "Успешно";
        String expectedMessage2 = "Ни одного пользователя не создано!";

        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage1));
        assertTrue(output.contains(expectedMessage2));
        System.setOut(System.out);
        System.setIn(System.in);
    }


    @Test
    public void testBlockedUser(){
        String input = "test@ya.ru\n1234\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        UserController controller = spy(new UserController(service));
        doNothing().when(controller).showGreetingScreen();


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        User testUser = new User("test", "test@ya.ru", "1234", 1);
        testUser.setActive(false);
        repo.addUser(testUser);
        repo.addEmail("test@ya.ru");

        controller.loginUser();

        String expectedMessage = "К сожалению, ваш аккаунт заблокирован! Обратитесь к администратору.";



        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));


        System.setOut(System.out);
        System.setIn(System.in);
    }
}
