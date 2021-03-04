package dao;

import af.sql.c3p0.AfSimpleDB;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import data.Post;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import service.UserService;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@Repository
public class PostDaoImpl implements PostDao{
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserService userService;
    @Override
    public List<Post> findOnTitle(String title) {
        String sql="select * from post where title=?";
        try {
            return  jdbcTemplate.query(sql,new BeanPropertyRowMapper<Post>(Post.class),title);
        }
       catch (Exception e){
            return  null;
       }
    }

    @Override
    public void add(Post post) {
        String jsonstr=null;
        if(post.getCommentList().size()>0){
            jsonstr=JSON.toJSONString(post.getCommentList(),SerializerFeature.PrettyFormat);
        }
        String  sql="insert into post(title,content,author_id,comments) values(?,?,?,?)";
        System.out.println("title："+post.getTitle());
        jdbcTemplate.update(sql,post.getTitle(),post.getContent(),post.getAuthor_id(),jsonstr);


            String query="select @@IDENTITY";


    }

    @Override
    public void updateComment(Post post) {
        String sql = "update post set comments=? where post_id=?";
        String jsonStr = "null";
        List<Post.Comment> commentList = post.getCommentList();
        if (commentList.size() > 0) {
            jsonStr = JSON.toJSONString(commentList, SerializerFeature.PrettyFormat);
        }

        jdbcTemplate.update(sql,jsonStr,post.getPost_id());
    }

    @Override
    public List<Post> getPosts() {
        String sql="select * from post limit 10";
        try
        {
            return jdbcTemplate.query(sql,new BeanPropertyRowMapper<Post>(Post.class));
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return  null;
        }


    }

    @Override
    public Post find(Integer id) {
        String sql="select * from post where post_id=?";
        try
        {
           Post post= jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<Post>(Post.class),id);
           post.resolve();
           List<Post.Comment> list=post.getCommentList();
            Iterator<Post.Comment> iterator=list.iterator();
            iterator.forEachRemaining((Post.Comment e)->{
                e.setUserName(userService.getName(e.getUserID()));
            });
           return post;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }
}
