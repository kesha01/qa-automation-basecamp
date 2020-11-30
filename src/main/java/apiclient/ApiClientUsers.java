package apiclient;

import config.Config;
import data.Endpoints;
import org.json.JSONObject;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ApiClientUsers {
    private static String usersUrl () {return Config.BASE_URL + Endpoints.USERS;}
    private static String userUrl (int id) {return Config.BASE_URL + Endpoints.USERS + "/" + id;}
    private static String registerUrl () {return Config.BASE_URL + Endpoints.REGISTER;}
    private static String loginUrl () {return Config.BASE_URL + Endpoints.LOGIN;}

    private static HttpURLConnection usersResource () throws IOException {
        URL url = new URL (usersUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        return connection;
    }



    private static HttpURLConnection userResource (int id) throws IOException {
        URL url = new URL (userUrl(id));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        return connection;
    }

    private static HttpURLConnection registerResource () throws IOException {
        URL url = new URL(registerUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        return connection;
    }
    private static HttpURLConnection loginResource () throws IOException {
        URL url = new URL(loginUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        return connection;
    }

    private static String rawResponse (HttpURLConnection connection) throws IOException {
        String line;
        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    private static JSONObject jsonResponse (HttpURLConnection connection) throws IOException {
        JSONObject response = new JSONObject(rawResponse(connection));

        return response;
    }

    private static void post (HttpURLConnection connection, String body) throws IOException {
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = body.getBytes("utf-8");
            for (int i = 0; i < input.length; i++) {
                os.write(input[i]);
            }
        }
    }

    private static void put (HttpURLConnection connection, String body) throws IOException {
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = body.getBytes("utf-8");
            for (int i = 0; i < input.length; i++) {
                os.write(input[i]);
            }
        }
    }




    public Response getUser (int id) throws IOException {
        HttpURLConnection connection = userResource(id);
        connection.setRequestMethod("GET");
        connection.disconnect();

        Response response = new Response(connection.getResponseCode(), jsonResponse(connection));

        return response;
    }

    public Response getUsers () throws IOException {
        HttpURLConnection connection = usersResource();
        connection.setRequestMethod("GET");
        connection.disconnect();

        Response response = new Response(connection.getResponseCode(), jsonResponse(connection));

        return response;
    }

    public Response createUser (String name, String job ) throws IOException {
        HttpURLConnection connection = usersResource();
        post(connection, "{\"name\": \"" + name + "\", \"job\": \""+job+"\"}");
        //System.out.println("Status code: "+connection.getResponseCode());
        connection.disconnect();

        Response response = new Response(connection.getResponseCode(), jsonResponse(connection));

        return response;
    }

    public Response updateUserPut (int id, String name, String job ) throws IOException {
        HttpURLConnection connection = userResource(id);
        put(connection, "{\"name\": \"" + name + "\", \"job\": \""+job+"\"}");
        //System.out.println("Status code: "+connection.getResponseCode());
        connection.disconnect();

        Response response = new Response(connection.getResponseCode(), jsonResponse(connection));

        return response;
    }

    public Response register (String email, String password) throws IOException {
        HttpURLConnection connection = registerResource();
        post(connection, "{\"email\": \"" + email + "\", \"password\": \""+password+"\"}");
        connection.disconnect();

        Response response = new Response(connection.getResponseCode(), jsonResponse(connection));

        return response;
    }

    public Response login (String email, String password) throws IOException {
        HttpURLConnection connection = loginResource();
        post(connection, "{\"email\": \"" + email + "\", \"password\": \""+password+"\"}");
        connection.disconnect();

        Response response = new Response(connection.getResponseCode(), jsonResponse(connection));

        return response;
    }

    public Response deleteUser (int id) throws IOException {
        HttpURLConnection connection = userResource(id);
        connection.setRequestMethod("DELETE");
        connection.disconnect();

        Response response = new Response(connection.getResponseCode());

        return response;
    }



}
