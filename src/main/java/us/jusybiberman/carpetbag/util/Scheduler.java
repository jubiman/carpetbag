package us.jusybiberman.carpetbag.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import us.jusybiberman.carpetbag.Carpetbag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;

public class Scheduler {
	private static class ScheduledTask implements Runnable {
		private final Timer timer;
		private final long _delay;
		private final Runnable _task;
		public boolean completed = false;
		public long ID;
		public ScheduledTask(Runnable task, long delay) {
			timer = new Timer();
			_task = task;
			_delay = delay;
		}

		public void run() {
			try {
				timer.wait(_delay);
			} catch (InterruptedException ignored) {}
			Carpetbag.SERVER_INSTANCE.addScheduledTask(_task);
			completed = true;
		}
	}

	private final HashMap<Long, ScheduledTask> tasks;
	private final HashSet<Thread> threads;
	public Scheduler() {
		threads = new HashSet<>();
		tasks = new HashMap<>();

		Thread thread = new Thread(() -> {
			Timer timer = new Timer();
			while(true) {
				Iterator<Thread> it = threads.iterator();
				while (it.hasNext()) {
					Thread t = it.next();
					ScheduledTask task = tasks.get(t.getId());
					if (task.completed) {
						tasks.remove(t.getId());
						threads.remove(t);
					}
				}
				try {
					timer.wait(5000);
				} catch (InterruptedException ignored) {}
			}
		});
		thread.start();
	}

	public void addScheduledTask(Runnable task, long delay) {
		ScheduledTask scheduledTask = new ScheduledTask(task, delay);
		//tasks.add(scheduledTask);
		Thread thread = new Thread(scheduledTask);
		scheduledTask.ID = thread.getId();
		threads.add(thread);
		thread.start();
	}
}
