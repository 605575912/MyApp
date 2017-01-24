//package com.library.uiframe.view;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.FrameLayout;
//
//import com.nostra13.universalimageloader.ServiceLoader;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Photoframe extends FrameLayout {
//	List<Object> list = new ArrayList<Object>();
//	int curritem = 0;
//	View topView;
//	int delay = 5;
//	int offset = 15;
//	onListener onlistener;
//	int defaultview = 0;
//
//	public interface onListener {
//		View addviewListener(int position, Object data);
//
//		void error(int tag, String string);
//
//		void onPageScrolled(View childview, int dx);
//
//		void onClick(View childview, int dx);
//	}
//
//	public Photoframe(Context context) {
//		super(context);
//		// TODO Auto-generated constructor stub
//		init();
//	}
//
//	public void setOnlListener(onListener onlistener) {
//		this.onlistener = onlistener;
//	}
//
//	public List<Object> getList() {
//		return list;
//	}
//
//	public void setList(List<Object> list) {
//		this.list = list;
//	}
//
//	void init() {
//		android.view.ViewGroup.LayoutParams layoutParams = new LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		setLayoutParams(layoutParams);
//	}
//
//	public void invalidateList(int curritem, int show) {
//		defaultview = getChildCount();
//		if (curritem >= list.size()) {
//			curritem = 0;
//		}
//		int showsize = 1;
//		showsize = list.size() - curritem;
//		if (showsize > show) {
//			showsize = show;
//		}
//		for (int i = curritem; i < curritem + showsize; i++) {
//			if (onlistener != null) {
//				View childview = onlistener.addviewListener(i, list.get(i));
//				if (childview != null) {
//					addView(childview, defaultview);
//				}
//			}
//
//		}
//
//		this.curritem = curritem;
//	}
//
//	float x = 0;
//	int sumx = 0;
//	int addview = 0; // 0 左 1 增加
//	int dx = 0;
//	long downtime = 0;
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		if (list == null && list.isEmpty()) {
//			return super.onTouchEvent(event);
//		}
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN: {
//
//			if (isruning) {
//				return true;
//			}
//			downtime = System.currentTimeMillis();
//			x = event.getX();
//			if (topView != null) {
//				topView.scrollTo(0, 0);
//			}
//			addview = 0;
//			sumx = 0;
//			return true;
//		}
//		case MotionEvent.ACTION_MOVE: {
//			if (isruning) {
//				return true;
//			}
//			int count = getChildCount();
//			float tempx = event.getX();
//			dx = (int) (tempx - x);
//			if (dx == 0) {
//				break;
//			}
//			if (dx > 0) {
//				if (addview == 1) {
//					if (topView != null) {
//						sumx = getWidth() - dx;
//						if (sumx < 0) {
//							sumx = 0;
//						}
//						if (onlistener != null) {
//							onlistener.onPageScrolled(topView, sumx);
//						}
//
//					}
//
//				} else {
//					if (addview == 0) {
//						sumx = 0;
//						if (onlistener != null && topView != null) {
//							onlistener.onPageScrolled(topView, sumx);
//						}
//						if (curritem > 0) {
//							addview = 1;
//							if (onlistener != null) {
//								curritem--;
//								View childview = onlistener.addviewListener(
//										curritem, list.get(curritem));
//								if (childview != null) {
//									if (count > defaultview + 3) {
//										removeViewAt(defaultview);
//									}
//									addView(childview, getChildCount());
//									topView = getChildAt(getChildCount() - 1);
//									sumx = getWidth();
//									topView.scrollTo(sumx, 0);
//								}
//							}
//						}
//					} else {
//						if (count > defaultview) {
//							if (sumx != 0) {
//								sumx = 0;
//								if (onlistener != null) {
//									onlistener.onPageScrolled(topView, sumx);
//								}
//							}
//
//						}
//					}
//
//				}
//
//			} else {
//				if (addview == 1) {
//					if (topView != null) {
//						removeView(topView);
//						topView = null;
//					}
//
//				} else {
//
//					if (count > defaultview) {
//						if (topView == null) {
//							topView = getChildAt(count - 1);
//						}
//						sumx = -dx;
//						if (onlistener != null) {
//							onlistener.onPageScrolled(topView, sumx);
//						}
//					}
//				}
//
//			}
//		}
//			break;
//		case MotionEvent.ACTION_UP: {
//
//			if (isruning) {
//				return true;
//			}
//			if (addview == 0 && sumx == 0) {
//				if (Math.abs(dx) < 30) {
//					long time = System.currentTimeMillis();
//					if (time - downtime < 1000) {
//						if (onlistener != null) {
//							onlistener.onClick(topView, sumx);
//						}
//						break;
//					}
//
//				}
//
//			}
//			// int count = getChildCount();
//			if (topView != null) {
//
//				if (addview == 1) {
//
//					if (sumx < 0) {
//					} else {
//						if (sumx < getWidth() * 3 / 4) {
//							handler.post(new Runnable() {
//
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									ServiceLoader.getInstance().submit(
//											addrunnable);
//								}
//							});
//						} else {
//
//							if (!isruning) {
//								handler.post(new Runnable() {
//
//									@Override
//									public void run() {
//										// TODO Auto-generated method stub
//										ServiceLoader.getInstance().submit(
//												addbackrunnable);
//									}
//								});
//							}
//
//						}
//					}
//				} else {
//
//					if (sumx > 0) {
//						if (sumx >= getWidth() / 4) {
//							handler.post(new Runnable() {
//
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									ServiceLoader.getInstance().submit(
//											leftrunnable);
//								}
//							});
//
//						} else {
//							if (!isruning) {
//								handler.post(new Runnable() {
//
//									@Override
//									public void run() {
//										// TODO Auto-generated method stub
//										ServiceLoader.getInstance().submit(
//												backrunnable);
//									}
//								});
//							}
//						}
//					} else {
//
//						// if (sumx > getWidth() * 5 / 4) {
//						// // removeView(topView);
//						// // if (curritem == 0) {
//						// // if (curritem < list.size() - 1) {
//						// // curritem++;
//						// // View view = inflater.inflate(
//						// // R.layout.photolayout, null);
//						// // ImageView imageView = (ImageView) view
//						// // .findViewById(R.id.iv);
//						// // ServiceLoader.getInstance().displayImage(
//						// // imageOptions, list.get(curritem),
//						// // getContext(), imageView);
//						// // addView(view, 0);
//						// // }
//						// // }
//						// } else {
//						// if (!isruning) {
//						// handler.post(new Runnable() {
//						//
//						// @Override
//						// public void run() {
//						// // TODO Auto-generated method stub
//						// ServiceLoader.getInstance().submit(
//						// backrunnable);
//						// }
//						// });
//						// }
//						//
//						// }
//					}
//				}
//
//			}
//		}
//			break;
//		}
//		return super.onTouchEvent(event);
//	}
//
//	boolean isruning = false;
//	Runnable leftrunnable = new Runnable() {
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			isruning = true;
//			int i = sumx;
//			while (sumx < getWidth() && isruning) {
//				Message msg = handler.obtainMessage(1);
//				i = i + offset;
//				msg.obj = i;
//				if (i >= getWidth()) {
//					handler.sendEmptyMessage(0);
//					break;
//				}
//				try {
//					Thread.sleep(delay);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				handler.sendMessage(msg);
//
//			}
//		}
//	};
//	Runnable addrunnable = new Runnable() {
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			isruning = true;
//			int i = sumx;
//			while (sumx > 0 && isruning) {
//				Message msg = handler.obtainMessage(1);
//				i = i - offset;
//				msg.obj = i;
//				if (i <= offset) {
//					Message msgs = handler.obtainMessage(1);
//					msgs.obj = 0;
//					handler.sendMessage(msgs);
//					break;
//				}
//				try {
//					Thread.sleep(delay);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				handler.sendMessage(msg);
//
//			}
//		}
//	};
//	Runnable backrunnable = new Runnable() {
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			isruning = true;
//			int i = sumx;
//			while (sumx > 0 && isruning) {
//				Message msg = handler.obtainMessage(1);
//				i = i - offset;
//				msg.obj = i;
//				if (i <= offset) {
//					sumx = 0;
//					isruning = false;
//					Message msgs = handler.obtainMessage(1);
//					msgs.obj = 0;
//					handler.sendMessage(msgs);
//					break;
//				}
//				try {
//					Thread.sleep(delay);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				handler.sendMessage(msg);
//
//			}
//		}
//	};
//	Runnable addbackrunnable = new Runnable() {
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			isruning = true;
//			int i = sumx;
//			while (sumx < getWidth() && isruning) {
//				Message msg = handler.obtainMessage(1);
//				i = i + offset;
//				msg.obj = i;
//				if (i >= getWidth()) {
//					sumx = 0;
//					handler.sendEmptyMessage(0);
//					break;
//				}
//				try {
//					Thread.sleep(delay);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				handler.sendMessage(msg);
//
//			}
//		}
//	};
//	Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			if (msg.what == 0) {
//				remove();
//			} else if (msg.what == 1) {
//				int x = (Integer) msg.obj;
//				if (x - 1 < 1) {
//					isruning = false;
//				}
//				if (topView != null) {
//					if (onlistener != null) {
//						onlistener.onPageScrolled(topView, x);
//					}
//				}
//			}
//		};
//	};
//
//	void remove() {
//
//		if (topView != null) {
//			removeView(topView);
//			topView = null;
//			curritem++;
//			int index = curritem + getChildCount() - defaultview;
//			if (index < list.size()) {
//				if (onlistener != null) {
//					View childview = onlistener.addviewListener(index,
//							list.get(index));
//					if (childview != null) {
//						addView(childview, defaultview);
//					}
//
//				}
//
//			}
//		}
//		isruning = false;
//	}
//
//}
