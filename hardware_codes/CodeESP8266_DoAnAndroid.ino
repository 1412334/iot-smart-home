#include "DHT.h"
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
//#include <SocketIOClient.h>
//#include <ArduinoJson.h>
 
//DHT
#define DHTPIN 4     // what digital pin we're connected to
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

//LED, MOTOR
#define FAN 5
#define AIRCON 14
#define LED  2

//wifi
//SocketIOClient client;
#define WIFI_SSID "Ceslab"          
#define WIFI_PASSWORD "labtuivuilam11"  

//Firebase
#define FIREBASE_HOST "smarthome-f50ea.firebaseio.com"
#define FIREBASE_AUTH "Uhbm5WbEyTJdbX1aZ3ThYGpIczFFUj7RIedIF47E"

#define humidityValuePath "measure/2/value"
#define temperatureValuePath "measure/1/value"
#define ledValuePath "component/1/value"
#define fanValuePath "component/3/value"
#define airConValuePath "component/2/value"
#define ledOpacityPath "component/1/opacity"

int ledValue;
int fanValue;
int airConValue;
int ledOpacity;

//char host[] = "ec2-18-218-68-216.us-east-2.compute.amazonaws.com";  //Địa chỉ IP dịch vụ, hãy thay đổi nó theo địa chỉ IP Socket server của bạn.
//int port = 3000;                  //Cổng dịch vụ socket server do chúng ta tạo!
//
//extern String RID;
//extern String Rfull;
//
//unsigned long previousMillis = 0;
//long interval = 2000;

void setup() {
  Serial.begin(9600);
  Serial.println("start");
  //DHT
  dht.begin();

  //
  pinMode(FAN, OUTPUT);
  pinMode(AIRCON, OUTPUT);
  pinMode(LED, OUTPUT);

  digitalWrite(FAN, LOW);
  digitalWrite(AIRCON, LOW);
  digitalWrite(LED, LOW);

  //WIFI
  //connect wifi
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  //wait wifi connect
  while (WiFi.status() != WL_CONNECTED) {
     Serial.println("Connecting....");
     digitalWrite(LED, HIGH);
     delay(500);
     digitalWrite(LED, LOW);
     delay(500);
     
  }
  digitalWrite(LED, LOW);
  Serial.println("Connected");
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
//   if (!client.connect(host, port)) {
//      Serial.println("Web_Server: Not connected");
//      return;
//  }

  Serial.println("EndSetUp");
}

void loop() {
  // Wait a few seconds between measurements.
  delay(2000);
  Serial.println("loop");
  
  //DHT 11
  float h = dht.readHumidity();
  float t = dht.readTemperature();

  // Check if any reads failed and exit early (to try again).
  if (isnan(h) || isnan(t)) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }

  Serial.print("Humidity: ");
  Serial.print(h);
  Serial.print(" \t");
  Serial.print("Temperature: ");
  Serial.print(t);
  Serial.println(" *C ");

  Firebase.setFloat(humidityValuePath, h);
  Firebase.setFloat(temperatureValuePath, t);

  ledValue = Firebase.getInt(ledValuePath);
  fanValue = Firebase.getInt(fanValuePath);
  airConValue = Firebase.getInt(airConValuePath);
  ledOpacity = Firebase.getInt(ledOpacityPath); 
  
   analogWrite(LED, (int) (ledValue*ledOpacity*10.23));
   digitalWrite(FAN, fanValue); 
   digitalWrite(AIRCON, airConValue); 
   
  if (Firebase.failed()) {
      Serial.print("setting /message failed:");
      Serial.println(Firebase.error());  
      return;
  }

 
//  //WIFI
//    if (client.monitor()) {
//      Serial.println("aaa");
//      Serial.println(RID);
//      Serial.println(Rfull);
//    }
//
//    if (millis() - previousMillis > interval) {
//    previousMillis = millis();
//  
//    //gửi sự kiện "atime" là một JSON chứa tham số message có nội dung là Time please?
//    client.send("temperature", "value", "1111");
//    }
//    
//    //check connect web server
//    if (!client.connected()){
//        if (!client.connect(host, port)) {
//        Serial.println("Web_Server: Not connected");
//        return;
//      }  
//    }  
}


