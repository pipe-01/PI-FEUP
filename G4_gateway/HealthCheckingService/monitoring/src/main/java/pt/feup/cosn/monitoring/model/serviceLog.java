package pt.feup.cosn.monitoring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="serviceLog")
public class serviceLog {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Timestamp timestamp;
    private int status;

    @ManyToOne
    @JoinColumn(name = "ser_id")
    @JsonIgnore
    private service ser;

    public serviceLog() {
    }

    public serviceLog(Timestamp timestamp, int status) {
        this.timestamp = timestamp;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public service getSer() {
        return ser;
    }

    public void setSer(service ser) {
        this.ser = ser;
    }
    
}
