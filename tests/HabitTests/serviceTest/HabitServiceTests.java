package HabitTests.serviceTest;

import models.Habit;
import org.junit.jupiter.api.*;

import org.testcontainers.containers.PostgreSQLContainer;
import repositories.HabitRepository;
import serviceClasses.Config;
import services.HabitService;
import models.User;
import repositories.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
@DisplayName("Тесты для сервиса, работающего с привычками")
public class HabitServiceTests {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    HabitService service;
    HabitRepository habitRepository;
    Connection connection;
    UserRepository userRepository;
    User user = new User("anya", "anya@ya.ru", "$2a$10$Lz/N/PPqZTdHgRQC6Wf6EeU/SZb/KxAEGm.H/MDvW315ygMq3wEwm", 1);

    @BeforeEach
    void setUp() {
        postgres.start();
        Config config = new Config();
        Object[] connections = config.establishConnections();
        connection = (Connection) connections[0];
        userRepository = new UserRepository(connection);
        user = userRepository.readUserByEmail("anya@ya.ru");
        habitRepository = new HabitRepository(connection, userRepository);
        service = new HabitService(habitRepository, this.user);
    }

    @AfterEach
    void clear() throws SQLException {
        connection.rollback();
        connection.close();
        postgres.stop();
    }

    @Test
    @DisplayName("Создание привычки с приемлемыми параметрами")
    public void basicCreateHabit() {
        Habit created = new Habit("Гулять по 20 минут", "", LocalDateTime.now(),
                this.user, Habit.Frequency.DAILY);
        service.deleteHabit("Гулять по 20 минут");
        try {
            service.createHabit("Гулять по 20 минут", "", created.getCreatedAt(), Habit.Frequency.DAILY);
        } catch (SQLException e) {
            fail();
        }

        Habit found = service.findHabitByName("Гулять по 20 минут");

        assertThat(found).isEqualTo(created);
    }

    @Test
    @DisplayName("Создание привычки с названием, повторяющим существующее")
    public void createHabitDuplicateName() {
        assertThatThrownBy(() -> service.createHabit("Пить воду", "", Habit.Frequency.DAILY)).isInstanceOf(SQLException.class);
    }

    //TODO: если время будет, можно перетащить все sqlEx по образцу createHabit
    @Test
    @DisplayName("Обновление названия привычки новым приемлемым названием")
    public void basicNameUpdate() {
        String oldName = "Пить воду";
        try {
            service.updateName("НЕ пить воду", oldName);
        } catch (SQLException e) {
            fail();
        }
        Habit updated = service.findHabitByName("НЕ пить воду");

        assertThat(updated).isNotNull();
    }

    @Test
    @DisplayName("Обновление названия привычки существующим названием")
    public void nameUpdateNonUniqueName() {
        String oldName = "Пить воду";
        assertThatThrownBy(() -> service.updateName("Вставать в 5 утра", oldName)).isInstanceOf(SQLException.class);
    }


