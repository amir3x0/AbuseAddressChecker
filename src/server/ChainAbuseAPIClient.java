package server;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ChainAbuseAPIClient {

    // Singleton instance
    private static ChainAbuseAPIClient instance;

    // Example: ChainAbuse v0 endpoint with default params
    private static final String CHAINABUSE_URL = 
        "https://api.chainabuse.com/v0/reports?includePrivate=false&page=1&perPage=50";

    // The snippet from the other code had a Basic auth header,
    // but you mentioned Bearer tokens in your code. 
    // If you truly need Basic auth, replace this with the base64 string.
    // If you need Bearer, keep the 'Bearer ' prefix.
    private static final String AUTH_HEADER_VALUE = "Basic Y2FfTjFkUWJqZFBhVkJGY1RKdFUxQnFSM0UyTW5BeFVrcE9Mbk5zWVdsWlZsUlZUR3RZY1M5MGJHVm9hMVpDYzFFOVBROmNhX04xZFFiamRQYVZCRmNUSnRVMUJxUjNFMk1uQXhVa3BPTG5Oc1lXbFpWbFJWVEd0WWNTOTBiR1ZvYTFaQ2MxRTlQUQ==";

    // We'll store the last parsed Report in a static field (like the old code did with report_entry)
    private static Report lastParsedReport;

    // Private constructor to enforce Singleton
    private ChainAbuseAPIClient() {}

    public static synchronized ChainAbuseAPIClient getInstance() {
        if (instance == null) {
            instance = new ChainAbuseAPIClient();
        }
        return instance;
    }

    /**
     * Makes a GET request to the ChainAbuse API (v0) for a given address.
     * Similar to the second snippet: it builds a URL with "&address=",
     * calls the endpoint, and parses the first result if present.
     * 
     * @param address The crypto address to check
     * @return A List<Report> with exactly one item if found; empty if no data
     * @throws Exception If the API returns a non-200 status code
     */
    public List<Report> checkAddress(String address) throws Exception {
        // Build full URL with address:
        String checkUrl = CHAINABUSE_URL + "&address=" + address;
        System.out.println("HttpRequest: " + checkUrl);

        // Prepare the request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(checkUrl))
                .header("accept", "application/json")
                .header("Authorization", AUTH_HEADER_VALUE)
                .GET()
                .build();

        // Send synchronously
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());

        if (response.statusCode() == 200) {
            // Parse and store in our static field
            parseResponse(response.body());
            // Return a single-report list (like old code returning "report_entry")
            List<Report> result = new ArrayList<>();
            if (lastParsedReport != null) {
                result.add(lastParsedReport);
            }
            return result;
        } else {
            // In case of error, print the body and throw
            System.err.println("Error response body: " + response.body());
            throw new Exception("API Error: " + response.statusCode() + " - " + response.body());
        }
    }

    /**
     * Parses the ChainAbuse response using Gson instead of org.json.
     * The data is usually an array of objects, each with fields like:
     *  - "id" (String)
     *  - "scamCategory" (String)
     *  - "addresses" (Array of objects { "address": "...", ... })
     * If empty, we'll store a dummy "no_data" report.
     */
    private static void parseResponse(String responseBody) {
        System.out.println("Parsing response...");

        // We'll parse it as a List of Maps to handle arbitrary JSON
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map<String,Object>>>(){}.getType();
        List<Map<String,Object>> dataList = gson.fromJson(responseBody, listType);

        if (dataList.isEmpty()) {
            // No data => store a dummy report
            lastParsedReport = new Report("no_data", 0, "N/A");
            return;
        }

        // Take the first object
        Map<String,Object> firstObj = dataList.get(0);

        // Extract "addresses" array if present
        String addressValue = "unknown_address";
        if (firstObj.containsKey("addresses")) {
            Object addressesRaw = firstObj.get("addresses");
            if (addressesRaw instanceof List) {
                List<?> addressesArr = (List<?>) addressesRaw;
                if (!addressesArr.isEmpty() && addressesArr.get(0) instanceof Map) {
                    Map<?,?> addrObj = (Map<?,?>) addressesArr.get(0);
                    if (addrObj.containsKey("address")) {
                        Object addrField = addrObj.get("address");
                        if (addrField instanceof String) {
                            addressValue = (String) addrField;
                        }
                    }
                }
            }
        }

        // We'll store the 'scamCategory' or fallback to 'id'
        String scamCategory = "N/A";
        if (firstObj.containsKey("scamCategory")) {
            Object catObj = firstObj.get("scamCategory");
            if (catObj instanceof String) {
                scamCategory = (String) catObj;
            }
        } else if (firstObj.containsKey("id")) {
            Object idObj = firstObj.get("id");
            if (idObj instanceof String) {
                scamCategory = (String) idObj;
            }
        }

        // We'll build a link if there's an "id"
        String link = "https://chainabuse.com";
        if (firstObj.containsKey("id")) {
            Object idObj = firstObj.get("id");
            if (idObj instanceof String) {
                link = "https://chainabuse.com/report/" + (String) idObj;
            }
        }

        // In your 'Report' class: 
        //   constructor: Report(String address, int abuseCount, String reportLink)
        // We'll treat 'scamCategory' as a placeholder for abuseCount=0 or link?
        // Let's do abuseCount=0, and the link as above
        lastParsedReport = new Report(addressValue, 0, link);
    }

    /**
     * For quick testing (like the old main):
     */
    public static void main(String[] args) {
        try {
            // Test address
            String testAddress = "3NmbqMoydJUDaUTkkyf3yt4EovrZV81mW1";

            ChainAbuseAPIClient client = ChainAbuseAPIClient.getInstance();
            List<Report> results = client.checkAddress(testAddress);

            System.out.println("Number of returned reports: " + results.size());
            for (Report r : results) {
                System.out.println("Address: " + r.getAddress()
                                 + ", Abuse Count: " + r.getAbuseCount()
                                 + ", Link: " + r.getReportLink());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
