package com.example.a95795.thegreenplant.side;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.FileUtil;
import com.example.a95795.thegreenplant.custom.MultiPartStack;
import com.example.a95795.thegreenplant.custom.MultiPartStringRequest;

import java.io.File;
import java.io.IOException;
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
    private  Button btn_upload;
    private static ProgressDialog mProgress;
    private RequestQueue requestQueue;
    private static RequestQueue mSingleQueue;
    public static final int RC_CHOOSE_PHOTO = 2;
    public static final int RC_TAKE_PHOTO = 1;
    public String URL = "";
    private String mTempPhotoPath;
    private Uri imageUri;
    ImageView ivImage;
    MultiPartStack multiPartStack;
    private int REQUESTCODE = 100;//请求码
    private ImageView img;
    private String INTENT_TYPE  = "image/*";//意图类型
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_uploading, container, false);
        ivImage= view.findViewById(R.id.imageView5);
        initView(view);
        Button button = (Button) view.findViewById(R.id.btn_upload);
        Button button1 = (Button) view.findViewById(R.id.choose_img);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadFile();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//获取行动内容
                intent.setType(INTENT_TYPE);
                startActivityForResult(intent,REQUESTCODE);
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
        if (URL.equals("")){
            Toast.makeText(getContext(),"不能上传空照片",Toast.LENGTH_LONG).show();
        }else {
            showProgress();
            Map<String, File> files = new HashMap<String, File>();
            files.put("image1", new File(
                    URL));
            Map<String, String> params = new HashMap<String, String>();
            params.put("token", "DJrlPbpJQs21rv1lP41yiA==");
            String uri = "http://134.175.176.168:80/Uploadfile/uploadServlet";
            addPutUploadFileRequest(
                    uri,
                    files, params, mResonseListenerString, mErrorListener, null);
        }

    }

    Response.Listener<String> mResonseListenerString = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //返回的数据
            Toast.makeText(getContext(),"已经成功上传",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            Log.e("TAG--->onresult","ActivityResult resultCode error");
            return;
        }

        //获得图片
        Bitmap bitmap = null;//位图
        ContentResolver resolver = getActivity().getContentResolver();//分析器
        if(requestCode == REQUESTCODE){
            Uri uri = data.getData();
            URL  =   getRealPathFromUri(getContext(),uri);


            try {
                bitmap = MediaStore.Images.Media.getBitmap(resolver,uri);//获得图片

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
         ivImage.setImageBitmap(bitmap);

    }

    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getRealPathFromUriAboveApi19(context, uri);
        } else { // api < 19
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())){
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


}
