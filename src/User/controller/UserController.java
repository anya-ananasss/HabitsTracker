package User.controller;

import Habit.controller.HabitController;
import Habit.repository.HabitRepository;
import Habit.service.HabitService;
import Habit.service.HabitStatisticsService;
import User.model.User;
import User.service.UserService;

import java.util.Scanner;
import java.util.Set;

public class UserController {
    public UserController(UserService service) {
        this.service = service;
    }

    Scanner scanner = new Scanner(System.in);
    private final UserService service;
    private String userEmail = "";
    private boolean mainPageShown = false;

    public void setMainPageShown(boolean mainPageShown) {
        this.mainPageShown = mainPageShown;
    }

    private String userName = "";

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void CREATE_ADMIN (){
        service.createUser("admin", "admin@admin.ru", "admin");
        service.readUser_byEmail("admin@admin.ru").setRole(0);
    }
    public void showGreetingScreen() {
        while (true) {
            if (!mainPageShown) {
                System.out.println("Здравствуйте! Введите 1, чтобы зарегистрироваться, 2 - чтобы войти в существующий аккаунт, 3 - чтобы выйти из программы");
            }
            setMainPageShown(true);
            String i = scanner.nextLine();
            switch (i) {
                case "1":
                    registerNewUser();
                    System.out.println("Введите 1, чтобы зарегистрироваться, 2 - чтобы войти в существующий аккаунт");
                    break;
                case "2":
                    loginUser();
                    break;
                case "3":
                    System.exit(0);
                default:
                    System.out.println("Пожалуйста, введите 1, чтобы зарегистрироваться, 2 - чтобы войти в существующий аккаунт, 3 - чтобы выйти!");
                    break;

            }
        }
    }

    public void registerNewUser() {
        setMainPageShown(false);
        System.out.println("=РЕГИСТРАЦИЯ=");
        String name = enterName_registration();
        String email = enterEmail();
        String password = enterPassword_registration();
        service.createUser(name, email, password);
        System.out.println("Регистрация прошла успешно!");
        showGreetingScreen();
    }

    public String enterName_registration() {
        String name;
        while (true) {
            System.out.println("Введите свое имя");
            name = scanner.nextLine();
            String state = service.nameCheck(name);
            if (!state.equals("Имя не может быть пустым! Пожалуйста, введите имя!")) {
                break;
            }
        }
        return name;
    }

    public String enterEmail() {
        String email;
        while (true) {
            System.out.println("Введите адрес электронной почты:");
            email = scanner.nextLine();
            service.emailCheck(email);
            String state = service.emailCheck(email);
            if (!state.equals("Пожалуйста, введите корректный email!")
                    && !state.equals("Пользователь с таким email уже зарегистрирован!")) {
                break;
            } else {
                System.out.println(state);
            }
        }
        return email;
    }
    public String enterEmail_login() {
        String email;
        while (true) {
            System.out.println("Введите адрес электронной почты:");
            email = scanner.nextLine();
            service.emailCheck(email);
            String state = service.emailCheck(email);
            if (state.equals("Пользователь с таким email уже зарегистрирован!")) {
                break;
            } else if (!state.equals("Пожалуйста, введите корректный email!")){
                System.out.println("Пользователь с таким email не найден!");

                System.out.println("Нажмите 1, чтобы вернуться в меню, 2, чтобы попробовать еще раз.");
                String i = scanner.nextLine();
                switch (i){
                    case "1":
                        showGreetingScreen();
                        break;
                    case "2":
                        break;
                    default:
                        System.out.println("Введите 1 или 2.");
                        break;
                }

            } else {
                System.out.println(state);
            }
        }
        return email;
    }

    public String enterPassword_registration() {
        String password;
        while (true) {
            System.out.println("Введите пароль");
            password = scanner.nextLine();
            System.out.println("Повторите пароль");
            String repeatedPass = scanner.nextLine();
            if (!password.equals(repeatedPass)) {
                System.out.println("Пароли не совпадают! Пожалуйста, повторите попытку!");
            } else {
                break;
            }
        }
        return password;
    }

