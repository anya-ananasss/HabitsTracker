package UserTests.serviceTests;


import User.controller.UserController;
import User.repository.UserRepository;
import User.service.UserService;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;


public class UserServiceTest {
    UserRepository repo = new UserRepository();
    UserService service = new UserService(repo);
    @Test
    public void blankName() {
        String expectedMessage = "Имя не может быть пустым! Пожалуйста, введите имя!";
        String actual = service.nameCheck("");
        assertEquals(expectedMessage, actual);
    }

    @Test
    public void checkName() {
        String expectedMessage = "anya";
        String actual = service.nameCheck("anya");
        assertEquals(expectedMessage, actual);
    }

    @Test
    public void incorrectEmail1() {
        String actual = service.emailCheck("anya");
        String expected = "Пожалуйста, введите корректный email!";

        assertEquals(expected, actual);
    }

    @Test
    public void incorrectEmail2() {
        String actual = service.emailCheck("anya@ya");
        String expected = "Пожалуйста, введите корректный email!";

        assertEquals(expected, actual);
    }

    @Test
    public void incorrectEmail3() {
        String actual = service.emailCheck("аня@я.ру");
        String expected = "Пожалуйста, введите корректный email!";

        assertEquals(expected, actual);
    }

    @Test
    public void incorrectEmail4() {
        String actual = service.emailCheck("anya.ru");
        String expected = "Пожалуйста, введите корректный email!";

        assertEquals(expected, actual);
    }

    @Test
    public void incorrectEmail5() {
        String actual = service.emailCheck("");
        String expected = "Пожалуйста, введите корректный email!";

        assertEquals(expected, actual);
    }

    @Test
    public void nonUniqueEmail() {

        service.createUser("anya", "anya@ya.ru", "1234");
        String expected = "Пользователь с таким email уже зарегистрирован!";
        String actual = service.emailCheck("anya@ya.ru");
        assertEquals(expected, actual);

    }

    @Test
    public void checkEmail() {
        String expected = "anya@ya.ru";
        String actual = service.emailCheck("anya@ya.ru");
        assertEquals(expected, actual);
    }

    @Test
    public void loginUser() {
        service.createUser("anya", "anya@ya.ru", "1234");
        Object[] expected = new Object[]{true, "anya"};
        Object[] actual = service.loginUser("anya@ya.ru", "1234");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void loginUser_unknownEmail() {
        service.createUser("anya", "anya@ya.ru", "1234");
        Object[] expected = new Object[]{false, "unknownEmail"};
        Object[] actual = service.loginUser("anyaa@ya.ru", "1234");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void loginUser_wrongPass() {
        service.createUser("anya", "anya@ya.ru", "1234");
        Object[] expected = new Object[]{false, "wrongPass"};
        Object[] actual = service.loginUser("anya@ya.ru", "5678");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void registration_repeatedPassMismatch() {
        String input = "1234\n1233\n1234\n1234";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        UserController controller = new UserController(service);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        controller.enterPassword_registration();


        String expectedMessage = "Пароли не совпадают! Пожалуйста, повторите попытку!\r\nВведите пароль";
        String output = outContent.toString();
        String expectedNameEmail = "";
        assertTrue(output.contains(expectedMessage));
        assertTrue(controller.getUserEmail().contentEquals(expectedNameEmail));
        assertTrue(controller.getUserName().contentEquals(expectedNameEmail));
        System.setOut(System.out);
        System.setIn(System.in);

    }
}