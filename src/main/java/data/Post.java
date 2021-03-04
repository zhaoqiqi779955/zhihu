package data;

import af.sql.c3p0.AfSimpleDB;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import service.PostService;
import service.UserService;

import java.util.*;
public class Post {

    private Integer post_id;//帖子id
    private String title;//帖子标题
    private String content;//帖子内容
    private Integer author_id;//帖子作者id
    private String comments=null;//评论
    private String authorName;
    private List<Comment> commentList=new ArrayList<>();
    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void resolve()
    {
        if(comments==null) return;
        setCommentList(JSONArray.parseArray(getComments(),Post.Comment.class));
    }
    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
    public static class Comment{

        Integer userID;//评论用户id
        String words;//评论内容
        String userName;

        public Comment(Integer userID, String words) {
            this.userID = userID;
            this.words = words;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }


        public Comment()
        {

        }

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }
}