    public void loginUser() {
        setMainPageShown(false);
        System.out.println("=ВХОД=");
        String email = enterEmail_login();
        if (service.readUser_byEmail(email).isActive()) {
            enterPassword_login(email);
            service.setUser(service.readUser_byEmail(email));
            if (service.readUser_byEmail(email).getRole() == 1) {
                System.out.println("Здравствуйте, " + userName + "!");
                showMainPage_user();
            } else {
                showMainPage_admin();
            }
        } else {
            System.out.println("К сожалению, ваш аккаунт заблокирован! Обратитесь к администратору.");
            showGreetingScreen();
        }
    }

    public void enterPassword_login(String email) {
        String password;
        while (true) {
            System.out.println("Введите пароль");
            password = scanner.nextLine();
            Object[] loginResults = service.loginUser(email, password);
            if (Boolean.parseBoolean(loginResults[0].toString())) {
                userEmail = email;
                userName = loginResults[1].toString();
                break;
            } else {
                System.out.println("Введен неверный пароль! Повторите попытку!");
            }
        }
    }

    public void showMainPage_user() {
        System.out.println("Нажмите 1, чтобы перейти в меню управления привычками, 2 - чтобы перейти в личный кабинет");
        mainLoop:
        while (true) {
            String j = scanner.nextLine();
            switch (j) {
                case "1":
                    goToHabitController(service.readUser_byEmail(userEmail));
                    break mainLoop;
                case "2":
                    showPersonalAccountSettings();
                    break mainLoop;
                default:
                    System.out.println("Пожалуйста, нажмите 1, чтобы перейти в меню управления привычками, 2 - чтобы перейти в личный кабинет!");
                    break;
            }
        }
    }
    public void showMainPage_admin(){
        System.out.println("Здравствуйте, Администратор!");
        mainLoop:
        while (true){
            System.out.println("""
                Выберите, что хотите сделать:
                1. Просмотреть пользователей
                2. Заблокировать пользователя
                3. Удалить пользователя
                4. Выйти из аккаунта
                """);
            String i = scanner.nextLine();
            switch (i) {
                case "1":
                    if (service.getAllUsers().size() == 1) { //только админ
                        System.out.println("Ни одного пользователя не создано!");
                        break;
                    } else {
                        Set<User> users = service.getAllUsers();
                        users.remove(service.readUser_byEmail("admin@admin.ru"));
                        int num = 1;
                        for (User user : users) {
                            System.out.println(num+". "+user.getName()+", "+user.getEmail()+", активен"+user.isActive());
                        }
                    }
                    break;
                case "2":
                    if (service.getAllUsers().size() == 1) { //только админ
                        System.out.println("Ни одного пользователя не создано!");
                    } else {
                        System.out.println("Введите email пользователя, которого хотите заблокировать.");
                        String email = scanner.nextLine(); //TODO спросить подтверждение
                        service.readUser_byEmail(email).setActive(false);
                        System.out.println("Успешно");
                    }
                    break;
                case "3":
                    if (service.getAllUsers().size() == 1) { //только админ
                        System.out.println("Ни одного пользователя не создано!");
                    } else {
                        System.out.println("Введите email пользователя, которого хотите удалить.");
                        String email = scanner.nextLine(); //TODO спросить подтверждение
                        service.deleteUser_byEmail(email);
                        System.out.println("Успешно");
                    }
                    break;
                case "4":
                    logoutUser();
                    break mainLoop;
                default:
                    System.out.println("Введите 1, 2, 3 или 4.");
                    break;
            }
        }
    }

