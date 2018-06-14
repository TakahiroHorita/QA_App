package jp.techacademy.takahiro.horita.qa_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DatabaseReference mDatabaseReference; //データベースへの読み書きに必要なクラス
    private DatabaseReference mFavoriteRef;
    private DatabaseReference mGenreRef;

    private LayoutInflater mLayoutInflater = null;
    private Question mQuestion;
    private Map<String, String> mFavoriteMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_favorite);
        mFavoriteMap = new HashMap<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mFavoriteRef = mDatabaseReference.child(Const.FavoritesPATH).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mFavoriteRef.addChildEventListener(mEventListener);

    }

    private ChildEventListener mEventListener = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            String quid = dataSnapshot.getKey();
            HashMap map = (HashMap) dataSnapshot.getValue();
            String genre = (String) map.get("genre");
            mFavoriteMap.put(quid, genre);

            mGenreRef = mDatabaseReference.child(Const.ContentsPATH).child(String.valueOf(genre));
            mGenreRef.addChildEventListener(mEventListener2); //child イベントのリスナーを追加する

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
            Log.d("ログ", "dataSnapshot : " + String.valueOf(dataSnapshot));
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

