package com.together;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends FragmentActivity {

    final String TAG_SUCCESS = "Together/LoginSuccess";
    final String TAG_CANCEL = "Together/LoginCancel";
    final String TAG_ERROR = "Together/LoginError";

    Button mLoginButton;
    CallbackManager mCallbackManager;

    private FacebookCallback<LoginResult> mFacebookLoginCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.e(TAG_SUCCESS, "Logged in successfully");
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), mSaveUserInfoCallback);
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Log.e(TAG_CANCEL,"On cancel");
        }

        @Override
        public void onError(FacebookException error) {
            Log.e(TAG_ERROR,error.toString());
        }
    };

    private GraphRequest.GraphJSONObjectCallback mSaveUserInfoCallback = new GraphRequest.GraphJSONObjectCallback() {

        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {
            if (response.getError() != null) {
                Log.e(TAG_ERROR, response.getError().toString());
            } else {
                try {
                    FacebookUser user = new FacebookUser();
                    user.facebookId = object.getString("id");
                    user.name = object.getString("name");
                    user.email = object.getString("email");
                    FacebookUserManager.setCurrentUser(user, getApplicationContext());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());


        if(FacebookUserManager.getCurrentUser(this) != null && AccessToken.getCurrentAccessToken() != null) {
            Intent intent = new Intent(this, ExploreActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_login);
            mLoginButton = (Button) findViewById(R.id.login_button);
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFacebookLogin();
                }
            });
        }
    }

    private void onFacebookLogin(){
        Log.e("TOGETHER", "onFacebookLogin");
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_friends"));
        LoginManager.getInstance().registerCallback(mCallbackManager, mFacebookLoginCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Together", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("Together", "callback called");
    }

}
