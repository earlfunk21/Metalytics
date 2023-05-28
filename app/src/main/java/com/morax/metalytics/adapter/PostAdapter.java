package com.morax.metalytics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.morax.metalytics.R;
import com.morax.metalytics.database.AppDatabase;
import com.morax.metalytics.database.dao.UserDao;
import com.morax.metalytics.database.entity.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    public void setPostList(List<Post> postList){
        this.postList = postList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        UserDao userDao = AppDatabase.getInstance(context).userDao();
        holder.tvTitle.setText(post.title);
        holder.tvContent.setText(post.content);
        holder.tvUsername.setText(userDao.getUserById(post.userId).username);

        holder.cvPostItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Modal the post
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvContent, tvUsername;
        public CardView cvPostItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_post);
            tvContent = itemView.findViewById(R.id.tv_content_post);
            cvPostItem = itemView.findViewById(R.id.cv_post_item);
            tvUsername = itemView.findViewById(R.id.tv_username_post);
        }
    }
}
