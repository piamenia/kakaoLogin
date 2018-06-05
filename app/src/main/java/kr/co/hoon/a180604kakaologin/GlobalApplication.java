package kr.co.hoon.a180604kakaologin;

import android.app.Activity;
import android.app.Application;

import com.kakao.auth.KakaoSDK;

public class GlobalApplication extends Application {
    // 인스턴스변수 선언
    // volatile 멀티스레드 프로그래밍에서 한번에 작업을 수행하도록하는 예약어
    // 멀티스레드 프로그래밍에서 long을 사용할 때 붙이는 경우가 많음
    private static volatile GlobalApplication obj = null;
    private static volatile Activity currentActivity = null;

    // 안드로이드 스튜디오에서는 상속을 받고 메소드를 재정의 할 떄 에러메시지를 출력하는 경우가 있는데
    // 이 경우는 상위클래스의 메소드를 반드시 호출해야함
    @Override
    public void onCreate() {
        super.onCreate();
        obj = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public static GlobalApplication getGlobalApplicationContext(){
        return obj;
    }

    public static Activity getCurrentActivity (){
        return currentActivity;
    }

    public static GlobalApplication getObj() {
        return obj;
    }

    public static void setObj(GlobalApplication obj) {
        GlobalApplication.obj = obj;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }
}
