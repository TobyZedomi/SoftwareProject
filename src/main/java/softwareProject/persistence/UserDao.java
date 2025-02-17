package softwareProject.persistence;

import softwareProject.business.User;

public interface UserDao {

    public int registerUser(User newUser);

    public User login(String username, String password);

    public User findUserByUsername(String username);

    public User findUserByThereEmail(String email);

    public User findUserByUsername2(String username);
}
