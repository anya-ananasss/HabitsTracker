//package HabitTests.controllerTest;
//
//import controllers.HabitController;
//import controllers.UserController;
//import org.junit.jupiter.api.*;
//
//import org.testcontainers.containers.PostgreSQLContainer;
//import repositories.HabitRepository;
//import serviceClasses.Config;
//import services.HabitService;
//import models.User;
//import repositories.UserRepository;
//import services.HabitStatisticsService;
//import services.UserService;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//import java.io.PrintStream;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DisplayName("Тесты для контроллера, обеспечивающего взаимодейтсвие с привычками")
//public class HabitControllerTests {
//
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
//            "postgres:latest"
//    );
//
//    HabitService habitService;
//    HabitRepository habitRepository;
//    Connection connection;
//    UserRepository userRepository;
//    HabitStatisticsService statService;
//    UserController userController;
//    UserService userService;
//
//    User user = new User("anya", "anya@ya.ru", "$2a$10$Lz/N/PPqZTdHgRQC6Wf6EeU/SZb/KxAEGm.H/MDvW315ygMq3wEwm", 1);
//
//    @BeforeEach
//    void setUp() {
//        postgres.start();
//        Config config = new Config();
//        Object[] connections = config.establishConnection();
//        connection = (Connection) connections[0];
//        userRepository = new UserRepository(connection);
//        user = userRepository.readUserByEmail("anya@ya.ru");
//        habitRepository = new HabitRepository(userRepository);
//        habitService = new HabitService(habitRepository, this.user);
//        userService = new UserService(userRepository);
//        userController = new UserController(userService);
//    }
//
//    @AfterEach
//    void clear() throws SQLException {
//        connection.rollback();
//        connection.close();
//        postgres.stop();
//    }
//
//
//    @Test
//    public void showHabits_void_1() {
//
//        String input = "1\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.showHabits();
//        String expectedMessage = "Привычки не были созданы!";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }

