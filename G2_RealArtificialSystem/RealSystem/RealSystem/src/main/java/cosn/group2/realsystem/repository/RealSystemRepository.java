package cosn.group2.realsystem.repository;

import cosn.group2.realsystem.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RealSystemRepository extends JpaRepository<SensorData, String> {

    List<SensorData> findBySensorID(String sensorID);
}