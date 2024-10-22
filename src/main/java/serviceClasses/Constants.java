package serviceClasses;

/**
 * Класс, хранящий текстовые блоки для контроллеров в виде констант
 * <p>
 * * @author Gureva Anna
 * * @version 1.0
 * * @since 19.10.2024
 * </p>
 */
public class Constants {

    /**
     * Текстовый блок для контроллера привычек: показать меню для работы с привычками.
     */
    public static final String HABIT_MAIN_MENU_TEXT_BLOCK = """
                    Выберите, что вы хотите сделать:
                    1. Просмотреть привычки
                    2. Отметить выполнение привычки
                    3. Просмотреть статистику
                    4. Отредактировать привычки
                    5. Назад
                    """;
    /**
     * Текстовый блок для контроллера привычек: показать меню для фильтрации привычек.
     */
    public static final String SHOW_HABITS_TEXT_BLOCK = """
                Выберите, как нужно отфильтровать привычки:
                1. Сначала старые
                2. Сначала новые
                3. По статусу отметок(сначала недавно отмеченные);
                4. По статусу отметок(сначала давно отмеченные);
                5. Назад""";
    /**
     * Текстовый блок для контроллера привычек: показать меню для настроек привычки.
     */
    public static final String SHOW_HABIT_SETTINGS_TEXT_BLOCK = """
                Выберите, что хотите сделать:
                1. Добавить новую привычку
                2. Отредактировать существующую
                3. Удалить привычку
                4. В главное меню""";
    /**
     * Текстовый блок для контроллера привычек: показать меню для редактирования привычки.
     */
    public static final String UPDATE_HABITS_TEXT_BLOCK = """
                Выберите, что хотите отредактировать:
                1. Название
                2. Описание
                3. Частоту
                4. В главное меню""";
    /**
     * Текстовый блок для контроллера привычек: показать меню для выбора режима просмотра статистики.
     */
    public static final String SHOW_STATISTICS_TEXT_BLOCK = """
                Выберите действие:
                1. Получить статистику по привычке
                2. Получить общую статистику
                3. Назад""";
    /**
     * Текстовый блок для контроллера привычек: показать меню для выбора статистики по одной привычке.
     */
    public static final String SHOW_SINGLE_HABIT_STATS_TEXT_BLOCK = """
                Что хотите просмотреть?
                1. Лучшую серию непрерывных отметок (streak)
                2. Последнюю серию непрерывных отметок
                3. Выполнение привычки в процентном соотношении
                4. В меню просмотра статистики""";
    /**
     * Текстовый блок для контроллера привычек: показать меню для выбора общей статистики.
     */
    public static final String SHOW_OVERALL_STATS_TEXT_BLOCK = """
                Что хотите просмотреть?
                1. Общее выполнение привычек в процентном соотношении
                2. Общее количество выполнения привычек
                3. Отчет по всем привычкам
                4. В меню просмотра статистики""";
    /**
     * Текстовый блок для контроллера пользователя: показать меню администратора.
     */
    public static final String SHOW_ADMIN_MAIN_PAGE_TEXT_BLOCK = """
                    Выберите, что хотите сделать:
                    1. Просмотреть пользователей
                    2. Заблокировать пользователя
                    3. Разблокировать пользователя
                    4. Удалить пользователя
                    5. Выйти из аккаунта
                    """;
    /**
     * Текстовый блок для контроллера пользователя: показать меню изменения данных для пользователя.
     */
    public static final String UPDATE_USER_TEXT_BLOCK = """
                Что вы хотите изменить?
                1. Имя пользователя
                2. Адрес электронной почты
                3. Пароль""";
}
