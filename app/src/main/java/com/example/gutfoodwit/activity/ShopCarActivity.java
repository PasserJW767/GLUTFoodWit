package com.example.gutfoodwit.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gutfoodwit.R;
import com.example.gutfoodwit.adapter.ShopCarAdapter;
import com.example.gutfoodwit.bean.ShopCarDatas;
import com.example.gutfoodwit.bean.ShopCarInstance;
import com.example.gutfoodwit.utlis.RoundCornerDialog;
import com.example.gutfoodwit.utlis.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopCarActivity extends AppCompatActivity {

    @BindView(R.id.tv_titlebar_center)//绑定一个view；id为一个view 变量
    TextView tvTitlebarCenter;//title
    @BindView(R.id.tv_titlebar_right)
    TextView tvTitlebarRight;//编辑
    @BindView(R.id.elv_shopping_car)
    ExpandableListView elvShoppingCar;//ExpandableListView可折叠的列表，它是ListView的子类， 在ListView的基础上它把应用中的列表项分为几组，每组里又可包含多个列表项
    @BindView(R.id.iv_select_all)
    ImageView ivSelectAll;//全选
    @BindView(R.id.ll_select_all)
    LinearLayout llSelectAll;
    @BindView(R.id.btn_order)
    Button btnOrder;//结算
    @BindView(R.id.btn_delete)
    Button btnDelete;//删除
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;//算总价的值
    @BindView(R.id.rl_total_price)
    RelativeLayout rlTotalPrice;//算总价的布局
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.iv_no_contant)
    ImageView ivNoContant;//购物车为空时的图片
    @BindView(R.id.rl_no_contant)
    RelativeLayout rlNoContant;//购物车为空时的布局
    @BindView(R.id.tv_titlebar_left)
    TextView tvTitlebarLeft;//刷新


    private List<ShopCarDatas.DatasBean> datas;//某用户订单信息
    private Context context;
    private ShopCarAdapter shoppingCarAdapter;

    int shop_id;
    int commodity_id;

    private String userTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        userTel=getIntent().getExtras().getString("userTel");
        ButterKnife.bind(this);
        context = this;

        initExpandableListView();
        initData();
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        initExpandableListViewData(datas);
    }*/

    /**
     * 初始化ExpandableListView
     * 创建数据适配器adapter，并进行初始化操作
     */
    private void initExpandableListView() {
        shoppingCarAdapter = new ShopCarAdapter(context, llSelectAll, ivSelectAll, btnOrder, btnDelete, rlTotalPrice, tvTotalPrice);
        elvShoppingCar.setAdapter(shoppingCarAdapter);

        //删除的回调
        shoppingCarAdapter.setOnDeleteListener(new ShopCarAdapter.OnDeleteListener() {
            @Override
            public void onDelete() {
                initDelete();
                initExpandableListViewData(datas);
            }
        });

        //修改商品数量的回调
        shoppingCarAdapter.setOnChangeCountListener(new ShopCarAdapter.OnChangeCountListener() {
            @Override
            public void onChangeCount(int shop_id,int commodity_id,int num) {
                //修改数据库，刷新
                ShopCarInstance shopCarInstance = new ShopCarInstance();
                shopCarInstance.init(userTel);
                shopCarInstance.updataNum(userTel,shop_id,commodity_id,num);
                List<ShopCarDatas.DatasBean> datasBeans=shopCarInstance.getDatasBeans();
                datas=datasBeans;
                initExpandableListViewData(datas);
            }
        });

        //结算的回调
        shoppingCarAdapter.setOnPayListener(new ShopCarAdapter.OnPayListener() {
            @Override
            public void toPay(int shop_id, int[] array) {
                Intent intent=new Intent(ShopCarActivity.this,PlaceOrderActivity.class);
                intent.putExtra("shopId",shop_id);
                intent.putExtra("commodities",array);
                intent.putExtra("userTel",userTel);
                // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent); // 跳转到意图对象指定的活动页面
            }
        });
    }

    /**
     * 初始化数据
     */
    @SuppressLint("LongLogTag")
    private void initData() {
        //通过用户id
        ShopCarInstance shopCarInstance = new ShopCarInstance();
        shopCarInstance.init(userTel);//用户电话
        List<ShopCarDatas.DatasBean> datasBeans=shopCarInstance.getDatasBeans();
        datas=datasBeans;
        initExpandableListViewData(datas);
    }

    /**
     * 判断是否要弹出删除的dialog
     * 通过bean类中的DatasBean的isSelect_shop属性，判断店铺是否被选中；
     * GoodsBean的isSelect属性，判断商品是否被选中，
     */
    private void initDelete() {
        //判断是否有店铺或商品被选中
        //true为有，则需要刷新数据；反之，则不需要；
        boolean hasSelect = false;
        //创建临时的List，用于存储没有被选中的购物车数据
        List<ShopCarDatas.DatasBean> datasTemp = new ArrayList<>();

        for (int i = 0; i < datas.size(); i++) {
            List<ShopCarDatas.DatasBean.CommodityBean> goods = datas.get(i).getCommodities();
            boolean isSelect_shop = datas.get(i).getIsSelect_shop();

            if (isSelect_shop) {
                shop_id=i;
                hasSelect = true;
                //跳出本次循环，继续下次循环。
                continue;
            } else {
                datasTemp.add(datas.get(i).clone());
                datasTemp.get(datasTemp.size() - 1).setCommodities(new ArrayList<ShopCarDatas.DatasBean.CommodityBean>());
            }

            for (int y = 0; y < goods.size(); y++) {
                ShopCarDatas.DatasBean.CommodityBean commodityBean = goods.get(y);
                boolean isSelect = commodityBean.getIsSelect();

                if (isSelect) {
                    commodity_id=y;
                    hasSelect = true;
                } else {
                    datasTemp.get(datasTemp.size() - 1).getCommodities().add(commodityBean);
                }
            }
        }

        if (hasSelect) {
            showDeleteDialog(datasTemp);
        } else {
            ToastUtil.makeText(context, "请选择要删除的商品");
        }
    }

    /**
     * 初始化ExpandableListView的数据
     * 并在数据刷新时，页面保持当前位置
     *
     * @param datas 购物车的数据
     */
    private void initExpandableListViewData(List<ShopCarDatas.DatasBean> datas) {
        Log.e("datas1", String.valueOf(datas));
        if (datas != null && datas.size() > 0) {
            //刷新数据时，保持当前位置
            shoppingCarAdapter.setData(datas);

            //使所有组展开
            for (int i = 0; i < shoppingCarAdapter.getGroupCount(); i++) {
                elvShoppingCar.expandGroup(i);
            }

            //使组点击无效果
            elvShoppingCar.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return true;
                }
            });

            tvTitlebarRight.setVisibility(View.VISIBLE);
            tvTitlebarRight.setText("编辑");
            rlNoContant.setVisibility(View.GONE);
            elvShoppingCar.setVisibility(View.VISIBLE);
            rl.setVisibility(View.VISIBLE);
            rlTotalPrice.setVisibility(View.VISIBLE);
            btnOrder.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
        } else {
            tvTitlebarRight.setVisibility(View.GONE);
            rlNoContant.setVisibility(View.VISIBLE);
            elvShoppingCar.setVisibility(View.GONE);
            rl.setVisibility(View.GONE);
        }
    }

    /**
     * 展示删除的dialog（可以自定义弹窗，不用删除即可）
     *
     * @param datasTemp
     */
    private void showDeleteDialog(final List<ShopCarDatas.DatasBean> datasTemp) {
        View view = View.inflate(context, R.layout.dialog_two_btn, null);
        final RoundCornerDialog roundCornerDialog = new RoundCornerDialog(context, 0, 0, view, R.style.RoundCornerDialog);
        roundCornerDialog.show();
        roundCornerDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        roundCornerDialog.setOnKeyListener(keylistener);//设置点击返回键Dialog不消失

        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        TextView tv_logout_confirm = (TextView) view.findViewById(R.id.tv_logout_confirm);
        TextView tv_logout_cancel = (TextView) view.findViewById(R.id.tv_logout_cancel);
        tv_message.setText("确定要删除商品吗？");

        //确定
        tv_logout_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundCornerDialog.dismiss();
                //datas = datasTemp;

                //操作库
                for (int i = 0; i < datas.size(); i++) {
                    List<ShopCarDatas.DatasBean.CommodityBean> goods = datas.get(i).getCommodities();
                    boolean isSelect_shop = datas.get(i).getIsSelect_shop();

                    if (isSelect_shop) {
                        shop_id=datas.get(i).getId();
                        Log.e("删除店铺", String.valueOf(shop_id));
                        ShopCarInstance shopCarInstance = new ShopCarInstance();
                        shopCarInstance.deleteByShopId(userTel,shop_id);//用户电话
                    }
                    for (int y = 0; y < goods.size(); y++) {
                        ShopCarDatas.DatasBean.CommodityBean commodityBean = goods.get(y);
                        boolean isSelect = commodityBean.getIsSelect();

                        if (isSelect) {
                            commodity_id=goods.get(y).getCommodity_id();
                            Log.e("删除商品", String.valueOf(commodity_id));
                            ShopCarInstance shopCarInstance = new ShopCarInstance();
                            shopCarInstance.deleteByCommodityId(userTel,shop_id);//用户电话
                        }
                    }
                }
                ShopCarInstance shopCarInstance = new ShopCarInstance();
                shopCarInstance.init(userTel);
                List<ShopCarDatas.DatasBean> datasBeans=shopCarInstance.getDatasBeans();
                datas=datasBeans;
                initExpandableListViewData(datas);
            }
        });
        //取消
        tv_logout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundCornerDialog.dismiss();
            }
        });
    }

    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };

    @OnClick({R.id.tv_titlebar_left, R.id.tv_titlebar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_titlebar_left://刷新数据
                initData();
                break;
            case R.id.tv_titlebar_right://编辑
                String edit = tvTitlebarRight.getText().toString().trim();
                if (edit.equals("编辑")) {
                    tvTitlebarRight.setText("完成");
                    rlTotalPrice.setVisibility(View.GONE);
                    btnOrder.setVisibility(View.GONE);
                    btnDelete.setVisibility(View.VISIBLE);
                } else {
                    tvTitlebarRight.setText("编辑");
                    rlTotalPrice.setVisibility(View.VISIBLE);
                    btnOrder.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }
}