    @Test
    @DisplayName("Обновление названия привычки пустым названием")
    public void nameUpdateEmptyName() {
        try {
            String oldName = "Пить воду";
            String res = service.updateName("", oldName);
            Habit updated = service.findHabitByName("Пить воду");
            String expectedMessage = "Название не может быть пустым!";

            assertThat(updated.getName()).isEqualTo(oldName);
            assertThat(res).isEqualTo(expectedMessage);
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Обновление описания привычки приемлемым описанием")
    public void basicDescriptionUpdate() {
        try {
            service.updateDescription("1,5 литра в день!", "Пить воду");
            Habit updated = service.findHabitByName("Пить воду");

            assertThat(updated.getDescription()).isEqualTo("1,5 литра в день!");
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Обновление описания привычки слишком длинным описанием")
    public void descriptionUpdateTooLong() {
        try {
            String res = service.updateDescription("Ожпб йрдхф Мктв Лръкмвеж. Опж 33 ерёв. Орл ёро пвчрёкфуб д ужджтр-друфрщпрл щвуфк Орткр, д твлрпж срожуфкл. Твгрфва д рцкуж ужфк овевйкпрд Kame Yu к ёрорл дрйдтвываую, уворж срйёпжж, д дружою джщжтв. Пж мхта, дэскдва кйтжёмв. Нрихую усвфю д 11 джщжтв к хгжиёваую, щфр срнхщва трдпр дружою щвурд упв, пжуорфтб пк пв щфр. Сжтжё упро б сюа фзснрж орнрмр, в фвмиж окпхф ёдвёшвфю хёжнба твйокпмж, сряфрох ёр хфтв усна гжй рургэч стргнжо. Хфтро б струэсваую, пж щхдуфдхб пк хуфвнруфк, пк уфтжуув, унрдпр онвёжпжш. Пв ожёруорфтж опж умвйвнк, щфр пкмвмкч стргнжо пжф. Б сэфваую ёрпжуфк, щфр б ргэщпэл щжнрджм, мрфртэл чрщжф икфю усрмрлпрл икйпюа. Б пж йвгкдва ужгж ернрдх стргнжовок дтрёж сргжё кнк сртвижпкл, к пж ргйвдрихую дтвевок, кй-йв мрфртэч пж оре гэ хупхфю. Б йпва пвджтпбмв: д фвмро усрургж дйвкорёжлуфдкб у ргыжуфдро к мтржфуб ущвуфюж. Чрфб, жунк гэ опж сткънрую утвивфюуб, б гэ пкмрох пж стркетвн.", "Пить воду");
            Habit updated = service.findHabitByName("Пить воду");
            String expectedMessage = "Описание не должно превышать 200 символов!";

            assertThat(updated.getDescription()).isEqualTo("Вода вкусная");
            assertThat(res).isEqualTo(expectedMessage);

        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Обновление частоты привычки")
    public void basicFrequencyUpdate() {

        try {
            service.updateFrequency(Habit.Frequency.WEEKLY, "Пить воду");
            Habit updated = service.findHabitByName("Пить воду");

            assertThat(updated.getFrequency()).isEqualTo(Habit.Frequency.WEEKLY);
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Сортировка привычек по дате создания (сначала новые)")
    public void readUserHabitsNewestFirst() {
        List<Habit> expected = new ArrayList<>();

        expected.add(service.findHabitByName("Пить воду"));
        expected.add(service.findHabitByName("Вставать в 5 утра"));


        List<Habit> res = service.filterByCreationDateNewestFirst();

        assertThat(expected).isEqualTo(res);
    }

    @Test
    @DisplayName("Сортировка привычек по дате создания (сначала старые)")
    public void readUserHabitsLatestFirst() {
        List<Habit> expected = new ArrayList<>();
        expected.add(service.findHabitByName("Вставать в 5 утра"));
        expected.add(service.findHabitByName("Пить воду"));



        List<Habit> res = service.filterByCreationDateLatestFirst();

        assertThat(expected).isEqualTo(res);
    }

    @Test
    @DisplayName("Удаление привычки")
    public void deleteHabit() {
        service.deleteHabit("Пить воду");

        Habit res = service.findHabitByName("Пить воду");

        assertThat(res).isNull();
    }

    @Test
    @DisplayName("Отметка еще не отмеченной сегодня ежедневной привычки")
    public void markDailyHabitToday() {
        String res = service.markHabit("Пить воду");
        String expectedMessage = "Отметка проставлена!";

        assertThat(res).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("Отметка уже отмеченной сегодня ежедневной привычки")
    public void markHabitTwiceADay() {
        service.markHabit("Пить воду");
        String res = service.markHabit("Пить воду");
        String expectedMessage = "Еще не время! Вы уже отмечались сегодня.";

        assertThat(res).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("Отметка уже отмеченной на неделе еженедельной привычки")
    public void markWeeklyHabitTwiceAWeek() {
        try {
            service.createHabit("Гулять по 20 минут", "", LocalDateTime.now(), Habit.Frequency.WEEKLY);
            habitRepository.addHabitStat(LocalDate.now().minusDays(5), true, "Гулять по 20 минут", this.user);
            String res = service.markHabit("Гулять по 20 минут");
            String expectedMessage = "Еще не время! Вы уже отмечались на этой неделе.";

            assertThat(res).isEqualTo(expectedMessage);
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Отметка уже отмеченной в течение двух недель привычки, выполняемой раз в две недели")
    public void markFortnightlyHabitTwiceAFortnigh() {
        try {
            service.createHabit("Гулять по 20 минут", "", LocalDateTime.now(), Habit.Frequency.FORTNIGHTLY);
            habitRepository.addHabitStat(LocalDate.now().minusDays(5), true, "Гулять по 20 минут", this.user);
            String res = service.markHabit("Гулять по 20 минут");
            String expectedMessage = "Еще не время! Вы уже отмечались в последние две недели.";

            assertThat(res).isEqualTo(expectedMessage);
        } catch (SQLException e) {
            fail();
        }
    }
    @Test
    @DisplayName("Отметка уже отмеченной в течение трех недель привычки, выполняемой раз в три недели")
    public void markHabitTwiceAThreeWeeks() {
        try {
            service.createHabit("Гулять по 20 минут", "", LocalDateTime.now(), Habit.Frequency.EVERYTHREEWEEKS);
            habitRepository.addHabitStat(LocalDate.now().minusDays(5), true, "Гулять по 20 минут", this.user);
            String res = service.markHabit("Гулять по 20 минут");
            String expectedMessage = "Еще не время! Вы уже отмечались в последние три недели.";

            assertThat(res).isEqualTo(expectedMessage);
        } catch (SQLException e) {
            fail();
        }
    }
    @Test
    @DisplayName("Отметка уже отмеченной за месяц ежемесячной привычки")
    public void marMonthlyHabitTwiceAMonth() {
        try {
            service.createHabit("Гулять по 20 минут", "", LocalDateTime.now(), Habit.Frequency.MONTHLY);
            habitRepository.addHabitStat(LocalDate.now().minusDays(5), true, "Гулять по 20 минут", this.user);
            String res = service.markHabit("Гулять по 20 минут");
            String expectedMessage = "Еще не время! Вы уже отмечались в этом месяце.";

            assertThat(res).isEqualTo(expectedMessage);
        } catch (SQLException e) {
            fail();
        }
    }
    @Test
    @DisplayName("Получить последнюю отметку true привычки Пить воду (отметки есть)")
    public void getLastMarkMarkPresent (){
        LocalDate expected = LocalDate.of(2024, 10, 18);
        LocalDate actual = service.getLatestTrueDate(service.findHabitByName("Пить воду"));

        assertThat(actual).isEqualTo(expected);
    }
    @Test
    @DisplayName("Получить последнюю отметку true привычки Вставать в 5 утра (отметок нет)")
    public void getLastMarkMarkAbsent (){
        LocalDate actual = service.getLatestTrueDate(service.findHabitByName("Вставать в 5 утра"));

        assertThat(actual).isNull();
    }
    @Test
    @DisplayName("Отсортировать привычки по статусу отметки (сначала недавно отмеченные)")
    public void sortHabitsByStatusRecentlyMarked (){
        LinkedHashMap<String, LocalDate> expected = new LinkedHashMap<>();
        expected.put("Пить воду", LocalDate.of(2024, 10, 18));
        expected.put("Вставать в 5 утра", null);

        LinkedHashMap<String, LocalDate> actual = service.sortHabitsByMarked(true);

        assertThat(actual).isEqualTo(expected);
    }
    @Test
    @DisplayName("Отсортировать привычки по статусу отметки (сначала давно отмеченные)")
    public void sortHabitsByStatusMarkedLongAgo (){
        LinkedHashMap<String, LocalDate> expected = new LinkedHashMap<>();
        expected.put("Пить воду", LocalDate.of(2024, 10, 18));
        expected.put("Вставать в 5 утра", null);

        LinkedHashMap<String, LocalDate> actual = service.sortHabitsByMarked(false);

        assertThat(actual).isEqualTo(expected);
    }

}