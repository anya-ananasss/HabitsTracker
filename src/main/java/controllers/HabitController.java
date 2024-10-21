package controllers;

import models.Habit;
import serviceClasses.Constants;
import services.HabitService;
import services.HabitStatisticsService;

import java.time.LocalDate;
import java.util.*;

/**
 * Контроллер для сущности Habit и взаимодействия с приложением через консольный интерфейс.
 * <p>
 * * @author Gureva Anna
 * * @version 1.0
 * * @since 19.10.2024
 * </p>
 */
public class HabitController {
    /**
     * Конструктор для создания контроллера с заданными сервисами по управлению привычками, статистикой привычек
     * и ссылкой на контроллер пользователя, привычки которого рассматриваются.
     * @param service сервис с бизнес-логикой для User
     */
    public HabitController(HabitService service, HabitStatisticsService statService, UserController userController) {
        this.service = service;
        this.statService = statService;
        this.userController = userController;
    }

    private final Scanner scanner = new Scanner(System.in);
    private final HabitService service;
    private final HabitStatisticsService statService;
    private final UserController userController;

    /**
     * Показывает главное меню для управления привычками.
     * Можно просмотреть привычки, отметить выполнение привычки, перейти в меню статитстики,
     * перейти в меню настроек по привычкам или вернуться на главную страницу пользователя.
     */
    public void showMainMenu() {
        while (true) {
            System.out.println(Constants.HABIT_MAIN_MENU_TEXT_BLOCK);
            String i = scanner.nextLine();
            switch (i) {
                case "1" -> showHabits();
                case "2" -> markHabit();
                case "3" -> showStatisticsMenu();
                case "4" -> showSettings();
                case "5" -> userController.showMainPageUser();
                default -> System.out.println("Пожалуйста, введите 1, 2, 3, 4 или 5.");
            }
        }
    }

    /**
     * Выводит привычки, отсортированные по дате добавления (сначада новые/сначала старые)
     * или по статусу последней отметки (сначала отмеченные недавно/сначала отмеченные давно)
     */

    public void showHabits() {
        String show;
        System.out.println(Constants.SHOW_HABITS_TEXT_BLOCK);
        mainLoop:
        while (true) {
            show = scanner.nextLine();
            switch (show) {
                case "1" -> {
                    if (service.filterByCreationDateLatestFirst().isEmpty()) {
                        System.out.println("Привычки не были созданы!");
                    } else {
                        showList(service.filterByCreationDateLatestFirst());
                    }
                    break mainLoop;
                }
                case "2" -> {
                    if (service.filterByCreationDateNewestFirst().isEmpty()) {
                        System.out.println("Привычки не были созданы!");
                    } else {
                        showList(service.filterByCreationDateNewestFirst());
                    }
                    break mainLoop;
                }
                case "3" -> {
                    if (service.sortHabitsByMarked(true).isEmpty()) {
                        System.out.println("Привычки не были созданы!");
                    } else {
                        showHabitsMap(service.sortHabitsByMarked(true));
                    }
                    break mainLoop;
                }
                case "4" -> {
                    if (service.sortHabitsByMarked(false).isEmpty()) {
                        System.out.println("Привычки не были созданы!");
                    } else {
                        showHabitsMap(service.sortHabitsByMarked(false));
                    }
                    break mainLoop;
                }
                case "5" -> showMainMenu();
                default -> System.out.println("Пожалуйста, введите 1, 2, 3, 4 или 5.");
            }
        }
    }

    private void showHabitsMap(LinkedHashMap<String, LocalDate> sorted) {
        int index = 1;
        for (Map.Entry<String, LocalDate> entry : sorted.entrySet()) {
            LocalDate value = entry.getValue();
            String val;
            if (value == null) {
                val = "-";
            } else {
                val = value.toString();
            }
            System.out.println(index + ". " + entry.getKey() + ", " + val);
            index++;
        }
    }

