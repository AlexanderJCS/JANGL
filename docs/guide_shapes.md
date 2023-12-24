# Shapes

There are a few types of shapes you can make using Jangl:
- Rectangles
- Circles
- Triangles

In this guide, you will learn how to create the shapes listed above. You can see the final source code for this guide [here](/src/test/java/guides/shapeguide).

It is also recommended to see the [quickstart guide](/README.md#quickstart-guide) before continuing this tutorial.

## Creating a base program

Before we start, we need to create a base program that will contain our shapes. This code is largely explained within the [quickstart guide](/README.md#quickstart-guide), and therefore will not be explained in great detail.

```java
import Jangl.Jangl;
import Jangl.io.Window;

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
            Jangl.update();
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        Jangl.init(1600, 900);
        Window.setVsync(true);
        
        new ShapeGuide().run();
        Window.close();
    }
}
```

## Rectangles

Rectangles take three arguments into its constructor:
```java
public Rect(WorldCoords topLeft, float width, float height)
```

All variables, including width and height, are in the units of world coordinates.

Now, let's add a `Rect` member variable to the program. In addition, we will run the `Rect.draw()` method (inherited from `Shape`) inside our own draw method, allowing the rect to be drawn to the screen.

The rectangle will have a top left coordinate of (0.25, 0.75), a width of 0.6, and a height of 0.3.

```java
import Jangl.Jangl;
import Jangl.coords.WorldCoords;
import Jangl.io.Window;
import Jangl.shapes.Rect;

public class ShapeGuide {
    private final Rect rect;

    public ShapeGuide() {
        this.rect = new Rect(new WorldCoords(0.25f, 0.75f), 0.6f, 0.3f);
    }

    public void draw() {
        Window.clear();

        this.rect.draw();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            // This is method is required to be called so the window doesn't say "not responding"
            Jangl.update();
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        Jangl.init(1600, 900);
        Window.setVsync(true);

        new ShapeGuide().run();
        Window.close();
    }
}
```

As you can see, a rectangle is now being drawn to the screen.

![image](https://github.com/AlexanderJCS/Jangl/assets/98898166/ac367f38-0adc-4cfd-914b-a91d88846772)

## Circles

Circles take three arguments into its constructor:
```java
public Circle(WorldCoords center, float radius, int sides)
```

The radius of the circle is in the units of WorldCoords.

Since circles are just approximations of an infinite-sided circle in computer graphics, a finite number of sides needs to be specified. Large numbers of sides will greatly reduce performance, so it is recommended to keep this number low while still maintaining a circular-looking shape. The `sides` argument can also be used to create other shapes, such as pentagons, hexagons, octagons, etc. 

**NOTE:** an `IllegalArgumentException` will be thrown if the number of sides is less than 2. This is because 2-sided shapes and below are impossible to draw in a two-dimensional space.

Going back to the code, let's create a new `Circle` member variable, initialize it in the constructor, and draw it within the `draw` method. For this example, the center of the circle will be at (1.2f, 0.5) and the circle will have a radius of 0.15. It will also have 48 sides, which should be more than enough for any individual side to not be noticeable.

```java
import Jangl.Jangl;
import Jangl.coords.WorldCoords;
import Jangl.io.Window;
import Jangl.shapes.Circle;
import Jangl.shapes.Rect;

public class ShapeGuide {
    private final Rect rect;
    private final Circle circle;

    public ShapeGuide() {
        this.rect = new Rect(new WorldCoords(0.25f, 0.75f), 0.6f, 0.3f);
        this.circle = new Circle(new WorldCoords(1.2f, 0.5f), 0.15f, 48);
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
            Jangl.update();
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        Jangl.init(1600, 900);
        Window.setVsync(true);

        new ShapeGuide().run();
        Window.close();
    }
}
```

You can now see the rectangle and circle are both being drawn to the screen.

![image](https://github.com/AlexanderJCS/Jangl/assets/98898166/af46df2c-f2fb-4672-aae4-15bd2b0a1d34)

## Triangles
The last shape that Jangl supports is a triangle. Out of all the shapes, the triangle constructor is the simplest and goes as follows:
```java
public Triangle(WorldCoords point1, WorldCoords point2, WorldCoords point3)
```

Points 1, 2, and 3 can go in any order: the triangle will still draw the same.

Incorporating this into the code, we can make a right triangle by setting the bottom left vertex to (0.1, 0.1), the rightmost vertex to (0.1, 0.4), and the topmost vertex to (0.4, 0.1).

```java
import Jangl.Jangl;
import Jangl.coords.WorldCoords;
import Jangl.io.Window;
import Jangl.shapes.Circle;
import Jangl.shapes.Rect;
import Jangl.shapes.Triangle;

public class ShapeGuide {
    private final Rect rect;
    private final Circle circle;
    private final Triangle triangle;

    public ShapeGuide() {
        this.rect = new Rect(new WorldCoords(0.25f, 0.75f), 0.6f, 0.3f);
        this.circle = new Circle(new WorldCoords(1.2f, 0.5f), 0.15f, 48);
        this.triangle = new Triangle(
                new WorldCoords(0.1f, 0.1f),
                new WorldCoords(0.4f, 0.1f),
                new WorldCoords(0.1f, 0.4f)
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
            Jangl.update();
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        Jangl.init(1600, 900);
        Window.setVsync(true);

        new ShapeGuide().run();
        Window.close();
    }
}
```

Now, the triangle is being drawn to the screen. Thank you for following this guide.

![image](https://github.com/AlexanderJCS/Jangl/assets/98898166/764a4dc1-0d60-469c-a6ea-9013fcfd21d3)

## Freeing Memory

Memory of shape objects are automatically freed when the garbage collector runs. However, automatically freeing memory may be slow if you are creating and deleting shapes rapidly. Therefore, it is recommended to manually free memory by calling the `Shape.close()` method. This method will free the memory of the shape and will no longer be drawn to the screen.

```java
import Jangl.Jangl;
import Jangl.coords.WorldCoords;
import Jangl.io.Window;
import Jangl.shapes.Circle;
import Jangl.shapes.Rect;
import Jangl.shapes.Triangle;

public class ShapeGuide {
    private final Rect rect;
    private final Circle circle;
    private final Triangle triangle;

    public ShapeGuide() {
        this.rect = new Rect(new WorldCoords(0.25f, 0.75f), 0.6f, 0.3f);
        this.circle = new Circle(new WorldCoords(1.2f, 0.5f), 0.15f, 48);
        this.triangle = new Triangle(
                new WorldCoords(0.1f, 0.1f),
                new WorldCoords(0.4f, 0.1f),
                new WorldCoords(0.1f, 0.4f)
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
            Jangl.update();
        }

        this.rect.close();
        this.circle.close();
        this.triangle.close();
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        Jangl.init(1600, 900);
        Window.setVsync(true);

        new ShapeGuide().run();
        Window.close();
    }
}
```

## Moving Shapes

Remember that you can always move and rotate shapes by calling methods within the Shape transform. You can access the Shape transform by doing the following:
```java
myShape.getTransform()
```

Thank you for following this guide.