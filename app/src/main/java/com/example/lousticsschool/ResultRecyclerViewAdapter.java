package com.example.lousticsschool;

import static com.example.lousticsschool.UtilsMethods.calculateFromString;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lousticsschool.db.AppDb;
import com.example.lousticsschool.db.Quiz;

import java.util.ArrayList;
import java.util.List;

public class ResultRecyclerViewAdapter extends RecyclerView.Adapter<ResultRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Long> idList;
    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private List<Boolean> isCorrectArray;
    private List<Boolean> isCorrectedArray;
    private String exerciceType;
    private Context context;

    // data is passed into the constructor
    ResultRecyclerViewAdapter(Context context, List<String> data, List<Boolean> isCorrectArray, String exerciceType) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.isCorrectArray = isCorrectArray;
        this.context = context;
        this.exerciceType = exerciceType;
        // init isCorrectedArray with false
        isCorrectedArray = new ArrayList<>();
        for (int i = 0; i < isCorrectArray.size(); i++) {
            isCorrectedArray.add(false);
        }
    }

    public ResultRecyclerViewAdapter(ResultActivity context, ArrayList<String> recapList, ArrayList<Boolean> isCorrectArray, String exerciceType, ArrayList<Long> id) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = recapList;
        this.isCorrectArray = isCorrectArray;
        this.exerciceType = exerciceType;
        // init isCorrectedArray with false
        isCorrectedArray = new ArrayList<>();
        for (int i = 0; i < isCorrectArray.size(); i++) {
            isCorrectedArray.add(false);
        }
        this.idList = id;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.default_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String str = mData.get(position);
        holder.myTextView.setText(str);
        if (isCorrectArray.get(position) && !isCorrectedArray.get(position)) {
            holder.myTextView.setTextColor(Color.GREEN);
        } else if (isCorrectArray.get(position) && isCorrectedArray.get(position)) {
            holder.myTextView.setTextColor(Color.YELLOW);
        } else {
            holder.myTextView.setTextColor(Color.RED);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateData(ArrayList<String> strList) {
        mData = strList;
        notifyDataSetChanged();
    }




    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvResult);

            itemView.setOnLongClickListener(view -> {
                // on long click, change the string to the correct anwser
                switch (exerciceType) {
                    case "Math":
                        if (isCorrectArray.get(getAdapterPosition())) {
                            Toast.makeText(context, "Reponse juste", Toast.LENGTH_SHORT).show();
                        } else {
                            String[] split = mData.get(getAdapterPosition()).split("=");
                            // trim the spaces
                            split[0] = split[0].trim();
                            String correctAnswer = String.valueOf(calculateFromString(split[0]));
                            mData.set(getAdapterPosition(), split[0] + " = " + correctAnswer);
                            // set the text color to yellow
                            isCorrectArray.set(getAdapterPosition(), true);
                            isCorrectedArray.set(getAdapterPosition(), true);
                            notifyItemChanged(getAdapterPosition());
                        }
                        break;
                    case "Quiz":
                        if (isCorrectArray.get(getAdapterPosition())) {
                            Toast.makeText(context, "Reponse juste", Toast.LENGTH_SHORT).show();
                        } else {
                            quizAsyncMethods(idList.get(getLayoutPosition()), "correct");
                        }
                        break;
                }
                return true;
            });

            itemView.setOnClickListener(view -> {
                if (!isCorrectArray.get(getLayoutPosition())) {
                    switch (exerciceType) {
                        case "Math":
                            String[] split = mData.get(getLayoutPosition()).split("=");
                            split[0] = split[0].trim();

                            new AlertDialog.Builder(context)
                                    .setTitle("Essaye encore")
                                    .setMessage("La reponse correcte est :\n" + split[0] + " = " + calculateFromString(split[0]))
                                    .setPositiveButton("OK", null)
                                    .show();
                            break;
                        case "Quiz":
                            quizAsyncMethods(idList.get(getLayoutPosition()), "alert");
                    }
                }
            });


        }

        private void quizAsyncMethods(long id, String action) {

            class QuizAsyncMethods extends AsyncTask<Void, Void, Quiz> {

                @Override
                protected Quiz doInBackground(Void... voids) {
                    return AppDb.getInstance(context).quizDao().getQuiz(id);
                }

                @Override
                protected void onPostExecute(Quiz qz) {
                    super.onPostExecute(qz);
                    if (action.equals("alert")) {
                        new AlertDialog.Builder(context)
                                .setTitle("Essaye encore")
                                .setMessage(qz.getQuestion() + "\n\nLa bonne reponse est : " + qz.getCorrectAnswer() + "\n\n-- " + qz.getExplanation() + " --")
                                .setPositiveButton("OK", null)
                                .show();
                    } else if (action.equals("correct")) {
                        mData.set(getAdapterPosition(), qz.getCorrectAnswer());
                        isCorrectArray.set(getAdapterPosition(), true);
                        isCorrectedArray.set(getAdapterPosition(), true);
                        notifyItemChanged(getAdapterPosition());

                    }
                }
            }

            QuizAsyncMethods gu = new QuizAsyncMethods();
            gu.execute();
        }

    }




}