package kz.codingwolves.unichat;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import kz.codingwolves.identicons.IdenticonGenerator;
import kz.codingwolves.unichat.models.ChatMessage;
import kz.codingwolves.unichat.models.User;
import kz.codingwolves.unichat.services.UserService;

@Component
public class LoadAllUsersJob implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	UserService userService;
	
	@Autowired
	private MessageStorer messageStorer;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
			List<User> users = userService.getAllAbsolute();
			for (User user: users) {
				if (!user.getNickname().equals(WordUtils.capitalize(user.getNickname()))) {
					user.setNickname(WordUtils.capitalizeFully(user.getNickname()));
					userService.save(user);
				}
			}
	    	ChatMessage chatMessage = new ChatMessage("Unichat", new Date(), "Всем привет! Если вы видите это сообщение, то, скорее всего, Unichat был недавно перезапущен. Хорошего дня!");
	    	messageStorer.add(chatMessage);
		}
	}
	
	public static String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	
	@SuppressWarnings("unused")
	private void generateIdenticon(User user) {
		BufferedImage identicon = IdenticonGenerator.generate(user.getEmail(), !user.isMale());
		File outputfile = new File("/opt/unichat/identicons/" + user.getNuId() + ".png");
		try {
			ImageIO.write(identicon, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}