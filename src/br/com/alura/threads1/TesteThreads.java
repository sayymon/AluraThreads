package br.com.alura.threads1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Vector;
/**
 * Thread e processamento em paralelo implementações de Thread e exemplos curso de threads 1
 * Thread thread = new Thread(runnable);
 *  - thread.start();
 *  - thread.sleep();
 * implements Runnable
 *  - implementacao do metodo run()
 * Object
 *  - wait()
 *  - notify
 *  
 *  Utilização do synchronized e usabilidade das chaves de acesso em paralelo
 * 
 * thread.setDaemon(true) - Finaliza a thread dependente pois identifica que eh um serviço para outras threads.
 * 
 * @author sayymon
 *
 */
public class TesteThreads {

	public static void main(String[] args) throws InterruptedException {
		List<String> listaNoThreadSafe = new ArrayList<String>();
		
		for (int i = 1; i <= 10; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (int j = 1; j <= 10; j++) {
						try {
							listaNoThreadSafe.add("Thread " + this + " - " + j);
						} catch (Throwable e) {
							System.err.println("Thread " + this + " - " + j +"erro ! " + e);
						}
					}
				}
			}).start();
		}
		
		Thread.sleep(1000);
		
		for (int i = 0; i < listaNoThreadSafe.size(); i++) {
			System.out.println(i+" - listaNoThreadSafe :"+listaNoThreadSafe.get(i));
		}
		
		/**
		 * Implementacao de lista sincronizada Vector
		 */
		List<String> listaThreadSafe = new Vector<String>();
		/**
		 * Implementacao de lista sincronizada com o utilitario collections que implementa a lista mais rapida e  fica a encargo da versão do Java utilizada
		 */		
		List<String> listaThreadSafe2Opcao = Collections.synchronizedList(new ArrayList<>());
		
		
		
		for (int i = 1; i <= 10; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (int j = 1; j <= 10; j++) {
						listaThreadSafe.add("Thread " + this + " - " + j);
						listaThreadSafe2Opcao.add("Thread " + this + " - " + j);
					}
				}
			}).start();
		}
		
		pegandoTodasAsThreads();
		
		for (int i = 0; i < listaThreadSafe.size(); i++) {
			System.out.println(i+" - listaThreadSafe :"+listaThreadSafe.get(i));
			System.out.println(i+" - listaThreadSafe2Opcao :"+listaThreadSafe2Opcao.get(i));			
		}
		
		quantidadeProcessadores();
	}
	
	public static void pegandoTodasAsThreads(){
		Set<Thread> todasAsThreads = Thread.getAllStackTraces().keySet();
		
		for (Thread thread : todasAsThreads) {
			System.out.println(thread.getName());
		}
	}
	
	public static void quantidadeProcessadores() {
		Runtime runtime = Runtime.getRuntime();
		int qtdProcessadores = runtime.availableProcessors();
		System.out.println("Qtd de processadores: " + qtdProcessadores);		
	}
	
}
