package com.example.project2bookingsample.data;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import com.example.project2bookingsample.HTTPRequestSyncRest;
import com.example.project2bookingsample.data.model.LoggedInUser;

import java.io.IOException;
import java.net.URL;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            String loginFmt = "http://10.0.2.2:8080/api/login/authenticate?userid=%d&password=%s";
            // giving the username as an integer (long) as the API requires
            @SuppressLint("DefaultLocale") URL loginURL =
                    new URL(String.format(loginFmt, Integer.parseInt(username), password));

            HTTPRequestSyncRest httpRequest = new HTTPRequestSyncRest();
            httpRequest.setUrl(loginURL);
            httpRequest.setRequestMethod("GET");
//            Log.d("DEBUG", "http request sent to authenticate");
            httpRequest.sendAndAwaitResponse();
//            Log.d("DEBUG", "await response");
            String message = httpRequest.getResponseContent();
//            Log.d("DEBUG", message);
            if(message.equals("User Not Found")){
                throw new Exception("User Not Found");
            } else if(message.equals("Invalid Password")){
                throw new Exception("Invalid Password");
            } else{
                LoggedInUser loggedinUser = new LoggedInUser(username, "tmp");
                return new Result.Success<>(loggedinUser);
            }

        }catch(NumberFormatException e){
            return new Result.Error(new IOException("Cannot convert username string to number", e));
        }
        catch (Exception e) {
//            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}