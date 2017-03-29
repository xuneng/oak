package com.nexu.oak.dto;

import java.io.Serializable;
import java.util.Date;

public class OakTask implements Serializable{

	 /**
	 * 
	 */
	private static final long serialVersionUID = -6449422802821199257L;

		private Long id;

	    private Date createTime;

	    private String createBy;

	    private Date modifyTime;

	    private String modifyBy;

	    private String status;

	    private Long metaId;

	    private String dbSchema;

	    private Date startTime;

	    private Date endTime;

	    private Integer total;

	    private String msg;

	    private Integer current;

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public Date getCreateTime() {
	        return createTime;
	    }

	    public void setCreateTime(Date createTime) {
	        this.createTime = createTime;
	    }

	    public String getCreateBy() {
	        return createBy;
	    }

	    public void setCreateBy(String createBy) {
	        this.createBy = createBy;
	    }

	    public Date getModifyTime() {
	        return modifyTime;
	    }

	    public void setModifyTime(Date modifyTime) {
	        this.modifyTime = modifyTime;
	    }

	    public String getModifyBy() {
	        return modifyBy;
	    }

	    public void setModifyBy(String modifyBy) {
	        this.modifyBy = modifyBy;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }

	    public Long getMetaId() {
	        return metaId;
	    }

	    public void setMetaId(Long metaId) {
	        this.metaId = metaId;
	    }

	    public String getDbSchema() {
	        return dbSchema;
	    }

	    public void setDbSchema(String dbSchema) {
	        this.dbSchema = dbSchema;
	    }

	    public Date getStartTime() {
	        return startTime;
	    }

	    public void setStartTime(Date startTime) {
	        this.startTime = startTime;
	    }

	    public Date getEndTime() {
	        return endTime;
	    }

	    public void setEndTime(Date endTime) {
	        this.endTime = endTime;
	    }

		public Integer getTotal() {
			return total;
		}

		public void setTotal(Integer total) {
			this.total = total;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public Integer getCurrent() {
			return current;
		}

		public void setCurrent(Integer current) {
			this.current = current;
		}

	  
}