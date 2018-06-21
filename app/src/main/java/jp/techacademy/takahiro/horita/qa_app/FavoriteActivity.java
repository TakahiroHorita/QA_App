package jp.techacademy.takahiro.horita.qa_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mDatabaseReference; //データベースへの読み書きに必要なクラス
    private DatabaseReference mFavoriteRef;
    private Map<String, String> mFavoriteMap;
    private ListView mListView;
    private ArrayList<Question> mQuestionArrayList;
    private QuestionsListAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_favorite);
        setTitle("お気に入り");
        mFavoriteMap = new HashMap<>();

        // ListViewの準備
        mListView = findViewById(R.id.listView);
        mAdapter = new QuestionsListAdapter(this);
        mQuestionArrayList = new ArrayList<Question>();
        mAdapter.notifyDataSetChanged();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFavoriteRef = mDatabaseReference.child(Const.FavoritesPATH).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mFavoriteRef.addChildEventListener(mEventListener);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Questionのインスタンスを渡して質問詳細画面を起動する
                Intent intent = new Intent(getApplicationContext(), QuestionDetailActivity.class);
                intent.putExtra("question", mQuestionArrayList.get(position));
                startActivity(intent);
            }
        });
    }

    private ChildEventListener mEventListener = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String quid = dataSnapshot.getKey();
            HashMap map = (HashMap) dataSnapshot.getValue();
            final String genre = (String) map.get("genre");

            mFavoriteMap.put(genre, quid);
            mDatabaseReference.child(Const.ContentsPATH).child(genre).child(dataSnapshot.getKey()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap map = (HashMap) dataSnapshot.getValue(); //一覧のデータを取得

                            String title = (String) map.get("title");
                            String body = (String) map.get("body");
                            String name = (String) map.get("name");
                            String uid = (String) map.get("uid");
                            String imageString = (String) map.get("image");
                            byte[] bytes;
                            if (imageString != null) {
                                bytes = Base64.decode(imageString, Base64.DEFAULT);
                            } else {
                                bytes = new byte[0];
                            }

                            ArrayList<Answer> answerArrayList = new ArrayList<Answer>();

                            HashMap answerMap = (HashMap) map.get("answers");
                            if (answerMap != null) {
                                for (Object key : answerMap.keySet()) {
                                    HashMap temp = (HashMap) answerMap.get( key);
                                    String answerBody = (String) temp.get("body");
                                    String answerName = (String) temp.get("name");
                                    String answerUid = (String) temp.get("uid");
                                    Answer answer = new Answer(answerBody, answerName, answerUid, (String) key);
                                    answerArrayList.add(answer);

                                }
                            }
                            Question question = new Question(title, body, name, uid, dataSnapshot.getKey(), Integer.parseInt(genre), bytes, answerArrayList);
                            mQuestionArrayList.add(question);
                            mAdapter.setQuestionArrayList(mQuestionArrayList);
                            mListView.setAdapter(mAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

}

