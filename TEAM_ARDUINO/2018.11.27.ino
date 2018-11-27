#include "HCPCA9685.h"
#include <Servo.h>
#include <SoftwareSerial.h>
#define I2CAdd 0x40
 int TX = 10;
 int RX = 11;
int Servo4Position;
int Servo8Position;
Servo servo5;
Servo servo6;
Servo servo7;
HCPCA9685 HCPCA9685(I2CAdd);
SoftwareSerial my_BT(TX,RX);

//함수
void first(){
  HCPCA9685.Servo(4,150);
  servo5.write(0);
  servo6.write(0);
  servo7.write(0);
  HCPCA9685.Servo(8,150);
  
}
void second(){
  HCPCA9685.Servo(4,375);
  servo5.write(90);
  servo6.write(90);
  servo7.write(90);
  HCPCA9685.Servo(8,375);
}
void third(){
  HCPCA9685.Servo(4,600);
  servo5.write(180);
  servo6.write(180);
  servo7.write(180);
  HCPCA9685.Servo(8,600);
}
 
void setup(){
  Serial.begin(9600);
  my_BT.begin(9600);
  HCPCA9685.Init(SERVO_MODE);
  HCPCA9685.Sleep(false);
  servo5.attach(5);
  servo5.write(0);
  servo6.attach(6);
  servo6.write(0);
  servo7.attach(7);
  servo7.write(0);
  Servo4Position=150;
  Servo8Position=150;
  
  HCPCA9685.Servo(4,Servo4Position);
  HCPCA9685.Servo(8,Servo8Position);
}
 
void loop(){
    if(my_BT.available()){ //만약 블루투스가 연결상태일 경우
    char pose = my_BT.read();
    if(pose=='1'){
      first();
    }
    if(pose=='2'){
      second();
    }
    if(pose=='3'){
      third();
    }
    }
    if(Serial.available()){
      my_BT.write(Serial.read());
    }
}
