package User.repository;

import User.model.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserRepository {
    private Set<User> users;
    private Set<String> emails;


    public UserRepository() {
        this.users = new HashSet<>();
        this.emails = new HashSet<>();
    }

    public Set<User> getUsers() {
        return users;
    }

    public Set<String> getEmails() {
        return emails;
    }

    public boolean addEmail(String email){
        return emails.add(email);
    }
    public boolean addUser(User user){
        return users.add(user);
    }

    public User readUser_byEmail(String email) {
        return users.stream().filter(user -> Objects.equals(user.getEmail(), email)).findAny().orElse(null);
    }
    public void deleteUser_byEmail (String email){
        users.remove(readUser_byEmail(email));
        emails.remove(email);
    }
}
