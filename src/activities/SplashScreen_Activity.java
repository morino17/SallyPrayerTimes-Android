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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import classes.AthanService;

import com.sally.R;

public class SplashScreen_Activity extends Activity{

	private static final int SPLASH_TIME = 150;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen_activity);
	
			if(!AthanService.STARTED)
			{
				getApplicationContext().startService(new Intent(SplashScreen_Activity.this,AthanService.class));
			}

        goToHomeProgram();
	}
	
	public void goToHomeProgram(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				SplashScreen_Activity.this.finish();
				Intent intent = new Intent(SplashScreen_Activity.this,Home_Programe_Activity.class);
		        SplashScreen_Activity.this.startActivity(intent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				
				if(!AthanService.STARTED)
				{
					startService(new Intent(SplashScreen_Activity.this,AthanService.class));
				}
			}
		}, SPLASH_TIME);	
	}
	
}
