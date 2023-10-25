package handlers;

import com.google.gson.Gson;
import requests.*;
import responses.*;
import services.Services;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class Handlers {
    public static String handleClearApplication(Request req, Response res){
        System.out.println("Clear app data");
        Gson gson = new Gson();

        ClearApplicationRequest request = gson.fromJson(req.body(), ClearApplicationRequest.class);
        ClearApplicationResponse response = Services.clearApplication(request);
        res.status(response.getStatus());

        response.setStatus(null);
        response.setSuccess(null);
        //res.body(gson.toJson(response, ClearApplicationResponse.class));
        return gson.toJson(response, ClearApplicationResponse.class);
    }
    public static String handleCreateGame(Request req, Response res){
        System.out.println("Create game; body:" + req.body() + ", token:" + req.headers("authorization"));
        Gson gson = new Gson();

        CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
        request.addHeader("authorization",req.headers("authorization") );
        CreateGameResponse response = Services.createGame(request);

        res.status(response.getStatus());
        response.setStatus(null);

        response.setSuccess(null);
        //res.body(gson.toJson(response, CreateGameResponse.class));
        return gson.toJson(response, CreateGameResponse.class);
    }
    public static String handleJoinGame(Request req, Response res){
        System.out.println("join game; body:" + req.body() + ", token:" + req.headers("authorization"));
        Gson gson = new Gson();
        JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
        request.addHeader("authorization",req.headers("authorization") );
        JoinGameResponse response = Services.joinGame(request);
        res.status(response.getStatus());

        response.setStatus(null);
        response.setSuccess(null);
        //res.body(gson.toJson(response, JoinGameResponse.class));
        return gson.toJson(response, JoinGameResponse.class);
    }
    public static String handleListGames(Request req, Response res){
        System.out.println("list games; body:" + req.body() + ", token:" + req.headers("authorization"));
        Gson gson = new Gson();

        ListGamesRequest request = new ListGamesRequest();
        request.addHeader("authorization",req.headers("authorization") );
        ListGamesResponse response = Services.listGames(request);
        res.status(response.getStatus());


        response.setStatus(null);
        response.setSuccess(null);
        //res.body(gson.toJson(response, ListGamesResponse.class));

        return gson.toJson(response, ListGamesResponse.class);
    }
    public static String handleLogin(Request req, Response res){
        System.out.println("login; body:" + req.body());
        Gson gson = new Gson();

        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
        LoginResponse response = Services.login(request);

        res.status(response.getStatus());
        response.setStatus(null);
        response.setSuccess(null);
        res.body(gson.toJson(response, LoginResponse.class));
        return gson.toJson(response, LoginResponse.class);
    }
    public static String handleLogout(Request req, Response res){
        System.out.println("logout; token:" + req.headers("authorization"));
        Gson gson = new Gson();
        LogoutRequest request = new LogoutRequest();
        request.addHeader("authorization",req.headers("authorization") );
        LogoutResponse response = Services.logout(request);
        res.status(response.getStatus());

        response.setStatus(null);
        response.setSuccess(null);
        //res.body(gson.toJson(response, JoinGameResponse.class));
        return gson.toJson(response, JoinGameResponse.class);
    }
    public static String handleRegister(Request req, Response res){
        System.out.println("register; body:" + req.body());
        Gson gson = new Gson();
        RegisterRequest request;
        if(req.body() == null || req.body().equals("")){
            request = new RegisterRequest();
        }else{
            request= gson.fromJson(req.body(), RegisterRequest.class);
        }
        RegisterResponse response = Services.registerUser(request);
        res.status(response.getStatus());

        response.setStatus(null);
        response.setSuccess(null);
        //res.body(gson.toJson(response, RegisterResponse.class));

        return gson.toJson(response, RegisterResponse.class);
    }
}
