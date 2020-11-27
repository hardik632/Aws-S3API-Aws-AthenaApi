package AthenaApi.AmazonAthena.SignatureCalculation;

import AthenaApi.AmazonAthena.util.ToHash;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CanonicalRequestCalculation {
    static String canonical_uri = null;
    static String canonical_querystring = null;
    static String canonical_request = null;
    static String amz_date = null;
    static String payload_hash = null;
    static String host = null;


    public static String canonicalRequest(String method, String region,
                                          String payload, String signed_headers,
                                          String canonical_headers,
                                          String request_parameter)
            throws Exception {
        Date today = new Date();
        SimpleDateFormat sdf = new
                SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        TimeZone utc = TimeZone.getTimeZone("UTC");
        sdf.setTimeZone(utc);
        amz_date = (sdf.format(today));

        canonical_uri = "/";
        canonical_querystring = request_parameter;
        host = "athena." + region + ".amazonaws.com";
        payload_hash = ToHash.toHexString((payload));
        canonical_request = method + '\n' + canonical_uri + '\n' +
                canonical_querystring + '\n' + canonical_headers + '\n' +
                signed_headers + '\n' + payload_hash;

        return canonical_request;
    }
}
