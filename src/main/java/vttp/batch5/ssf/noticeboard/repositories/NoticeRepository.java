package vttp.batch5.ssf.noticeboard.repositories;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.batch5.ssf.noticeboard.constant.Constant;

@Repository
public class NoticeRepository {

	// TODO: Task 4
	// You can change the signature of this method by adding any number of
	// parameters
	// and return any type
	//
	/*
	 * Write the redis-cli command that you use in this method in the comment.
	 * For example if this method deletes a field from a hash, then write the
	 * following
	 * redis-cli command
	 * hdel myhashmap a_key
	 *
	 *
	 */

	// @Autowired
	// @Qualifier("notice")
	RedisTemplate<String, String> template;

	public void insertNotices(String id, String timestamp) {
		// After task 3 on success, will call this task 4 method as well.
		// Add to redis server whatever
		// On success insert
		// Save the ID and timestamp from the REST api into my own redis server
		// then all is done, service pass back whawtevevr success/message/failure to the
		// controller
		// based on that controller show the view2 or view 3 pages.

		// Add into redis list
		// create json object then tostring.
		// save as value
		JsonObject jo = Json.createObjectBuilder()
				.add("id", id)
				.add("timestamp", timestamp)
				.build();

		//lpush rediskey values
		template.opsForList().leftPush(Constant.redisKey, jo.toString());

	}

}
