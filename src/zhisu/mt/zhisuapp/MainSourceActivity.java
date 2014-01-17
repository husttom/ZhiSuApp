package zhisu.mt.zhisuapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainSourceActivity extends Activity implements OnGestureListener,
		OnTouchListener {

	private ViewFlipper viewFlipper;
	private boolean showNext = true;
	private boolean isRun = true;
	private int currentPage = 0;
	private final int SHOW_NEXT = 0011;
	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;
	private GestureDetector mGestureDetector;

	MainScrollView myScrollView;

	private RelativeLayout Warehouse_layout;
	private RelativeLayout ExtraCode_layout;
	private RelativeLayout ProductBill_layout;

	private ImageButton footbar_settings_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_source_layout);

		viewFlipper = (ViewFlipper) findViewById(R.id.main_viewflipper);
		Warehouse_layout = (RelativeLayout) findViewById(R.id.mainmetro_one);
		ExtraCode_layout = (RelativeLayout) findViewById(R.id.mainmetro_two);
		ProductBill_layout = (RelativeLayout) findViewById(R.id.mainmetro_three);

		footbar_settings_btn = (ImageButton) findViewById(R.id.main_footbar_settings);

		mGestureDetector = new GestureDetector(this);
		viewFlipper.setOnTouchListener(this);
		viewFlipper.setLongClickable(true);
		viewFlipper.setOnClickListener(clickListener);
		displayRatio_selelct(currentPage);

		myScrollView = (MainScrollView) findViewById(R.id.viewflipper_scrollview);
		myScrollView.setGestureDetector(mGestureDetector);

		Warehouse_layout.setOnClickListener(Warehouse_Listener);
		ExtraCode_layout.setOnClickListener(ExtraCode_Listener);
		ProductBill_layout.setOnClickListener(ProductBill_Listener);

		footbar_settings_btn.setOnClickListener(morebtn_Listener);

		thread.start();
	}

	private OnClickListener Warehouse_Listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), WarehouseActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};

	private OnClickListener ExtraCode_Listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ExtraCodeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};

	private OnClickListener ProductBill_Listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ProductBillActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};

	public Button.OnClickListener morebtn_Listener = new Button.OnClickListener() {

		public void onClick(View v) {

			Intent m_intent = new Intent();
			// m_intent.setClass(getApplicationContext(), MoreActivity.class);
			// startActivity(m_intent);
		}
	};

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			toastInfo("µã»÷ÊÂ¼þ");

		}
	};

	private OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			return mGestureDetector.onTouchEvent(event);
		}
	};

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SHOW_NEXT:
				if (showNext) {
					showNextView();
				} else {
					showPreviousView();
				}
				break;

			default:
				break;
			}
		}
	};

	Thread thread = new Thread() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isRun) {
				try {
					Thread.sleep(1000 * 5);
					Message msg = new Message();
					msg.what = SHOW_NEXT;
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	};

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.w("view", "onFling");
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			Log.w("fling", "left");
			showNextView();
			showNext = true;
			// return true;
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			Log.w("fling", "right");
			showPreviousView();
			showNext = false;
			// return true;
		}
		return false;

	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	private void showNextView() {

		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_out));
		viewFlipper.showNext();
		currentPage++;
		if (currentPage == viewFlipper.getChildCount()) {
			displayRatio_normal(currentPage - 1);
			currentPage = 0;
			displayRatio_selelct(currentPage);
		} else {
			displayRatio_selelct(currentPage);
			displayRatio_normal(currentPage - 1);
		}
		Log.w("currentPage", currentPage + "");

	}

	private void showPreviousView() {
		displayRatio_selelct(currentPage);
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_out));
		viewFlipper.showPrevious();
		currentPage--;
		if (currentPage == -1) {
			displayRatio_normal(currentPage + 1);
			currentPage = viewFlipper.getChildCount() - 1;
			displayRatio_selelct(currentPage);
		} else {
			displayRatio_selelct(currentPage);
			displayRatio_normal(currentPage + 1);
		}
		Log.w("currentPage", currentPage + "");
	}

	private void displayRatio_selelct(int id) {
		int[] ratioId = { R.id.home_ratio_img_04, R.id.home_ratio_img_03,
				R.id.home_ratio_img_02, R.id.home_ratio_img_01 };
		ImageView img = (ImageView) findViewById(ratioId[id]);
		img.setSelected(true);
	}

	private void displayRatio_normal(int id) {
		int[] ratioId = { R.id.home_ratio_img_04, R.id.home_ratio_img_03,
				R.id.home_ratio_img_02, R.id.home_ratio_img_01 };
		ImageView img = (ImageView) findViewById(ratioId[id]);
		img.setSelected(false);
	}

	private void toastInfo(String string) {
		Toast.makeText(MainSourceActivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			MainSourceActivity.this.finish();

			System.exit(0);

			// int nPid = android.os.Process.myPid();
			// android.os.Process.killProcess(nPid);

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_source, menu);
		return true;
	}

}
