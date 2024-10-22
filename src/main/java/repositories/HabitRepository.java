package repositories;

import models.Habit;
import models.User;
import serviceClasses.Config;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Репозиторий для работы с сущностью Habit.
 * <p>
 * * @author Gureva Anna
 * * @version 1.0
 * * @since 19.10.2024
 * </p>
 */
public class HabitRepository {
    private final Connection connection;

    private final UserRepository userRepository;

    /**
     * Конструктор для инициализации репозитория для работы с привычкой, используя
     * репозиторий для работы с пользователем, создавшим эту привычку.
     * Сессия для работы с базой данных создается внутри контроллера.
     *
     * @param userRepository репозиторий пользователя, создавшего привычку
     */
    public HabitRepository(UserRepository userRepository) {
        Config config = new Config();
        this.connection = (Connection) config.establishConnection()[0];
        this.userRepository = userRepository;
    }


    /**
     * Добавляет новую запись в таблицу привычек. В случае выброса SQLException выводится содержимое исключения.
     *
     * @param name        название привычки; нормализуется перед добавлением записи в таблицу
     * @param description описание для привычки
     * @param createdAt   время создания привычки
     * @param user        пользователь, создающий привычку; в методе используется его id, полученный через метод findUserIdByEmail из userRepository
     * @param frequency   частота выполнения привычки
     * @see UserRepository
     */
    public void addHabit(String name, String description, LocalDateTime createdAt, User user, Habit.Frequency frequency) throws SQLException {
        name = name.toLowerCase().replaceAll("\\s+", " ").trim();
        int userId = userRepository.findUserIdByEmail(user.getEmail());
        String sql = "INSERT INTO main.habits (name, description, created_at, user_id, frequency) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setString(2, description);
        statement.setTimestamp(3, Timestamp.valueOf(createdAt));
        statement.setInt(4, userId);
        statement.setString(5, frequency.toString());

        statement.executeUpdate();
        statement.close();
    }

    /**
     * Находит все привычки, созданные пользователем. В случае выброса SQLException выводится содержимое исключения.
     *
     * @param user пользователь, для которого нужно найти все привычки; в методе используется его id, полученный через метод findUserIdByEmail из userRepository
     * @return список всех привычек пользователя с имеющейся статистикой по каждой из них
     * @see UserRepository
     */
    public List<Habit> getAllHabitsByUser(User user) {
        List<Habit> userHabits = new ArrayList<>();
        int userId = userRepository.findUserIdByEmail(user.getEmail());
        String sql = "SELECT * FROM main.habits WHERE habits.user_id = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Habit.Frequency frequency = Habit.Frequency.valueOf(resultSet.getString("frequency"));
                String name = resultSet.getString("name");
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                Habit habit = new Habit(
                        name,
                        resultSet.getString("description"),
                        resultSet.getTimestamp("created_at").toLocalDateTime(),
                        user, frequency);
                habit.setStatistics(findHabitStatistics(habit.getName(), user));
                userHabits.add(habit);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
        return userHabits;
    }

    /**
     * Находит привычку заданного пользователя по имени привычки. В случае выброса SQLException выводится содержимое исключения.
     *
     * @param name название привычки; нормализуется в методе
     * @param user пользователь, для которого нужно найти привычку; в методе используется его id, полученный через метод findUserIdByEmail из userRepository
     * @return объект Habit в случае, если привычка найдена, null, если привычка не найдена
     * @see UserRepository
     */
    public Habit findHabitByName(String name, User user) {
        String normalizedName = name.toLowerCase().replaceAll("\\s+", " ").trim();

        int userId = userRepository.findUserIdByEmail(user.getEmail());

        String sql = "SELECT * FROM main.habits WHERE habits.name = ? AND habits.user_id = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, normalizedName);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Habit.Frequency frequency = Habit.Frequency.valueOf(resultSet.getString("frequency"));
                name = resultSet.getString("name");
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                Habit habit = new Habit(
                        name,
                        resultSet.getString("description"),
                        resultSet.getTimestamp("created_at").toLocalDateTime(),
                        user, frequency);

                habit.setStatistics(findHabitStatistics(habit.getName(), user));
                return habit;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
        return null;
    }

    /**
     * Изменяет название для заданной привычки заданного пользователя. В случае выброса SQLException выводится содержимое исключения.
     *
     * @param newName новое название привычки; нормализуется в методе
     * @param habit   привычка, для которой меняется название
     * @param user    пользователь, чья привычка изменяется; в методе используется его id, полученный через метод findUserIdByEmail из userRepository
     * @see UserRepository
     */
    public void updateHabitName(String newName, Habit habit, User user) throws SQLException {
        int userId = userRepository.findUserIdByEmail(user.getEmail());

        String normalizedName = newName.toLowerCase().replaceAll("\\s+", " ").trim();

        String sql = "UPDATE main.habits SET name = ? WHERE name = ? AND user_id = ?";

        executeUpdateStatement(normalizedName, habit, userId, sql);
    }

    /**
     * Изменяет описание для заданной привычки заданного пользователя. В случае выброса SQLException выводится содержимое исключения.
     *
     * @param description новое описание для привычки
     * @param habit       привычка, для которой меняется описание
     * @param user        пользователь, чья привычка изменяется; в методе используется его id, полученный через метод findUserIdByEmail из userRepository
     * @see UserRepository
     */
    public void updateHabitDescription(String description, Habit habit, User user) throws SQLException {
        int userId = userRepository.findUserIdByEmail(user.getEmail());

        String sql = "UPDATE main.habits SET description = ? WHERE name = ? AND user_id = ?";

        executeUpdateStatement(description, habit, userId, sql);
    }

