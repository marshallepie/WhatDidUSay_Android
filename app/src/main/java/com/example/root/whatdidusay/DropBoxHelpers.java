package com.example.root.whatdidusay;

import android.content.Context;

import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

/**
 * Created by dottechnologies on 7/9/15.
 */
public class DropBoxHelpers {
    private Context mContext;

    public DropBoxHelpers(Context cont){
        mContext = cont;

    }
    public void storeAuth(AndroidAuthSession session) {
        // Store the OAuth 2 access token, if there is one.
        String oauth2AccessToken = session.getOAuth2AccessToken();
        if (oauth2AccessToken != null) {
            Prefrences prefs = new Prefrences(mContext);
            prefs.setStringPrefs(Prefrences.ACCESS_KEY_NAME, "oauth2:");
            prefs.setStringPrefs(Prefrences.ACCESS_SECRET_NAME, oauth2AccessToken);
            return;
        }
        // Store the OAuth 1 access token, if there is one.  This is only necessary if
        // you're still using OAuth 1.
        AccessTokenPair oauth1AccessToken = session.getAccessTokenPair();
        if (oauth1AccessToken != null) {

            Prefrences prefs = new Prefrences(mContext);
            prefs.setStringPrefs(Prefrences.ACCESS_KEY_NAME, oauth1AccessToken.key);
            prefs.setStringPrefs(Prefrences.ACCESS_SECRET_NAME, oauth1AccessToken.secret);

            return;
        }
    }


    public AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(ConfigDropBox.APP_KEY, ConfigDropBox.APP_SECRET);

        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        loadAuth(session);
        return session;
    }

    public void loadAuth(AndroidAuthSession session) {
        Prefrences prefs = new Prefrences(mContext);
        String key = prefs.getString(Prefrences.ACCESS_KEY_NAME);
        String secret = prefs.getString(Prefrences.ACCESS_SECRET_NAME);
        if (key == null || secret == null || key.length() == 0 || secret.length() == 0) return;

        if (key.equals("oauth2:")) {
            // If the key is set to "oauth2:", then we can assume the token is for OAuth 2.
            session.setOAuth2AccessToken(secret);
        } else {
            // Still support using old OAuth 1 tokens.
            session.setAccessTokenPair(new AccessTokenPair(key, secret));
        }
    }


}
