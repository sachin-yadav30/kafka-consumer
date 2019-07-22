package com.gracenote.relay.consumer.model;

import java.io.Serializable;

/**
 * Created by sayadav on 10/04/18.
 */
public class JobVO implements Serializable {
    private static final long serialVersionUID = 1L;
    public String startedAt;
    public String finishedAt;
    public String startedBy;
    String tokenName;
    String jobId;
    String jobType;


    @Override
    public String toString()

    {
        return "{" +
                "'startedAt':'" + startedAt + '\'' +
                ", 'finishedAt':'" + finishedAt + '\'' +
                ", 'startedBy'" + startedBy + '\'' +
                ", 'tokenName':'" + tokenName + '\'' +
                ", 'jobId':" + jobId + '\'' +
                ", 'jobType':'" + jobType + '\'' +
                '}';
    }

    public JobVO(String jobId, String jobType,String tokenName, String startedAt, String startedBy) {
        this.startedAt = startedAt;
        this.startedBy = startedBy;
        this.tokenName = tokenName;
        this.jobId = jobId;
        this.jobType = jobType;
    }

    // Default Constructor Needed for GSON library
    public JobVO() {
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(String startedBy) {
        this.startedBy = startedBy;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(String finishedAt) {
        this.finishedAt = finishedAt;
    }


}
