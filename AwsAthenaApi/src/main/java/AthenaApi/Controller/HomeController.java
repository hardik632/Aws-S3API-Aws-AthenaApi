package AthenaApi.Controller;


import AthenaApi.AmazonAthena.API.AthenApi;

import AthenaApi.AmazonAthena.util.HttpUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.util.Map;

@RestController
public class HomeController {
    String response;
    String region;
    String access_key;
    String secret_key;

    HomeController() throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(new FileReader
                ("src/main/java/AthenaApi/AmazonAthena/main/credentials.json"));
        JSONObject jsonObject = (JSONObject) obj;
        region = (String) jsonObject.get("region");
        access_key = (String) jsonObject.get("aws_access_key");
        secret_key = (String) jsonObject.get("aws_secret_key");
    }

    HttpHeaders httpHeaders = HttpUtils.httpHeaders;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> Athena(HttpServletRequest request,@RequestBody String body, @RequestHeader Map<String, String> headers)
            throws Exception {

        System.out.println("************Request Headers***************");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("******************************************");
        System.out.println(body);
        String request_parameter;
        if (request.getQueryString() == null) {
            request_parameter = "";
        } else {
            request_parameter = request.getQueryString() + "=";
        }
        if (region == null || access_key == null || secret_key == null)
            response = "Fill up the credentials.json";
        else
            response = AthenApi.getS3Object(region, access_key, secret_key, body,headers, request_parameter);
        return ResponseEntity.ok().headers(httpHeaders).body(response);

    }
}