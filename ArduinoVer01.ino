#include<SoftwareSerial.h>  //블루투스 송수신을 위한 라이브러리
#include<Servo.h>            //서보모터 사용을 위한 라이브러리

Servo servo1;  //서보모터 선언
int TX=10;     //블루투스 작동을 위한 RX,TX선언 및 연결
int RX=11;
SoftwareSerial my_BT(TX,RX); //블루투스 연결을 my_BT라는 이름으로 선언.

//String readString; string형을 

void first(){
  servo1.write(30);
}
void second(){
  servo1.write(90);
}
void third(){
  servo1.write(180);
}

void setup(){
  Serial.begin(9600); //시리얼통신과 블루투스간의 연결 설정. 9600은 연결 속도를 의미한다.
  my_BT.begin(9600);
  servo1.attach(7);   //서보모터 포트 설정

/*
  Serial.print("AT+NAME");
  Serial.print("MYBT");
  Serial.print("\r\n");
  delay(100);
  Serial.print("AT+PIN");
  Serial.print("1111");
  Serial.print("\r\n");
  delay(100);
  Serial.print("AT+BAUD4");
  Serial.print("\r\n");
  delay(100);
  */
}

void loop(){
  if(my_BT.available()){ //만약 블루투스가 연결상태일 경우
    /*
    int value= my_BT.parseInt(); //bluetooth spp를 이용한 데이터 전송은 문자열만 해당되므로 INT형으로 변경, value에 저장
    my_BT.write(value);         //디바이스 내에 value값을 표시
    servo1.write(value);        //서보모터를 value 값만큼 회전
    */
    
    char pose = my_BT.read();
    if(pose=='a'){
      first();
    }
    if(pose=='b'){
      second();
    }
    if(pose=='c'){
      third();
    }
    
    //Serial.write(my_BT.read()); //초기설정용
  }
  if(Serial.available()){       //serial이 연결상태일 경우 시리얼 모니터에서 보낸 값을 디바이스 내에 표시 //현재로선 필요없는 기능
    my_BT.write(Serial.read());
  }
}

/*
 * 문제점
 * 1. 처음 bluetooth 설정시 Serial monitor 내에 AT+NAME, AT+PIN, AT+BAUD를 설정해 줘야 하는데 a,b,c 만 input값을 받는 현재로서는 설정을 바꿔줄수가없음. *초기 설정시에는 초기설정용 코드를 활성화하고 나머지를 비활성화할것
 * 2. 디바이스에서 숫자를 받아 직접적으로 서보모터 각도를 조절하려 했으나 실패 **두자리 이상의 문자를 받는데 문제가 있는듯
 * 3. Serial monitor을 사용하지 않고 주석처리된 부분처럼 설정을 변경해주려 했으나 실패
 * 
 * 현재로서의 결론
 * 각각의 손동작을 first,second,third 함수처럼 각각 서보모터에서 설정해 줄 필요가 있음.
 * 안드로이드 디바이스에서는 주먹은 a, 가위는 b, 보는 c 처럼 손동작을 각각 하나의 문자로 넘겨줘야함.
 * 각각에 해당하는 함수로 구현 가능할듯
 */
 
