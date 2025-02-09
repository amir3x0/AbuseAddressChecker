// ChainAbuseAPIClient.java
package server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class ChainAbuseAPIClient {
    private static ChainAbuseAPIClient instance;
    private static final String CHAINABUSE_URL = "https://api.chainabuse.com/v0/reports?includePrivate=false&page=1&perPage=50";
    private static final String AUTH_HEADER_VALUE = "Basic Y2FfYVRCRVFrRTVVRmhvYVZsWmNGZGxSMGxxYWtvM1RrVmFMbXAyVEhkSVYyNVJObVEyUjNKUmVsVmtjRk5pYjFFOVBROmNhX2FUQkVRa0U1VUZob2FWbFpjRmRsUjBscWFrbzNUa1ZhTG1wMlRIZElWMjVSTm1RMlIzSlJlbFZrY0ZOaWIxRTlQUQ==";

    private ChainAbuseAPIClient() {}

    public static synchronized ChainAbuseAPIClient getInstance() {
        if (instance == null) {
            instance = new ChainAbuseAPIClient();
        }
        return instance;
    }

    public List<Report> checkAddress(String originalAddress) throws Exception {
        String checkUrl = CHAINABUSE_URL + "&address=" + originalAddress;
        HttpClient client = HttpClient.newHttpClient();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(checkUrl))
                .header("accept", "application/json")
                .header("Authorization", AUTH_HEADER_VALUE)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        if (response.statusCode() == 200) {
            return parseResponse(response.body(), originalAddress);
        } else {
            throw new Exception("API Error: " + response.statusCode() + " - " + response.body());
        }
    }

    private List<Report> parseResponse(String responseBody, String originalAddress) {
        List<Report> reports = new ArrayList<>();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> dataList = gson.fromJson(responseBody, listType);

        if (dataList.isEmpty()) {
            reports.add(new Report(originalAddress, 0, "No abuse reports"));
            return reports;
        }

        int abuseCount = dataList.size();
        String firstReportId = dataList.get(0).containsKey("id") ? 
                             dataList.get(0).get("id").toString() : 
                             "unknown";

        reports.add(new Report(
            originalAddress,
            abuseCount,
            "https://chainabuse.com/report/" + firstReportId
        ));

        return reports;
    }
}