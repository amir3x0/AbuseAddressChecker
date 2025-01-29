package server;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.google.gson.Gson;

public class ChainAbuseAPIClient {
    private static ChainAbuseAPIClient instance;
    private static final String API_KEY = "ca_U1ljUkFuT2FNd21NZFJFdDZkYTNCMEdsLjRvc0xOWkFHYll6YiszNlUzaE9VeXc9PQ";
    private static final String API_URL = "https://api.chainabuse.com/v0/reports";

    private ChainAbuseAPIClient() {}

    public static synchronized ChainAbuseAPIClient getInstance() {
        if (instance == null) {
            instance = new ChainAbuseAPIClient();
        }
        return instance;
    }

    public List<Report> checkAddress(String address) throws Exception {
    	
    	//String endpoint = API_URL + "?address=" + address + "&includePrivate=false";
    	
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "?address=" + address))
                .header("Authorization", "Bearer " + API_KEY)
                .header("accept", "application/json")
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