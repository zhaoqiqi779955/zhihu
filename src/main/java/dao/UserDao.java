package dao;

import data.User;

public interface UserDao {
    public void add(User user);
    public User find(Integer id);
    public boolean isExistent(Integer id);
    public String getName(Integer id);
    public String getPath(Integer id);

}
