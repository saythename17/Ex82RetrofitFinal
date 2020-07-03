package com.icandothisallday2020.ex82retrofitfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<BoardItem> boardItems=new ArrayList<>();
    BoardAdapter adapter;

    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recycler);
        adapter=new BoardAdapter(this,boardItems);
        recyclerView.setAdapter(adapter);

        refreshLayout=findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //리사이클러뷰에 보여줄 데이터들을 리로딩
                loadData();

                //리프레시 아이콘 숨기기
                refreshLayout.setRefreshing(false);
            }
        });
    }

    //액티비티가 화면에 보여질 때 자동실행되는 LifeCycle method [onCreate,onStart,onResume,onPause,onDestroyed...]
    @Override
    protected void onResume() {
        super.onResume();
        //서버 데이터 읽어오기
        loadData();
    }

    //서버에서 데이터를 불러들이는 작업메소드
    void loadData(){

        //레트로핏으로 읽기
        Retrofit retrofit=RetrofitHelper.getInstanceFromGson();
        RetrofitService retrofitService=retrofit.create(RetrofitService.class);
        Call<ArrayList<BoardItem>> call=retrofitService.loadDataFromBoard();

        call.enqueue(new Callback<ArrayList<BoardItem>>() {
            @Override
            public void onResponse(Call<ArrayList<BoardItem>> call, Response<ArrayList<BoardItem>> response) {
                if(response.isSuccessful()){
                    //서버데이터를 읽어와 새로운 List 객체를 얻어옴
                    ArrayList<BoardItem> items=response.body();

                    //새로운 아답터를 만드는 것은 오래 걸리므로 기존 아답터가 보여주던 boardItems 리스트객체의 값들을 갱신
                    boardItems.clear();//(기존리스트 모두 삭제)
                    adapter.notifyDataSetChanged();//※※변경사항이 있다면 반드시 아답터에게 알려야 함

                    //서버에서 읽어온 items ->boardItems 에 추가
                    for(BoardItem item:items){
                        boardItems.add(0,item);//0번째(맨 앞)에 제일 최신의 데이터가 저장되도록
                        adapter.notifyItemInserted(0);//0번째(맨 앞)에 있는 데이터만 추가 ,갱신 하므로 보다 빠른 작업 가능
                    }


                    //리사이클러에서 보여줘야 하므로-위에서 얻어온 리스트로 새로운 아답터 객체 생성
//                    adapter=new BoardAdapter(MainActivity.this,items);
//                    recyclerView.setAdapter(adapter);//새로운 아답터 set : 바꿔치기(다 뿌셨다 다시넣는것) - 데이터 양이 많을 땐 비효율적
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BoardItem>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void clickEdit(View view) {
        Intent intent=new Intent(this ,EditActivity.class);
        startActivity(intent);
    }
}
