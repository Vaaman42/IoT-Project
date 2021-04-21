#include <WS2801FX.h>           // https://github.com/r41d/WS2801FX
#include <Adafruit_WS2801.h>    // https://github.com/adafruit/Adafruit-WS2801-Library

/**********************************RGB CONTROLLERS**********************************/
uint8_t LED_COUNT = 19;
uint8_t LED_DATA_PIN = A1;
uint8_t LED_CLOCK_PIN = A0;
WS2801FX ws2801fx(LED_COUNT, LED_DATA_PIN, LED_CLOCK_PIN, WS2801_RGB);

/**********************************KEYBOARD MATRIX**********************************/
byte rows[] = {13,7};
const uint8_t rowCount = sizeof(rows)/sizeof(rows[0]);

byte cols[] = {2,3,4,5,6,8,9,10,11,12};
const uint8_t colCount = sizeof(cols)/sizeof(cols[0]);

bool keys[colCount][rowCount];
bool previousKeys[colCount][rowCount];

int currentRGB = 0;

void setup() {
  Serial.begin(31250);
  ws2801fx.init();
  ws2801fx.setBrightness(75);
  ws2801fx.setSpeed(200);
  ws2801fx.setMode(FX_MODE_RAINBOW_CYCLE);
  ws2801fx.start();

  for (uint8_t i=0; i<rowCount; i++) {
    pinMode(rows[i], INPUT);
  }
  for (uint8_t i=0; i<colCount; i++) {
    pinMode(cols[i], INPUT_PULLUP);
  }
  
  for (uint8_t col=0; col<colCount; col++) {
    pinMode(cols[col], OUTPUT);
    digitalWrite(cols[col], LOW);
    for (uint8_t row=0; row<rowCount; row++) {
      pinMode(rows[row], INPUT_PULLUP);
      previousKeys[col][row] = digitalRead(rows[row]) == LOW;
      pinMode(rows[row], INPUT);
    }
    pinMode(cols[col], INPUT);
  }
}

void loop() {
  ws2801fx.service();
  readMatrix();
  checkPressedKeys();
}

void readMatrix() {
  for (uint8_t col=0; col<colCount; col++) {
    pinMode(cols[col], OUTPUT);
    digitalWrite(cols[col], LOW);
    for (uint8_t row=0; row<rowCount; row++) {
      pinMode(rows[row], INPUT_PULLUP);
      keys[col][row] = digitalRead(rows[row]) == LOW;
      pinMode(rows[row], INPUT);
    }
    pinMode(cols[col], INPUT);
  }
}

void checkPressedKeys() {
  byte note = 60;
  for (byte col=0; col<colCount; col++) {
    for (byte row=0; row<rowCount; row++) {
      if (keys[col][row] && !previousKeys[col][row]) {
        previousKeys[col][row] = true;
        if (note == 60+LED_COUNT-1)
          nextRGB();
        else
          sendNoteOn(0, note, 127);
      }
      else if (!keys[col][row] && previousKeys[col][row]) {
        previousKeys[col][row] = false;
        if (note != 60+LED_COUNT-1)
          sendNoteOff(0, note, 0);
      }
      note++;
    }
  }
}

void sendNoteOn(uint8_t channel, uint8_t note, uint8_t velocity) {
  Serial.write(0x90 | (channel & 0xf));
  Serial.write(note & 0x7f);
  Serial.write(velocity & 0x7f);
  Serial.flush();
}

void sendNoteOff(uint8_t channel, uint8_t note, uint8_t velocity) {
  Serial.write(0x80 | (channel & 0xf));
  Serial.write(note & 0x7f);
  Serial.write(velocity & 0x7f);
  Serial.flush();
}

void nextRGB() {
  switch(++currentRGB) {
    case 1:
      ws2801fx.setMode(FX_MODE_STATIC);
      break;
    case 2:
      ws2801fx.setMode(FX_MODE_COLOR_SWEEP_RANDOM);
      break;
    case 3:
      ws2801fx.setMode(FX_MODE_RAINBOW);
      break;
    case 4:
      ws2801fx.setMode(FX_MODE_RUNNING_RED_BLUE);
      break;
    case 5:
      ws2801fx.setMode(FX_MODE_COLOR_WIPE_RANDOM);
      break;
    case 6:
      ws2801fx.setMode(FX_MODE_LARSON_SCANNER);
      break;
    default:
      currentRGB=0;
      ws2801fx.setMode(FX_MODE_RAINBOW_CYCLE);
      break;
      
  }
}
