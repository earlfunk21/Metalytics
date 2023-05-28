package com.morax.metalytics.fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.morax.metalytics.R;
import com.morax.metalytics.adapter.PostAdapter;
import com.morax.metalytics.database.AppDatabase;
import com.morax.metalytics.database.dao.PostDao;
import com.morax.metalytics.database.entity.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    private List<Post> postList;
    private FloatingActionButton fabPost;
    private PostDao postDao;
    private PostAdapter postAdapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postDao = AppDatabase.getInstance(requireContext()).postDao();
        initData();

        RecyclerView recyclerView = view.findViewById(R.id.rv_post);
        postAdapter = new PostAdapter(requireContext(), postList);
        recyclerView.setAdapter(postAdapter);

        fabPost = view.findViewById(R.id.fab_post);
        fabPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.popup_add_post, null);
                dialogBuilder.setView(dialogView);
                EditText etTitle = dialogView.findViewById(R.id.et_title_post);
                EditText etContent = dialogView.findViewById(R.id.et_content_post);
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Retrieve the text from the EditText
                        String title = etTitle.getText().toString();
                        String content = etContent.getText().toString();
                        SharedPreferences userPrefs = requireContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
                        long user_id = userPrefs.getLong("user_id", 0);
                        Post post = new Post(title, content, user_id);
                        postDao.insert(post);
                        // Add the new post at position 0
                        postList.add(0, post);

                        // Notify the adapter about the new item at position 0
                        postAdapter.notifyItemInserted(0);

                        // Scroll the RecyclerView to the top
                        recyclerView.scrollToPosition(0);
                        createNotification();
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
    public void initData(){
        postList = new ArrayList<>();
        try {
            postList.addAll(postDao.getPosts());
        } catch (NullPointerException ignored){}
    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "Metalytics Notification Alert!", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "My Notification");
        builder.setContentTitle("Notification Alert!");
        builder.setContentText("Someone is posted!");
        builder.setSmallIcon(R.drawable.icon_person);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(requireContext());
        managerCompat.notify(1, builder.build());
    }
}