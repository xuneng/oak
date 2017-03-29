package com.nexu.oak.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.nexu.oak.dto.OakTableMetaInfo;

@Repository
public class OakTableMetaInfoDAO extends SqlSessionDaoSupport {

	public int batchInsert(List<OakTableMetaInfo> list) {
		return getSqlSession().insert("OakTableMetaInfo.batchInsert", list);
	}

	public int insert(OakTableMetaInfo record) {
		return getSqlSession().insert("OakTableMetaInfo.insert", record);
	}

	public OakTableMetaInfo selectByPrimaryKey(Long id) {
		return (OakTableMetaInfo) getSqlSession().selectOne("OakTableMetaInfo.selectByPrimaryKey", id);
	}

	public List<OakTableMetaInfo> selectByUk(String dbSchema, String tableName, String dbHost, String port) {
		OakTableMetaInfo record = new OakTableMetaInfo();
		record.setDbSchema(dbSchema);
		record.setTableName(tableName);
		record.setDbHost(dbHost);
		record.setDbPort(port);
		return selectByParam(record);
	}

	public List<OakTableMetaInfo> selectByParam(OakTableMetaInfo record) {
		return getSqlSession().selectList("OakTableMetaInfo.selectByParam", record);
	}

	public int update(OakTableMetaInfo record) {
		return getSqlSession().update("OakTableMetaInfo.updateByPrimaryKeySelective", record);
	}
	public List<OakTableMetaInfo> selectAllOrderByBD(){
		return getSqlSession().selectList("OakTableMetaInfo.selectAllOrderByBD");
	}

}
