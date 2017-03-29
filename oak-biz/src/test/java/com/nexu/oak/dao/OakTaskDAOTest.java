package com.nexu.oak.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.nexu.oak.dao.OakTaskDAO;
import com.nexu.oak.dto.OakTask;


@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/resources/META-INF/spring/oak-context.xml",
"file:src/main/resources/META-INF/spring/oak-db.xml" })
public class OakTaskDAOTest extends AbstractTransactionalJUnit4SpringContextTests{

    @Resource
    OakTaskDAO oakTaskDAO;

    @Test
    @Rollback(true)
    public void testData() {

        for (int i = 1; i < 30; i++) {
            // insert
            OakTask dto = new OakTask();
            Date sysdate = new Date();
            dto.setCreateTime(sysdate);
            dto.setCreateBy("ehjb");
            dto.setModifyTime(sysdate);
            dto.setCurrent(i);
            dto.setDbSchema("dbschema" + i);
            dto.setEndTime(sysdate);
            dto.setMetaId((long) i);
            dto.setModifyBy("xbb");
            dto.setMsg("msg" + i);
            dto.setStartTime(sysdate);
            dto.setStatus("status" + i);
            dto.setTotal(i * i);
            int insertResutlt = oakTaskDAO.insert(dto);
            System.out.println(dto.getId());
            System.out.println("insertResult第:" + i + "次:" + insertResutlt);
        }
        // batchInsert
        List<OakTask> list = new ArrayList<OakTask>();
        for (int i = 0; i < 23; i++) {
            OakTask dto = new OakTask();
            Date sysdate = new Date();
            // sysdate.setTime(7823l);
            dto.setCreateTime(sysdate);
            dto.setCreateBy("ehjb");
            dto.setModifyTime(sysdate);
            dto.setCurrent(i);
            dto.setDbSchema("dbschema" + i);
            dto.setEndTime(sysdate);
            dto.setMetaId((long) i * 100);
            dto.setModifyBy("xbb");
            dto.setMsg("msg" + i);
            dto.setStartTime(sysdate);
            dto.setStatus("status" + i);
            dto.setTotal(i * i);
            list.add(dto);
        }
        int batchInsertResult = oakTaskDAO.batchInsert(list);
        System.out.println("batchInsertResult:" + batchInsertResult);
        // update
        OakTask dto = new OakTask();
        dto.setId(216l);
        dto.setCreateBy("new people");
        int updateResult = oakTaskDAO.update(dto);
        System.out.println("updateResult:" + updateResult);
        // selectById
        OakTask selectByIdResult = oakTaskDAO.selectByPrimaryKey(216l);
        System.out.println("selectByIdResult:" + selectByIdResult.getMsg());
        // selectByParam
        OakTask param = new OakTask();
        param.setModifyBy("xbb");
        List<OakTask> selectByParamResult = oakTaskDAO.selectByParam(param);
        System.out.println("selectByParam's size:" + selectByParamResult.size());
        // selectByUk
        List<OakTask> selectByUkResult = oakTaskDAO.selectByUk(21, null, null);
        System.out.println("selectByUk's size:" + selectByUkResult.size());

    }

}
