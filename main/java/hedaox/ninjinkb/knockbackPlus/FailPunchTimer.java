package hedaox.ninjinkb.knockbackPlus;

import java.util.Timer;
import java.util.TimerTask;

public class FailPunchTimer {

	private static boolean taskAlreadyScheduled = false;
	private static boolean safetoSoundFailPunch = true;

	private static FailPunchTimer failPunchTimerSingletonInstance = null;

	public static FailPunchTimer getInstance()
	{
		if(failPunchTimerSingletonInstance == null)
		{
			synchronized (FailPunchTimer .class) {
				if (failPunchTimerSingletonInstance == null) {
					failPunchTimerSingletonInstance = new FailPunchTimer();
				}
			}
		}
		return failPunchTimerSingletonInstance;
	}

	public boolean isSafetoSoundFailPunch()
	{
		return safetoSoundFailPunch;
	}

	public void setSafetoSoundFailPunch(boolean b)
	{
		safetoSoundFailPunch = b;
	}

	public boolean isTaskAlreadyScheduled() {
		return taskAlreadyScheduled;
	}

	public void setTaskAlreadyScheduled(boolean taskAlreadyScheduled) {
		this.taskAlreadyScheduled = taskAlreadyScheduled;
	}
	
	Timer t = new Timer();

	/**
	 * Timer for reducing the speed in which the fail punch sound appear
	 * 
	 * @author Hedaox
	 */
	TimerTask taskSafeSoundFailPunch = new TimerTask() {
		@Override
		public void run() {
			taskAlreadyScheduled = false;
			safetoSoundFailPunch = true;
		}
	};
}