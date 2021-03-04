package utils.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Bean {

    /**获取beans中配置的对象，beanXML为配置文件*/
    public static Object getBean(String beanXML,String id,Object obj)
    {

        ApplicationContext context=new ClassPathXmlApplicationContext(beanXML);
        Object object=context.getBean(id,obj);
        return object;

    }
}
