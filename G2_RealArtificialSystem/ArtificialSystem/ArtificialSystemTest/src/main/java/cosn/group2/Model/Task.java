package cosn.group2.Model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "Task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long taskID;

    @Column
    public String timestamp;

    @Column
    public String value;
    @Column
    public String unit;

    @Column String filename;

    @ManyToOne
    @JoinColumn(name = "experienceID")
    //@Column
    public Experience experienceID;

    public Task(String timestamp, String value, String unit, Experience experienceID) {
        this.timestamp = timestamp;
        this.value = value;
        this.unit = unit;
        this.experienceID = experienceID;
    }

    public Task() {
    }

    public Task execute(Experience id, String filename){
        this.taskID= new Random().nextLong();
        this.experienceID=id;
        this.timestamp = String.valueOf(System.currentTimeMillis());
        this.value = String.valueOf(Math.random());
        this.unit = "G-Unit";
        this.filename=filename;
        return this;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", timestamp='" + timestamp + '\'' +
                ", value='" + value + '\'' +
                ", unit='" + unit + '\'' +
                ", experienceID=" + experienceID +
                '}';
    }

    public long getTaskID() {
        return taskID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Experience getExperienceID() {
        return experienceID;
    }

    public void setExperienceID(Experience experienceID) {
        this.experienceID = experienceID;
    }
}
