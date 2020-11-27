package SpringWebServer.AmazonS3.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    static public HttpHeaders httpHeaders = new HttpHeaders();

    public static String invokeHttpRequest(URL endpointUrl, String httpMethod, Map<String, String> headers,
                                           String requestBody) throws IOException {
        HttpURLConnection connection;
        connection = createHttpConnection(endpointUrl, httpMethod, headers);
        try {
            if (requestBody != null) {
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(requestBody);
                wr.flush();
                wr.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Request failed. " + e.getMessage(), e);
        }
        return executeHttpRequest(connection);
    }

    public static String executeHttpRequest(HttpURLConnection connection) throws IOException {

        System.out.println("Status Code: " + connection.getResponseCode());
        System.out.println("***********Response Headers************");
        Map<String, List<String>> map = connection.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println(entry.getKey() +
                    ": " + entry.getValue());
        }
        System.out.println("****************************************");

        HashMap<String, List<String>> a = new HashMap<>();
        Map<String, List<String>> b = connection.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : b.entrySet()) {

            a.put(entry.getKey(), ((entry.getValue())));
        }
        a.remove(null);

        HashMap<String, String> map1 = new HashMap<>();
        for (String i : a.keySet()) {
            map1.put(i, a.get(i).get(0));
        }
        HashMap<String, String> responseheaders = new HashMap<>(map1);
        for (String i : responseheaders.keySet()) {
            httpHeaders.set(i, responseheaders.get(i));
        }
        System.out.println(httpHeaders);
        StringBuffer response = new StringBuffer();
        InputStream is;
        try {
            // Get Response
            try {
                is = connection.getInputStream();
            } catch (IOException e) {
                is = connection.getErrorStream();
            }
            BufferedReader bd = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bd.readLine()) != null) {
                response.append(line).append('\n');
            }
            String res = new String(response);
            if (res.contains("<Error>")) {
                res = StringUtils.substringBetween(res, "<Message>", "</Message>");
                return res;
            }
            bd.close();

        } catch (Exception e) {
            //throw new RuntimeException("Request failed. " + e.getMessage(), e);
            response.append("disconnected..... ,check your internet connection");
        } finally {
            connection.disconnect();
        }
        return response.toString();
    }

    public static HttpURLConnection createHttpConnection(URL endpointUrl, String httpMethod,
                                                         Map<String, String> headers) {

        try {
            HttpURLConnection connection = (HttpURLConnection) endpointUrl.openConnection();
            connection.setRequestMethod(httpMethod);
            if (headers != null) {
                for (String headerKey : headers.keySet()) {
                    connection.setRequestProperty(headerKey, headers.get(headerKey));
                }
            }
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create connection. " + e.getMessage(), e);
        }
    }
}
