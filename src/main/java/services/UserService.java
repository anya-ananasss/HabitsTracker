package services;

import lombok.Getter;
import lombok.Setter;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import repositories.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Сервис для работы с сущностью User
 * <p>
 * * @author Gureva Anna
 * * @version 1.0
 * * @since 19.10.2024
 * </p>
 */
@Getter
@Setter
public class UserService {
    private final UserRepository repository;

    /**
     * Конструктор для создания сервиса с заданным репозиторием для работы с пользователями.
     *
     * @param repository репозиторий для работы с пользователями
     */
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Получает сессию соединения с базой данных через репозиторий.
     *
     * @return сессия соединения с базой данных
     */
    public Connection getRepoConnection() {
        return this.repository.getConnection();
    }

    /**
     * Создает нового пользователя с заданными данными.
     *
     * @param name     имя пользователя
     * @param email    электронная почта пользователя
     * @param password пароль пользователя; шифруется в методе
     */
    public void createUser(String name, String email, String password) throws SQLException {
        String encryptedPass = encrypt(password);
        repository.addUser(name, email, encryptedPass, 1);
    }

    /**
     * Выполняет вход пользователя в систему по электронной почте и паролю.
     *
     * @param email    электронная почта пользователя
     * @param password пароль пользователя в незашифрованном виде; сравнивается с зашифрованным в методе
     * @return массив, где первый элемент - результат входа true/false,
     * а второй элемент - сообщение о причине неудачи при входе или имя пользователя
     */
    public Object[] loginUser(String email, String password) {
        User foundUser = repository.readUserByEmail(email);
        if (foundUser == null) {
            return new Object[]{false, "unknownEmail"};
        }
        if (comparePass(password, foundUser.getEmail())) {
            return new Object[]{true, foundUser.getName()};
        } else {
            return new Object[]{false, "wrongPass"};
        }
    }

    /**
     * Шифрует пароль с использованием алгоритма BCrypt.
     *
     * @param password пароль в незашифрованном виде
     * @return зашифрованный пароль
     */
    public String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Сравнивает незашифрованный пароль с зашифрованным паролем пользователя.
     *
     * @param password  введенный пароль
     * @param userEmail электронная почта пользователя
     * @return true, если пароли совпадают; иначе false
     */
    public boolean comparePass(String password, String userEmail) {
        String hashed = repository.readUserByEmail(userEmail).getPassword();
        return BCrypt.checkpw(password, hashed);
    }

    /**
     * Проверяет, приемлемо ли имя пользователя (не пусто ли оно).
     *
     * @param name имя для проверки
     * @return имя или сообщение об ошибке
     */
    public String nameCheck(String name) {
        if (name.isEmpty()) {
            return "Имя не может быть пустым! Пожалуйста, введите имя!";
        } else {
            return name;
        }
    }

    /**
     * Проверяет, приемлема ли электронная почта пользователя (соответствует ли шаблону для электронной почты
     * и не зарегистрирована ли она уже другим пользователем).
     *
     * @param email электронная почта пользователя
     * @return электронная почта или сообщение об ошибке
     */
    public String emailCheck(String email) {
        if (!isEmailValid(email)) {
            return "Пожалуйста, введите корректный email!";
        } else {
            String normalizedEmail = email.toLowerCase().replaceAll("\\s+", " ").trim();
            if (repository.getEmails().contains(normalizedEmail)) {
                return "Пользователь с таким email уже зарегистрирован!";
            }
        }
        return email;
    }

    private boolean isEmailValid(String email) {
        String emailRegex =
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        return emailPattern.matcher(email).matches();
    }

    /**
     * Обновляет имя пользователя.
     *
     * @param newName новое имя пользователя
     * @param email   электронная почта пользователя
     * @return новое имя или сообщение об ошибке
     */
    public String updateName(String newName, String email) throws SQLException {
        User user = repository.readUserByEmail(email);
        String res = nameCheck(newName);
        if (res.equals(newName)) {
            repository.updateName(newName, user);
        }
        return res;
    }
    /**
     * Обновляет электронную почту пользователя.
     *
     * @param newEmail новая электронная почта пользователя
     * @param oldEmail старая электронная почта пользователя
     * @return новая электронная почта или сообщение об ошибке
     */
    public String updateEmail(String newEmail, String oldEmail) throws SQLException {
        User user = repository.readUserByEmail(oldEmail);
        String res = emailCheck(newEmail);
        if (res.equals(newEmail)) {
            repository.updateEmail(newEmail, user);
        }
        return res;
    }
    /**
     * Обновляет пароль пользователя.
     *
     * @param newPass новый пароль в незашифрованном виде; шифруется в методе
     * @param email электронная почта пользователя
     */

    public void updatePassword(String newPass, String email) throws SQLException {
        User user = repository.readUserByEmail(email);
        String encryptedPass = encrypt(newPass);
        repository.updatePassword(encryptedPass, user);
    }
    /**
     * Обновляет статус активности аккаунта пользователя.
     *
     * @param isActive активен или неактивен аккаунт (true - активен, false - неактивен)
     * @param email электронная почта пользователя
     */
    public void updateActive(boolean isActive, String email) throws SQLException {
        repository.updateActive(isActive, readUserByEmail(email));
    }
    /**
     * Удаляет пользователя.
     *
     * @param email электронная почта пользователя, которого нужно удалить
     */
    public void deleteUserByEmail(String email) throws SQLException {
        repository.deleteUserByEmail(email);
    }

    /**
     * Находит пользователя по его электронной почте.
     * @param email электронная почта пользователя
     * @return объект User, если пользователь найден; null иначе
     */
    public User readUserByEmail(String email) {
        return repository.readUserByEmail(email);
    }
    /**
     * Получает список всех зарегистрированных пользователей вне зависимости от статуса их аккаунта.
     *
     * @return список пользователей (пустой, если пользователей нет)
     */
    public List<User> getAllUsers() {
        return repository.getUsers();
    }
}
