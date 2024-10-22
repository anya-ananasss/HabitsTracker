package services;

import models.Habit;
import repositories.HabitRepository;
import models.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Сервис для работы с сущностью Habit
 * <p>
 * * @author Gureva Anna
 * * @version 1.0
 * * @since 19.10.2024
 * </p>
 */
public class HabitService {

    private final HabitRepository repository;

    private final User user;

    /**
     * Конструктор для создания сервиса с заданным репозиторием по работе с привычками и заданным пользователем.
     *
     * @param repository репозиторий для работы с привычками
     * @param user       пользователь, с привычками которого ведется работа
     */
    public HabitService(HabitRepository repository, User user) {
        this.repository = repository;
        this.user = user;
    }

    /**
     * Создает новую привычку с заданным названием, описанием и частотой для заданного пользователя.
     * @param name название привычки
     * @param description описание привычки
     * @param frequency частота выполнения привычки
     * @return сообщение об успешном создании привычки
     */
    public String createHabit(String name, String description, Habit.Frequency frequency) throws SQLException {
        LocalDateTime createdAt = LocalDateTime.now();
        repository.addHabit(name, description, createdAt, this.user, frequency);


        return "Привычка успешно создана!";
    }

    /**
     * Создает новую привычку с заданным названием, описанием, временем создания и частотой для заданного пользователя.
     *
     * @param name        название привычки
     * @param description описание привычки
     * @param createdAt   время создания привычки
     * @param frequency   частота привычки
     */
    public void createHabit(String name, String description, LocalDateTime createdAt, Habit.Frequency frequency) throws SQLException {
        repository.addHabit(name, description, createdAt, this.user, frequency);
    }

    /**
     * Находит привычку по ее названию у заданного пользователя.
     *
     * @param name название привычки
     * @return Habit, если привычка найдена; null иначе
     */
    public Habit findHabitByName(String name) {
        return repository.findHabitByName(name.toLowerCase().replaceAll("\\s+", " ").trim(), this.user);
    }

    /**
     * Проверяет, существует ли привычка с заданным названием у заданного пользователя.
     *
     * @param name название привычки
     * @return true, если привычка с таким названием существует; иначе false
     */
    public boolean nameExists(String name) {
        String normalizedName = name.toLowerCase().replaceAll("\\s+", " ").trim();
        return repository.findHabitByName(normalizedName, this.user) != null;
    }

    /**
     * Обновляет название привычки у заданного пользователя.
     *
     * @param newName новое нащвание привычки
     * @param oldName старое название привычки
     * @return сообщение о результате обновления названия
     */
    public String updateName(String newName, String oldName) throws SQLException {
        Habit manipulated = repository.findHabitByName(oldName, this.user);
        if (newName.isEmpty()) {
            return "Название не может быть пустым!";
        } else {
            if (user.getHabits().containsKey(newName)) {
                return "Привычка с таким названием уже существует!";
            } else {
                repository.updateHabitName(newName, manipulated, this.user);
                return "Успешно обновлено!";
            }
        }
    }

    /**
     * Обновляет описание привычки с заданным названием у заданного пользователя.
     *
     * @param newDescription новое описание привычки
     * @param habitName      название привычки
     * @return сообщение о результате обновления описания
     */
    public String updateDescription(String newDescription, String habitName) throws SQLException {
        Habit manipulated = repository.findHabitByName(habitName, this.user);
        if (newDescription.toCharArray().length > 200) {
            return "Описание не должно превышать 200 символов!";
        } else {
            repository.updateHabitDescription(newDescription, manipulated, this.user);
            return "Описание обновлено!";
        }
    }

    /**
     * Обновляет частоту привычки с заданным названием у заданного пользователя.
     *
     * @param newFrequency новая частота привычки
     * @param habitName    название привычки
     * @return сообщение о результате обновления частоты
     */
    public String updateFrequency(Habit.Frequency newFrequency, String habitName) throws SQLException {
        Habit manipulated = repository.findHabitByName(habitName, this.user);
        repository.updateHabitFrequency(newFrequency, manipulated, this.user);
        return "Успешно обновлено!";
    }

    /**
     * Сортирует привычки пользователя по дате создания (сначала новые)
     *
     * @return отсортированный список привычек
     */
    public List<Habit> filterByCreationDateNewestFirst() {
        List<Habit> habits = repository.getAllHabitsByUser(this.user);
        return sortHabitsByCreation(habits, true);
    }

    /**
     * Сортирует привычки пользователя по дате создания (сначала старые)
     *
     * @return отсортированный список привычек
     */
    public List<Habit> filterByCreationDateLatestFirst() {
        List<Habit> habits = repository.getAllHabitsByUser(this.user);
        return sortHabitsByCreation(habits, false);
    }

    /**
     * Сортирует список привычек пользователя по дате создания при помощи Comparator.
     *
     * @param habits    список привычек пользователя
     * @param ascending true - сначала новые, false - стачала старые
     * @return отсортированный список привычек
     */
    public List<Habit> sortHabitsByCreation(List<Habit> habits, boolean ascending) {

        habits.sort(Comparator.comparing(Habit::getCreatedAt));

        if (!ascending) {
            Collections.reverse(habits);
        }

        return habits;
    }

