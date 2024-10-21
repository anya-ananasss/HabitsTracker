package HabitTests.controllerTest;

import controllers.HabitController;
import models.Habit;
import repositories.HabitRepository;
import services.HabitService;
import services.HabitStatisticsService;
import controllers.UserController;
import models.User;
import repositories.UserRepository;
import services.UserService;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class HabitControllerTests {
    private final HabitRepository repo = new HabitRepository();
    User user = new User("anya", "anya@ya.ru", "1234", 1);
    private final UserRepository userRepository = new UserRepository();
    private final UserService userService = new UserService(userRepository);
    private final UserController userController = new UserController(userService);
    private final HabitService service = new HabitService(repo, user, userService);
    private final HabitStatisticsService statService = new HabitStatisticsService(repo, user);

    public void addUser() {
        userRepository.addUser(user);
    }

    @Test
    public void showHabits_void_1() {
        addUser();
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        HabitController controller = new HabitController(service, statService, userController);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.showHabits(true);
        String expectedMessage = "Привычки не были созданы!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showHabits_void_2() {
        addUser();
        String input = "2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        HabitController controller = new HabitController(service, statService, userController);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.showHabits(true);
        String expectedMessage = "Привычки не были созданы!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showHabits_void_3() {
        addUser();
        String input = "3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        HabitController controller = new HabitController(service, statService, userController);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.showHabits(true);
        String expectedMessage = "Привычки не были созданы!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showHabits_void_4() {
        addUser();
        String input = "4\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        HabitController controller = new HabitController(service, statService, userController);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.showHabits(true);
        String expectedMessage = "Привычки не были созданы!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showHabits_1() throws InterruptedException {
        addUser();
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Habit habit1 = new Habit("Пить воду", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
        repo.addHabit(habit1);
        user.getHabits().put(habit1.getName(), habit1);
        Thread.sleep(5000);
        Habit habit2 = new Habit("Вставать в 5 утра", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
        repo.addHabit(habit2);
        user.getHabits().put(habit2.getName(), habit2);

        List<Habit> res = service.readUserHabits_filterByCreationDate_earilerLast();

        controller.showHabits(false);


        String output = outContent.toString();
        StringBuilder actualBuilder = new StringBuilder();
        for (int i = 0; i < res.size(); i++) {
            actualBuilder.append(i + 1).append(". ").append(res.get(i).getName()).append("\r\n");
        }
        String expectedMessage = actualBuilder.toString();


        assertTrue(output.contains(expectedMessage));

        System.setOut(System.out);
        System.setIn(System.in);

    }

    @Test
    public void showHabits_2() throws InterruptedException {
        addUser();
        String input = "2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Habit habit1 = new Habit("Пить воду", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
        repo.addHabit(habit1);
        user.getHabits().put(habit1.getName(), habit1);
        Thread.sleep(5000);
        Habit habit2 = new Habit("Вставать в 5 утра", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
        repo.addHabit(habit2);
        user.getHabits().put(habit2.getName(), habit2);

        List<Habit> res = service.readUserHabits_filterByCreationDate_earilerFirst();

        controller.showHabits(false);


        String output = outContent.toString();
        StringBuilder actualBuilder = new StringBuilder();
        for (int i = 0; i < res.size(); i++) {
            actualBuilder.append(i + 1).append(". ").append(res.get(i).getName()).append("\r\n");
        }
        String expectedMessage = actualBuilder.toString();


        assertTrue(output.contains(expectedMessage));

        System.setOut(System.out);
        System.setIn(System.in);

    }

    @Test
    public void showHabits_3() throws InterruptedException {
        addUser();
        String input = "3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        HabitController controller = new HabitController(service, statService, userController);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));


        Habit habit1 = new Habit("Пить воду", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
        repo.addHabit(habit1);
        user.getHabits().put(habit1.getName(), habit1);
        Thread.sleep(5000);
        Habit habit2 = new Habit("Вставать в 5 утра", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
        repo.addHabit(habit2);
        user.getHabits().put(habit2.getName(), habit2);

        service.markHabit("Пить воду");
        habit2.getStatistics().put(LocalDate.now(), false);

        LinkedHashMap<String, LocalDate> res = service.sortHabits_marked(true);

        controller.showHabits(false);

        StringBuilder builder = new StringBuilder();
        int index = 1;
        for (Map.Entry<String, LocalDate> entry : res.entrySet()) {
            LocalDate value = entry.getValue();
            String val;
            if (value == null) {
                val = "-";
            } else {
                val = value.toString();
            }
            builder.append(index).append(". ").append(entry.getKey()).append(", ").append(val).append("\r\n");
            index++;
        }
        String expectedMessage = builder.toString();
        String output = outContent.toString();


        assertTrue(output.contains(expectedMessage));

        System.setOut(System.out);
        System.setIn(System.in);


    }

    @Test
    public void showHabits_4() throws InterruptedException {
        addUser();
        String input = "4\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        HabitController controller = new HabitController(service, statService, userController);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));


        Habit habit1 = new Habit("Пить воду", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
        repo.addHabit(habit1);
        user.getHabits().put(habit1.getName(), habit1);
        Thread.sleep(5000);
        Habit habit2 = new Habit("Вставать в 5 утра", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
        repo.addHabit(habit2);
        user.getHabits().put(habit2.getName(), habit2);

        service.markHabit("Пить воду");
        habit2.getStatistics().put(LocalDate.now(), false);

        LinkedHashMap<String, LocalDate> res = service.sortHabits_marked(false);

        controller.showHabits(false);

        StringBuilder builder = new StringBuilder();
        int index = 1;
        for (Map.Entry<String, LocalDate> entry : res.entrySet()) {
            LocalDate value = entry.getValue();
            String val;
            if (value == null) {
                val = "-";
            } else {
                val = value.toString();
            }
            builder.append(index).append(". ").append(entry.getKey()).append(", ").append(val).append("\r\n");
            index++;
        }
        String expectedMessage = builder.toString();
        String output = outContent.toString();


        assertTrue(output.contains(expectedMessage));

        System.setOut(System.out);
        System.setIn(System.in);

    }

    @Test
    public void showHabits_opinionMismatch() throws InterruptedException {
        addUser();
        String input = "34567\n4\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);


        HabitController controller = new HabitController(service, statService, userController);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));


        Habit habit1 = new Habit("Пить воду", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
        repo.addHabit(habit1);
        user.getHabits().put(habit1.getName(), habit1);
        Thread.sleep(5000);
        Habit habit2 = new Habit("Вставать в 5 утра", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
        repo.addHabit(habit2);
        user.getHabits().put(habit2.getName(), habit2);

        service.markHabit("Пить воду");
        habit2.getStatistics().put(LocalDate.now(), false);

        service.sortHabits_marked(false);

        controller.showHabits(false);

        String expectedMessage = "Пожалуйста, введите 1, 2, 3, 4 или 5.";
        String output = outContent.toString();


        assertTrue(output.contains(expectedMessage));

        System.setOut(System.out);
        System.setIn(System.in);

    }

    @Test
    public void showSettings_void_2() {
        addUser();
        String input = "2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        HabitController controller = new HabitController(service, statService, userController);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.showSettings();
        String expectedMessage = "Ни одной привычки не было создано!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showSettings_void_3() {
        addUser();
        String input = "3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        HabitController controller = new HabitController(service, statService, userController);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.showSettings();
        String expectedMessage = "Ни одной привычки не было создано!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void basicCreateHabit() {
        addUser();

        String input = "Пить воду\n\nраз в Две недели\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        userService.setUser(user);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.createHabit();
        String expectedMessage = "Привычка успешно создана!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void createHabit_nonUnique_diffCase() {
        addUser();

        String input = "Пить воду\n\nежедневно\nпить воду\nНе пить воду\n\nежемесячно";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        userService.setUser(user);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.createHabit();
        controller.createHabit();
        String expectedMessage = "Привычка с таким названием уже существует!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void updateHabit_nonExistent_plusName() {
        addUser();

        String input = "Пить воду\n\nеженедельно\nНе пить воду\n1\nПить воду\n1\nМного воды пить";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        userService.setUser(user);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.createHabit();
        controller.updateHabit();

        String expectedMessage1 = "Такая привычка не найдена!";
        String expectedMessage2 = "Успешно обновлено!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage1));
        assertTrue(output.contains(expectedMessage2));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void basicUpdateHabit_descr() {
        addUser();

        String input = "Пить воду\n\nРаз в    три недели\nПить воду \n2\nВода вкусная\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        userService.setUser(user);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.createHabit();
        controller.updateHabit();
        String expectedMessage = "Описание обновлено!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void basicUpdateHabit_freq() {
        addUser();

        String input = "Пить воду\n\nежедневно\nПить воду\n3\nЕжедневно\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        userService.setUser(user);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.createHabit();
        controller.updateHabit();
        String expectedMessage = "Успешно обновлено!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void deleteUnexistent_plusBasicDelete() {
        addUser();

        String input = "Не пить воду\n\nЕжедневно\nПить воду\n1\nНе пить воду\nда";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        userService.setUser(user);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.createHabit();
        controller.deleteHabit();


        String expectedMessage1 = "Такая привычка не найдена!";
        String expectedMessage2 = "Успешно удалено.";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage1));
        assertTrue(output.contains(expectedMessage2));
        System.setOut(System.out);
        System.setIn(System.in);

    }

    @Test
    public void markHabit_unsuccessAndSuccess() {
        addUser();

        String input = "Не пить воду\n\nЕжедневно\nПить воду\n1\nНе пить воду\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        userService.setUser(user);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        controller.createHabit();
        controller.markHabit();


        String expectedMessage1 = "Такая привычка не найдена!";
        String expectedMessage2 = "Отметка проставлена!";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage1));
        assertTrue(output.contains(expectedMessage2));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showSingleHabitStats_unsuccessAndSuccess_bestStreak() {
        addUser();

        String input = "Пить воду\n1\nНе пить воду\nall\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        userService.setUser(user);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        createWeekStreak_dailyHabit("Не пить воду");

        controller.showSingleHabitStats();

        String expectedMessage1 = "Такая привычка не найдена!";
        String expectedMessage2 = "Лучший стрейк: 8";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage1));
        assertTrue(output.contains(expectedMessage2));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showSingleHabitStats_lastStreak() {
        addUser();

        String input = "Не пить воду\nall\n2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        userService.setUser(user);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        createWeekStreak_dailyHabit("Не пить воду");

        controller.showSingleHabitStats();


        String expectedMessage2 = "Последний стрейк: 8";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage2));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showSingleHabitStats_percentage() {
        addUser();

        String input = "Не пить воду\n15\n3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        userService.setUser(user);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        createWeekStreak_dailyHabit("Не пить воду");

        controller.showSingleHabitStats();


        String expectedMessage2 = "Выполнение привычки в процентном соотношении: 100,00%";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage2));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showOverall_percentage() {
        addUser();

        String input = "all\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        userService.setUser(user);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        createWeekStreak_dailyHabit("Пить воду");
        createInconsistentWeek_dailyHabit("Не пить воду");


        controller.showOverallStats();


        String expectedMessage2 = "Общее выполнение привычек в процентном соотношении: 81,25%";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage2));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showOverall_amount() {
        addUser();

        String input = "-1\nall\n2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        userService.setUser(user);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        createWeekStreak_dailyHabit("Пить воду");
        createInconsistentWeek_dailyHabit("Не пить воду");


        controller.showOverallStats();

        String expectedMessage1 = "Пожалуйста, введите корректный период";
        String expectedMessage2 = "Общее количество выполнения привычек: 13";
        String output = outContent.toString();
        assertTrue(output.contains(expectedMessage1));
        assertTrue(output.contains(expectedMessage2));
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void showOverall_report() {
        addUser();

        String input = "all\n3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        userService.setUser(user);

        HabitController controller = new HabitController(service, statService, userController);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        createWeekStreak_dailyHabit("Пить воду");
        createInconsistentWeek_dailyHabit("Не пить воду");


        controller.showOverallStats();

        String expectedMessage1 = "Общий процент отметок: 81,25%";
        String expectedMessage2 = "Общее количество отметок: 13";

        String expectedValue1 = "5";
        String expectedValue2 = "8";
        String expectedValue3 = "62,50";
        String expectedValue4 = "100,00";
        String expectedValue5 = "2";
        String expectedValue6 = "1";
        String output = outContent.toString();
        assertTrue(output.contains(expectedValue1));
        assertTrue(output.contains(expectedValue2));
        assertTrue(output.contains(expectedValue3));
        assertTrue(output.contains(expectedValue4));
        assertTrue(output.contains(expectedValue5));
        assertTrue(output.contains(expectedValue6));

        assertTrue(output.contains(expectedMessage1));
        assertTrue(output.contains(expectedMessage2));

        System.setOut(System.out);
        System.setIn(System.in);
    }

    public void createWeekStreak_dailyHabit(String name) {

        service.createHabit(name, "", Habit.Frequency.DAILY);
        LocalDate before = LocalDate.now().minusWeeks(1);

        Habit marked = repo.findHabitByName(name, this.user);
        marked.getStatistics().put(before, true);

        for (int i = 0; i < 7; i++) {
            before = before.plusDays(1);
            marked.getStatistics().put(before, true);
        }
    }

    public void createInconsistentWeek_dailyHabit(String name) {
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
    }


}
