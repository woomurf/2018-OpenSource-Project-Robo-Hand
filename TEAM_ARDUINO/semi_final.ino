/*
 * 12월 1일 전체 구동 시험 코드
 * 가위 바위 보 오케이 구현 
 * 1,2,3,4,fuck,call 추가 구혀ㄴ
 * 전력 문제 없음, 대체로 잘 됨
 * 부드러운 움직임 구현(step 30)
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

int THUMB = 0;
int INDEX = 0;
int MID = 180;
int RING = 150;
int BABY = 150;

// first - 0
void paper(){

  int numberOfBranch = 180/30;
  int numberOfPca = 450/30;

  for(int i = 0; i<30; i++)
  {
    if(THUMB != 0)
    {
      servo5.write(THUMB - numberOfBranch);
      THUMB = THUMB - numberOfBranch;
    }
    if(INDEX != 0)
    {
      servo6.write(INDEX - numberOfBranch);
      INDEX = INDEX-numberOfBranch;
    }
    if(MID != 180)
    {
      servo7.write(MID+numberOfBranch);
      MID = MID + numberOfBranch;
    }
    if(RING > 150)
    {
      HCPCA9685.Servo(4,RING - numberOfPca);
      RING = RING - numberOfPca;
    }
    if(BABY > 150)
    {
      HCPCA9685.Servo(8,BABY - numberOfPca);
      BABY = BABY - numberOfPca;
    }
    delay(50);
  }
  

}


void rock(){

  int numberOfBranch = 180/30;  // 6
  int numberOfPca = 450/30; // 15

  for(int i = 0; i<30; i++)
  {
    if(THUMB != 180)
    { 
      servo5.write(THUMB + numberOfBranch);
      THUMB = THUMB + numberOfBranch;
    }
    if(INDEX != 180)
    {
      servo6.write(INDEX + numberOfBranch);
      INDEX = INDEX + numberOfBranch;
    }
    if(MID != 0)
    {
      servo7.write(MID - numberOfBranch);
      MID = MID - numberOfBranch;
    }
    if(RING != 600)
    {
      HCPCA9685.Servo(4,RING + numberOfPca);
      RING = RING + numberOfPca;
    }
    if(BABY != 600)
    {
      HCPCA9685.Servo(8,BABY + numberOfPca);
      BABY = BABY + numberOfPca;
    }
    delay(50);
  }
}

void scissor()
{
  int numberOfBranch = 180/30;
  int numberOfPca = 450/30;

  for(int i = 0; i<30; i++)
  {
    if(THUMB != 0)
    {
      servo5.write(THUMB - numberOfBranch);
      THUMB = THUMB - numberOfBranch;
    }
    if(INDEX != 0)
    {
      servo6.write(INDEX - numberOfBranch);
      INDEX = INDEX-numberOfBranch;
    }
    if(MID != 0)
    {
      servo7.write(MID-numberOfBranch);
      MID = MID - numberOfBranch;
    }
    if(RING != 600)
    {
      HCPCA9685.Servo(4,RING + numberOfPca);
      RING = RING + numberOfPca;
    }
    if(BABY != 600)
    {
      HCPCA9685.Servo(8,BABY + numberOfPca);
      BABY = BABY + numberOfPca;
    }
    delay(50);
  }
}
// third - 5

void one(){

  int numberOfBranch = 180/30;
  int numberOfPca = 450/30;

  for(int i = 0; i<30; i++)
  {
    if(THUMB != 180)
    {
      servo5.write(THUMB + numberOfBranch);
      THUMB = THUMB + numberOfBranch;
    }
    if(INDEX != 0)
    {
      servo6.write(INDEX - numberOfBranch);
      INDEX = INDEX - numberOfBranch;
    }
    if(MID != 0)
    {
      servo7.write(MID - numberOfBranch);
      MID = MID  - numberOfBranch;
    }
    if(RING != 600)
    {
      HCPCA9685.Servo(4,RING + numberOfPca);
      RING = RING + numberOfPca;
    }
    if(BABY != 600)
    {
      HCPCA9685.Servo(8,BABY + numberOfPca);
      BABY = BABY + numberOfPca;
    }
    delay(50);
  }
}

void two(){

  int numberOfBranch = 180/30;
  int numberOfPca = 450/30;

  for(int i = 0; i<30; i++)
  {
    if(THUMB != 180)
    {
      servo5.write(THUMB + numberOfBranch);
      THUMB = THUMB + numberOfBranch;
    }
    if(INDEX != 0)
    {
      servo6.write(INDEX - numberOfBranch);
      INDEX = INDEX - numberOfBranch;
    }
    if(MID != 180)
    {
      servo7.write(MID + numberOfBranch);
      MID = MID  + numberOfBranch;
    }
    if(RING != 600)
    {
      HCPCA9685.Servo(4,RING + numberOfPca);
      RING = RING + numberOfPca;
    }
    if(BABY != 600)
    {
      HCPCA9685.Servo(8,BABY + numberOfPca);
      BABY = BABY + numberOfPca;
    }
    delay(50);
  }
}

void three(){
  
  int numberOfBranch = 180/30;
  int numberOfPca = 450/30;

  for(int i = 0; i<30; i++)
  {
    if(THUMB != 180)
    {
      servo5.write(THUMB + numberOfBranch);
      THUMB = THUMB + numberOfBranch;
    }
    if(INDEX != 0)
    {
      servo6.write(INDEX - numberOfBranch);
      INDEX = INDEX - numberOfBranch;
    }
    if(MID != 180)
    {
      servo7.write(MID + numberOfBranch);
      MID = MID  + numberOfBranch;
    }
    if(RING != 150)
    {
      HCPCA9685.Servo(4,RING - numberOfPca);
      RING = RING - numberOfPca;
    }
    if(BABY != 600)
    {
      HCPCA9685.Servo(8,BABY + numberOfPca);
      BABY = BABY + numberOfPca;
    }
    delay(50);
  }
}

void four(){
  
  int numberOfBranch = 180/30;
  int numberOfPca = 450/30;

  for(int i = 0; i<30; i++)
  {
    if(THUMB != 180)
    {
      servo5.write(THUMB + numberOfBranch);
      THUMB = THUMB + numberOfBranch;
    }
    if(INDEX != 0)
    {
      servo6.write(INDEX - numberOfBranch);
      INDEX = INDEX - numberOfBranch;
    }
    if(MID != 180)
    {
      servo7.write(MID + numberOfBranch);
      MID = MID  + numberOfBranch;
    }
    if(RING != 150)
    {
      HCPCA9685.Servo(4,RING - numberOfPca);
      RING = RING - numberOfPca;
    }
    if(BABY != 150)
    {
      HCPCA9685.Servo(8,BABY - numberOfPca);
      BABY = BABY - numberOfPca;
    }
    delay(50);
  }
}

void call()
{
 int numberOfBranch = 180/30;
  int numberOfPca = 450/30;

  for(int i = 0; i<30; i++)
  {
    if(THUMB != 0)
    {
      servo5.write(THUMB - numberOfBranch);
      THUMB = THUMB - numberOfBranch;
    }
    if(INDEX != 180)
    {
      servo6.write(INDEX + numberOfBranch);
      INDEX = INDEX + numberOfBranch;
    }
    if(MID != 0)
    {
      servo7.write(MID - numberOfBranch);
      MID = MID  - numberOfBranch;
    }
    if(RING != 600)
    {
      HCPCA9685.Servo(4,RING + numberOfPca);
      RING = RING + numberOfPca;
    }
    if(BABY != 150)
    {
      HCPCA9685.Servo(8,BABY - numberOfPca);
      BABY = BABY - numberOfPca;
    }
    delay(50);
  }
}

void fuck()
{
  int numberOfBranch = 180/30;
  int numberOfPca = 450/30;

  for(int i = 0; i<30; i++)
  {
    if(THUMB != 180)
    {
      servo5.write(THUMB + numberOfBranch);
      THUMB = THUMB + numberOfBranch;
    }
    if(INDEX != 180)
    {
      servo6.write(INDEX + numberOfBranch);
      INDEX = INDEX + numberOfBranch;
    }
    if(MID != 180)
    {
      servo7.write(MID + numberOfBranch);
      MID = MID  + numberOfBranch;
    }
    if(RING != 600)
    {
      HCPCA9685.Servo(4,RING + numberOfPca);
      RING = RING + numberOfPca;
    }
    if(BABY != 600)
    {
      HCPCA9685.Servo(8,BABY + numberOfPca);
      BABY = BABY + numberOfPca;
    }
    delay(50);
  }
}

void setup(){
  Serial.begin(9600);
  my_BT.begin(9600);
  
  HCPCA9685.Init(SERVO_MODE);
  HCPCA9685.Sleep(false);
  
  servo5.attach(5); // thumb
  servo5.write(THUMB);
  servo6.attach(6); // index
  servo6.write(INDEX);
  servo7.attach(7); // mid
  servo7.write(MID);
  
  HCPCA9685.Servo(4,RING);  // ring
  HCPCA9685.Servo(8,BABY);  // baby
}
 
void loop(){
    if(my_BT.available()){ //만약 블루투스가 연결상태일 경우
    char pose = my_BT.read();
    if(pose=='1'){
      one();
    }
    else if(pose=='2'){
      two();
    }
    else if(pose=='3'){
      three();
    }
    else if(pose=='4'){
      four();
    }
    else if(pose=='5'){
      paper();
    }
    else if(pose=='6'){
      scissor();
    }
    else if(pose=='7'){
      rock();
    }
    else if(pose=='8'){
      fuck();
    }
    else if(pose=='9'){
      call();
    }
    
    }
    if(Serial.available()){
      my_BT.write(Serial.read());
    }
}

//void loop()
//{
//  first();
//  delay(3000);
//  second();
//  delay(3000);
////////  third();
////////  delay(3000);
//  fourth();
//  delay(3000);
//  first();
//  delay(3000);
//  fifth();
//  delay(3000);
//}
