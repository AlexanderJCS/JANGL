# JANGL
JANGL means JAva Graphics Library (the N doesn't mean anything).

This library is built on top of LWJGL, an OpenGL wrapper. This library abstracts a lot of the work required when creating an OpenGL project from scratch, but it still allows the user of the library (you) to have control over the program. 

Benefits of this library over OpenGL include:
- Ease of use when creating rectangles and circles
- Collision detection between any two polygons or circles
- Provides more control with custom shaders, as well as some pre-defined shaders
- Easily load in images using the `Image` class
- Easy input operations using the `Mouse` and `Keyboard` class.
- Control over how you structure your project by providing two coordinate systems (`ScreenCoords` and `PixelCoords`)
- Several other abstractions

## Installing JANGL

To install JANGL, first find the latest release in the [releases section](https://github.com/AlexanderJCS/JANGL/releases/), then download the jar file. **JANGL cannot be installed via Maven or Gradle.**

Then, put the jar file in a libs directory in your project. Then, [configure this as a dependency directory](https://www.jetbrains.com/help/idea/working-with-module-dependencies.html#add-a-new-dependency).

After this, JANGL should be set up in your project. You can test it out by following the [quickstart guide](#quickstart-guide)

## Compiling Source

To compile the source code to a jar, follow [these instructions](https://www.jetbrains.com/help/idea/compiling-applications.html#compile_module) with IntelliJ.

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

The top left of the window has the coordinates `(1, 1)`. The bottom right of has the coordinates `(1, -1)`. The center of the screen has the coordinates `(0, 0)`. What's great about this coordinate system is that your window scales and stretches correctly regardless what your resolution is (the two parameters you passed to `JANGL.init()`).

Here, you can see a diagram of important coordinates to know in the window.

![image](https://github.com/AlexanderJCS/JGLT/assets/98898166/722f8e9c-5c11-4974-bf19-cefe0ac51515)

With that knowledge, you can make your first rectangle. One important thing to note is that all shape classes, the `Texture` class, and the `Shader` class all implement `AutoClosable`: if you forget to close them, there will be a memory leak.

Because of this, you can put the `Rect` inside a try-with-resources statement inside your `run()` method.

```java
import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.shapes.Rect;

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
The first argument of the `Rect` constructor is the `ScreenCoords` location, which requires an x and y position. For this rectangle, you set the position at `(0, 0)`. The second and third parameter is the `width` and `height` respectively, both in the units of screen coordinates. That value is set to 0.5.

Next, you need to draw your shape. First, you need to run `JANGL.update()`. This method will populate events, but more on that later. If this method is not called, the application will not respond.

Then, you run `Window.clear()`. This ensures that the previous frame is cleared from the screen before drawing the next one. This makes sure that a "trail" is not left behind moving objects.

Finally, you can draw your rectangle using `rect.draw()`.

When those three methods are combined, you get the following code:

```java
import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.io.Window;
import jangl.shapes.Rect;

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

However, if you run the code in its current state, you might notice that a window opens then closes. This is because the program is not drawing inside a loop, so it draws once then reaches the end of the program. To fix this, you can add a while loop: `while (Window.shouldRun())`. This condition evaluates to true when the "X" on the top right of the window is not pressed.

Also, at the end of the `run()` method, you can call `Window.close()` to de-initialize the window.

```java
import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.io.Window;
import jangl.shapes.Rect;

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

Now, we have a rectangle drawn to your screen.
![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/9f331770-2d59-4887-ab79-08d20f2312f9)

However, one thing you may notice is that the width and height of the rectangle are different, even though the specified the width and height passed to the rectangle are the same. This is a common limitation of the `ScreenCoords` type. Since the window must be two units in width and two units in height, if the window does not have a 1:1 aspect ratio, one unit on the Y axis will not equal the same distance as one unit ont he X axis. To circumvent this, we can specify the number of pixels the `Rect` will be on the X and Y axis instead of using ScreenCoords.

We can do this by using the `PixelCoords` object. It allows us to convert a certain number of pixels in the X axis and a certain number of pixels in the Y axis to screen coordinates using the `distXtoScreenCoords` and `distYtoScreenCoords` method. For example, if we want our cube to be 400 pixels wide and tall, we can initialize our rect like so:
```java
new Rect(new ScreenCoords(0, 0), PixelCoords.distXtoScreenCoords(400), PixelCoords.distYtoScreenCoords(400));
```

Now, you can incorporate this into your program:
```java
import jangl.JANGL;
import jangl.coords.PixelCoords;
import jangl.coords.ScreenCoords;
import jangl.io.Window;
import jangl.shapes.Rect;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public void run() {
        try (Rect rect = new Rect(
                new ScreenCoords(0, 0),
                PixelCoords.distXtoScreenDist(400),
                PixelCoords.distYtoScreenDist(400)
            )
        ) {
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
![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/5e44f87a-f76e-49bf-85cd-ea4a1bb28ce8)

Finally, let's give this square some color! Computers handle colors by displaying different levels of red, green, and blue values per pixel. For example, 100% red, 100% green, and 0% blue would result in a yellow color. In addition, there is also an alpha value, which is the transparency of the pixel. 

You can specify the red, green, blue, and alpha values of a shape using the `ColorShader` class. Its constructor method header is:
```java
public ColorShader(float red, float green, float blue, float alpha)
```

The red, green, blue, and alpha values are 32-bit floating-point values between 0 and 1, where 0 = 0%, 0.5f = 50%, and 1 = 100%, just to name a few notable values. So, if we wanted to define a `ColorShader` with the color yellow, we need to define 100% red, 100% green, and 100% alpha (0% transparency, since transparency = 100% - alpha). We can do so using the following declaration:
```java
ColorShader yellow = new ColorShader(1, 1, 0, 1);
```

Once the shader is created, you can pass it into the overloaded `Shape.draw(Shader shader)` method to draw a shape with the shader.

Before incorporating shaders into our program, it's also important to note that all shaders, including `ColorShader`s, need to be closed to prevent a memory leak. So, in our program, it will go inside the try-with-resources statement.

With this knowledge, let's add a shader to our rectangle:
```java
import jangl.JANGL;
import jangl.coords.PixelCoords;
import jangl.coords.ScreenCoords;
import jangl.graphics.shaders.ColorShader;
import jangl.io.Window;
import jangl.shapes.Rect;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public void run() {
        try (
                Rect rect = new Rect(
                    new ScreenCoords(0, 0),
                    PixelCoords.distXtoScreenDist(400),
                    PixelCoords.distYtoScreenDist(400)
                );

                ColorShader yellow = new ColorShader(1, 1, 0, 1)
        ) {
            while (Window.shouldRun()) {
                JANGL.update();
                Window.clear();

                rect.draw(yellow);
            }
        }

        Window.close();
    }

    public static void main(String[] args) {
        new Quickstart().run();
    }
}
```

![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/dfe97fdd-0859-485d-ab3b-73329e112ee2)