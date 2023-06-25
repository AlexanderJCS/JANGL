# JANGL
JANGL is an acronym for "JAva Graphics Library" (the N doesn't mean anything).

This library is built on top of LWJGL, an OpenGL wrapper. JANGL abstracts a lot of the work required when creating an OpenGL project from scratch, but it still allows the user of the library (you) to have control over the program. This project does not have any affiliation with LWJGL.

Benefits of this library over OpenGL include:
- Ease of use when creating rectangles and circles
- Collision detection between any two polygons or circles
- Provides the ability to program custom shaders. JANGL also provides some pre-defined shaders
- Easily render images using the `Image` class
- Easy input operations using the `Mouse` and `Keyboard` class.
- Control over how you structure your project by providing two coordinate systems (normalized device coordinates and pixel coordinates)
- Several other abstractions

## Installing JANGL

To install JANGL, first find the latest release in the [releases section](https://github.com/AlexanderJCS/JANGL/releases/), then download the jar file. **JANGL cannot be installed via Maven or Gradle.**

Then, put the jar file in a libs directory in your project. Then, [configure this as a dependency directory](https://www.jetbrains.com/help/idea/working-with-module-dependencies.html#add-a-new-dependency).

After this, JANGL should be set up in your project. You can test it out by following the [quickstart guide](#quickstart-guide). If you receive any errors, look at the [errors section](#errors).

## Errors

If an error you are experiencing is not listed below, please [create an issue](https://github.com/AlexanderJCS/JANGL/issues/new).

### MacOS Thread Error
If you receive an error on macOS when running JANGL, make sure to add the following VM option:
```
-XstartOnFirstThread
```
This will start the program on the first thread, which will allow GLFW, a dependency of JANGL, to initialize.

### Access Violation Error
If you get an error along the lines of:
```
#
# A fatal error has been detected by the Java Runtime Environment:
#
#  EXCEPTION_ACCESS_VIOLATION (0xc0000005) at ...
```
The most common cause for this error is that JANGL is not initialized. This error can also be thrown if you try to access JANGL on a thread it was not initialized on. To fix this error, run:
```java
import jangl.JANGL;

// Somewhere near the start of your program:
JANGL.init(screenWidthPixels, screenHeightPixels);
```
where `screenWidthPixels` and `screenHeightPixels` is the screen width and height of your window.

## Compiling Source

To compile the source code to a jar, follow [these instructions](https://www.jetbrains.com/help/idea/compiling-applications.html#compile_module) with IntelliJ.

## Examples

For simple code examples, see [src/demo/java](src/demo/java).

For more detailed code examples (things that make an actual application, not just some demos), see:
- [Conway's Game of Life](https://github.com/AlexanderJCS/jangl-conways-game-of-life)
- [A falling sand game](https://github.com/AlexanderJCS/jangl-falling-sand)

You can also see [the quickstart guide](#quickstart-guide) or more guides within [the docs directory](/docs)

## Quickstart Guide

To start, create a new class containing your main method:
```java
public class Quickstart {
    public static void main(String[] args) {
        
    }
}
```

Then, create a constructor which initializes JANGL. The two arguments that are passed into `init()` are the width and height of the screen, in pixels. Set these values to whatever value feels right for your monitor resolution.

```java
import jangl.JANGL;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public static void main(String[] args) {

    }
}
```

Now, you can add a `run()` method to the program. This will be the main loop of the program, and will only exit from this method when the program is complete.

You can also create a new `Quickstart` object and call `Quickstart.run()` within the main method.

```java
import jangl.JANGL;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public void run() {

    }

    public static void main(String[] args) {
        new Quickstart().run();
    }
}
```

Inside the run method, you can include your first object to draw to a screen: a rectangle!

To initialize a rectangle, you need to pass the `coords`, `width`, and `height`. Now is a good time to talk about how coordinates work.

The top left of the window has the coordinates `(-1, 1)`. The bottom right of has the coordinates `(1, -1)`. The center of the screen has the coordinates `(0, 0)`. What's great about this coordinate system is that your window scales and stretches correctly regardless what your resolution is (the two parameters you passed to `JANGL.init()`).

Here, you can see a diagram of important coordinates to know in the window.

![image](https://github.com/AlexanderJCS/JGLT/assets/98898166/722f8e9c-5c11-4974-bf19-cefe0ac51515)

With that knowledge, you can make your first rectangle. One important thing to note is that all shape classes (including `Rect`s) as well as several other classes in JANGL implement AutoCloseable. It is important to close these classes when you're done to avoid a memory leak.

Because of this, you can put the `Rect` inside a try-with-resources statement inside your `run()` method.

```java
import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.shapes.Rect;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public void run() {
        try (Rect rect = new Rect(new NDCoords(0, 0), 0.5f, 0.5f)) {

        }
    }

    public static void main(String[] args) {
        new Quickstart().run();
    }
}
```
The first argument of the `Rect` constructor is the `NDCoords` (normalized device coordinates) location, which requires an x and y position. For this rectangle, you set the position at `(0, 0)`. The second and third parameter is the `width` and `height` respectively, both in the units of normalized device coordinates. That value is set to 0.5.

Next, you need to draw your shape. First, you need to run `JANGL.update()`. This method will populate events, but more on that later. If this method is not called, the application will not respond.

Then, you run `Window.clear()`. This ensures that the previous frame is cleared from the screen before drawing the next one. This makes sure that a "trail" is not left behind moving objects.

Finally, you can draw your rectangle using `rect.draw()`.

When those three methods are combined, you get the following code:

```java
import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.io.Window;
import jangl.shapes.Rect;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public void run() {
        try (Rect rect = new Rect(new NDCoords(0, 0), 0.5f, 0.5f)) {
            JANGL.update();
            Window.clear();

            rect.draw();
        }
    }

    public static void main(String[] args) {
        new Quickstart().run();
    }
}
```

However, if you run the code in its current state, you might notice that a window opens then closes. This is because the program is not drawing inside a loop, so it draws once then reaches the end of the program. To fix this, you can add a while loop: `while (Window.shouldRun())`. This condition evaluates to true when the "X" on the top right of the window is not pressed.

Also, at the end of the `run()` method, you can call `Window.close()` to de-initialize the window.

```java
import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.io.Window;
import jangl.shapes.Rect;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public void run() {
        try (Rect rect = new Rect(new NDCoords(0, 0), 0.5f, 0.5f)) {
            while (Window.shouldRun()) {
                JANGL.update();
                Window.clear();

                rect.draw();
            }
        }

        Window.close();
    }

    public static void main(String[] args) {
        new Quickstart().run();
    }
}
```

Now, we have a rectangle drawn to your screen.
![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/9f331770-2d59-4887-ab79-08d20f2312f9)

If you notice that your computer fans start spinning faster when running this program, that's no coincidence. You're running the window at the maximum speed your computer can handle, so it's going to put strain on the CPU and GPU. You can limit this by using the `Clock.smartTick(int fps)` method. This will run the window at the desired frames per second.

The smart tick method throws an interrupted exception if the program is interrupted while it is `Thread.sleep`ing, so it is important to handle that as well.

```java
import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.io.Window;
import jangl.shapes.Rect;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public void run() {
        try (Rect rect = new Rect(new NDCoords(0, 0), 0.5f, 0.5f)) {
            while (Window.shouldRun()) {
                JANGL.update();
                Window.clear();

                rect.draw();

                // Run the window at 60 FPS, handling any interrupted exceptions that may occur
                try {
                    Clock.smartTick(60);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        Window.close();
    }

    public static void main(String[] args) {
        new Quickstart().run();
    }
}
```

Another way to limit FPS is to make the window run at VSync, or the maximum framerate your monitor can display. To do this, get rid of the `Clock.smartTick()` and run `Window.setVsync(true)` after JANGL initializes.

```java
import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.io.Window;
import jangl.shapes.Rect;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
        Window.setVsync(true);  // turn vsync on
    }

    public void run() {
        try (Rect rect = new Rect(new NDCoords(0, 0), 0.5f, 0.5f)) {
            while (Window.shouldRun()) {
                JANGL.update();
                Window.clear();

                rect.draw();
            }
        }

        Window.close();
    }

    public static void main(String[] args) {
        new Quickstart().run();
    }
}
```

One thing you may notice is that the width and height of the rectangle are different, even though the specified the width and height passed to the rectangle are the same. This is a common limitation of the `NDCoords` type. Since the window must be two units in width and two units in height, if the window does not have a 1:1 aspect ratio, one unit on the Y axis will not equal the same distance as one unit on the X axis. To circumvent this, we can specify the width and height of the `Rect` in pixels using the `PixelCoords` class.

We can do this by using the `PixelCoords` type. It allows us to convert a certain number of pixels in the X axis and a certain number of pixels in the Y axis to normalized device coordinates using the `distXtoNDC` and `distYtoNDC` method. For example, if we want our cube to be 400 pixels wide and tall, we can initialize our rect like so:
```java
new Rect(new NDCoords(0, 0), PixelCoords.distXtoNDC(400), PixelCoords.distYtoNDC(400));
```

Now, you can incorporate this into your program:

```java
import jangl.JANGL;
import jangl.coords.PixelCoords;
import jangl.coords.NDCoords;
import jangl.io.Window;
import jangl.shapes.Rect;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public void run() {
        try (Rect rect = new Rect(
                new NDCoords(0, 0),
                PixelCoords.distXtoNDC(400),
                PixelCoords.distYtoNDC(400)
        )
        ) {
            while (Window.shouldRun()) {
                JANGL.update();
                Window.clear();

                rect.draw();

                // Run the window at 60 FPS, handling any interrupted exceptions that may occur
                try {
                    Clock.smartTick(60);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        Window.close();
    }

    public static void main(String[] args) {
        new Quickstart().run();
    }
}
```

Thanks for following!
![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/5e44f87a-f76e-49bf-85cd-ea4a1bb28ce8)