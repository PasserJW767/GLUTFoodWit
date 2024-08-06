package com.example.gutfoodwit.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.example.gutfoodwit.Dao.ShopCarDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.adapter.ImagePagerAdapater;
import com.example.gutfoodwit.base.BaseActivity;
import com.example.gutfoodwit.bean.ShopInfo;
import com.example.gutfoodwit.fragment.EmptyLoopViewPagerFragment;

import java.util.ArrayList;
import java.util.List;

import static com.mob.MobSDK.getContext;

public class HomePageActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener, ViewPager.OnPageChangeListener {
    EditText et_search;
    ImageButton ib_search;
    private List<ShopInfo> shopInfoList; // 店铺列表

    private List<View> mViews = new ArrayList<View>();
    private LocalActivityManager manager;
    private Intent intentFirst, intentSecond, intentThird;//三个食堂

    ViewPager vp_content;

    private TextView tv_count;// 显示购物车图标中的商品数量
    private ImageView iv_cart;//购物车图标*/

    private Button btn_navigation_home = null;
    private Button btn_navigation_recommend = null;
    private Button btn_navigation_me = null;

    private String userTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(this);
//        设置透明状态栏
        setStatusBar();
        setContentView(R.layout.activity_home_page);
        userTel=getIntent().getExtras().getString("userTel");