    /**
     * Показывает меню для редактирования привычек.
     * Можно создать новую, отредактировать или удалить существующую.
     */
    public void showSettings() {
        System.out.println(Constants.SHOW_HABIT_SETTINGS_TEXT_BLOCK);
        mainLoop:
        while (true) {
            String i = scanner.nextLine();
            switch (i) {
                case "1" -> {
                    createHabit();
                    break mainLoop;
                }
                case "2" -> {
                    if (service.filterByCreationDateLatestFirst().isEmpty()) {
                        System.out.println("Ни одной привычки не было создано!");
                    } else {
                        updateHabit();
                    }
                    break mainLoop;
                }
                case "3" -> {
                    if (service.filterByCreationDateLatestFirst().isEmpty()) {
                        System.out.println("Ни одной привычки не было создано!");
                    } else {
                        deleteHabit();
                    }
                    break mainLoop;
                }
                case "4" -> {
                    showMainMenu();
                    break mainLoop;
                }
                default -> System.out.println("Пожалуйста, введите 1, 2, 3 или 4.");
            }
        }
    }

    /**
     * Создает новую привычку, проверяя, нет ли привычки с введенным именем у данного пользователя и
     * приемлема ли введенная частота (имеется ли в перечислении возможных частот).
     */
    public void createHabit() {
        while (true) {
            System.out.println("Введите название привычки.");
            String name = scanner.nextLine();
            if (service.nameExists(name)) {
                System.out.println("Привычка с таким названием уже существует!");
                System.out.println("Нажмите 1, чтобы попробовать еще раз, 2, чтобы вернуться в меню.");
                String i = scanner.nextLine();
                switch (i) {
                    case "1" -> {
                    }
                    case "2" -> showSettings();
                    default -> System.out.println("Введите 1 или 2.");
                }
            } else {
                System.out.println("Введите описание привычки (опционально)");
                String description = scanner.nextLine();
                System.out.println("Введите желаемую частоту выполнения привычки: ежедневно, еженедельно, раз в две недели, раз в три недели, ежемесячно.");
                Habit.Frequency frequency = setFrequency();
                System.out.println(service.createHabit(name, description, frequency));
                break;
            }
        }
    }

    /**
     * Обновляет привычку: название, описание или частоту, проверяя, существует ли привычка, которую пользователь хочет обновить,
     * а также приемлемость значений, на которые пользователь хочет произвести обновление.
     */
    public void updateHabit() {
        System.out.println("Введите название привычки, которую изменить");
        String name;
        while (true) {
            name = scanner.nextLine();
            String checkRes = checkName(name);
            if (checkRes.equals("2")) {
                return;
            } else if (checkRes.equals("НАЙДЕНО")) {
                break;
            }
        }

        System.out.println(Constants.UPDATE_HABITS_TEXT_BLOCK);
        loop1:
        while (true) {
            String i = scanner.nextLine();
            switch (i) {
                case "1" -> {
                    while (true) {
                        System.out.println("Введите новое название.");
                        String newName = scanner.nextLine();
                        String res = service.updateName(newName, name);
                        System.out.println(res);
                        if (res.equals("Успешно обновлено!")) {
                            break;
                        } else {
                            continueOrNot();
                        }
                    }
                    break loop1;
                }
                case "2" -> {
                    while (true) {
                        System.out.println("Введите новое описание.");
                        String newDescription = scanner.nextLine();
                        String res = service.updateDescription(newDescription, name);
                        System.out.println(res);
                        if (res.equals("Описание обновлено!")) {
                            break;
                        } else {
                            continueOrNot();
                        }
                    }
                    break loop1;
                }
                case "3" -> {
                    System.out.println("Введите новую частоту: ежедневно, еженедельно, раз в две недели, раз в три недели или ежемесячно.");
                    Habit.Frequency frequency = setFrequency();
                    System.out.println(service.updateFrequency(frequency, name));
                    break loop1;
                }
                case "4" -> showSettings();
            }
        }
    }

    /**
     * Проверяет, хочет ли пользователь продолжить действие или вернуться к настройкам при вводе неприемлемого значения
     * при обновлении привычки.
     */
    private void continueOrNot() {
        System.out.println("Хотите продолжить? да/нет");
        String cont = scanner.nextLine();
        aux:
        while (true) {
            switch (cont) {
                case "да" -> {
                    break aux;
                }
                case "нет" -> {
                    showSettings();
                    break aux;
                }
                default -> System.out.println("Введите да или нет.");
            }
        }
    }

