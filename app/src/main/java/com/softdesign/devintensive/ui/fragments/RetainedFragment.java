package com.softdesign.devintensive.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.softdesign.devintensive.data.network.res.UserListRes;

import java.util.List;

/**
 * Created by roman on 16.07.16.
 */
public class RetainedFragment extends Fragment {

    private List<UserListRes.UserData> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setData(List<UserListRes.UserData> teamList) {
        data = teamList;
    }

    public List<UserListRes.UserData> getData () {
        return data;
    }
}
