package cosn.group2.realsystem.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class SensorData {

    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column
    public String id;
    @Column
    public String sensorID;
    @Column
    public String value;
    @Column
    public String timestamp;
    @Column
    public String unit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorData that = (SensorData) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sensorID, that.sensorID) &&
                Objects.equals(value, that.value) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sensorID, value, timestamp, unit);
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "id=" + id +
                ", sensorID=" + sensorID +
                ", value='" + value + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
