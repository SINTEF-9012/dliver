/**
 * Copyright (C) 2013 SINTEF <steffen.dalgard@sintef.no>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingml.dliver.driver;

public interface DliverListener {
	
		// Control and status messages
		void cUSerialNumber(long value);// 		@code "110"; 
		void cUFWRevision(String value);// 			@code "117";
		void batteryStatus(int value);// 		@code "98";
		void indicationDev(int value);// 			@code "105";
		void measurementPatient(int value, int timestamp);// 			@code "109";
		void messageOverrun(int value);//  		@code "100";
		
		// Time and clock synchronization messages
		void referenceClockTime(long value, boolean seconds);// 						@code "106";
		void fullClockTimeSync(long value, boolean seconds);// 						@code "107";
		
		// ECG and Heart rate messages
		void heartRate(int valueHr, int timestamp);//  @code "104"; //12 bit value in 0.1 bpm, ie 0.0-409.5 bpm + HRV in ms
		//void heartRateConfidence(int value, int timestamp);//  	@code "99";
		void heartRateInterval(int valueHri, int timestamp);//  	@code "99";
		void eCGData(int value);//  			@code "101";
		void eCGSignalQuality(int value, int timestamp);//  	@code "102";
		void eCGRaw(int value, int timestamp);//  				@code "103"; // Not sure which data type
		
                // PPG Messages
                //void pPGData(int value);
		//void pPGSignalQuality(int value, int timestamp);
		//void pPGRaw(int value, int timestamp);

                // ICG Messages
		void iCGAbs(int icgAbs, int timestamp);//  		@code "102";
		void combinedICG(int icgAbsDer, int icgAbsAc, int timestamp);//  		@code "122";

                // EMG Messages
                //void eMGData(int value);
		//void eMGSignalQuality(int value, int timestamp);
		//void eMGRaw(int value, int timestamp);
                //void eMGRMS(int channelA, int channelB, int timestamp);

		void ptt(int value, int timestamp);

		void ppg(int ppgRaw, int ppgDer, int timestamp);      //  			@code "121";
                
                void btPutChar(int value); // 123
                
                
		// Gyroscope messages
		void gyroPitch(int value, int timestamp);//  			@code "112";
		void gyroRoll(int value, int timestamp);// 			@code "114";
		void gyroYaw(int value, int timestamp);//  			@code "121";
		
		// Accelerometer and activity messages
		void accLateral(int value, int timestamp);//  			@code "115";
		void accLongitudinal(int value, int timestamp);//  	@code "119";
		void accVertical(int value, int timestamp);//  		@code "118";
		void rawActivityLevel(int value, int timestamp);//  		@code "97";
		
		void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp);//  		@code "120";

		void stepCount(long step, int timestamp);      // 	@code "126";
                
                void quaternion(int w, int x, int y, int z, int timestamp);
		
                void magnetometer(int x, int y, int z, int timestamp);
                
		// IR Temperature sensor messages
		void skinTemperature(int value, int timestamp);//  	@code "116";
		
                void connectionLost();
                
                // @code "107"; as well as fullClockTimeSync but with the timesync bit set
                void referenceClockTimeSync(int timeSyncSeqNum, long value); 
                
                void eventEpoch(int eventNum, int val, long epoch);

                void playStart(long epoch);
                void playStop();
                
}
