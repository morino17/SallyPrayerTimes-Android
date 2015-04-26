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
package widget;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import classes.ArabicReshape;
import classes.AthanService;
import classes.UserConfig;

import com.sally.R;

public class MyWidgetProvider extends AppWidgetProvider {
    
    private NumberFormat formatter = new DecimalFormat("00");
    public static boolean isWidget = false;

       @Override
       public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    	   if(!AthanService.STARTED)
			{
				context.startService(new Intent(context,AthanService.class));
			}
    	    else{
    	   final int widgetsCount = appWidgetIds.length;

           // Perform this loop procedure for each App Widget that belongs to this provider
           for (int i=0; i<widgetsCount; i++) {
               int appWidgetId = appWidgetIds[i];

        		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

       			if(UserConfig.getSingleton().getLanguage().equalsIgnoreCase("ar"))
       			{
       		        remoteViews.setTextViewText(R.id.fajr_label, ArabicReshape.reshape(context.getResources().getString(R.string.fajr)));
       		        remoteViews.setTextViewText(R.id.shorouk_label, ArabicReshape.reshape(context.getResources().getString(R.string.shorouk)));
       		        remoteViews.setTextViewText(R.id.duhr_label, ArabicReshape.reshape(context.getResources().getString(R.string.duhr)));
       		        remoteViews.setTextViewText(R.id.asr_label, ArabicReshape.reshape(context.getResources().getString(R.string.asr)));
       		        remoteViews.setTextViewText(R.id.maghrib_label, ArabicReshape.reshape(context.getResources().getString(R.string.maghrib)));
       		        remoteViews.setTextViewText(R.id.ishaa_label, ArabicReshape.reshape(context.getResources().getString(R.string.ishaa)));
       			    
       		        remoteViews.setTextViewText(R.id.missing_to, ArabicReshape.reshape(context.getResources().getString(R.string.missing_to)));
       		        remoteViews.setTextViewText(R.id.missing_salat, ArabicReshape.reshape(getNextPrayerName(context)));
       			}
       			else
       			{
       		        remoteViews.setTextViewText(R.id.fajr_label, context.getResources().getString(R.string.fajr));
       		        remoteViews.setTextViewText(R.id.shorouk_label, context.getResources().getString(R.string.shorouk));
       		        remoteViews.setTextViewText(R.id.duhr_label, context.getResources().getString(R.string.duhr));
       		        remoteViews.setTextViewText(R.id.asr_label, context.getResources().getString(R.string.asr));
       		        remoteViews.setTextViewText(R.id.maghrib_label, context.getResources().getString(R.string.maghrib));
       		        remoteViews.setTextViewText(R.id.ishaa_label, context.getResources().getString(R.string.ishaa));
       		        
       		        remoteViews.setTextViewText(R.id.missing_to, context.getResources().getString(R.string.missing_to));
       		        remoteViews.setTextViewText(R.id.missing_salat, getNextPrayerName(context));
       			}

       			remoteViews.setTextViewText(R.id.missing_time, formatter.format(AthanService.missing_hours_to_nextPrayer)+":"+formatter.format(AthanService.missing_minutes_to_nextPrayer)+":"+formatter.format(AthanService.missing_seconds_to_nextPrayer));
       			setActualPrayerTextColor(remoteViews , AthanService.actualPrayerCode);
       			
       	        remoteViews.setTextViewText(R.id.fajrTime, AthanService.prayerTimes.getFajrFinalTime());
       	        remoteViews.setTextViewText(R.id.shoroukTime, AthanService.prayerTimes.getShorou9FinalTime());
       	        remoteViews.setTextViewText(R.id.duhrTime, AthanService.prayerTimes.getDuhrFinalTime());
       	        remoteViews.setTextViewText(R.id.asrTime, AthanService.prayerTimes.getAsrFinalTime());
       	        remoteViews.setTextViewText(R.id.maghribTime, AthanService.prayerTimes.getMaghribFinalTime());
       	        remoteViews.setTextViewText(R.id.ishaaTime, AthanService.prayerTimes.getIshaaFinalTime());
       	        
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
           }
       }
           MyWidgetProvider.isWidget = true;
    }

      @Override
       public void onDeleted(Context context, int[] appWidgetIds) {
    	  MyWidgetProvider.isWidget = false;
    	  super.onDeleted(context, appWidgetIds);
       }

       @Override
       public void onDisabled(Context context) {
    	   MyWidgetProvider.isWidget = false;
    	   Intent intent = new Intent(context, MyWidgetProviderBroadcastReceiver.class);
    	   PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
    	   AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    	   alarmManager.cancel(sender);
    	   super.onDisabled(context);
       }

       @Override
       public void onEnabled(final Context context) {
    	   super.onEnabled(context);
			if(!AthanService.STARTED)
			{
				context.startService(new Intent(context,AthanService.class));
			}
    	   MyWidgetProvider.isWidget = true;
    	   AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    	   Intent intent = new Intent(context, MyWidgetProviderBroadcastReceiver.class);
    	   PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
    	   //After after 1 second
    	   am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ 1000 * 1, 1000 , pi);
    	  
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
       
}
