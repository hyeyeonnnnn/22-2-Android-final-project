package ddwu.mobile.finalproject.ma01_20200554;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    int start=1;
    //초기값 설정

    RecyclerView list;
    BlogAdapter adapter;
    Button result;

    String query="맛집";

    ArrayList<BlogDTO> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        list=findViewById(R.id.list);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        list.setLayoutManager(manager);



        array=new ArrayList<BlogDTO>();
        //MainActivity가 실행이 될때 NaverThread 실행
        new NaverThread().execute();

        final EditText edtsearch=findViewById(R.id.edtsearch);
        edtsearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                start =1;
                array.clear();
                query=edtsearch.getText().toString();
                //Toast.makeText(MainActivity.this,query,Toast.LENGTH_SHORT).show();
                new NaverThread().execute();
                return false;
            }
        });

//        result.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                start =1;
//                array.clear();
//                query=edtsearch.getText().toString();
//                new NaverThread().execute();
//            }
//        });

        FloatingActionButton btnmore=findViewById(R.id.btnmore);
        btnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start += 10;
                new NaverThread().execute();
            }
        });
    }
    //BackThread
    class NaverThread extends AsyncTask<String, String, String>{

        //doinbackground실행후 post로 감
        @Override
        protected String doInBackground(String... strings) {
            //결과값이 콘솔에만 찍힘->결과값을 받기위해 리턴
            //NaverAPI.main(query);

            //array생성

            return NaverAPI.main(query,start);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("결과:" +s);

            //결과를 하나씩 분석
            //데이터 만듦
            try {
                //items값을 가져온다.
                JSONArray jArray=new JSONObject(s).getJSONArray("items");
                for(int i=0; i<jArray.length(); i++){
                    //하나씩 하나씩 꺼내오는 것은 object
                    JSONObject obj=jArray.getJSONObject(i);
                    BlogDTO blog=new BlogDTO();
                    blog.setTitle(obj.getString("title"));
                    blog.setDescription(obj.getString("description"));
                    blog.setLink(obj.getString("link"));
                    array.add(blog);
                    System.out.println(blog.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter=new BlogAdapter(array,SearchActivity.this);
            list.setAdapter(adapter);
            list.scrollToPosition(start);
        }


    }



}