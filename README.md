# JANGL
JANGL means JAva Graphics Library (the N doesn't mean anything).

This library is built on top of LWJGL, an OpenGL wrapper. This library abstracts a lot of the work required when creating an OpenGL project from scratch, but it still allows the user of the library (you) to have control over the program. 

## Examples

For code examples, see [src/demo/java](src/demo/java). Or see the quickstart guide.

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
import jglt.JANGL;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }
    
    public static void main(String[] args) {
        
    }
}
```

Now, we're going to add a `run()` method to our program. This will be the main loop of the program, and will only exit from this method when the program is complete.

We will also create a new `Quickstart` object and call `Quickstart.run()` within the main method.

```java
import jglt.JANGL;

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

Inside the run method, we can include our first object to draw to a screen: a rectangle!

To initialize a rectangle, we need to pass the `coords`, `width`, and `height`. Now is a good time to talk about how coordinates work.

The top left of the window has the coordinates `(1, 1)`. The bottom right of has the coordinates `(1, -1)`. The center of the screen has the coordinates `(0, 0)`. What's great about this coordinate system is that your window scales and stretches correctly regardless what your resolution is (the two parameters you passed to `JANGL.init()`).

Here, you can see a diagram of important coordinates to know in the window.

![image](https://github.com/AlexanderJCS/JGLT/assets/98898166/722f8e9c-5c11-4974-bf19-cefe0ac51515)

With that knowledge, we can make our first rectangle. One important thing to note is that all shape classes, the `Texture` class, and the `Shader` class all implement `AutoClosable`: if you forget to close them, there will be a memory leak.

Because of this, we can put the `Rect` inside a try-with-resources statement inside our `run()` method.

```java
import jglt.JANGL;
import jglt.coords.ScreenCoords;
import jglt.shapes.Rect;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public void run() {
        try (Rect rect = new Rect(new ScreenCoords(0, 0), 0.5f, 0.5f)) {
            
        }
    }

    public static void main(String[] args) {
        new Quickstart().run();
    }
}
```
The first argument of the `Rect` constructor is the `ScreenCoords` location, which requires an x and y position. For this rectangle, we set the position at `(0, 0)`. The second and third parameter is the `width` and `height` respectively, both in the units of screen coordinates. That value is set to 0.5.

Next, we need to draw our shape. First, we need to run `JANGL.update()`. This method will populate events, but more on that later. If this method is not called, the application will not respond.

Then, we run `Window.clear()`. This ensures that the previous frame is cleared from the screen before drawing the next one. This makes sure that a "trail" is not left behind moving objects.

Finally, we can draw our rectangle using `rect.draw()`.

When those three methods are combined, we get the following code:
```java
import jglt.JANGL;
import jglt.coords.ScreenCoords;
import jglt.io.Window;
import jglt.shapes.Rect;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public void run() {
        try (Rect rect = new Rect(new ScreenCoords(0, 0), 0.5f, 0.5f)) {
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

However, if you run the code in its current state, you might notice that a window opens then closes. This is because the program is not drawing inside a loop, so it draws once then reaches the end of the program. To fix this, we can add a while loop: `while (Window.shouldRun())`. This condition evaluates to true when the "X" on the top right of the window is not pressed.

Also, at the end of the `run()` method, we can call `Window.close()` to de-initialize the window.

```java
import jglt.JANGL;
import jglt.coords.ScreenCoords;
import jglt.io.Window;
import jglt.shapes.Rect;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public void run() {
        try (Rect rect = new Rect(new ScreenCoords(0, 0), 0.5f, 0.5f)) {
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

Now, we have a rectangle drawn to our screen. Thank you for going through this quickstart guide and looking at my library.
![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/9f331770-2d59-4887-ab79-08d20f2312f9)