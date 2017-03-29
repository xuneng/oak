package com.nexu.oak.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.nexu.oak.dao.OakTableMetaInfoDAO;
import com.nexu.oak.dto.OakTableMetaInfo;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = { "file:src/main/resources/META-INF/spring/oak-context.xml",
		"file:src/main/resources/META-INF/spring/oak-db.xml" })
public class MetaInfoDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Resource
	OakTableMetaInfoDAO oakTableMetaInfoDAO;

	@Test
	@Rollback(true)
	public void testData() {

		for (int i = 1; i < 30; i++) {
			// insert
			OakTableMetaInfo dto = new OakTableMetaInfo();
			Date sysdate = new Date();
			dto.setCreateBy("sys");
			dto.setCreateTime(sysdate);
			dto.setDbColums("colums" + i);
			dto.setDbHost("dbhost" + i);
			dto.setDbPort("dbport" + i);
			dto.setDbPwd("dbpwd" + i);
			dto.setDbSchema("dbschema" + i);
			dto.setDbType("dbType" + i);
			dto.setDbUser("dbUser" + i);
			dto.setModifyBy("xbb");
			dto.setModifyTime(sysdate);
			dto.setTableName("tableName" + i);

			int insertResutlt = oakTableMetaInfoDAO.insert(dto);
			System.out.println(dto.getId());
			System.out.println("insertResult第:" + i + "次:" + insertResutlt);
		}
		// batchInsert
		List<OakTableMetaInfo> list = new ArrayList<OakTableMetaInfo>();
		for (int i = 0; i < 50; i++) {
			OakTableMetaInfo dto = new OakTableMetaInfo();
			Date sysdate = new Date();
			dto.setCreateBy("sys");
			dto.setCreateTime(sysdate);
			dto.setDbColums("colums" + i);
			dto.setDbHost("dbHost");
			dto.setDbPort("dbPort" + i);
			dto.setDbPwd("dbPwd" + i * 100);
			dto.setDbSchema("dbSchema" + i);
			dto.setDbType("dbType" + i);
			dto.setDbUser("dbUser" + i);
			dto.setModifyBy("xbb");
			dto.setModifyTime(sysdate);
			dto.setTableName("tableName" + i);
			list.add(dto);
		}
		int batchInsertResult = oakTableMetaInfoDAO.batchInsert(list);
		System.out.println("batchInsertResult:" + batchInsertResult);
		// update
		OakTableMetaInfo dto = new OakTableMetaInfo();
		dto.setId(1l);
		dto.setCreateBy("new people");
		int updateResult = oakTableMetaInfoDAO.update(dto);
		System.out.println("updateResult:" + updateResult);
		// selectById
		OakTableMetaInfo selectByIdResult = oakTableMetaInfoDAO.selectByPrimaryKey(2l);
		System.out.println("selectByIdResult:" + selectByIdResult.getDbPwd());
		// selectByParam
		OakTableMetaInfo param = new OakTableMetaInfo();
		param.setModifyBy("xbb");
		List<OakTableMetaInfo> selectByParamResult = oakTableMetaInfoDAO.selectByParam(param);
		System.out.println("selectByParam's size:" + selectByParamResult.size());
		// selectByUk
		List<OakTableMetaInfo> selectByUkResult = oakTableMetaInfoDAO.selectByUk("dbSchema3", "tableName3", "dbHost3",
				"dbPort3");
		System.out.println("selectByUk's size:" + selectByUkResult.size());

	}
}