        tv_count=findViewById(R.id.tv_count);
        iv_cart=findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建一个意图对象，准备跳到指定的活动页面
                Intent intent=new Intent(HomePageActivity.this,ShopCarActivity.class);
                intent.putExtra("userTel",userTel);//
                // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent); // 跳转到意图对象指定的活动页面
            }
        });

        et_search=findViewById(R.id.et_search);
        et_search.clearFocus();
        et_search.setSelected(false);

        ib_search=findViewById(R.id.ib_search);
        //设置搜索事件监听
        et_search.setOnEditorActionListener(this);
        ib_search.setOnClickListener(this);
        closeSoftKeybord(et_search,getContext());

        initPagerStrip(); // 初始化翻页标签栏

        //设置各个页的活动
        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);
        intentFirst = new Intent(this, CanteenFirstActivity.class);
        intentFirst.putExtra("userTel",userTel);
        View tab01 = manager.startActivity("viewID", intentFirst).getDecorView();
        intentSecond = new Intent(this, CanteenSecondActivity.class);
        intentSecond.putExtra("userTel",userTel);
        View tab02 = manager.startActivity("viewID", intentSecond).getDecorView();
        intentThird = new Intent(this, CanteenThirdActivity.class);
        intentThird.putExtra("userTel",userTel);
        View tab03 = manager.startActivity("viewID", intentThird).getDecorView();

        mViews.add(tab01);//将页面添加到View集合
        mViews.add(tab02);
        mViews.add(tab03);

        initViewPager(); // 初始化翻页视图
        //        初始化导航栏
        initNavigationRadioButton();

        showCount(); // 显示购物车的商品数量
    }

    @Override
    protected void onResume(){
        super.onResume();
        showCount();
    }


    @Override
    protected void setUpView() { }

    @Override
    protected void setUpData(Bundle savedInstanceState) {
        LoopViewPagerToEmpty();
    }

    private void LoopViewPagerToEmpty() {
        EmptyLoopViewPagerFragment instance = EmptyLoopViewPagerFragment.getInstance();

        replace(instance);

        Toast.makeText(mContext, "登录成功！", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.ib_search) {
            closeSoftKeybord(et_search,getContext());
            showSearchInfo(et_search.getText().toString());
        }
        else if(v.getId() == R.id.btn_navigation_me){
            // 创建一个意图对象，准备跳到指定的活动页面
            Intent intent = new Intent(HomePageActivity.this, My.class);
            // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
            intent.putExtra("userTel", userTel);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // 设置启动标志
            startActivity(intent); // 跳转到意图对象指定的活动页面
        }else if(v.getId() == R.id.btn_navigation_recommend){
            // 创建一个意图对象，准备跳到指定的活动页面
            Intent intent = new Intent(this, RecommendActivity.class);
            // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // 设置启动标志
            intent.putExtra("userTel", userTel);
            startActivity(intent); // 跳转到意图对象指定的活动页面
        }

    }

    /**
     * 搜索更新数据
     */
    private void replace(Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        fragmentTransaction.commit();
    }


    private  void showSearchInfo(String search){
        //先根据店名搜，店名有关键字时显示店名和销量最高的一个商品，店名没有时查商品并展示

        // 创建一个意图对象，准备跳到指定的活动页面
        Intent intent = new Intent(this, HomePageSearchShowActivity.class);
        // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // 设置启动标志
        intent.putExtra("search",search);
        intent.putExtra("userTel",userTel);
        startActivity(intent); // 跳转到意图对象指定的活动页面
    }

    /**
     * 监听搜索按钮
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            showSearchInfo(v.getText().toString());
            return true;
        }
        return false;
    }

    /**
     *收起系统键盘的操作
     */
    public void closeSoftKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    // 初始化翻页标签栏
    private void initPagerStrip() {
        // 从布局视图中获取名叫pts_tab的翻页标签栏
        PagerTabStrip pts_tab = findViewById(R.id.pts_tab);
        // 设置翻页标签栏的文本大小
        pts_tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        pts_tab.setTextColor(Color.BLACK); // 设置翻页标签栏的文本颜色
        pts_tab.setTabIndicatorColorResource(R.color.mainColor);//指示条的颜色
    }

    // 初始化翻页视图    ---添加各个页面要显示的
    private void initViewPager() {
        shopInfoList = ShopInfo.getDefaultShopList();

        // 构建一个商品图片的翻页适配器
        ImagePagerAdapater adapter = new ImagePagerAdapater(this, shopInfoList,mViews);
        // 从布局视图中获取名叫vp_content的翻页视图
        vp_content = findViewById(R.id.vp_content);
        vp_content.setAdapter(adapter); // 设置翻页视图的适配器
        vp_content.setCurrentItem(0); // 设置翻页视图显示第一页
        vp_content.addOnPageChangeListener(this); // 给翻页视图添加页面变更监听器
    }

    // 翻页状态改变时触发。state取值说明为：0表示静止，1表示正在滑动，2表示滑动完毕
    // 在翻页过程中，状态值变化依次为：正在滑动→滑动完毕→静止
    public void onPageScrollStateChanged(int state) {}

    // 在翻页过程中触发。该方法的三个参数取值说明为 ：第一个参数表示当前页面的序号
    // 第二个参数表示当前页面偏移的百分比，取值为0到1；第三个参数表示当前页面的偏移距离
    public void onPageScrolled(int position, float ratio, int offset) {}

    // 在翻页结束后触发。position表示当前滑到了哪一个页面
    public void onPageSelected(int position) {
        Toast.makeText(this, "您现在浏览的是：" + shopInfoList.get(position).getCanteenName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    /**
     * 初始化按钮组，设置按钮组的初始状态
     * */
    private void initNavigationRadioButton(){
        btn_navigation_home = findViewById(R.id.btn_navigation_home);
        btn_navigation_home.setOnClickListener(this);
        btn_navigation_recommend = findViewById(R.id.btn_navigation_recommend);
        btn_navigation_recommend.setOnClickListener(this);
        btn_navigation_me = findViewById(R.id.btn_navigation_me);
        btn_navigation_me.setOnClickListener(this);
//        设置“首页”
        Drawable drawable = getResources().getDrawable(R.drawable.navigation_home_selector);
        drawable.setBounds(0,0,65,65);
        RadioButton radioButton = findViewById(R.id.btn_navigation_home);
        radioButton.setCompoundDrawables(null,drawable,null,null);
//        设置“推荐”
        drawable = getResources().getDrawable(R.drawable.navigation_recommend_normal);
        drawable.setBounds(0,0,65,65);
        radioButton = findViewById(R.id.btn_navigation_recommend);
        radioButton.setCompoundDrawables(null,drawable,null,null);
//        设置“我的”
        drawable = getResources().getDrawable(R.drawable.navigation_me_normal);
        drawable.setBounds(0,0,65,65);
        radioButton = findViewById(R.id.btn_navigation_me);
        radioButton.setCompoundDrawables(null,drawable,null,null);
    }

    // 显示购物车图标中的商品数量
    public void showCount() {
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                int countCar=ShopCarDao.getCommiditiesCount(userTel);

                Message ms=handler.obtainMessage();
                ms.what=1;
                ms.obj=countCar;
                handler.sendMessage(ms);
            }
        }).start();

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:

                    /*// 获取当前应用的Application实例
                    MainApplication app = MainApplication.getInstance();
                    // 以下直接修改Application实例中保存的映射全局变量
                    app.infoMap.put("count", msg.obj+"");*/

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
            int permission = ActivityCompat.checkSelfPermission(HomePageActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(HomePageActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if(ContextCompat.checkSelfPermission(HomePageActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(HomePageActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
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
}
