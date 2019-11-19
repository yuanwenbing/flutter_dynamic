// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Log;

import com.yuan.dynamic.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import io.flutter.app.FlutterPluginRegistry;
import io.flutter.embedding.engine.FlutterEngine.EngineLifecycleListener;
import io.flutter.embedding.engine.FlutterJNI;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.embedding.engine.renderer.FlutterUiDisplayListener;
import io.flutter.plugin.common.BinaryMessenger;

public class FlutterNativeView implements BinaryMessenger {
    private static final String TAG = "FlutterNativeView";

    private final FlutterPluginRegistry mPluginRegistry;
    private final DartExecutor dartExecutor;
    private FlutterView mFlutterView;
    private final FlutterJNI mFlutterJNI;
    private final Context mContext;
    private boolean applicationIsRunning;

    private final FlutterUiDisplayListener flutterUiDisplayListener = new FlutterUiDisplayListener() {
        @Override
        public void onFlutterUiDisplayed() {
            if (mFlutterView == null) {
                return;
            }
            mFlutterView.onFirstFrame();
        }

        @Override
        public void onFlutterUiNoLongerDisplayed() {
            // no-op
        }
    };

    public FlutterNativeView(@NonNull Context context) {
        this(context, false);
    }

    public FlutterNativeView(@NonNull Context context, boolean isBackgroundView) {
        mContext = context;
        mPluginRegistry = new FlutterPluginRegistry(this, context);
        mFlutterJNI = new FlutterJNI();
        mFlutterJNI.addIsDisplayingFlutterUiListener(flutterUiDisplayListener);
        this.dartExecutor = new DartExecutor(mFlutterJNI, context.getAssets());
        mFlutterJNI.addEngineLifecycleListener(new EngineLifecycleListenerImpl());
        attach(this, isBackgroundView);
        assertAttached();
    }

    public void detachFromFlutterView() {
        mPluginRegistry.detach();
        mFlutterView = null;
    }

    public void destroy() {
        mPluginRegistry.destroy();
        dartExecutor.onDetachedFromJNI();
        mFlutterView = null;
        mFlutterJNI.removeIsDisplayingFlutterUiListener(flutterUiDisplayListener);
        mFlutterJNI.detachFromNativeAndReleaseResources();
        applicationIsRunning = false;
    }

    @NonNull
    public DartExecutor getDartExecutor() {
        return dartExecutor;
    }

    @NonNull
    public FlutterPluginRegistry getPluginRegistry() {
        return mPluginRegistry;
    }

    public void attachViewAndActivity(FlutterView flutterView, Activity activity) {
        mFlutterView = flutterView;
        mPluginRegistry.attach(flutterView, activity);
    }

    public boolean isAttached() {
        return mFlutterJNI.isAttached();
    }

    public void assertAttached() {
        if (!isAttached()) throw new AssertionError("Platform view is not attached");
    }

    public void runFromBundle(FlutterRunArguments args) {
        if (args.entrypoint == null) {
            throw new AssertionError("An entrypoint must be specified");
        }
        assertAttached();

        AssetManager assetManager =
                assetManager(mContext.getFilesDir() + File.separator + "res.apk");

        try {
            String[] flutter_assets = assetManager.list("flutter_assets");
            if(flutter_assets != null) {
                Log.d(TAG, "flutter_assets.length:" + flutter_assets.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String[] flutter_assets = assetManager.list("Tencent");
            for (String flutter_asset : flutter_assets) {
                if (BuildConfig.DEBUG) Log.d("AAAAA", flutter_asset);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (applicationIsRunning)
            throw new AssertionError(
                    "This Flutter engine instance is already running an application");
        mFlutterJNI.runBundleAndSnapshotFromLibrary(
                args.bundlePath,
                args.entrypoint,
                args.libraryPath,
                assetManager
        );

        applicationIsRunning = true;
    }

    private AssetManager assetManager(String skinPath) {
        AssetManager assetManager = null;
        try {
            assetManager = AssetManager.class.newInstance();
            try {
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath",
                        String.class);
                addAssetPath.invoke(assetManager, skinPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Resources superRes = mContext.getResources();
            Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(),
                    superRes.getConfiguration());
            return resources.getAssets();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assetManager;
    }



    public boolean isApplicationRunning() {
        return applicationIsRunning;
    }

    public static String getObservatoryUri() {
        return FlutterJNI.getObservatoryUri();
    }

    @Override
    @UiThread
    public void send(String channel, ByteBuffer message) {
        dartExecutor.getBinaryMessenger().send(channel, message);
    }

    @Override
    @UiThread
    public void send(String channel, ByteBuffer message, BinaryReply callback) {
        if (!isAttached()) {
            Log.d(TAG, "FlutterView.send called on a detached view, channel=" + channel);
            return;
        }

        dartExecutor.getBinaryMessenger().send(channel, message, callback);
    }

    @Override
    @UiThread
    public void setMessageHandler(String channel, BinaryMessageHandler handler) {
        dartExecutor.getBinaryMessenger().setMessageHandler(channel, handler);
    }

    /*package*/ FlutterJNI getFlutterJNI() {
        return mFlutterJNI;
    }

    private void attach(FlutterNativeView view, boolean isBackgroundView) {
        mFlutterJNI.attachToNative(isBackgroundView);
        dartExecutor.onAttachedToJNI();
    }

    private final class EngineLifecycleListenerImpl implements EngineLifecycleListener {
        // Called by native to notify when the engine is restarted (cold reload).
        @SuppressWarnings("unused")
        public void onPreEngineRestart() {
            if (mFlutterView != null) {
                mFlutterView.resetAccessibilityTree();
            }
            if (mPluginRegistry == null) {
                return;
            }
            mPluginRegistry.onPreEngineRestart();
        }
    }
}
