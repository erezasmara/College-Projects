
// ----------------this program made By erez asmara--------------//

#include "msp430.h"
#include <stdio.h>


static int RPM=120;
static float _switch=1;


void main(void) {
  WDTCTL = WDTPW | WDTHOLD; 
  PM5CTL0 &= ~LOCKLPM5; 
  
  
 
  
  // P4.7  ---->  TA1.2 TO 5HZ TA1 Block CCR2
  P4SEL0 |= BIT7;
  P4SEL1 |= BIT7;
  P4DIR |= BIT7; 
  
  TA1CTL = TASSEL_1 + MC_1  + TACLR; //ACLK,interrupt, UP MODE
  TA1CCR0 = 6553;

  TA1CCTL2 = OUTMOD_2;
  TA1CCR2 = TA1CCR0;
  //--------------------------

  
 
  
  //input p3.6 
  P3SEL0 |= ~BIT6;
  P3SEL1 |= BIT6;
  P3DIR &= ~BIT6; // TB0CC2A
  
  TB0CTL = TBSSEL_3 + MC_2 + TBCLR;
  TB0CCTL2 = CAP + CM_1 + CCIE;
  //--------------------------

  //input p3.3 TB0CLK
  P3SEL0 |= BIT3;
  P3SEL1 |= BIT3;
  P3DIR &= ~BIT3; // TB0CLK
  
  TB0CTL = TBSSEL_3 + MC_2 + TBCLR;
  TB0CCTL2 = CAP + CM_1 + CCIE;
  //--------------------------

  //Receiving and sending information
  UCA1CTLW0 = UCSWRST + UCSSEL__SMCLK;                 	 //  SMCLK,reset
  UCA1BRW = 6;                         // 9600 baud match coolterm
  UCA1MCTLW = 0x22D1; 		      //  by the GUIDE
  UCA1CTLW0 &= ~UCSWRST;                	// Init USCI
  UCA1IE |= UCRXIE;
 //--------------------------
  
  
  
  // P1.7 --> T0.2 TO 100HZ Using TA0 Block CCR2

  TA0CCR2 =TA0CCR0;
  P1SEL0 |= BIT7;
  P1SEL1 |= BIT7;
  P1DIR |= BIT7;
  
  TA0CTL = TASSEL_1 + MC_1 + TACLR; //ACLK,interrupt, UP MODE
  TA0CCR0 = 327; 
  TA0CCTL2 = OUTMOD_2;
  TA0CCR2 = TA1CCR0/2; // 50% duty cycle
  
 //--------------------------

  _BIS_SR (LPM0_bits + GIE);
}

#pragma vector=TIMER0_B1_VECTOR
__interrupt void TIMER0(void) {
  long Fhz=TB0CCR2*1.25;
  static int tmp=0;      
  if (TB0CCTL2 & CCIFG) {
    //down speed
    if(RPM>Fhz)
    {
      _switch=_switch-0.01;
      TA0CCR2 =TA0CCR0*_switch;
    }
    //up speed
      if(RPM<Fhz)
    {
      _switch=_switch+0.01;
      TA0CCR2 =TA0CCR0*_switch;
    }
   TB0CTL |= TBCLR;

    tmp++;
    if(tmp==25) /* 1/5*25 tims=5sec*/
    {
      //print speed every 5 second
    while(!(UCA1IFG&UCTXIFG));
    UCA1TXBUF = Fhz;
    tmp=0;

    }
  }
  TB0CCTL2 &= ~CCIFG;
}

#pragma vector=USCI_A1_VECTOR
__interrupt void USCI() {
  
  static int digitf=0;
  static int var=0;
  if  (UCA1IFG & UCRXIFG)  {
    var =var+ UCA1RXBUF-48;
    digitf++;
    if(digitf==2)
    {
      RPM=var;
      var=0;
      digitf=0;
    }
    var*=10;
    }
  
    UCA1IFG &= ~UCRXIFG;
  }	
