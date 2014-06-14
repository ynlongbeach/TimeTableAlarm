package com.timetablealarm.twitter;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationContext;

import com.timetablealarm.R;
import com.timetablealarm.R.layout;

import android.app.Activity;
import android.content.*;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TwitterOAuthActivity extends Activity {

	final String CONSUMER_KEY = "vaglMm7Ya2a5ND7RxT6KkWE8j";
	final String CONSUMER_SECRET = "F8GkUMK4t7nGtB4QtJZKXhKCRcpevu1HtpdEY6i81ffXerr1xs";
	final String CALLBACK_URL = "foo://bar";  
	final String OAUTH_VERIFIER = "oauth_verifier";
	final String KEY_TOKEN ="pikachu";  
	final String KEY_TOKEN_SECRET ="kairyu";  
	private AccessToken accessToken;
	private String token;
	private String tokenSecret;
	

    public static RequestToken _req = null;
    public static OAuthAuthorization _oauth = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
		setContentView(R.layout.activity_twitter_oauth);
		Button btn = (Button)findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ

                startTwitterOAuth();
			}
		});
		
		Button btn2 = (Button)findViewById(R.id.button2);
		btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ

                Tweet();
			}
		});
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		Uri uri = getIntent().getData();
        if(uri != null && uri.toString().startsWith("demotwittercallback://TwitterOAuthActivity")){
            TwitterCallbackAsyncTask callbackTask = new TwitterCallbackAsyncTask();
            callbackTask.execute(uri);
            try {
				this.accessToken = callbackTask.get();
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
        }
	}
	
	private void startTwitterOAuth(){
        //Twitetr4jの設定を読み込む
		try{
			ConfigurationBuilder cb = new ConfigurationBuilder();
	        _oauth = new OAuthAuthorization(cb.build());
		} catch (Exception e) {
            Log.v("TEST", "err:" + e.getMessage(), e);
        }
        //Oauth認証オブジェクト作成
        //Oauth認証オブジェクトにconsumerKeyとconsumerSecretを設定
        _oauth.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        //アプリの認証オブジェクト作成
        try {
            //認証後アプリに戻るようにcallbackを設定
            _req = _oauth.getOAuthRequestToken("demotwittercallback://TwitterOAuthActivity");
        } catch (TwitterException e) {
            Log.v("TEST", "err:" + e.getErrorMessage(), e);
        }
        String uri = _req.getAuthorizationURL();

        //ブラウザーアプリへ移動、RequestTokenを取得するために
        startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(uri)));
        finish();
    }
	
	private void Tweet(){
		Twitter twitter = new TwitterFactory().getInstance();  
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);  
		twitter.setOAuthAccessToken(this.accessToken);  
		try {  
			twitter.updateStatus("Test");  
		} catch (TwitterException e) {  
		    android.util.Log.e("TwitterException", e.toString());  
		}  
	}
}