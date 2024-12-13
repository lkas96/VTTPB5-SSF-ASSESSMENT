package vttp.batch5.ssf.noticeboard.services;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

import vttp.batch5.ssf.noticeboard.constant.Urls;

@Service
public class NoticeService {

	// To write to redis
	@Autowired
	NoticeRepository nr;

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

		// this returns 502 application error
		// {"status":"error","code":502,"message":"Application failed to
		// respond","request_id":"Wu_GjdZCQN-8RSCm5DKLlw_98031763"}"
		// HttpHeaders headers = new HttpHeaders();
		// headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		// HttpEntity<String> entity = new HttpEntity<>(jo.toString(), headers);
		// restTemplate.exchange(Urls.ssf, HttpMethod.POST, entity, String.class);

		// Call and send the data to api
		// Why wont this work lmao deadass
		// .headers part error.
		// returns 404 error
		// RequestEntity<String> req = RequestEntity
		// .post(Urls.ssf)
		// .contentType(MediaType.APPLICATION_JSON)
		// .headers("Accept", MediaType.APPLICATION_JSON)
		// .body(jo.toString(), String.class);

		RequestEntity<String> req2 = RequestEntity
		.post(Urls.ssf)
		.header("Content-Type", "application/json")
		.header("Accept", "application/json")
		.body(jo.toString(), String.class);

		// above keep failing, 404 resource not found post /
		// I can get data from api, idk how to postttttttttt
		// System.out.println("AFTER REQUEST ENTITY - POST CONTENTTYPE HEADERS
		// BODY-------------------------");

		ResponseEntity<String> response = restTemplate.exchange(req2, String.class);

		System.out.println(response.getBody());

		String apiBody = response.getBody();
		JsonReader jr = Json.createReader(new StringReader(apiBody));
		JsonObject job = jr.readObject();

		List<String> responses = new ArrayList<>();

		responses.add("success");
		responses.add(job.getString("id"));
		responses.add(job.getString("timestamp"));

		//Assume successful, calls insert to redis to save id, timestamp
		//apibody should be the json format thingy, call to insert redis method and pass the two id/timestamps
		nr.insertNotices(job.getString("id"), job.getString("timestamp"));

		return responses;
	}
}
