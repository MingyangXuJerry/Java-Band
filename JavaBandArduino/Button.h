#ifndef Buttons_h
#define Buttons_h

#include "Arduino.h"

class Button
{
  public:
    Button(int);
    Button(int, int);
    
    void process();
    void pressHandler(void (*f)(int));
    void releaseHandler(void (*f)(int));
    void (*pressCallback)(int);
    void (*releaseCallback)(int);
    bool isOn();
    void setState(bool);

    int val;
    int buttonPin;
    bool buttonState;
    bool lastButtonState;
    bool on;

  private:
};

#endif
