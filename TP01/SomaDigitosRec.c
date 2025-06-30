#include <stdio.h>
#include <string.h>

// Função recursiva para calcular a soma dos dígitos de um número representado como string
int somaDigitos(char str[], int i) {
    int resp;
    if (i == strlen(str)) {
        resp = 0; // Caso base: se chegou ao final da string, retorna 0
    }else{
        // Converte o caractere para inteiro e soma recursivamente
        resp = (str[i] - '0') + somaDigitos(str, i + 1);
    }
    return resp;
}

int main() {
    char linha[100];

    scanf("%s", linha);

    while (!(strlen(linha) == 3 && linha[0] == 'F' && linha[1] == 'I' && linha[2] == 'M')) {
        printf("%d\n", somaDigitos(linha, 0));
        scanf("%s", linha);
    }

    return 0;
}