    public void showPersonalAccountSettings() {
        System.out.println("Введите 1, чтобы изменить личную информацию, 2 - чтобы выйти из аккаунта, 3 - чтобы удалить аккаунт, 4 - вернуться назад");
        mainLoop:
        while (true) {
            String i = scanner.nextLine();
            switch (i) {
                case "1":
                    updateUser();
                    break mainLoop;
                case "2":
                    logoutUser();
                    break mainLoop;
                case "3":
                    deleteUser();
                    break mainLoop;
                case "4":
                    showMainPage_user();
                    break mainLoop;
                default:
                    System.out.println("Пожалуйста, введите 1, чтобы изменить личную информацию, 2 - чтобы выйти из аккаунта, 3 - чтобы удалить аккаунт, 4 - вернуться назад!");
                    break;

            }
        }
    }

    public void updateUser() {

        System.out.println("""
                Что вы хотите изменить?
                1. Имя пользователя
                2. Адрес электронной почты
                3. Пароль""");
        mainLoop:
        while (true) {
            String i = scanner.nextLine();
            switch (i) {
                case "1":
                    updateName();
                    showPersonalAccountSettings();
                    break mainLoop;
                case "2":
                    updateEmail();
                    showPersonalAccountSettings();
                    break mainLoop;
                case "3":
                    updatePass();
                    showPersonalAccountSettings();
                    break mainLoop;
                default:
                    System.out.println("Пожалуйста, введите цифру 1, чтобы изменить имя пользователя, 2, чтобы изменить email или 3, чтобы изменить пароль!");
                    break;

            }
        }
    }

    public void logoutUser() {
        userEmail = "";
        userName = "";
        service.setUser(null);
        showGreetingScreen();
    }

    public void updatePass() {
        String updatedValue;
        while (true) {
            System.out.println("Введите старый пароль");
            String exPass = scanner.nextLine();
            if (service.comparePass(exPass, userEmail)) {
                System.out.println("Введите новый пароль");
                updatedValue = scanner.nextLine();
                service.updateUser_changePassword(updatedValue, userEmail);
                System.out.println("Пароль успешно обновлен!");
                break;
            } else {
                System.out.println("Неправильный пароль! Повторите попытку!");
            }
        }
    }

    public void updateEmail() {

        String updatedValue;
        while (true) {
            System.out.println("Введите новый адрес электронной почты");
            updatedValue = scanner.nextLine();
            String res = service.updateUser_changeEmail(updatedValue, userEmail);
            if (res.equals(updatedValue)) {
                userEmail = updatedValue;
                System.out.println("Адрес электронной почты обновлен успешно! Новый адрес: " + updatedValue);
                break;
            } else {
                System.out.println(res);
            }
        }
    }


    public void updateName() {
        String updatedValue;
        while (true) {
            System.out.println("Введите новое имя");
            updatedValue = scanner.nextLine();
            String res = service.updateUser_changeName(updatedValue, userEmail);
            if (res.equals(updatedValue)) {
                userEmail = updatedValue;
                System.out.println(updatedValue+", имя изменено успешно!");
                break;
            } else {
                System.out.println(res);
            }
        }
    }

    public void deleteUser() {
        mainLoop:
        while (true) {
            System.out.println("Вы действительно хотите удалить свой аккаунт? Это действие невозможно отменить! да/нет");
            String i = scanner.nextLine().toLowerCase();
            switch (i) {
                case "да":
                    service.deleteUser_byEmail(userEmail);
                    userEmail = "";
                    userName = "";
                    System.out.println("Аккаунт успешно удален.");
                    showGreetingScreen();
                    break mainLoop;
                case "нет":
                    showPersonalAccountSettings();
                    break mainLoop;
                default:
                    System.out.println("Введите \"да\" или \"нет\".");
                    break;
            }
        }
    }
    public void goToHabitController(User user) {
        HabitRepository habitRepository = new HabitRepository();
        HabitService habitService = new HabitService(habitRepository, user, this.service);
        HabitStatisticsService habitStatisticsService = new HabitStatisticsService(habitRepository, user);
        HabitController habitController = new HabitController(habitService, habitStatisticsService, this);

        habitController.showMainMenu();
    }
}
