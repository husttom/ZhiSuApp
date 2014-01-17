package zhisu.mt.zhisuapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProductBillActivity extends Activity{
	
	private Button Productbill_backbtn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productbill_layout);
		
		Productbill_backbtn = (Button) findViewById(R.id.productbill_button_back);
		Productbill_backbtn.setOnClickListener(backbtn_Listener);
	}
	
	public Button.OnClickListener backbtn_Listener = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MainSourceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};
	
}