    /**
     * Удаляет привычку у пользователя, если она была найдена, после дополнительного подтверждения;
     * если подтверждение не было получено, возвращает к настройкам для привычек.
     */
    public void deleteHabit() {
        String name;
        main:
        while (true) {
            System.out.println("Введите название привычки, которую хотите удалить.");
            while (true) {
                name = scanner.nextLine();
                String checkRes = checkName(name);
                if (checkRes.equals("2")) {
                    return;
                } else if (checkRes.equals("НАЙДЕНО")) {
                    break;
                }
            }
            while (true) {
                System.out.println("Вы действительно хотите удалить привычку " + name + "? Это действие невозможно отменить! да/нет");
                String i = scanner.nextLine();
                switch (i) {
                    case "да" -> {
                        service.deleteHabit(name);
                        System.out.println("Успешно удалено.");
                        break main;
                    }
                    case "нет" -> {
                        showSettings();
                        break main;
                    }
                    default -> System.out.println("Пожалуйста, введите да или нет.");
                }
            }
        }
    }

    /**
     * Устанавливает частоту из перечисления в соответствии с переданной строкой.
     * @return установленная частота из перечисления.
     */
    private Habit.Frequency setFrequency() {
        while (true) {
            String f = scanner.nextLine().toLowerCase();
            f = f.replaceAll("\\s+", " ").trim();
            switch (f) {
                case "ежедневно" -> {
                    return Habit.Frequency.DAILY;
                }
                case "еженедельно" -> {
                    return Habit.Frequency.WEEKLY;
                }
                case "раз в две недели" -> {
                    return Habit.Frequency.FORTNIGHTLY;
                }
                case "раз в три недели" -> {
                    return Habit.Frequency.EVERYTHREEWEEKS;
                }
                case "ежемесячно" -> {
                    return Habit.Frequency.MONTHLY;
                }
                default -> System.out.println("Пожалуйста, введите одно из предложенных значений.");
            }
        }
    }

    private void showList(List<Habit> habits) {
        for (int i = 0; i < habits.size(); i++) {
            System.out.println(i + 1 + ". " + habits.get(i).getName());
        }
    }

    /**
     * Ставит отметку о выполнении для привычки, если таковая была найдена и если в соответствии с частотой
     * привычки нужно поставить отметку.
     */
    public void markHabit() {
        String name;
        System.out.println("Введите название привычки, которую хотите отметить.");
        while (true) {
            name = scanner.nextLine();
            String checkRes = checkName(name);
            if (checkRes.equals("2")) {
                return;
            } else if (checkRes.equals("НАЙДЕНО")) {
                break;
            }
        }
        System.out.println(service.markHabit(name));
    }

    /**
     * Показывает меню для работы со статистикой привычек.
     * Можно перейти к статистике для одной привычке или просмотреть глобальную статистику.
     */
    public void showStatisticsMenu() {
        System.out.println(Constants.SHOW_STATISTICS_TEXT_BLOCK);
        if (service.filterByCreationDateLatestFirst().isEmpty()) {
            System.out.println("Ни одной привычки не было создано!");
        } else {
            mainLoop:
            while (true) {
                String i = scanner.nextLine();
                switch (i) {
                    case "1" -> {
                        showSingleHabitStats();
                        break mainLoop;
                    }
                    case "2" -> {
                        showOverallStats();
                        break mainLoop;
                    }
                    case "3" -> {
                        showMainMenu();
                        break mainLoop;
                    }
                    default -> System.out.println("Пожалуйста, введите 1, 2 или 3.");
                }
            }
        }
    }

    /**
     * Показывает статистику по заданной привычке, если таковая найдена, в заданный период.
     * Можно просмотреть лучший и текущий стрейк за период и выполнение привычки в процентах за период.
     */
    public void showSingleHabitStats() {
        String name;
        System.out.println("Введите название привычки, статистику по которой хотите получить");
        while (true) {
            name = scanner.nextLine();
            String checkRes = checkName(name);
            if (checkRes.equals("2")) {
                return;
            } else if (checkRes.equals("НАЙДЕНО")) {
                break;
            }
        }
        int period = initPeriod();
        System.out.println(Constants.SHOW_SINGLE_HABIT_STATS_TEXT_BLOCK);
        mainLoop:
        while (true) {
            String i = scanner.nextLine();
            switch (i) {
                case "1" -> {
                    System.out.print("Лучший стрейк: ");
                    System.out.print(statService.findLongestHabitStreak(name, period));
                    break mainLoop;
                }
                case "2" -> {
                    System.out.print("Последний стрейк: ");
                    System.out.println(statService.findLastHabitStreak(name, period));
                    break mainLoop;
                }
                case "3" -> {
                    System.out.print("Выполнение привычки в процентном соотношении: ");
                    System.out.printf("%.2f%%n", statService.getHabitCompleteness(name, period)[1]);
                    System.out.println();
                    break mainLoop;
                }
                case "4" -> {
                    showStatisticsMenu();
                    break mainLoop;
                }
                default -> System.out.println("Пожалуйста, введите 1, 2, 3 или 4.");

            }
        }
    }

