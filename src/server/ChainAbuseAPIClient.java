package server;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.google.gson.Gson;

public class ChainAbuseAPIClient {
    private static ChainAbuseAPIClient instance;
    private static final String API_KEY = "your_api_key_here";
    private static final String API_URL = "https://api.chainabuse.com/v1/reports";

    private ChainAbuseAPIClient() {}

    public static synchronized ChainAbuseAPIClient getInstance() {
        if (instance == null) {
            instance = new ChainAbuseAPIClient();
        }
        return instance;
    }

    public List<Report> checkAddress(String address) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "?address=" + address))
                .header("X-API-Key", API_KEY)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            ApiResponse apiResponse = gson.fromJson(response.body(), ApiResponse.class);
            return apiResponse.getReports();
        } else {
            throw new Exception("API Error: " + response.statusCode());
        }
    }
}