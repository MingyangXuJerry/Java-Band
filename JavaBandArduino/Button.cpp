#include "Arduino.h"
#include "Button.h"

Button::Button(int _buttonPin)
: Button(_buttonPin, _buttonPin)
{}

Button::Button(int _buttonPin, int _val)
{
  buttonPin = _buttonPin;
  val = _val;
  on = LOW;
  pinMode(buttonPin, INPUT);
}

void Button::process()
{
  lastButtonState = buttonState;
  buttonState = digitalRead(buttonPin);

  if (lastButtonState == LOW && buttonState == HIGH) {
    pressCallback(val);
    setState(HIGH);
    delay(3);
  }
  if (lastButtonState == HIGH && buttonState == LOW) {
    releaseCallback(val);
    setState(LOW);
    delay(3);
  }
}

bool Button::isOn(){
  return on;
}

void Button::setState(bool _on){
  on = _on;
}

void Button::pressHandler(void (*f)(int))    //button down
{
  pressCallback = *f;
}

void Button::releaseHandler(void (*f)(int))  //button release
{
  releaseCallback = *f;
}
