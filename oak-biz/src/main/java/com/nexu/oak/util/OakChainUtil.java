package com.nexu.oak.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nexu.oak.dao.OakBlockChainDAO;
import com.nexu.oak.dao.OakTaskDAO;
import com.nexu.oak.dto.OakBlockChain;
import com.nexu.oak.dto.OakTask;

@Component
public class OakChainUtil {
       
    @Autowired
    private OakBlockChainDAO oakChainDAO;
    @Autowired
    private OakTaskDAO taskDAO;
    /**
     * 批量生成区块链数据
     *
     * @param rs 目标表结果集
     * @param columns 目标表columns列名
     * @param lastHashValue 链上的最后一个hash值 - clumn ‘HASH_VALUE’
     * @param fkUID
     * @param metaId
     * @return
     * @throws SQLException 
     * @throws NoSuchAlgorithmException 
     */
    public static List<OakBlockChain> convertResultToChainData(ResultSet rs, 
        String[] columns,String lastHashValue,Long metaId) throws SQLException, NoSuchAlgorithmException{
        
        List<OakBlockChain> chain = null;
        
        if(null!=rs &&null!=columns && null!=metaId){
            chain = new ArrayList<OakBlockChain>();
            while(rs.next()){
                
                List<String> columnsValue = new ArrayList<String>();
                for(int i=0;i<columns.length;i++){
                    columnsValue.add( rs.getString(columns[i]));
                }
                OakBlockChain chainDto = genChainDto(genTargetHash(columnsValue),lastHashValue,
                    rs.getString("id"),metaId);
                
                chain.add(chainDto);
            } 
        }
        return chain;
    }
    
    private static OakBlockChain genChainDto(String targetHash, String lastHash,String fkUID,Long metaId) throws NoSuchAlgorithmException{
        
        OakBlockChain dto = new OakBlockChain();
        Date sysdate = new Date();
        dto.setCreateBy("SYS");
        dto.setCreateTime(sysdate);
        dto.setFkUid(fkUID);
        List<String> values = new ArrayList<String>();
        values.add(lastHash);
        values.add(targetHash);
        dto.setHashValue(genTargetHash(values));
        dto.setMetaId(metaId);
        dto.setModifyBy("SYS");
        dto.setModifyTime(sysdate);
        dto.setTargetHash(targetHash);
        
        return dto;
    }
    
    
    private static String genTargetHash(List<String> columnsValue) throws NoSuchAlgorithmException{
        
        StringBuilder sb = new StringBuilder();
        for(String value:columnsValue)
            sb.append(value);
        
        // 生成一个MD5加密计算摘要
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 计算md5函数
        md.update(sb.toString().getBytes());
        String targetHash = new BigInteger(1, md.digest()).toString(16);
        
        return targetHash;
    }
    
    @Transactional
    public int batchInsertAndUpdate(List<OakBlockChain> blocks,int currentLine,OakTask task){
        //insert
        int count = oakChainDAO.batchInsert(blocks);
        //update task currentLine
        task.setCurrent(currentLine);
        task.setModifyTime(new Date());
        taskDAO.update(task);
        
        return count;
    }
    
}
