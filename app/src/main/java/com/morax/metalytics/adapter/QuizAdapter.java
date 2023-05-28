package com.morax.metalytics.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.morax.metalytics.R;
import com.morax.metalytics.activity.MainActivity;
import com.morax.metalytics.database.entity.Quiz;
import com.morax.metalytics.util.ImageHelper;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    private Context context;
    private List<Quiz> quizList;

    public QuizAdapter(Context context, List<Quiz> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.tvQuestion.setText(quiz.question);
        holder.rbOption1.setText(quiz.choices[0]);
        holder.rbOption2.setText(quiz.choices[1]);
        holder.rbOption3.setText(quiz.choices[2]);
        holder.rbOption4.setText(quiz.choices[3]);

        holder.rbOption1.setEnabled(true);
        holder.rbOption2.setEnabled(true);
        holder.rbOption3.setEnabled(true);
        holder.rbOption4.setEnabled(true);

        holder.ivImage.setImageBitmap(ImageHelper.convertByteArrayToBitmap(quiz.image));

        holder.rgChoices.setOnCheckedChangeListener(null); // Remove previous listener to avoid multiple callbacks
        holder.rgChoices.clearCheck(); // Clear the checked radio button

        holder.tvCorrectAnswer.setVisibility(View.GONE);
        holder.tvWrongAnswer.setVisibility(View.GONE);

        holder.rgChoices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedPosition = group.indexOfChild(group.findViewById(checkedId));
                if (checkedPosition == quiz.correctAnswer) {
                    holder.tvCorrectAnswer.setVisibility(View.VISIBLE);
                } else {
                    holder.tvWrongAnswer.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < group.getChildCount(); i++) {
                    group.getChildAt(i).setEnabled(false);
                }
            }
        });

        if (position == getItemCount() - 1) {
            holder.btnNext.setText("FINISHED");
            holder.btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            });
        } else {
            holder.btnNext.setText("Next");
            holder.btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        quizList.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return quizList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivImage;
        public TextView tvQuestion;
        public RadioGroup rgChoices;
        public RadioButton rbOption1;
        public RadioButton rbOption2;
        public RadioButton rbOption3;
        public RadioButton rbOption4;
        public TextView tvCorrectAnswer;
        public TextView tvWrongAnswer;
        public Button btnNext;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image_quiz);
            tvQuestion = itemView.findViewById(R.id.tv_question);
            rgChoices = itemView.findViewById(R.id.rg_choices);
            rbOption1 = itemView.findViewById(R.id.rb_option1);
            rbOption2 = itemView.findViewById(R.id.rb_option2);
            rbOption3 = itemView.findViewById(R.id.rb_option3);
            rbOption4 = itemView.findViewById(R.id.rb_option4);
            tvCorrectAnswer = itemView.findViewById(R.id.tv_correct_answer);
            tvWrongAnswer = itemView.findViewById(R.id.tv_wrong_answer);
            btnNext = itemView.findViewById(R.id.btn_next);
        }
    }
}
