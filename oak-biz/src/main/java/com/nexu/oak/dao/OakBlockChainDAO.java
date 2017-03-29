package com.nexu.oak.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.nexu.oak.dto.OakBlockChain;

@Repository
public class OakBlockChainDAO extends SqlSessionDaoSupport {

	public int batchInsert(List<OakBlockChain> list) {
		return getSqlSession().insert("OakBlockChain.batchInsert", list);
	}

	public int insert(OakBlockChain record) {
		return getSqlSession().insert("OakBlockChain.insert", record);
	}

	public OakBlockChain selectByPrimaryKey(Long id) {
		return (OakBlockChain) getSqlSession().selectOne("OakBlockChain.selectByPrimaryKey", id);
	}

	public List<OakBlockChain> selectByUk(String fkUid, long metaId) {
		OakBlockChain record = new OakBlockChain();
		record.setFkUid(fkUid);
		record.setMetaId(metaId);
		return selectByParam(record);
	}

	public List<OakBlockChain> selectByParam(OakBlockChain record) {
		return getSqlSession().selectList("OakBlockChain.selectByParam", record);
	}

	public OakBlockChain selectLatestRecord() {
		return (OakBlockChain) getSqlSession().selectOne("OakBlockChain.selectLatestRecord", null);
	}

	public int update(OakBlockChain record) {
		return getSqlSession().update("OakBlockChain.updateByPrimaryKeySelective", record);
	}

	public static void main(String[] args) {
		Date a = new Date();
		System.out.println(a);
		a.setTime(a.getTime() + 3245l);
		System.out.println(a);
	}
}
