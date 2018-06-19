package jp.techacademy.takahiro.horita.qa_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DatabaseReference mDatabaseReference; //データベースへの読み書きに必要なクラス
    private DatabaseReference mFavoriteRef;
    private DatabaseReference mGenreRef;

    private Map<String, String> mFavoriteMap;

    private int mGenre = 0;

    private ListView mListView;
    private ArrayList<Question> mQuestionArrayList;
    private QuestionsListAdapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_favorite);
        mFavoriteMap = new HashMap<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mFavoriteRef = mDatabaseReference.child(Const.FavoritesPATH).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mFavoriteRef.addChildEventListener(mEventListener);

        // ListViewの準備
        mListView = findViewById(R.id.listView);
        mAdapter = new QuestionsListAdapter(this);
        mQuestionArrayList = new ArrayList<Question>();
        mAdapter.notifyDataSetChanged();

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
            String genre = (String) map.get("genre");
            mFavoriteMap.put(genre, quid);
            Log.d("mFavoriteMap", String.valueOf(mFavoriteMap));

            mGenreRef = mDatabaseReference.child(Const.ContentsPATH).child(genre);
//            mGenreRef = mDatabaseReference.child(Const.ContentsPATH).child(genre).child(quid);
            mGenreRef.addChildEventListener(mEventListener2);


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
            Log.d("ログ", "onCancelled");
        }
    };

    private ChildEventListener mEventListener2 = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            HashMap map = (HashMap) dataSnapshot.getValue();

            if (mFavoriteMap.containsValue(key)) {
                Log.d("ログ", "一緒です。");
                Log.d("ログ","mQuid :" + mFavoriteMap);
                Log.d("ログ","key :" + key);
            } else {
                Log.d("ログ", "違います。");
                Log.d("ログ","mQuid :" + mFavoriteMap);
                Log.d("ログ","key :" + key);
            }

            //            Log.d("ログ", "map :" + String.valueOf(map));
//            Log.d("ログ", "dataSnapshot :" + String.valueOf(dataSnapshot));
            Log.d("ログ","map :" + map);

//            HashMap map = (HashMap) dataSnapshot.getValue();
//            String title = (String) map.get("title");
//            Log.d("title", String.valueOf(title));
//            HashMap map = (HashMap) dataSnapshot.getValue();
//            Log.d("map", String.valueOf(map));
            /*
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
                    Log.d("Answer",String.valueOf(answer));
                    answerArrayList.add(answer);
                }
            }

            Question question = new Question(title, body, name, uid, dataSnapshot.getKey(), mGenre, bytes, answerArrayList);
            Log.d("Question",String.valueOf(question));
            mQuestionArrayList.add(question);
            mAdapter.notifyDataSetChanged();
*/
/*
            if(mFavoriteMap.containsKey(quid)){
                Log.d("イコール", quid);
            }
*/
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
            Log.d("ログ", "onCancelled");
        }
    };
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }



}