    /**
     * Получает последнюю дату, в которую для данной привычки данного пользователя была поставлена отметка (mark=true).
     *
     * @param habit привычка, для которой нужно получить дату последней отметки
     * @return дата последней отметки
     */
    public LocalDate getLatestTrueDate(Habit habit) {
        LinkedHashMap<LocalDate, Boolean> statistics = habit.getStatistics();
        LocalDate latestDate = null;

        for (Map.Entry<LocalDate, Boolean> entry : statistics.entrySet()) {
            if (entry.getValue() && (latestDate == null || entry.getKey().isAfter(latestDate))) {
                latestDate = entry.getKey();
            }
        }
        return latestDate;
    }

    /**
     * Сортирует привычки пользователя по последней дате их выполнения.
     *
     * @param recentlyMarked true для сортировки от более поздних по отметке (недавно отмеченных) к давно отмеченным, false — наоборот
     * @return LinkedHashMap отсортированных привычек, где ключ - название привычки, значение - дата последней отметки
     */
    public LinkedHashMap<String, LocalDate> sortHabitsByMarked(boolean recentlyMarked) {
        List<Habit> habits = repository.getAllHabitsByUser(this.user);
        LinkedHashMap<String, LocalDate> habitsSorted = new LinkedHashMap<>();
        List<Habit> habitsWithDates = new ArrayList<>();
        List<Habit> habitsWithoutDates = new ArrayList<>();

        for (Habit habit : habits) {
            LocalDate latestTrueDate = getLatestTrueDate(habit);
            if (latestTrueDate != null) {
                habitsWithDates.add(habit);
                habitsSorted.put(habit.getName(), latestTrueDate);
            } else {
                habitsWithoutDates.add(habit);
            }
        }

        habitsWithDates.sort((h1, h2) -> {
            LocalDate date1 = getLatestTrueDate(h1);
            LocalDate date2 = getLatestTrueDate(h2);
            if (recentlyMarked) {
                return date2.compareTo(date1);
            } else {
                return date1.compareTo(date2);
            }
        });

        habitsSorted.clear();

        if (!recentlyMarked) {
            for (Habit habit : habitsWithoutDates) {
                habitsSorted.put(habit.getName(), null);
            }
        }

        for (Habit habit : habitsWithDates) {
            habitsSorted.put(habit.getName(), getLatestTrueDate(habit));
        }

        if (recentlyMarked) {
            for (Habit habit : habitsWithoutDates) {
                habitsSorted.put(habit.getName(), null);
            }
        }

        return habitsSorted;
    }

    /**
     * Удаляет привычку с заданным названием у заданного пользователя.
     *
     * @param deletingName название привычки для удаления
     */
    public void deleteHabit(String deletingName) {
        repository.deleteHabit(deletingName, this.user);
    }

    /**
     * Отмечает привычку с заданным названием у заданного пользователя. В зависимости от указанной в привычке периодичности отметок
     * проверяет, пришло ли время для новой отметки, отмечает, если да, иначе - не отмечает.
     *
     * @param habitName название отмечаемой привычки
     * @return сообщение об успешной отметки или о том, что еще не нужно отмечать привычку в силу заданной периодичности привычки
     */

    public String markHabit(String habitName) {
        Habit marking = repository.findHabitByName(habitName, this.user);

        Habit.Frequency frequency = marking.getFrequency();
        if (!marking.getStatistics().isEmpty()) {
            LocalDate lastDate = LocalDate.now();

            for (LocalDate date : marking.getStatistics().keySet()) {
                if (marking.getStatistics().get(date)) {
                    lastDate = date;
                }
            }
            LocalDate today = LocalDate.now();
            boolean doMark = true;

            String sentenceEnd = "сегодня";
            LocalDate minused;
            switch (frequency) {
                case DAILY:
                    if (today.isEqual(lastDate)) {
                        doMark = false;
                    }
                    break;
                case WEEKLY:
                    minused = today.minusWeeks(1);
                    if (!lastDate.isBefore(minused)) {
                        doMark = false;
                        sentenceEnd = "на этой неделе";
                    }
                    break;
                case FORTNIGHTLY:
                    minused = today.minusWeeks(2);
                    if (!lastDate.isBefore(minused)) {
                        doMark = false;
                        sentenceEnd = "в последние две недели";
                    }
                    break;
                case EVERYTHREEWEEKS:
                    minused = today.minusWeeks(31);
                    if (!lastDate.isBefore(minused)) {
                        doMark = false;
                        sentenceEnd = "в последние три недели";
                    }
                    break;
                case MONTHLY:
                    minused = today.minusMonths(1);
                    if (!lastDate.isBefore(minused)) {
                        doMark = false;
                        sentenceEnd = "в этом месяце";
                    }
                    break;
            }
            if (doMark) {
                repository.deleteHabitStat(LocalDate.now(), habitName, this.user);
                repository.addHabitStat(LocalDate.now(), true, habitName, this.user);
                return "Отметка проставлена!";
            } else {
                return "Еще не время! Вы уже отмечались " + sentenceEnd + ".";
            }
        } else {
            repository.deleteHabitStat(LocalDate.now(), habitName, this.user);
            repository.addHabitStat(LocalDate.now(), true, habitName, this.user);
            return "Отметка проставлена!";
        }
    }
}