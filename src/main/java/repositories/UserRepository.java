package repositories;

import lombok.Getter;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с сущностью User
 * <p>
 * * @author Gureva Anna
 * * @version 1.0
 * * @since 19.10.2024
 * </p>
 */
@Getter
public class UserRepository {
    private final Connection connection;
    /**
     * Конструктор для инициализации репозитория для работы с пользователем, используя созданную
     * сессию соединения с базой данных.
     * @param connection сессия соединения с базой данных
     */
    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Находит всех созданных пользователей. В случае выброса SQLException выводится содержимое исключения.
     * @return список из всех пользователей
     */
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM main.users";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("email").toLowerCase().replaceAll("\\s+", " ").trim(),
                        resultSet.getString("password"),
                        resultSet.getInt("role_id"));
                user.setActive(resultSet.getBoolean("is_active"));

                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
        return users;
    }
    /**
     * Находит все адреса электронной почты всех созданных пользователей. В случае выброса SQLException выводится содержимое исключения.
     * @return список из всех зарегистрированных адресов электронной почты
     */
    public List<String> getEmails() {
        List<String> emails = new ArrayList<>();
        String sql = "SELECT email FROM main.users";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                emails.add(resultSet.getString("email").toLowerCase().replaceAll("\\s+", " ").trim());
            }
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
        return emails;
    }

    /**
     * Добавляет новую запись в таблицу пользователей. Аккаунт пользователя всегда имеет роль 1 (обычный пользователь),
     * изначально активен: is_active = true. В случае выброса SQLException выводится содержимое исключения.
     * @param name имя пользователя
     * @param email адрес электронной почты пользователя; нормализуется в методе
     * @param password зашифрованный пароль пользователя
     * @param roleId роль пользователя (всегда 1, за исключением случая создания нового администратора)
     */
    public void addUser(String name, String email, String password, int roleId) {
        email = email.toLowerCase().replaceAll("\\s+", " ").trim();
        String sql = "INSERT INTO main.users (name, email, password, role_id, is_active) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setInt(4, roleId);
            statement.setBoolean(5, true);

            statement.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
    }

    /**
     * Находит пользователя по заданному адресу электронной почты. В случае выброса SQLException выводится содержимое исключения.
     * @param email адрес электронной почты пользователя; нормализуется в методе
     * @return объект User, если пользователь найден; null иначе
     */
    public User readUserByEmail(String email) {
        String normalizedEmail = email.toLowerCase().replaceAll("\\s+", " ").trim();

        String sql = "SELECT * FROM main.users WHERE users.email = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, normalizedEmail);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("email").toLowerCase().replaceAll("\\s+", " ").trim(),
                        resultSet.getString("password"),
                        resultSet.getInt("role_id"));
                user.setActive(resultSet.getBoolean("is_active"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
        return null;
    }

    /**
     * Удаляет пользователя по заданному адресу электронной почты. В случае выброса SQLException выводится содержимое исключения.
     * @param email адрес электронной почты пользователя; нормализуется в методе
     */

    public void deleteUserByEmail(String email) {
        email = email.toLowerCase().replaceAll("\\s+", " ").trim();
        String sql = "DELETE FROM main.users WHERE users.email = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, email.toLowerCase().replaceAll("\\s+", " ").trim());

            statement.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
    }

    /**
     * Находит id пользователя по заданному адресу электронной почты. В случае выброса SQLException выводится содержимое исключения.
     * @param email адрес электронной почты пользователя; нормализуется в методе
     * @return int id - id пользователя с заданным адресом электронной почты, если пользователь существует;
     * -1 иначе
     */
    public int findUserIdByEmail(String email) {
        email = email.toLowerCase().replaceAll("\\s+", " ").trim();
        String sql = "SELECT id FROM main.users WHERE users.email = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            int userId = -1;
            if (resultSet.next()){
               userId = resultSet.getInt("id");
            }
            return userId;

        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
        return -1;
    }

    /**
     * Обновляет имя заданного пользователя. В случае выброса SQLException выводится содержимое исключения.
     * @param newName новое имя для пользователя
     * @param user пользователь, для котрого обновляется имя
     */
    public void updateName(String newName, User user){
        String sql = "UPDATE main.users SET name = ? WHERE users.email = ?";

        executeNameOrPassUpdate(newName, user, sql);
    }

    /**
     * Обновляет адрес электронной почты пользователя. В случае выброса SQLException выводится содержимое исключения.
     * @param newEmail новая электронная почта пользователя
     * @param user пользователь, для которого обновляется электронная почта
     */
    public void updateEmail(String newEmail, User user){
        String sql = "UPDATE main.users SET email = ? WHERE users.email = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, newEmail.toLowerCase().replaceAll("\\s+", " ").trim());
            statement.setString(2, user.getEmail().toLowerCase().replaceAll("\\s+", " ").trim());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
    }
    /**
     * Присваиает пользователю новый зашифрованный пароль. В случае выброса SQLException выводится содержимое исключения.
     * @param newPass новый зашифрованный пароль
     * @param user пользователь, для которого обновляется пароль
     */
    public void updatePassword(String newPass, User user){
        String sql = "UPDATE main.users SET password = ? WHERE users.email = ?";

        executeNameOrPassUpdate(newPass, user, sql);
    }

    private void executeNameOrPassUpdate(String firstValue, User user, String sql) {
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, firstValue);
            statement.setString(2, user.getEmail().toLowerCase().replaceAll("\\s+", " ").trim());

            statement.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
    }

    /**
     * Изменяет статус активности аккаунта пользователя. В случае выброса SQLException выводится содержимое исключения.
     * @param isActive новый статус активности аккаунта пользователя
     * @param user пользователь, для которого обновлется статус
     */
    public void updateActive(boolean isActive, User user){
        String email = user.getEmail().toLowerCase().replaceAll("\\s+", " ").trim();

        String sql = "UPDATE main.users SET is_active = ? WHERE users.email = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setBoolean(1, isActive);
            statement.setString(2, email);

            statement.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
    }
}