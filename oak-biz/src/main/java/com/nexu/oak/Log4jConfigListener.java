package com.nexu.oak;

import com.baidu.disconf.client.listener.Log4jDisconfConfigListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
 
public class Log4jConfigListener extends Log4jDisconfConfigListener implements ServletContextListener {
 
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.log4jInitialized();
    }
 
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
 
    }
}