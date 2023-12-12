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

		StringBuilder content = new StringBuilder();

		if (connection.getResponseCode() == 200) {
			try( BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

				String line;

				while ((line = br.readLine()) != null) {
					content.append(line);
					content.append(System.lineSeparator());
				}
			}
		} else {
			try( BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {

				String line;

				while ((line = br.readLine()) != null) {
					content.append(line);
					content.append(System.lineSeparator());
				}
			} catch(Exception exception){
				exception.printStackTrace();
			}
		}

		connection.disconnect();


		RegisterResponse registerResponse;

		if (connection.getResponseCode() == 200) {
			registerResponse = gson.fromJson(content.toString(), RegisterResponse.class);
			registerResponse.setStatus(connection.getResponseCode());
		} else {
			registerResponse = new RegisterResponse();
			registerResponse.setMessage(content.toString());
			registerResponse.setStatus(connection.getResponseCode());
		}

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

		StringBuilder content=new StringBuilder();

		if (connection.getResponseCode() == 200) {
			try ( BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String line;
				while((line = br.readLine()) != null){
					content.append(line);
					content.append(System.lineSeparator());
				}
			}
		} else {
			try ( BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
				String line;
				while((line = br.readLine()) != null){
					content.append(line);
					content.append(System.lineSeparator());
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		connection.disconnect();


		CreateGameResponse createGameResponse;

		if (connection.getResponseCode() == 200) {
			createGameResponse = gson.fromJson(content.toString(), CreateGameResponse.class);
			createGameResponse.setStatus(connection.getResponseCode());
		} else {
			createGameResponse = new CreateGameResponse();
			createGameResponse.setMessage(content.toString());
			createGameResponse.setStatus(connection.getResponseCode());
		}

		return createGameResponse;
	}

	public static ListGamesResponse listGames(String authToken) throws IOException {
		Gson gson = new Gson();
		URL url = new URL(source + "/game");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setReadTimeout(5000);
		connection.setRequestMethod("GET");

		connection.addRequestProperty("Authorization", authToken);

		connection.connect();

		StringBuilder content = new StringBuilder();

		if (connection.getResponseCode() == 200) {
			try ( BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String line;
				while((line = br.readLine()) != null){
					content.append(line);
					content.append(System.lineSeparator());
				}
			}
		} else {
			try ( BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
				String line;
				while((line = br.readLine()) != null){
					content.append(line);
					content.append(System.lineSeparator());
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		connection.disconnect();

		ListGamesResponse listGamesResponse;

		if (connection.getResponseCode() == 200) {
			listGamesResponse = gson.fromJson(content.toString(), ListGamesResponse.class);
			listGamesResponse.setStatus(connection.getResponseCode());
		} else {
			listGamesResponse = new ListGamesResponse();
			listGamesResponse.setMessage(content.toString());
			listGamesResponse.setStatus(connection.getResponseCode());
		}

		return listGamesResponse;
	}

	public static LoginResponse login(String username, String password) throws IOException {
		Gson gson = new Gson();
		URL url = new URL("http://localhost:8080/session");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setReadTimeout(5000);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.addRequestProperty("Content-Type", "application/json");
		connection.connect();

		try (OutputStream requestBody = connection.getOutputStream();) {
			String inputData = "{ \"username\":\"" + username + "\", \"password\":\"" + password + "\" }";
			requestBody.write(inputData.getBytes());
			requestBody.flush();
		}

		StringBuilder content = new StringBuilder();

		if (connection.getResponseCode() == 200) {
			try ( BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String line;
				while((line = br.readLine()) != null){
					content.append(line);
					content.append(System.lineSeparator());
				}
			}
		} else {
			try ( BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
				String line;
				while((line = br.readLine()) != null){
					content.append(line);
					content.append(System.lineSeparator());
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		connection.disconnect();

		LoginResponse loginResponse;

		if (connection.getResponseCode() == 200) {
			loginResponse = gson.fromJson(content.toString(), LoginResponse.class);
			loginResponse.setStatus(connection.getResponseCode());
		} else {
			loginResponse = new LoginResponse();
			loginResponse.setMessage(content.toString());
			loginResponse.setStatus(connection.getResponseCode());
		}

		return loginResponse;
	}

	public static LogoutResponse logout(String authToken) throws IOException {
		Gson gson = new Gson();

		URL url = new URL(source + "/session");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setReadTimeout(5000);
		connection.setRequestMethod("DELETE");

		connection.addRequestProperty("Content-Type", "application/json");
		connection.addRequestProperty("authorization", authToken);

		connection.connect();

		StringBuilder content=new StringBuilder();

		if (connection.getResponseCode() != 200) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()))){
				String line;
				while((line = br.readLine()) != null){
					content.append(line);
					content.append(System.lineSeparator());
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		connection.disconnect();

		LogoutResponse logoutResponse = new LogoutResponse();

		if (connection.getResponseCode() == 200) {
			logoutResponse.setStatus(connection.getResponseCode());
		} else {
			logoutResponse.setMessage(content.toString());
			logoutResponse.setStatus(connection.getResponseCode());
		}

		return logoutResponse;
	}

	public static JoinGameResponse joinGame(String authorization, String playerColor, int gameID) throws IOException {
		Gson gson = new Gson();
		URL url = new URL(source+"/game");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("authorization", authorization);
		connection.setReadTimeout(5000);
		connection.setRequestMethod("PUT");
		connection.setDoOutput(true);
		connection.connect();

		try (OutputStream requestBody = connection.getOutputStream()) {
			String inputData;

			if(playerColor != null) {
				inputData = "{ \"playerColor\":\"" + playerColor + "\", \"gameID\":" + gameID + " }";
			} else {
				inputData = "{ \"gameID\":" + gameID + " }";
			}

			requestBody.write(inputData.getBytes());
			requestBody.flush();
		}

		StringBuilder content = new StringBuilder();

		if (connection.getResponseCode() != 200) {
			try ( BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
				String line;
				while((line = br.readLine()) != null){
					content.append(line);
					content.append(System.lineSeparator());
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		connection.disconnect();
		JoinGameResponse joinGameResponse = new JoinGameResponse();

		if (connection.getResponseCode() == 200) {
			joinGameResponse.setStatus(connection.getResponseCode());
		} else {
			joinGameResponse.setMessage(content.toString());
			joinGameResponse.setStatus(connection.getResponseCode());
		}

		return joinGameResponse;
	}
}
