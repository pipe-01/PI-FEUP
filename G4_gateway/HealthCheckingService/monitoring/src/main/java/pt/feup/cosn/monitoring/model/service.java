package pt.feup.cosn.monitoring.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name="services")
public class service {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String ip;
    @JsonIgnore
    private String port;

    @OneToMany(mappedBy= "ser", fetch = FetchType.EAGER)
    @OrderBy("id DESC")
    private List<serviceLog> logs;

    public service() {
    }

    public service(String name, String ip, String port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void clone(service ser) {
        if (ser.getName() != null){
            setName(ser.getName());
        }

        if (ser.getPort() != null){
            setPort(ser.getPort());
        }

        if (ser.getIp() != null){
            setIp(ser.getIp());
        }
    }

    public List<serviceLog> getLogs() {
        return logs;
    }

    public void setLogs(List<serviceLog> logs) {
        this.logs = logs;
    }

    public void addSerLog(serviceLog serLog){
        if (logs!= null){
            logs.add(serLog);
        }
    }
}
