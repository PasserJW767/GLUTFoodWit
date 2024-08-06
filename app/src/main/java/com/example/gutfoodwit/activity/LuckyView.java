package com.example.gutfoodwit.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import androidx.annotation.Nullable;

import com.example.gutfoodwit.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LuckyView extends View {
    private boolean flag = true;
    private Paint mPaint;
    private float mStrokeWidth = 5;
    private int mRepeatCount = 5; // 转的圈数
    private int mRectSize; // 矩形的宽和高（矩形为正方形）
    private boolean mShouldStartFlag;
    private boolean mShouldStartNextTurn = true; // 标记是否应该开启下一轮抽奖
    private int mStartLuckPosition = 0; // 开始抽奖的位置
    private int mCurrentPosition = -1; // 当前转圈所在的位置

    private OnLuckAnimationEndListener mLuckAnimationEndListener;

    /**
     * 可以通过对 mLuckNum 设置计算策略，来控制用户 中哪些奖 以及 中大奖 的概率
     */
    private int mLuckNum = 2; // 默认最终中奖位置

    private List<RectF> mRectFs; // 存储矩形的集合
    private int[] mItemColor = {Color.parseColor("#ffefd6"), Color.parseColor("#ffefd6")}; // 矩形的颜色
//    存储九宫格的字
    private String[] mPrizeDescription = {"￥10代金券", "很遗憾,未中奖", "￥30代金券", "很遗憾,未中奖", "￥10代金券", "很遗憾,未中奖", "￥30代金券", "很遗憾,未中奖", "点击此处抽奖"};
//    存储九宫格的图片
    private int[] mLuckyPrizes = {R.drawable.lottery_voncher, R.drawable.lottery_sad, R.drawable.lottery_voncher, R.drawable
            .lottery_sad, R.drawable.lottery_voncher, R.drawable.lottery_sad, R.drawable.lottery_voncher, R.drawable.lottery_sad,
            R.drawable.lottery_start};

    private List<String> lettersBeans;
    private float left;
    private float top;
    private Bitmap[] bitmaps;
    private String[] luckyName;
    private String[] id;
    private int selectPos;

    public LuckyView(Context context) {
        this(context, null);
    }

    public LuckyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        设置为只绘制图形内容（不绘制轮廓）
        mPaint.setStyle(Paint.Style.FILL);
//        设置描边的宽度
        mPaint.setStrokeWidth(mStrokeWidth);
//        初始化存储矩形的集合
        mRectFs = new ArrayList<>();
    }

    /**
     * 设置监听器
     * */
    public void setLuckAnimationEndListener(OnLuckAnimationEndListener luckAnimationEndListener) {
        mLuckAnimationEndListener = luckAnimationEndListener;
    }

    /*public int getLuckNum() {
        return mLuckNum;
    }*/

    /**
     * 设置幸运数
     * */
    public void setLuckNum(int luckNum) {
        mLuckNum = luckNum;
    }

    /*public int[] getLuckyPrizes() {
        return mLuckyPrizes;
    }*/

    /**
     * 设置中奖图片集合
     * */
    /*public void setLuckyPrizes(int[] luckyPrizes) {
        mLuckyPrizes = luckyPrizes;
    }

    public void setData(List<String> lettersBeans) {
        this.lettersBeans = lettersBeans;
        invalidate();
    }*/

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectSize = (Math.min(w, h)) / 3; // 矩形的宽高

        mRectFs.clear(); // 当控件大小改变的时候清空数据
        initNineRect();
    }

    /**
     * 初始化 9 个矩形（正方形）的位置信息
     */
    private void initNineRect() {
//        获得屏幕宽度
        final float width = getWidth();

//        加载前三个矩形，最顶层，从左往右加载
        for (int i = 0; i < 3; i++) {
//            mRectSize是矩形宽度
//            left代表矩形左边的位置，+5表padding-left = 5px
            float left = i * mRectSize + 5;
//            right代表矩形右边的位置，第一个图形右边的位置就是一个矩形宽，第二个图形右边的位置就是两个矩形宽，以此类推
            float right = (i + 1) * mRectSize;
            float top = 5;
            float bottom = mRectSize;
            RectF rectF = new RectF(left, top, right, bottom);
            mRectFs.add(rectF);
        }

        // 加载第 4 个矩形
        mRectFs.add(new RectF(width - mRectSize + 5, mRectSize + 5, width, 2 * mRectSize));

        // 加载第 5~7 个矩形，最底层，从右往左加载
        for (int j = 3; j > 0; j--) {
            float left = width - (4 - j) * mRectSize + 5;
            float right = width - (3 - j) * mRectSize;
            float top = 2 * mRectSize + 5;
            float bottom = 3 * mRectSize;
            RectF rectF = new RectF(left, top, right, bottom);
            mRectFs.add(rectF);
        }

        // 加载第 8 个矩形
        mRectFs.add(new RectF(5, mRectSize + 5, mRectSize, 2 * mRectSize));

        // 加载中心第 9 个矩形
        mRectFs.add(new RectF(mRectSize + 5, mRectSize + 5, 2 * mRectSize, 2 * mRectSize));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawNineRect(canvas);
        drawNineBitmaps(canvas); // 填充奖品图片
        drawNineText(canvas); // 填充奖品文字
    }

    /**
     * 在每个矩形中填充奖品图片
     */
    private void drawNineBitmaps(final Canvas canvas) {

        for (int i = 0; i < mRectFs.size(); i++) {
            RectF rectF = mRectFs.get(i);
            // 将图片设置在每个矩形中央
            left = rectF.left + mRectSize / 4;
            top = rectF.top + mRectSize / 4;
            canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), mLuckyPrizes[i]), mRectSize / 2, mRectSize / 2, false), left, top, null);
        }
    }


    /**
     * 在每个矩形中央填充文字
     */
    private void drawNineText(Canvas canvas) {
        for (int i = 0; i < mRectFs.size(); i++) {
            RectF rectF = mRectFs.get(i);
            float x = rectF.left + mRectSize / 4 + 1; // 将文字设置在每个矩形中央
            float y = rectF.top + mRectSize - 30;
            mPaint.setColor(Color.parseColor("#5e5448"));
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextSize(35); // unit px
            canvas.drawText(mPrizeDescription[i], x, y, mPaint);
        }
    }

    /**
     * 执行真正的绘制矩形操作
     */
    private void drawNineRect(Canvas canvas) {
        for (int x = 0; x < mRectFs.size(); x++) {
            RectF rectF = mRectFs.get(x);
            if (x == 8) {
                mPaint.setColor(Color.WHITE);
            } else {
                if (mCurrentPosition == x) {
                    mPaint.setColor(Color.parseColor("#edcea9"));
                } else {
                    mPaint.setColor(mItemColor[x % 2]); // 标记当前转盘经过的位置
                }
            }
            canvas.drawRect(rectF, mPaint);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mShouldStartFlag = mRectFs.get(8).contains(event.getX(), event.getY());
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mShouldStartFlag) {
                if (mRectFs.get(8).contains(event.getX(), event.getY()) && flag) {
                    startAnim(); // 判断只有手指落下和抬起都在中间的矩形内时才开始执行动画抽奖
                    flag = false;
                }
//                不能抽奖了
                else if(flag == false){
                    mLuckAnimationEndListener.onLuckAnimationEnd(-1,
                            "您今天已经抽过奖了，不能再抽奖了哦~明天再来吧");
                }
                mShouldStartFlag = false;
            }
        }
        return super.onTouchEvent(event);
    }

    private void startAnim() {
        if (!mShouldStartNextTurn) {
            return;
        }
        Random random = new Random();
        int temp;
//        生成双数的随机数，保证不中奖
        while ((temp = random.nextInt(8))%2 == 0){
        }
        setLuckNum(temp);
//        setDuration()设置动画的时长为5s
        ValueAnimator animator = ValueAnimator.ofInt(mStartLuckPosition, mRepeatCount * 8 + mLuckNum)
                .setDuration(5000);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final int position = (int) animation.getAnimatedValue();
                setCurrentPosition(position % 8);
                mShouldStartNextTurn = false;
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mShouldStartNextTurn = true;
                mStartLuckPosition = mLuckNum;
                //最终选中的位置
                if (mLuckAnimationEndListener != null) {
                    mLuckAnimationEndListener.onLuckAnimationEnd(mCurrentPosition,
                            mPrizeDescription[mCurrentPosition]);
                }
            }
        });

        animator.start();
    }

    private void setCurrentPosition(int position) {
        mCurrentPosition = position;
        invalidate(); // 强制刷新，在 UI 线程回调 onDraw()
    }

    /**
     * 选中id
     *
     */
    public void setSelectId(int datas) {
        String selectId = datas + "";

        if (id != null && id.length != 0) {
            for (int i = 0; i < id.length; i++) {
                if (id[i].equals(selectId)) {
                    selectPos = i;
                }
            }
        }

        startAnim();
    }

    /**
     * 用于抽奖结果回调，内部接口
     * 即定义以什么样的方式来回调
     * 前者表位置，后者表奖品内容
     */
    public interface OnLuckAnimationEndListener {
        void onLuckAnimationEnd(int pos, String msg);
    }
}
