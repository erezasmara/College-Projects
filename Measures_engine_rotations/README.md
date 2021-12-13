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
- ms430FR6989
-  motor dc 
-  wires
- Trem cool communication over USB

Pictures:
![](https://user-images.githubusercontent.com/33747218/137758886-5566ee9c-ae31-4aa8-8707-05f892986234.png)


Start:
1) download IDE IAR system.
2) connect your ms430FR6989
3) download and debug motor.c file to the micro controller
4) send commands via trem cool
