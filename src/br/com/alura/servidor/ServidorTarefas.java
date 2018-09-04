package br.com.alura.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorTarefas {


	private ExecutorService threadPool;
	private ServerSocket servidor;
	/**
	 * Volatile para desabilitar os caches criados nativamentes quando se instancia threads
	 * ou AtomicBoolean
	 * AtomicBoolean para não precisar usar volatile ou syncronized ao acessar a uma variável. A classe faz parte do pacote java.util.concurrent.atomic onde podemos encontrar outras classes, como por exemplo AtomicInteger e AtomicLong.
	 * 
	 * https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/atomic/package-summary.html
	 * 
	 * No tutorial da Oracle é apresentado um pequeno exemplo como seria uma classe usando syncronized comparado com AtomicInteger. Vale à pena conferir:
	 * 
	 * https://docs.oracle.com/javase/tutorial/essential/concurrency/atomicvars.html	 
	 **/
	
	private volatile boolean isRodando;
	

	public ServidorTarefas() throws IOException {
		System.out.println("Iniciando servidor");
		this.servidor = new ServerSocket(12345);
		/**
		 * Implementacao que disponibiliza o reaproveitamento das threads
		 */
		this.threadPool = Executors.newCachedThreadPool();
		this.isRodando = true;
	}
	
	public static void main(String[] args) throws Exception {
		ServidorTarefas servidorTarefas = new ServidorTarefas();
		servidorTarefas.rodar();
	}

	public void rodar() throws IOException {
		while (this.isRodando && !servidor.isClosed()) {
			try {
				Socket socket = servidor.accept();
				System.out.println("Aceitando um novo cliente na porta ; "+socket.getPort());
				
				DistribuirTarefas distribuirTarefas = new DistribuirTarefas(socket,this);
				/**
				 * Executa o start e aciona o metodo run()
				 */
				threadPool.execute(distribuirTarefas);
			} catch (SocketException e) {
				break;
			}
		}
	}
	
	public void parar() throws IOException {
		System.out.println("Parando o Servidor !");
		this.isRodando = false;
		servidor.close();
		threadPool.shutdown();
	}

}
