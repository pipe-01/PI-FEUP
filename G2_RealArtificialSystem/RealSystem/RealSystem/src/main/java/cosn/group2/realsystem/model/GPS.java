package cosn.group2.realsystem.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Objects;


public class GPS {

    public String type;

    public double[] coordinates;

    public GPS() {}

    public GPS(String type, double coordinatesX, double coordinatesY) {
        this.type = type;
        this.coordinates = new double[] {
                coordinatesX,
                coordinatesY
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GPS gps = (GPS) o;
        return Objects.equals(type, gps.type) && Arrays.equals(coordinates, gps.coordinates);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(type);
        result = 31 * result + Arrays.hashCode(coordinates);
        return result;
    }

    @Override
    public String toString() {
        return "GPS{" +
                "type='" + type + '\'' +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }
}