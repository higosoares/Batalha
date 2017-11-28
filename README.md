# Batalha Naval
Jogo de batalha naval em JAVA desenvolvido no UniCEUB por Higo Soares e Walner Pessoa.

## Instruções para a compilação
É necessário ter instalado o JDK na máquina. Após instalado ir em Variáveis de Ambiente no Windows e setar as seguintes variáveis de usuário: 
- Nome : Java_Home
- Valor : Caminho do jdk. Ex: C:\Program Files\Java\jdk1.8.0_151
- Nome : ClassPath
- Valor : Inserir '.' e ';' antes do caminho do jdk com a pasta lib. Ex: .;C:\Program Files\Java\jdk1.8.0_151\lib
- Nome : Path
- Valor : Caminho do jdk com a pasta bin. Ex: C:\Program Files\Java\jdk1.8.0_151\bin

Na pasta onde foi extraído os arquivos Servidor, Cliente e Coordenada, rodar o Servidor em um prompt e o Cliente e outro, 
podendo ser em máquina diferentes também estando na mesma rede. Para tal deve rodar cada um com os seguintes comandos :
- javac -encoding UTF8 Servidor.java
- java Servidor

- javac -encoding UTF8 Cliente.java
- java Cliente

## Uso
Para jogar o batalha naval, o processo que estiver rodando o Servidor deve esperar o adversário conectar para poder começar o jogo
, o que estiver rodando o Cliente deverá informar o ip e a porta do Servidor, a porta por padrão é a 9876. Após o jogo começar
é só informar a coordenada X de A a G em maiúsculo e em seguida a coordenada Y de 0 a 6 para atirar. O jogador que destruir todos os navios do adversário ganha.

