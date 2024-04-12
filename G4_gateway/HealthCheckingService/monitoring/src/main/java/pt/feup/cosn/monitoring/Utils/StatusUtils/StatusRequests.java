package pt.feup.cosn.monitoring.Utils.StatusUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.feup.cosn.monitoring.Utils.StatusUtils.HealthAPi;
import pt.feup.cosn.monitoring.model.service;
import pt.feup.cosn.monitoring.model.serviceLog;
import pt.feup.cosn.monitoring.services.serviceLogsServices;
import pt.feup.cosn.monitoring.services.serviceServices;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class StatusRequests {
    private final serviceServices serServices;
    private final serviceLogsServices serLogServices;

    @Autowired
    public StatusRequests(serviceServices serServices, serviceLogsServices serLogServices) {
        this.serServices = serServices;
        this.serLogServices = serLogServices;

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                List<service> serviceList = serServices.getAllService();

                for (service ser : serviceList) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://" + ser.getIp())
                            .build();
                    
                    HealthAPi api = retrofit.create(HealthAPi.class);
                    Call<Void> chamada = api.checkHealth();
                    
                    System.out.println("Pinging " + ser.getName() + " At address " + "https://" + ser.getIp()+ "/health");
                    chamada.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            int status = response.code();
                            System.out.println("Ping with status" + status+ " " + ser.getName() + " At address " + "https://" + ser.getIp()+ "/health");

                            if (response.isSuccessful()) {
                                serLogServices.addLog(ser, new serviceLog(Timestamp.from(Instant.now()), status));
                            } else {
                                serLogServices.addLog(ser, new serviceLog(Timestamp.from(Instant.now()), status));
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            System.out.println("Failed Pinging " + ser.getName() + " At address " + "https://" + ser.getIp()+ "/health");
                            serLogServices.addLog(ser, new serviceLog(Timestamp.from(Instant.now()), 404));
                        }
                    });
                    // try {
                    // int status = chamada.execute().code();
                    // serLogServices.addLog(ser, new serviceLog(Timestamp.from(Instant.now()),
                    // status));

                    // } catch (IOException e) {
                    // System.out.println(e.getMessage());
                    // serLogServices.addLog(ser, new serviceLog(Timestamp.from(Instant.now()),
                    // 404));
                    // }
                }
            }
        }, 0, 1000 * 30);
    }
}
