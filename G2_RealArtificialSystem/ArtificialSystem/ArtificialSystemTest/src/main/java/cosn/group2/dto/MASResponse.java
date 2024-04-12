package cosn.group2.dto;

import java.io.Serializable;

public class MASResponse implements Serializable {
    private long workflowExecutionId;
    private String filename;

    public MASResponse(long workflowExecutionId, String filename) {
        this.workflowExecutionId = workflowExecutionId;
        this.filename = filename;
    }

    public long getWorkflowExecutionId() {
        return workflowExecutionId;
    }

    public void setWorkflowExecutionId(long workflowExecutionId) {
        this.workflowExecutionId = workflowExecutionId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "MASResponse{" +
                "workflowExecutionId=" + workflowExecutionId +
                ", filename='" + filename + '\'' +
                '}';
    }
}
