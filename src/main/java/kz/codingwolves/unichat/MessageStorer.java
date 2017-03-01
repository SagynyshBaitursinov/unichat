package kz.codingwolves.unichat;

import java.util.ArrayDeque;
import java.util.Deque;

import kz.codingwolves.unichat.models.ChatMessage;

public class MessageStorer {

	private Deque<ChatMessage> deque;
	
	public MessageStorer() {
		deque = new ArrayDeque<ChatMessage>();
	}
	
	public synchronized void add(ChatMessage message) {
		if (deque.size() > 99) {
			deque.removeFirst();
		}
		deque.addLast(message);
	}
	
	public Deque<ChatMessage> getAll() {
		return deque;
	}
}
