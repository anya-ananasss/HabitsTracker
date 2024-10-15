package HabitTests.serviceTest;

import Habit.model.Habit;
import Habit.repository.HabitRepository;
import Habit.service.HabitService;
import User.model.User;
import User.repository.UserRepository;
import User.service.UserService;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.*;

public class HabitServiceTests {
    HabitRepository repo = new HabitRepository();
    User user = new User("anya", "anya@ya.ru", "1234", 1);
    UserService userService = new UserService(user, new UserRepository());
    HabitService service = new HabitService(repo, user, userService);

    @Test
    public void basicCreateHabit() {
        String res = service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit created = repo.findHabitByName("Пить воду", this.user);

        String expectedMessage = "Привычка успешно создана!";
        assertEquals(expectedMessage, res);
        assertEquals(created.getUser(), this.user);
        assertEquals(created.getName(), "Пить воду");
        assertEquals(created.getDescription(), "");
        assertEquals(created.getFrequency(), Habit.Frequency.DAILY);
    }

    @Test
    public void createHabit_duplicateName() {
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        Habit created = repo.findHabitByName("Пить воду", this.user);
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);

        assertTrue(user.getHabits().containsKey(created.getName()));
    }

    @Test
    public void basicNameUpdate() {
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        String oldName = "Пить воду";
        String res = service.updateHabit_name("НЕ пить воду", oldName);
        Habit updated = repo.findHabitByName("НЕ пить воду", this.user);
        String expectedMessage = "Успешно обновлено!";

        assertEquals(expectedMessage, res);
        assertTrue(user.getHabits().containsKey(updated.getName()));
        assertNull(repo.findHabitByName(oldName, this.user));
    }

    @Test
    public void nameUpdate_nonUniqueName() {
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        service.createHabit("НЕ пить воду", "", Habit.Frequency.DAILY);
        String oldName = "Пить воду";
        String res = service.updateHabit_name(oldName, "НЕ пить воду");
        String expectedMessage = "Привычка с таким названием уже существует!";

        assertEquals(expectedMessage, res);
        assertTrue(user.getHabits().containsKey("Пить воду"));
    }


    @Test
    public void nameUpdate_emptyName() {
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        String oldName = "Пить воду";
        String res = service.updateHabit_name("", oldName);
        String expectedMessage = "Название не может быть пустым!";

        assertEquals(expectedMessage, res);
        assertTrue(user.getHabits().containsKey("Пить воду"));
    }

    @Test
    public void basicDescriptionUpdate() {
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        String res = service.updateHabit_description("Вода вкусная", "Пить воду");
        Habit updated = repo.findHabitByName("Пить воду", this.user);
        String expectedMessage = "Описание обновлено!";

        assertEquals(expectedMessage, res);
        assertEquals("Вода вкусная", updated.getDescription());
    }

    @Test
    public void descriptionUpdate_tooLong() {
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        String res = service.updateHabit_description("Ожпб йрдхф Мктв Лръкмвеж. Опж 33 ерёв. Орл ёро пвчрёкфуб д ужджтр-друфрщпрл щвуфк Орткр, д твлрпж срожуфкл. Твгрфва д рцкуж ужфк овевйкпрд Kame Yu к ёрорл дрйдтвываую, уворж срйёпжж, д дружою джщжтв. Пж мхта, дэскдва кйтжёмв. Нрихую усвфю д 11 джщжтв к хгжиёваую, щфр срнхщва трдпр дружою щвурд упв, пжуорфтб пк пв щфр. Сжтжё упро б сюа фзснрж орнрмр, в фвмиж окпхф ёдвёшвфю хёжнба твйокпмж, сряфрох ёр хфтв усна гжй рургэч стргнжо. Хфтро б струэсваую, пж щхдуфдхб пк хуфвнруфк, пк уфтжуув, унрдпр онвёжпжш. Пв ожёруорфтж опж умвйвнк, щфр пкмвмкч стргнжо пжф. Б сэфваую ёрпжуфк, щфр б ргэщпэл щжнрджм, мрфртэл чрщжф икфю усрмрлпрл икйпюа. Б пж йвгкдва ужгж ернрдх стргнжовок дтрёж сргжё кнк сртвижпкл, к пж ргйвдрихую дтвевок, кй-йв мрфртэч пж оре гэ хупхфю. Б йпва пвджтпбмв: д фвмро усрургж дйвкорёжлуфдкб у ргыжуфдро к мтржфуб ущвуфюж. Чрфб, жунк гэ опж сткънрую утвивфюуб, б гэ пкмрох пж стркетвн.", "Пить воду");
        Habit updated = repo.findHabitByName("Пить воду", this.user);
        String expectedMessage = "Описание не должно превышать 200 символов!";

        assertEquals(expectedMessage, res);
        assertEquals("", updated.getDescription());
    }

    @Test
    public void basicFrequencyUpdate() {
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        String res = service.updateHabit_frequency(Habit.Frequency.WEEKLY, "Пить воду");
        Habit updated = repo.findHabitByName("Пить воду", this.user);
        String expectedMessage = "Успешно обновлено!";

        assertEquals(expectedMessage, res);
        assertEquals(Habit.Frequency.WEEKLY, updated.getFrequency());
    }

    @Test
    public void readUserHabits_earlierFirst() throws InterruptedException {
        Habit habit1 = new Habit("Пить воду", "", LocalDateTime.now(), Habit.Frequency.DAILY, this.user);
        repo.addHabit(habit1);
        user.getHabits().put(habit1.getName(), habit1);
        Thread.sleep(5000);
        Habit habit2 = new Habit("Вставать в 5 утра", "", LocalDateTime.now(), Habit.Frequency.DAILY, this.user);
        repo.addHabit(habit2);
        user.getHabits().put(habit2.getName(), habit2);

        List<Habit> expected = new ArrayList<>();
        expected.add(habit1);
        expected.add(habit2);

        List<Habit> res = service.readUserHabits_filterByCreationDate_earilerFirst();

        assertEquals(expected, res);
    }

    @Test
    public void readUserHabits_earlierLast() throws InterruptedException {
        Habit habit1 = new Habit("Пить воду", "", LocalDateTime.now(), Habit.Frequency.DAILY, this.user);
        repo.addHabit(habit1);
        user.getHabits().put(habit1.getName(), habit1);
        Thread.sleep(5000);
        Habit habit2 = new Habit("Вставать в 5 утра", "", LocalDateTime.now(), Habit.Frequency.DAILY, this.user);
        repo.addHabit(habit2);
        user.getHabits().put(habit2.getName(), habit2);

        List<Habit> expected = new ArrayList<>();
        expected.add(habit2);
        expected.add(habit1);

        List<Habit> res = service.readUserHabits_filterByCreationDate_earilerLast();

        assertEquals(expected, res);
    }

    @Test
    public void deleteHabit() {
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        String res = service.deleteHabit("Пить воду");
        String expectedMessage = "Удаление прошло успешно";

        assertEquals(expectedMessage, res);
        assertNull(repo.findHabitByName("Пить воду", this.user));
    }

    @Test
    public void markDailyHabit_today() {
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        String res = service.markHabit("Пить воду");
        String expectedMessage = "Отметка проставлена!";
        LinkedHashMap<LocalDate, Boolean> expectedStat = new LinkedHashMap<>();
        expectedStat.put(LocalDate.now(), true);

        Habit markedHabit = repo.findHabitByName("Пить воду", this.user);

        assertEquals(expectedMessage, res);
        assertEquals(expectedStat, markedHabit.getStatistics());
    }

    @Test
    public void markHabit_twiceADay() {
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        service.markHabit("Пить воду");
        String res = service.markHabit("Пить воду");
        String expectedMessage = "Еще не время! Вы уже отмечались сегодня.";
        LinkedHashMap<LocalDate, Boolean> expectedStat = new LinkedHashMap<>();
        expectedStat.put(LocalDate.now(), true);

        Habit markedHabit = repo.findHabitByName("Пить воду", this.user);

        assertEquals(expectedMessage, res);
        assertEquals(expectedStat, markedHabit.getStatistics());
    }

    @Test
    public void markHabit_twiceAWeek() {
        service.createHabit("Пить воду", "", Habit.Frequency.WEEKLY);
        LocalDate before = LocalDate.now().minusDays(4);

        Habit marked = repo.findHabitByName("Пить воду", this.user);
        marked.getStatistics().put(before, true);

        String res = service.markHabit("Пить воду");
        String expectedMessage = "Еще не время! Вы уже отмечались на этой неделе.";
        LinkedHashMap<LocalDate, Boolean> expectedStat = new LinkedHashMap<>();
        expectedStat.put(before, true);

        Habit markedHabit = repo.findHabitByName("Пить воду", this.user);

        assertEquals(expectedMessage, res);
        assertEquals(expectedStat, markedHabit.getStatistics());
    }

    @Test
    public void markHabit_twiceAFortnight() {
        service.createHabit("Пить воду", "", Habit.Frequency.FORTNIGHTLY);
        LocalDate before = LocalDate.now().minusWeeks(1).minusDays(4);

        Habit marked = repo.findHabitByName("Пить воду", this.user);
        marked.getStatistics().put(before, true);

        String res = service.markHabit("Пить воду");
        String expectedMessage = "Еще не время! Вы уже отмечались в последние две недели.";
        LinkedHashMap<LocalDate, Boolean> expectedStat = new LinkedHashMap<>();
        expectedStat.put(before, true);

        Habit markedHabit = repo.findHabitByName("Пить воду", this.user);

        assertEquals(expectedMessage, res);
        assertEquals(expectedStat, markedHabit.getStatistics());
    }

    @Test
    public void markHabit_twiceInAThreeWeeks() {
        service.createHabit("Пить воду", "", Habit.Frequency.EVERYTHREEWEEKS);
        LocalDate before = LocalDate.now().minusWeeks(2).minusDays(4);

        Habit marked = repo.findHabitByName("Пить воду", this.user);
        marked.getStatistics().put(before, true);

        String res = service.markHabit("Пить воду");
        String expectedMessage = "Еще не время! Вы уже отмечались в последние три недели.";
        LinkedHashMap<LocalDate, Boolean> expectedStat = new LinkedHashMap<>();
        expectedStat.put(before, true);

        Habit markedHabit = repo.findHabitByName("Пить воду", this.user);

        assertEquals(expectedMessage, res);
        assertEquals(expectedStat, markedHabit.getStatistics());
    }

    @Test
    public void markHabit_twiceAMonth() {
        service.createHabit("Пить воду", "", Habit.Frequency.MONTHLY);
        LocalDate before = LocalDate.now().minusWeeks(3).minusDays(6);

        Habit marked = repo.findHabitByName("Пить воду", this.user);
        marked.getStatistics().put(before, true);

        String res = service.markHabit("Пить воду");
        String expectedMessage = "Еще не время! Вы уже отмечались в этом месяце.";
        LinkedHashMap<LocalDate, Boolean> expectedStat = new LinkedHashMap<>();
        expectedStat.put(before, true);

        Habit markedHabit = repo.findHabitByName("Пить воду", this.user);

        assertEquals(expectedMessage, res);
        assertEquals(expectedStat, markedHabit.getStatistics());
    }

    @Test
    public void markHabit_forTwoDays() {
        service.createHabit("Пить воду", "", Habit.Frequency.DAILY);
        LocalDate before = LocalDate.now().minusDays(1);

        Habit marked = repo.findHabitByName("Пить воду", this.user);
        marked.getStatistics().put(before, true);

        String res = service.markHabit("Пить воду");
        String expectedMessage = "Отметка проставлена!";
        LinkedHashMap<LocalDate, Boolean> expectedStat = new LinkedHashMap<>();
        expectedStat.put(before, true);
        expectedStat.put(LocalDate.now(), true);

        Habit markedHabit = repo.findHabitByName("Пить воду", this.user);

        assertEquals(expectedMessage, res);
        assertEquals(expectedStat, markedHabit.getStatistics());
    }

    @Test
    public void sortStatus_Earlier() throws InterruptedException {
        Habit habit1 = new Habit("Пить воду", "", LocalDateTime.now(), Habit.Frequency.DAILY, this.user);
        repo.addHabit(habit1);
        user.getHabits().put(habit1.getName(), habit1);
        Thread.sleep(5000);
        Habit habit2 = new Habit("Вставать в 5 утра", "", LocalDateTime.now(), Habit.Frequency.DAILY, this.user);
        repo.addHabit(habit2);
        user.getHabits().put(habit2.getName(), habit2);

        service.markHabit("Пить воду");
        habit2.getStatistics().put(LocalDate.now(), false);

        LinkedHashMap<String, LocalDate> expected = new LinkedHashMap<>();
        expected.put(habit1.getName(), LocalDate.now());
        expected.put(habit2.getName(), null);

        LinkedHashMap<String, LocalDate> res = service.sortHabits_marked(true);

        assertEquals(expected, res);
    }

    @Test
    public void sortStatus_Later() throws InterruptedException {
        Habit habit1 = new Habit("Пить воду", "", LocalDateTime.now(), Habit.Frequency.DAILY, this.user);
        repo.addHabit(habit1);
        user.getHabits().put(habit1.getName(), habit1);
        Thread.sleep(5000);
        Habit habit2 = new Habit("Вставать в 5 утра", "", LocalDateTime.now(), Habit.Frequency.DAILY, this.user);
        repo.addHabit(habit2);
        user.getHabits().put(habit2.getName(), habit2);

        service.markHabit("Пить воду");
        habit2.getStatistics().put(LocalDate.now(), false);

        LinkedHashMap<String, LocalDate> expected = new LinkedHashMap<>();
        expected.put(habit2.getName(), null);
        expected.put(habit1.getName(), LocalDate.now());


        LinkedHashMap<String, LocalDate> res = service.sortHabits_marked(false);

        assertEquals(expected, res);
    }
}