    /**
     * Задает период (положительное целое число) для просмотра статистики.
     * @return int период для просмотра статистики; -1, если нужно просмотреть статистику за все время
     */
    public int initPeriod() {
        int period;
        while (true) {
            System.out.println("Введите период, за который хотите получить статитстику (числом) или \"all\", чтобы получить статистику за весь период отслеживания привычки");
            String i = scanner.nextLine();
            if (i.equals("all")) {
                period = -1;
                break;
            } else if (isPosInteger(i)) {
                period = Integer.parseInt(i);
                break;
            } else {
                System.out.println("Пожалуйста, введите корректный период");
            }
        }
        return period;
    }

    /**
     * Показывает статистику по всем привычкам.
     * Можно просмотреть общее выполнение привычек в процентах, количество всех отметченных привычек или
     * сформировать отчет, содержащий детальную информацию про стрейки и выполнение каждой привычки,
     * а так же общее количественное и процентное выполнение.
     */
    public void showOverallStats() {
        int period = initPeriod();
        System.out.println(Constants.SHOW_OVERALL_STATS_TEXT_BLOCK);
        mainLoop:
        while (true) {
            String i = scanner.nextLine();
            switch (i) {
                case "1" -> {
                    System.out.print("Общее выполнение привычек в процентном соотношении: ");
                    System.out.printf("%.2f%%n", (double) statService.formAllHabitsReport(period)[2]);
                    System.out.println();
                    break mainLoop;
                }
                case "2" -> {
                    System.out.print("Общее количество выполнения привычек: ");
                    System.out.println((int) statService.formAllHabitsReport(period)[1]);
                    break mainLoop;
                }
                case "3" -> {
                    System.out.println("=ОТЧЕТ=");
                    HashMap<String, double[]> report = (HashMap<String, double[]>) statService.formAllHabitsReport(period)[0];
                    System.out.printf("%-15s %-20s %-25s %-20s %-20s%n", "Название", "Количество отметок", "Количество отметок в процентах", "Длиннейший стрейк", "Текущий стрейк");
                    System.out.println("----------------------------------------------------------------------------------------------------------------");

                    for (Map.Entry<String, double[]> entry : report.entrySet()) {
                        String name = entry.getKey();
                        double[] values = entry.getValue();

                        System.out.printf("%-15s %-20.0f %-25.2f %-20.0f %-20.0f%n", name, values[0], values[1], values[2], values[3]);
                    }
                    System.out.print("Общий процент отметок: ");
                    System.out.printf("%.2f%%", (double) statService.formAllHabitsReport(period)[2]);
                    System.out.println();
                    System.out.println("Общее количество отметок: " + (int) statService.formAllHabitsReport(period)[1]);
                    break mainLoop;
                }
                case "4" -> {
                    showStatisticsMenu();
                    break mainLoop;
                }
                default -> System.out.println("Пожалуйста, введите 1, 2, 3 или 4.");
            }
        }
    }

    /**
     * Проверяет, существует ли привычка с заданным названием, если нет - предлагает повторить ввод или вернуться
     * в меню настроек.
     * @param name название привычки, которое нужно проверить
     * @return "НАЙДЕНО" - сообщение об успешном нахождении привычки с заданным названием; если таковой не находится,
     * пользователь выходит в меню настроек.
     */
    private String checkName(String name) {
        String i;
        if (!service.nameExists(name)) {
            System.out.println("Такая привычка не найдена!");
            System.out.println("Введите 1, если хотите продолжить, или 2, чтобы вернуться назад.");
            while (true) {
                i = scanner.nextLine();
                switch (i) {
                    case "1" -> {
                        return i;
                    }
                    case "2" -> {
                        showSettings();
                        return i;
                    }
                    default -> System.out.println("Пожалуйста, введите 1 или 2.");
                }
            }
        } else {
            return "НАЙДЕНО";
        }
    }

    private boolean isPosInteger(String str) {
        try {
            int integer = Integer.parseInt(str);
            return integer > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}


