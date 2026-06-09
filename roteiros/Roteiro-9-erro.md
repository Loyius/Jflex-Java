# Página 1

Compiladores
Roteiro - Registro de Erros com Localiza¸ c˜ ao
1 Introdu¸ c˜ ao
Ao longo de nossos exemplos JCup e JFlex , fizemos algumas verifica¸ c˜ oes de erros.
Como por exemplo:
• verificamos se o MOD est´ a sendo aplicado APENASn´ umeros inteiros;
• verificamos se o denominador de uma divis˜ ao ´ e zero;
• verificamos se estamos usando valores negativos no ´ ındice de um vetor;
• entre outras.
Neste ROTEIRO, estudaremos com mais aten¸ c˜ ao sobre o tema doserros .
A primeira coisa que precisamos definir ´ e que, em um compilador, n´ os apenas notificamos o pro-
gramador a lista de erros . Ou seja, n´ os n˜ ao fazemos nenhum tratamento como fizemos nos exemplos
anteriores.
Os exemplos anteriores, foram feitos para treinarmos e estudarmos os conceitos apresentados. Por
isso, foram de grande valia.
Assim, a partir de agora, vamos criar uma lista de erros e, ao longo das nossas an´ alises (l´ exica,
sint´ atica, semˆ antica. . . ), iremos adicionando os erros nesta lista.
Ao final, imprimiremos na tela todos os erros da lista de erros .
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 2

Compiladores
2 Pacote de Erros
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 3

Compiladores
2.1 Arquivo Erro.java
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 4

Compiladores
2.2 Arquivo ListaErro.java
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 5

Compiladores
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 6

Compiladores
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 7

Compiladores
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 8

Compiladores
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 9

Compiladores
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 10

Compiladores
2.3 Divis˜ ao por Zero
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 11

Compiladores
2.4 if SEM Parˆ enteses( )
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 12

Compiladores
2.5 Express~ aoSEM operador
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 13

Compiladores
3 Notifica¸ c˜ ao Erro L´ exico
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 14

Compiladores
3.1 Diagrama de Classes
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 15

Compiladores
3.2 Arquivo Scanner.flex
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 16

Compiladores
3.3 Arquivo Parser.cup
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 17

Compiladores
3.4 Arquivo ListaErros.java
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao


# Página 18

Compiladores
3.5 Arquivo Erros.java
Prof.: Alessandra Hauck Roteiro - Registro de Erros com Localiza¸ c˜ ao
