package tfn.android.lollipops;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.text.SimpleDateFormat;

import tfn.android.lollipops.view.widget.blogtextview.BlogTextView;

public class MainActivity extends AppCompatActivity {

    private Oauth2AccessToken accessToken;
    private SsoHandler mSsoHandler;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv_token);
        BlogTextView t = findViewById(R.id.blog_text);
        t.setContent("#圣诞老人吴世勋# @wu世勋-EXO 我在 #明星势力榜# 韩国榜上为你加油啦，你是我今生唯一的执著哦。棒棒哒！http://t.cn/R4Znv1l @超级应援 http://t.cn/R6o35Tb ?");

        WbSdk.install(this, new AuthInfo(this, AppConstants.APP_KEY, AppConstants.REDIRECT_URL, AppConstants.SCOPE));
        mSsoHandler = new SsoHandler(this);


        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSsoHandler.authorize(new AuthListener());

            }
        });

    }

    class AuthListener implements com.sina.weibo.sdk.auth.WbAuthListener{

        @Override
        public void onSuccess(Oauth2AccessToken token) {
            accessToken = token;
            if (accessToken.isSessionValid()) {
                showToken(false);
                AccessTokenKeeper.writeAccessToken(MainActivity.this, accessToken);
                Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void cancel() {
            Toast.makeText(MainActivity.this, "授权取消", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            Toast.makeText(MainActivity.this,
                    "Auth exception : " + wbConnectErrorMessage.getErrorCode() + wbConnectErrorMessage.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    private void showToken(boolean b) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new java.util.Date(accessToken.getExpiresTime()));
        String format = "Token：%1$s \\n有效期：%2$s";
        tv.setText(String.format(format, accessToken.getToken(), date));

        String message = String.format(format, accessToken.getToken(), date);
        if (b) {
            message = "Token 仍在有效期内，无需再次登录。"
                    + "\n" + message;
        }
        tv.setText(message);

    }


}
