package Habit.model;


import User.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Habit {
    private String name;
    private String description; //ограничение - 200 символов
    private final LocalDateTime createdAt;
    private LinkedHashMap<LocalDate, Boolean> statistics;

    private final User user;


    private Frequency frequency;


    public Habit(String name, String description, LocalDateTime createdAt, Frequency frequency, User user) {
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.user = user;
        this.frequency = frequency;
        this.statistics = new LinkedHashMap<>();

        scheduleDailyUpdate();
    }

    public enum Frequency {
        DAILY,
        WEEKLY,
        FORTNIGHTLY,
        EVERYTHREEWEEKS,

        MONTHLY
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LinkedHashMap<LocalDate, Boolean> getStatistics() {
        return statistics;
    }

    public void setStatistics(LinkedHashMap<LocalDate, Boolean> statistics) {
        this.statistics = statistics;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    private void scheduleDailyUpdate() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        long initialDelay = calculateInitialDelay();
        long period = 24 * 60 * 60;

        scheduler.scheduleAtFixedRate(() -> statistics.put(LocalDate.now(), false), initialDelay, period, TimeUnit.SECONDS);
    }

    private long calculateInitialDelay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(1);

        return java.time.Duration.between(now, nextRun).getSeconds();
    }

}
