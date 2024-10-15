package Habit.controller;

import Habit.model.Habit;
import Habit.service.HabitService;
import Habit.service.HabitStatisticsService;
import User.controller.UserController;

import java.time.LocalDate;
import java.util.*;

public class HabitController {
    public HabitController(HabitService service, HabitStatisticsService statService, UserController userController) {
        this.service = service;
        this.statService = statService;
        this.userController = userController;
    }

    Scanner scanner = new Scanner(System.in);
    private final HabitService service;
    private final HabitStatisticsService statService;
    private final UserController userController;

    public void showMainMenu() {
        while (true) {
            System.out.println("""
                    Выберите, что вы хотите сделать:
                    1. Просмотреть привычки
                    2. Отметить выполнение привычки
                    3. Просмотреть статистику
                    4. Отредактировать привычки
                    5. Назад
                    6. Выход из программы
                    """);
            String i = scanner.nextLine();
            switch (i) {
                case "1":
                    showHabits(true); //+
                    break;
                case "2":
                    markHabit(); //+
                    break;
                case "3":
                    showStatisticsMenu();//+
                    break;
                case "4":
                    showSettings();
                    break;
                case "5":
                    userController.showMainPage_user();
                    break;
                case "6":
                    System.exit(0);
                default:
                    System.out.println("Пожалуйста, введите 1, 2, 3, 4, 5 или 6.");
                    break;
            }
        }
    }

