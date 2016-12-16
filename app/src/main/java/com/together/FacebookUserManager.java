package com.together;

import android.content.Context;

/**
 * Created by ziv on 12/7/2016.
 */

public class FacebookUserManager {

    private static final String FACEBOOK_USER_PREFS_NAME = "facebook_user_prefs";
    private static final String FACEBOOK_USER_PREFS_KEY = "current_facebook_user_value";

    public static void setCurrentUser(FacebookUser user, Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, FACEBOOK_USER_PREFS_NAME, 0);
        complexPreferences.putObject(FACEBOOK_USER_PREFS_KEY, user);
        complexPreferences.commit();
    }

    public static FacebookUser getCurrentUser(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, FACEBOOK_USER_PREFS_NAME, 0);
        return complexPreferences.getObject(FACEBOOK_USER_PREFS_KEY, FacebookUser.class);
    }

    public static void clearCurrentUser(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, FACEBOOK_USER_PREFS_NAME, 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }

}
