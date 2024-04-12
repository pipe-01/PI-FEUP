package cosn.group2.dto;

public class Algorithm {

    private String id;
    public String name;
    public String description;
    public String command;

    public Algorithm(String id, String name, String description, String command) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.command = command;
    }

    @Override
    public String toString() {
        return "Algorithm{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", command='" + command + '\'' +
                '}';
    }

    /*“id”: “a25943c4-3f22-4715-82e7-581e1591481c”,
            “name”: “Algoritmo1",
            “description”: “Descrição do algoritmo1.“,
            “command”: “Comando - algoritmo1.“,*/
}
