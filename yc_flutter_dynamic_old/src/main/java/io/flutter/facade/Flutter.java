//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.flutter.facade;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.StringCodec;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterRunArguments;
import io.flutter.view.FlutterView;

public final class Flutter {
    private Flutter() {
    }

    public static void startInitialization(@NonNull Context applicationContext) {
        FlutterMain.startInitialization(applicationContext);
    }

    @NonNull
    public static FlutterFragment createFragment(String initialRoute) {
        FlutterFragment fragment = new FlutterFragment();
        Bundle args = new Bundle();
        args.putString("route", initialRoute);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    public static FlutterView createView(@NonNull final Activity activity, @NonNull Lifecycle lifecycle, String initialRoute) {
        FlutterMain.startInitialization(activity.getApplicationContext());
        FlutterMain.ensureInitializationComplete(activity.getApplicationContext(), (String[]) null);
        FlutterNativeView nativeView = new FlutterNativeView(activity);
        final FlutterView flutterView = new FlutterView(activity, (AttributeSet) null, nativeView) {
            private final BasicMessageChannel<String> lifecycleMessages;

            {
                this.lifecycleMessages = new BasicMessageChannel<String>(this, "flutter/lifecycle",
                        StringCodec.INSTANCE);
            }

            public void onFirstFrame() {
                super.onFirstFrame();
                this.setAlpha(1.0F);
            }

            public void onPostResume() {
                this.lifecycleMessages.send("AppLifecycleState.resumed");
            }
        };
        if (initialRoute != null) {
            flutterView.setInitialRoute(initialRoute);
        }

        lifecycle.addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            public void onCreate() {
                FlutterRunArguments arguments = new FlutterRunArguments();
                arguments.bundlePath = FlutterMain.findAppBundlePath(activity.getApplicationContext());
                arguments.entrypoint = "main";
                flutterView.runFromBundle(arguments);
                GeneratedPluginRegistrant.registerWith(flutterView.getPluginRegistry());

            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            public void onStart() {
                flutterView.onStart();
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            public void onResume() {
                flutterView.onPostResume();
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            public void onPause() {
                flutterView.onPause();
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            public void onStop() {
                flutterView.onStop();
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            public void onDestroy() {
                flutterView.destroy();
            }
        });
        flutterView.setAlpha(0.0F);
        return flutterView;
    }
}
