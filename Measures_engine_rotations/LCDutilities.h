// LCDutilities.h
#ifndef __LCDUTILITIES_H
#define __LCDUTILITIES_H
#include "msp430.h"

extern const unsigned char lcd_num[10];
extern const unsigned int lcd_digit[6];
extern const unsigned int lcd_char[37];

void LCDinit (void);
void LCD_All_Off(void);
void LCD_All_On(void);
void Display_Digit(int p, int d);
void Display_Number(long n);
void Display_Unsigned(unsigned int n);
void Display_Char(int p, unsigned char ch);
void Display_6(unsigned char *str);
 void MOVE(int RPM);

#endif // __LCDUTILITIES_H
