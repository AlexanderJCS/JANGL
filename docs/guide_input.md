# Input Guide

This guide will demonstrate how to get user input. It covers:
- Checking if a key is down
- Keyboard events
- Checking if the mouse is down
- Getting the mouse position
- Mouse events
- Scroll events

An example of some of these features can be found in the [input demo](/src/test/java/demos/inputdemo).

## What are Events?

Events are individual presses or releases of keys. They can also be used to listen to the mouse scroll wheel or to check if a key should repeat itself due to being held for too long (a behavior you can find in any text editor by holding down a key).

Events differ from checking if a key or mouse button is held down because **an event only occurs once per press**, while checking if the item is held will result as true for every frame that it is held.

## Keyboard

### Checking if a key is down

You can check if a key is down using the `Keyboard.getKeyDown()` method:
```java
import jangl.JANGL;
import jangl.io.keyboard.Keyboard;
import org.lwjgl.glfw.GLFW;

JANGL.init(...);  // initialize JANGL before doing anything

// Checks if the key "A" is down.
if (Keyboard.getKeyDown('A') {
    System.out.println("A is down!");    
}

// Checks if shift is down. The org.lwjgl.glfw.GLFW class must be imported
if (Keyboard.getKeyDown(GLFW.GLFW_KEY_SHIFT)) {
    System.out.println("Shift is down!");
}
```

### Keyboard events

You can get and parse all the keyboard events by doing the following.
```java
import java.util.List;

import jangl.JANGL;
import jangl.io.keyboard.Keyboard;
import jangl.io.keyboard.KeyEvent;
import org.lwjgl.glfw.GLFW;

JANGL.init(...);  // initialize JANGL before doing anything

// Get all key events since the last time you called this method. Maxes out at jangl.io.EventsConfig.MAX_EVENTS.
List<KeyEvent> keyEvents = Keyboard.getEvents();

for (KeyEvent event : keyEvents) {
    System.out.println("Key character: " + event.key);
    System.out.println("Key scancode: " + event.scancode);
    
    if (event.action == GLFW.GLFW_PRESS) {
        // Key is pressed
    }
    
    if (event.action == GLFW.GLFW_RELEASE) {
        // Key is released
    }
    
    if (event.action == GLFW.GLFW_REPEAT) {
        // The key is repeated because it is held down.
    }
}
```

## Mouse

### Checking if the mouse is down

You can check if either of the mouse buttons are down by doing:
```java
import jangl.JANGL;
import jangl.io.mouse.Mouse;
import org.lwjgl.glfw.GLFW;

JANGL.init(...);  // initialize JANGL before doing anything

boolean isLeftClicking = Mouse.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_LEFT);  // returns true if left click is held
boolean isRightClicking = Mouse.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT);  // returns true if right click is held
boolean isMiddleClicking = Mouse.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);  // returns true if middle click is held
```

### Getting the mouse position
You can get the mouse position using the following code. It is important to note that the mouse position does not move with the camera. In other words, if the camera is moved, scaled, or rotated, the mouse position will not reflect those changes.

```java
import jangl.JANGL;
import jangl.io.mouse.Mouse;
import jangl.coords.WorldCoords;

JANGL.init(...);  // initialize JANGL before doing anything

WorldCoords mousePos = Mouse.getMousePos();  // returns the mouse position in the units of WorldCoords
```

### Mouse events
Mouse events can be used to check if a mouse button is clicked or released. You can get the mouse events and parse mouse events by doing the following:

```java
import java.util.List;

import jangl.JANGL;
import jangl.io.mouse.Mouse;
import jangl.io.mouse.MouseEvent;
import org.lwjgl.glfw.GLFW;

JANGL.init(...);  // initialize JANGL before doing anything

// Get all mouse events that happened since the last getEvents() call
List<MouseEvent> mouseEvents = Mouse.getEvents();

for (MouseEvent mouseEvent : mouseEvent) {
    if (mouseEvent.button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
        System.out.println("The left mouse button is being pressed");
    } else if (mouseEvent.button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
        System.out.println("The right mouse button is being pressed");
    } else if (mouseEvent.button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
        System.out.println("The middle mouse button is being pressed");
    } else {
        // If the mouse button that was pressed is not left, right or middle.
        // This may occur if the mouse has extra buttons
        System.out.println("Mouse button " + mouse.button + " was pressed.");
    }

    if (event.action == GLFW.GLFW_PRESS) {
        System.out.println("The mouse was pressed");
    }

    if (event.action == GLFW.GLFW_RELEASE) {
        System.out.println("The mouse was released");
    }
}
```

### Scroll Events

Accessing scroll events (events that occur when the scroll wheel is used) is similar to accessing the mouse and keyboard events. We will be using the `Scroll` class.

```java
import java.util.List;

import jangl.JANGL;
import jangl.io.mouse.Scroll;
import jangl.io.mouse.ScrollEvent;

JANGL.init(...);  // initialize JANGL before doing anything

// Get all scroll events that happened since the last getEvents() call
List<ScrollEvent> scrollEvents = Scroll.getEvents();

for (ScrollEvent scrollEvent : scrollEvents) {
    System.out.println("Scroll event: x offset is " + scrollEvent.xOffset + " and y offset is " + scrollEvent.yOffset);
}
```