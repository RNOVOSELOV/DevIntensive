package com.softdesign.devintensive.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by roman on 16.07.16.
 */
public class RetainedFragment extends Fragment {

    private List<UserListRes.UserData> mData;
    private NetworkRequestListener mListener;

    public interface NetworkRequestListener {
        void onDataReceived(int responseCode, List<UserListRes.UserData> data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NetworkRequestListener) {
            mListener = (NetworkRequestListener) context;
        } else {
            throw new IllegalStateException("Parent activity must implement NetworkRequestListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void loadUsers() {
        Call<UserListRes> call = DataManager.getInstance().getUsersList();
        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == 200) {
                        mData = response.body().getData();
                        mListener.onDataReceived(response.code(), mData);
                    } else if (response.code() == 401) {
                        mListener.onDataReceived(response.code(), null);
                    } else {
                        mListener.onDataReceived(response.code(), null);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                try {
                    mListener.onDataReceived(0, null);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public List<UserListRes.UserData> getData() {
        return mData;
    }
}
