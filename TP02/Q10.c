#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <time.h>
#include <strings.h>

#define MAX_SHOWS 1400
#define MAX_SELECIONADOS 1000
#define MAX_STR 200

typedef struct {
    char id[MAX_STR], type[MAX_STR], title[MAX_STR], director[MAX_STR], country[MAX_STR];
    char dateAdded[MAX_STR], rating[MAX_STR], duration[MAX_STR];
    int releaseYear;
    char **cast;
    int castSize;
    char **listedIn;
    int listedInSize;
} Show;

char* trim(char* str) {
    while (isspace(*str)) str++;
    if (*str == 0) return str;
    char* end = str + strlen(str) - 1;
    while (end > str && isspace(*end)) end--;
    *(end+1) = 0;
    return str;
}

char** split(char* str, const char* delim, int* size) {
    char* token = strtok(str, delim);
    char** result = NULL;
    *size = 0;
    while (token) {
        while (isspace(*token)) token++;
        result = realloc(result, (*size + 1) * sizeof(char*));
        result[*size] = strdup(token);
        (*size)++;
        token = strtok(NULL, delim);
    }
    return result;
}

int maiorQue(const char* a, const char* b) {
    return strcmp(a, b) > 0;
}

void ordenarCampos(char** array, int size) {
    for (int i = 0; i < size-1; i++) {
        for (int j = i+1; j < size; j++) {
            if (maiorQue(array[i], array[j])) {
                char* tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
            }
        }
    }
}

void lerCSV(char* path, Show* lista, int* total) {
    FILE* file = fopen(path, "r");
    if (!file) {
        perror("Erro ao abrir o arquivo");
        exit(1);
    }

    char linha[1000];
    fgets(linha, sizeof(linha), file); // cabeçalho

    while (fgets(linha, sizeof(linha), file) && *total < MAX_SHOWS) {
        Show s;
        char* campos[11] = {NULL};
        int col = 0, aspas = 0, b = 0;
        char buffer[1000];

        for (int i = 0; linha[i]; i++) {
            char c = linha[i];
            if (c == '"') {
                aspas = !aspas;
            } else if (c == ',' && !aspas) {
                buffer[b] = '\0';
                campos[col++] = strdup(trim(buffer));
                b = 0;
            } else {
                buffer[b++] = c;
            }
        }
        buffer[b] = '\0';
        campos[col] = strdup(trim(buffer));

        strcpy(s.id, (campos[0] && strlen(campos[0]) > 0) ? campos[0] : "NaN");
        strcpy(s.type, (campos[1] && strlen(campos[1]) > 0) ? campos[1] : "NaN");
        strcpy(s.title, (campos[2] && strlen(campos[2]) > 0) ? campos[2] : "NaN");
        strcpy(s.director, (campos[3] && strlen(campos[3]) > 0) ? campos[3] : "NaN");
        

        if (!campos[4] || strlen(campos[4]) == 0) {
            s.cast = malloc(sizeof(char*));
            s.cast[0] = strdup("NaN");
            s.castSize = 1;
        } else {
            s.cast = split(campos[4], ",", &s.castSize);
            ordenarCampos(s.cast, s.castSize);
        }

        strcpy(s.country, (campos[5] && strlen(campos[5]) > 0) ? campos[5] : "NaN");
        strcpy(s.dateAdded, (campos[6] && strlen(campos[6]) > 0) ? campos[6] : "NaN");
        s.releaseYear = (campos[7] && strlen(campos[7]) > 0) ? atoi(campos[7]) : -1;
        strcpy(s.rating, (campos[8] && strlen(campos[8]) > 0) ? campos[8] : "NaN");
        strcpy(s.duration, (campos[9] && strlen(campos[9]) > 0) ? campos[9] : "NaN");
        

        if (!campos[10] || strlen(campos[10]) == 0) {
            s.listedIn = malloc(sizeof(char*));
            s.listedIn[0] = strdup("NaN");
            s.listedInSize = 1;
        } else {
            s.listedIn = split(campos[10], ",", &s.listedInSize);
            ordenarCampos(s.listedIn, s.listedInSize);
        }

        lista[(*total)++] = s;

        for (int i = 0; i <= col; i++) free(campos[i]);
    }

    fclose(file);
}

