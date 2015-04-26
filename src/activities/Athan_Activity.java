/*******************************Copyright Block*********************************
 *                                                                             *
 *                Sally Prayer Times Calculator (Final 1.2.15)                 *
 *           Copyright (C) 2015 http://www.sallyproject.altervista.org/        *
 *                         bibali1980@gmail.com                              *
 *                                                                             *
 *     This program is free software: you can redistribute it and/or modify    *
 *     it under the terms of the GNU General Public License as published by    *
 *      the Free Software Foundation, either version 3 of the License, or      *
 *                      (at your option) any later version.                    *
 *                                                                             *
 *       This program is distributed in the hope that it will be useful,       *
 *        but WITHOUT ANY WARRANTY; without even the implied warranty of       *
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the        *
 *                 GNU General Public License for more details.                *
 *                                                                             *
 *      You should have received a copy of the GNU General Public License      *
 *      along with this program.  If not, see http://www.gnu.org/licenses      *
 *******************************************************************************/
package activities;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import classes.ArabicReshape;
import classes.AthanService;
import classes.UserConfig;

import com.sally.R;
import com.sally.R.id;


public class Athan_Activity extends Activity {
	
	private TextView athan_label;
	private Button stop_athan_button;
	private int nextPrayerCode;
	private String language;
	private String athanType;
	private Typeface tf;
	private MediaPlayer mediaPlayer;
	private Vibrator vibrator;
	
	private TimerTask timerTask;
	private Timer timer;
	private int counter = 0;
	
	public static boolean STARTED = false;//if athan started or not

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.athan_activity);
		
		STARTED = true;
		
		changeMainBackground();
		
		this.mediaPlayer = new MediaPlayer();
		
		this.mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.stop();
				finish();
			}
		});
		
		Bundle localBundle = getIntent().getExtras();
		if(localBundle!=null){
			this.nextPrayerCode = localBundle.getInt("nextPrayerCode");
			this.language = localBundle.getString("language");
			this.athanType = localBundle.getString("athanType");
		}
		
		this.athan_label = (TextView)findViewById(id.athan_label);
		this.stop_athan_button = (Button)findViewById(id.stop_athan);
		
		String actualAthanTime ="";
    	switch (nextPrayerCode){
		case 1020:actualAthanTime = this.getResources().getString(R.string.shorouk);break;
		case 1021:actualAthanTime = this.getResources().getString(R.string.duhr);break;
		case 1022:actualAthanTime = this.getResources().getString(R.string.asr);break;
		case 1023:actualAthanTime = this.getResources().getString(R.string.maghrib);break;
		case 1024:actualAthanTime = this.getResources().getString(R.string.ishaa);break;
		case 1025:actualAthanTime = this.getResources().getString(R.string.fajr);break;
		default:break;
		}
    	if(language.equalsIgnoreCase("ar")){
    		tf = Typeface.createFromAsset(this.getAssets(), "arabic_font.ttf");
    		athan_label.setTypeface(tf);
    		athan_label.setText(ArabicReshape.reshape(getResources().getString(R.string.its_the_hour_of)+ " " + actualAthanTime));
    	    stop_athan_button.setTypeface(tf);
    	    stop_athan_button.setText(ArabicReshape.reshape(getResources().getString(R.string.stopathan)));
    	}
    	else{
    		athan_label.setText(getResources().getString(R.string.its_the_hour_of)+ " " + actualAthanTime);
    		stop_athan_button.setText(getResources().getString(R.string.stopathan));
    	}
		
		this.stop_athan_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mediaPlayer.isPlaying()){
					mediaPlayer.stop();
				}
				
				if(vibrator != null){
					vibrator.cancel();
				}
				if (timer != null) {
					timer.cancel();
			        timer.purge();
					}
				
				if (timerTask != null) {
					timerTask.cancel();
					}
			    
				finish();
			}
		});
		
		if(this.athanType.equalsIgnoreCase("athan")){
			playAthan();
		}
		else{
			this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			long[] pattern = {500,500,500};
			this.vibrator.vibrate(pattern , 0);
			this.timer = new Timer();
			this.timerTask = new TimerTask() {
				@Override
				public void run() {
					counter++;
					if(counter == 10){
						vibrator.cancel();
					}
					if(counter == 20){
						if(vibrator != null){
							vibrator.cancel();
						}
						if (timer != null) {
							timer.cancel();
					        timer.purge();
							}
						
						if (timerTask != null) {
							timerTask.cancel();
							}
						
						finish();
					}
				}
			};
		  this.timer.schedule(timerTask, 0, 1000);	
		}
	}
	
	public void playAthan() {
	    try {
	        if (mediaPlayer.isPlaying()) {
	        	mediaPlayer.stop();
	        	mediaPlayer.release();
	        	mediaPlayer = new MediaPlayer();
	        }
	        
	       String athanName = UserConfig.getSingleton().getAthan(); 
           String athan = "ali_ben_ahmed_mala.mp3";//athan path
            
            if(athanName.equalsIgnoreCase("ali_ben_ahmed_mala")){
            	athan = "ali_ben_ahmed_mala.mp3";
            }else{
            	if(athanName.equalsIgnoreCase("abd_el_basset_abd_essamad")){
                	athan = "abd_el_basset_abd_essamad.mp3";
                }else{
                	if(athanName.equalsIgnoreCase("farou9_abd_errehmane_hadraoui")){
                    	athan = "farou9_abd_errehmane_hadraoui.mp3";
                    }else{
                    	if(athanName.equalsIgnoreCase("mohammad_ali_el_banna")){
                        	athan = "mohammad_ali_el_banna.mp3";
                        }else{
                        	if(athanName.equalsIgnoreCase("mohammad_khalil_raml")){
                            	athan = "mohammad_khalil_raml.mp3";
                            }
                        }
                    }
                }  
            }
	        
	        AssetFileDescriptor descriptor = getAssets().openFd(athan);
	        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
	        descriptor.close();

	        mediaPlayer.prepare();
	        mediaPlayer.setLooping(false);
	        mediaPlayer.start();
	    } catch (Exception e) {
	    }
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(mediaPlayer.isPlaying()){
			mediaPlayer.stop();
		}
		if(vibrator != null){
			vibrator.cancel();
		}
		if (this.timer != null) {
			this.timer.cancel();
	        this.timer.purge();
			}
		
		if (this.timerTask != null) {
			this.timerTask.cancel();
			}
		finish();
	}
	
	public void changeMainBackground()
	{
		Drawable image;
		switch (AthanService.actualPrayerCode) {
   		case 1020:
   			image = getResources().getDrawable(R.drawable.shorou9_background); break;
   		case 1021:
   			image = getResources().getDrawable(R.drawable.duhr_background); break;
   		case 1022:
   			image = getResources().getDrawable(R.drawable.asr_background); break;
   		case 1023:
   			image = getResources().getDrawable(R.drawable.maghrib_background); break;
   		case 1024:
   			image = getResources().getDrawable(R.drawable.ishaa_background); break;
   		case 1025:
   			image = getResources().getDrawable(R.drawable.fajr_background); break;
   		default: image = getResources().getDrawable(R.drawable.maghrib_background); break;
   		}
		RelativeLayout layout = (RelativeLayout)this.findViewById(R.id.athanMainBackground);
		layout.setBackgroundDrawable(image);
	}
	

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
}
