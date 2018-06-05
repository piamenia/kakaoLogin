package kr.co.hoon.a180604kakaologin;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {
    // 안드로이드 키해시를 로그로 출력하는 메소드
    // 키해시: 안드로이드 앱마다 별도로 생성되는 ID 같은 개념
    private void getHashKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo("kr.co.hoon.a180604kakaologin", PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){
                // SHA는 암호화 알고리즘의 종류, MessageDigest는 암호화 관련모듈
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                // 키해시값 출력
                Log.e("키해시", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }catch(Exception e){
            Log.e("해시 가져오기 실패", e.getMessage());
        }
    }

    class SessionCallback implements ISessionCallback{
        @Override
        public void onSessionOpened() {
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {

                }

                @Override
                public void onNotSignedUp() {

                }

                // 로그인 성공했을 때
                @Override
                public void onSuccess(UserProfile result) {
//                    Log.e("사용자 정보", result.toString());
//                    Log.e("사용자 이름", result.getNickname());
                    Toast.makeText(MainActivity.this, result.getNickname()+"님으로 로그인되었습니다.",Toast.LENGTH_LONG).show();
                }

                // 로그인 실패했을 경우
                @Override
                public void onFailure(ErrorResult errorResult) {
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
//            Log.e("연결 실패", exception.toString());
        }
    }

    SessionCallback sessionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 키해시를 출력하기 위해 메소드 호출
        // /2cgLpQkfZS5IrAdLhIFCurGu8o=
        getHashKey();

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);

        findViewById(R.id.logout).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Toast.makeText(MainActivity.this, "로그아웃 되었습니다.",Toast.LENGTH_LONG).show();
                        Log.e("로그아웃", "됐음");
                    }
                });
            }
        });
    }
}
