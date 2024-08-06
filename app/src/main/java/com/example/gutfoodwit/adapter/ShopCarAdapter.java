package com.example.gutfoodwit.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.activity.PlaceOrderActivity;
import com.example.gutfoodwit.bean.ShopCarDatas;
import com.example.gutfoodwit.utlis.ToastUtil;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopCarAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final LinearLayout llSelectAll;
    private final ImageView ivSelectAll;
    private final Button btnOrder;
    private final Button btnDelete;
    private final RelativeLayout rlTotalPrice;
    private final TextView tvTotalPrice;
    private List<ShopCarDatas.DatasBean> data;
    private boolean isSelectAll = false;
    private double total_price;

    public ShopCarAdapter(Context context, LinearLayout llSelectAll,
                              ImageView ivSelectAll, Button btnOrder, Button btnDelete,
                              RelativeLayout rlTotalPrice, TextView tvTotalPrice) {
        this.context = context;
        this.llSelectAll = llSelectAll;
        this.ivSelectAll = ivSelectAll;
        this.btnOrder = btnOrder;
        this.btnDelete = btnDelete;
        this.rlTotalPrice = rlTotalPrice;
        this.tvTotalPrice = tvTotalPrice;
    }

    /**
     * 自定义设置数据方法；
     * 通过notifyDataSetChanged()刷新数据，可保持当前位置
     *
     * @param data 需要刷新的数据
     */
    public void setData(List<ShopCarDatas.DatasBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        if (data != null && data.size() > 0) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopping_car_group, null);

            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final ShopCarDatas.DatasBean datasBean = data.get(groupPosition);
        //店铺ID
        int id = datasBean.getId();
        //店铺名称
        String shop_name = datasBean.getShop_name();

        if (shop_name != null) {
            groupViewHolder.tvStoreName.setText(shop_name);
        } else {
            groupViewHolder.tvStoreName.setText("");
        }

        //店铺内的商品都选中的时候，店铺的也要选中
        for (int i = 0; i < datasBean.getCommodities().size(); i++) {
            ShopCarDatas.DatasBean.CommodityBean goodsBean = datasBean.getCommodities().get(i);
            boolean isSelect = goodsBean.getIsSelect();
            if (isSelect) {
                datasBean.setIsSelect_shop(true);
            } else {
                datasBean.setIsSelect_shop(false);
                break;
            }
        }

        //因为set之后要重新get，所以这一块代码要放到一起执行
        //店铺是否在购物车中被选中
        final boolean isSelect_shop = datasBean.getIsSelect_shop();
        if (isSelect_shop) {
            groupViewHolder.ivSelect.setImageResource(R.drawable.select);
        } else {
            groupViewHolder.ivSelect.setImageResource(R.drawable.unselect);
        }

        //店铺选择框的点击事件
        groupViewHolder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datasBean.setIsSelect_shop(!isSelect_shop);

                List<ShopCarDatas.DatasBean.CommodityBean> commodities = datasBean.getCommodities();
                for (int i = 0; i < commodities.size(); i++) {
                    ShopCarDatas.DatasBean.CommodityBean goodsBean = commodities.get(i);
                    goodsBean.setIsSelect(!isSelect_shop);
                }
                notifyDataSetChanged();
            }
        });

        //当所有的选择框都是选中的时候，全选也要选中
        w:
        for (int i = 0; i < data.size(); i++) {
            List<ShopCarDatas.DatasBean.CommodityBean> goods = data.get(i).getCommodities();
            for (int y = 0; y < goods.size(); y++) {
                ShopCarDatas.DatasBean.CommodityBean goodsBean = goods.get(y);
                boolean isSelect = goodsBean.getIsSelect();
                if (isSelect) {
                    isSelectAll = true;
                } else {
                    isSelectAll = false;
                    break w;//根据标记，跳出嵌套循环
                }
            }
        }
        if (isSelectAll) {
            ivSelectAll.setBackgroundResource(R.drawable.select);
        } else {
            ivSelectAll.setBackgroundResource(R.drawable.unselect);
        }

        //全选的点击事件
        llSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectAll = !isSelectAll;

                if (isSelectAll) {
                    for (int i = 0; i < data.size(); i++) {
                        List<ShopCarDatas.DatasBean.CommodityBean> commodities = data.get(i).getCommodities();
                        for (int y = 0; y < commodities.size(); y++) {
                            ShopCarDatas.DatasBean.CommodityBean goodsBean = commodities.get(y);
                            goodsBean.setIsSelect(true);
                        }
                    }
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        List<ShopCarDatas.DatasBean.CommodityBean> commodities = data.get(i).getCommodities();
                            for (int y = 0; y < commodities.size(); y++) {
                                ShopCarDatas.DatasBean.CommodityBean goodsBean = commodities.get(y);
                                goodsBean.setIsSelect(false);
                            }
                    }
                }
                notifyDataSetChanged();
            }
        });

        //合计的计算
        total_price = 0.0;
        tvTotalPrice.setText("¥0.00");
        for (int i = 0; i < data.size(); i++) {
            List<ShopCarDatas.DatasBean.CommodityBean> commodities = data.get(i).getCommodities();
                for (int y = 0; y < commodities.size(); y++) {
                    ShopCarDatas.DatasBean.CommodityBean goodsBean = commodities.get(y);
                    boolean isSelect = goodsBean.getIsSelect();
                    if (isSelect) {
                        int num = goodsBean.getCommodity_num();
                        double price = goodsBean.getCommodity_price();

                        int v = num;
                        double v1 = price;

                        total_price = total_price + v * v1;

                        //让Double类型完整显示，不用科学计数法显示大写字母E
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        tvTotalPrice.setText("¥" + decimalFormat.format(total_price));
                    }
                }
        }

        //去结算的点击事件
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建临时的List，用于存储有商品被选中的店铺
                List<ShopCarDatas.DatasBean> tempShops = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    //店铺中是否有商品被选中
                    boolean hasGoodsSelect = false;
                    //创建临时的List，用于存储被选中的商品
                    List<ShopCarDatas.DatasBean.CommodityBean> tempCommodities = new ArrayList<>();

                    ShopCarDatas.DatasBean datasBean1 = data.get(i);
                    List<ShopCarDatas.DatasBean.CommodityBean> commodities = datasBean1.getCommodities();
                    for (int y = 0; y < commodities.size(); y++) {
                        ShopCarDatas.DatasBean.CommodityBean goodsBean = commodities.get(y);
                        boolean isSelect = goodsBean.getIsSelect();
                        if (isSelect) {
                            hasGoodsSelect = true;
                            tempCommodities.add(goodsBean);
                        }
                    }

                    if (hasGoodsSelect) {
                        ShopCarDatas.DatasBean datasBean2 = new ShopCarDatas.DatasBean();
                        datasBean2.setId(datasBean1.getId());
                        datasBean2.setShop_name(datasBean1.getShop_name());
                        datasBean2.setCommodities(tempCommodities);

                        tempShops.add(datasBean2);
                    }
                }

                if (tempShops != null && tempShops.size() > 0) {//如果有被选中的
                    /**
                     * 实际开发中，如果有被选中的商品，
                     * 则跳转到确认订单页面，完成后续订单流程。
                     */
                    if(tempShops.size()>1)
                        ToastUtil.makeText(context, "请选择一家店铺的商品进行支付！");
                    else {
                        List<ShopCarDatas.DatasBean.CommodityBean> commodities = tempShops.get(0).getCommodities();
                        int[] array=new int[commodities.size()];
                        for(int i=0;i<commodities.size();i++){
                            Log.e("array"+i, String.valueOf(commodities.get(i).getCommodity_id()));
                            array[i]=commodities.get(i).getCommodity_id();
                        }
                        if (mPayListener != null) {
                            mPayListener.toPay(tempShops.get(0).getId(),array);
                        }
                    }
                } else {
                    ToastUtil.makeText(context, "请选择要购买的商品");
                }
            }
        });

        //删除的点击事件
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 实际开发中，通过回调请求后台接口实现删除操作
                 */
                if (mDeleteListener != null) {
                    mDeleteListener.onDelete();
                }
            }
        });

        return convertView;
    }

    static class GroupViewHolder {
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.tv_store_name)
        TextView tvStoreName;
        @BindView(R.id.ll)
        LinearLayout ll;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //------------------------------------------------------------------------------------------------
    @Override
    public int getChildrenCount(int groupPosition) {
        if (data.get(groupPosition).getCommodities() != null && data.get(groupPosition).getCommodities().size() > 0) {
            return data.get(groupPosition).getCommodities().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getCommodities().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopping_car_child, null);

            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final ShopCarDatas.DatasBean datasBean = data.get(groupPosition);
        //店铺ID
        int id = datasBean.getId();
        //店铺名称
        String shop_name = datasBean.getShop_name();
        //店铺是否在购物车中被选中
        final boolean isSelect_shop = datasBean.getIsSelect_shop();
        final ShopCarDatas.DatasBean.CommodityBean commodityBean = datasBean.getCommodities().get(childPosition);
        //商品图片
        String commodity_img = commodityBean.getCommodity_img();
        //商品ID
        final int commodity_id = commodityBean.getCommodity_id();
        //商品名称
        String commodity_name = commodityBean.getCommodity_name();
        //商品价格
        double commodity_price = commodityBean.getCommodity_price();
        //商品数量
        int commodity_num = commodityBean.getCommodity_num();
        //商品是否被选中
        final boolean isSelect = commodityBean.getIsSelect();

        childViewHolder.ivPhoto.setImageResource(getImageID(commodity_img));
        /*Glide.with(context)
                .load(commodity_img)
                .into(childViewHolder.ivPhoto);*/
        if (commodity_name != null) {
            childViewHolder.tvName.setText(commodity_name);
        } else {
            childViewHolder.tvName.setText("");
        }
        if (commodity_price != 0) {
            childViewHolder.tvPriceValue.setText(String.valueOf(commodity_price));
        } else {
            childViewHolder.tvPriceValue.setText("");
        }
        if (commodity_num != 0) {
            childViewHolder.tvEditBuyNumber.setText(commodity_num+"");
        } else {
            childViewHolder.tvEditBuyNumber.setText("");
        }

        //商品是否被选中
        if (isSelect) {
            childViewHolder.ivSelect.setImageResource(R.drawable.select);
        } else {
            childViewHolder.ivSelect.setImageResource(R.drawable.unselect);
        }

        //商品选择框的点击事件
        childViewHolder.ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commodityBean.setIsSelect(!isSelect);
                if (!isSelect == false) {
                    datasBean.setIsSelect_shop(false);
                }
                notifyDataSetChanged();
            }
        });

        //加号的点击事件
        childViewHolder.ivEditAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟加号操作
                int num = commodityBean.getCommodity_num();
                Integer integer = Integer.valueOf(num);
                integer++;
                commodityBean.setCommodity_num(integer);
                //int commodity_id1 = commodityBean.getCommodity_id();


                notifyDataSetChanged();//动态刷新

                /**
                 * 实际开发中，通过回调请求后台接口实现数量的加减
                 */
                if (mChangeCountListener != null) {
                    mChangeCountListener.onChangeCount(id,commodity_id,integer);
                }
            }
        });
        //减号的点击事件
        childViewHolder.ivEditSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟减号操作
                int num = commodityBean.getCommodity_num();
                Integer integer = Integer.valueOf(num);
                if (integer>1) {
                    integer--;
                    commodityBean.setCommodity_num(integer);

                    /**
                     * 实际开发中，通过回调请求后台接口实现数量的加减
                     */
                    if (mChangeCountListener != null) {
                        mChangeCountListener.onChangeCount(id,commodity_id,integer);
                    }
                } else {
                    ToastUtil.makeText(context, "商品不能再减少了");
                }
                notifyDataSetChanged();
            }
        });

        if (childPosition == data.get(groupPosition).getCommodities().size() - 1) {
            childViewHolder.view.setVisibility(View.GONE);
            childViewHolder.viewLast.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.view.setVisibility(View.VISIBLE);
            childViewHolder.viewLast.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ChildViewHolder {
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price_key)
        TextView tvPriceKey;
        @BindView(R.id.tv_price_value)
        TextView tvPriceValue;
        @BindView(R.id.iv_edit_subtract)
        ImageView ivEditSubtract;
        @BindView(R.id.tv_edit_buy_number)
        TextView tvEditBuyNumber;
        @BindView(R.id.iv_edit_add)
        ImageView ivEditAdd;
        @BindView(R.id.view)
        View view;
        @BindView(R.id.view_last)
        View viewLast;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //-----------------------------------------------------------------------------------------------

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //删除的回调
    public interface OnDeleteListener {
        void onDelete();
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        mDeleteListener = listener;
    }

    private OnDeleteListener mDeleteListener;

    //修改商品数量的回调
    public interface OnChangeCountListener {
        void onChangeCount(int shop_id,int goods_id,int num);
    }

    public void setOnChangeCountListener(OnChangeCountListener listener) {
        mChangeCountListener = listener;
    }

    private OnChangeCountListener mChangeCountListener;

    //通过得到的String得对应图片ID
    protected int getImageID(String name){
        int id = -1;
        try {
            Log.i("图片name",name);
            Field field = R.drawable.class.getDeclaredField(name);
            id = field.getInt(field.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    //购物车结算的回调
    public interface OnPayListener {
        void toPay(int shop_id,int[] array);
    }

    public void setOnPayListener(OnPayListener listener) {
        mPayListener = listener;
    }

    private OnPayListener mPayListener;
}

