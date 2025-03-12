package com.trip.mymy.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;

@Service
@PropertySource("classpath:application.properties") 
public class PortOneService {
	
	@Value("${imp.apiKey}")
	private String apiKey;
	@Value("${imp.apiSecretKey}")
	private String apiSecretKey;

	public String getAccessToken(String bankCode, String bankNum) {
		
        
        // 토큰 요청 보낼 주소
		String strUrl = "https://api.iamport.kr/users/getToken"; 
		String bankHolderInfo = "";

		try {
         // url Http 연결 생성
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// POST 요청
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);// outputStreamm으로 post 데이터를 넘김

			conn.setRequestProperty("content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			// 파라미터 세팅
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

			JsonObject requestData = new JsonObject();
			requestData.addProperty("imp_key", apiKey);
			requestData.addProperty("imp_secret", apiSecretKey);

			bw.write(requestData.toString());
			bw.flush();
			bw.close();

			int resposeCode = conn.getResponseCode();

			System.out.println("응답코드 =============" + resposeCode);
			if (resposeCode == 200) {// 성공
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}

				br.close();

				// 토큰 값 빼기
				JsonElement jsonElement = JsonParser.parseString(sb.toString());
		        String access_token = jsonElement.getAsJsonObject().getAsJsonObject("response").get("access_token").getAsString();
				System.out.println("Access Token: " + access_token);

				String getPaymentUrl = "https://api.iamport.kr/vbanks/holder";

				String query = String.format("?bank_code=%s&bank_num=%s", URLEncoder.encode(bankCode, "UTF-8"),
						URLEncoder.encode(bankNum, "UTF-8"));
				URL bankurl = new URL(getPaymentUrl + query);
				System.out.println(bankurl);

				HttpURLConnection getConn = (HttpURLConnection) bankurl.openConnection();
				getConn.setRequestMethod("GET");
				getConn.setRequestProperty("Content-Type", "application/json");
				getConn.setRequestProperty("Authorization", "Bearer " + access_token);

				int getResponseCode = getConn.getResponseCode();
				System.out.println("GET 응답코드 =============" + getResponseCode);

				if (getResponseCode == 200) {
					System.out.println("#########성공");

					BufferedReader getBr = new BufferedReader(new InputStreamReader(getConn.getInputStream()));
					StringBuilder getResponseSb = new StringBuilder();
					String getLine;
					while ((getLine = getBr.readLine()) != null) {
						getResponseSb.append(getLine).append("\n");
					}
					getBr.close();

					String getResponse = getResponseSb.toString();
					System.out.println("GET 응답 결과: " + getResponse);

					//JsonParser parser1 = new JsonParser();
					//JsonObject phoneJson1 = parser1.parse(getResponse).getAsJsonObject();

					JsonElement jsonElement1 = JsonParser.parseString(getResponse);
					JsonObject phoneJson1 = jsonElement1.getAsJsonObject();
					
					// 예금주만 값 빼기
					bankHolderInfo = phoneJson1.getAsJsonObject("response").get("bank_holder").getAsString();
					System.out.println("bankHolderInfo: " + bankHolderInfo);

				} else {
					
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bankHolderInfo;
	}
}