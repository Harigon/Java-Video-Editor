package src.multiThreading;

import src.multiThreading.threads.DecodingThread;
import src.multiThreading.threads.RenderingThread;
import src.multiThreading.threads.TaskThread;


public class ThreadManager {

/**
 * Class provided by Graham Edgecombe (Open-source)
 * 
 * The purpose of this class is to allow multiple threads to run at specific and consistent speeds.
 * 
 * The speeds that while loops run at are different on different machines, due to different specs etc. This is
 * unfortunate when writing code that is dependent on a consistent speed (such as playing a video at a specific frame-rate).  
 * This thread allows the user to lock a while loop to a specific speed, which will be maintained
 * on different machines as long as the machine is fast enough to keep up.
 *  
* FULL credit to Graham Edgecombe for providing this code.  http://grahamedgecombe.com/
*/
	
	public static void runRenderingThread() {
		/*
		 * Run this process on a separate thread, to stop it freezing the GUI.
		 */
		new Thread() {
			public void run() {
				final long[] processorSpeedTestTimes = new long[10];
				int delayTime = 20;
				int minDelay = 1;

				int i = 0;
				int maximumSpeed = 256;
				int sleepTime = 1;


				/*
				 * This allows us to measure the computer speed, by recording the times between
				 * each loop. This time can then be used to sync the speed of our while loop.
				 */
				for(int k1 = 0; k1 < 10; k1++){
					processorSpeedTestTimes[k1] = System.currentTimeMillis();
				}

				/*
				 * Start the loop
				 */
				while(true) 
				{
					int tempMax = maximumSpeed;
					int tempTime = sleepTime;
					maximumSpeed = 300;
					sleepTime = 1;
					long l1 = System.currentTimeMillis();
					if(processorSpeedTestTimes[i] == 0L)
					{
						maximumSpeed = tempMax;
						sleepTime = tempTime;
					} else
						if(l1 > processorSpeedTestTimes[i])
							maximumSpeed = (int)((long)(2560 * delayTime) / (l1 - processorSpeedTestTimes[i]));
					if(maximumSpeed < 25)
						maximumSpeed = 25;
					if(maximumSpeed > 256)
					{
						maximumSpeed = 256;
						sleepTime = (int)((long) delayTime - (l1 - processorSpeedTestTimes[i]) / 10L);
					}
					if(sleepTime > delayTime)
						sleepTime = delayTime;
					processorSpeedTestTimes[i] = l1;
					i = (i + 1) % 10;
					if(sleepTime > 1)
					{
						for(int k2 = 0; k2 < 10; k2++)
							if(processorSpeedTestTimes[k2] != 0L)
								processorSpeedTestTimes[k2] += sleepTime;

					}
					if(sleepTime < minDelay)
						sleepTime = minDelay;
					try {
						Thread.sleep(sleepTime);
					}
					catch(InterruptedException _ex) {

					}

					try {
						RenderingThread.processTasks();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}.start();
	}
	
	
	public static void runDecodingThread() {
		/*
		 * Run this process on a separate thread, to stop it freezing the GUI.
		 */
		new Thread() {
			public void run() {
				final long[] processorSpeedTestTimes = new long[10];
				int delayTime = 20;
				int minDelay = 1;

				int i = 0;
				int maximumSpeed = 256;
				int sleepTime = 1;


				/*
				 * This allows us to measure the computer speed, by recording the times between
				 * each loop. This time can then be used to sync the speed of our while loop.
				 */
				for(int k1 = 0; k1 < 10; k1++){
					processorSpeedTestTimes[k1] = System.currentTimeMillis();
				}

				/*
				 * Start the loop
				 */
				while(true) 
				{
					int tempMax = maximumSpeed;
					int tempTime = sleepTime;
					maximumSpeed = 300;
					sleepTime = 1;
					long l1 = System.currentTimeMillis();
					if(processorSpeedTestTimes[i] == 0L)
					{
						maximumSpeed = tempMax;
						sleepTime = tempTime;
					} else
						if(l1 > processorSpeedTestTimes[i])
							maximumSpeed = (int)((long)(2560 * delayTime) / (l1 - processorSpeedTestTimes[i]));
					if(maximumSpeed < 25)
						maximumSpeed = 25;
					if(maximumSpeed > 256)
					{
						maximumSpeed = 256;
						sleepTime = (int)((long) delayTime - (l1 - processorSpeedTestTimes[i]) / 10L);
					}
					if(sleepTime > delayTime)
						sleepTime = delayTime;
					processorSpeedTestTimes[i] = l1;
					i = (i + 1) % 10;
					if(sleepTime > 1)
					{
						for(int k2 = 0; k2 < 10; k2++)
							if(processorSpeedTestTimes[k2] != 0L)
								processorSpeedTestTimes[k2] += sleepTime;

					}
					if(sleepTime < minDelay)
						sleepTime = minDelay;
					try {
						Thread.sleep(sleepTime);
					}
					catch(InterruptedException _ex) {

					}

					try {
						DecodingThread.processTasks();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}.start();
	}
	

	
	
	public static void runTaskThread() {
		/*
		 * Run this process on a separate thread, to stop it freezing the GUI.
		 */
		new Thread() {
			public void run() {
				final long[] processorSpeedTestTimes = new long[10];
				int delayTime = 20;
				int minDelay = 1;

				int i = 0;
				int maximumSpeed = 256;
				int sleepTime = 1;


				/*
				 * This allows us to measure the computer speed, by recording the times between
				 * each loop. This time can then be used to sync the speed of our while loop.
				 */
				for(int k1 = 0; k1 < 10; k1++){
					processorSpeedTestTimes[k1] = System.currentTimeMillis();
				}

				/*
				 * Start the loop
				 */
				while(true) 
				{
					int tempMax = maximumSpeed;
					int tempTime = sleepTime;
					maximumSpeed = 300;
					sleepTime = 1;
					long l1 = System.currentTimeMillis();
					if(processorSpeedTestTimes[i] == 0L)
					{
						maximumSpeed = tempMax;
						sleepTime = tempTime;
					} else
						if(l1 > processorSpeedTestTimes[i])
							maximumSpeed = (int)((long)(2560 * delayTime) / (l1 - processorSpeedTestTimes[i]));
					if(maximumSpeed < 25)
						maximumSpeed = 25;
					if(maximumSpeed > 256)
					{
						maximumSpeed = 256;
						sleepTime = (int)((long) delayTime - (l1 - processorSpeedTestTimes[i]) / 10L);
					}
					if(sleepTime > delayTime)
						sleepTime = delayTime;
					processorSpeedTestTimes[i] = l1;
					i = (i + 1) % 10;
					if(sleepTime > 1)
					{
						for(int k2 = 0; k2 < 10; k2++)
							if(processorSpeedTestTimes[k2] != 0L)
								processorSpeedTestTimes[k2] += sleepTime;

					}
					if(sleepTime < minDelay)
						sleepTime = minDelay;
					try {
						Thread.sleep(sleepTime);
					}
					catch(InterruptedException _ex) {

					}

					try {
						TaskThread.processTasks();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}.start();
	}
	
	
	
}
