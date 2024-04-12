package cosn.group2.dto;

public class DomainModel {

    private int id;
    public String topology;
    public int modelType;


    public DomainModel(int id, String topology, int modelType) {
        this.id = id;
        this.topology = topology;
        this.modelType = modelType;
    }

    @Override
    public String toString() {
        return "DomainModel{" +
                "id=" + id +
                ", topology='" + topology + '\'' +
                ", modelType=" + modelType +
                '}';
    }
    /*
    * “id”: 1,
    “topology”: “a”,
    “modelType”: 1
    * */
}
