/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.*;
import java.io.*;
import java.util.Random;
/**
 *
 * @author Higo Soares e Walner Pessoa
 */
public class Servidor {

    public static String[][] tabuleiro = new String[7][7];
    public static BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
    public static DatagramSocket socketServidor = null;
    public static DatagramPacket pedido = null;
    public static byte[] mensagemPedido = new byte[1024];
    public static DatagramPacket resposta = null;
    public static byte[] mensagemResposta = new byte[1024];

    public static void main(String args[]) throws IOException {
        jogoServidor();

    }

    public static void jogoServidor() throws IOException {

        try {
            socketServidor = new DatagramSocket(9876);
            // create socket at agreed port
            while (true) {
                receber();
                enviar("conectado");
                criarNavios();
                mostrarTabuleiro();
                enviar("esperando");
                receber();
                System.out.println("Começar!");    
                boolean jogo = true;
                while (jogo) {
                    
                    //Atirar e receber resposta
                    int[] tiro = new int[2];
                    mensagemResposta = converterCoordenadaParaByte(atirar(tiro));
                    enviarTiro(mensagemResposta);
                    receber();
                    mensagem(new String(pedido.getData()));
                    mostrarTabuleiro();
                    System.out.println("Aguarde seu adversário atirar!");
                    receber();
                    
                    //Verifica se ganhou
                    String vencer = new String(pedido.getData());
                    if(vencer.contains("fim")){
                        mensagem(vencer);
                        jogo = false;
                    }
                    else{
                    //Informações se o adversário acertou o tiro
                    Coordenada coordenada = null;
                    coordenada = converterByteParaCoordenada(pedido.getData());
                    System.out.println("Seu adversário atirou nas coordenadas (" + coordenada.getX() + "," + coordenada.getY() + ")");
                    String informacaoTiro = informacoesTiro(coordenada);
                    enviar(informacaoTiro);
                    System.out.println(informacaoTiro);
                    mostrarTabuleiro();
                    
                    if (verificaTabuleiro().equals("fim")) {
                        System.out.println("Seu adversário destruiu todos seus navios!");
                        //envia que perdeu
                        enviar("fim");
                        jogo = false;
                    } else {                        
                        jogo = true;
                    }
                    }
                }
                System.out.println("Fim de jogo!");
                socketServidor.close();
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (socketServidor != null) {
                socketServidor.close();
            }
        }
    }

    public static void receber() throws IOException {
        pedido = new DatagramPacket(mensagemPedido, mensagemPedido.length);
        socketServidor.receive(pedido);
    }

    public static void enviar(String mensagem) throws IOException {
        mensagemResposta = new String(mensagem).getBytes();
        resposta = new DatagramPacket(mensagemResposta, mensagemResposta.length,
                pedido.getAddress(), pedido.getPort());
        socketServidor.send(resposta);
    }

    public static void enviarTiro(byte[] mensagemResposta) throws IOException {
        resposta = new DatagramPacket(mensagemResposta, mensagemResposta.length,
                pedido.getAddress(), pedido.getPort());
        socketServidor.send(resposta);
    }

    public static void mostrarTabuleiro() {
        System.out.println("    0 | 1 | 2 | 3 | 4 | 5 | 6 ");
        int linha = -1;
        for (int i = 65; i < 72; i++) {
            linha++;
            char character = (char) i;
            System.out.print(character);
            for (int coluna = 0; coluna < 7; coluna++) {
                if (tabuleiro[linha][coluna] == null) {
                    System.out.print(" | " + "_");
                } else if ("S".equals(tabuleiro[linha][coluna])) {
                    System.out.print(" | " + "S");
                } else if ("C".equals(tabuleiro[linha][coluna])) {
                    System.out.print(" | " + "C");
                } else if ("P".equals(tabuleiro[linha][coluna])) {
                    System.out.print(" | " + "P");
                } else if ("X".equals(tabuleiro[linha][coluna])) {
                    System.out.print(" | " + "X");
                }
            }
            System.out.println();
        }
    }

    public static void criarNavios() {
        Random gerador = new Random();

        for (int barco = 1; barco < 4; barco++) {
            boolean podeConstruir = false;

            while (!podeConstruir) {
                int x = gerador.nextInt(7);
                int y = gerador.nextInt(7);

                if (tabuleiro[x][y] != null) {
                    podeConstruir = false;
                } else {
                    switch (barco) {
                        case 1:
                            tabuleiro[x][y] = "S";
                            podeConstruir = true;
                            break;
                        case 2:
                            for (int i = 0; i < barco; i++) {
                                if (y < 6) {
                                    tabuleiro[x][y + i] = "C";
                                } else {
                                    tabuleiro[x][y - i] = "C";
                                }
                            }
                            podeConstruir = true;
                            break;
                        case 3:
                            for (int i = 0; i < barco; i++) {
                                if (y < 5) {
                                    tabuleiro[x][y + i] = "P";
                                } else {
                                    tabuleiro[x][y - i] = "P";
                                }
                            }
                            podeConstruir = true;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public static Coordenada atirar(int[] tiro) throws IOException {
        System.out.println("Digite a coordenada X do seu tiro: ");
        String x2 = entrada.readLine();
        int x = converterCharInt(x2);

        System.out.println("Digite a coordenada Y do seu tiro: ");
        int y = new Integer(entrada.readLine());
        
        Coordenada coordenadaTiro = null;      
        if((y >= 0 && y <= 6) && (x >=0 && y<= 6)){
        coordenadaTiro = new Coordenada(x, y);    
        tiro[0] = x;
        tiro[1] = y;
        System.out.println("Você atirou na coordenada (" + tiro[0] + "," + tiro[1] + ")");
        return coordenadaTiro;
        }else{
            return atirar(tiro);
            
        }
        
    }

    public static String informacoesTiro(Coordenada coordenada) {
                
        int x = coordenada.getX();
        int y = coordenada.getY();

        if (tabuleiro[x][y] == "S" || tabuleiro[x][y] == "C" || tabuleiro[x][y] == "P") {
            tabuleiro[x][y] = "X";
            return "acertou";
        } else {
            return "errou";
        }
       
    }

    public static void mensagem(String mensagem) {
        if (mensagem.contains("acertou")) {
            System.out.println("Acertou o tiro!");
        } else if (mensagem.contains("errou")) {
            System.out.println("O tiro foi na água!");
        } else if (mensagem.contains("fim")) {
            System.out.println("Você ganhou o jogo!");      
        }

    }

    public static String verificaTabuleiro() {

        int qtdOutros = 0;
        for (int linha = 0; linha < 7; linha++) {
            for (int coluna = 0; coluna < 7; coluna++) {
                String valor = tabuleiro[linha][coluna] != null ? tabuleiro[linha][coluna].toString() : "";

                if (valor == "C" || valor == "S" || valor == "P") {
                    qtdOutros++;
                }
            }
        }
        String retorno = qtdOutros > 0 ? "continua" : "fim";
        return retorno;
    }

    //Converte a entrada de letras em int
    public static int converterCharInt(String character) {
        int saida = -1;
        switch (character) {
            case "A":
                saida = 0;
                break;
            case "B":
                saida = 1;
                break;
            case "C":
                saida = 2;
                break;
            case "D":
                saida = 3;
                break;
            case "E":
                saida = 4;
                break;
            case "F":
                saida = 5;
                break;
            case "G":
                saida = 6;
                break;
        }
        return saida;
    }

    public static byte[] converterCoordenadaParaByte(Coordenada coordenada) {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream ous;
            ous = new ObjectOutputStream(bao);
            ous.writeObject(coordenada);
            return bao.toByteArray();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static Coordenada converterByteParaCoordenada(byte[] coordenadaAsByte) {
        try {
            ByteArrayInputStream bao = new ByteArrayInputStream(coordenadaAsByte);
            ObjectInputStream ous;
            ous = new ObjectInputStream(bao);
            return (Coordenada) ous.readObject();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
  
