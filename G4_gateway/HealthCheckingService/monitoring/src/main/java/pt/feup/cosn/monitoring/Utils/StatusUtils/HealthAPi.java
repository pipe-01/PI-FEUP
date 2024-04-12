package pt.feup.cosn.monitoring.Utils.StatusUtils;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HealthAPi {
    @GET("health")
    Call<Void> checkHealth();
}
