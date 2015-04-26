package classes;

import java.util.Calendar;
import widget.MyWidgetProvider;
import activities.Athan_Activity;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class AthanServiceBroasdcastReceiver extends BroadcastReceiver{


	private int totalMinutes;//next prayer time in minutes

    private int hour;// hour
    private int minutes;// minutes
    private int second;// seconds
    
	private boolean isTimePlus1 = true;
	
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
		    	refreshTime(context);
		    }
	}
	
	public void refreshTime(Context context)
	{
		try {
			   Calendar calendar = Calendar.getInstance();
	           hour = calendar.get(Calendar.HOUR_OF_DAY);
	           minutes = calendar.get(Calendar.MINUTE);
	           second = calendar.get(Calendar.SECOND);
	           
	           totalMinutes = (hour * 60) + minutes;
	        
             if (AthanService.isAfterDay(calendar, AthanService.calendar)) {//if hour is 24H calculate prayer times for next day
             	refreshDay(context);
             	AthanService.calendar = calendar;
             }
             else
             {
             if (totalMinutes == AthanService.nextPrayerTimeInMinutes) {//if actual hour and actual minutes equal the next prayer time your and minutes
             	
             	AthanService.missing_hours_to_nextPrayer = 0;
             	AthanService.missing_minutes_to_nextPrayer = 0;
             	AthanService.missing_seconds_to_nextPrayer = 0;
         		
             	if (Athan_Activity.STARTED == false){
                 	startAthanActivity(AthanService.actualPrayerCode , context);//start athan
                 }
             }
             else
             {
             	Athan_Activity.STARTED = false;
                 if (totalMinutes == AthanService.nextPrayerTimeInMinutes + 1 && isTimePlus1 == true) {//if salat time passing by 1 minute , getting new next paryer time
	                	
                 	isTimePlus1 = false;
                 	
                 	AthanService.getNextPrayer();
	                   	
	                   	if(AthanService.nextPrayerTimeInMinutes > totalMinutes){
	                   		AthanService.missing_hours_to_nextPrayer = ((AthanService.nextPrayerTimeInMinutes-1) - totalMinutes) / 60;
	                   		AthanService.missing_minutes_to_nextPrayer = ((AthanService.nextPrayerTimeInMinutes-1) - totalMinutes) % 60;
	                   		AthanService.missing_seconds_to_nextPrayer = 59 - second;
                 	}else{
                 		AthanService.getNextPrayer();
                 	}
                 }
                 else{	
                 	isTimePlus1 = true;
	                	
	                   	if(AthanService.nextPrayerTimeInMinutes > totalMinutes){
	                   		AthanService.missing_hours_to_nextPrayer = ((AthanService.nextPrayerTimeInMinutes-1) - totalMinutes) / 60;
	                   		AthanService.missing_minutes_to_nextPrayer = ((AthanService.nextPrayerTimeInMinutes-1) - totalMinutes) % 60;
	                   		AthanService.missing_seconds_to_nextPrayer = 59 - second;
                 	}else{
                 		AthanService.getNextPrayer();
                 	}
                }
              }
            }
          } catch (Exception ex) {
        	  this.wl.release();
         }
	}
	
	public void startAthanActivity(int nextPrayerCode , Context context){
		  try{
		   String athanType = getAthanAlertType(nextPrayerCode);
		   if(athanType.equalsIgnoreCase("athan")){
			   final Intent athan_intent = new Intent(context, Athan_Activity.class);
			   athan_intent.putExtra("nextPrayerCode",nextPrayerCode);
			   athan_intent.putExtra("language",UserConfig.getSingleton().getLanguage());
			   athan_intent.putExtra("athanType","athan");
			   athan_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);       
			   context.startActivity(athan_intent);
		   }
		   else{
			   
			   if(athanType.equalsIgnoreCase("vibration")){
				   final Intent athan_intent = new Intent(context, Athan_Activity.class);
				   athan_intent.putExtra("nextPrayerCode",nextPrayerCode);
				   athan_intent.putExtra("language",UserConfig.getSingleton().getLanguage());
				   athan_intent.putExtra("athanType","vibration");
				   athan_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);       
				   context.startActivity(athan_intent);
			   }
		   }
		  }catch(Exception exx){Athan_Activity.STARTED = false;}
	   }
	   
	  public static String getAthanAlertType(int value){
		   String athanType = "athan";
		   switch (value) {
   		case 1020:athanType = UserConfig.getSingleton().getShorouk_athan();break;
   		case 1021:athanType = UserConfig.getSingleton().getDuhr_athan();break;
   		case 1022:athanType = UserConfig.getSingleton().getAsr_athan();break;
   		case 1023:athanType = UserConfig.getSingleton().getMaghrib_athan();break;
   		case 1024:athanType = UserConfig.getSingleton().getIshaa_athan();break;
   		case 1025:athanType = UserConfig.getSingleton().getFajr_athan();break;
   		default:break;
   		}
		   return athanType;
	   }
	  
	  public void refreshDay(Context context){
			try {
				AthanService.prayerTimes = new PrayersTimes(Calendar.getInstance(),UserConfig.getSingleton());
				AthanService.prayerTimesInMinutes = AthanService.prayerTimes.getAllPrayrTimesInMinutes();//get all prayer times in minutes
	           
				AthanService.actualPrayerCode = 1025;//ishaa time code 
				AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[0];
         	
         	if(AthanService.nextPrayerTimeInMinutes > totalMinutes){
         		AthanService.missing_hours_to_nextPrayer = ((AthanService.nextPrayerTimeInMinutes-1) - totalMinutes) / 60;
         		AthanService.missing_minutes_to_nextPrayer = ((AthanService.nextPrayerTimeInMinutes-1) - totalMinutes) % 60;
         		AthanService.missing_seconds_to_nextPrayer = 59 - second;
      	}else{
      		AthanService.getNextPrayer();
      	}
         	
         	if(MyWidgetProvider.isWidget)
	        {
	        	try//update widget
				{
					Intent intent = new Intent(context , MyWidgetProvider.class);
					intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
					
					int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context.getPackageName(), MyWidgetProvider.class.getName()));
					intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
					context.sendBroadcast(intent);
				}catch(Exception e){}
	       }
         	
		  } catch (Exception e) {refreshDay(context);}
	    }
	  
}
