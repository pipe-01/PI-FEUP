package cosn.group2.realsystem;

import cosn.group2.realsystem.controller.RealSystemController;
import cosn.group2.realsystem.model.SensorData;
import cosn.group2.realsystem.service.SensorService;
import cosn.group2.realsystem.service.ServiceConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = RealSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RealSystemApplicationTests {

	public static final String AUTHORIZATION_TOKEN_DEBUG =
			"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0IiwiaWF0IjoxNjQyNTQyNTEwLCJzdWIiOiJwZWRyb2FsbWVpZGFAZ21haWwuY29tIiwiaXNzIjoiR0FURVdBWSIsImV4cCI6MTY0MjU3MTMxMH0.wds7EaD39TG_SlVXo3vsuB2HXcFt6_vEPy8OrAQKVXc";

	@Autowired
	private TestRestTemplate template;

	@Test
	void contextLoads() {}

	@Test
	public void health() throws Exception {
		ResponseEntity<String> response = template.getForEntity("/health", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo("Up and running!");
	}

	@Test
	public void addData() throws Exception {

		final HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTHORIZATION_TOKEN_DEBUG);

		final String sensorID = UUID.randomUUID().toString();

		SensorData sensorData = new SensorData();
		sensorData.sensorID = sensorID;
		sensorData.value = String.valueOf(Math.random());
		sensorData.unit = "G-Unit";
		sensorData.timestamp = String.valueOf(System.currentTimeMillis());
		ResponseEntity<SensorData> response = template
				.exchange("/", HttpMethod.POST, new HttpEntity<>(sensorData, headers), SensorData.class);

		sensorData.id = response.getBody().id;

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(sensorData);

		// add another
		SensorData sensorData02 = new SensorData();
		sensorData02.sensorID = sensorID;
		sensorData02.value = String.valueOf(Math.random());
		sensorData02.unit = "G-Unit";
		sensorData02.timestamp = String.valueOf(System.currentTimeMillis());

		response = template
				.exchange("/", HttpMethod.POST, new HttpEntity<>(sensorData02, headers), SensorData.class);

		sensorData02.id = response.getBody().id;

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(sensorData02);

		// get both
		ResponseEntity<SensorData[]> getResponse = template
				.exchange("/" + sensorID, HttpMethod.GET, new HttpEntity<>(headers), SensorData[].class, headers);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.getBody().length).isEqualTo(2);
		assertThat(getResponse.getBody()[0]).isEqualTo(sensorData);
		assertThat(getResponse.getBody()[1]).isEqualTo(sensorData02);
	}

	@Test
	public void getData() throws Exception {

		final HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTHORIZATION_TOKEN_DEBUG);

		final SensorData sensorData = new SensorData();
		sensorData.sensorID = UUID.randomUUID().toString();
		sensorData.value = String.valueOf(Math.random());
		sensorData.unit = "G-Unit";
		sensorData.timestamp = String.valueOf(System.currentTimeMillis());
		ResponseEntity<SensorData> postResponse = template
				.exchange("/", HttpMethod.POST, new HttpEntity<>(sensorData, headers), SensorData.class, headers);

		final SensorData insertSensorData = postResponse.getBody();
		final String id = insertSensorData.id;
		final String sensorID = insertSensorData.sensorID;

		sensorData.id = id;

		assertThat(insertSensorData).isEqualTo(sensorData);

		ResponseEntity<SensorData[]> getResponse = template
				.exchange("/" + sensorID, HttpMethod.GET, new HttpEntity<>(headers), SensorData[].class, headers);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.getBody()[0]).isEqualTo(sensorData);
		assertThat(getResponse.getBody()[0]).isEqualTo(insertSensorData);


		final HttpHeaders fromHeaders = new HttpHeaders();
		fromHeaders.setBearerAuth(AUTHORIZATION_TOKEN_DEBUG);
		fromHeaders.set(ServiceConstants.START_TIMESTAMP, String.valueOf(System.currentTimeMillis()));

		getResponse = template
				.exchange("/" + sensorID, HttpMethod.GET, new HttpEntity<>(fromHeaders), SensorData[].class, headers);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.getBody().length).isEqualTo(0);


		fromHeaders.set(ServiceConstants.START_TIMESTAMP, String.valueOf(System.currentTimeMillis() - 1000000));

		getResponse = template
				.exchange("/" + sensorID, HttpMethod.GET, new HttpEntity<>(fromHeaders), SensorData[].class, headers);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.getBody().length).isEqualTo(1);

		fromHeaders.set(ServiceConstants.END_TIMESTAMP, String.valueOf(System.currentTimeMillis() - 10000));

		getResponse = template
				.exchange("/" + sensorID, HttpMethod.GET, new HttpEntity<>(fromHeaders), SensorData[].class, headers);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.getBody().length).isEqualTo(0);
	}


	@Autowired
	private SensorService sensorService;

	@Autowired
	private RealSystemController realSystemController;

	@Test
	public void getData_invalidMicroServiceURL_mustStillWork() throws Exception {

		sensorService.BASE_URL = "invalid-url";

		this.getData();
	}

	@Test
	public void getData_ButAddParameterToFailIfMicroServiceIsUnavailable_mustFail() throws Exception {

		sensorService.BASE_URL = "invalid-url";

		final SensorData sensorData = new SensorData();
		sensorData.sensorID = UUID.randomUUID().toString();
		sensorData.value = String.valueOf(Math.random());
		sensorData.unit = "G-Unit";
		sensorData.timestamp = String.valueOf(System.currentTimeMillis());

		final HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTHORIZATION_TOKEN_DEBUG);

		ResponseEntity<SensorData> postResponse;

		// must fail
		realSystemController.MUST_FAIL_IF_UNAVAILABLE = true;
		postResponse = template
				.exchange("/", HttpMethod.POST, new HttpEntity<>(sensorData, headers), SensorData.class);

		assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		// must succeed
		realSystemController.MUST_FAIL_IF_UNAVAILABLE = false;
		postResponse = template
				.exchange("/", HttpMethod.POST, new HttpEntity<>(sensorData, headers), SensorData.class);

		final SensorData insertSensorData = postResponse.getBody();
		final String id = insertSensorData.id;
		final String sensorID = insertSensorData.sensorID;

		sensorData.id = id;

		assertThat(insertSensorData).isEqualTo(sensorData);

		// must fail
		realSystemController.MUST_FAIL_IF_UNAVAILABLE = true;
		ResponseEntity<Object> getResponseFailed = template
				.exchange("/" + sensorID, HttpMethod.GET, new HttpEntity<>(headers), Object.class);

		assertThat(getResponseFailed.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		// must succeed
		realSystemController.MUST_FAIL_IF_UNAVAILABLE = false;
		ResponseEntity<SensorData[]> getResponse = template
				.exchange("/" + sensorID, HttpMethod.GET, new HttpEntity<>(headers), SensorData[].class);

		assertThat(getResponse.getBody()[0]).isEqualTo(sensorData);
		assertThat(getResponse.getBody()[0]).isEqualTo(insertSensorData);
	}

}
