package vttp.batch5.ssf.noticeboard.services;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

import vttp.batch5.ssf.noticeboard.constant.Urls;

@Service
public class NoticeService {

	// To write to redis
	@Autowired
	NoticeRepository np;

	// For API calls
	RestTemplate restTemplate = new RestTemplate();

	// TODO: Task 3
	// You can change the signature of this method by adding any number of
	// parameters
	// and return any type
	public List<String> postToNoticeServer(Notice notice) {
		// Notice object is passed from the controller

		// Convert time to epoch milisecc
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date temp = notice.getPostDate();
		String postedString = sdf.format(temp);
		Long epoch = LocalDate.parse(postedString, formatter)
				.atStartOfDay(ZoneId.systemDefault())
				.toInstant()
				.toEpochMilli();

		String[] parts = notice.getCategories().split(",");
		System.out.println(parts);

		String tempx = "";

		for (int i = 0; i < parts.length; i++) {
			if (i == 0) {
				tempx += "[";
			}
			tempx = tempx + '"' + parts[i] + '"';
			if (i < parts.length - 1) {
				tempx += ",";
			}
			if (i == parts.length - 1) {
				tempx += "]";
			}
		}

		System.out.println("------------------testing CONERT ARRAY-------------------------");
		System.out.println(tempx);

		// //Convert categories to Json array
		// JsonReader jr2 = Json.createReader(new StringReader(notice.getCategories()));
		// JsonObject jo2 =

		// Build the json object of notice
		JsonObject jo = Json.createObjectBuilder()
				.add("title", notice.getTitle()) // String
				.add("poster", notice.getPoster()) // String
				.add("postDate", epoch.toString()) // Convert first to epochmili
				.add("categories", tempx) // Convert first into array
				.add("text", notice.getText()) // String
				.build();

		// NOTICE SERVICE JSON TESTING
		System.out.println();
		System.out.println();
		System.out.println(
				"JSONNNNN  JSON   JSONNNOTICE OBJECT HAS BEEN PASSED IN TESTING---------------------------------------");
		System.out.println(jo.toString());
		System.out.println(
				"JSONNNNN  JSON   JSONNNOTICE OBJECT HAS BEEN PASSED IN TESTING---------------------------------------");
		System.out.println();
		System.out.println();

		RequestEntity<String> requestEntity = RequestEntity.post(Urls.ssf)
				.contentType(MediaType.APPLICATION_JSON).body(jo.toString());
		restTemplate.exchange(requestEntity, )
		;

		// Call and send the data to api
		RequestEntity<String> req = RequestEntity
				.post(Urls.ssf, "/notice")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(jo.toString(), String.class);

		RequestEntity<String> req2 = RequestEntity
				.post(Urls.ssf)
				.headers(HttpHeaders)
				.contentType(MediaType.APPLICATION_JSON).

		// Receive response from api
		ResponseEntity<String> response = restTemplate.exchange(req, String.class);
		ResponseEntity<String> response2 = restTemplate.exchange(req2, String.class);

		System.out.println(response.getStatusCode());

		System.out.println("-----------------------RESPONSES------------------------------------------");
		System.out.println(response);
		System.out.println(response2);
		System.out.println("-----------------------RESPONSES------------------------------------------");

		String apiBody = response.getBody();
		JsonReader jr = Json.createReader(new StringReader(apiBody));
		JsonObject job = jr.readObject();

		List<String> responses = new ArrayList<>();

		responses.add("success");
		responses.add(job.getString("id"));
		responses.add(job.getString("timestamp"));

		return responses;
	}
}
