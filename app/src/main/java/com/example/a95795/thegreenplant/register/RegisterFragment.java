package com.example.a95795.thegreenplant.register;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.custom.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.yokeyword.swipebackfragment.SwipeBackFragment;


public class RegisterFragment extends SwipeBackFragment {


    private EditText userName, passWord,name,ID_number,job_number,phone_number,passWordAgain;
    private TextInputLayout mTILUsername,mTILUsername2,mTILUsername3,mTILUsername4,mTILUsername5,mTILUsername6,mTILUsername7 ;
    private String PassWord,PassWord_again,UserName,Name,ID_Number,Job_Number,Phone_Number;
    private Button button;
    private int Id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        init(view);user();passwordagain();id_number();job_number();phone_number();password();name();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextInit();ifInit();
            }
        });
        return attachToSwipeBack(view);

    }

    public void init(View view){
        userName = view.findViewById(R.id.user);
        name = view.findViewById(R.id.name);
        ID_number = view.findViewById(R.id.ID_number);
        job_number = view.findViewById(R.id.job_number);
        phone_number = view.findViewById(R.id.phone_number);
        passWord = view.findViewById(R.id.password);
        passWordAgain = view.findViewById(R.id.password_agin);
        mTILUsername = view.findViewById(R.id.textInputLayout3);
        mTILUsername2 = view.findViewById(R.id.textInputLayout4);
        mTILUsername3 = view.findViewById(R.id.textInputLayout5);
        mTILUsername4 = view.findViewById(R.id.textInputLayout6);
        mTILUsername5 = view.findViewById(R.id.textInputLayout7);
        mTILUsername6 = view.findViewById(R.id.textInputLayout8);
        mTILUsername7 = view.findViewById(R.id.textInputLayout9);
        button = view.findViewById(R.id.button3);
    }
    public void editTextInit(){
        UserName = userName.getText().toString();
        Name = name.getText().toString();
        ID_Number = ID_number.getText().toString();
        Job_Number = job_number.getText().toString();
        Phone_Number = phone_number.getText().toString();
        PassWord = passWord.getText().toString();
        PassWord_again = passWordAgain.getText().toString();
    }
    public void ifInit(){
        if(UserName.equals("")){
            mTILUsername.setError("用户名不能为空");
        }else if(Name.equals("")){
            mTILUsername2.setError("姓名不能为空");
        }else if (ID_Number.equals("")){
            mTILUsername3.setError("身份证不能为空");
        }else if(Job_Number.equals("")){
            mTILUsername4.setError("工号不能为空");
        }else if(Phone_Number.equals("")){
            mTILUsername5.setError("手机号码不能为空");
        }else if (PassWord.equals("")){
            mTILUsername6.setError("密码不能为空");
        }else if (PassWord_again.equals("")){
            mTILUsername7.setError("密码不能为空");
        }else if (PassWord.length() <= PassWord_again.length()) {
            mTILUsername7.setErrorEnabled(false);
            if (PassWord_again.equals(PassWord)) {
                mTILUsername7.setErrorEnabled(false);
                acquire();
            } else {
                mTILUsername7.setError("两次密码不相同 请重新输入！");
            }
        }
    }
    public void user(){
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    mTILUsername.setError("账号不能为空");
                }else {
                    mTILUsername.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void name(){
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    mTILUsername2.setError("姓名不能为空");
                }else {
                    mTILUsername2.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void id_number(){
        ID_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    mTILUsername3.setError("身份证不能为空");
                }else {
                    mTILUsername3.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void job_number(){
        job_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    mTILUsername4.setError("工号不能为空");
                }else {
                    mTILUsername4.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void phone_number(){
        phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    mTILUsername5.setError("手机号不能为空");
                }else {
                    mTILUsername5.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void password(){
        passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    mTILUsername6.setError("密码不能为空");
                }else {
                    mTILUsername6.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void passwordagain(){
        passWordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    mTILUsername7.setError("密码不能为空");
                }else {
                    mTILUsername7.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void register(){
        String url = getString(R.string.ip)+"user/useradd";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "{\n" +
                        "\t\"id\":\""+Id+"\",\n" +
                        "\t\"userid\":\""+UserName+"\",\n" +
                        "\t\"name\":\""+Name+"\",\n" +
                        "\t\"numberid\":\""+ID_Number+"\",\n" +
                        "\t\"number\":\""+Job_Number+"\",\n" +
                        "\t\"phone\":\""+Phone_Number+"\",\n" +
                        "\t\"password\":\""+PassWord+"\"\n" +
                        "}",
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String UserList;
                            UserList = response.getString("UserList");
                            if(UserList.equals("1")){
                                mTILUsername.setError("用户名已经存在，请重新输入");
                            }else{
                                Toast.makeText(RegisterFragment.this.getActivity(),"注册成功",Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley",error.toString());
                    }
                }
        );
        MyApplication.addRequest(jsonObjectRequest,"MainActivity");
    }
    public void acquire(){
        String url = getString(R.string.ip)+"user/userfindnumber?number="+Job_Number;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "",
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            String UserList;
                            UserList = response.getString("UserList");
                            if(UserList.equals("0")){
                                mTILUsername4.setError("工号不存在");
                            }else {
                                List<User> subjectList = gson.fromJson(response.getJSONArray("UserList").toString(),new TypeToken<List<User>>(){}.getType());
                                Id = subjectList.get(0).getId();
                                register();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley",error.toString());
                    }
                }
        );
        MyApplication.addRequest(jsonObjectRequest,"MainActivity");
    }
}