    //просмотреть привычки:
    public void showHabits(boolean habitsPresent) {
        String show;
        System.out.println("""
                Выберите, как нужно отфильтровать привычки:
                1. Сначала старые
                2. Сначала новые
                3. По статусу отметок(сначала недавно отмеченные);
                4. По статусу отметок(сначала давно отмеченные);
                5. Назад
                """);
        mainLoop:
        while (true) {
            show = scanner.nextLine();
            switch (show) {
                case "1":
                    if (service.readUserHabits_filterByCreationDate_earilerLast().isEmpty()) {
                        System.out.println("Привычки не были созданы!");
                        habitsPresent = false;
                    } else {
                        showList(service.readUserHabits_filterByCreationDate_earilerLast());
                    }
                    break mainLoop;
                case "2":
                    if (service.readUserHabits_filterByCreationDate_earilerFirst().isEmpty()) {
                        System.out.println("Привычки не были созданы!");
                        habitsPresent = false;
                    } else {
                        showList(service.readUserHabits_filterByCreationDate_earilerFirst());
                    }
                    break mainLoop;
                case "3":
                    if (service.sortHabits_marked(true).isEmpty()) {
                        System.out.println("Привычки не были созданы!");
                    } else {
                        showHabitsMap(service.sortHabits_marked(true));
                    }
                    habitsPresent = false;
                    break mainLoop;
                case "4":
                    if (service.sortHabits_marked(false).isEmpty()) {
                        System.out.println("Привычки не были созданы!");
                        habitsPresent = false;
                    } else {
                        showHabitsMap(service.sortHabits_marked(false));
                    }
                    break mainLoop;
                case "5":
                    showMainMenu();
                    break mainLoop;
                default:
                    System.out.println("Пожалуйста, введите 1, 2, 3, 4 или 5.");
                    break;
            }
        }
        if (habitsPresent) {
            mainLoop1:
            while (true) {
                System.out.println("Хотите отредактировать привычки? да/нет");
                String i = scanner.nextLine();
                switch (i) {
                    case "да":
                        showSettings();
                        break mainLoop1;
                    case "нет":
                        if (show.equals("1")) {
                            showList(service.readUserHabits_filterByCreationDate_earilerLast());
                        } else {
                            showList(service.readUserHabits_filterByCreationDate_earilerFirst());
                        }
                        break mainLoop1;
                    default:
                        System.out.println("Пожалуйста, введите да или нет.");
                        break;
                }
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

    public void showSettings() {
        System.out.println("""
                Выберите, что хотите сделать:
                1. Добавить новую привычку
                2. Отредактировать существующую
                3. Удалить привычку
                4. В главное меню
                """);
        mainLoop:
        while (true) {
            String i = scanner.nextLine();
            switch (i) {
                case "1":
                    createHabit();//+
                    break mainLoop;
                case "2":
                    if (service.readUserHabits_filterByCreationDate_earilerLast().isEmpty()) {
                        System.out.println("Ни одной привычки не было создано!");
                    } else {
                        updateHabit();
                    }//+
                    break mainLoop;
                case "3":
                    if (service.readUserHabits_filterByCreationDate_earilerLast().isEmpty()) {
                        System.out.println("Ни одной привычки не было создано!");
                    } else {
                        deleteHabit();//+
                    }
                    break mainLoop;
                case "4":
                    showMainMenu();
                    break mainLoop;
                default:
                    System.out.println("Пожалуйста, введите 1, 2, 3 или 4.");
                    break;
            }
        }
    }

    public void createHabit() {
        while (true) {
            System.out.println("Введите название привычки.");
            String name = scanner.nextLine();
            if (service.nameExists(name)) {
                System.out.println("Привычка с таким названием уже существует!");
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

        System.out.println("""
                Выберите, что хотите отредактировать:
                1. Название
                2. Описание
                3. Частоту
                4. В главное меню
                """);
        loop1:
        while (true) {
            String i = scanner.nextLine();
            switch (i) {
                case "1":
                    while (true) {
                        System.out.println("Введите новое название.");
                        String newName = scanner.nextLine();
                        String res = service.updateHabit_name(newName, name);
                        System.out.println(res);
                        if (res.equals("Успешно обновлено!")) {
                            break;
                        }
                    }
                    break loop1;
                case "2":
                    while (true) {
                        System.out.println("Введите новое описание.");
                        String newDescr = scanner.nextLine();
                        String res = service.updateHabit_description(newDescr, name);
                        System.out.println(res);
                        if (res.equals("Описание обновлено!")) {
                            break;
                        }

                    }
                    break loop1;
                case "3":
                    System.out.println("Введите новую частоту: ежедневно, еженедельно, раз в две недели, раз в три недели или ежемесячно.");
                    Habit.Frequency frequency = setFrequency();
                    System.out.println(service.updateHabit_frequency(frequency, name));
                    break loop1;
                case "4":
                    showSettings();
                    break;
            }
        }
    }


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
                    case "да":
                        service.deleteHabit(name);
                        System.out.println("Успешно удалено.");
                        break main;
                    case "нет":
                        showSettings();
                        break main;
                    default:
                        System.out.println("Пожалуйста, введите да или нет.");
                        break;
                }
            }
        }
    }

    public Habit.Frequency setFrequency() {
        Habit.Frequency frequency;
        aux:
        while (true) {
            String f = scanner.nextLine().toLowerCase();
            f = f.replaceAll("\s+", " ").trim();
            switch (f) {
                case "ежедневно":
                    frequency = Habit.Frequency.DAILY;
                    break aux;

                case "еженедельно":
                    frequency = Habit.Frequency.WEEKLY;
                    break aux;

                case "раз в две недели":
                    frequency = Habit.Frequency.FORTNIGHTLY;
                    break aux;

                case "раз в три недели":
                    frequency = Habit.Frequency.EVERYTHREEWEEKS;
                    break aux;

                case "ежемесячно":
                    frequency = Habit.Frequency.MONTHLY;
                    break aux;

                default:
                    System.out.println("Пожалуйста, введите одно из предложенных значений.");
                    break;
            }
        }
        return frequency;
    }

    private void showList(List<Habit> habits) {
        for (int i = 0; i < habits.size(); i++) {
            System.out.println(i + 1 + ". " + habits.get(i).getName());
        }
    }

    //отметить привычку
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

    //посмотреть статистику:
    public void showStatisticsMenu() {
        System.out.println("""
                Выберите действие:
                1. Получить статистику по привычке
                2. Получить общую статистику
                3. Назад
                """);
        if (service.readUserHabits_filterByCreationDate_earilerLast().isEmpty()) {
            System.out.println("Ни одной привычки не было создано!");
        } else {
            mainLoop:
            while (true) {
                String i = scanner.nextLine();
                switch (i) {
                    case "1":
                        showSingleHabitStats();
                        break mainLoop;
                    case "2":
                        showOverallStats();
                        break mainLoop;
                    case "3":
                        showMainMenu();
                        break mainLoop;
                    default:
                        System.out.println("Пожалуйста, введите 1, 2 или 3.");
                        break;
                }
            }
        }
    }

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
        System.out.println("""
                Что хотите просмотреть?
                1. Лучшую серию непрерывных отметок (streak)
                2. Последнюю серию непрерывных отметок
                3. Выполнение привычки в процентном соотношении
                4. В меню просмотра статистики
                """);
        mainLoop:
        while (true) {
            String i = scanner.nextLine();
            switch (i) {
                case "1":
                    System.out.print("Лучший стрейк: ");
                    System.out.print(statService.findLongestHabitStreak(name, period));
                    break mainLoop;
                case "2":
                    System.out.print("Последний стрейк: ");
                    System.out.println(statService.findLastHabitStreak(name, period));
                    break mainLoop;
                case "3":
                    System.out.print("Выполнение привычки в процентном соотношении: ");
                    System.out.printf("%.2f%%n", statService.getHabitCompleteness(name, period)[1]);
                    System.out.println();
                    break mainLoop;
                case "4":
                    showStatisticsMenu();
                    break mainLoop;
                default:
                    System.out.println("Пожалуйста, введите 1, 2, 3 или 4.");
                    break;
            }
        }
    }

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

