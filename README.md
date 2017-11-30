# Batalha Naval
Jogo de batalha naval em JAVA desenvolvido no UniCEUB por Higo Soares e Walner Pessoa.

## Instruções para a compilação
É necessário ter instalado o JDK. Após instalado ir em Variáveis de Ambiente no Windows e setar as seguintes variáveis de usuário: 
- Nome : Java_Home
- Valor : Caminho do jdk. Ex: C:\Program Files\Java\jdk1.8.0_151
- Nome : ClassPath
- Valor : Inserir '.' e ';' antes do caminho do jdk com a pasta lib. Ex: .;C:\Program Files\Java\jdk1.8.0_151\lib
- Nome : Path
- Valor : Caminho do jdk com a pasta bin. Ex: C:\Program Files\Java\jdk1.8.0_151\bin

Para compilação e execução deve-se entrar na pasta onde foi extraído os arquivos e em seguida digitar em cada prompt os seguintes comandos :
No primeiro prompt :
- javac -encoding UTF8 Servidor.java
- java Servidor

No segundo prompt : 
- javac -encoding UTF8 Cliente.java
- java Cliente

*Caso for fazer em dois computadores na mesma rede é só repetir o procedimento das variáveis de usuário e executar um prompt em cada computador.

## Uso
Para jogar o batalha naval, o prompt que estiver rodando o Servidor deve esperar o adversário conectar para poder começar o jogo e o que estiver rodando o Cliente deverá informar o ip e a porta do Servidor, a porta por padrão é a 9876. Após o jogo começar é só informar a coordenada X de A a G em maiúsculo e em seguida a coordenada Y de 0 a 6 para atirar. O jogador que destruir todos os navios do adversário ganha.