package com.nexu.oak.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.nexu.oak.dao.OakBlockChainDAO;
import com.nexu.oak.dto.OakBlockChain;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = { "file:src/main/resources/META-INF/spring/oak-context.xml",
		"file:src/main/resources/META-INF/spring/oak-db.xml" })
public class ChainDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Resource
	OakBlockChainDAO oakBlockChainDAO;

	@Test
	@Rollback(true)
	public void testData() {

		for (int i = 1; i < 30; i++) {
			// insert
			OakBlockChain dto = new OakBlockChain();
			Date sysdate = new Date();
			dto.setCreateBy("xbb");
			dto.setCreateTime(sysdate);
			dto.setFkUid("fkUid"+i+100000);
			dto.setHashValue("hashValue"+i);
			dto.setMetaId((long)i*i);
			dto.setModifyBy("xbb");
			dto.setModifyTime(sysdate);
			dto.setTargetHash("targetHash"+i);
			int insertResutlt = oakBlockChainDAO.insert(dto);
			System.out.println(dto.getId());
			System.out.println("insertResult第:" + i + "次:" + insertResutlt);
		}
		// batchInsert
		List<OakBlockChain> list = new ArrayList<OakBlockChain>();
		for (int i = 0; i < 50; i++) {
			OakBlockChain dto = new OakBlockChain();
			Date sysdate = new Date();
			dto.setCreateBy("xbb");
			dto.setCreateTime(sysdate);
			dto.setFkUid("fkUid"+i*i+10000);
			dto.setHashValue("hashValue"+i);
			dto.setMetaId((long)i*i+10000);
			dto.setModifyBy("sys");
			dto.setModifyTime(sysdate);
			dto.setTargetHash("targetHash"+i);
			list.add(dto);
		}
		int batchInsertResult = oakBlockChainDAO.batchInsert(list);
		System.out.println("batchInsertResult:" + batchInsertResult);
		// update
		OakBlockChain dto = new OakBlockChain();
		dto.setId(1l);
		dto.setCreateBy("new people");
		int updateResult = oakBlockChainDAO.update(dto);
		System.out.println("updateResult:" + updateResult);
		// selectById
		OakBlockChain selectByIdResult = oakBlockChainDAO.selectByPrimaryKey(1l);
		System.out.println("selectByIdResult:" + selectByIdResult.getFkUid());
		// selectByParam
		OakBlockChain param = new OakBlockChain();
		param.setModifyBy("xbb");
		List<OakBlockChain> selectByParamResult = oakBlockChainDAO.selectByParam(param);
		System.out.println("selectByParam's size:" + selectByParamResult.size());
		// selectByUk
		List<OakBlockChain> selectByUkResult = oakBlockChainDAO.selectByUk( "fkUid4",4);
		System.out.println("selectByUk's size:" + selectByUkResult.size());

	}
}
