#ifndef Switches_h
#define Switches_h

#include "Arduino.h"

class Switch
{
  public:
    Switch(int);
    Switch(int, int);
   
    void process();
    void switchedOnHandler(void (*f)(int));
    void switchedOffHandler(void (*f)(int));
    void (*switchedOnCallback)(int);
    void (*switchedOffCallback)(int);
    bool isOn();
    void flipState();
    void setState(bool);

    int val;
    int switchPin;
    bool switchState;
    bool lastSwitchState;
    bool on;

  private:
};

#endif
