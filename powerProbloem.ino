#include "HCPCA9685.h"
#include <Servo.h>
#define I2CAdd 0x40
 
int Servo4Position;
int Servo8Position;
int SW1=7;
int SW2=8;
Servo servo4;
Servo servo3;
HCPCA9685 HCPCA9685(I2CAdd);
 
void setup(){
 
  HCPCA9685.Init(SERVO_MODE);
  HCPCA9685.Sleep(false);
  servo4.attach(4);
  servo4.write(0);
  servo3.attach(3);
  servo3.write(0);
  Servo4Position=240;
  Servo8Position=240;
  
  HCPCA9685.Servo(4,Servo4Position);
  HCPCA9685.Servo(8,Servo8Position);
  HCPCA9685.Servo(11,Servo4Position);
  //HCPCA9685.Servo(12,Servo4Position);
  //HCPCA9685.Servo(15,Servo8Position);
  pinMode(SW1, INPUT_PULLUP);
  pinMode(SW2, INPUT_PULLUP);
}
 
void loop(){
 
  if(!digitalRead(SW1)==HIGH) {
  delay(1000);
    Servo4Position=30;
    Servo8Position=30;
     HCPCA9685.Servo(4,Servo4Position); 
     HCPCA9685.Servo(8,Servo8Position);
     HCPCA9685.Servo(11,Servo4Position);
    // HCPCA9685.Servo(12,Servo4Position);
     servo4.write(0);
     servo3.write(0);
     //HCPCA9685.Servo(15,Servo8Position);
}
   if(!digitalRead(SW2)==HIGH){
  delay(1000);
    Servo4Position=410;
    Servo8Position=410;
     HCPCA9685.Servo(4,Servo4Position);
     HCPCA9685.Servo(11,Servo4Position); 
     HCPCA9685.Servo(8,Servo8Position);
     //HCPCA9685.Servo(12,Servo4Position);
     servo3.write(180);
     servo4.write(180);
     //HCPCA9685.Servo(15,Servo8Position); 
 
}
}
