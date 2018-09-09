package br.com.alura.threads;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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

	public static void main(String[] args) throws Exception {
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
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (int j = 1; j <= 10; j++) {
						listaThreadSafe.add("Thread " + this + " - " + j);
						listaThreadSafe2Opcao.add("Thread " + this + " - " + j);
					}
				}
			});
			thread.setUncaughtExceptionHandler(tratadorExcecoesThreads());
			thread.start();
		}
		
		pegandoTodasAsThreads();
		
		for (int i = 0; i < listaThreadSafe.size(); i++) {
			System.out.println(i+" - listaThreadSafe :"+listaThreadSafe.get(i));
			System.out.println(i+" - listaThreadSafe2Opcao :"+listaThreadSafe2Opcao.get(i));			
		}
		
		/**
		 * Implementacao que veio a partir do java 5 para melhorar a utilizacao das threads retornar 
		 * referencias e excecoes de forma mais nativa
		 */
		Callable<String> callable = new Callable<String>() {

			@Override
			public String call() throws Exception {
				return "Exemplo de callable";
			}
			
		};
		/**
		 * Exemplo FutureTask
		 */
		FutureTask<String> futureTask = new FutureTask<>(callable);
		
		
		new Thread(futureTask).start();
		
		/**
		 * Aguarda 10 segundos caso o future nao retorne antes do tempo limite
		 */
		futureTask.get(10, TimeUnit.SECONDS);
		
		quantidadeProcessadores();
	}

	/**
	 * Exemplo de tratador de excecoes das threads nao tratadas
	 * 
	 * @return
	 */
	private static UncaughtExceptionHandler tratadorExcecoesThreads() {
		return new UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println(t.getName() +" excessao :"+e.getMessage());
				
			}
		};
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
