package controller;

import af.spring.AfRestData;
import af.spring.AfRestError;
import com.alibaba.fastjson.JSONObject;
import data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.UserService;
import service.PostService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Controller
public class UserController {

    public static  Map<Integer,String> message=new HashMap<>();
    static {
        message.put(-1,"服务器异常");
        message.put(0,"成功");
        message.put(1,"用户不存在");
        message.put(2,"存在不良记录");
        message.put(3,"已达最大借书量");
        message.put(4,"图书余量不足");
        message.put(5,"重复预约");
        message.put(6,"重复借阅");
    }
   public static List<String> topTen=PostService.getTitles();
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;


/*
处理注册
 */
    @PostMapping("/register.do")
    public  Object register(@RequestBody JSONObject jreq, HttpSession session)
    {

         String tem;
         Integer id=jreq.getInteger("id");
         if(userService.isExistent(id)){
             return new AfRestError("账号已存在");
         }
         String name=jreq.getString("name");
         tem=jreq.getString("sex");
         Boolean sex=tem.endsWith("女");
         Date birth =jreq.getDate("birth");
         String tel=jreq.getString("tel");
         String password=jreq.getString("password");

        User user =new User();
        user.setName(name);
        user.setPw(password);
        user.setId(id);
        user.setBirth(birth);
        user.setSex(sex);
        user.setTel(tel);
        String path=(String) session.getAttribute("path");
        String fileName;
        if(path!=null)
        {
            String suffix= Util.getSuffix(path);
            fileName="head."+suffix;
            String dbPath=String.format("%010d",id)+"/"+fileName;
            user.setPath(dbPath);
            File src=new File(Common.tmpDir,path);
            File desDir=new File(Common.userFile,String.format("%010d",id));
            desDir.mkdirs();
            File desFile=new File(desDir,fileName);
            Common.exector.submit(new SaveFileTask(src,desFile));
            session.setAttribute("path",String.format("%010d",id)+"/"+fileName);
        }
        userService.add(user);
        //保存用户信息到session

        session.setAttribute("userName",name);
        session.setAttribute("userID",id);
        return new AfRestData("");
    }
/*
注册
 */
    @GetMapping("/register")
    public String register()
    {
        return "register";
    }



    //-------------登录---------------
    @GetMapping("/login")
    public String borrowerLogin(){

        return "login_page";
    }

    @PostMapping("/login.do")
    public Object borrowerLogin(@RequestBody JSONObject jreq, Model model, HttpServletRequest req, HttpSession session){

        System.out.println("正在处理登录");
        Integer userID = jreq.getInteger("userID");
        String password = jreq.getString("password");
        User user =userService.find(userID);
        if(user ==null){
            return  new AfRestError(-1,"用户不存在");
        }
        if(!user.getPw().equals(password)){
            return new AfRestError(-2,"密码错误");
        }
        String userName= user.getName();
        session.setAttribute("userID", userID);
        session.setAttribute("userName",userName);
        session.setAttribute("level",1);
        session.setAttribute("path", user.getPath());
        System.out.println(userID+" "+userName);
        System.out.println("登陆成功");
        return new AfRestData("");
    }

    //-------------个人信息---------------

   @RequestMapping("/search")
   public String search(@RequestParam("title") String title,Model model)
   {
       model.addAttribute("titles", UserController.topTen);
       List<Post> list= postService.findOnTitle(title);
       model.addAttribute("posts",list);
       return  "index";
   }
    //获取帖子
    @GetMapping("/post")
    public String posts(@RequestParam("id") Integer id,Model model)
    {
        model.addAttribute("titles",UserController.topTen);
        Post post=postService.getPost(id);
        Integer author=post.getAuthor_id();
        model.addAttribute("post",post);
        String name= userService.getName(author);
        model.addAttribute("name",name);
        return "/post";
    }
    //提交评论
    @PostMapping("/comment.do")
    public Object comment(@RequestBody JSONObject jreq,HttpSession session)
    {

        Integer userID=(Integer) session.getAttribute("userID");
        if(userID==null) return new AfRestError("未登录");
        Integer post_id=jreq.getInteger("post_id");
        String  comment=jreq.getString("comment");
        Post post=postService.getPost(post_id);
        List list=post.getCommentList();
        list.add(new Post.Comment(userID,comment));
        postService.updateComments(post);
        return new AfRestData();
    }




    @GetMapping("/info")
    public  String personInfo(Model model,HttpSession session)
    {
        if(session.getAttribute("userID")==null) return "redirect:/login";
        Integer userID=(Integer) session.getAttribute("userID");
        User user = userService.find(userID);
        model.addAttribute("user", user);
        model.addAttribute("titles",UserController.topTen);
        return "personal_page";
    }

    @GetMapping("/write")
    public String writeLog(HttpSession session,Model model)
    {

        model.addAttribute("titles",UserController.topTen);
        if(session.getAttribute("userID")!=null)
          return "write";
        else return "login_page";
    }

    @PostMapping("/write.do")
    public Object saveLog(@RequestBody JSONObject jreq,HttpSession session)
    {
        String title=jreq.getString("title");
        String content=jreq.getString("content");
        System.out.println(title);
        System.out.println(content);
        topTen.add(0,title);
        Post post=new Post();
        post.setAuthor_id((Integer)session.getAttribute("userID"));
        post.setContent(content);
        post.setTitle(title);
        postService.add(post);
        return  new AfRestData("ok");
    }
    @RequestMapping("/exit")
    public String exit(HttpSession session)
    {
        session.invalidate();
        return  "redirect:/index";
    }


}

class SaveFileTask implements Runnable {
    File src = null;
    File des = null;

    public SaveFileTask(File src, File des) {
        this.src = src;
        this.des = des;
    }

    @Override
    public void run() {
        Common.saveFileTo(src, des);
    }

}
