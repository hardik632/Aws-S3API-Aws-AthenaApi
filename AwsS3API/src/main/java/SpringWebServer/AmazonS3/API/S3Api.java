package SpringWebServer.AmazonS3.API;


import SpringWebServer.AmazonS3.SignatureCalculation.CanonicalRequestCalculation;
import SpringWebServer.AmazonS3.SignatureCalculation.SignatureCalculation;
import SpringWebServer.AmazonS3.SignatureCalculation.StringToSignCalculation;
import SpringWebServer.AmazonS3.util.HttpUtils;
import SpringWebServer.AmazonS3.util.ToHash;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class S3Api {

    static String service = "s3";
    static String algorithm = "AWS4-HMAC-SHA256"; // algorithm for string to sign
    static String amz_date;
    static String today_date;
    static String response;
    static String signed_headers;
    static Map<String, String> headers = new HashMap<>();
    static String method;
    static String host;
    static String scope;

    public static String getS3Object(String key, String bucket, String region, String access_key, String secret_key, Map<String, String> headers1, String request_parameter)
            throws Exception {

        TimeZone utc = TimeZone.getTimeZone("UTC");
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(utc);
        amz_date = (sdf.format(today));

        method = "GET";
        host = bucket + ".s3." + "amazonaws.com";
        URL endpoint = new URL("https://" + host + "/" + key + "?" + request_parameter);
        String payload = "";
        String authorization = Signature(method, host, key, bucket, region, payload, secret_key, access_key, headers1, request_parameter);

        headers.put("x-amz-date", amz_date);
        headers.put("x-amz-content-sha256", headers1.get("x-amz-content-sha256"));
        headers.put("Authorization", authorization);

        response = HttpUtils.invokeHttpRequest(endpoint, "GET", headers, null);
        System.out.println("---------------------------RESPONSE--------------------------------------");
        System.out.println(response);
        System.out.println("-------------------------------------------------------------------------");
        return response;

    }

    public static String DeleteS3Object(String key, String bucket, String region, String access_key, String secret_key, Map<String, String> headers1, String request_parameter)
            throws Exception {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(utc);
        amz_date = (sdf.format(today));

        method = "DELETE";
        host = bucket + ".s3." + "amazonaws.com";
        URL endpoint = new URL("https://" + host + "/" + key + "?" + request_parameter);
        String payload = "";
        String authorization = Signature(method, host, key, bucket, region, payload, secret_key, access_key, headers1, request_parameter);

        headers.put("x-amz-date", amz_date);
        headers.put("x-amz-content-sha256", headers1.get("x-amz-content-sha256"));
        headers.put("Authorization", authorization);

        response = HttpUtils.invokeHttpRequest(endpoint, "DELETE", headers, null);
        System.out.println("---------------------------RESPONSE--------------------------------------");
        System.out.println(response);
        System.out.println("-------------------------------------------------------------------------");
        return response;
    }

    public static String putS3Object(String key, String bucket, String content, String region, String access_key, String secret_key, Map<String, String> headers1, String request_parameter)
            throws Exception {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(utc);
        amz_date = (sdf.format(today));

        method = "PUT";
        host = bucket + ".s3." + "amazonaws.com";
        URL endpoint = new URL("https://" + host + "/" + key + "?" + request_parameter);
        scope = today_date + '/' + region + '/' + service + '/' + "aws4_request";
        String payload = ToHash.toHexString(content);//"UNSIGNED-PAYLOAD";
        String authorization = Signature(method, host, key, bucket, region, payload, secret_key, access_key, headers1, request_parameter);

        headers.put("x-amz-date", amz_date);
        headers.put("x-amz-content-sha256", payload);
        headers.put("Authorization", authorization);

        response = HttpUtils.invokeHttpRequest(endpoint, "PUT", headers, content);
        System.out.println("---------------------------RESPONSE--------------------------------------");
        System.out.println(response);
        System.out.println("-------------------------------------------------------------------------");
        return response;
    }

    public static String createS3Bucket(String bucket, String key, String region, String access_key, String secret_key, Map<String, String> headers1, String request_parameter)
            throws Exception {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(utc);
        amz_date = (sdf.format(today));

        Date today1 = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        sdf1.setTimeZone(utc);
        today_date = (sdf1.format(today1));

        method = "PUT";
        host = bucket + ".s3." + "amazonaws.com";
        URL endpoint = new URL("https://" + host + "/" + "?" + request_parameter);

        String payload = "";
        String authorization = Signature(method, host, key, bucket, region, payload, secret_key, access_key, headers1, request_parameter);

        headers.put("x-amz-date", amz_date);
        headers.put("x-amz-content-sha256", headers1.get("x-amz-content-sha256"));
        headers.put("Authorization", authorization);

        response = HttpUtils.invokeHttpRequest(endpoint, "PUT", headers, null);
        System.out.println("---------------------------RESPONSE--------------------------------------");
        System.out.println(response);
        System.out.println("-------------------------------------------------------------------------");
        return response;
    }

    public static String DeleteS3Bucket(String key, String bucket, String region, String access_key, String secret_key, Map<String, String> headers1, String request_parameter)
            throws Exception {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(utc);
        amz_date = (sdf.format(today));


        method = "DELETE";
        host = bucket + ".s3." + "amazonaws.com";
        URL endpoint = new URL("https://" + host + "/" + key + "?" + request_parameter);
        String payload = "";
        String authorization = Signature(method, host, key, bucket, region, payload, secret_key, access_key, headers1, request_parameter);

        headers.put("x-amz-date", amz_date);
        headers.put("x-amz-content-sha256", headers1.get("x-amz-content-sha256"));
        headers.put("Authorization", authorization);

        response = HttpUtils.invokeHttpRequest(endpoint, "DELETE", headers, null);
        System.out.println("---------------------------RESPONSE--------------------------------------");
        System.out.println(response);
        System.out.println("-------------------------------------------------------------------------");
        return response;
    }

    public static String ListS3Bucket(String key, String bucket, String region, String access_key, String secret_key, Map<String, String> headers1, String request_parameter)
            throws Exception {

        TimeZone utc = TimeZone.getTimeZone("UTC");
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(utc);
        amz_date = (sdf.format(today));

        method = "GET";
        host = "s3." + "amazonaws.com";
        URL endpoint = new URL("https://" + host + "/" + key + "?" + request_parameter);
        String payload = "";
        String authorization = Signature(method, host, key, bucket, region, payload, secret_key, access_key, headers1, request_parameter);

        headers.put("x-amz-date", amz_date);
        headers.put("x-amz-content-sha256", headers1.get("x-amz-content-sha256"));
        headers.put("Authorization", authorization);

        response = HttpUtils.invokeHttpRequest(endpoint, "GET", headers, null);
        System.out.println("---------------------------RESPONSE--------------------------------------");
        System.out.println(response);
        System.out.println("-------------------------------------------------------------------------");

        return response;

    }

    public static String ListObjects(String key, String bucket, String region, String access_key, String secret_key, Map<String, String> headers1, String request_parameter)
            throws Exception {

        TimeZone utc = TimeZone.getTimeZone("UTC");
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(utc);
        amz_date = (sdf.format(today));


        method = "GET";
        host = bucket + ".s3." + "amazonaws.com";
        URL endpoint = new URL("https://" + host + "/" + key + "?" + request_parameter);

        String payload = "";
        String authorization = Signature(method, host, key, bucket, region, payload, secret_key, access_key, headers1, request_parameter);

        headers.put("x-amz-date", amz_date);

        headers.put("x-amz-content-sha256", headers1.get("x-amz-content-sha256"));
        headers.put("Authorization", authorization);

        response = HttpUtils.invokeHttpRequest(endpoint, "GET", headers, null);
        System.out.println("---------------------------RESPONSE--------------------------------------");
        System.out.println(response);
        System.out.println("-------------------------------------------------------------------------");

        return response;

    }

    public static String Signature(String method, String host, String key, String bucket, String region, String payload, String secret_key, String access_key, Map<String, String> headers1, String request_parameter) throws Exception {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(utc);
        amz_date = (sdf.format(today));

        Date today1 = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        sdf1.setTimeZone(utc);
        today_date = (sdf1.format(today1));
        scope = today_date + '/' + region + '/' + service + '/' + "aws4_request";

        signed_headers = "host;x-amz-content-sha256;x-amz-date";
        String canonical_headers = "host:" + host + '\n' + "x-amz-content-sha256:" + headers1.get("x-amz-content-sha256") + '\n' + "x-amz-date:" + amz_date + '\n';
        String canonical_request = CanonicalRequestCalculation.canonicalRequest(method, key, bucket, region, payload, signed_headers, canonical_headers, request_parameter);
        String string_to_sign = StringToSignCalculation.stringToSign(algorithm, scope, canonical_request);
        String signature_hash = SignatureCalculation.signature(secret_key, region, service, string_to_sign);

        return algorithm + ' ' + "Credential=" + access_key + '/' + scope + ", "
                + "SignedHeaders=" + signed_headers + ", " + "Signature=" + signature_hash;
    }


}

