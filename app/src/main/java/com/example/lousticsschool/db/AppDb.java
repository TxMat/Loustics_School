package com.example.lousticsschool.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.lousticsschool.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

@Database(entities = {User.class, Quiz.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDb extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract QuizDao quizDao();

    private static AppDb INSTANCE;

    private static Context ctx;

    public static synchronized AppDb getInstance(Context context) {

        ctx = context.getApplicationContext();

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                    AppDb.class, "LSDb")
                    .addCallback(roomCallback)
                    .build();
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private QuizDao quizDao;
        private PopulateDbAsyncTask(AppDb db) {
            quizDao = db.quizDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            fillWithStartingData(ctx);
            return null;
        }

    }


    private static void fillWithStartingData(Context context) {
        QuizDao quizDao = getInstance(context).quizDao();
        JSONArray jsonArray = loadJSONArray(context);

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String question = jsonObject.getString("question");
                String answer = jsonObject.getString("réponse");
                //remove " and [ and ] from the string
                String prop = jsonObject.getString("propositions")
                        .replace("[", "")
                        .replace("]", "")
                        .replace("\"", "");
                String[] choices = prop.split(",");
                String explanation = jsonObject.getString("anecdote");
                System.out.println(Arrays.toString(choices));

                Quiz quiz = new Quiz();
                quiz.setQuestion(question);
                quiz.setCorrectAnswer(answer);
                quiz.setAnswers(choices);
                quiz.setExplanation(explanation);
                System.out.println(quiz);

                quizDao.insert(quiz);






            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // load data from raw/questionsData.json to the database
    private static JSONArray loadJSONArray(Context context) {
        StringBuilder sb = new StringBuilder();
        InputStream is = context.getResources().openRawResource(R.raw.questiondata);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            return jsonObject.getJSONArray("quizz");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
