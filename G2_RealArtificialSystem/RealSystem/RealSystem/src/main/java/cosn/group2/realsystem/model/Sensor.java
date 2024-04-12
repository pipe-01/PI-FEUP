package cosn.group2.realsystem.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table
@IdClass(Sensor.CompositeKey.class)
public class Sensor {

    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column
    public String id;
    @Column
    public String city;
    @Column
    public String type;
    @Column
    @Transient public GPS position; // gps

    @Column
    @Id
    public String nature; // static or mobile


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return Objects.equals(id, sensor.id)
                && Objects.equals(city, sensor.city)
                && Objects.equals(type, sensor.type)
                && Objects.equals(position, sensor.position)
                && Objects.equals(nature, sensor.nature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, type, position, nature);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id='" + id + '\'' +
                ", coty='" + city + '\'' +
                ", type='" + type + '\'' +
                ", position=" + position +
                ", nature='" + nature + '\'' +
                '}';
    }

    public static class CompositeKey implements Serializable {
        @Id public String id;
        @Id public String nature; // static or mobile
    }
}
