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
package classes;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.sally.R;

public class AthanService extends Service{
	
	public static boolean STARTED = false;

	public static int nextPrayerTimeInMinutes;//next prayer time in minutes
    public static int actualPrayerCode;
    public static int missing_hours_to_nextPrayer;
    public static int missing_minutes_to_nextPrayer;
    public static int missing_seconds_to_nextPrayer;
	
    public static int[] prayerTimesInMinutes;//all prayer times in minutes
	public static PrayersTimes prayerTimes;
	public static Calendar calendar;
    
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
        try {
			XmlHandler.getSingleton().setParameters(getApplicationContext());
			AthanService.prayerTimes = new PrayersTimes(Calendar.getInstance() , XmlHandler.getSingleton().getUserConfig());
        } catch (Exception e){
        	try{
        		if(UserConfig.getSingleton().getLanguage().equals("ar")){
        			Toast.makeText(getApplicationContext(), ArabicReshape.reshape(this.getResources().getString(R.string.error_corrupt_user_file_config)) , Toast.LENGTH_LONG).show();
        		}else{
        			Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.error_corrupt_user_file_config) , Toast.LENGTH_LONG).show();
        		}
        	}catch(Exception e1){}
       }
	}

	@Override
	public void onStart(Intent intent, int startId) {
		
		AthanService.prayerTimesInMinutes = new int[6];
		AthanService.prayerTimesInMinutes = AthanService.prayerTimes.getAllPrayrTimesInMinutes();//get all prayer times in minutes

		AthanService.calendar = Calendar.getInstance();
        AthanService.STARTED = true;
        getNextPrayer();
        
 	   AlarmManager am = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
 	   Intent AthanServiceBroasdcastReceiverIntent = new Intent(getApplicationContext(), AthanServiceBroasdcastReceiver.class);
 	   PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, AthanServiceBroasdcastReceiverIntent, 0);

 	   am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 , pi);
 	   
	}
	
	@Override
	public void onDestroy() {
	   AthanService.STARTED = false;
 	   Intent intent = new Intent(getApplicationContext(), AthanServiceBroasdcastReceiver.class);
 	   PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
 	   AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
 	   alarmManager.cancel(sender);
	}

	  public static void  getNextPrayer() {//get time code name and his time in minutes
		  Calendar calendar = Calendar.getInstance();
		  int totalMinutes = (calendar.get(Calendar.HOUR_OF_DAY) * 60) + (calendar.get(Calendar.MINUTE));
	        if (totalMinutes == 0 || totalMinutes == 1440 || (totalMinutes >= 0 && totalMinutes <= AthanService.prayerTimesInMinutes[0])) {//if actual time is between 0 and fajr time , means that the next prayer time is fajr
	        	AthanService.actualPrayerCode = 1025;//ishaa time code 
	        	AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[0];
	        } else {
	            if (totalMinutes > AthanService.prayerTimesInMinutes[0] && totalMinutes <= AthanService.prayerTimesInMinutes[1]) {//if actual time is between fajr time and shorou9 time , means that the next prayer time is shorou9
	            	AthanService.actualPrayerCode = 1020;//fajr time code
	            	AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[1];
	            } else {
	                if (totalMinutes > AthanService.prayerTimesInMinutes[1] && totalMinutes <= AthanService.prayerTimesInMinutes[2]) {//if actual time is between shorou9 time and duhr time , means that the next prayer time is duhr
	                	AthanService.actualPrayerCode = 1021;//shorou9 time code
	                	AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[2];
	                } else {
	                    if (totalMinutes > AthanService.prayerTimesInMinutes[2] && totalMinutes <= AthanService.prayerTimesInMinutes[3]) {//if actual time is between duhr and asr time , means that the next prayer time is asr
	                    	AthanService.actualPrayerCode = 1022;//duhr time code
	                    	AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[3];
	                    } else {
	                        if (totalMinutes > AthanService.prayerTimesInMinutes[3] && totalMinutes <= AthanService.prayerTimesInMinutes[4]) {//if actual time is between asr and maghrib time , means that the next prayer time is maghrib
	                        	AthanService.actualPrayerCode = 1023;//asr time code
	                        	AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[4];
	                        } else {
	                            if (totalMinutes > AthanService.prayerTimesInMinutes[4] && totalMinutes <= AthanService.prayerTimesInMinutes[5]) {//if actual time is between maghrib and ishaa time , means that the next prayer time is ishaa
	                            	AthanService.actualPrayerCode = 1024;//maghrib time code
	                            	AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[5];
	                            } else {
	                                if (totalMinutes > AthanService.prayerTimesInMinutes[5] && totalMinutes < 1440) {//if actual time is between ishaa and 24H  , means that the next prayer time is fajr
	                                	AthanService.actualPrayerCode = 1025;//ishaa time code
	                                	AthanService.nextPrayerTimeInMinutes = AthanService.prayerTimesInMinutes[0] + 1440;
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	        }
	     }
	  
	    public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
	        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false;
	        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true;
	        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false;
	        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true;
	        return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
	    }
}
