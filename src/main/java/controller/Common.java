package controller;

import af.spring.AfRestData;
import af.spring.AfRestError;
import com.alibaba.fastjson.JSONObject;
import data.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import service.UserService;
import service.PostService;
import utils.web.AfSimpleDownload;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class Common {
    public  static  File storeFile = null;
    public static File bookFile;
    public static File userFile;
    public static File tmpDir;
    public  static ExecutorService exector= Executors.newFixedThreadPool(3);
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;
    static {
  
        storeFile=new File("D:/store");
        storeFile.mkdirs();
        bookFile=new File(storeFile,"/book");
        bookFile.mkdirs();
        userFile=new File(storeFile,"/user");
        userFile.mkdirs();
        
    }
    ServletContext sc;
    String contextPath;

    public Common(ServletContext servletContext)
    {
        sc=servletContext;
        contextPath=sc.getContextPath();
        //零时文件
        Common.tmpDir=new File(sc.getRealPath("/tem"));
        tmpDir.mkdirs();
        System.out.println(sc.getRealPath("/"));
    }
    @GetMapping("/code")
    public void verifyCode(HttpServletResponse response,HttpSession session)
    {
        int width = 100;
        int height = 30;

        String data = "abcdefghijklmnopqrst01234567890";

        Random random = new Random();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.gray);
        graphics.fillRect(0, 0, width, height);

        StringBuffer code=new StringBuffer();
        graphics.setColor(Color.black);
        for (int i = 0; i < 4; i++) {

            int position = random.nextInt(data.length());
            String randomStr = data.substring(position, position + 1);
            graphics.drawString(randomStr, width / 5 * (i + 1), 15);
            code.append(randomStr);
        }
        session.setAttribute("code",code.toString());


        try {
            ImageIO.write(image, "jpg", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequestMapping("/code.do")
    public Object verify(@RequestBody JSONObject jreq, HttpSession session)
    {
        System.out.println("正在验证");
        String right=(String)session.getAttribute("code");
        String code= jreq.getString("code");
        if(right.equals(code))
        {
            return new AfRestData("");
        }
        else {
            return new AfRestError("wrong");
        }
    }
    //用户获取图像（包括管管理员和普通用户）
    @GetMapping("/user/photo")
    public Object getPhoto(HttpSession session)
    {

        String path=(String)session.getAttribute("path");
        if(path!=null){


            if(path!=null) {
                File file = new File(userFile, path);
                InputStream in;
                try {
                    in = new FileInputStream(file);
                    String contentType = sc.getMimeType(path);
                    AfSimpleDownload simpleDownload = new AfSimpleDownload(in, contentType);
                    return simpleDownload;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }



        }
        else {

             path=sc.getRealPath("/img/user.jpg");
            System.out.println("wen:"+path);
            File file=new File(path);
            InputStream in;
            try {
                in = new FileInputStream(file);
                AfSimpleDownload simpleDownload = new AfSimpleDownload(in, "image/jpg");
                return simpleDownload;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new AfRestError();
      
    }
    @GetMapping("/photo")
    public  Object getPhoto(@RequestParam("id") Integer id)
    {
        System.out.println(id);
        String path= userService.getPath(id);
        if(path!=null){

               System.out.println("path: "+path);
                File file = new File(userFile, path);
                System.out.println(file.getAbsolutePath());
                InputStream in;
                try {
                    in = new FileInputStream(file);
                    String contentType = sc.getMimeType(path);
                    System.out.println("文件路径："+file.getAbsolutePath()+" "+contentType);
                    AfSimpleDownload simpleDownload = new AfSimpleDownload(in, contentType);
                    return simpleDownload;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            

        }
        else {

            path=sc.getRealPath("/img/user.jpg");
            System.out.println("wen:"+path);
            File file=new File(path);
            InputStream in;
            try {
                in = new FileInputStream(file);
                AfSimpleDownload simpleDownload = new AfSimpleDownload(in, "image/jpg");
                return simpleDownload;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new AfRestError();
    }
    @RequestMapping("/photo/upload.do")
    public Object upload(HttpServletRequest request,HttpSession session) throws Exception
    {
        MultipartHttpServletRequest mhr = (MultipartHttpServletRequest) request;
        String path=(String)session.getAttribute("path");
        // 文件域
        MultipartFile mf = mhr.getFile("file"); // 表单里的 name='file'
        String url = "";
        if(mf != null && !mf.isEmpty())
        {
            String realName = mf.getOriginalFilename();
            String tmpName = Util.guid() + "." + Util.getSuffix(realName);

            File tmpFile = new File(tmpDir, tmpName);
            mf.transferTo(tmpFile);
            url=contextPath+"/"+"tem/"+tmpName;
            System.out.println("** Save To: " + tmpFile.getAbsolutePath());
            session.setAttribute("path",tmpName);
        }

        Map<String,Object> data = new HashMap<>();
        data.put("url", url);
        return new AfRestData(data);
    }
    public static void saveFileTo(File src,File des)
    {
        byte buffer []=new byte[4096];
        BufferedInputStream in=null;
        BufferedOutputStream out=null;
        try {
            in=new BufferedInputStream(new FileInputStream(src));;
            out=new BufferedOutputStream(new FileOutputStream(des));
            int x;
            while ((x=in.read(buffer))!=-1)
            {
                out.write(buffer,0,x);
                out.flush();
            };

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @RequestMapping("/index")
    public String goHome(HttpSession session, Model model)
    {
        model.addAttribute("titles",UserController.topTen);
       List<Post> list= postService.getPosts();
       model.addAttribute("posts",list);
       return  "index";
    }
    @RequestMapping({"/",""})
    public String forward()
    {
        return "redirect:/index";
    }

}
class TestHelloController implements org.springframework.web.servlet.mvc.Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("msg","hello world");
        modelAndView.setViewName("testHello");
        return modelAndView;
    }
}