void imprimir(Show* s) {
    printf("=> %s ## %s ## %s ## %s ## [", s->id, s->title, s->type, s->director);
    for (int i = 0; i < s->castSize; i++) {
        printf("%s", s->cast[i]);
        if (i < s->castSize - 1) printf(", ");
    }
    printf("] ## %s ## %s ## %d ## %s ## %s ## [", s->country, s->dateAdded, s->releaseYear, s->rating, s->duration);
    for (int i = 0; i < s->listedInSize; i++) {
        printf("%s", s->listedIn[i]);
        if (i < s->listedInSize - 1) printf(", ");
    }
    printf("] ##\n");
}

Show cloneShow(Show* s) {
    Show c = *s;
    c.cast = malloc(s->castSize * sizeof(char*));
    for (int i = 0; i < s->castSize; i++) c.cast[i] = strdup(s->cast[i]);
    c.listedIn = malloc(s->listedInSize * sizeof(char*));
    for (int i = 0; i < s->listedInSize; i++) c.listedIn[i] = strdup(s->listedIn[i]);
    return c;
}

int mesParaNumero(const char* mes) {
    const char* meses[] = {"January", "February", "March", "April", "May", "June",
                           "July", "August", "September", "October", "November", "December"};
    for (int i = 0; i < 12; i++) {
        if (strcasecmp(mes, meses[i]) == 0) return i + 1;
    }
    return 0;
}

int dataToInt(const char* data) {
    if (strcasecmp(data, "NaN") == 0) return 0;
    int dia, ano;
    char mesStr[20];
    if (sscanf(data, "%s %d, %d", mesStr, &dia, &ano) == 3) {
        int mes = mesParaNumero(mesStr);
        return ano * 10000 + mes * 100 + dia;
    }
    return 0;
}

int comparar(Show* a, Show* b, int* comparacoes) {
    (*comparacoes)++;
    int dataA = dataToInt(a->dateAdded);
    int dataB = dataToInt(b->dateAdded);
    if (dataA != dataB) return dataA - dataB;
    return strcasecmp(a->title, b->title);
}

void quicksort(Show* arr, int esq, int dir, int* comparacoes) {
    if (esq < dir) {
        Show pivo = arr[(esq + dir) / 2];
        int i = esq, j = dir;

        while (i <= j) {
            while (comparar(&arr[i], &pivo, comparacoes) < 0) i++;
            while (comparar(&arr[j], &pivo, comparacoes) > 0) j--;
            if (i <= j) {
                Show tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }

        quicksort(arr, esq, j, comparacoes);
        quicksort(arr, i, dir, comparacoes);
    }
}

int main() {
    Show lista[MAX_SHOWS], selecionados[MAX_SELECIONADOS];
    int total = 0, qtdSelecionados = 0;

    lerCSV("/tmp/disneyplus.csv", lista, &total);

    char entrada[MAX_STR];
    while (1) {
        fgets(entrada, sizeof(entrada), stdin);
        entrada[strcspn(entrada, "\n")] = 0;
        if (strcmp(entrada, "FIM") == 0) break;

        for (int i = 0; i < total; i++) {
            if (strcmp(lista[i].id, entrada) == 0 && qtdSelecionados < MAX_SELECIONADOS) {
                selecionados[qtdSelecionados++] = cloneShow(&lista[i]);
            }
        }
    }

    clock_t inicio = clock();
    int comparacoes = 0;

    quicksort(selecionados, 0, qtdSelecionados - 1, &comparacoes);

    clock_t fim = clock();
    double tempo = 1000.0 * (fim - inicio) / CLOCKS_PER_SEC;

    for (int i = 0; i < qtdSelecionados; i++) {
        imprimir(&selecionados[i]);
    }

    FILE* log = fopen("843275_quicksort.txt", "w");
    fprintf(log, "843275\t%.3f\t%d\n", tempo, comparacoes);
    fclose(log);

    return 0;
}
