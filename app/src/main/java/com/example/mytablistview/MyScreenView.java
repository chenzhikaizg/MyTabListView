package com.example.mytablistview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by ${chenzhikai} on 2018/5/7.
 */

public class MyScreenView extends LinearLayout implements View.OnClickListener{
    private Context mContext;
    private LinearLayout mMenuTabView;
    private View mShadowView;
    // 内容菜单的高度
    private int mMenuContainerHeight;

    // 阴影的颜色
    private int mShadowColor = 0x88888888;
    private FrameLayout mContentView;
    private long DURATION_TIME = 350;

    private BaseMenuAdapter menuAdapter;

    // 动画是否在执行
    private boolean mAnimatorExecute;
    // 当前打开的位置
    private int mCurrentPosition = -1;
    public MyScreenView(Context context) {
        this(context,null);
    }

    public MyScreenView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mMenuContainerHeight==0&& height>0){
            //内容的高度使整个高度的75%
            mMenuContainerHeight = (int )(height* 75f/100);
            ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
            layoutParams.height = mMenuContainerHeight;
            mContentView.setLayoutParams(layoutParams);
            //进来内容是不显示的
            mContentView.setTranslationY(-mMenuContainerHeight);
        }


    }

    private void initLayout() {
        setOrientation(VERTICAL);
        //创建头部的tab的容器
        mMenuTabView = new LinearLayout(mContext);
        mMenuTabView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mMenuTabView);

        //创建framlayout用来存放菜单的布局内容和阴影
        FrameLayout mMenuContainerView=  new FrameLayout(mContext);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight=1;
        mMenuContainerView.setLayoutParams(layoutParams);

        addView(mMenuContainerView);

        //创建阴影
        mShadowView = new View(mContext);
        mShadowView.setBackgroundColor(mShadowColor);
        mShadowView.setAlpha(0f);
        mShadowView.setOnClickListener(this);
        mShadowView.setVisibility(GONE);
        mMenuContainerView.addView(mShadowView);

        //创建container的内容部分
        mContentView = new FrameLayout(mContext);
        mContentView.setBackgroundColor(Color.WHITE);
        mMenuContainerView.addView(mContentView);



    }


    public void setAdapter(BaseMenuAdapter adapter){
        this.menuAdapter = adapter;
        int count = menuAdapter.getConut();
        for (int i=0;i<count;i++){
            View tabView = menuAdapter.getTabView(i, mMenuTabView);
            mMenuTabView.addView(tabView);
            LinearLayout.LayoutParams layoutParams = (LayoutParams) tabView.getLayoutParams();
            layoutParams.weight=1;
            tabView.setLayoutParams(layoutParams);

            setTabClick(tabView,i);

            View menuView = menuAdapter.getMenuView(i, mContentView);
            menuView.setVisibility(GONE);
            mContentView.addView(menuView);
        }



    }

    private void setTabClick(final View tabView, final int i) {
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition ==-1){
                    //没打开，现在打开
                    openMenu(i,tabView);


                }else {
                    if (mCurrentPosition == i){
                        //打开了关闭掉
                        closeMenu();

                    }else {
                        //切换到我们点击的view
                        View childAt = mContentView.getChildAt(mCurrentPosition);
                        childAt.setVisibility(GONE);
                        menuAdapter.close(mMenuTabView.getChildAt(mCurrentPosition));
                        mCurrentPosition = i;
                        View mCurrentView = mContentView.getChildAt(mCurrentPosition);
                        mCurrentView.setVisibility(VISIBLE);
                        menuAdapter.menuOpen(mMenuTabView.getChildAt(mCurrentPosition));
                    }
                }
            }
        });








    }

    public void closeMenu(){
        if (mAnimatorExecute) {
            return;
        }
        //关闭动画
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(mContentView,"translationY",0, -mMenuContainerHeight);
        transAnimator.setDuration(DURATION_TIME);
        transAnimator.start();
        mShadowView.setVisibility(View.VISIBLE);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView, "alpha", 1f, 0f);
        alphaAnimator.setDuration(DURATION_TIME);
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 要等关闭动画执行完才能去隐藏当前菜单
                View menuView = mContentView.getChildAt(mCurrentPosition);
                menuView.setVisibility(GONE);
                mCurrentPosition = -1;
                mShadowView.setVisibility(GONE);
                mAnimatorExecute = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {

                mAnimatorExecute = true;
                menuAdapter.close(mMenuTabView.getChildAt(mCurrentPosition));
            }
        });

        alphaAnimator.start();
    }

    public void openMenu(final int posiiton , final View tabView){

        if (mAnimatorExecute) {
            return;
        }

        mShadowView.setVisibility(VISIBLE);
        View menuView = mContentView.getChildAt(posiiton);
        menuView.setVisibility(VISIBLE);

        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(mContentView,"translationY",-mMenuContainerHeight,0);
        transAnimator.setDuration(DURATION_TIME);
        transAnimator.start();

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mShadowView,"alpha",0f,1f);
        alphaAnimator.setDuration(DURATION_TIME);
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorExecute = false;
                mCurrentPosition = posiiton;

            }

            @Override
            public void onAnimationStart(Animator animation) {
                mAnimatorExecute = true;
                //把当前的tab传到外面
                menuAdapter.menuOpen(tabView);

            }
        });


        alphaAnimator.start();


    }


    @Override
    public void onClick(View v) {
        closeMenu();
    }
}
