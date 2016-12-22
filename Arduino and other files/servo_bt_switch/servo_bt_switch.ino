// DEVICE TO BLUETOOTH
/*
  D11 (arduino)   >>>  Rx(bluetooth)
  D10  (arduino) >>>  Tx (bluetooth)
*/

#include <SoftwareSerial.h>
#include <Servo.h>

#include <string.h>
#define len 3
#define SERVO_PIN 9 
#define BT_STATE_PIN 2
#define SWITCH_PIN 4

SoftwareSerial serialObj(10, 11);

char ch; // the data given from Computer
char pass[len]={'z','i','m'};
char data[len];

int btFound=0;
int i=0,j=0;

int isMatched=1;

//servo codes
Servo myservo;  // create servo object to control a servo
// twelve servo objects can be created on most boards

int pos = 0;    // variable to store the servo position
int switch_state=0;

void setup()
{
  // put your setup code here, to run once:

	serialObj.begin(9600);
	pinMode(LED_BUILTIN, OUTPUT);
	pinMode(BT_STATE_PIN, INPUT);
	pinMode(SWITCH_PIN, INPUT);

	//servo codes
	myservo.attach(SERVO_PIN); // attaches the servo on pin 9 to the servo object
	myservo.write(0);
	delay(2000);
	myservo.detach();
  
}

void loop()
{
  // put your main code here, to run repeatedly:
  digitalWrite(SWITCH_PIN,HIGH);
  switch_state=digitalRead(SWITCH_PIN);
  while(switch_state==HIGH){
    switch_state=digitalRead(SWITCH_PIN);
  }
  
  serialObj.println(switch_state);
  
   digitalWrite(LED_BUILTIN,switch_state);
   delay(200);
  //always checks whether switch is on
	if (switch_state==LOW && digitalRead(BT_STATE_PIN)==HIGH)
	{
		if (serialObj.available()) btFound=1;
		else btFound=0;



		if (btFound)
		{
		  //serialObj.println(pass);
		  ch = serialObj.read();
		  //Serial.println(ch);
		  serialObj.println(ch);

			if (ch!='.')
			{
				if ((int)ch!=-1)
				{
				  //serialObj.println("normal");
				  //serialObj.println(ch);
				  data[i]=ch;
				  i++;
				}
				else
				{
				  //serialObj.println("Garbage");
				}
				 
			}
			else
			{
				serialObj.println(data[0]);
				serialObj.println(data[1]);
				serialObj.println(data[2]);

				i=0;

				for(j=0;j<len;j++)
				{
					if(data[j]!=pass[j])
					{
						isMatched=0;
						break;
					}
				}

				if (isMatched==1) // if number 1 pressed ....
				{
					digitalWrite(LED_BUILTIN, 1);
					serialObj.println("password matched");
					delay(100);
					digitalWrite(LED_BUILTIN, 0);

					//Servo Works

					myservo.attach(SERVO_PIN);
					myservo.write(100);
					pos=180;
					delay(2000);

					delay(200);
					myservo.detach();



				  
				}
				else
				{
					digitalWrite(LED_BUILTIN, 0);
					serialObj.println("Try Again");

					isMatched=1;
				}
			}
		}

	}

  //if switch and bluetooth both are off, then we lock
	else
	{

  		if(digitalRead(BT_STATE_PIN)==LOW)
  		{
//        digitalWrite(SWITCH_PIN,HIGH);
//        switch_state=digitalRead(SWITCH_PIN);
//        while(switch_state==HIGH){
//          switch_state=digitalRead(SWITCH_PIN);
//        }
        
  			if (pos!=0)
  			{
  				myservo.attach(SERVO_PIN);
  				myservo.write(0);
  				pos=0;
  				delay(2000);
  				myservo.detach();
  			}
  		
		  }
    

	}
  
  
  	delay(100);       // prepare for next data ...
}

