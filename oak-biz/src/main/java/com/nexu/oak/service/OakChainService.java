package com.nexu.oak.service;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nexu.oak.dao.OakBlockChainDAO;
import com.nexu.oak.dao.OakTableMetaInfoDAO;
import com.nexu.oak.dao.OakTaskDAO;
import com.nexu.oak.domain.ChainResult;
import com.nexu.oak.dto.OakBlockChain;
import com.nexu.oak.dto.OakTableMetaInfo;
import com.nexu.oak.dto.OakTask;
import com.nexu.oak.dto.enums.OakTaskStatusEnum;
import com.nexu.oak.util.MysqlQueryExecutor;
import com.nexu.oak.util.OakChainUtil;
import com.tongbanjie.commons.util.DateUtil;

/**
 * 生成目标表区块数据服务
 * 
 * 发生错误，抛runtimeException，task置失败
 *
 * @author dongfeng
 * @version $Id: OakChainService.java, v 0.1 2017-1-12 下午1:39:40 dongfeng Exp $
 */
@Service
public class OakChainService {
    
    @Autowired
    private OakBlockChainDAO oakChainDAO;
    @Autowired
    private OakTableMetaInfoDAO metaDAO;
    @Autowired
    private OakTaskDAO taskDAO;
    @Autowired
    private OakChainUtil chainUtil;
    private static final int SIZE=500; // batch query size
    
    
    public String doTask(OakTask task,String lastHash) throws Exception{
        //update task as process
        task.setStatus(OakTaskStatusEnum.PROCESS.name());
        taskDAO.update(task);
        
        MysqlQueryExecutor queryExec = null;
        try{
            
            String lasthash = lastHash;
            if(StringUtils.isEmpty(lasthash)){
                OakBlockChain oakBlockChain = oakChainDAO.selectLatestRecord();
                if(null==oakBlockChain){
                    lasthash = "init_hashvalue";
                }else{
                    lasthash = oakBlockChain.getHashValue();
                }

            }    
            
            long metaId = task.getMetaId();
            OakTableMetaInfo metaInfo = metaDAO.selectByPrimaryKey(metaId);
            //sample: jdbc:mysql://192.168.1.6:3306/oak?useUnicode=true&characterEncoding=UTF-8
            String DBUrl = "jdbc:mysql://"+metaInfo.getDbHost()+":"+metaInfo.getDbPort()+"/"+metaInfo.getDbSchema();
            queryExec = new MysqlQueryExecutor(DBUrl,metaInfo.getDbUser(), metaInfo.getDbPwd());     
            ChainResult result = genBlockChain(task,queryExec,metaInfo,lasthash,SIZE,task.getCurrent());

            //update task as success
            task.setStatus(OakTaskStatusEnum.SUCCESS.name());
            task.setTotal(result.getTotalRecord());
            taskDAO.update(task);
            return result.getLastHash();
            
        }catch(Exception e){
            task.setStatus(OakTaskStatusEnum.FAIL.name());
            String msg = org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(e);
            if(msg.length()>256)
                msg = msg.substring(0,255);
            task.setMsg(msg);
            taskDAO.update(task);
            throw e;
        }finally{
            if(null!=queryExec)
                queryExec.closeConnection();
        }
    }
    
    /**
     * 按批查询数据，生成区块链数据，并批量插入
     *
     * @param queryExec
     * @param metaInfo
     * @param lastHash
     * @param size
     * @return 本次任务的最后一条记录的lastHash值
     * @throws SQLException 
     * @throws NoSuchAlgorithmException 
     */  
    public ChainResult genBlockChain(OakTask task,MysqlQueryExecutor queryExec,OakTableMetaInfo metaInfo, String lastHash,int size,int startLine) throws NoSuchAlgorithmException, SQLException{
        
        ChainResult result = new ChainResult();
        int currentLine = task.getCurrent(); // start from 0
        Date endTime = task.getEndTime();
        Date startTime = task.getStartTime();
        String sql = "";
        ResultSet rs = null;
        int rowCount=0;
        int total = task.getCurrent();
        do{
            //组装查询目标表数据的SQL
            sql = genSQL(metaInfo.getDbColums(), metaInfo.getTableName(), startTime, endTime, currentLine, SIZE);
            //query
            rs = queryExec.query(sql);
            
            rs.last(); //移到最后一行   
            rowCount = rs.getRow(); //得到当前行号，也就是记录数   
            rs.beforeFirst(); //如果还要用结果集，就把指针再移到初始化的位置  
            
            if(null!=rs && rowCount>0){
                //convet to chain data
                List<OakBlockChain> blocks = OakChainUtil.convertResultToChainData(rs, metaInfo.getDbColums().split(","), lastHash, metaInfo.getId());
                // set next batch's input currentLine & hash
                currentLine = currentLine+rowCount;
                lastHash = blocks.get(blocks.size()-1).getHashValue();
                
                chainUtil.batchInsertAndUpdate(blocks, currentLine, task);
                total = total + rowCount;
            }

        }while(null!=rs && rowCount>0);
        
        result.setLastHash(lastHash);
        result.setTotalRecord(total);
        return result;
    }
    
    /**
     * @param columns
     * @param tableName
     * @param start yyyy-mm-dd h24:mm:ss
     * @param end
     * @param offset
     * @param size
     * @return
     */
    public String genSQL(String columns,String tableName, Date start,Date end, int offset, int size){
        //sample:select * from micro_demo where create_time>='2016-12-10 10:00:00' and create_time<'2016-12-22 11:00:00' order by id asc limit 1,1;
       
        return String.format("select id,%s from %s where create_time>='%s' and create_time<'%s' order by id asc limit %d,%d;", columns,tableName
            ,DateUtil.format(start, DateUtil.DEFAULT_FORMAT),DateUtil.format(end, DateUtil.DEFAULT_FORMAT),offset,size);

    }
    
    
    
}
