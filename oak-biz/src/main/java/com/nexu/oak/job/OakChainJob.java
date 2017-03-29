package com.nexu.oak.job;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nexu.oak.dao.OakBlockChainDAO;
import com.nexu.oak.dao.OakTaskDAO;
import com.nexu.oak.dto.OakTask;
import com.nexu.oak.dto.enums.OakTaskStatusEnum;
import com.nexu.oak.service.OakChainService;
import com.tongbanjie.legends.client.core.AbstractJob;

/**
 * 根据oak_task表，到指定目标表拉取数据，生成对应区块，插入链
 * 
 * 1）判断系统里是否有失败的任务，如果有，则不进行任何动作
 * 2）当前是否有 PROCESS状态的任务，如果有，也不进行任何动作
 * 3）除以上情况， 捞取最老的未执行INIT的任务(10个一捞)，按顺序依次处理，中间任何一个任务失败，整个job不再执行
 *    --捞出的任务按schema分组，这样就可以重用connection，每一组只创建一次db connection（可选实现）
 *
 * @author dongfeng
 * @version $Id: OakChainJob.java, v 0.1 2017-1-3 下午3:53:27 dongfeng Exp $
 */
@Service
public class OakChainJob extends AbstractJob{
    
    @Autowired
    private OakTaskDAO oakTaskDAO;
    @Autowired
    private OakChainService service;
    
    private static final int BATCH_SIZE = 10;
    private static Logger logger = LoggerFactory.getLogger(OakChainJob.class);
    
    @Override
    public String execute(String param) {
        
        try{
            List<OakTask> failTasks = oakTaskDAO.selectByStatus(OakTaskStatusEnum.FAIL);
            List<OakTask> processTasks = oakTaskDAO.selectByStatus(OakTaskStatusEnum.PROCESS);
            if(!CollectionUtils.isEmpty(failTasks)||!CollectionUtils.isEmpty(processTasks)){
                //just return, no any further process, let monitor do alert
                return "success";
            }else{
                List<OakTask> initTasks = oakTaskDAO.selectByStatusWithBatchSize(OakTaskStatusEnum.INIT, BATCH_SIZE);
                if(!CollectionUtils.isEmpty(initTasks)){
                    OakTask lastTask = initTasks.get(initTasks.size()-1);
                    //check last task's end time,if current time is less then end time, just return
                    // until current time is larger than end time
                    Date current = new Date();
                    if(current.after(lastTask.getEndTime())){
                        String lasthash = null;
                        for(OakTask task : initTasks){
                            lasthash = service.doTask(task,lasthash);
                        }
                    }
                }
     
                return "success";
            }
        }catch(Exception e){
            logger.error("oak chain job failed.", e);
            throw new RuntimeException("false");
        }

    }

}
