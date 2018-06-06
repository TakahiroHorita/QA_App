package jp.techacademy.takahiro.horita.qa_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class QuestionDetailActivity extends AppCompatActivity {

    private ListView mListView;
    private Question mQuestion;
    private QuestionDetailListAdapter mAdapter;

    private ImageButton mFavoriteButton_ON;
    private ImageButton mFavoriteButton_OFF;

    private DatabaseReference mAnswerRef;

    private ChildEventListener mEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap map = (HashMap) dataSnapshot.getValue();

            String answerUid = dataSnapshot.getKey();

            for(Answer answer : mQuestion.getAnswers()) {
                // 同じAnswerUidのものが存在しているときは何もしない
                if (answerUid.equals(answer.getAnswerUid())) {
                    return;
                }
            }

            String body = (String) map.get("body");
            String name = (String) map.get("name");
            String uid = (String) map.get("uid");

            Answer answer = new Answer(body, name, uid, answerUid);
            mQuestion.getAnswers().add(answer);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        // 渡ってきたQuestionのオブジェクトを保持する
        Bundle extras = getIntent().getExtras();
        mQuestion = (Question) extras.get("question");

        setTitle(mQuestion.getTitle());

        // ListViewの準備
        mListView = findViewById(R.id.listView);
        mAdapter = new QuestionDetailListAdapter(this, mQuestion);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mFavoriteButton_ON = findViewById(R.id.FavoriteButton_ON);
        mFavoriteButton_OFF = findViewById(R.id.FavoriteButton_OFF);

        //Todo お気に入りに登録済みかどうかを確認する処理
        //Todo アイコンを変更する


        mFavoriteButton_ON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference dataBaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference answerRef = dataBaseReference.child(Const.FavoritesPATH).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Map<String, String> data = new HashMap<String, String>();

                data.put("qid", mQuestion.getQuestionUid());
                answerRef.push().setValue(data);

                Snackbar.make(view, "お気に入りに登録しました", Snackbar.LENGTH_LONG).show();

                mFavoriteButton_ON.setVisibility(View.GONE);
                mFavoriteButton_OFF.setVisibility(View.VISIBLE);

            }
        });

        mFavoriteButton_OFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference dataBaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference answerRef = dataBaseReference.child(Const.FavoritesPATH).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mQuestion.getQuestionUid());
                //answerRef.removeValue();
                answerRef.setValue(null);

                Snackbar.make(view, "お気に入りから削除しました", Snackbar.LENGTH_LONG).show();

                mFavoriteButton_ON.setVisibility(View.VISIBLE);
                mFavoriteButton_OFF.setVisibility(View.GONE);

            }
        });



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ログイン済みのユーザーを取得する
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user == null) {
                    // ログインしていなければログイン画面に遷移させる
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    // Questionを渡して回答作成画面を起動する
                    // --- ここから ---
                    Intent intent = new Intent(getApplicationContext(), AnswerSendActivity.class);
                    intent.putExtra("question", mQuestion);
                    startActivity(intent);
                    // --- ここまで ---
                }
            }
        });

        DatabaseReference dataBaseReference = FirebaseDatabase.getInstance().getReference();
        mAnswerRef = dataBaseReference.child(Const.ContentsPATH).child(String.valueOf(mQuestion.getGenre())).child(mQuestion.getQuestionUid()).child(Const.AnswersPATH);
        mAnswerRef.addChildEventListener(mEventListener);
    }

    protected void onResume() {
        super.onResume();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // ログインしていなければお気に入りボタンは表示しない
            mFavoriteButton_ON.setVisibility(View.INVISIBLE);
            mFavoriteButton_OFF.setVisibility(View.INVISIBLE);
        } else {
            // ログインしていればお気に入りボタンを表示する
            mFavoriteButton_ON.setVisibility(View.VISIBLE);
            mFavoriteButton_OFF.setVisibility(View.VISIBLE);
        }
    }
}