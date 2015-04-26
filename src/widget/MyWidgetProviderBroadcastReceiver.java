package widget;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import classes.ArabicReshape;
import classes.AthanService;
import classes.UserConfig;

import com.sally.R;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.widget.RemoteViews;

public class MyWidgetProviderBroadcastReceiver extends BroadcastReceiver{

	private NumberFormat formatter = new DecimalFormat("00");
	private PowerManager pm;
	private WakeLock wl;
	
	@Override
	 public void onReceive(Context context, Intent intent) {
	  this.pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	  this.wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWidgetProviderBroadcastReceiver");
	  //Acquire the lock
	  this.wl.acquire();
	 
	  //update the widget
	   if(!AthanService.STARTED)
		{
			context.startService(new Intent(context,AthanService.class));
		}
	    else{
	      pushWidgetUpdate(context);
	    }
	 }
	
	public void pushWidgetUpdate(Context context) 
	  {
  	   try{
  	   if(AthanService.missing_hours_to_nextPrayer == 0 && AthanService.missing_minutes_to_nextPrayer == 0 && AthanService.missing_seconds_to_nextPrayer == 0)
			{
  	 		  RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
  	 		  
 				if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar")){
 					remoteViews.setTextViewText(R.id.missing_to, ArabicReshape.reshape(context.getResources().getString(R.string.its_the_hour_of)));
 			        remoteViews.setTextViewText(R.id.missing_salat, ArabicReshape.reshape(getNextPrayerName(context)));
 			        remoteViews.setTextViewText(R.id.missing_time, ArabicReshape.reshape(getNextPrayerName(context))); 
 				}else{
 					remoteViews.setTextViewText(R.id.missing_to, context.getResources().getString(R.string.its_the_hour_of));
 					remoteViews.setTextViewText(R.id.missing_salat, getNextPrayerName(context));
 			        remoteViews.setTextViewText(R.id.missing_time, getNextPrayerName(context)); 
 				}
 				
				if(AthanService.actualPrayerCode == 1025)
				{
					setActualPrayerTextColor(remoteViews , 1020);
				}
				else
				{
					setActualPrayerTextColor(remoteViews , AthanService.actualPrayerCode + 1);
				}
 				
 				ComponentName myWidget = new ComponentName(context, MyWidgetProvider.class);
 			    AppWidgetManager manager = AppWidgetManager.getInstance(context);
 			    manager.updateAppWidget(myWidget, remoteViews);	
			    //Release the lock
				this.wl.release();
			}
			else
			{
	 		    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
	 		    
	 		    if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar"))
	 				{
	 			        remoteViews.setTextViewText(R.id.missing_time, formatter.format(AthanService.missing_hours_to_nextPrayer)+":"+formatter.format(AthanService.missing_minutes_to_nextPrayer)+":"+formatter.format(AthanService.missing_seconds_to_nextPrayer));
	 			        remoteViews.setTextViewText(R.id.missing_to, ArabicReshape.reshape(context.getResources().getString(R.string.missing_to)));
	 			        remoteViews.setTextViewText(R.id.missing_salat, ArabicReshape.reshape(getNextPrayerName(context)));
	 					
	 			        remoteViews.setTextViewText(R.id.fajrTime, AthanService.prayerTimes.getFajrFinalTime());
	 			        remoteViews.setTextViewText(R.id.shoroukTime, AthanService.prayerTimes.getShorou9FinalTime());
	 			        remoteViews.setTextViewText(R.id.duhrTime, AthanService.prayerTimes.getDuhrFinalTime());
	 			        remoteViews.setTextViewText(R.id.asrTime, AthanService.prayerTimes.getAsrFinalTime());
	 			        remoteViews.setTextViewText(R.id.maghribTime, AthanService.prayerTimes.getMaghribFinalTime());
	 			        remoteViews.setTextViewText(R.id.ishaaTime, AthanService.prayerTimes.getIshaaFinalTime());
	 			        
	 					ComponentName myWidget = new ComponentName(context, MyWidgetProvider.class);
	 				    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	 				    manager.updateAppWidget(myWidget, remoteViews);	
	 				   //Release the lock
	 				   this.wl.release();
	 				}
	 				else
	 				{
	 					remoteViews.setTextViewText(R.id.missing_to, context.getResources().getString(R.string.missing_to));
	 			        remoteViews.setTextViewText(R.id.missing_salat, getNextPrayerName(context));
	 			        remoteViews.setTextViewText(R.id.missing_time, formatter.format(AthanService.missing_hours_to_nextPrayer)+":"+formatter.format(AthanService.missing_minutes_to_nextPrayer)+":"+formatter.format(AthanService.missing_seconds_to_nextPrayer));
	 					
	 			        remoteViews.setTextViewText(R.id.fajrTime, AthanService.prayerTimes.getFajrFinalTime());
	 			        remoteViews.setTextViewText(R.id.shoroukTime, AthanService.prayerTimes.getShorou9FinalTime());
	 			        remoteViews.setTextViewText(R.id.duhrTime, AthanService.prayerTimes.getDuhrFinalTime());
	 			        remoteViews.setTextViewText(R.id.asrTime, AthanService.prayerTimes.getAsrFinalTime());
	 			        remoteViews.setTextViewText(R.id.maghribTime, AthanService.prayerTimes.getMaghribFinalTime());
	 			        remoteViews.setTextViewText(R.id.ishaaTime, AthanService.prayerTimes.getIshaaFinalTime());
	 			        
	 			       setActualPrayerTextColor(remoteViews , AthanService.actualPrayerCode);
	 			       
	 					ComponentName myWidget = new ComponentName(context, MyWidgetProvider.class);
	 				    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	 				    manager.updateAppWidget(myWidget, remoteViews);	
		 			   //Release the lock
	 				   this.wl.release();
	 				}
			}
	  }catch(Exception ex){this.wl.release();}
	  }

	private void setActualPrayerTextColor(RemoteViews remoteViews , int actualPrayercode){
        remoteViews.setInt(R.id.fajr_label, "setTextColor", android.graphics.Color.WHITE);
        remoteViews.setInt(R.id.fajrTime, "setTextColor", android.graphics.Color.WHITE);
		remoteViews.setInt(R.id.shorouk_label, "setTextColor", android.graphics.Color.WHITE);
        remoteViews.setInt(R.id.shoroukTime, "setTextColor", android.graphics.Color.WHITE);
		remoteViews.setInt(R.id.duhr_label, "setTextColor", android.graphics.Color.WHITE);
        remoteViews.setInt(R.id.duhrTime, "setTextColor", android.graphics.Color.WHITE);
		remoteViews.setInt(R.id.asr_label, "setTextColor", android.graphics.Color.WHITE);
        remoteViews.setInt(R.id.asrTime, "setTextColor", android.graphics.Color.WHITE);
		remoteViews.setInt(R.id.maghrib_label, "setTextColor", android.graphics.Color.WHITE);
        remoteViews.setInt(R.id.maghribTime, "setTextColor", android.graphics.Color.WHITE);
		remoteViews.setInt(R.id.ishaa_label, "setTextColor", android.graphics.Color.WHITE);
        remoteViews.setInt(R.id.ishaaTime, "setTextColor", android.graphics.Color.WHITE);
    	switch (actualPrayercode) {
   		case 1020:
		        remoteViews.setInt(R.id.fajr_label, "setTextColor", android.graphics.Color.BLUE);
		        remoteViews.setInt(R.id.fajrTime, "setTextColor", android.graphics.Color.BLUE);break;
   		case 1021:
   			remoteViews.setInt(R.id.shorouk_label, "setTextColor", android.graphics.Color.BLUE);
	        remoteViews.setInt(R.id.shoroukTime, "setTextColor", android.graphics.Color.BLUE);break;
   		case 1022:
   			remoteViews.setInt(R.id.duhr_label, "setTextColor", android.graphics.Color.BLUE);
	        remoteViews.setInt(R.id.duhrTime, "setTextColor", android.graphics.Color.BLUE);break;
   		case 1023:
   			remoteViews.setInt(R.id.asr_label, "setTextColor", android.graphics.Color.BLUE);
	        remoteViews.setInt(R.id.asrTime, "setTextColor", android.graphics.Color.BLUE);break;
   		case 1024:
   			remoteViews.setInt(R.id.maghrib_label, "setTextColor", android.graphics.Color.BLUE);
	        remoteViews.setInt(R.id.maghribTime, "setTextColor", android.graphics.Color.BLUE);break;
   		case 1025:
   			remoteViews.setInt(R.id.ishaa_label, "setTextColor", android.graphics.Color.BLUE);
	        remoteViews.setInt(R.id.ishaaTime, "setTextColor", android.graphics.Color.BLUE);break;
   		default:break;
   		}
	}
	  
	 private String getNextPrayerName(Context context){
		String nextPrayerName = context.getString(R.string.not_set);
    	switch (AthanService.actualPrayerCode) {
   		case 1020:
   			nextPrayerName = context.getString(R.string.shorouk); break;
   		case 1021:
   			nextPrayerName = context.getString(R.string.duhr); break;
   		case 1022:
   			nextPrayerName = context.getString(R.string.asr); break;
   		case 1023:
   			nextPrayerName = context.getString(R.string.maghrib); break;
   		case 1024:
   			nextPrayerName = context.getString(R.string.ishaa); break;
   		case 1025:
   			nextPrayerName = context.getString(R.string.fajr); break;
   		default: context.getString(R.string.not_set); break;
   		}
    	return nextPrayerName;
	} 
	 
}
