package service;

import dao.PostDao;
import data.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.database.JdbcPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Service
public class PostService {
    @Autowired
    UserService userService;
    @Autowired
    PostDao postDao;
    public  Post getPost(Integer post_id)
    {

       return  postDao.find(post_id);
    }

    public  List<Post> getPosts()
    {
        return postDao.getPosts();
    }
    public static List<String> getTitles()
    {
        List<String> list=new ArrayList<>();
        String sql="select title from post limit 10";
        try (Connection conn = JdbcPool.getConnection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData res=rs.getMetaData();

            while(rs.next())
            {
                for (int i = 1; i <= res.getColumnCount(); i++) {
                    System.out.println(rs.getString(1));
                    list.add(rs.getString(1));
                }

            }
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }
    public List<Post> findOnTitle(String title)
    {
        return  postDao.findOnTitle(title);
    }
    public void add(Post post)
    {
        postDao.add(post);
    }
    public  void updateComments(Post post)
    {
        postDao.updateComment(post);
    }
}
