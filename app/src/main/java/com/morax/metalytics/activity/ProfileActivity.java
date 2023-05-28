package com.morax.metalytics.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.morax.metalytics.R;
import com.morax.metalytics.adapter.PostAdapter;
import com.morax.metalytics.database.AppDatabase;
import com.morax.metalytics.database.dao.PostDao;
import com.morax.metalytics.database.dao.UserDao;
import com.morax.metalytics.database.entity.Post;
import com.morax.metalytics.database.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private UserDao userDao;
    private User user;
    private SharedPreferences userPrefs;

    private List<Post> postList;
    private PostDao postDao;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userDao = AppDatabase.getInstance(this).userDao();
        userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        user = userDao.getUserById(userPrefs.getLong("user_id", 0));

        TextView tvUsername = findViewById(R.id.tv_username_settings);
        TextView tvFullName = findViewById(R.id.tv_full_name);

        tvUsername.setText(user.username);
        String fullName = user.firstname + " " + user.lastname;
        tvFullName.setText(fullName);

        postDao = AppDatabase.getInstance(this).postDao();
        initData();

        RecyclerView recyclerView = findViewById(R.id.rv_post);
        postAdapter = new PostAdapter(this, postList, true);
        recyclerView.setAdapter(postAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Post post = postAdapter.getPostByPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this, R.style.AlertDialogCustom);
                builder.setCancelable(true);
                builder.setTitle("Are you sure?");
                builder.setMessage("You want to remove this post?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postDao.delete(post);
                                postList.remove(position);
                                postAdapter.notifyItemRemoved(position);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // retrieve the after if cancel
                        postAdapter.notifyDataSetChanged();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }).attachToRecyclerView(recyclerView);


    }

    public void initData() {
        postList = new ArrayList<>();
        try {
            postList.addAll(postDao.getPostsByUserId(user.id));
        } catch (NullPointerException ignored) {
        }
    }

    public void openEditProfile(View view) {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void changePassword(View view) {
        Intent intent = new Intent(this, ChangePassword.class);
        startActivity(intent);
    }
}