package test;

import apiclient.ApiClientUsers;
import apiclient.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TestUsers {
    public static void main(String[] args) {
        try {
            //positive
            test.itCanCreateUser(1, true, "Yarik", "QA");
            test.itCanUpdateUser(2, true, 3, "Oleg", "support");
            test.itCanDeleteUser(3, true, 3);
            test.itCanGetUser(4, true, 2);
            test.itCanGetUsers(5, true);
            test.itCanRegister(6, true, "eve.holt@reqres.in", "password123");
            test.itCanLogin(7, true, "eve.holt@reqres.in", "password123");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static ApiClientUsers api = new ApiClientUsers();
    static TestUsers test = new TestUsers();


    @Test
    public void itCanCreateUser (int testId, boolean logsOn, String name, String job) throws IOException {
        //data
        Response user = api.createUser(name, job);

        //logs
        if (logsOn) {
            System.out.print(testId+": ");
            responseLog(user);
        }

        //asserts
        assertCode(user, 201);
        Assert.assertEquals(name, user.body.getString("name"));
        Assert.assertEquals(job, user.body.getString("job"));
        Assert.assertTrue(user.body.toString().contains("\"id\""));
        Assert.assertTrue(user.body.toString().contains("\"createdAt\""));

        //report
        passed(testId);


    }

    @Test
    public void itCanUpdateUser (int testId, boolean logsOn, int userId, String name, String job) throws IOException {
        //data
        Response user = api.updateUserPut(userId, name, job);

        //logs
        if (logsOn) {
            System.out.print(testId+": ");
            responseLog(user);
        }

        //asserts
        assertCode(user, 200);
        Assert.assertEquals(name, user.body.getString("name"));
        Assert.assertEquals(job, user.body.getString("job"));
        Assert.assertTrue(user.body.toString().contains("\"updatedAt\""));

        //report
        passed(testId);
    }

    public void itCanDeleteUser (int testId, boolean logsOn, int userId) throws IOException {
        //data
        Response user = api.deleteUser(userId);

        //logs
        if (logsOn) {
            System.out.print(testId+": ");
            responseLog(user);
        }

        //asserts
        assertCode(user, 204);

        //report
        passed(testId);
    }

    public void itCanGetUser (int testId, boolean logsOn, int userId) throws IOException {
        //data
        Response user = api.getUser(userId);
        JSONObject data = user.body.getJSONObject("data");
        JSONObject support = user.body.getJSONObject("support");

        //logs
        if (logsOn) {
            System.out.print(testId+": ");
            responseLog(user);
        }

        //asserts
        assertCode(user, 200);
        dataValidate(data);
        supportValidate(support);

        //report
        passed(testId);
    }

    public void itCanGetUsers (int testId, boolean logsOn) throws IOException {
        //data
        Response users = api.getUsers();
        JSONArray data = users.body.getJSONArray("data");
        JSONObject support = users.body.getJSONObject("support");

        //logs
        if (logsOn) {
            System.out.print(testId+": ");
            responseLog(users);
        }

        //asserts
        assertCode(users, 200);
        for (int i = 0; i < data.length(); i++) {
            dataValidate(data.getJSONObject(i));
        }
        supportValidate(support);

        //report
        passed(testId);

    }

    public void itCanRegister (int testId, boolean logsOn, String email, String password) throws IOException {
        //data
        Response user = api.register(email, password);

        //logs
        if (logsOn) {
            System.out.print(testId+": ");
            responseLog(user);
        }

        //asserts
        assertCode(user, 200);
        Assert.assertTrue(user.body.toString().contains("id"));
        Assert.assertTrue(user.body.toString().contains("token"));

        //report
        passed(testId);

    }

    public void itCanLogin (int testId, boolean logsOn, String email, String password) throws IOException {
        //data
        Response user = api.login(email, password);

        //logs
        if (logsOn) {
            System.out.print(testId+": ");
            responseLog(user);
        }

        //asserts
        assertCode(user, 200);
        Assert.assertTrue(user.body.toString().contains("token"));

        //report
        passed(testId);

    }







    static void assertCode (Response resp, int code) {
        Assert.assertEquals(resp.statusCode, code);
    }

    static void dataValidate(JSONObject data) {
        Assert.assertTrue(data.toString().contains("id"));
        Assert.assertTrue(data.toString().contains("email"));
        Assert.assertTrue(data.toString().contains("first_name"));
        Assert.assertTrue(data.toString().contains("last_name"));
        Assert.assertTrue(data.toString().contains("avatar"));
    }

    static void supportValidate (JSONObject support) {
        Assert.assertTrue(support.toString().contains("url"));
        Assert.assertTrue(support.toString().contains("text"));

    }

    static void passed (int testId) {
        System.out.println(testId +": passed");
    }

    static void responseLog (Response response) {
        System.out.println(response.body);
    }


}
