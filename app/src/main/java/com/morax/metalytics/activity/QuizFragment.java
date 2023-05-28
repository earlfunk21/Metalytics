package com.morax.metalytics.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.morax.metalytics.R;
import com.morax.metalytics.adapter.QuizAdapter;
import com.morax.metalytics.database.entity.Quiz;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuizFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizFragment newInstance(String param1, String param2) {
        QuizFragment fragment = new QuizFragment();
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
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Quiz> quizList = new ArrayList<>();

        quizList.add(new Quiz("Who is this champion?",
                new String[]{"Thresh", "Veigar", "Gragas", "Teemo"}, 0, getBytesByDrawable(R.drawable.thresh)));
        quizList.add(new Quiz("What is the name of this item?",
                new String[]{"Doran's Ring", "Dark Seal", "Mejai's SoulStealer", "Teemo"}, 1, getBytesByDrawable(R.drawable.dark_seal)));

        quizList.add(new Quiz("What is the name of this item?",
                new String[]{"Infinity Age", "Bloodthirster", "Lich bane", "Rabadon's DeathCap"}, 1, getBytesByDrawable(R.drawable.bloodthirster)));
        quizList.add(new Quiz("What is the name of this item?",
                new String[]{"Bloodthirster", "Infinity Age", "Lich bane", "Rabadon's DeathCap"}, 0, getBytesByDrawable(R.drawable.bloodthirster)));

        RecyclerView rvQuiz = view.findViewById(R.id.rv_quiz);
        QuizAdapter quizAdapter = new QuizAdapter(requireContext(), quizList);
        rvQuiz.setAdapter(quizAdapter);
    }

    private byte[] getBytesByDrawable(int drawable) {
        Bitmap bmpDiamond = BitmapFactory.decodeResource(getResources(), drawable);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmpDiamond.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}