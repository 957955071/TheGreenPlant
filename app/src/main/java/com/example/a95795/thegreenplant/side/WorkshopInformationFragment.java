package com.example.a95795.thegreenplant.side;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.a95795.thegreenplant.R;
import com.example.a95795.thegreenplant.adapter.WorkshopAdapter;
import com.example.a95795.thegreenplant.custom.MyApplication;
import com.example.a95795.thegreenplant.custom.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;


public class WorkshopInformationFragment extends SupportFragment {


    private ListView listView;
    public static WorkshopInformationFragment newInstance() {
        return new WorkshopInformationFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workshop_information, container, false);



        String url = getString(R.string.ip) + "user/userall";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Gson gson = new Gson();
                            List<User> subjectList = gson.fromJson(response.getJSONArray("UserList").toString(),new TypeToken<List<User>>(){}.getType());
                            WorkshopAdapter workshopAdapter = new WorkshopAdapter(WorkshopInformationFragment.this.getActivity(), R.layout.workshop,subjectList);
                            listView = (ListView) getView().findViewById(R.id.workshoplist);
                            listView.setAdapter(workshopAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", error.toString());
                    }
                }
        );
        MyApplication.addRequest(jsonObjectRequest, "MainActivity");



        return view;

    }

}
