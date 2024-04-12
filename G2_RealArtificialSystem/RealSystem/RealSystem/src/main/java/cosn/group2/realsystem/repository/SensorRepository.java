package cosn.group2.realsystem.repository;

import cosn.group2.realsystem.model.Sensor;
import cosn.group2.realsystem.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, String> {

    @Query("SELECT s FROM Sensor s WHERE s.nature LIKE 'static' AND s.id = :id")
    Sensor findStaticSensorByID(String id);

    @Query("SELECT s FROM Sensor s WHERE s.nature LIKE 'mobile' AND s.id = :id")
    Sensor findMobileSensorByID(String id);
}