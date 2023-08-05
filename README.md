# JANGL
JANGL is an acronym for "JAva Graphics Library" (the N doesn't mean anything).

This library is built on top of LWJGL, an OpenGL wrapper. JANGL abstracts a lot of the work required when creating an OpenGL project from scratch, but it still allows the user of the library (you) to have control over the program. This project does not have any affiliation with LWJGL.

## Compatibility

This library is tested on Windows, and is therefore guaranteed to work on Windows. It should work on Linux and macOS, but it may not since it is not tested on the platform. If you receive an error on the other operating systems, please [create an issue](https://github.com/AlexanderJCS/JANGL/issues/new).

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


// Somewhere near the start of your program:
JANGL.init(screenWidthPixels,screenHeightPixels);
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
    public static void main(String[] args) {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);

        Window.close();
    }
}
```

Now, you can add a `run()` method to the program. This will be the main loop of the program, and will only exit from this method when the program is complete.

You can also create a new `Quickstart` object and call `Quickstart.run()` within the main method.

```java
import jangl.JANGL;

public class Quickstart {
    public Quickstart() {

    }

    public void run() {

    }

    public static void main(String[] args) {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);

        new Quickstart().run();

        Window.close();
    }
}
```

Inside the run method, you can include your first object to draw to a screen: a rectangle!

To initialize a rectangle, you need to pass the `coords`, `width`, and `height`. Now is a good time to talk about how coordinates work.

The bottom left of the window has the coordinates (0, 0) and the top left of your window has the coordinates (1, 0). The maximum X coordinate on the screen depends on the aspect ratio. The coordinate system is designed to ensure that the window can be scaled seamlessly.
![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/f06a4d8d-262e-42fa-939f-e9886db49bdd)

With that knowledge, you can make your first rectangle.

```java
import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.shapes.Rect;

public class Quickstart {
    private final Rect rect;

    public Quickstart() {
        this.rect = new Rect(new WorldCoords(0.5f, 0.75f), 0.5f, 0.5f);
    }

    public void run() {

    }

    public static void main(String[] args) {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);

        new Quickstart().run();

        Window.close();
    }
}
```

One important thing to note is that all shape classes (including `Rect`s) as well as several other classes in JANGL implement AutoCloseable. It is important to close these classes when you're done to avoid a memory leak. To prevent this, we can implement `AutoCloseable` ourselves and close everything when the program stops running.

```java
import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.shapes.Rect;

public class Quickstart implements AutoCloseable {
    private final Rect rect;

    public Quickstart() {
        this.rect = new Rect(new WorldCoords(0.5f, 0.75f), 0.5f, 0.5f);
    }

    public void run() {

    }

    @Override
    public void close() {
        this.rect.close();
    }

    public static void main(String[] args) {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);

        Quickstart quickstart = new Quickstart();
        quickstart.run();
        quickstart.close();

        Window.close();
    }
}
```

The first argument of the `Rect` constructor is the coordinate of the top left vertex in `WorldCoords`, which requires an x and y position. For this rectangle, you set the position at `(0.5, 0.75)`. The second and third parameter is the `width` and `height` respectively, both in the units of world coordinates. That value is set to 0.5.


Next, you need to draw your shape. First, you need to run `JANGL.update()`. If this method is not called, the application will not respond.

Then, you run `Window.clear()`. This ensures that the previous frame is cleared from the screen before drawing the next one. This makes sure that a "trail" is not left behind moving objects. It also ensures that semi-transparent objects do not become non-transparent.

Finally, you can draw your rectangle using `rect.draw()`.

When those three methods are combined, you get the following code:

```java
import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.io.Window;
import jangl.shapes.Rect;

public class Quickstart implements AutoCloseable {
    private final Rect rect;

    public Quickstart() {
        this.rect = new Rect(new WorldCoords(0.5f, 0.75f), 0.5f, 0.5f);
    }

    public void run() {
        JANGL.update();
        Window.clear();

        rect.draw();
    }

    @Override
    public void close() {
        this.rect.close();
    }

    public static void main(String[] args) {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);

        Quickstart quickstart = new Quickstart();
        quickstart.run();
        quickstart.close();

        Window.close();
    }
}
```

However, if you run the code in its current state, you might notice that a window opens then closes. This is because the program is not drawing inside a loop, so it draws once then reaches the end of the program. To fix this, you can add a while loop: `while (Window.shouldRun())`. This condition evaluates to true when the "X" on the top right of the window is not pressed.

```java
import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.io.Window;
import jangl.shapes.Rect;

public class Quickstart implements AutoCloseable {
    private final Rect rect;

    public Quickstart() {
        this.rect = new Rect(new WorldCoords(0.5f, 0.75f), 0.5f, 0.5f);
    }

    public void run() {
        while (Window.shouldRun()) {
            JANGL.update();
            Window.clear();

            rect.draw();
        }
    }

    @Override
    public void close() {
        this.rect.close();
    }

    public static void main(String[] args) {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);

        Quickstart quickstart = new Quickstart();
        quickstart.run();
        quickstart.close();

        Window.close();
    }
}
```

Now, we have a rectangle drawn to your screen.
![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/9f331770-2d59-4887-ab79-08d20f2312f9)

If you notice that your computer fans start spinning faster when running this program, that's no coincidence. You're running the window at the maximum speed your computer can handle, so it's going to put strain on the CPU and GPU. You can limit this by using the `Clock.smartTick(int fps)` method. This will run the window at the desired frames per second.

The smart tick method throws an interrupted exception if the program is interrupted while it is `Thread.sleep`ing, so it is important to handle that as well.

```java
import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.io.Window;
import jangl.shapes.Rect;
import jangl.time.Clock;

public class Quickstart implements AutoCloseable {
    private final Rect rect;

    public Quickstart() {
        this.rect = new Rect(new WorldCoords(0.5f, 0.75f), 0.5f, 0.5f);
    }

    public void run() {
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

        Window.close();
    }

    @Override
    public void close() {
        this.rect.close();
    }

    public static void main(String[] args) {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);

        Quickstart quickstart = new Quickstart();
        quickstart.run();
        quickstart.close();

        Window.close();
    }
}
```

Another way to limit FPS is to make the window run at VSync, or the maximum framerate your monitor can display. To do this, get rid of the `Clock.smartTick()` and run `Window.setVsync(true)` after JANGL initializes.

```java
import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.io.Window;
import jangl.shapes.Rect;

public class Quickstart implements AutoCloseable {
    private final Rect rect;

    public Quickstart() {
        this.rect = new Rect(new WorldCoords(0.5f, 0.75f), 0.5f, 0.5f);
    }

    public void run() {
        while (Window.shouldRun()) {
            JANGL.update();
            Window.clear();

            this.rect.draw();
        }
    }

    @Override
    public void close() {
        this.rect.close();
    }

    public static void main(String[] args) {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
        Window.setVsync(true);  // turn vsync on

        Quickstart quickstart = new Quickstart();
        quickstart.run();
        quickstart.close();

        Window.close();
    }
}
```

Now you have a rectangle drawn to the screen. Thanks for following!