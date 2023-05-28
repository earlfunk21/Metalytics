package com.morax.metalytics.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.morax.metalytics.R;
import com.morax.metalytics.database.AppDatabase;
import com.morax.metalytics.database.dao.PostDao;
import com.morax.metalytics.database.dao.UserDao;
import com.morax.metalytics.database.entity.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> postList;
    private boolean modify = false;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    public PostAdapter(Context context, List<Post> postList, boolean modify) {
        this.context = context;
        this.postList = postList;
        this.modify = modify;
    }

    public Post getPostByPosition(int position) {
        return postList.get(position);
    }

    public void setPostList(List<Post> postList) {
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
        PostDao postDao = AppDatabase.getInstance(context).postDao();
        Post post = postList.get(position);
        UserDao userDao = AppDatabase.getInstance(context).userDao();
        holder.tvTitle.setText(post.title);
        holder.tvContent.setText(post.content);
        holder.tvUsername.setText(userDao.getUserById(post.userId).username);

        if (modify) {
            holder.cvPostItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Modal the post
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.popup_add_post, null);
                    dialogBuilder.setView(dialogView);
                    EditText etTitle = dialogView.findViewById(R.id.et_title_post);
                    EditText etContent = dialogView.findViewById(R.id.et_content_post);
                    etTitle.setText(post.title);
                    etContent.setText(post.content);
                    dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Retrieve the text from the EditText
                            String title = etTitle.getText().toString();
                            String content = etContent.getText().toString();
                            post.title = title;
                            post.content = content;
                            postDao.update(post);
                            holder.tvTitle.setText(title);
                            holder.tvContent.setText(content);
                        }
                    });
                    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                }
            });
        }
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
