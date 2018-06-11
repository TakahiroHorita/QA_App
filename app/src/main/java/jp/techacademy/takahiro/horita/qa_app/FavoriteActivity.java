package jp.techacademy.takahiro.horita.qa_app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteActivity extends AppCompatActivity{

    private DatabaseReference mDatabaseReference; //データベースへの読み書きに必要なクラス
    private DatabaseReference mFavoriteRef; //データベースへの読み書きに必要なクラス

    private LayoutInflater mLayoutInflater = null;
    private Question mQuestion;

/*
    private ChildEventListener mEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap map = (HashMap) dataSnapshot.getValue();
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

//            Question question = new Question(title, body, name, uid, dataSnapshot.getKey(), mGenre, bytes, answerArrayList);
//            mQuestionArrayList.add(question);
//            mAdapter.notifyDataSetChanged();
        }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_favorite);

        // 渡ってきたQuestionのオブジェクトを保持する
//        Bundle extras = getIntent().getExtras();
//        mQuestion = (Question) extras.get("question");

//        setTitle(mQuestion.getTitle());



        DatabaseReference dataBaseReference = FirebaseDatabase.getInstance().getReference();
        mFavoriteRef = dataBaseReference.child(Const.FavoritesPATH).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mFavoriteRef.addChildEventListener(mEventListener);
        Log.d("ログ2", String.valueOf(mFavoriteRef));


        /*
        Favorite

        HashMap<String, String> mFaroviteMap

        Key Value
        質問ＩＤ　ジャンル
        Ａ　　　　１
        Ｄ　　　　３
        Ｇ　　　　２

        mFariteMap.put(質問のＩＤ、ジャンル)

        ジャンル　質問ＩＤ


                Contents


        for(int i=1 i<=4 i++);

        MGenreRef=
                mGenreRef.addEventLisetener

        if(mFariteMap.conteinsKey(dataSnapshot.getKey()){
        }
  */
}

