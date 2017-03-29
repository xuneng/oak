package com.nexu.oak.service;

import static org.junit.Assert.assertTrue;

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
import com.nexu.oak.job.OakScanJob;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = { "file:src/main/resources/META-INF/spring/oak-context.xml",
        "file:src/main/resources/META-INF/spring/oak-db.xml" })
public class OakScanJobTest extends AbstractTransactionalJUnit4SpringContextTests {
    
    @Autowired
    private OakScanJob oakScanJob;
 
    @Test@Rollback(true)
    public void testService(){

        try {
            oakScanJob.execute(null);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
           
        }   
    }

}
