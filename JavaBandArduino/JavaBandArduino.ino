
#include "Button.h"
#include "Switch.h"

int sw1Pin = 33;
int joyLYPin = A16;
int joyLXPin = A15;
int joyLKPin = A14;
int joyRYPin = A5;
int joyRXPin = A4;
int joyRKPin = A3;
int joyLY = 0;
int joyLX = 0;
int joyLK = 0;
int joyRY = 0;
int joyRX = 0;
int joyRK = 0;
int lx = 0;
int ly = 0;
int rx = 0;
int ry = 0;
boolean lk = false;
boolean rk = false;

int ledPins[] = {7, 6, 5, 4, 3, 2};
int currentIns = 0;
Switch sw1(joyLKPin, 0);
Button bt1(joyRKPin, 1);

void setup() {
  Serial.begin(9600);
  for (int i = 0; i < sizeof(ledPins) / sizeof(ledPins[0]); i++) {
    pinMode(ledPins[i], OUTPUT);
  }
  sw1.switchedOnHandler(switchedOn);
  sw1.switchedOffHandler(switchedOff);
  bt1.pressHandler(onPress);
  bt1.releaseHandler(onRelease);
}

void loop() {
  if (sw1.isOn()) {
    turnOnLow(ledPins[currentIns]);
    if (joyLX <= 5) {
      turnOff(ledPins[currentIns]);
      currentIns = (currentIns - 1) % (sizeof(ledPins) / sizeof(ledPins[0]));
    }
    if (joyLX >= 1020) {
      turnOff(ledPins[currentIns]);
      currentIns = (currentIns + 1) % (sizeof(ledPins) / sizeof(ledPins[0]));
    }
  } else {
    turnOnHigh(ledPins[currentIns]);
  }

  JoyLHandler();
  JoyRHandler();
  //  Serial.println("currentIns");
  //  Serial.println(currentIns);
  //  Serial.println();

  lx = map(joyLX, 0, 1023, 0, 1023);
  ly = map(joyLY, 0, 1023, 1023, 0);
  rx = map(joyRX, 0, 1023, 0, 1023);
  ry = map(joyRY, 0, 1023, 1023, 0);

  //  Serial.println("Mapping:");
  //  Serial.print("x ");
  //  Serial.print(x);
  //  Serial.print(" y ");
  //  Serial.println(y);
  //  Serial.println("mappedRX ");
  //  Serial.print(getMappedRX());
  //  Serial.print(" mappedRY ");
  //  Serial.println(getMappedRY());

  Serial.print(getMappedLX());
  Serial.print(" ");
  Serial.print(getMappedLY());
  Serial.print(" ");
  Serial.print(getMappedRX());
  Serial.print(" ");
  Serial.print(getMappedRY());
  Serial.print(" ");
  Serial.print(getLK());
  Serial.print(" ");
  Serial.print(getRK());
  Serial.print(" ");
  Serial.print(currentIns);
  Serial.print("\n");
  delay(10);
}

void JoyLHandler() {
  joyLY = analogRead(joyLYPin);
  joyLX = analogRead(joyLXPin);
  joyLK = analogRead(joyLKPin);
  //  Serial.println("Left joystick:");
  //  Serial.print(" LX ");
  //  Serial.print(joyLX);
  //  Serial.print(" LY ");
  //  Serial.println(joyLY);
  if (joyLX >= 1022 || joyLX <= 1) {
    delay(200);
  }
  sw1.process();
  lk = sw1.isOn();
}
void JoyRHandler() {
  joyRY = analogRead(joyRYPin);
  joyRX = analogRead(joyRXPin);
  joyRK = analogRead(joyRKPin);
  //  Serial.println("Right joystick:");
  //  Serial.print(" RX ");
  //  Serial.print(joyRX);
  //  Serial.print(" RY ");
  //  Serial.println(joyRY);
  bt1.process();
  rk = bt1.isOn();
}
void turnOnLow(int out) {
  analogWrite(out, 20);
  // turn on one LED
}

void turnOnHigh(int out) {
  analogWrite(out, 100);
}

void turnOff(int out) {
  analogWrite(out, 0);
  // turn off one LED
}

void onPress(int val) {
}

void onRelease(int val) {
}

void switchedOn(int val) {
}

void switchedOff(int val) {
}

int getLX() {
  return lx;
}

int getLY() {
  return ly;
}

int getRX() {
  return rx;
}

int getRY() {
  return ry;
}
int getCurrentInstrument() {
  return currentIns;
}

double getMappedLX() {
  double _lx = (double)lx;
  double mappedLX = _lx / 1023.;
  return mappedLX;
}

double getMappedLY() {
  double _ly = (double) ly;
  double mappedLY = _ly / 1023.;
  return mappedLY;
}
double getMappedRX() {
  double _rx = (double)rx;
  double mappedRX = _rx / 1023.;
  return mappedRX;
}

double getMappedRY() {
  double _ry = (double) ry;
  double mappedRY = _ry / 1023.;
  return mappedRY;
}

boolean getLK() {
  return lk;
}
boolean getRK() {
  return rk;
}
