#include "Arduino.h"
#include "Switch.h"

Switch::Switch(int _switchPin)
: Switch(_switchPin, _switchPin)
{}

Switch::Switch(int _switchPin, int _val)
{
  switchPin = _switchPin;
  val = _val;
  pinMode(switchPin, INPUT);
  on = LOW;
  switchState = on;
  Serial.println(isOn());
}

void Switch::process()
{
  lastSwitchState = switchState;
  switchState = digitalRead(switchPin);

  if (lastSwitchState == LOW && switchState == HIGH) {
    flipState();
    if (isOn()){
      switchedOnCallback(val);
    } else {
      switchedOffCallback(val);
    }
    delay(3);
  }
}

bool Switch::isOn(){
  return on;
}

void Switch::flipState(){
  on = !on;
}

void Switch::setState(bool _on){
  on = _on;
}

void Switch::switchedOnHandler(void (*f)(int))    //Switched on
{
  switchedOnCallback = *f;
}

void Switch::switchedOffHandler(void (*f)(int))  //Switched off
{
  switchedOffCallback = *f;
}
