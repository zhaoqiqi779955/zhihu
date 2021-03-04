package dao;

import data.Post;
import javafx.geometry.Pos;

import java.util.List;

public interface PostDao {
    public List<Post> findOnTitle(String title);
    public void add(Post post);
    public void updateComment(Post post);
    public List<Post> getPosts();
    public Post find(Integer id);
}
