package User.model;

import Habit.model.Habit;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User {
    private String name;
    private String email;
    private String password;
    private int role; //0 - администратор, 1 - пользователь

    private boolean isActive;
    private Map<String, Habit> habits;

    @Override
    public boolean equals(Object obj) {
        User toWork = (User) obj;
        return (Objects.equals(this.email, toWork.email) &
                Objects.equals(this.name, toWork.name) &
                Objects.equals(this.password, toWork.password));
    }

    public User(String name, String email, String password, int role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = true;
        this.role = role;
        this.habits = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Map<String, Habit> getHabits() {
        return habits;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
