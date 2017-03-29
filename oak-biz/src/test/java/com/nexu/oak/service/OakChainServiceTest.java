package com.nexu.oak.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.nexu.oak.dao.OakTableMetaInfoDAO;
import com.nexu.oak.dao.OakTaskDAO;
import com.nexu.oak.dto.OakTableMetaInfo;
import com.nexu.oak.dto.OakTask;
import com.nexu.oak.dto.enums.OakDBTypeEnum;
import com.nexu.oak.dto.enums.OakTaskStatusEnum;
import com.nexu.oak.service.OakChainService;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = { "file:src/main/resources/META-INF/spring/oak-context.xml",
        "file:src/main/resources/META-INF/spring/oak-db.xml" })
public class OakChainServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    
    @Autowired
    private OakChainService service;
    @Autowired
    private OakTaskDAO taskDao;
    @Autowired
    private OakTableMetaInfoDAO metaInfoDao;
    
    private OakTask task=null;
    private OakTableMetaInfo metaInfo=null;
    
    
    @Before
    public void setUp(){
  
        metaInfo = new OakTableMetaInfo();
        Date sysdate = new Date();
        metaInfo.setCreateBy("sys");
        metaInfo.setCreateTime(sysdate);
        metaInfo.setDbColums("order_id,account_id,type,amount,current_amount");
        metaInfo.setDbHost("192.168.1.151");
        metaInfo.setDbPort("3306");
        metaInfo.setDbPwd("tbj2017");
        metaInfo.setDbSchema("pay");
        metaInfo.setDbType(OakDBTypeEnum.MYSQL.name());
        metaInfo.setDbUser("xu.neng");
        metaInfo.setModifyBy("sys");
        metaInfo.setModifyTime(sysdate);
        metaInfo.setTableName("balance_detail_0");
        metaInfoDao.insert(metaInfo);

        task = new OakTask();
        task.setCreateTime(sysdate);
        task.setCreateBy("sys");
        task.setModifyTime(sysdate);
        task.setCurrent(0);
        task.setDbSchema(metaInfo.getDbSchema());
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(sysdate);
        cal.add(Calendar.DATE, -12);
        task.setStartTime(cal.getTime());
        task.setEndTime(sysdate);
 
        task.setMetaId(metaInfo.getId());
        task.setModifyBy("sys");
        task.setMsg(null);
        task.setStatus(OakTaskStatusEnum.INIT.name());
        task.setTotal(0);
        taskDao.insert(task);
        
    }
    
    @Test@Rollback(true)
    public void testService(){

        String lastHash=null;
        try {
            lastHash = service.doTask(task, null);
            assertTrue(lastHash!=null); 
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }   
    }

}
