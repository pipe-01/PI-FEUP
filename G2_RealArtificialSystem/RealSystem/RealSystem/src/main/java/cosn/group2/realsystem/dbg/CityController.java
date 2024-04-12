package cosn.group2.realsystem.dbg;

import cosn.group2.realsystem.model.GPS;
import cosn.group2.realsystem.model.Sensor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// simulate the 'City' microservice
@RestController
@RequestMapping(path = "/city")
class CityController {


    // Single item
    @GetMapping("/sensor/static/{id}")
    Sensor oneStatic(@PathVariable String id) {

        // just generate a random one
        Sensor sensor = new Sensor();
        sensor.id = id;
        sensor.type = "TYPE" + String.valueOf((int) (Math.random() * 10.0));
        sensor.position = new GPS("Point", (Math.random() * 180.0), (Math.random() * 180.0));
        sensor.nature = "static";

        return sensor;
    }
    // Single item
    @GetMapping("/sensor/mobile/{id}")
    Sensor oneMobile(@PathVariable String id) {

        // just generate a random one
        Sensor sensor = new Sensor();
        sensor.id = id;
        sensor.type = "TYPE" + String.valueOf((int) (Math.random() * 10.0));
        sensor.position = new GPS("Point", (Math.random() * 180.0), (Math.random() * 180.0));
        sensor.nature =  "mobile";

        return sensor;
    }
}
