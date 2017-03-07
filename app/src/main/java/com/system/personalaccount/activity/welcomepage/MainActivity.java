package com.system.personalaccount.activity.welcomepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.system.personalaccount.R;
import com.system.personalaccount.activity.function1.FirstActivity;

/**
 * 引导页，等待3秒后自动跳转到第一功能页
 */
public class MainActivity extends Activity {

    /** 线程 **/
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 使当前界面全屏 **/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        /** 开启线程 **/
        if (thread == null) {
            thread = new myThread();
            thread.start();
        }
    }

    /**
     * 自定义线程类，实现等待3秒
     */
    class myThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            welcomeFunction();
        }
    }

    /**
     * 页面跳转，进入第一功能页，并关闭本页
     */
    private void welcomeFunction() {
        Intent intent = new Intent(MainActivity.this, FirstActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }
}
