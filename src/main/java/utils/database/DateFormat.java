package utils.database;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
    private static SimpleDateFormat sdf= new SimpleDateFormat("yyyy年MM月dd日");
    private static SimpleDateFormat dbDateSdf= new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf_time=new SimpleDateFormat("yyyy-MM-dd   hh-mm-ss");
    public static String simpleDate(Date date){
        if(date==null) return "";
        return  sdf.format(date);
    }
    public static String DBDate(Date date){
         if (date==null) return "";
       return dbDateSdf.format(date);
    }
    public  static  String simpleTime(Date date){
        if(date==null) return "";
        return sdf_time.format(date);
    }
}
