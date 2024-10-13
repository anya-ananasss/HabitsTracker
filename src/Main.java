import User.controller.UserController;
import User.repository.UserRepository;
import User.service.UserService;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);

        userController.CREATE_ADMIN();
        userController.showGreetingScreen();
    }
}