package com.lzxmy.demo.suspension;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lzxmy.demo.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by apple on 15/10/19.
 */
public class SuspensionActivity extends Activity {
    LinearLayout left, right;
    ImageView imageView;
    float valuetemp = 0.0f;
    float letfwith = 0;
    float rightwith = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suspension_layout);

        imageView = (ImageView) findViewById(R.id.imageView2);
        left = (LinearLayout) findViewById(R.id.left);
        right = (LinearLayout) findViewById(R.id.right);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (relative_1.indexOfChild(tip_su) > -1) {
//                    relative_1.removeView(tip_su);
//                    return;
//                }
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.addRule(RelativeLayout.LEFT_OF, R.id.imageView2);
//                params.addRule(RelativeLayout.CENTER_VERTICAL);
//
//                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) relative_1.getLayoutParams();
//                Log.i("TAG", tip_su.getMeasuredWidth() + "==");
//                params1.width = 500;
//                relative_1.setLayoutParams(params1);
//                relative_1.addView(tip_su);
                if (valuetemp > 0.0) {
                    return;
                }
                if (left.getVisibility() == View.GONE) {
                    left.setVisibility(View.VISIBLE);
                    right.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) left.getLayoutParams();
                    params.rightMargin = -getResources().getDimensionPixelOffset(R.dimen.right_margn);
                    left.setLayoutParams(params);
                    ViewHelper.setScaleX(left, 0.0f);
                } else {
                    right.setVisibility(View.VISIBLE);
                    left.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) right.getLayoutParams();
                    params.leftMargin = -getResources().getDimensionPixelOffset(R.dimen.right_margn);
                    right.setLayoutParams(params);
                    ViewHelper.setScaleX(right, 0.0f);
                }

                ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                               @Override
                                               public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                                   Float value = (Float) valueAnimator.getAnimatedValue();
                                                   if (valuetemp == value)
                                                       return;
                                                   valuetemp = value;
                                                   ViewHelper.setRotation(imageView, valuetemp * 360);
                                                   if (left.getVisibility() == View.GONE) {
                                                       ViewHelper.setTranslationX(right, right.getWidth() * value / 2);
                                                       ViewHelper.setScaleX(right, valuetemp);
                                                   } else {
                                                       ViewHelper.setTranslationX(left, -left.getWidth() * value / 2);
                                                       ViewHelper.setScaleX(left, valuetemp);
                                                   }

                                               }
                                           }

                );
                animator.setDuration(1000);
                animator.start();
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        valuetemp = 0.0f;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
//                Log.i("TAG", imageView.getWidth() + "==");
//                ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,left.getHeight(),left.getHeight());
//                scaleAnimation.setDuration(2000);
////                scaleAnimation.setRepeatCount(1);
//                scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//                left.startAnimation(scaleAnimation);
            }
        });
    }


}
