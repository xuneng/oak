package com.nexu.oak.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.nexu.oak.dao.OakTableMetaInfoDAO;
import com.nexu.oak.dao.OakTaskDAO;
import com.nexu.oak.dto.OakTableMetaInfo;
import com.nexu.oak.dto.OakTask;
import com.nexu.oak.dto.enums.OakTaskStatusEnum;
import com.tongbanjie.legends.client.core.AbstractJob;

/**
 * 定时扫描oak_table_meta_info表，生成对应的区块链任务
 * 
 * 1）捞取所有meta_info 2）按DB Group 3）同一个group的每张表， 3-1）查询得到这张表的最近一条任务
 * 3-2）按配置的时间步进，在此基础上生成下一条任务，如果没有，则按当前时间取整数时间插入
 * （取整逻辑sample： 比如现在是14：14：58，则时间取14：00：00，14：45：54，则时间取14：30：00）
 *
 * @author dongfeng
 * @version $Id: OakScanJob.java, v 0.1 2017-1-3 下午3:55:15 dongfeng Exp $
 */
@Service
public class OakScanJob extends AbstractJob {
	@Resource
	OakTableMetaInfoDAO oakTableMetaInfoDAO;
	@Resource
	OakTaskDAO oakTaskDAO;
	private static Logger logger = LoggerFactory.getLogger(OakScanJob.class);

	@Override
	public String execute(String param) {
	    
		try {
			List<OakTableMetaInfo> list = oakTableMetaInfoDAO.selectAllOrderByBD();
			for (OakTableMetaInfo info : list) {
  
		        OakTask para = new OakTask();
		        Date now = new Date();
		        para.setMetaId(info.getId());
		        List<OakTask> records = oakTaskDAO.selectByParam(para);
		        OakTask newTask = new OakTask();
		        newTask.setCurrent(0);
                newTask.setDbSchema(info.getDbSchema());
                newTask.setMetaId(info.getId());
                newTask.setStatus(OakTaskStatusEnum.INIT.getValue());
                newTask.setCreateBy("sys");
                newTask.setCreateTime(now);
                newTask.setModifyBy("sys");
                newTask.setModifyTime(now);
                newTask.setTotal(0);
                //此表第一次生成任务
                if(CollectionUtils.isEmpty(records)){
		            Calendar start = Calendar.getInstance();
		            start.setTime(now);
		            start.set(Calendar.MINUTE, 0);
		            start.set(Calendar.SECOND, 0);
		            Calendar end = (Calendar)start.clone();        
	   	            end.add(Calendar.MINUTE, 30);  
	                newTask.setStartTime(start.getTime());
	                newTask.setEndTime(end.getTime());
	                
		        }else{
		           
		            OakTask lastTask = records.get(records.size()-1);
		            Date lastEndTime = lastTask.getEndTime();
		            newTask.setStartTime(lastEndTime);
		            Date end = new Date(lastEndTime.getTime() + (30 * 60 * 1000));// 增加半小时
                    newTask.setEndTime(end);
		        }
			    
                int result = oakTaskDAO.insert(newTask);
                if (result <= 0) {
                    throw new Exception("OakTask插入失败");
                }
			}
		} catch (Exception e) {
		    logger.error("Init oak task failed", e);
		    throw new RuntimeException("false");
		}

		return "success";
	}

}
