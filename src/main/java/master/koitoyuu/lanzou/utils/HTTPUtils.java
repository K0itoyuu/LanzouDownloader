package master.koitoyuu.lanzou.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPUtils {
    private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36 Edg/121.0.0.0";

    public static String doGet(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("User-Agent", userAgent);
        con.addRequestProperty("Cookie", "codelen=1; pc_ad1=1");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while((inputLine = in.readLine())!= null) {
            response.append(inputLine);
            response.append("\n");
        }

        in.close();

        return response.toString();
    }
    
    public static String doPost(String url,String params,String referer) {
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept", "application/json, text/javascript, */*");
            connection.setRequestProperty("User-Agent",userAgent);
            connection.addRequestProperty("Cookie", "codelen=1; pc_ad1=1");
            connection.addRequestProperty("DNT","1");
            connection.addRequestProperty("sec-ch-ua","\"Not A(Brand\";v=\"99\", \"Microsoft Edge\";v=\"121\", \"Chromium\";v=\"121\"");
            connection.setRequestProperty("Referer",referer);
            connection.addRequestProperty("sec-ch-ua-platform","\"Windows\"");

            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = params.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            return response.toString();
        } catch (IOException e) {
            return "";
        }
    } 
}
