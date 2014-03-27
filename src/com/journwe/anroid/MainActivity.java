package com.journwe.anroid;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.journwe.android.LOGIN";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void login(View view) {
		// Intent intent = new Intent(this, DisplayMessageActivity.class);
		// String message = editText.getText().toString();
		// intent.putExtra(EXTRA_MESSAGE, message);
		// startActivity(intent);

		Log.i("login", "start login");

//		EditText editUser = (EditText) findViewById(R.id.editUser);
//		String user = editUser.getText().toString();
//		EditText editPass = (EditText) findViewById(R.id.editPass);
//		String pass = editPass.getText().toString();

		OAuthService service = new ServiceBuilder()
				.provider(FacebookApi.class)
				.apiKey("515340035175937")
				.apiSecret("b5788518d689a6613a581722c98f4cf4")
				.build();
		
		Log.i("login", "still running");

//		Token requestToken = service.getRequestToken();
//		String authUrl = service.getAuthorizationUrl(requestToken);
//
//		Verifier v = new Verifier("verifier you got from the user");
//		Token accessToken = service.getAccessToken(requestToken, v);
//
//		OAuthRequest request = new OAuthRequest(Verb.GET,
//				"https://graph.facebook.com/me");
//		service.signRequest(accessToken, request); // the access token from step
//													// 4
//		Response response = request.send();
//		editUser.setText(response.getBody());
	}
}
