package com.journwe.anroid;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

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

		EditText editUser = (EditText) findViewById(R.id.editUser);
		String user = editUser.getText().toString();
		EditText editPass = (EditText) findViewById(R.id.editPass);
		String pass = editPass.getText().toString();

        String callbackURL = "oauth://facebook";

        final OAuthService s = new ServiceBuilder()
                .provider(FacebookApi.class)
                .apiKey("515340035175937")
                .apiSecret("b5788518d689a6613a581722c98f4cf4").callback(callbackURL).debug().scope("email,xmpp_login")
                .build();

        Log.i("login", "still running");


		String authorisationURL = s.getAuthorizationUrl(null);

        final WebView webview = (WebView) findViewById(R.id.webview);
      //  final Token requestToken = s.getRequestToken();
        final String authURL = s.getAuthorizationUrl(null);

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {

                //check for our custom callback protocol
                //otherwise use default behavior
                if(url.startsWith("oauth"))
                {
                    //authorization complete hide webview for now.
                    webview.setVisibility(View.GONE);
                    Uri uri = Uri.parse(url);
                    String verifier = uri.getQueryParameter("oauth_verifier");
                    Verifier v = new Verifier(verifier);

                    //save this token for practical use.
                    Token accessToken = s.getAccessToken(null, v);

                    OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.fitbit.com/1/user/-/activities/date/2012-12-15.json");
                    s.signRequest(accessToken, request);
                    Response response = request.send();
                    System.out.print("JSON:"+response.getCode());

                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        //send user to authorization page
        webview.loadUrl(authURL);

        Log.i("login", authURL);

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
