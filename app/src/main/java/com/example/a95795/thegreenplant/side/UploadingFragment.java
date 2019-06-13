package com.example.a95795.thegreenplant.side;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.MultiPartStack;
import com.example.a95795.thegreenplant.custom.MultiPartStringRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadingFragment extends SupportFragment {
    public static UploadingFragment newInstance() {
        return new UploadingFragment();
    }

    private static String TAG = "MainActivity";
    private Button btn_upload;
    private static ProgressDialog mProgress;
    private RequestQueue requestQueue;
    private static RequestQueue mSingleQueue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_uploading, container, false);
        initView(view);
        requestWritePermission();
        Button button = (Button) view.findViewById(R.id.btn_upload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadFile();
            }
        });
        return view;

    }
    private void initView(View view) {

        btn_upload = (Button) view.findViewById(R.id.btn_upload);
        requestQueue = Volley.newRequestQueue(getContext());
        mSingleQueue = Volley.newRequestQueue(getContext(), new MultiPartStack());

    }

    private void UploadFile(){
        showProgress();
        Map<String, File> files = new HashMap<String, File>();
        files.put("image1", new File(
                "/sdcard/IMG_1528.JPG"));
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", "DJrlPbpJQs21rv1lP41yiA==");
        String uri = "http://134.175.176.168:80/Uploadfile/uploadServlet";
        addPutUploadFileRequest(
                uri,
                files, params, mResonseListenerString, mErrorListener, null);
    }
    Response.Listener<String> mResonseListenerString = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //返回的数据
            Toast.makeText(getContext(),"返回的数据:"+response,Toast.LENGTH_SHORT).show();
        }
    };

    Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error != null) {
                if (error.networkResponse != null)
                    Log.i(TAG, " error " + new String(error.networkResponse.data));
            }
        }
    };

    public static void addPutUploadFileRequest(final String url,
                                               final Map<String, File> files, final Map<String, String> params,
                                               final Response.Listener<String> responseListener, final Response.ErrorListener errorListener,
                                               final Object tag) {
        if (null == url || null == responseListener) {
            return;
        }

        MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
                Request.Method.POST, url, responseListener, errorListener) {
            @Override
            public Map<String, File> getFileUploads() {
                return files;
            }
            @Override
            public Map<String, String> getStringUploads() {
                return params;
            }

        };
        hiddenProgress();
        mSingleQueue.add(multiPartRequest);
    }

    private void showProgress() {
        mProgress = ProgressDialog
                .show(getContext(), "提示", "请稍等", false);
    }

    private static void hiddenProgress() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.cancel();
        }
    }
    //运行时候获取权限
    private void requestWritePermission(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadingFragment.this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

}
