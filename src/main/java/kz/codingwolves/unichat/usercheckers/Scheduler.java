package kz.codingwolves.unichat.usercheckers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.google.common.util.concurrent.Striped;

import kz.codingwolves.unichat.MessageStorer;

@Configuration
@EnableScheduling
public class Scheduler implements SchedulingConfigurer {

	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		return new ThreadPoolTaskScheduler();
	}
	
	@Bean
	public ActiveUserPinger activeUserPinger(SimpMessagingTemplate template, ActiveUserService activeUserService, RouletteWheel rouletteWheel) {
		return new ActiveUserPinger(template, activeUserService, rouletteWheel);
	}
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		 taskRegistrar.setTaskScheduler(taskScheduler());
	}
	
	@Bean
	public ActiveUserService activeUserService() {
		return new ActiveUserService();
	}
	
	@Bean
	public List<MessageNumber> messageNumber() {
		return new ArrayList<MessageNumber>();
	}
	
	@Bean
	public Striped<Lock> locks() {
		return Striped.lock(3000);
	}
	
	@Bean
	public MessageStorer messageStorer() {
		return new MessageStorer();
	}
}
