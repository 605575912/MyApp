package com.library.uiframe.view;//package com.support.serviceloader.view;
//
//import android.os.Handler;
//import android.os.Message;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnTouchListener;
//
//import com.support.serviceloader.util.Swipe;
//
//public class Aninationt {
//	private int RightViewWidth = 0;
//	private float mFirstX, mFirstY;
//	int endx = 0;
//	private View mPreItemView;
//	int delayed = 10;
//	Swipe oldSwipe;
//	private View mCurrentItemView;
//	onListener listener;
//	long ondown = 0;
//	Handler handler = new Handler() {
//		@Override
//		public void handleMessage(android.os.Message msg) {
//			if (msg.what == 0) {
//				View view = (View) msg.obj;
//				int sx = view.getScrollX();
//				if (sx > 0) {
//					int de = sx - 10;
//					if (de > 0) {
//						view.scrollTo(de, 0);
//						Message msgs = handler.obtainMessage();
//						msgs.obj = view;
//						msgs.arg1 = sx;
//						msgs.what = 0;
//						handler.sendMessageDelayed(msgs, delayed);
//					} else {
//						view.scrollTo(0, 0);
//					}
//
//				} else {
//					view.scrollTo(0, 0);
//				}
//
//			} else if (msg.what == 1) {
//				View view = (View) msg.obj;
//				int sx = view.getScrollX();
//				if (view.getWidth() > sx) {
//					int de = sx + 25;
//					if (view.getWidth() > de) {
//						view.scrollTo(de, 0);
//						Message msgs = handler.obtainMessage();
//						msgs.obj = view;
//						msgs.arg1 = sx;
//						msgs.what = 1;
//						handler.sendMessageDelayed(msgs, delayed);
//					} else {
//
//						if (listener != null) {
//							listener.onfinishListener(oldSwipe);
//							view.scrollTo(0, 0);
//						}
//					}
//
//				} else {
//					if (listener != null) {
//						listener.onfinishListener(oldSwipe);
//						view.scrollTo(0, 0);
//					}
//
//				}
//			}
//
//		};
//	};
//
//	public int getRightViewWidth() {
//		return RightViewWidth;
//	}
//
//	public Aninationt(int RightViewWidth, onListener listener) {
//		this.RightViewWidth = RightViewWidth;
//		this.listener = listener;
//	}
//
//	public onListener getListener() {
//		return listener;
//	}
//
//	public void setListener(onListener listener) {
//		this.listener = listener;
//	}
//
//	public View setlist(View view, final Object object) {
//		if (object instanceof Swipe) {
//			view.setLongClickable(true);
//			if (oldSwipe == null) {
//				oldSwipe = (Swipe) object;
//			}
//			mCurrentItemView = view;
//			if (((Swipe) object).getTag() == 1) {
//				mCurrentItemView.scrollTo(RightViewWidth, 0);
//				mPreItemView = mCurrentItemView;
//			} else {
//				if (mPreItemView != null) {
//					mCurrentItemView.scrollTo(0, 0);
//					// HidenRight(view);
//				}
//			}
//			mCurrentItemView.setOnTouchListener(new OnTouchListener() {
//
//				@Override
//				public boolean onTouch(View view, MotionEvent event) {
//					// TODO Auto-generated method stub
//
//					float lastX = event.getRawX();
//					float lastY = event.getRawY();
//					switch (event.getAction()) {
//					case MotionEvent.ACTION_DOWN: {
//						mFirstX = lastX;
//						mFirstY = lastY;
//						ondown = System.currentTimeMillis();
//					}
//						break;
//					case MotionEvent.ACTION_MOVE: {
//						float dx = lastX - mFirstX;
//						float dy = lastY - mFirstY;
//						if (Math.abs(dx) > 8 || Math.abs(dy) > 8) {
//							ondown = 0;
//						}
//						if (0 <= dx && dx < RightViewWidth) {
//							if (view.getScrollX() > 0) {
//								if (endx == 0) {
//									endx = view.getScrollX();
//								}
//								if (mPreItemView != null) {
//									if (oldSwipe.getTag() == 1) {
//										HidenRight(mPreItemView);
//										oldSwipe.setTag(0);
//									}
//								}
//								view.scrollTo((int) (endx - dx), 0);
//							}
//						} else {
//
//							if (Math.abs(dx) >= 5
//									&& RightViewWidth >= Math.abs(dx)) { // showright
//
//								if (mPreItemView != null) {
//									if (oldSwipe.getTag() == 1) {
//										HidenRight(mPreItemView);
//										oldSwipe.setTag(0);
//									}
//								}
//
//								view.scrollTo((int) -dx, 0);
//
//							}
//						}
//					}
//						break;
//					case MotionEvent.ACTION_CANCEL:
//					case MotionEvent.ACTION_UP: {
//						int sx = view.getScrollX();
//						if (sx < (RightViewWidth / 2)) {
//							HidenRight(view);
//						} else {
//							view.scrollTo(RightViewWidth, 0);
//							oldSwipe = (Swipe) object;
//							oldSwipe.setTag(1);
//							mPreItemView = view;
//						}
//						if (System.currentTimeMillis() - ondown < 300) {
//							listener.onClick(view, object);
//						}
//					}
//
//					default:
//						break;
//					}
//					return false;
//				}
//			});
//		}
//		return view;
//	}
//
//	void HidenRight(View view) {
//		if (view == null) {
//			return;
//		}
//		Message msg = handler.obtainMessage();
//		msg.obj = view;
//		msg.arg1 = view.getScrollX();
//		msg.what = 0;
//		handler.sendMessage(msg);
//	}
//
//	public void DeleteRight(View view) {
//
//		if (view == null) {
//			return;
//		}
//		Message msg = handler.obtainMessage();
//		msg.obj = view;
//		msg.arg1 = view.getScrollX();
//		msg.what = 1;
//		handler.sendMessage(msg);
//	}
//
//	public void HidenRight(View view, Swipe object) {
//		HidenRight(view);
//		if (object != null) {
//			object.setTag(0);
//		}
//	}
//
//	public interface onListener {
//		void onfinishListener(Object object);
//
//		void onEditClick(View v, Object object);
//
//		void onClick(View v, Object object);
//	}
//
//}
