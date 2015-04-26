/*
 * Copyright (C) 2011 Iranian Supreme Council of ICT, The FarsiTel Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASICS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package classes;

public class KiblaDirectionCalculator {
	public static final double QIBLA_LATITUDE = Math.toRadians(21.423333);
    public static final double QIBLA_LONGITUDE = Math.toRadians(39.823333);

    public static float getQiblaDirectionFromNorth(double degLongitude,double degLatitude) {
    double latitude2 = Math.toRadians(degLatitude);
    double longitude = Math.toRadians(degLongitude);

    double soorat = Math.sin(QIBLA_LONGITUDE - longitude);
    double makhraj = Math.cos(latitude2) * Math.tan(QIBLA_LATITUDE)- Math.sin(latitude2) * Math.cos(QIBLA_LONGITUDE - longitude);
    double returnValue = Math.toDegrees(Math.atan(soorat / makhraj));

        if (latitude2 > QIBLA_LATITUDE) {
            if ((longitude > QIBLA_LONGITUDE || longitude < (Math
                    .toRadians(-180d) + QIBLA_LONGITUDE))
                    && (returnValue > 0 && returnValue <= 90)) {

                returnValue += 180;

            } else if (!(longitude > QIBLA_LONGITUDE || longitude < (Math
                    .toRadians(-180d) + QIBLA_LONGITUDE))
                    && (returnValue > -90 && returnValue < 0)) {

                returnValue += 180;

            }

        }
        if (latitude2 < QIBLA_LATITUDE) {

            if ((longitude > QIBLA_LONGITUDE || longitude < (Math
                    .toRadians(-180d) + QIBLA_LONGITUDE))
                    && (returnValue > 0 && returnValue < 90)) {

                returnValue += 180;

            }
            if (!(longitude > QIBLA_LONGITUDE || longitude < (Math
                    .toRadians(-180d) + QIBLA_LONGITUDE))
                    && (returnValue > -90 && returnValue <= 0)) {

                returnValue += 180;
            }

        }
        return (float)(returnValue);
    }

}
