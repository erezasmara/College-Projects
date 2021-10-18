# Measures engine rotations
Project Structure:

```
├─ Measures_engine_rotations
│  ├─ Debug
│  │  ├─ Exe
│  │  │  └─ FinalProject.d43
│  │  ├─ List
│  │  │  └─ FinalProject.map
│  │  └─ Obj
│  │     ├─ FinalProject.pbd
│  │     ├─ FinalProject.pbd.browse
│  │     ├─ FinalProject.pbd.linf
│  │     ├─ LCDutilities.pbi
│  │     ├─ LCDutilities.pbi.xcl
│  │     ├─ LCDutilities.r43
│  │     ├─ Projecton.pbi.xcl
│  │     ├─ Projecton.r43
│  │     ├─ Untitled1.pbi.xcl
│  │     ├─ motor.pbi
│  │     └─ motor.r43
│  ├─ FinalProject.dep
│  ├─ FinalProject.ewd
│  ├─ FinalProject.ewp
│  ├─ FinalProject.ewt
│  ├─ LCDutilities.c
│  ├─ LCDutilities.h
│  ├─ motor.c
│  ├─ problem.docx
│  ├─ settings
│  │  ├─ FinalProject.Debug.cspy.bat
│  │  ├─ FinalProject.Debug.cspy.ps1
│  │  ├─ FinalProject.Debug.driver.xcl
│  │  ├─ FinalProject.Debug.general.xcl
│  │  ├─ FinalProject.dbgdt
│  │  ├─ FinalProject.dnx
│  │  ├─ FinalProject.reggroups
│  │  └─ workspace.wsdt
│  └─ workspace.eww
```
Description:
code written with c programming, and developed on msp430 and his timers, the goal its control and measures engine rotations

Requirement:
- ms430 , engine , wires ,transistors
- LCDutilities library
- any software to communication over USB

code :
```c

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


```

Pictures:
![](https://user-images.githubusercontent.com/33747218/137758851-286516fc-5a44-488b-8458-e28342c46da7.png)
![](https://user-images.githubusercontent.com/33747218/137758882-81f8097e-d6fa-4a10-9237-2fabf8e96863.png)
![](https://user-images.githubusercontent.com/33747218/137758886-5566ee9c-ae31-4aa8-8707-05f892986234.png)
![](https://user-images.githubusercontent.com/33747218/137758888-442ae784-b96d-48c5-92ce-084240ecfea8.png)
