package br.com.alura.servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTarefas {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("localhost", 12345);
		System.out.println("Conexão estabelecida");

		Thread tratandoRetornoServidor = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("Recebendo dados do servidor!");
				Scanner respostaServidor;
				try {
					respostaServidor = new Scanner(socket.getInputStream());
					while (respostaServidor.hasNextLine()) {
						System.out.println(respostaServidor.nextLine());
					}
					respostaServidor.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		/**
		 * Exemplo de thread java 8
		 */
		Thread enviaServidor = new Thread(() -> {

				try {
					PrintStream saida = new PrintStream(socket.getOutputStream());
					Scanner teclado = new Scanner(System.in);
					while (teclado.hasNextLine()) {
						String line = teclado.nextLine();
						if (line == null || line.isEmpty() ) {
							break;
						}
						saida.println(line);
						if (line.equals("fim")) {
							break;
						}
					}
					teclado.close();
					saida.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		});

		enviaServidor.start();
		tratandoRetornoServidor.start();
		
		/**
		 * Junta o processamento da thread principal para somente continuar quando a thread 
		 * individual terminar
		 */
		enviaServidor.join();
		/**
		 * A thread main ficará esperando por 1s a resposta da thread .
		 */
		tratandoRetornoServidor.join(1000);
		
		socket.close();
	}
}
