#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define MAX_SHOWS 1400
#define MAX_SELECIONADOS 1000
#define MAX_LINE 1024

typedef struct {
    char id[20];
    char type[20];
    char title[200];
    char director[100];
    char cast[10][100];
    int qtdCast;
    char country[100];
    char dateAdded[20];
    int releaseYear;
    char rating[10];
    char duration[20];
    char listedIn[10][100];
    int qtdListed;
} Show;

void lerCSV(const char *filename, Show *lista, int *total) {
    FILE *file = fopen(filename, "r");
    if (!file) {
        perror("Erro ao abrir o arquivo");
        exit(1);
    }

    char linha[MAX_LINE];
    *total = 0;
    fgets(linha, MAX_LINE, file); // Pular cabeçalho

    while (fgets(linha, MAX_LINE, file) && *total < MAX_SHOWS) {
        Show s;
        char *token;
        int col = 0;
        int i = 0;
        bool aspas = false;
        char campo[11][300] = {{0}};
        int k = 0, j = 0;

        while (linha[i] != '\0' && linha[i] != '\n') {
            if (linha[i] == '"') {
                aspas = !aspas;
            } else if (linha[i] == ',' && !aspas) {
                campo[k][j] = '\0';
                k++; j = 0;
            } else {
                campo[k][j++] = linha[i];
            }
            i++;
        }
        campo[k][j] = '\0';

        strcpy(s.id, campo[0][0] ? campo[0] : "NaN");
        strcpy(s.type, campo[1][0] ? campo[1] : "NaN");
        strcpy(s.title, campo[2][0] ? campo[2] : "NaN");
        strcpy(s.director, campo[3][0] ? campo[3] : "NaN");

        s.qtdCast = 0;
        if (strcmp(campo[4], "") == 0) {
            strcpy(s.cast[0], "NaN");
            s.qtdCast = 1;
        } else {
            token = strtok(campo[4], ",");
            while (token && s.qtdCast < 10) {
                while (*token == ' ') token++;
                strcpy(s.cast[s.qtdCast++], token);
                token = strtok(NULL, ",");
            }
        }

        strcpy(s.country, campo[5][0] ? campo[5] : "NaN");
        strcpy(s.dateAdded, campo[6][0] ? campo[6] : "NaN");
        s.releaseYear = campo[7][0] ? atoi(campo[7]) : -1;
        strcpy(s.rating, campo[8][0] ? campo[8] : "NaN");
        strcpy(s.duration, campo[9][0] ? campo[9] : "NaN");

        s.qtdListed = 0;
        if (strcmp(campo[10], "") == 0) {
            strcpy(s.listedIn[0], "NaN");
            s.qtdListed = 1;
        } else {
            token = strtok(campo[10], ",");
            while (token && s.qtdListed < 10) {
                while (*token == ' ') token++;
                strcpy(s.listedIn[s.qtdListed++], token);
                token = strtok(NULL, ",");
            }
        }

        lista[(*total)++] = s;
    }

    fclose(file);
}

int compararTitulo(const void *a, const void *b) {
    const Show *s1 = (const Show *)a;
    const Show *s2 = (const Show *)b;
    return strcmp(s1->title, s2->title);
}

int buscaBinaria(Show *arr, int n, char *titulo) {
    int esq = 0, dir = n - 1;
    while (esq <= dir) {
        int meio = (esq + dir) / 2;
        int cmp = strcmp(arr[meio].title, titulo);
        if (cmp == 0) return meio;
        else if (cmp < 0) esq = meio + 1;
        else dir = meio - 1;
    }
    return -1;
}

int main() {
    Show lista[MAX_SHOWS], selecionados[MAX_SELECIONADOS];
    int total = 0, qtdSelecionados = 0;
    char entrada[200];

    lerCSV("/tmp/disneyplus.csv", lista, &total);

    // Seleção de shows por ID
    while (true) {
        fgets(entrada, sizeof(entrada), stdin);
        entrada[strcspn(entrada, "\n")] = 0;
        if (strcmp(entrada, "FIM") == 0) break;

        for (int i = 0; i < total; i++) {
            if (strcmp(lista[i].id, entrada) == 0) {
                if (qtdSelecionados < MAX_SELECIONADOS) {
                    selecionados[qtdSelecionados++] = lista[i];
                }
                break;
            }
        }
    }

    // Ordena os selecionados por título para busca binária
    qsort(selecionados, qtdSelecionados, sizeof(Show), compararTitulo);

    // Busca por título
    while (true) {
        fgets(entrada, sizeof(entrada), stdin);
        entrada[strcspn(entrada, "\n")] = 0;
        if (strcmp(entrada, "FIM") == 0) break;

        int pos = buscaBinaria(selecionados, qtdSelecionados, entrada);
        if (pos != -1) {
            printf("SIM\n");
        } else {
            printf("NAO\n");
        }
    }

    return 0;
}
