#include<SoftwareSerial.h>  //블루투스 송수신을 위한 라이브러리
#include<Servo.h>            //서보모터 사용을 위한 라이브러리

Servo servo1;  //서보모터 선언
Servo servo2;
Servo servo3;
Servo servo4;
Servo servo5;

int TX=10;     //블루투스 작동을 위한 RX,TX선언 및 연결
int RX=11;
SoftwareSerial my_BT(TX,RX); //블루투스 연결을 my_BT라는 이름으로 선언.

//String readString; string형을 

void first(){
  servo1.write(0);
  servo2.write(0);
  servo3.write(0);
  servo4.write(0);
  servo5.write(0);
}
void second(){
  servo1.write(90);
  servo2.write(90);
  servo3.write(90);
  servo4.write(90);
  servo5.write(90);
}
void third(){
  servo1.write(180);
  servo2.write(180);
  servo3.write(180);
  servo4.write(180);
  servo5.write(180);
}

void setup(){
  Serial.begin(9600); //시리얼통신과 블루투스간의 연결 설정. 9600은 연결 속도를 의미한다.
  my_BT.begin(9600);
  servo1.attach(3);   //서보모터 포트 설정
  servo2.attach(4);
  servo3.attach(5);
  servo4.attach(6);
  servo5.attach(7);
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
    
    //Serial.write(my_BT.read()); //초기설정용
  }
  if(Serial.available()){       //serial이 연결상태일 경우 시리얼 모니터에서 보낸 값을 디바이스 내에 표시 //현재로선 필요없는 기능
    my_BT.write(Serial.read());
  }
}
