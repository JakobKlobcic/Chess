import com.google.gson.Gson;
import responses.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiCalls{
	private static ApiCalls instance;
	ApiCalls(){}
	public static synchronized ApiCalls getInstance() {
		if (instance == null) {
			instance = new ApiCalls();
		}
		return instance;
	}

	static String source = "http://localhost:8080";

	public static RegisterResponse register(String username, String password, String email) throws IOException{
		Gson gson = new Gson();
		URL url = new URL(source+"/user");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setReadTimeout(5000);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);

		connection.addRequestProperty("Content-Type", "application/json");

		connection.connect();

		try(OutputStream requestBody = connection.getOutputStream();) {
			String inputData = "{ \"username\":\"" + username + "\", \"password\":\"" + password + "\", \"email\":\"" + email + "\" }";
			requestBody.write(inputData.getBytes());
			requestBody.flush();
		}
		StringBuilder content;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

			String line;
			content = new StringBuilder();

			while ((line = br.readLine()) != null) {
				content.append(line);
				content.append(System.lineSeparator());
			}
		} finally {
			connection.disconnect();
		}

		RegisterResponse registerResponse = gson.fromJson(content.toString(), RegisterResponse.class);
		registerResponse.setStatus(connection.getResponseCode());

		return registerResponse;
	}


	public static CreateGameResponse createGame(String gameName, String authToken) throws IOException {
		Gson gson = new Gson();

		URL url = new URL(source + "/game");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setReadTimeout(5000);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);

		connection.addRequestProperty("Content-Type", "application/json");
		connection.addRequestProperty("authorization", authToken);

		connection.connect();

		try (OutputStream requestBody = connection.getOutputStream()) {
			String inputData = "{ \"gameName\":\"" + gameName + "\" }";
			requestBody.write(inputData.getBytes());
			requestBody.flush();
		}

		StringBuilder content;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

			String line;
			content = new StringBuilder();

			while ((line = br.readLine()) != null) {
				content.append(line);
				content.append(System.lineSeparator());
			}
		} finally {
			connection.disconnect();
		}

		CreateGameResponse createGameResponse = gson.fromJson(content.toString(), CreateGameResponse.class);
		createGameResponse.setStatus(connection.getResponseCode());

		return createGameResponse;
	}

	public static ListGamesResponse listGames(String authToken) throws IOException{
		Gson gson = new Gson();
		URL url = new URL(source + "/game");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setReadTimeout(5000);
		connection.setRequestMethod("GET");

		connection.addRequestProperty("Authorization", authToken);

		connection.connect();

		StringBuilder content;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

			String line;
			content = new StringBuilder();

			while ((line = br.readLine()) != null) {
				content.append(line);
				content.append(System.lineSeparator());
			}
			System.out.println(content);
		} finally {
			connection.disconnect();
		}

		ListGamesResponse listGamesResponse = gson.fromJson(content.toString(), ListGamesResponse.class);
		listGamesResponse.setStatus(connection.getResponseCode());

		return listGamesResponse;
	}
	/*
	public LoginResponse login(String username, String password) throws IOException {
		// Code for Login API goes here
	}

	public LogoutResponse logout(String authToken) throws IOException {
		// Code for Logout API goes here
	}

	public ClearApplicationResponse clearApplication(String urlString) throws IOException {
		// Code for Clear Application API goes here
	}


	public Response joinGame(String urlString, String playerColor, String authToken, int gameId) throws IOException {
		// Code for Join Game API goes here
	}
	*/
}
