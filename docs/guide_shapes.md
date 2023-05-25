# Shapes

There are a few types of shapes you can make using JANGL:
- Rectangles
- Circles
- Triangles

In this guide, you will learn how to create the shapes listed above. You can see the final source code for this guide [here](/src/guideCode/java/shapeguide/ShapeGuide.java).

It is also recommended to see the [quickstart guide](/README.md#quickstart-guide) before continuing this tutorial.

## Creating a base program

Before we start, we need to create a base program that will contain our shapes. This code is largely explained within the [quickstart guide](/README.md#quickstart-guide), and therefore will not be explained in great detail.

```java
import jangl.JANGL;
import jangl.io.Window;
import jangl.time.Clock;
import jangl.time.Clock;

public class ShapeGuide {
    public ShapeGuide() {

    }

    public void draw() {
        Window.clear();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            // This is method is required to be called so the window doesn't say "not responding"
            JANGL.update();

            // Run the window at 60 fps
            try {
                Clock.smartTick(60);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        JANGL.init(1600, 900);
        new ShapeGuide().run();
        Window.close();
    }
}
```

## Rectangles

Rectangles take three arguments into its constructor:
```java
public Rect(ScreenCoords topLeft, float width, float height)
```

All variables, including width and height, are in the units of screen coordinates.

Now, let's add a `Rect` member variable to the program. In addition, we will run the `Rect.draw()` method (inherited from `Shape`) inside our own draw method, allowing the rect to be drawn to the screen.

The rectangle will have a top left coordinate of (-0.75, 0.75), a width of 0.6, and a height of 0.6.

```java
import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.io.Window;
import jangl.shapes.Rect;
import jangl.time.Clock;
import jangl.time.Clock;

public class ShapeGuide {
    private final Rect rect;

    public ShapeGuide() {
        this.rect = new Rect(new ScreenCoords(-0.75f, 0.75f), 0.6f, 0.6f);
    }

    public void draw() {
        Window.clear();

        this.rect.draw();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            // This is method is required to be called so the window doesn't say "not responding"
            JANGL.update();

            // Run the window at 60 fps
            try {
                Clock.smartTick(60);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        JANGL.init(1600, 900);
        new ShapeGuide().run();
        Window.close();
    }
}
```

As you can see, a rectangle is now being drawn to the screen.

![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/212651a8-539d-4f9a-a56f-2ec2e8f5c22b)

At the end of this tutorial, you will learn how to give all shapes, including this shape, color. If you want to give this rectangle a texture, you can also read the [image guide](guide_image.md).

## Circles

Circles take three arguments into its constructor:
```java
public Circle(ScreenCoords center, float radius, int sides)
```

The radius of the circle is in the units of ScreenCoords x-distance. It is important to differentiate between x and y distance since, if the aspect ratio of the window is not 1:1, one unit on the x-axis will not equal one unit on the y-axis.

Since circles are just approximations of an infinite-sided circle in computer graphics, a finite number of sides needs to be specified. Large numbers of sides will greatly reduce performance, so it is recommended to keep this number low while still maintaining a circular-looking shape. The `sides` argument can also be used to create other shapes, such as pentagons, hexagons, octagons, etc. 

**NOTE:** an `IllegalArgumentException` will be thrown if the number of sides is less than 2. This is because 2-sided shapes and below are impossible to draw in a two-dimensional space.

Going back to the code, let's create a new `Circle` member variable, initialize it in the constructor, and draw it within the `draw` method. For this example, the center of the circle will be at (-0.5, -0.5) and the circle will have a radius of 0.3. It will also have 48 sides, which should be enough for any individual side to not be noticeable.

```java
import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.io.Window;
import jangl.shapes.Circle;
import jangl.shapes.Rect;
import jangl.time.Clock;
import jangl.time.Clock;

public class ShapeGuide {
    private final Rect rect;
    private final Circle circle;

    public ShapeGuide() {
        this.rect = new Rect(new ScreenCoords(-0.75f, 0.75f), 0.6f, 0.6f);
        this.circle = new Circle(new ScreenCoords(-0.5f, -0.5f), 0.25f, 48);
    }

    public void draw() {
        Window.clear();

        this.rect.draw();
        this.circle.draw();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            // This is method is required to be called so the window doesn't say "not responding"
            JANGL.update();

            // Run the window at 60 fps
            try {
                Clock.smartTick(60);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        JANGL.init(1600, 900);
        new ShapeGuide().run();
        Window.close();
    }
}
```

You can now see the rectangle and circle are both being drawn to the screen.

![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/b2aee5c5-5d1e-4946-a37d-a4f61646c3d2)

## Triangles
The last shape that JANGL supports is a triangle. Out of all the shapes, the triangle constructor is the simplest and goes as follows:
```java
public Triangle(ScreenCoords point1, ScreenCoords point2, ScreenCoords point3)
```

Points 1, 2, and 3 can go in any order: the triangle will still draw the same.

Incorporating this into the code, the first vertex (bottom left) will be at (0, -0.3). The second vertex (bottom right) will be at (0.5, -0.3). The final vertex (top vertex) will be at (0.25, 0.3). The triangle will also be drawn within the `draw()` method.

```java
package shapeguide;

import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.io.Window;
import jangl.shapes.Circle;
import jangl.shapes.Rect;
import jangl.shapes.Triangle;
import jangl.time.Clock;
import jangl.time.Clock;

public class ShapeGuide {
    private final Rect rect;
    private final Circle circle;
    private final Triangle triangle;

    public ShapeGuide() {
        this.rect = new Rect(new ScreenCoords(-0.75f, 0.75f), 0.6f, 0.6f);
        this.circle = new Circle(new ScreenCoords(-0.5f, -0.5f), 0.25f, 48);
        this.triangle = new Triangle(
                new ScreenCoords(0, -0.3f),
                new ScreenCoords(0.5f, -0.3f),
                new ScreenCoords(0.25f, 0.3f)
        );
    }

    public void draw() {
        Window.clear();

        this.rect.draw();
        this.circle.draw();
        this.triangle.draw();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            // This is method is required to be called so the window doesn't say "not responding"
            JANGL.update();

            // Run the window at 60 fps
            try {
                Clock.smartTick(60);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        JANGL.init(1600, 900);
        new ShapeGuide().run();
        Window.close();
    }
}
```

Now, the triangle is being drawn to the screen. Thank you for following this guide.

![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/96dd96d3-6035-4b57-805c-0e18466d97be)
