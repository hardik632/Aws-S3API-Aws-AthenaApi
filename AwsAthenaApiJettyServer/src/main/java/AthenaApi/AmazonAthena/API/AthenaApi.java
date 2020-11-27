package AthenaApi.AmazonAthena.API;


import AthenaApi.AmazonAthena.SignatureCalculation.CanonicalRequestCalculation;
import AthenaApi.AmazonAthena.SignatureCalculation.SignatureCalculation;
import AthenaApi.AmazonAthena.SignatureCalculation.StringToSignCalculation;
import AthenaApi.AmazonAthena.util.HttpUtils;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class AthenaApi {

    static String service = "athena";
    static String algorithm = "AWS4-HMAC-SHA256"; // algorithm for string to sign
    static String amz_date;
    static String today_date;
    static String response;
    static String signed_headers;
    static Map<String, String> headers = new HashMap<>();
    static String method;
    static String host;
    static String scope;

    public static String ApiRequest(String region, String access_key, String secret_key, String body, Map<String, String> headers1, String request_parameter)
            throws Exception {

        TimeZone utc = TimeZone.getTimeZone("UTC");
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(utc);
        amz_date = (sdf.format(today));

        method = "POST";
        host = "athena." + region + ".amazonaws.com";
        URL endpoint = new URL("https://" + host + "/" + "?" + request_parameter);
        String authorization = Signature(method, host, region, body, secret_key, access_key, headers1, request_parameter);

        headers.put("x-amz-date", amz_date);
        headers.put("content-type", "application/x-amz-json-1.1");
        headers.put("Authorization", authorization);
        headers.put("x-amz-target", headers1.get("X-Amz-Target"));

        response = HttpUtils.invokeHttpRequest(endpoint, "POST", headers, body);
        return response;

    }

    public static String Signature(String method, String host, String region, String payload, String secret_key, String access_key, Map<String, String> headers1, String request_parameter) throws Exception {
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

        signed_headers = "content-type;host;x-amz-date;x-amz-target";
        String canonical_headers = "content-type:" + "application/x-amz-json-1.1" + '\n' +
                "host:" + host + '\n' + "x-amz-date:" + amz_date + '\n'
                + "x-amz-target:" + headers1.get("X-Amz-Target") + '\n';

        String canonical_request = CanonicalRequestCalculation.canonicalRequest(method, region, payload, signed_headers, canonical_headers, request_parameter);
        String string_to_sign = StringToSignCalculation.stringToSign(algorithm, scope, canonical_request);
        String signature_hash = SignatureCalculation.signature(secret_key, region, service, string_to_sign);
        String authorization = algorithm + ' ' + "Credential=" + access_key + '/' + scope + ", "
                + "SignedHeaders=" + signed_headers + ", " + "Signature=" + signature_hash;
        return authorization;
    }
}

