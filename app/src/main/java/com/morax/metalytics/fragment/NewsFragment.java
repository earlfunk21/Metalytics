package com.morax.metalytics.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.morax.metalytics.R;
import com.morax.metalytics.adapter.NewsAdapter;
import com.morax.metalytics.database.entity.News;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NewsAdapter newsAdapter;
    private List<News> newsList;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        RecyclerView recyclerView = view.findViewById(R.id.rv_news);
        NewsAdapter newsAdapter = new NewsAdapter(requireContext(), newsList);
        recyclerView.setAdapter(newsAdapter);
    }

    private void initData() {
        newsList = new ArrayList<>();
        newsList.add(new News(getBytesByDrawable(R.drawable.image1),
                "Some LoL players are getting scammed by the shopkeeper with new Wardstone bug",
                "The item is also disabled due to an unrelated exploit.",
                "https://dotesports.com/league-of-legends/news/some-lol-players-are-getting-scammed-by-the-shopkeeper-with-new-wardstone-bug"));
        newsList.add(new News(getBytesByDrawable(R.drawable.image2),
                "Playoff rematches kick off LEC’s final split of 2023",
                "The battle for the crown begins soon.",
                "https://dotesports.com/league-of-legends/news/playoff-rematches-kick-off-lecs-final-split-of-2023"));
    }

    private byte[] getBytesByDrawable(int drawable) {
        Bitmap bmpDiamond = BitmapFactory.decodeResource(getResources(), drawable);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmpDiamond.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}