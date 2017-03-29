package com.nexu.oak.domain;

public class ChainResult {
    
    private String lastHash = null;
    private int totalRecord = 0;
    public String getLastHash() {
        return lastHash;
    }
    public void setLastHash(String lastHash) {
        this.lastHash = lastHash;
    }
    public int getTotalRecord() {
        return totalRecord;
    }
    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

}
