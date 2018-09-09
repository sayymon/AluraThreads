package br.com.alura.servidor;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class DistribuirTarefas implements Runnable {

	private Socket socket;
	private ServidorTarefas servidorTarefas;

	public DistribuirTarefas(Socket socket, ServidorTarefas servidorTarefas) {
		this.socket = socket;
		this.servidorTarefas = servidorTarefas;
	}

	@Override
	public void run() {
		try {
			System.out.println("Dstribuindo tarefas para :"+socket);
			Scanner scanner = new Scanner(socket.getInputStream());
			PrintStream printStream = new PrintStream(socket.getOutputStream());
			
			while (scanner.hasNext()) {
				String mensagemCliente = scanner.nextLine();
				System.out.println(mensagemCliente);
				printStream.println("Recebi a mensagem ;"+mensagemCliente);
				if (mensagemCliente.equals("fim")) {
					servidorTarefas.parar();
				}else {
					servidorTarefas.getThreadPool().execute(() -> {
						System.out.println("Executando Comando :"+mensagemCliente);
						try {
							Thread.sleep(2000L);
						} catch (InterruptedException e) {
							System.err.println(e);
						}
					});
				}
			}
			
			scanner.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
