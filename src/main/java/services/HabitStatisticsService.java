package services;

import models.Habit;
import repositories.HabitRepository;
import models.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * Сервис для работы со статистикой сущности Habit
 * <p>
 * * @author Gureva Anna
 * * @version 1.0
 * * @since 19.10.2024
 * </p>
 */
public class HabitStatisticsService {
    private final HabitRepository repository;

    private final User user;

    /**
     * Конструктор для создания сервиса с заданным репозиторием по работе с привычками и заданным пользователем.
     * @param repository репозиторий для работы с привычками
     * @param user пользователь, с привычками которого ведется работа
     */
    public HabitStatisticsService(HabitRepository repository, User user) {
        this.repository = repository;
        this.user = user;
    }

    /**
     * Находит максимальный streak заданной привычки заданного пользователя за указанный период.
     *
     * @param habitName название привычки
     * @param period период, за который нужно найти streak (-1 = весь период отметки привычки)
     * @return длина максимального streak привычки
     */

    public int findLongestHabitStreak(String habitName, int period) {
        Habit examined = repository.findHabitByName(habitName, this.user);
        int maxStreak = 0;
        int currStreak = 0;

        if (period == -1 || period >= examined.getStatistics().size()) {
            for (Boolean value : examined.getStatistics().values()) {
                if (value) {
                    currStreak++;
                } else {
                    if (currStreak > maxStreak) {
                        maxStreak = currStreak;
                    }
                    currStreak = 0;
                }
            }
        } else {
            for (int i = examined.getStatistics().size() - 1; i >= examined.getStatistics().size() - period; i--) {
                if (new ArrayList<>(examined.getStatistics().values()).get(i)) {
                    currStreak++;
                } else {
                    if (currStreak > maxStreak) {
                        maxStreak = currStreak;
                    }
                    currStreak = 0;
                }
            }
        }
        if (currStreak > maxStreak) {
            maxStreak = currStreak;
        }
        return maxStreak;

    }
    /**
     * Находит последний (текущий) streak заданной привычки заданного пользователя за указанный период.
     *
     * @param habitName название привычки
     * @param period период, за который нужно найти streak (-1 = весь период отметки привычки)
     * @return длина текущего streak привычки
     */

    public int findLastHabitStreak(String habitName, int period) {
        Habit examined = repository.findHabitByName(habitName, this.user);
        int currStreak = 0;
        List<Boolean> values = new ArrayList<>(examined.getStatistics().values());
        if (period == -1 || period >= examined.getStatistics().size()) {
            for (int i = values.size() - 1; i >= 0; i--) {
                if (values.get(i)) {
                    currStreak++;
                } else {
                    break;
                }
            }
        } else {
            for (int i = values.size() - 1; i >= values.size() - period; i--) {
                if (values.get(i)) {
                    currStreak++;
                } else {
                    break;
                }
            }
        }
        return currStreak;
    }


    /**
     * Вычисляет выполнение заданной привычки заданного пользователя за указанный период.
     *
     * @param period период, за который надо рассчитать выполнение (-1 = весь период отметки привычки)
     * @return Double[], где первое значение - количество дней, в которые привычка была отмечена за период;
     * второе значение - выполнение привычки в процентах (количество дней с отметками*100/весь заданный переиод отслеживания)
     */
    public Double[] getHabitCompleteness(String habitName, int period) {
        int CODE_FOR_ALL = -1;

        Habit examined = repository.findHabitByName(habitName, this.user);
        if (period > examined.getStatistics().size()) {
            period = CODE_FOR_ALL;
        }
        double markedDays = 0;
        List<Boolean> values = new ArrayList<>(examined.getStatistics().values());
        if (period == CODE_FOR_ALL) {
            for (int i = values.size() - 1; i >= 0; i--) {
                if (values.get(i)) {
                    markedDays++;
                }
            }
            return new Double[]{markedDays, markedDays * 100.0 / values.size()};
        } else {
            for (int i = values.size() - 1; i >= values.size() - period; i--) {
                if (values.get(i)) {
                    markedDays++;
                }
            }
            return new Double[]{markedDays, markedDays * 100.0 / period};
        }
    }

