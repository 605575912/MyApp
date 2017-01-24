/*
 * Copyright (C) 2011 VOV IO (http://vov.io/)
 */

package io.vov.android.vitamio.demo;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoViewDemo extends Activity {

	private String path = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "／tencent/QQfile_recv/27.mp4";
	private VideoView mVideoView;
	private AudioManager mAudioManager;
	
	private RelativeLayout gesture_volume_layout;// �������Ʋ���
	private TextView geture_tv_volume_percentage;// �����ٷֱ�
	private ImageView gesture_iv_player_volume;// ����ͼ��
	
	
	private RelativeLayout gesture_progress_layout;// ����ͼ�겼��
	private TextView geture_tv_progress_time;// ����ʱ�����
	private ImageView gesture_iv_progress;// �������˱�־

	private int maxVolume, currentVolume;
	private static final float STEP_PROGRESS = 2f;// �趨���Ȼ���ʱ�Ĳ���������ÿ�λ������ı䣬���¸ı����
	private static final float STEP_VOLUME = 2f;// Э����������ʱ�Ĳ���������ÿ�λ������ı䣬���¸ı����
	private boolean firstScroll = false;// ÿ�δ�����Ļ�󣬵�һ��scroll�ı�־
	
	private int GESTURE_FLAG = 0;// 1,���ڽ��ȣ�2����������
	private static final int GESTURE_MODIFY_PROGRESS = 1;
	private static final int GESTURE_MODIFY_VOLUME = 2;
	
	
	private GestureDetector mGestureDetector;
	private MediaController mediaController;
	
	  private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
	private int maxLength ;
	private int currentLentth;
	
	private static final float vsped = 150f;
	private static final float hsped = 150f;
	
	private static final float HORIZENTAOL = 0.2f;
	private static final float VITER = 0.2f;
	int screenWidth ;
	int screenHeitht;
	
	
	private long palyerCurrentPosition;  // �Ȳ��ŵĵ�ǰ��־������
	private long playerDuration ;// ������Դ��ʱ��������
	Context context;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
        context = this;
		setContentView(R.layout.videoview);
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		gesture_volume_layout = (RelativeLayout) findViewById(R.id.gesture_volume_layout);
		gesture_progress_layout = (RelativeLayout) findViewById(R.id.gesture_progress_layout);
		geture_tv_progress_time = (TextView) findViewById(R.id.geture_tv_progress_time);
		geture_tv_volume_percentage = (TextView) findViewById(R.id.geture_tv_volume_percentage);
		gesture_iv_progress = (ImageView) findViewById(R.id.gesture_iv_progress);
		gesture_iv_player_volume = (ImageView) findViewById(R.id.gesture_iv_player_volume);
		
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		Log.i("TAG","path"+path);
		mVideoView.setVideoPath(path);
		mediaController = new MediaController(this);
		mVideoView.setMediaController(mediaController);
		mVideoView.requestFocus();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics( dm );
		screenWidth = dm.widthPixels;
		screenHeitht = dm.heightPixels;
		maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mGestureDetector = new GestureDetector(this, new MyGestectoroListener());

	}

	class MyGestectoroListener extends SimpleOnGestureListener {
	
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (firstScroll) {// �Դ�����Ļ���һ�λ���Ϊ��׼����������Ļ�ϲ����л�����
				// ����ľ���仯����������ȣ�����ı仯�����������
				if (Math.abs(distanceX) >= Math.abs(distanceY)) {
					gesture_volume_layout.setVisibility(View.INVISIBLE);
					gesture_progress_layout.setVisibility(View.VISIBLE);
					GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
				} else {
					gesture_volume_layout.setVisibility(View.VISIBLE);
					gesture_progress_layout.setVisibility(View.INVISIBLE);
					GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
				}
			}
			palyerCurrentPosition = mVideoView.getCurrentPosition();
			playerDuration = mVideoView.getDuration();
			
			// ���ÿ�δ�����Ļ���һ��scroll�ǵ��ڽ��ȣ���֮���scroll�¼��������������ȣ�ֱ���뿪��Ļִ����һ�β���
			if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {
				// distanceX=lastScrollPositionX-currentScrollPositionX�����Ϊ��ʱ�ǿ��
				if (Math.abs(distanceX) > Math.abs(distanceY)) {// �����ƶ����������ƶ�
					if (distanceX >= DensityUtil.dip2px(context, STEP_PROGRESS)) {// ���ˣ��ò������Ƹı��ٶȣ���΢��
						gesture_iv_progress.setImageResource(R.drawable.souhu_player_backward);
						if (palyerCurrentPosition > 3 * 1000) {// ����Ϊ��
							palyerCurrentPosition -= 3 * 1000;// scroll����ִ��һ�ο���3��
						} else {
							palyerCurrentPosition = 3 * 1000;
						}
					} else if (distanceX <= -DensityUtil.dip2px(context, STEP_PROGRESS)) {// ���
						gesture_iv_progress.setImageResource(R.drawable.souhu_player_forward);
						if (palyerCurrentPosition < playerDuration - 16 * 1000) {// ���ⳬ����ʱ��
							palyerCurrentPosition += 3 * 1000;// scrollִ��һ�ο��3��
						} else {
							palyerCurrentPosition = playerDuration - 10 * 1000;
						}
					}
				}

				geture_tv_progress_time.setText(converLongTimeToStr(palyerCurrentPosition) + "/"
						+ converLongTimeToStr(playerDuration));
				mVideoView.seekTo(palyerCurrentPosition);

			}
			// ���ÿ�δ�����Ļ���һ��scroll�ǵ�����������֮���scroll�¼��������������ڣ�ֱ���뿪��Ļִ����һ�β���
			else if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {
				currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // ��ȡ��ǰֵ
				if (Math.abs(distanceY) > Math.abs(distanceX)) {// �����ƶ����ں����ƶ�
					if (distanceY >= DensityUtil.dip2px(context, STEP_VOLUME)) {// ��������,ע�����ʱ��������ϵ,�������Ͻ���ԭ�㣬���������ϻ���ʱdistanceYΪ��
						if (currentVolume < maxVolume) {// Ϊ������ڹ��죬distanceYӦ����һ���趨ֵ
							currentVolume++;
						}
						gesture_iv_player_volume.setImageResource(R.drawable.souhu_player_volume);
					} else if (distanceY <= -DensityUtil.dip2px(context, STEP_VOLUME)) {// ������С
						if (currentVolume > 0) {
							currentVolume--;
							if (currentVolume == 0) {// �������趨�������е�ͼƬ
								gesture_iv_player_volume.setImageResource(R.drawable.souhu_player_silence);
							}
						}
					}
					int percentage = (currentVolume * 100) / maxVolume;
					geture_tv_volume_percentage.setText(percentage + "%");
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
				}

			}

			firstScroll = false;// ��һ��scrollִ����ɣ��޸ı�־
			return false;
		}

		  /** ˫�� */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
            else
                mLayout++;
            if (mVideoView != null)
                mVideoView.setVideoLayout(mLayout, 0);
            return true;
        }
		
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
		     firstScroll = true;
		     
			return false;
		}
		
		

	}

	
	/**
	 * ת���������ɡ��֡��롱���硰01:53����������60��������ʾ��ʱ���֡��롱���硰01:01:30
	 * 
	 * @param ��ת���ĺ�����
	 * */
	private String converLongTimeToStr(long time) {
		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;

		long hour = (time) / hh;
		long minute = (time - hour * hh) / mi;
		long second = (time - hour * hh - minute * mi) / ss;

		String strHour = hour < 10 ? "0" + hour : "" + hour;
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String strSecond = second < 10 ? "0" + second : "" + second;
		if (hour > 0) {
			return strHour + ":" + strMinute + ":" + strSecond;
		} else {
			return strMinute + ":" + strSecond;
		}
	}
	


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			endGesture();
			break;

		default:
			break;
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
			case 0:
				GESTURE_FLAG = 0;// ��ָ�뿪��Ļ�����õ�����������ȵı�־
				gesture_volume_layout.setVisibility(View.INVISIBLE);
				gesture_progress_layout.setVisibility(View.INVISIBLE);
				break;

			default:
				break;
			}
		};
	};

	/** ���ƽ��� */
	private void endGesture() {

		// ����
		handler.removeMessages(0);
		handler.sendEmptyMessageDelayed(0, 500);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		playerDuration =mVideoView.getDuration();
		Log.i("tag", "onresume���ܳ���"+playerDuration);
		super.onResume();
	}
}
