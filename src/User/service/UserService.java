package User.service;

import Habit.model.Habit;
import User.model.User;
import User.repository.UserRepository;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class UserService {

    User user;

    public void setUser(User user) {
        this.user = user;
    }

    public UserService(User user, UserRepository repository) {
        this.user = user;
        this.repository = repository;
    }
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    private final UserRepository repository;
    private final byte[] salt = generateSalt();

    public boolean createUser(String name, String email, String password) {
        String encryptedPass = encrypt(password);
        User user = new User(name, email, encryptedPass, 1);
        repository.addUser(user);
        repository.addEmail(email);
        return true;
    }


    public Object[] loginUser(String email, String password) {
        User foundUser = repository.readUser_byEmail(email);
        if (foundUser == null){
            return new Object[]{false, "unknownEmail"};
        }
        if (comparePass(password, email)) {
            return new Object[]{true, foundUser.getName()};
        } else {
            return new Object[]{false, "wrongPass"};
        }
    }


    private byte[] generateSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    public String encrypt(String data) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            char encryptedChar = (char) (c + salt[0]);
            encrypted.append(encryptedChar);
        }
        return encrypted.toString();
    }


    private String decrypt(String encryptedData) {
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < encryptedData.length(); i++) {
            char c = encryptedData.charAt(i);
            char decryptedChar = (char) (c - salt[0]);
            decrypted.append(decryptedChar);
        }
        return decrypted.toString();
    }

    public boolean comparePass(String passToCheck, String usersEmail) {
        User foundUser = repository.getUsers().stream().
                filter(user -> Objects.equals(user.getEmail(), usersEmail)).findAny().get();
        String userPass = decrypt(foundUser.getPassword());
        return userPass.equals(passToCheck);
    }


    public String nameCheck(String name) {
        if (name.isEmpty()) {
            return "Имя не может быть пустым! Пожалуйста, введите имя!";
        } else {
            return name;
        }
    }

    public String emailCheck(String email) {
        if (!isEmailValid(email)) {
            return "Пожалуйста, введите корректный email!";
        } else {
            if (repository.getEmails().contains(email)) {
                return "Пользователь с таким email уже зарегистрирован!";
            }
        }
        return email;
    }

    private boolean isEmailValid(String email) {
        String emailRegex =
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        return emailPattern.matcher(email).matches();
    }

    public String updateUser_changeName(String newName, String email){
        User user = repository.readUser_byEmail(email); //по логике проверка на нул не нужна (авторизированный пользователь)
        String res = nameCheck(newName);
        if (res.equals(newName)){
            user.setName(newName);
        }
        return res;
    }
    public String updateUser_changeEmail(String newEmail, String oldEmail){
        User user = repository.readUser_byEmail(oldEmail);
        String res = emailCheck(newEmail);
        if (res.equals(newEmail)){
            user.setEmail(newEmail);
        }
        return res;
    }
    public void updateUser_changePassword(String newPass, String email){
        User user = repository.readUser_byEmail(email);
        String encryptedPass = encrypt(newPass);
        user.setPassword(encryptedPass);
    }

    public void deleteUser_byEmail(String email){
        repository.deleteUser_byEmail(email);
    }

    public void addUserHabit(Habit habit) {
        this.user.getHabits().put(habit.getName(), habit);
    }
    public void deleteUserHabit(String habitName) {
        this.user.getHabits().remove(habitName);
    }
    public void updateHabit_name(String oldName, Habit manipulated){
        this.user.getHabits().remove(oldName);
        this.user.getHabits().put(manipulated.getName(), manipulated);
    }
    public User readUser_byEmail(String email){ //с ридхэбит то же самое сделать
        return repository.readUser_byEmail(email);
    }
    public Set<User> getAllUsers(){
        return  repository.getUsers();
    }

}
