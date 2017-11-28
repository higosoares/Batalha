/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.*;
import java.io.*;

/**
 *
 * @author Higo Soares e Walner Pessoa
 */
public class Cliente {

    public static BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
    public static DatagramSocket socketCliente = null;
    public static byte[] mensagemPedido = new byte[1024];
    public static DatagramPacket pedido = null;
    public static byte[] mensagemResposta = new byte[1024];
    public static DatagramPacket resposta = null;

    public static void main(String args[]) throws IOException {
        jogoCliente();
    }

    public static void jogoCliente() throws IOException {
        System.out.println("Digite o IP do Servidor: ");
        String ip = entrada.readLine();
        System.out.println("Digite a porta do Servidor: ");
        int porta = new Integer(entrada.readLine());
        try {
            enviarMensagem("conectando", ip, porta);
            receberResposta();
            Servidor.criarNavios();
            Servidor.mostrarTabuleiro();
            enviarMensagem("pronto", ip, porta);
            System.out.println("Aguarde o adversário atirar!");
            receberResposta();
            boolean jogo = true;
            while (jogo) {
                
                //Informações se o adversário acertou o tiro
                Coordenada coordenada = null;
                coordenada = Servidor.converterByteParaCoordenada(resposta.getData());
                System.out.println("Seu adversário atirou nas coordenadas (" + coordenada.getX() + "," + coordenada.getY() + ")");
                String informacaoTiro = Servidor.informacoesTiro(coordenada);
                enviarMensagem(informacaoTiro, ip, porta);
                System.out.println(informacaoTiro);
                Servidor.mostrarTabuleiro();

                if (Servidor.verificaTabuleiro().equals("fim")) {
                    System.out.println("Seu adversário conseguiu destruir o seu ultimo navio! Fim de jogo!");
                    //envia que perdeu
                    enviarMensagem("fim", ip, porta);
                    jogo = false;
                }else{
                    System.out.println("Pode começar!");

                    //Atirar e receber resposta
                    int[] tiro = new int[2];
                    mensagemPedido = Servidor.converterCoordenadaParaByte(Servidor.atirar(tiro));
                    enviarTiro(mensagemPedido, ip, porta);
                    receberResposta();
                    Servidor.mensagem(new String(resposta.getData()));
                    Servidor.mostrarTabuleiro();
                    
                    System.out.println("Aguarde o adversário atirar!");
                    receberResposta();
                    //verifica se ganhou
                    
                    String vencer = new String(resposta.getData());
                    if(vencer.contains("fim")){
                    Servidor.mensagem(vencer); 
                    jogo=false;
                    }
                    else{
                        jogo=true;
                    }
                }
                
            }


        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (socketCliente != null) {
                socketCliente.close();
            }
        }
    }

    public static void enviarMensagem(String mensagem, String ip, int porta) throws IOException {
        socketCliente = new DatagramSocket();
        mensagemPedido = new String(mensagem).getBytes();
        InetAddress ipServidor = InetAddress.getByName(ip);
        pedido = new DatagramPacket(mensagemPedido, mensagemPedido.length, ipServidor, porta);
        socketCliente.send(pedido);
    }

    public static void enviarTiro(byte[] mensagemPedido, String ip, int porta) throws IOException {
        socketCliente = new DatagramSocket();
        InetAddress ipServidor = InetAddress.getByName(ip);
        pedido = new DatagramPacket(mensagemPedido, mensagemPedido.length, ipServidor, porta);
        socketCliente.send(pedido);
    }

    public static String receberResposta() throws IOException {
        //byte[] mensagemResposta = new byte[1000];
        resposta = new DatagramPacket(mensagemResposta, mensagemResposta.length);
        socketCliente.receive(resposta);
        String resposta2 = new String(resposta.getData());
        return resposta2;
    }

}
