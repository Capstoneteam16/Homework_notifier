package finalproject;



   import java.io.BufferedReader;
	import java.io.InputStreamReader;
	import java.net.URL;
	import java.net.URLConnection;
	import java.net.URLEncoder;
	import org.json.JSONArray;      // JSON library from http://www.json.org/java/
	import org.json.JSONObject;

	public class Funnycrawler {

		// The request also includes the userip parameter which provides the end
		// user's IP address. Doing so will help distinguish this legitimate
		// server-side traffic from traffic which doesn't come from an end-user.
		URL url = new URL(
		    "https://ajax.googleapis.com/ajax/services/search/web?v=1.0&"
		    + "q=Paris%20Hilton&userip=USERS-IP-ADDRESS");
		URLConnection connection = url.openConnection();
		connection.addRequestProperty("Referer", /* Enter the URL of your site here */);

		String line;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while((line = reader.readLine()) != null) {
		 builder.append(line);
		}

		JSONObject json = new JSONObject(builder.toString());
	}