    public void showOverallStats() {
        int period = initPeriod();
        System.out.println("""
                Что хотите просмотреть?
                1. Общее выполнение привычек в процентном соотношении
                2. Общее количество выполнения привычек
                3. Отчет по всем привычкам
                4. В меню просмотра статистики
                """);
        mainLoop:
        while (true) {
            String i = scanner.nextLine();
            switch (i) {
                case "1":
                    System.out.print("Общее выполнение привычек в процентном соотношении: ");
                    System.out.printf("%.2f%%n", (double) statService.formAllHabitsReport(period)[2]);
                    System.out.println();
                    break mainLoop;
                case "2":
                    System.out.print("Общее количество выполнения привычек: ");
                    System.out.println((int) statService.formAllHabitsReport(period)[1]);
                    break mainLoop;
                case "3":
                    System.out.println("=ОТЧЕТ=");
                    HashMap<String, double[]> report = (HashMap<String, double[]>) statService.formAllHabitsReport(period)[0];
                    System.out.printf("%-15s %-20s %-25s %-20s %-20s%n", "Название", "Количество отметок", "Количество отметок в процентах", "Длиннейший стрейк", "Текущий стрейк");
                    System.out.println("----------------------------------------------------------------------------------------------------------------");

                    for (Map.Entry<String, double[]> entry : report.entrySet()) {
                        String name = entry.getKey();
                        double[] values = entry.getValue();

                        System.out.printf("%-15s %-20.0f %-25.2f %-20.0f %-20.0f%n",
                                name,
                                values[0],
                                values[1],
                                values[2],
                                values[3]
                        );
                    }
                    System.out.print("Общий процент отметок: ");
                    System.out.printf("%.2f%%", (double) statService.formAllHabitsReport(period)[2]);
                    System.out.println();
                    System.out.println("Общее количество отметок: " + (int) statService.formAllHabitsReport(period)[1]);
                    break mainLoop;
                case "4":
                    showStatisticsMenu();
                    break mainLoop;
                default:
                    System.out.println("Пожалуйста, введите 1, 2, 3 или 4.");
                    break;
            }
        }
    }

    private String checkName(String name) {
        String i;
        if (!service.nameExists(name)) {
            System.out.println("Такая привычка не найдена!");
            System.out.println("Введите 1, если хотите продолжить, или 2, чтобы вернуться назад.");
            while (true) {
                i = scanner.nextLine();
                switch (i) {
                    case "1":
                        return i;
                    case "2":
                        showSettings();
                        return i;
                    default:
                        System.out.println("Пожалуйста, введите 1 или 2.");
                        break;
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


