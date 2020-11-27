package AthenaApi;

import AthenaApi.AmazonAthena.API.AthenaApi;
import AthenaApi.AmazonAthena.util.HttpUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/"}, loadOnStartup = 1)
public class MainServlet extends HttpServlet {

    String result;
    String region;
    String access_key;
    String secret_key;
    HashMap<String, String> ResponseHeaders;

    public void credentials() {
        JSONParser parser = new JSONParser();
        Object obj = null;

        try {
            obj = parser.parse(new FileReader
                    ("src/main/java/com/example/AmazonS3/main/credentials.json"));
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;
        region = (String) jsonObject.get("region");
        access_key = (String) jsonObject.get("aws_access_key");
        secret_key = (String) jsonObject.get("aws_secret_key");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        credentials();
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> hearderNames = request.getHeaderNames();
        while (hearderNames.hasMoreElements()) {
            String headerName = hearderNames.nextElement();
            headersMap.put(headerName, request.getHeader(headerName));
        }
        System.out.println("request headers: " + headersMap);
        String request_parameter;
        if (request.getQueryString() == null) {
            request_parameter = "";
        } else {
            request_parameter = request.getQueryString() + "=";
        }
        StringBuffer content;
        try (BufferedReader reader = request.getReader()) {
            content = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append('\n');
            }
        }

        try {
            result = AthenaApi.ApiRequest(region, access_key, secret_key, content.toString(), headersMap, request_parameter);
            ResponseHeaders = HttpUtils.ResponseHeaders;
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String i : ResponseHeaders.keySet())
            response.setHeader(i, ResponseHeaders.get(i));

        response.getOutputStream().print(result);
    }
}