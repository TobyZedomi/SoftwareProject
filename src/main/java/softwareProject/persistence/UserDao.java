package softwareProject.persistence;

import softwareProject.business.User;

import java.util.ArrayList;

public interface UserDao {

    public int registerUser(User newUser);

    public User login(String username, String password);

    public User findUserByUsername(String username);

    public User findUserByThereEmail(String email);

    public ArrayList<User> findUserByUsername2(String username);

    boolean updatePassword(String email, String newPassword);


}
