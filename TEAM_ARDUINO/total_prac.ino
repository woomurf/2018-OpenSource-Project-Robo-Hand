/*
 * 11월 27일 전체 구동 시험 코드
 * 가위 바위 보 오케이 구현 
 * 전력 문제 없음, 대체로 잘 됨
 * 다음까지 부드러운 움직임 구현
 */

#include "HCPCA9685.h"
#include <Servo.h>
#include <SoftwareSerial.h>
#define I2CAdd 0x40
 int TX = 10;
 int RX = 11;
int Servo4Position;
int Servo8Position;
Servo servo5; // thumb
Servo servo6; // index
Servo servo7; // mid
HCPCA9685 HCPCA9685(I2CAdd);
SoftwareSerial my_BT(TX,RX);

// first - 0
void first(){
  HCPCA9685.Servo(4,150); // ring
  servo5.write(0);  // thumb
  servo6.write(0);  // index
  servo7.write(180);  // mid
  HCPCA9685.Servo(8,150); // baby
  
}
void second(){
  servo5.write(0);
  servo6.write(0);
  servo7.write(0);
  HCPCA9685.Servo(4,600);
  HCPCA9685.Servo(8,600);
}
// third - 5
void third(){
  HCPCA9685.Servo(4,150);
  servo5.write(130);
  servo6.write(70);
  servo7.write(180);
  HCPCA9685.Servo(8,150);
}
 
void setup(){
  Serial.begin(9600);
  my_BT.begin(9600);
  HCPCA9685.Init(SERVO_MODE);
  HCPCA9685.Sleep(false);
  servo5.attach(5); // thumb
  servo5.write(0);
  servo6.attach(6); // index
  servo6.write(0);
  servo7.attach(7); // mid
  servo7.write(180);
  Servo4Position=150;
  Servo8Position=150;
  
  HCPCA9685.Servo(4,Servo4Position);  // ring
  HCPCA9685.Servo(8,Servo8Position);  // baby
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
