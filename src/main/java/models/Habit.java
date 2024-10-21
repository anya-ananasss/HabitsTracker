package models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
/**
 * Класс, описывающий модель для привычки.
 * <p>
 *   Содержит название и описание для привычки, время ее создания, статистику в формате LinkedHashMap,
 *   где ключ - дата отметки, значение - сама отметка (отмечено/не отмечено), пользователя, которому принадлежит привычка,
 *   заданную частоту выполнения привычки.
 * </p>
 *
 *  @author Gureva Anna
 *  @version 1.0
 *  @since 19.10.2024
 *
 */
@Getter
@Setter
public class Habit {
    private String name;
    private String description;
    private final LocalDateTime createdAt;
    private LinkedHashMap<LocalDate, Boolean> statistics;
    private final User user;
    private Frequency frequency;


    /**
     * Конструктор для создания новой привычки. LinkedHashMap для хранения статистики создается внутри конструктора.
     * @param name название привычки
     * @param description описание для привычки (не более 200 символов)
     * @param createdAt дата создания привычки
     * @param user пользователь, создавший привычку
     * @param frequency частота выполнения привычки
     */

    public Habit(String name, String description, LocalDateTime createdAt,  User user, Frequency frequency) {
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.user = user;
        this.frequency = frequency;
        this.statistics = new LinkedHashMap<>();
    }

    /**
     * Перечисление, содержащее доступные варианты частоты для привычек.
     */
    public enum Frequency {
        DAILY,
        WEEKLY,
        FORTNIGHTLY,
        EVERYTHREEWEEKS,
        MONTHLY
    }
}
