package com.example.gutfoodwit.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gutfoodwit.Dao.ShopCarDao;
import com.example.gutfoodwit.Dao.ShopDetailDao;
import com.example.gutfoodwit.Dao.ShopHeaderDao;
import com.example.gutfoodwit.Dao.UserCollectDao;
import com.example.gutfoodwit.Dao.UserTrackDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.bean.CarCommodities;
import com.example.gutfoodwit.bean.CommodityInfo;
import com.example.gutfoodwit.bean.LinkBean;
import com.example.gutfoodwit.bean.ShopHeader;
import com.example.gutfoodwit.recycleview.BaseRecyclerHolder;
import com.example.gutfoodwit.recycleview.BaseRecyclerViewAdater;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShopDetailActivity extends AppCompatActivity implements View.OnClickListener{
    int id;//店铺id
    String user_tel;//用户电话

    int i = 1;
    ImageView iv_head_icon;//店铺头部图片路径
    TextView tv_shopname;//店铺名
    TextView tv_star_size;//评分
    TextView tv_monthly_sales;//月售
    ImageView iv_shop_icon;//店铺图片路径
    TextView tv_shop_describe;//公告（店铺描述）

    private TextView shopdetail_order;
    private TextView shopdetail_evaluate;

    private RecyclerView rvL, rvR;
    private TextView tv_head;
    private LAdapter lAdapter;
    private RAdapter rAdapter;
    private boolean moveToTop = false;
    private int index;

    private LinkBean linkBean;

    private Map<String, Integer> map;
    private Set<String> keys;

    private ShopHeader shopHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(this);
//        设置透明状态栏
        setStatusBar();
        setContentView(R.layout.activity_shopdetail);

        id=getIntent().getExtras().getInt("id");
        user_tel = getIntent().getExtras().getString("userTel");



        iv_head_icon=findViewById(R.id.iv_head_icon);
        tv_shopname=findViewById(R.id.tv_shopname);
        tv_star_size=findViewById(R.id.tv_star_size);
        tv_monthly_sales=findViewById(R.id.tv_monthly_sales);
        iv_shop_icon=findViewById(R.id.iv_shop_icon);
        tv_shop_describe=findViewById(R.id.tv_shop_describe);
//        初始化商品数据
        initData();
        initShopDetailHead();//初始化店铺相关信息
//        初始化组件
        initComponent();
//        插入足迹库
        insertToTrack();
        showCount();
    }

    private void insertToTrack(){
        new Thread(() -> {
            new UserTrackDao().insertTrack(id,user_tel);
        }).start();
    }

    @Override
    protected void onResume(){
        super.onResume();
        i = 1;
        initData();
        showCount();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    LinkBean.ItemL itemL = new LinkBean.ItemL();
                    itemL.setTitle(String.valueOf(msg.obj));
                    linkBean.itemLS.add(itemL);
                    break;
                case 1:
                    List<CommodityInfo> shopDetail = (List<CommodityInfo>) msg.obj;
                    for (int j = 0; j < msg.arg1; j++) {
                        LinkBean.Item item = new LinkBean.Item();
                        item.setId(shopDetail.get(j).getCommodity_id());
                        item.setTitle(shopDetail.get(j).getCommodity_type());
                        item.setName(shopDetail.get(j).getCommodity_name());
                        item.setPrice(String.valueOf(shopDetail.get(j).getCommodity_price()));
                        String content = shopDetail.get(j).getCommodity_content();
                        if(content!=null){
                            if(content.length() >= 10){
                                item.setContent(content.substring(0,10) + "...");
                            } else {
                                item.setContent(content);
                            }
                        }
                        item.setImg(shopDetail.get(j).getCommodity_img());
                        linkBean.itemS.add(item);
                    }
                    break;
                case 2:
                    initView();
                    break;
                case 3:
                    initListener();
                    break;
                case 4:
//                    Log.e("Header", sh.getIv_head_icon()+sh.getTv_shopname()+sh.getTv_star_size()+sh.getTv_monthly_sales()+sh.getIv_shop_icon()+sh.getTv_shop_describe());
                    iv_head_icon.setImageResource(getImageID(shopHeader.getIv_head_icon()));
                    tv_shopname.setText(shopHeader.getTv_shopname());
                    tv_star_size.setText(shopHeader.getTv_star_size());
                    tv_monthly_sales.setText(shopHeader.getTv_monthly_sales()+"");
                    iv_shop_icon.setImageResource(getImageID(shopHeader.getIv_shop_icon()));
                    tv_shop_describe.setText(shopHeader.getTv_shop_describe());
                    break;
                case 5:
                    ImageView img = findViewById(R.id.select);
                    if(msg.arg1 == 1){
//                        收藏了
                        img.setImageDrawable(getResources().getDrawable(R.drawable.collect_select));
                    } else {
//                        未收藏
                        img.setImageDrawable(getResources().getDrawable(R.drawable.collect_unselect));
                    }
                    break;
                case 6:
                    img = findViewById(R.id.select);
                    if(msg.arg1 == 1){
//                        取消收藏
                        img.setImageDrawable(getResources().getDrawable(R.drawable.collect_unselect));
                        Toast.makeText(ShopDetailActivity.this,"取消收藏成功",Toast.LENGTH_SHORT).show();
                    } else {
//                        加入收藏
                        img.setImageDrawable(getResources().getDrawable(R.drawable.collect_select));
                        Toast.makeText(ShopDetailActivity.this,"加入收藏成功",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    //初始化店铺页面头部信息
    protected void initShopDetailHead(){
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {

                shopHeader=ShopHeaderDao.getShopHeaderByid(id);
                Message messageHeader=handler.obtainMessage();
                messageHeader.what=4;
                handler.sendMessage(messageHeader);
            }
        }).start();

    }

    protected void initData(){
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        Log.e("i", String.valueOf(i));
        new Thread(new Runnable() {
        @Override
        public void run() {
            linkBean = new LinkBean();
            linkBean.itemLS = new ArrayList<>();
            linkBean.itemS = new ArrayList<>();
            map = new ShopDetailDao().getShopMap(id);
            keys = map.keySet();
            for (String key : keys) {
                Message msg = handler.obtainMessage();
                msg.what = 0;
                msg.obj = key;
                handler.sendMessage(msg);

                List<CommodityInfo> shopDetail = new ShopDetailDao().getShopDetail(key,id);
                Message msg2 = handler.obtainMessage();
                msg2.what = 1;
                msg2.obj = shopDetail;
                msg2.arg1 = map.get(key);
                handler.sendMessage(msg2);
            }
            Message msg3 = handler.obtainMessage();
            msg3.what = 2;
            handler.sendMessage(msg3);
            Message msg4 = handler.obtainMessage();
            msg4.what = 3;
            handler.sendMessage(msg4);


            Message msg5 = handler.obtainMessage();
            boolean judgeCollect = new UserCollectDao().judgeCollect(id, user_tel);
            if(judgeCollect){
                msg5.arg1 = 1;
            } else {
                msg5.arg1 = 0;
            }
            msg5.what = 5;
            handler.sendMessage(msg5);

            }
        }).start();
    }

    private void initComponent(){
//        findViewById(R.id.shopdetail_head).bringToFront();
//        获得“点菜”按钮，并设置点击监听
        shopdetail_order = findViewById(R.id.shopdetail_order);
        shopdetail_order.setOnClickListener(this);
//        获得“评价”按钮，并设置监听
        shopdetail_evaluate = findViewById(R.id.shopdetail_evaluate);
        shopdetail_evaluate.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.select).setOnClickListener(this);
        findViewById(R.id.shopCart).setOnClickListener(this);
    }

    private void initView() {
        tv_head = findViewById(R.id.tv_header);
        tv_head.setText(linkBean.itemLS.get(0).getTitle());
        rvL = findViewById(R.id.rv1);
        rvR = findViewById(R.id.rv2);
        rvL.setLayoutManager(new LinearLayoutManager(this));
        rvR.setLayoutManager(new LinearLayoutManager(this));
        lAdapter = new LAdapter(this,R.layout.item,linkBean.itemLS);
        lAdapter.bindToRecyclerView(rvL);
        rvL.setAdapter(lAdapter);
        rAdapter = new RAdapter(this,R.layout.item_goods,linkBean.itemS);
        rvR.setAdapter(rAdapter);
    }


    private void initListener() {
        lAdapter.setOnItemClickListener(new BaseRecyclerViewAdater.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (rvR.getScrollState() != RecyclerView.SCROLL_STATE_IDLE)return;
                lAdapter.fromClick = true;
                lAdapter.setChecked(position);
                String tag = lAdapter.getmData().get(position).getTitle();
                for (int i = 0; i < rAdapter.getmData().size(); i++) {
                    //根据左边选中的条目获取到右面此条目Title相同的位置索引；
                    if (TextUtils.equals(tag,rAdapter.getmData().get(i).getTitle())){
                        index = i;
                        moveToPosition_R(index);
                        return;
                    }
                }
            }
        });

        rvR.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) rvR.getLayoutManager();
                if (moveToTop){ //向下滑动时，只会把改条目显示出来；我们还需要让该条目滑动到顶部；
                    moveToTop = false;
                    int m = index - layoutManager.findFirstVisibleItemPosition();
                    if (m >= 0 && m <= layoutManager.getChildCount()){
                        int top = layoutManager.getChildAt(m).getTop();
                        rvR.smoothScrollBy(0,top);
                    }
                }else {
                    int index = layoutManager.findFirstVisibleItemPosition();
                    tv_head.setText(rAdapter.getmData().get(index).getTitle());
                    lAdapter.setToPosition(rAdapter.getmData().get(index).getTitle());
                }
            }
        });

        rvR.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                lAdapter.fromClick = false;
                return false;
            }
        });
    }

    private void moveToPosition_R(int index) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rvR.getLayoutManager();
        int f = layoutManager.findFirstVisibleItemPosition();
        int l = layoutManager.findLastVisibleItemPosition();
        if (index <= f){ //向上移动时
            layoutManager.scrollToPosition(index);
        }else if (index <= l){ //已经再屏幕上面显示时
            int m = index - f;
            if (0 <= m && m <= layoutManager.getChildCount()) {
                int top = layoutManager.getChildAt(m).getTop();
                rvR.smoothScrollBy(0, top);
            }
        }else { //向下移动时
            moveToTop = true;
            layoutManager.scrollToPosition(index);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shopdetail_order:
                shopdetail_order.setTextColor(Color.BLACK);
                shopdetail_evaluate.setTextColor(Color.argb(100,181,176,176));
                findViewById(R.id.order_underline).setBackground(getResources().getDrawable(R.drawable.shopdetail_choose));
                findViewById(R.id.order_evaluate).setBackground(getResources().getDrawable(R.drawable.shopdetail_unselect));
                break;
            case R.id.shopdetail_evaluate:
                shopdetail_evaluate.setTextColor(Color.BLACK);
                shopdetail_order.setTextColor(Color.argb(100,181,176,176));
                findViewById(R.id.order_evaluate).setBackground(getResources().getDrawable(R.drawable.shopdetail_choose));
                findViewById(R.id.order_underline).setBackground(getResources().getDrawable(R.drawable.shopdetail_unselect));
                break;
            case R.id.btn_back:
                ShopDetailActivity.this.finish();
                break;
            case R.id.select:
                updateSelectButton();
                break;
            case R.id.shopCart:
                // 创建一个意图对象，准备跳到指定的活动页面
                Intent intent=new Intent(ShopDetailActivity.this,ShopCarActivity.class);
                intent.putExtra("userTel",user_tel);//
                // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent); // 跳转到意图对象指定的活动页面
                break;
        }
    }

    /**
     * 更新收藏按钮
     * */
    private void updateSelectButton(){
        new Thread(() -> {
            boolean flag = new UserCollectDao().judgeCollect(id, user_tel);
            Message msg = handler.obtainMessage();
            msg.what = 6;
            if(flag == true){
//                收藏了，则取消收藏
                new UserCollectDao().deleteCollectShop(user_tel, id);
                msg.arg1 = 1;
            } else {
//                未收藏，则加入收藏
                new UserCollectDao().insertCollectShop(user_tel,id);
                msg.arg1 = 2;
            }
            handler.sendMessage(msg);
        }).start();
    }

    /**
     *  开启本地读写权限
     *  */
    private void checkPermission(Activity activity) {//开启本地的照片读取与写入权限
        // Storage Permissions
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,//读内存权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE};//写内存权限
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(ShopDetailActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(ShopDetailActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if(ContextCompat.checkSelfPermission(ShopDetailActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ShopDetailActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置透明状态栏
     * */
    private void setStatusBar(){
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }


//    左右适配器
    class LAdapter extends BaseRecyclerViewAdater<LinkBean.ItemL> {
        public LAdapter(Context context, int resLayout, List<LinkBean.ItemL> data) {
            super(context, resLayout, data);
        }

        @Override
        public void convert(BaseRecyclerHolder holder, final int position) {
            TextView tv = ((TextView)holder.getView(R.id.tv));
            tv.setText(getmData().get(position).getTitle());
            if (checked == position){
                tv.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                tv.setBackgroundResource(R.color.colorfff);
            }else {
                tv.setTextColor(ContextCompat.getColor(context,R.color.color666));
                tv.setBackgroundResource(R.color.color16333333);
            }

        }

        private int checked; //当前选中项
        public boolean fromClick; //是否是自己点击的

        public void setChecked(int checked) {
            this.checked = checked;
            notifyDataSetChanged();
        }

        //让左边的额条目选中
        public void setToPosition(String title){
            if (fromClick)return;
            if (TextUtils.equals(title,getmData().get(checked).getTitle()))return;
            if (TextUtils.isEmpty(title))return;
            for (int i = 0; i < getmData().size(); i++) {
                if (TextUtils.equals(getmData().get(i).getTitle(),title)){
                    setChecked(i);
                    moveToPosition(i);
                    return;
                }
            }

        }

        private void moveToPosition(int index){
            //如果选中的条目不在显示范围内，要滑动条目让该条目显示出来
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getRecyclerView().getLayoutManager();
            int f = linearLayoutManager.findFirstVisibleItemPosition();
            int l = linearLayoutManager.findLastVisibleItemPosition();
            if (index<=f || index >= l){
                linearLayoutManager.scrollToPosition(index);
            }

        }

    }

    class RAdapter extends BaseRecyclerViewAdater<LinkBean.Item>{
        public RAdapter(Context context, int resLayout, List<LinkBean.Item> data) {
            super(context, resLayout, data);
        }


        private void setCount(int commodityId,BaseRecyclerHolder holder) {
            new Thread(() -> {
                int count = new ShopCarDao().getCount(user_tel, id, commodityId);
                Log.e("count", String.valueOf(count));
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.arg1 = count;
                msg.obj = holder;
                handler.sendMessage(msg);
            }).start();
        }

        Handler handler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        BaseRecyclerHolder holder = (BaseRecyclerHolder) msg.obj;
                        ((TextView)holder.getView(R.id.count)).setText(String.valueOf(msg.arg1));
                        break;
                }
            }
        };


        @Override
        public void convert(BaseRecyclerHolder holder, final int position) {
            ((TextView)holder.getView(R.id.tvId)).setText(String.valueOf(getmData().get(position).getId()));
            setCount(getmData().get(position).getId(),holder);
//            int count = new ShopCarDao().getCount(user_tel, id, getmData().get(position).getId());
            ((TextView)holder.getView(R.id.tvName)).setText(getmData().get(position).getName());
            ((TextView)holder.getView(R.id.tvPrice)).setText("￥" + getmData().get(position).getPrice());
            ((TextView)holder.getView(R.id.tvContent)).setText(getmData().get(position).getContent());
            int i = getImageID(getmData().get(position).getImg());
            ((ImageView)holder.getView(R.id.img)).setImageResource(i);

            //悬停的标题头
            FrameLayout headLayout = holder.getView(R.id.stick_header);
            TextView tvHead = holder.getView(R.id.tv_header);
            if (position == 0){
                headLayout.setVisibility(View.VISIBLE);
                tvHead.setText(getmData().get(position).getTitle());
            }else {
                if (TextUtils.equals(getmData().get(position).getTitle(),getmData().get(position-1).getTitle())){
                    headLayout.setVisibility(View.GONE);
                }else {
                    headLayout.setVisibility(View.VISIBLE);
                    tvHead.setText(getmData().get(position).getTitle());
                }
            }



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    在这里写加入购物车的语句
//                    Toast.makeText(ShopDetailActivity.this,getmData().get(position).getId(),Toast.LENGTH_SHORT).show();
                    int commodity_id = getmData().get(position).getId();
                    Intent intent = new Intent(ShopDetailActivity.this, CommodityDetailActivity.class);
                    intent.putExtra("commodity_id", commodity_id);
                    intent.putExtra("userTel", user_tel);
                    startActivity(intent);
                }
            });


            Button btn_add = holder.itemView.findViewById(R.id.btn_add);//加
            Button btn_sub = holder.itemView.findViewById(R.id.btn_sub);//减
            EditText editText = holder.itemView.findViewById(R.id.count);
            btn_add.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String s = editText.getText().toString();
                    int count = Integer.parseInt(s);
                    count++;
                    editText.setText(String.valueOf(count));

                    //改存的库
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean flag1=ShopCarDao.judgeCarShops(user_tel,id);//1.看店铺库里有没有本店
                            if(flag1){
                                CarCommodities carComm = ShopCarDao.getCarCommoditiesByCodeShopIdCommodityId(user_tel, id, getmData().get(position).getId());//有没有加购过本物
                                if(carComm!=null){
                                    ShopCarDao.updataNum(user_tel,id,getmData().get(position).getId(), Integer.parseInt(editText.getText().toString()));
                                }else {
                                    ShopCarDao.addCommodity(user_tel,id,getmData().get(position).getId(),Integer.parseInt(editText.getText().toString()));
                                }
                            }
                            else {
                                ShopCarDao.addShop(user_tel,id);
                                ShopCarDao.addCommodity(user_tel,id,getmData().get(position).getId(),Integer.parseInt(editText.getText().toString()));
                            }
                            showCount();
                        }
                    }).start();
                }
            });

            btn_sub.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String s = editText.getText().toString();
                    int count = Integer.parseInt(s);
                    if(count <= 1){
                        Toast.makeText(ShopDetailActivity.this, "不能再少啦~去购物车删掉我吧", Toast.LENGTH_SHORT).show();
                    }else {
                        count--;
                        editText.setText(String.valueOf(count));
                        //改存的库
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ShopCarDao.deleteCarByCommodityId(user_tel,getmData().get(position).getId());
                                CarCommodities carCommody=ShopCarDao.getCarCommoditiesByCodeShopIdCommodityId(user_tel,id,getmData().get(position).getId());
                                if(carCommody==null){
                                    List<CarCommodities> commodities = ShopCarDao.getCarCommoditiesByCodeShopId(id, user_tel);//判断此用户在本店是否还有加购的商品了
                                    if(commodities==null)
                                        ShopCarDao.deleteShop(user_tel,id);
                                }
                                showCount();
                            }
                        }).start();
                    }


                }
            });
        }
    }

    // 显示购物车图标中的商品数量
    public void showCount() {
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                int countCar=ShopCarDao.getCommiditiesCount(user_tel);

                Message ms=handler2.obtainMessage();
                ms.what=1;
                ms.obj=countCar;
                handler2.sendMessage(ms);
            }
        }).start();

    }

    @SuppressLint("HandlerLeak")
    private Handler handler2 = new Handler(){
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:

                    /*// 获取当前应用的Application实例
                    MainApplication app = MainApplication.getInstance();
                    // 以下直接修改Application实例中保存的映射全局变量
                    app.infoMap.put("count", msg.obj+"");*/

                    TextView tv_count = findViewById(R.id.tv_count);
                    tv_count.setText(msg.obj+"");
                    break;
            }

            /*// 获取当前应用的Application实例
            MainApplication app = MainApplication.getInstance();
            // 获取Application实例中保存的映射全局变量
            Map<String, String> mapParam = app.infoMap;
            // 遍历映射全局变量内部的键值对信息
            for (Map.Entry<String, String> item_map : mapParam.entrySet()) {
                String value = item_map.getValue();
            }*/
        }
    };

    private int getImageID(String name){
        int id = -1;
        try{
            Log.i("name",name);
            Field field = R.drawable.class.getDeclaredField(name);
            id = field.getInt(field.getName());
        } catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

}
