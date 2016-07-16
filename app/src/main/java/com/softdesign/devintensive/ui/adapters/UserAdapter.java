package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by roman on 15.07.16.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    Context mContext;
    List<UserListRes.UserData> mUsers;
    UserViewHolder.CustomClickListener mCustomClickListener;

    public UserAdapter(Context mContext, List<UserListRes.UserData> mUsers, UserViewHolder.CustomClickListener customClickListener) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.mCustomClickListener = customClickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user_list, parent, false);
        return new UserViewHolder(convertView, mCustomClickListener);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserListRes.UserData user = mUsers.get(position);
        Picasso.with(mContext)
                .load(user.getPublicInfo().getPhoto())
                .placeholder(ContextCompat.getDrawable(mContext, R.drawable.user_bg))
                .error(ContextCompat.getDrawable(mContext, R.drawable.user_bg))
                .fit()
                .into(holder.mUserPhoto);
        holder.mFullName.setText(user.getName());
        holder.mRating.setText(String.valueOf(user.getProfileValues().getRait()));
        holder.mCodeLines.setText(String.valueOf(user.getProfileValues().getLinesCode()));
        holder.mProjects.setText(String.valueOf(user.getProfileValues().getProjects()));

        if (user.getPublicInfo().getBio() == null || user.getPublicInfo().getBio().isEmpty()) {
            holder.mBio.setVisibility(View.GONE);
        } else {
            holder.mBio.setText(user.getPublicInfo().getBio());
            holder.mBio.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected AspectRatioImageView mUserPhoto;
        protected TextView mFullName;
        protected TextView mRating;
        protected TextView mCodeLines;
        protected TextView mProjects;
        protected TextView mBio;
        protected AspectRatioImageView mImage;

        protected Button mShowMore;
        private CustomClickListener mListener;

        public UserViewHolder(View itemView, CustomClickListener customClickListener) {
            super(itemView);

            mListener = customClickListener;

            mImage = ((AspectRatioImageView) itemView.findViewById(R.id.item_image));
            mShowMore = ((Button) itemView.findViewById(R.id.item_user_info_btn));
            mUserPhoto = (AspectRatioImageView) itemView.findViewById(R.id.item_image);

            mFullName = (TextView) itemView.findViewById(R.id.item_full_name);
            mRating = (TextView) itemView.findViewById(R.id.item_user_rating);
            mCodeLines = (TextView) itemView.findViewById(R.id.item_user_code_lines);
            mProjects = (TextView) itemView.findViewById(R.id.item_user_projects);
            mBio = (TextView) itemView.findViewById(R.id.item_user_bio);

            mShowMore.setOnClickListener(this);
            mImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onUserItemClickListener(getAdapterPosition());
            }
        }

        public interface CustomClickListener {
            void onUserItemClickListener(int position);
        }
    }
}
