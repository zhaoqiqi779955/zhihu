package service;

import af.sql.c3p0.AfSimpleDB;
import dao.UserDao;
import data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.database.DateFormat;
import utils.database.SqlUpdate;
@Service
public class UserService {
    @Autowired
    UserDao userDao;
    /*
    根据id判断用户是否存在
     */
    public  boolean isExistent(int id){
       return userDao.isExistent(id);
    }
    /*
    获取指定id的borrower对象
     */
    public  User find(int id){
        return  userDao.find(id);
    }
    /*
    项数据库添加一个borrower,返回为true表示添加成功
     */
    public  void add(User user){
       userDao.add(user);
    }
    /*
    更新个人信息
     */
    public static void  update(User user){
        SqlUpdate asu=new SqlUpdate("borrower");
        asu.add("name", user.getName());
        asu.add("adr", user.getAdr());
        asu.add3("sex", user.getSex());
        asu.add2("birth", DataUtil.isNull(user.getBirth()) ? null: DateFormat.DBDate(user.getBirth()));
        asu.add2("tel", user.getTel());
       
        asu.add2("pw", user.getPw());
        String s1 = asu + " where borrower_id=" + user.getId();
        try {
            AfSimpleDB.execute(s1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    根据id删除借阅者
     */
    public  static  void delete(int id){
        String sql="delete from borrower where borrower_id="+id;
        try {
            AfSimpleDB.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public   String getName(Integer userID)
    {
        return userDao.getName(userID);

    }
    public    String getPath(Integer userID)
    {
        return userDao.getPath(userID);
    }

}
