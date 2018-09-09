package br.com.alura.threads;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TesteBlockingQueue {

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(3);
		
		arrayBlockingQueue.put("c1");
		arrayBlockingQueue.put("c2");
		arrayBlockingQueue.put("c3");
		
		System.out.println(arrayBlockingQueue.poll());
		System.out.println(arrayBlockingQueue.poll());
		System.out.println(arrayBlockingQueue.poll());
		/**
		 * Aguarda até ter uma mensagem para ser processado
		 */
		System.out.println(arrayBlockingQueue.take());
	}
}
