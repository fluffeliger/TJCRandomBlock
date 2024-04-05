# Random Block

Die Random-Block-Challenge ist eine Challenge, wobei man in einem gewissen Zeitabstand ein zufälliges Item bekommt.
Wer als letztes Steht, gewinnt.

## Setup

Damit man nicht dauerhaft unnötig das End und den Nether generiert sollte man es deaktivieren.
Außerdem wird die standard Welt als Lobby verwendet.

### Nether

``server.properties``
````properties
allow-nether=false
````

### End

``bukkit.yml``
````yml
settings:
  allow-end: false
````