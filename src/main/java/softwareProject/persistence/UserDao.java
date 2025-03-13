package softwareProject.persistence;

import softwareProject.business.User;

import java.util.ArrayList;

public interface UserDao {

    public int registerUser(User newUser);

    public User login(String username, String password);

    public User findUserByUsername(String username);

    public User findUserByThereEmail(String email);

    public ArrayList<User> findUserByUsername2(String username);

    public int updatePassword(String email, String newPassword);

    public int updateUserImage(String user,String image);

    public int updateDisplayName(String username,String displayName);


}
