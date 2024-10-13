package Habit.service;

import Habit.model.Habit;
import Habit.repository.HabitRepository;
import User.model.User;
import User.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class HabitService {
    public HabitService(HabitRepository repository, User user, UserService userService) {
        this.repository = repository;
        this.user = user;
        this.userService = userService;
    }

    public final HabitRepository repository;

    public final User user;
    public final UserService userService;


    public String createHabit(String name, String description, Habit.Frequency frequency) {
        LocalDateTime createdAt = LocalDateTime.now();
        Habit habit = new Habit(name, description, createdAt, frequency, this.user);
        repository.addHabit(habit);
        userService.addUserHabit(habit);
        return "Привычка успешно создана!";
    }

    public boolean nameExists(String name) {
        return user.getHabits().containsKey(name);
    }

    public String updateHabit_name(String newName, String oldName) {//это все не должно быть уникальным вообще, но уникальным в границах юзера
        Habit manipulated = repository.findHabitByName(oldName, this.user);
        //проверка на уникальность в рамках юзера
        if (newName.isEmpty()) {
            return "Название не может быть пустым!";
        } else {
            if (user.getHabits().containsKey(newName)) {
                return "Привычка с таким названием уже существует!";
            } else {
                repository.updateHabit_name(newName, manipulated);
                userService.updateHabit_name(oldName, manipulated);
                return "Успешно обновлено!";
            }
        }
    }

    public String updateHabit_description(String newDescription, String habitName) {
        Habit manipulated = repository.findHabitByName(habitName, this.user);
        if (newDescription.toCharArray().length > 200) {
            return "Описание не должно превышать 200 символов!";
        } else {
            repository.updateHabit_description(newDescription, manipulated);
            return "Описание обновлено!";
        }
    }

    public String updateHabit_frequency(Habit.Frequency newFrequency, String habitName) {
        Habit manipulated = repository.findHabitByName(habitName, this.user);
        repository.updateHabit_frequency(newFrequency, manipulated);
        return "Успешно обновлено!";
    }

    public List<Habit> readUserHabits_filterByCreationDate_earilerFirst() {
        List<Habit> habits = repository.getAllHabits_byUser(this.user);
        return sortHabitsByCreation(habits, true);
    }

    public List<Habit> readUserHabits_filterByCreationDate_earilerLast() {
        List<Habit> habits = repository.getAllHabits_byUser(this.user);
        return sortHabitsByCreation(habits, false);
    }
    public List<Habit> sortHabitsByCreation(List<Habit> habits, boolean ascending) {

        habits.sort(Comparator.comparing(Habit::getCreatedAt));

        if (!ascending) {
            Collections.reverse(habits);
        }

        return habits;
    }

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
    public LinkedHashMap<String, LocalDate> sortHabits_marked(boolean earlier) {
        List<Habit> habits = repository.getAllHabits_byUser(this.user);
        LinkedHashMap<String, LocalDate> habitsSorted = new LinkedHashMap<>();
        List<Habit> habitsWithDates = new ArrayList<>();

        for (Habit habit : habits) {
            LocalDate latestTrueDate = getLatestTrueDate(habit);
            if (latestTrueDate != null) {
                habitsWithDates.add(habit);
                habitsSorted.put(habit.getName(), latestTrueDate);
            }
        }


        habitsWithDates.sort((h1, h2) -> {
            LocalDate date1 = getLatestTrueDate(h1);
            LocalDate date2 = getLatestTrueDate(h2);
            if (earlier){
                return date2.compareTo(date1);
            } else {
                return date1.compareTo(date2);
            }
        });


        habitsSorted.clear();
        for (Habit habit : habitsWithDates) {
            habitsSorted.put(habit.getName(), getLatestTrueDate(habit));
        }

        for (Habit habit : habits) {
            if (!habitsWithDates.contains(habit)) {
                habitsSorted.put(habit.getName(), null);
            }
        }

        return habitsSorted;
    }
    public String deleteHabit(String deletingName) {
        List<Habit> habits = repository.getAllHabits_byUser(this.user);
        Habit deleting = repository.findHabitByName(deletingName, this.user);
        habits.remove(deleting);
        this.userService.deleteUserHabit(deletingName);
        return "Удаление прошло успешно";
    }

    public String markHabit(String habitName) {
        Habit marking = repository.findHabitByName(habitName, this.user);

        Habit.Frequency frequency = marking.getFrequency();
        if (!marking.getStatistics().isEmpty()) {
            LocalDate lastDate = LocalDate.now();

            for (LocalDate date : marking.getStatistics().keySet()) {
                if (marking.getStatistics().get(date)) {
                    lastDate = date; // последняя отметка с true
                }
            }
            LocalDate today = LocalDate.now();
            boolean doMark = true;

            String sentenceEnd = "сегодня";
            switch (frequency) {
                case DAILY:
                    if (today.isEqual(lastDate)) {
                        doMark = false;
                    }
                    break;
                case WEEKLY:
                    LocalDate minused = today.minusWeeks(1);
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
                marking.getStatistics().remove(today);
                marking.getStatistics().put(today, true);
                return "Отметка проставлена!";
            } else {
                return "Еще не время! Вы уже отмечались " + sentenceEnd + ".";
            }
        } else {
            marking.getStatistics().put(LocalDate.now(), true);
            return "Отметка проставлена!";
        }
    }

}