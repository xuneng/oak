package com.nexu.oak.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.nexu.oak.dao.OakBlockChainDAO;
import com.nexu.oak.dao.OakTableMetaInfoDAO;
import com.nexu.oak.dao.OakTaskDAO;
import com.nexu.oak.dto.OakBlockChain;
import com.nexu.oak.dto.OakTableMetaInfo;
import com.nexu.oak.dto.OakTask;
import com.nexu.oak.dto.enums.OakDBTypeEnum;
import com.nexu.oak.dto.enums.OakTaskStatusEnum;
import com.nexu.oak.job.OakChainJob;
import com.nexu.oak.job.OakScanJob;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = { "file:src/main/resources/META-INF/spring/oak-context.xml",
        "file:src/main/resources/META-INF/spring/oak-db.xml" })
public class OakPerformanceTest extends AbstractTransactionalJUnit4SpringContextTests {
    
    @Autowired
    private OakScanJob oakScanJob;
    @Autowired
    private OakChainJob oakChainJob;
    @Autowired
    private OakBlockChainDAO oakChainDAO;
    @Autowired
    private OakTaskDAO oakTaskDAO;
 
    @Test
    @Rollback(false)
    public void testPerformance(){

        try {
            OakBlockChain currentRecord = oakChainDAO.selectLatestRecord();
            long start = System.currentTimeMillis();
//            System.out.println("start scan:"+start);
//            oakScanJob.execute(null);
//            long cost = System.currentTimeMillis()-start;
//            System.out.println("end scan,cost ms:"+cost);
//            List<OakTask> tasks = oakTaskDAO.selectByStatus(OakTaskStatusEnum.INIT);
//            System.out.println("generated task num:"+tasks.size());
            
            /*
             * mock 3 task
             */
            
            List<OakTask> taskList = new ArrayList<OakTask>();
            /*
             *   
             * 209 - 0 2016-07-29 21:34:21 / 2017-01-11 17:34:35 281
             * 241-32 2016-08-16 19:54:53 / 2016-08-16 19:56:06  2000
             * 272 -63 2016-07-29 15:51:00 / 2017-02-04 17:00:02 409
             * 
             */
            Calendar kaishi = Calendar.getInstance();
            Calendar jieshu = Calendar.getInstance();
            
            kaishi.set(2016, 6, 29, 21, 30);
            jieshu.set(2017, 0, 11, 17, 35);
            OakTask task1 = getMockTask(209l,kaishi.getTime(),jieshu.getTime());
           
            kaishi.set(2016, 7, 16, 19, 50);
            jieshu.set(2016, 7, 16, 19, 57);
            OakTask task2 = getMockTask(241l,kaishi.getTime(),jieshu.getTime());
            
            kaishi.set(2016, 6, 29, 15, 50);
            jieshu.set(2017, 1, 4, 17, 1);
            OakTask task3 = getMockTask(272l,kaishi.getTime(),jieshu.getTime());
            
            taskList.add(task1);
            taskList.add(task2);
            taskList.add(task3);
            oakTaskDAO.batchInsert(taskList);
            
            
            start = System.currentTimeMillis();
            System.out.println("start gen chain:"+start);
            oakChainJob.execute(null);
            long cost = System.currentTimeMillis()-start;
            System.out.println("end gen chain,cost ms:"+cost);
            OakBlockChain afterGen = oakChainDAO.selectLatestRecord();
            long total=afterGen.getId() - currentRecord.getId();
            System.out.println("oak processed total num:" + total);
            taskList = oakTaskDAO.selectByStatus(OakTaskStatusEnum.SUCCESS);
            System.out.println("success task num:"+taskList.size());
            for(OakTask task : taskList){
                System.out.println("==task==,metaId:"+task.getMetaId()+",total:"+task.getTotal()+"taskId:"+task.getId());
            } 
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
           
        }   
    }

    
    private OakTask getMockTask(long metaId,Date start, Date end){

        OakTask task1 = new OakTask();
        Date sysdate = new Date();
        task1.setCreateTime(sysdate);
        task1.setCreateBy("test");
        task1.setModifyTime(sysdate);
        task1.setCurrent(0);
        task1.setDbSchema("pay");
        
        task1.setStartTime(start);
        task1.setEndTime(end);
        
        task1.setMetaId(metaId);  
        task1.setModifyBy("test");
        
        task1.setStatus(OakTaskStatusEnum.INIT.name());
        task1.setTotal(0);
        
        return task1;
    }
}
