package HabitTests.serviceTest;

import Habit.model.Habit;
import Habit.repository.HabitRepository;
import Habit.service.HabitService;
import Habit.service.HabitStatisticsService;
import User.model.User;
import User.repository.UserRepository;
import User.service.UserService;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class HabitStatisticsServiceTests {
    HabitRepository repo = new HabitRepository();
    User user = new User("anya", "anya@ya.ru", "1234", 1);
    UserService userService = new UserService(user, new UserRepository());
    HabitService service = new HabitService(repo, user, userService);
    HabitStatisticsService habitStatisticsService = new HabitStatisticsService(repo, user);

    public void createUser() {
        userService.createUser("anya", "anya@ya.ru", "1234");
        userService.readUser_byEmail("anya@ya.ru");
    }

    public LinkedHashMap<LocalDate, Boolean> createWeekStreak_dailyHabit(String name) {

        service.createHabit(name, "", Habit.Frequency.DAILY);
        LocalDate before = LocalDate.now().minusWeeks(1);

        Habit marked = repo.findHabitByName(name, this.user);
        marked.getStatistics().put(before, true);

        for (int i = 0; i < 7; i++) {
            before = before.plusDays(1);
            marked.getStatistics().put(before, true);
        }
        return marked.getStatistics();
    }

    public LinkedHashMap<LocalDate, Boolean> createTwoWeeksStreak_weeklyHabit(String name) {
        createUser();
        service.createHabit(name, "", Habit.Frequency.WEEKLY);
        LocalDate before = LocalDate.now().minusWeeks(2);

        Habit marked = repo.findHabitByName(name, this.user);
        marked.getStatistics().put(before, true);

        for (int i = 0; i < 2; i++) {
            before = before.plusWeeks(1);
            marked.getStatistics().put(before, true);
        }
        return marked.getStatistics();
    }

    public LinkedHashMap<LocalDate, Boolean> createInconsistentWeek_dailyHabit(String name) {
        service.createHabit(name, "", Habit.Frequency.DAILY);
        LocalDate before = LocalDate.now().minusWeeks(1);

        Habit marked = repo.findHabitByName(name, this.user);
        marked.getStatistics().put(before, true);

        for (int i = 0; i < 7; i++) {
            before = before.plusDays(1);
            if (i % 2 == 0) {
                marked.getStatistics().put(before, true);
            } else {
                marked.getStatistics().put(before, false);
            }
        }
        return marked.getStatistics();
    }

    @Test
    public void formStatistics() {
        LinkedHashMap<LocalDate, Boolean> expected = createWeekStreak_dailyHabit("Пить воду");
        LinkedHashMap<LocalDate, Boolean> actual = habitStatisticsService.formStatistics("Пить воду");
        assertEquals(expected, actual);
    }


    @Test
    public void findLongestStreak_daily() {
        int expected = 8;
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit habit = repo.findHabitByName("Пить воду", this.user);
        habit.setStatistics(createWeekStreak_dailyHabit("Пить воду"));
        int actual = habitStatisticsService.findLongestHabitStreak(habit.getName(), -1);
        assertEquals(expected, actual);
    }

    @Test
    public void findLastStreak_streakWeek_daily() {
        int expected = 8;
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit habit = repo.findHabitByName("Пить воду", this.user);
        habit.setStatistics(createWeekStreak_dailyHabit("Пить воду"));
        int actual = habitStatisticsService.findLastHabitStreak(habit.getName(), -1);
        assertEquals(expected, actual);
    }

    @Test
    public void findLongestStreak_weekly() {
        int expected = 3;
        service.createHabit("Пить воду", "", Habit.Frequency.WEEKLY);
        Habit habit = repo.findHabitByName("Пить воду", this.user);
        habit.setStatistics(createTwoWeeksStreak_weeklyHabit("Пить воду"));
        int actual = habitStatisticsService.findLongestHabitStreak(habit.getName(), -1);
        assertEquals(expected, actual);
    }

    @Test
    public void findLastStreak_streakWeek_weekly() {
        int expected = 3;
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit habit = repo.findHabitByName("Пить воду", this.user);
        habit.setStatistics(createTwoWeeksStreak_weeklyHabit("Пить воду"));
        int actual = habitStatisticsService.findLastHabitStreak(habit.getName(), -1);
        assertEquals(expected, actual);
    }

    @Test
    public void findLongestStreak_daily_incons() {
        int expected = 2;//за счет того, что один элемент статистики добавляется вне цикла
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit habit = repo.findHabitByName("Пить воду", this.user);
        habit.setStatistics(createInconsistentWeek_dailyHabit("Пить воду"));
        int actual = habitStatisticsService.findLongestHabitStreak(habit.getName(), -1);
        assertEquals(expected, actual);
    }

    @Test
    public void findLastStreak_streakWeek_daily_incons() {
        int expected = 1;
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit habit = repo.findHabitByName("Пить воду", this.user);
        habit.setStatistics(createInconsistentWeek_dailyHabit("Пить воду"));
        int actual = habitStatisticsService.findLastHabitStreak(habit.getName(), -1);
        assertEquals(expected, actual);
    }

    @Test
    public void getHabitCompleteness_streak1() {
        double expected = 100.0;
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit habit = repo.findHabitByName("Пить воду", this.user);
        habit.setStatistics(createWeekStreak_dailyHabit("Пить воду"));

        double actual = habitStatisticsService.getHabitCompleteness(habit.getName(), -1)[1];

        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void getHabitCompleteness_streak2() {
        double expected = 100.0;
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit habit = repo.findHabitByName("Пить воду", this.user);
        habit.setStatistics(createWeekStreak_dailyHabit("Пить воду"));

        double actual = habitStatisticsService.getHabitCompleteness(habit.getName(), 5)[1]; //за 5 последних дней

        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void getHabitCompleteness_streak3() {
        double expected = 100.0;
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit habit = repo.findHabitByName("Пить воду", this.user);
        habit.setStatistics(createWeekStreak_dailyHabit("Пить воду"));

        double actual = habitStatisticsService.getHabitCompleteness(habit.getName(), 15)[1]; //больше, чем дней, за которые отслеживается привычка

        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void getHabitCompleteness_incons_allperiod() {
        double expected = 62.5;
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit habit = repo.findHabitByName("Пить воду", this.user);
        habit.setStatistics(createInconsistentWeek_dailyHabit("Пить воду"));


        double actual = habitStatisticsService.getHabitCompleteness(habit.getName(), -1)[1]; //больше, чем дней, за которые отслеживается привычка

        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void getHabitCompleteness_incons_5lastDays() {
        double expected = 60.0;
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit habit = repo.findHabitByName("Пить воду", this.user);
        habit.setStatistics(createInconsistentWeek_dailyHabit("Пить воду"));

        double actual = habitStatisticsService.getHabitCompleteness(habit.getName(), 5)[1]; //больше, чем дней, за которые отслеживается привычка

        assertEquals(expected, actual, 0.01);
    }

    @Test
    public void formAllHabitsReport_allPeriod() {


        Object[] expected = new Object[3];
        Map<String, double[]> report = new HashMap<>();
        report.put("Вставать в 5 утра", new double[]{5.0, 62.5, 2.0, 1.0});
        report.put("Пить воду", new double[]{8.0, 100.0, 8.0, 8.0});
        expected[0] = report;
        expected[1] = 13;
        expected[2] = 81.25;

        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit habit1 = repo.findHabitByName("Пить воду", this.user);
        habit1.setStatistics(createWeekStreak_dailyHabit("Пить воду"));

        service.createHabit("Вставать в 5 утра", "", Habit.Frequency.DAILY);
        Habit habit2 = repo.findHabitByName("Вставать в 5 утра", this.user);
        habit2.setStatistics(createInconsistentWeek_dailyHabit("Вставать в 5 утра"));

        Object[] actual = habitStatisticsService.formAllHabitsReport(-1);

        Map<String, double[]> expectedReport = (Map<String, double[]>) expected[0];
        Map<String, double[]> actualReport = (Map<String, double[]>) actual[0];

        assertEquals(expectedReport.size(), actualReport.size());

        for (String key : expectedReport.keySet()) {
            assertTrue(actualReport.containsKey(key));
            assertArrayEquals(expectedReport.get(key), actualReport.get(key), 0.01);
        }

        assertEquals(expected[1], actual[1]);


        assertEquals((double) expected[2], (double) actual[2], 0.01);
    }

    @Test
    public void formAllHabitsReport_5lastDays() {
        Object[] expected = new Object[3];
        Map<String, double[]> report = new HashMap<>();
        report.put("Вставать в 5 утра", new double[]{3.0, 60.0, 1.0, 1.0});
        report.put("Пить воду", new double[]{5.0, 100.0, 5.0, 5.0});
        expected[0] = report;
        expected[1] = 8;
        expected[2] = 80.0;

        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit habit1 = repo.findHabitByName("Пить воду", this.user);
        habit1.setStatistics(createWeekStreak_dailyHabit("Пить воду"));

        service.createHabit("Вставать в 5 утра", "", Habit.Frequency.DAILY);
        Habit habit2 = repo.findHabitByName("Вставать в 5 утра", this.user);
        habit2.setStatistics(createInconsistentWeek_dailyHabit("Вставать в 5 утра"));

        Object[] actual = habitStatisticsService.formAllHabitsReport(5);

        Map<String, double[]> expectedReport = (Map<String, double[]>) expected[0];
        Map<String, double[]> actualReport = (Map<String, double[]>) actual[0];

        assertEquals(expectedReport.size(), actualReport.size());

        for (String key : expectedReport.keySet()) {
            assertTrue(actualReport.containsKey(key));
            assertArrayEquals(expectedReport.get(key), actualReport.get(key), 0.01);
        }

        assertEquals(expected[1], actual[1]);


        assertEquals((double) expected[2], (double) actual[2], 0.01);
    }

    @Test
    public void formAllHabitsReport_noHabits() {
        Object[] res = habitStatisticsService.formAllHabitsReport(-1);
        String expected = "Ни одной привычки еще не было создано!";
        assertEquals(expected, res[0]);
    }
}
