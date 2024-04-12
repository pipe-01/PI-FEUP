package cosn.group2.dto;

public class MAS {

    public int workflowExecutionId;
    public Algorithm algorithm;
    public DomainModel domainModel;
    public String path;

    public MAS(int experienceID, Algorithm algorithm, DomainModel domainModel, String path) {
        this.workflowExecutionId = experienceID;
        this.algorithm = algorithm;
        this.domainModel = domainModel;
        this.path = path;
    }

    @Override
    public String toString() {
        return "MAS{" +
                "experienceID=" + workflowExecutionId +
                ", algorithm=" + algorithm +
                ", domainModel=" + domainModel +
                ", path='" + path + '\'' +
                '}';
    }

    public int getExperienceID() {
        return workflowExecutionId;
    }

    public void setExperienceID(int experienceID) {
        this.workflowExecutionId = experienceID;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public DomainModel getDomainModel() {
        return domainModel;
    }

    public void setDomainModel(DomainModel domainModel) {
        this.domainModel = domainModel;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
