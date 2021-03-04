package dao;

import data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import utils.database.DateFormat;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public void add(User user) {
       String sql="insert into user(id,name,sex,birth,adr,path,tel,pw) values(?,?,?,?,?,?,?,?)";
       Object [] args={user.getId(),user.getName(),user.getSex(), DateFormat.DBDate(user.getBirth()),user.getAdr(),user.getPath(),user.getTel(),user.getPw()};
       jdbcTemplate.update(sql,args);
    }

    @Override
    public User find(Integer id) {
        User user;
        String sql="select * from user where id=?";
        try {
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), id);
        }
        catch (Exception e){
            return  null;
        }
        return user;
    }
    @Override
    public boolean isExistent(Integer id)
    {
        String sql="select id from user where id=?";
        try {
            Integer user_id = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Integer>(Integer.class), id);
        }
        catch (Exception e){
            return  false;
        }
       return true;
    }

    @Override
    public String getName(Integer id) {
        String sql="select name from user where id=?";
        String name="";
        try {
            name = jdbcTemplate.queryForObject(sql, String.class, id);
        }
        catch (Exception e)
        {
            return name;
        }
        return  name;

    }

    @Override
    public String getPath(Integer id) {
        String sql="select path from user where id=?";
        String path=jdbcTemplate.queryForObject(sql, String.class,id);
        return  path;
    }
}