    /**
     * Получает общее количество дней, в которые отмечались все привычки заданного пользователя в заданный период.
     * @param period период для отслеживания (-1 = весь период)
     * @return суммарное количество дней с отметками по всем привычкам за период
     */

    private int getAllHabitsCompleteness(int period) {
        List<Habit> habits = repository.getAllHabitsByUser(this.user);
        int overallMarked = 0;
        for (Habit habit : habits) {
            overallMarked += getHabitCompleteness(habit.getName(), period)[0];
        }
        return overallMarked;
    }

    /**
     * Формирует отчет по всем привычкам заданного пользователя за заданный период, в который входят:
     * для каждой привычки:
     * -количество отметок за период;
     * -процент отметок за период;
     * -максимальный streak за период;
     * -длина текущего streak за период;
     * для всех привычек:
     * -суммарное количество дней с отметками по всем привычкам за период
     * -общий процент отметок всех привычек за период.
     * @param period период, за который нужно сформировать отчет по привычкам
     *              (-1 = период с начала отслеживания каждой привычки)
     * @return массив Object, в котором первый элемент - Map, где ключ - название привычки, значение - массив Double,
     * содержащий количество отметок, процент отметок, максимальный streak и текущий streak за период для этой привычки;
     * второй элемент - суммарное количество дней с отметками по всем привычкам за период;
     * третий элемент - общий процент отметок всех привычек за период.
     */
    public Object[] formAllHabitsReport(int period) {
        Map<String, double[]> report = new HashMap<>();
        List<Habit> habits = repository.getAllHabitsByUser(this.user);
        if (habits.isEmpty()) {
            String message = "Ни одной привычки еще не было создано!";
            return new Object[]{message};
        }
        if (period == -1) {
            period = habits.get(0).getStatistics().size();
        }
        for (Habit habit : habits) {
            double completeness_int = getHabitCompleteness(habit.getName(), period)[0];
            double completeness_percentage = getHabitCompleteness(habit.getName(), period)[1];
            double longestStreak = findLongestHabitStreak(habit.getName(), period);
            double currStreak = findLastHabitStreak(habit.getName(), period);


            double[] habitData = new double[]{completeness_int, completeness_percentage, longestStreak, currStreak};
            report.put(habit.getName(), habitData);
        }

        int overallCompleteness = getAllHabitsCompleteness(period);
        double overallCompleteness_percentage = (getAllHabitsCompleteness(period) * 100.0) / (period * habits.size());

        return new Object[]{report, overallCompleteness, overallCompleteness_percentage};
    }
    /**
     * Запускает планировщик для ежедневного выставления
     * дефолтной отметки false для заданной привычки заданного пользователя.
     *
     * @param habitName название привычки, для которой инициализируется планировщик
     */
    private void scheduleDailyUpdate(String habitName) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        long initialDelay = calculateInitialDelay();
        long period = 24 * 60 * 60;

        scheduler.scheduleAtFixedRate(() -> repository.addHabitStat(LocalDate.now(), false, habitName, this.user), initialDelay, period, TimeUnit.SECONDS);
    }

    /**
     * Рассчитывает задержку для следующего запуска планировщика в 0:00:00 следующего дня.
     *
     * @return задержка в секундах
     */
    private long calculateInitialDelay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(1);

        return java.time.Duration.between(now, nextRun).getSeconds();
    }

    /**
     * Инициализирует планировщик для всех привычек заданного пользователя.
     */
    public void initAllHabitsScheduler() {
        List<Habit> userHabits = repository.getAllHabitsByUser(this.user);
        for(Habit habit: userHabits){
            scheduleDailyUpdate(habit.getName());
        }
    }
}