    /**
     * Изменяет частоту для заданной привычки заданного пользователя. В случае выброса SQLException выводится содержимое исключения.
     *
     * @param frequency новая частота для привычки
     * @param habit     привычка, для которой меняется частота
     * @param user      пользователь, чья привычка изменяется; в методе используется его id, полученный через метод findUserIdByEmail из userRepository
     * @see UserRepository
     */
    public void updateHabitFrequency(Habit.Frequency frequency, Habit habit, User user) throws SQLException {
        int userId = userRepository.findUserIdByEmail(user.getEmail());


        String sql = "UPDATE main.habits SET frequency = ? WHERE name = ? AND user_id = ?";
        executeUpdateStatement(frequency.toString(), habit, userId, sql);
    }

    private void executeUpdateStatement(String firstValue, Habit habit, int userId, String sql) throws SQLException{
         PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setString(1, firstValue);
            statement.setString(2, habit.getName().toLowerCase().replaceAll("\\s+", " ").trim());
            statement.setInt(3, userId);

            statement.executeUpdate();
        statement.close();
    }

    /**
     * Удаляет привычку с заданным названием у заданного пользователя. В случае выброса SQLException выводится содержимое исключения.
     *
     * @param name название привычки, которую надо удалить; нормализуется в методе
     * @param user пользователь, чья привычка удалется; в методе используется его id, полученный через метод findUserIdByEmail из userRepository
     * @see UserRepository
     */
    public void deleteHabit(String name, User user) {
        int userId = userRepository.findUserIdByEmail(user.getEmail());


        String sql = "DELETE FROM main.habits WHERE name = ? AND user_id = ?";

        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, name.toLowerCase().replaceAll("\\s+", " ").trim());
            statement.setInt(2, userId);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
    }

    /**
     * Находит всю статистику по привычке с заданным названием для заданного пользователя. В случае выброса SQLException выводится содержимое исключения.
     *
     * @param habitName название привычки, по которой нужно найти статистику
     * @param user      пользователь, которому принадлежит привычка; в методе используется его id, полученный через метод findUserIdByEmail из userRepository
     * @return LinkedHashMap, ключ - дата отметки, значение - сама отметка (отмечено/не отмечено),
     * в котором в порядке добавления идут отметки привычки.
     * @see UserRepository
     */
    public LinkedHashMap<LocalDate, Boolean> findHabitStatistics(String habitName, User user) {
        String normalizedName = habitName.toLowerCase().replaceAll("\\s+", " ").trim();
        LinkedHashMap<LocalDate, Boolean> stats = new LinkedHashMap<>();
        int userId = userRepository.findUserIdByEmail(user.getEmail());
        String sql = "SELECT * FROM main.habit_stats WHERE habit_stats.habit_name = ? AND habit_stats.user_id = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, normalizedName);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                stats.put(resultSet.getTimestamp("date").toLocalDateTime().toLocalDate(), resultSet.getBoolean("mark"));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
        return stats;
    }

    /**
     * Добавляет запись об отметке привычки с заданным названием в таблицу со статистикой. В случае выброса SQLException выводится содержимое исключения.
     *
     * @param date      время отметки; для добавления в таблицу и приведению к Timestamp приводится к виду LocalDateTime(date, (0, 0, 0))
     * @param mark      отметка - отмечено (true) или не отмечено (false)
     * @param habitName название привычки, для которой добавляется отметка; нормализуется в методе
     * @param user      пользователь, которому принадлежит данная привычка; в методе используется его id, полученный через метод findUserIdByEmail из userRepository
     * @see UserRepository
     */
    public void addHabitStat(LocalDate date, boolean mark, String habitName, User user) {
        String sql = "INSERT INTO main.habit_stats (habit_name, user_id, date, mark) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, habitName.toLowerCase().replaceAll("\\s+", " ").trim());
            statement.setInt(2, userRepository.findUserIdByEmail(user.getEmail()));
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.of(date, LocalTime.of(0, 0, 0))));
            statement.setBoolean(4, mark);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
    }

    /**
     * Удаляет запись об отметке привычки с заданным названием. В случае выброса SQLException выводится содержимое исключения.
     *
     * @param date      время отметки; для нахождения в таблице и приведения к Timestamp приводится к виду LocalDateTime(date, (0, 0, 0))
     * @param habitName название привычки, для которой нужно удалить отметку; нормализуется в методе
     * @param user      пользователь, которому принадлежит данная привычка; в методе используется его id, полученный через метод findUserIdByEmail из userRepository
     * @see UserRepository
     */
    public void deleteHabitStat(LocalDate date, String habitName, User user) {
        String sql = "DELETE FROM main.habit_stats WHERE date = ? AND habit_name = ? and  user_id = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(date, LocalTime.of(0, 0, 0))));
            statement.setString(2, habitName.toLowerCase().replaceAll("\\s+", " ").trim());
            statement.setInt(3, userRepository.findUserIdByEmail(user.getEmail()));

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка! " + e);
        }
    }
}
