package Habit.repository;

import Habit.model.Habit;
import User.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class HabitRepository {

    List<Habit> habitList = new ArrayList<>();

    public HabitRepository() {
    }

    public void addHabit(Habit habit) {
        habitList.add(habit);
    }

    public List<Habit> getAllHabits_byUser(User user) {
       return new ArrayList<>(user.getHabits().values());
    }

    public Habit findHabitByName(String name, User user) {
        return user.getHabits().get(name);
    }

    public void updateHabit_name(String name, Habit habit) {
        habit.setName(name);
    }

    public void updateHabit_description(String description, Habit habit) {
        habit.setDescription(description);
    }

    public void updateHabit_frequency(Habit.Frequency frequency, Habit habit) {
        habit.setFrequency(frequency);
    }


    public LinkedHashMap<LocalDate, Boolean> formHabitStatistics(Habit habit){
        return habit.getStatistics();
    }
}
