package com.fueltracker.UI;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class Loading extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		showAnimation();
		/*new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);

				} catch (Exception e) {
					
				}

				Intent myIntent = new Intent(Loading.this,FuelTracker.class);
				
				 startActivity(myIntent);
				 finish();
			}
		}.start();
		*/
	}

	private void showAnimation() {
		// TODO Auto-generated method stub
		
		ImageView logo1=(ImageView)findViewById(R.id.rotate1);
    	Animation fade1=AnimationUtils.loadAnimation(this, R.anim.loading_anim);//R.anim.fade_in
    	
    	
    	logo1.startAnimation(fade1);
    	
    	
    	fade1.setAnimationListener(new AnimationListener() {
			
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
		
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(Loading.this,FuelTracker.class);
				
				 startActivity(myIntent);
				 finish();
			}
		});
	}
}
