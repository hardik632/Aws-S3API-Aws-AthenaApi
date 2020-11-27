package SpringWebServer.Controller;


import SpringWebServer.AmazonS3.API.S3Api;

import SpringWebServer.AmazonS3.util.HttpUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
                ("src/main/java/SpringWebServer/AmazonS3/main/credentials.json"));
        JSONObject jsonObject = (JSONObject) obj;
        region = (String) jsonObject.get("region");
        access_key = (String) jsonObject.get("aws_access_key");
        secret_key = (String) jsonObject.get("aws_secret_key");
    }

    HttpUtils httpUtils = new HttpUtils();
    HttpHeaders httpHeaders = HttpUtils.httpHeaders;

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @RequestMapping(method = RequestMethod.GET, path = "/{bucket}/{key}")
    public ResponseEntity<String> getObject(@PathVariable("bucket") String bucket, @PathVariable("key")
            String key, HttpServletRequest request, @RequestHeader Map<String, String> headers)
            throws Exception {

        System.out.println("************Request Headers***************");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("******************************************");
        String request_parameter;
        if (request.getQueryString() == null) {
            request_parameter = "";
        } else {
            request_parameter = request.getQueryString() + "=";
        }
        if (region == null || access_key == null || secret_key == null)
            response = "Fill up the credentials.json";
        else
            response = S3Api.getS3Object(key, bucket,
                    region, access_key, secret_key, headers, request_parameter);
        return ResponseEntity.ok().headers(httpHeaders).body(response);

    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(method = RequestMethod.DELETE, path = "/{bucket}/{key}")
    public ResponseEntity<String> DeleteS3Object(@PathVariable("bucket") String bucket, @PathVariable("key")
            String key, HttpServletRequest request, @RequestHeader Map<String, String> headers)
            throws Exception {

        System.out.println("************Request Headers***************");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("******************************************");
        String request_parameter;
        if (request.getQueryString() == null) {
            request_parameter = "";
        } else {
            request_parameter = request.getQueryString() + "=";
        }
        if (region == null || access_key == null || secret_key == null)
            response = "Fill up the credentials.json";
        else
            response = S3Api.DeleteS3Object(key, bucket, region, access_key, secret_key, headers, request_parameter);
        return ResponseEntity.ok().headers(httpHeaders).body(response);

    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(method = RequestMethod.PUT, path = "/{bucket}/{key}")
    public ResponseEntity<String> putObject(@PathVariable("bucket") String bucket, @PathVariable("key")
            String key, HttpServletRequest request, @RequestBody String content, @RequestHeader Map<String, String> headers)
            throws Exception {
        System.out.println("************Request Headers***************");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("******************************************");
        String request_parameter;
        if (request.getQueryString() == null) {
            request_parameter = "";
        } else {
            request_parameter = request.getQueryString() + "=";
        }
        content = new String(content.getBytes(), StandardCharsets.UTF_8);

        if (region == null || access_key == null || secret_key == null)
            response = "Fill up the credentials.json";
        else
            response = S3Api.putS3Object(key, bucket, content,
                    region, access_key, secret_key, headers, request_parameter);
        return ResponseEntity.ok().headers(httpHeaders).body(response);
    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(method = RequestMethod.PUT, path = "/{bucket}")
    public ResponseEntity<String> CreateBucket(@PathVariable("bucket") String bucket, HttpServletRequest request, @RequestHeader Map<String, String> headers)
            throws Exception {
        String key = "";
        System.out.println("************Request Headers***************");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("******************************************");
        String request_parameter;
        if (request.getQueryString() == null) {
            request_parameter = "";
        } else {
            request_parameter = request.getQueryString() + "=";
        }
        if (region == null || access_key == null || secret_key == null)
            response = "Fill up the credentials.json";
        else
            response = S3Api.createS3Bucket(bucket, key,
                    region, access_key, secret_key, headers, request_parameter);
        return ResponseEntity.ok().headers(httpHeaders).body(response);

    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(method = RequestMethod.DELETE, path = "/{bucket}")
    public ResponseEntity<String> DeleteS3Bucket(@PathVariable("bucket") String bucket, HttpServletRequest request, @RequestHeader Map<String, String> headers)
            throws Exception {
        String key = "";
        System.out.println("************Request Headers***************");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("******************************************");
        String request_parameter;
        if (request.getQueryString() == null) {
            request_parameter = "";
        } else {
            request_parameter = request.getQueryString() + "=";
        }
        if (region == null || access_key == null || secret_key == null)
            response = "Fill up the credentials.json";
        else
            response = S3Api.DeleteS3Bucket(key, bucket, region, access_key, secret_key, headers, request_parameter);
        return ResponseEntity.ok().headers(httpHeaders).body(response);


    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> ListS3Bucket(HttpServletRequest request, @RequestHeader Map<String, String> headers)
            throws Exception {
        String bucket = "";
        String key = "";
        System.out.println("************Request Headers***************");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("******************************************");
        String request_parameter;
        if (request.getQueryString() == null) {
            request_parameter = "";
        } else {
            request_parameter = request.getQueryString() + "=";
        }
        if (region == null || access_key == null || secret_key == null)
            response = "Fill up the credentials.json";
        else
            response = S3Api.ListS3Bucket(key, bucket,
                    region, access_key, secret_key, headers, request_parameter);
        return ResponseEntity.ok().headers(httpHeaders).body(response);


    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(method = RequestMethod.GET, path = "/{bucket}")
    public ResponseEntity<String> ListObjects(@PathVariable("bucket") String bucket, HttpServletRequest request, @RequestHeader Map<String, String> headers)
            throws Exception {
        String key = "";
        System.out.println("************Request Headers***************");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("******************************************");

        String request_parameter;
        if (request.getQueryString() == null) {
            request_parameter = "";
        } else {
            request_parameter = request.getQueryString();
        }
        if (region == null || access_key == null || secret_key == null)
            response = "Fill up the credentials.json";
        else
            response = S3Api.ListObjects(key, bucket, region, access_key, secret_key, headers, request_parameter);
        return ResponseEntity.ok().headers(httpHeaders).body(response);

    }
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------


}
