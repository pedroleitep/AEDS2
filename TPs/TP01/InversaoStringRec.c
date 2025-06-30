#include <stdio.h>
#include <string.h>

// Função recursiva para inverter a string
void inverteString(char palavra[], int inicio, int fim) {
    if (inicio < fim) {
        // Troca os caracteres nas posições inicio e fim
        char temp = palavra[inicio];
        palavra[inicio] = palavra[fim];
        palavra[fim] = temp;

        // Chamada recursiva para os próximos caracteres
        inverteString(palavra, inicio + 1, fim - 1);
    }
}

// Função wrapper para facilitar a chamada
void inverter(char palavra[]) {
    inverteString(palavra, 0, strlen(palavra) - 1);
    printf("%s\n", palavra);
}

int main() {
    char palavra[100];

    scanf("%s", palavra);

    while (!(strlen(palavra) == 3 && palavra[0] == 'F' && palavra[1] == 'I' && palavra[2] == 'M')) {
        inverter(palavra);
        scanf("%s", palavra);
    }

    return 0;
}
