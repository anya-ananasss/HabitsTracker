package models;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Класс, описывающий модель для пользователя.
 *  * <p>
 *  *   * Содержит имя пользователя, адрес электронной почты, пароль в закодированном виде,
 *  обозначение роли, обозначение, активен ли аккаунт или заблокирован (true/false)
 *  и Map созданных пользователем привычек, где ключ - название привычки, значение - сама привычка.
 *  * </p>
 *
 *   @author Gureva Anna
 *   @version 1.0
 *   @since 19.10.2024
 */
@Getter
@Setter
public class User {
    private String name;
    private String email;
    private String password;
    private int role;
    private boolean isActive;
    private Map<String, Habit> habits;

    /**
     * Переопределение метода equals для корректного сравнения пользователей.
     * @param obj пользователь, с которым производится сравнение
     * @return true, если пользователи имеют одинаковое имя, адрес почты и пароль (являются одним и тем же пользователем), false - иначе.
     */
    @Override
    public boolean equals(Object obj) {
        User toWork = (User) obj;
        return (Objects.equals(this.email, toWork.email) &
                Objects.equals(this.name, toWork.name) &
                Objects.equals(this.password, toWork.password));
    }

    /**
     * Конструктор для создания нового пользователя. Map для хранения привычек создается в конструкторе.
     * @param name имя пользователя
     * @param email адрес электронной почты пользователя
     * @param password закодированный пароль
     * @param role роль пользователя (0 - администратор, 1 - обычный пользователь)
     */
    public User(String name, String email, String password, int role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = true;
        this.role = role;
        this.habits = new HashMap<>();
    }
}
