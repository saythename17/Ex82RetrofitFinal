package com.icandothisallday2020.ex82retrofitfinal;

//서버에서 읽어온 게시글(market) 테이블의 한 record(row)의 데이터를 저장하는 VO(Value Object) class
public class BoardItem {
    int no;//저장된 아이템 번호
    String name,title,msg,price,file,date;
    int favor; // 좋아요 여부 [mySQL 에서는 true, false 를 1,0로 대체해서 저장]

    public BoardItem() {
    }

    public BoardItem(int no, String name, String title, String msg, String price, String file, String date, int favor) {
        this.no = no;
        this.name = name;
        this.title = title;
        this.msg = msg;
        this.price = price;
        this.file = file;
        this.date = date;
        this.favor = favor;
    }
}
