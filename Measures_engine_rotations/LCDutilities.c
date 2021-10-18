// LCDutilities.c
#include "msp430.h"

const unsigned char lcd_num[10] = {
  0xFC,   // 0
  0x60,   // 1
  0xDB,   // 2
  0xF3,   // 3
  0x67,   // 4
  0xB7,   // 5
  0xBF,   // 6
  0xE4,   // 7
  0xFF,   // 8
  0xF7    // 9
};

const unsigned int lcd_digit[6] = {
  0x0A29, // A1 = LCDM10
  0x0A25, // A2 = LCDM6
  0x0A23, // A3 = LCDM4
  0x0A32, // A4 = LCDM19
  0x0A2E, // A5 = LCDM15
  0x0A27  // A6 = LCDM8
};

const unsigned int lcd_char[37] = {
  0x28FC, // 0
  0x2060, // 1
  0x00DB, // 2
  0x00F1, // 3
  0x0067, // 4
  0x00B7, // 5
  0x00BF, // 6
  0x3080, // 7
  0x00FF, // 8
  0x00F7, // 9  
  0x0000, // _=10
  0x00EF, // A=11
  0x50F1, // B=12
  0x009C, // C=13
  0x50F0, // D=14
  0x009F, // E=15
  0x008F, // F=16
  0x00BD, // G=17        
  0x006F, // H=18
  0x5090, // I=19
  0x0078, // J=20
  0x220E, // K=21
  0x001C, // L=22
  0xA06C, // M=23
  0x826C, // N=24
  0x00FC, // O=25
  0x00CF, // P=26
  0x02FC, // Q=27       
  0x02CF, // R=28
  0x80B1, // S=29
  0x5080, // T=30
  0x007C, // U=31
  0x280C, // V=32
  0x0A6C, // W=33
  0xAA00, // X=34       
  0x1047, // Y=35
  0x2890  // Z=36
};
//=========================================================================
void LCDinit (void)
{
  PJSEL0 = BIT4 | BIT5;                   // For LFXT
  
  // Initialize LCD segments 0 - 21; 26 - 43
  LCDCPCTL0 = 0xFFFF;
  LCDCPCTL1 = 0xFC3F;
  LCDCPCTL2 = 0x0FFF;
  
  // Disable the GPIO power-on default high-impedance mode
  // to activate previously configured port settings
  PM5CTL0 &= ~LOCKLPM5;
  
  // Configure LFXT 32kHz crystal
  CSCTL0_H = CSKEY >> 8;                  // Unlock CS registers
  CSCTL4 &= ~LFXTOFF;                     // Enable LFXT
  do
  {
    CSCTL5 &= ~LFXTOFFG;                  // Clear LFXT fault flag
    SFRIFG1 &= ~OFIFG;
  } while (SFRIFG1 & OFIFG);              // Test oscillator fault flag
  CSCTL0_H = 0;                           // Lock CS registers
  
  // Initialize LCD_C
  // ACLK, Divider = 1, Pre-divider = 16; 4-pin MUX
  LCDCCTL0 = LCDDIV__1 | LCDPRE__16 | LCD4MUX | LCDLP;
  
  // VLCD generated internally,
  // V2-V4 generated internally, v5 to ground
  // Set VLCD voltage to 2.60v
  // Enable charge pump and select internal reference for it
  LCDCVCTL = VLCD_1 | VLCDREF_0 | LCDCPEN;
  
  LCDCCPCTL = LCDCPCLKSYNC;               // Clock synchronization enabled
  LCDCMEMCTL = LCDCLRM;                   // Clear LCD memory
  LCDCCTL0 |= LCDON;
}
//=========================================================================
void LCD_All_Off(void)
{
  int i;
  char *ptr = 0;
  ptr += 0x0A20;
  
  for (i=0; i<21; i++) {
    *ptr = 0x00;
    ptr++;
  }
}
//========================================================================
void LCD_All_On(void)
{
  int i;
  char *ptr = 0;
  ptr += 0x0A20;
  
  for (i=0; i<21; i++) {
    *ptr = 0xFF;
    ptr++;
  }
}
//========================================================================
void Display_Digit(int p, int d)
{ 
  char *ptr = 0;
  ptr += lcd_digit[p-1];
  *ptr = lcd_num[d];
}
//========================================================================
void Display_Number(long n)
{
  int i=0;
  
  if (n < 0) {  // Minus sign
    LCDM11 = BIT2;
    n = -n;
  }
  do {
    Display_Digit(6-i, n%10);
    i++;
    n = n/10;
  } while ((n != 0) && (i <= 6));
}
//========================================================================
void Display_Unsigned(unsigned int n)
{
  int i=0;
  
  do {
    Display_Digit(6-i, n%10);
    i++;
    n = n/10;
  } while ((n != 0) && (i <= 6));  
}
//========================================================================
void Display_Char(int p, unsigned char ch)
{
  char *ptr  = 0;
  unsigned char tmp;
  ptr += lcd_digit[p-1];
  
  // Convert ASCII to index
  // Digits:
  if ((ch >= 48)&&(ch <= 57)) ch -= 48;
  else {    
    // CAPITAL LETTERS:
    if ((ch >= 65)&&(ch <= 90)) ch -= 54;
    else {
      // small letters:
      if ((ch >= 97)&&(ch <= 122)) ch -= 86;
      // Space:
      else ch = 10;
    }
  }
  // Set lower byte:
  tmp = (char)(lcd_char[ch]&0x00FF);
  *ptr = tmp;
  // Set upper byte:
  ptr++;
  tmp = (char)(lcd_char[ch]>>8);
  *ptr = tmp;
}
//========================================================================
void Display_6(unsigned char *str)
{
  int i;
  for (i=0; i<6; i++)
    Display_Char(i+1, str[i]);
}
//========================================================================
  void MOVE(int RPM){
  static int second_digit_flag=0;
  static int temp=0;
  if  (UCA1IFG & UCRXIFG)  {
    temp =temp+ UCA1RXBUF-48;
    second_digit_flag++;
    if(second_digit_flag==2)
    {
      RPM=temp;
      temp=0;
      second_digit_flag=0;
    }
    temp*=10;
    }
  
    UCA1IFG &= ~UCRXIFG;
  }