//    @Test
//    public void showHabits_void_2() {
//
//        String input = "2\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.showHabits();
//        String expectedMessage = "Привычки не были созданы!";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showHabits_void_3() {
//
//        String input = "3\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.showHabits();
//        String expectedMessage = "Привычки не были созданы!";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showHabits_void_4() {
//
//        String input = "4\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.showHabits();
//        String expectedMessage = "Привычки не были созданы!";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showHabits_1() throws InterruptedException {
//
//        String input = "1\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//
//        List<Habit> res = habitService.filterByCreationDateLatestFirst();
//
//        controller.showHabits();
//
//
//        String output = outContent.toString();
//        StringBuilder actualBuilder = new StringBuilder();
//        for (int i = 0; i < res.size(); i++) {
//            actualBuilder.append(i + 1).append(". ").append(res.get(i).getName()).append("\r\n");
//        }
//        String expectedMessage = actualBuilder.toString();
//
//
//        assertThat(output).contains(expectedMessage);
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//
//    }
//
//    @Test
//    public void showHabits_2() throws InterruptedException {
//
//        String input = "2\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//
//        List<Habit> res = habitService.filterByCreationDateNewestFirst();
//
//        controller.showHabits();
//
//
//        String output = outContent.toString();
//        StringBuilder actualBuilder = new StringBuilder();
//        for (int i = 0; i < res.size(); i++) {
//            actualBuilder.append(i + 1).append(". ").append(res.get(i).getName()).append("\r\n");
//        }
//        String expectedMessage = actualBuilder.toString();
//
//
//        assertThat(output).contains(expectedMessage);
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//
//    }
//
//    @Test
//    public void showHabits_3() throws InterruptedException {
//
//        String input = "3\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//
//        habitService.markHabit("Пить воду");
//
//        LinkedHashMap<String, LocalDate> res = habitService.sortHabits_marked(true);
//
//        controller.showHabits();
//
//        StringBuilder builder = new StringBuilder();
//        int index = 1;
//        for (Map.Entry<String, LocalDate> entry : res.entrySet()) {
//            LocalDate value = entry.getValue();
//            String val;
//            if (value == null) {
//                val = "-";
//            } else {
//                val = value.toString();
//            }
//            builder.append(index).append(". ").append(entry.getKey()).append(", ").append(val).append("\r\n");
//            index++;
//        }
//        String expectedMessage = builder.toString();
//        String output = outContent.toString();
//
//
//        assertThat(output).contains(expectedMessage);
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//
//
//    }
//
//    @Test
//    public void showHabits_4() throws InterruptedException {
//
//        String input = "4\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//
//        Habit habit1 = new Habit("Пить воду", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
//        repo.addHabit(habit1);
//        user.getHabits().put(habit1.getName(), habit1);
//        Thread.sleep(5000);
//        Habit habit2 = new Habit("Вставать в 5 утра", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
//        repo.addHabit(habit2);
//        user.getHabits().put(habit2.getName(), habit2);
//
//        habitService.markHabit("Пить воду");
//        habit2.getStatistics().put(LocalDate.now(), false);
//
//        LinkedHashMap<String, LocalDate> res = habitService.sortHabits_marked(false);
//
//        controller.showHabits();
//
//        StringBuilder builder = new StringBuilder();
//        int index = 1;
//        for (Map.Entry<String, LocalDate> entry : res.entrySet()) {
//            LocalDate value = entry.getValue();
//            String val;
//            if (value == null) {
//                val = "-";
//            } else {
//                val = value.toString();
//            }
//            builder.append(index).append(". ").append(entry.getKey()).append(", ").append(val).append("\r\n");
//            index++;
//        }
//        String expectedMessage = builder.toString();
//        String output = outContent.toString();
//
//
//        assertThat(output).contains(expectedMessage);
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//
//    }
//
//    @Test
//    public void showHabits_opinionMismatch() throws InterruptedException {
//
//        String input = "34567\n4\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//
//        Habit habit1 = new Habit("Пить воду", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
//        repo.addHabit(habit1);
//        user.getHabits().put(habit1.getName(), habit1);
//        Thread.sleep(5000);
//        Habit habit2 = new Habit("Вставать в 5 утра", "", LocalDateTime.now(), this.user, Habit.Frequency.DAILY);
//        repo.addHabit(habit2);
//        user.getHabits().put(habit2.getName(), habit2);
//
//        habitService.markHabit("Пить воду");
//        habit2.getStatistics().put(LocalDate.now(), false);
//
//        habitService.sortHabits_marked(false);
//
//        controller.showHabits();
//
//        String expectedMessage = "Пожалуйста, введите 1, 2, 3, 4 или 5.";
//        String output = outContent.toString();
//
//
//        assertThat(output).contains(expectedMessage);
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//
//    }
//
//    @Test
//    public void showSettings_void_2() {
//
//        String input = "2\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.showSettings();
//        String expectedMessage = "Ни одной привычки не было создано!";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showSettings_void_3() {
//
//        String input = "3\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.showSettings();
//        String expectedMessage = "Ни одной привычки не было создано!";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void basicCreateHabit() {
//
//
//        String input = "Пить воду\n\nраз в Две недели\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        userService.setUser(user);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.createHabit();
//        String expectedMessage = "Привычка успешно создана!";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void createHabit_nonUnique_diffCase() {
//
//
//        String input = "Пить воду\n\nежедневно\nпить воду\nНе пить воду\n\nежемесячно";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        userService.setUser(user);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.createHabit();
//        controller.createHabit();
//        String expectedMessage = "Привычка с таким названием уже существует!";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void updateHabit_nonExistent_plusName() {
//
//
//        String input = "Пить воду\n\nеженедельно\nНе пить воду\n1\nПить воду\n1\nМного воды пить";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        userService.setUser(user);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.createHabit();
//        controller.updateHabit();
//
//        String expectedMessage1 = "Такая привычка не найдена!";
//        String expectedMessage2 = "Успешно обновлено!";
//        String output = outContent.toString();
//        assertTrue(output.contains(expectedMessage1));
//        assertTrue(output.contains(expectedMessage2));
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void basicUpdateHabit_descr() {
//
//
//        String input = "Пить воду\n\nРаз в    три недели\nПить воду \n2\nВода вкусная\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        userService.setUser(user);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.createHabit();
//        controller.updateHabit();
//        String expectedMessage = "Описание обновлено!";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void basicUpdateHabit_freq() {
//
//
//        String input = "Пить воду\n\nежедневно\nПить воду\n3\nЕжедневно\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        userService.setUser(user);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.createHabit();
//        controller.updateHabit();
//        String expectedMessage = "Успешно обновлено!";
//        String output = outContent.toString();
//        assertThat(output).contains(expectedMessage);
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void deleteUnexistent_plusBasicDelete() {
//
//
//        String input = "Не пить воду\n\nЕжедневно\nПить воду\n1\nНе пить воду\nда";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        userService.setUser(user);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.createHabit();
//        controller.deleteHabit();
//
//
//        String expectedMessage1 = "Такая привычка не найдена!";
//        String expectedMessage2 = "Успешно удалено.";
//        String output = outContent.toString();
//        assertTrue(output.contains(expectedMessage1));
//        assertTrue(output.contains(expectedMessage2));
//        System.setOut(System.out);
//        System.setIn(System.in);
//
//    }
//
//    @Test
//    public void markHabit_unsuccessAndSuccess() {
//
//
//        String input = "Не пить воду\n\nЕжедневно\nПить воду\n1\nНе пить воду\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        userService.setUser(user);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        controller.createHabit();
//        controller.markHabit();
//
//
//        String expectedMessage1 = "Такая привычка не найдена!";
//        String expectedMessage2 = "Отметка проставлена!";
//        String output = outContent.toString();
//        assertTrue(output.contains(expectedMessage1));
//        assertTrue(output.contains(expectedMessage2));
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showSingleHabitStats_unsuccessAndSuccess_bestStreak() {
//
//
//        String input = "Пить воду\n1\nНе пить воду\nall\n1\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        userService.setUser(user);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        createWeekStreak_dailyHabit("Не пить воду");
//
//        controller.showSingleHabitStats();
//
//        String expectedMessage1 = "Такая привычка не найдена!";
//        String expectedMessage2 = "Лучший стрейк: 8";
//        String output = outContent.toString();
//        assertTrue(output.contains(expectedMessage1));
//        assertTrue(output.contains(expectedMessage2));
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showSingleHabitStats_lastStreak() {
//
//
//        String input = "Не пить воду\nall\n2\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        userService.setUser(user);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        createWeekStreak_dailyHabit("Не пить воду");
//
//        controller.showSingleHabitStats();
//
//
//        String expectedMessage2 = "Последний стрейк: 8";
//        String output = outContent.toString();
//        assertTrue(output.contains(expectedMessage2));
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showSingleHabitStats_percentage() {
//
//
//        String input = "Не пить воду\n15\n3\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        userService.setUser(user);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        createWeekStreak_dailyHabit("Не пить воду");
//
//        controller.showSingleHabitStats();
//
//
//        String expectedMessage2 = "Выполнение привычки в процентном соотношении: 100,00%";
//        String output = outContent.toString();
//        assertTrue(output.contains(expectedMessage2));
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showOverall_percentage() {
//
//
//        String input = "all\n1\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        userService.setUser(user);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        createWeekStreak_dailyHabit("Пить воду");
//        createInconsistentWeek_dailyHabit("Не пить воду");
//
//
//        controller.showOverallStats();
//
//
//        String expectedMessage2 = "Общее выполнение привычек в процентном соотношении: 81,25%";
//        String output = outContent.toString();
//        assertTrue(output.contains(expectedMessage2));
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showOverall_amount() {
//
//
//        String input = "-1\nall\n2\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        userService.setUser(user);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        createWeekStreak_dailyHabit("Пить воду");
//        createInconsistentWeek_dailyHabit("Не пить воду");
//
//
//        controller.showOverallStats();
//
//        String expectedMessage1 = "Пожалуйста, введите корректный период";
//        String expectedMessage2 = "Общее количество выполнения привычек: 13";
//        String output = outContent.toString();
//        assertTrue(output.contains(expectedMessage1));
//        assertTrue(output.contains(expectedMessage2));
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//
//    @Test
//    public void showOverall_report() {
//
//
//        String input = "all\n3\n";
//        InputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//
//        userService.setUser(user);
//
//        HabitController controller = new HabitController(habitService, statService, userController, connection);
//
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//
//        createWeekStreak_dailyHabit("Пить воду");
//        createInconsistentWeek_dailyHabit("Не пить воду");
//
//
//        controller.showOverallStats();
//
//        String expectedMessage1 = "Общий процент отметок: 81,25%";
//        String expectedMessage2 = "Общее количество отметок: 13";
//
//        String expectedValue1 = "5";
//        String expectedValue2 = "8";
//        String expectedValue3 = "62,50";
//        String expectedValue4 = "100,00";
//        String expectedValue5 = "2";
//        String expectedValue6 = "1";
//        String output = outContent.toString();
//        assertTrue(output.contains(expectedValue1));
//        assertTrue(output.contains(expectedValue2));
//        assertTrue(output.contains(expectedValue3));
//        assertTrue(output.contains(expectedValue4));
//        assertTrue(output.contains(expectedValue5));
//        assertTrue(output.contains(expectedValue6));
//
//        assertTrue(output.contains(expectedMessage1));
//        assertTrue(output.contains(expectedMessage2));
//
//        System.setOut(System.out);
//        System.setIn(System.in);
//    }
//}
