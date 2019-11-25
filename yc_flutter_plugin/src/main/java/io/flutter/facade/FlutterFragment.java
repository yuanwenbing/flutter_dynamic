//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.flutter.facade;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import io.flutter.view.FlutterView;

public class FlutterFragment extends Fragment {
    public static final String ARG_ROUTE = "route";
    private String mRoute = "/";

    public FlutterFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            this.mRoute = this.getArguments().getString("route");
        }

    }

    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

    public FlutterView onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return Flutter.createView(this.getActivity(), this.getLifecycle(), this.mRoute);
    }
}
