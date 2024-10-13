package Habit.service;

import Habit.model.Habit;
import Habit.repository.HabitRepository;
import User.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class HabitStatisticsService {
    public final HabitRepository repository;

    public final User user;

    public HabitStatisticsService(HabitRepository repository, User user) {
        this.repository = repository;
        this.user = user;
    }

    public LinkedHashMap<LocalDate, Boolean> formStatistics(String habitName) {
        Habit examined = repository.findHabitByName(habitName, this.user);
        return repository.formHabitStatistics(examined);
    }

    public int findLongestHabitStreak(String habitName, int period) {
        Habit examined = repository.findHabitByName(habitName, this.user);
        int maxStreak = 0;
        int currStreak = 0;

        if (period==-1 || period >= examined.getStatistics().size()) {
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
            if (currStreak > maxStreak) {
                maxStreak = currStreak;
            }
        } else{
            for (int i = examined.getStatistics().size() - 1; i >= examined.getStatistics().size() - period; i--) {
                if (examined.getStatistics().values().stream().collect(Collectors.toList()).get(i)) {
                    currStreak++;
                } else {
                    if (currStreak > maxStreak) {
                        maxStreak = currStreak;
                    }
                    currStreak = 0;
                }
            }
            if (currStreak > maxStreak) {
                maxStreak = currStreak;
            }
        }
        return maxStreak;

    }

    public int findLastHabitStreak(String habitName, int period) {
        Habit examined = repository.findHabitByName(habitName, this.user);
        int currStreak = 0;
        List<Boolean> values = new ArrayList<>(examined.getStatistics().values());
        if (period==-1 || period >= examined.getStatistics().size()) {
            for (int i = values.size() - 1; i >= 0; i--) {
                if (values.get(i)) {
                    currStreak++;
                } else {
                    break;
                }
            }
        } else{
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

    public Double[] getHabitCompleteness(String habitName, int period) {
        int CODE_FOR_ALL = -1;

        Habit examined = repository.findHabitByName(habitName, this.user);
        if (period>examined.getStatistics().size()){
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

    private int getAllHabitsCompleteness(int period) {
        List<Habit> habits = repository.getAllHabits_byUser(this.user);
        int overallMarked = 0;
        for (Habit habit : habits) {
            overallMarked += getHabitCompleteness(habit.getName(), period)[0];
        }
        return overallMarked;
    }

    public Object[] formAllHabitsReport(int period) {
        Map<String, double[]> report = new HashMap<>();
        List<Habit> habits = repository.getAllHabits_byUser(this.user);
        if (habits.isEmpty()){
            String message = "Ни одной привычки еще не было создано!";
            return new Object[]{message};
        }
        if (period==-1){
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
       double overallCompleteness_percentage = (getAllHabitsCompleteness(period)*100.0)/(period*habits.size());

       return new Object[]{report, overallCompleteness, overallCompleteness_percentage};
    }
}
