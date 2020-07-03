package com.icandothisallday2020.ex82retrofitfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<BoardItem> boardItems;


    public BoardAdapter(Context context, ArrayList<BoardItem> boardItems) {
        this.context = context;
        this.boardItems = boardItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.board_item,parent,false);
        VH holder=new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh=(VH)holder;
        BoardItem item=boardItems.get(position);

        //DB 안에는 업로드된 파일의 서버내부 경로만 존재(실제 이미지 경로X)
        //Glide 에게 서버 주소를 포함한 풀서버 url 을 전달해야 함
        String imgUrl="http://soon0.dothome.co.kr/Retrofit/"+item.file;
        Glide.with(context).load(imgUrl).into(vh.iv);

        vh.tvTitle.setText(item.title);
        vh.tvMsg.setText(item.msg);
        vh.tvPrice.setText(item.price+"$");
        vh.tbMoney.setChecked(item.favor==1? true: false);//토글버튼의 체크상태 지정




    }

    @Override
    public int getItemCount() {
        return boardItems.size();
    }

    //inner
    class VH extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tvTitle,tvMsg,tvPrice;
        ToggleButton tbMoney;

        public VH(@NonNull View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.iv);
            tvTitle=itemView.findViewById(R.id.tv_title);
            tvMsg=itemView.findViewById(R.id.tv_msg);
            tvPrice=itemView.findViewById(R.id.tv_price);
            tbMoney=itemView.findViewById(R.id.tb_money);

            //좋아요 토글버튼 선택 리스너
            tbMoney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //바꿔야 할 데이터는 'favor'뿐이지만 확장성을 위해서(나중에)
                    BoardItem item=boardItems.get(getLayoutPosition());//현재 누른 아이템 항목 얻어오기
                    item.favor=isChecked?1:0;//삼항연산자 -true 면 1 :false 면 0

                    RetrofitService retrofitService=RetrofitHelper.getInstanceFromGson().create(RetrofitService.class);
                    Call<BoardItem> call =retrofitService.updateData("updateFavor.php",item);
                    call.enqueue(new Callback<BoardItem>() {
                        @Override
                        public void onResponse(Call<BoardItem> call, Response<BoardItem> response) {
                            Toast.makeText(context, "RESPOND", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Call<BoardItem> call, Throwable t) {
                            Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
        }
    }
}
