#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>

bool ehPalindromoRec(char palavra[], int i, int j);
bool ehPalindromo(char palavra[]);

bool ehPalindromo(char palavra[]){
    return ehPalindromoRec(palavra, 0, strlen(palavra) - 1);
}
bool ehPalindromoRec(char palavra[], int i, int j){
    bool palindromo;
    if(i >= j){
        palindromo = true;
    }
    else if(palavra[i] != palavra[j]){
        palindromo = false;
    }
    else{
        palindromo = ehPalindromoRec(palavra, i + 1, j - 1);
    }
    return palindromo;
}

int main(){
    char palavra[500];
    scanf(" %[^\r\n]", palavra);
    
    while (strcmp(palavra, "FIM") != 0) {
        if(ehPalindromo(palavra)){
            printf("SIM\n");
        }else{
            printf("NAO\n");
        }
        scanf(" %[^\r\n]", palavra);
    }
    
    return 0;
}