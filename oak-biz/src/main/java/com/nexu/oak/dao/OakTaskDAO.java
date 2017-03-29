package com.nexu.oak.dao;

import java.util.Date;
import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.nexu.oak.dto.OakTask;
import com.nexu.oak.dto.enums.OakTaskStatusEnum;
import com.nexu.oak.util.MapUtils;

@Repository
public class OakTaskDAO extends SqlSessionDaoSupport {

	public int batchInsert(List<OakTask> list) {
		return getSqlSession().insert("OakTask.batchInsert", list);
	}

	public int insert(OakTask record) {
		return getSqlSession().insert("OakTask.insert", record);
	}

	public OakTask selectByPrimaryKey(Long id) {
		return (OakTask) getSqlSession().selectOne("OakTask.selectByPrimaryKey", id);
	}

	public List<OakTask> selectByUk(long metaId, Date startTime, Date endTime) {
		OakTask record = new OakTask();

		record.setMetaId(metaId);
		record.setStartTime(startTime);
		record.setEndTime(endTime);
		return selectByParam(record);
	}

	public List<OakTask> selectByParam(OakTask record) {
		return getSqlSession().selectList("OakTask.selectByParam", record);
	}

	public List<OakTask> selectByStatus(OakTaskStatusEnum status) {
		return getSqlSession().selectList("OakTask.selectByStatus", status.name());
	}

	public List<OakTask> selectByStatusWithBatchSize(OakTaskStatusEnum status, int batchSize) {

		return getSqlSession().selectList("OakTask.selectByStatusWithBatchSize",
				MapUtils.buildKeyValueMap("status", status.name(), "batchSize", batchSize));
	}

	public int update(OakTask record) {
		return getSqlSession().update("OakTask.updateByPrimaryKeySelective", record);
	}

}
