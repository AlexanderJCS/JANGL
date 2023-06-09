# Image Guide

This guide will show you how to display an image to the screen. Before following this guide, it is recommended to view the [quickstart guide](/README.md#quickstart-guide) and the [rectangles section of the shape guide](guide_shapes.md#rectangles).

## Preparing an image

The first step is to prepare an image to use for this guide. Common file formats that are supported are `.png` and `.jpg`, among others. For the purposes of this guide, it is recommended that your image has a square aspect ratio.

If you do not want to create an image, you can use the one below:

![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/c3ea4ebc-8fb9-4974-b033-de762ea1b337)

## Creating a base program

Before we start, we need to create a base program that will contain our shapes. This code is largely explained within the [quickstart guide](/README.md#quickstart-guide), and therefore will not be explained in great detail. This code will be used later.

```java
import jangl.JANGL;
import jangl.io.Window;
import jangl.time.Clock;

public class ImageGuide {
    public ImageGuide() {

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
        new ImageGuide().run();
        Window.close();
    }
}
```

## Creating an image

The `Image` record provides a wrapper for the combination of a `Texture` and `Rect` class. The `Texture` contains image information, while the `Rect` is what the image is displayed on. 

Below is the constructor for the `Image` record:
```java
public Image(Rect rect, Image image)
```

### Creating a rectangle

The first item the `Image` record needs is a rectangle for the image to be displayed on to. Images can be scaled and stretched to any size, but to maintain image quality, we will make the rectangle's aspect ratio equal to the image's aspect ratio (square in the case of the example image).

If you do not understand the code below, it is recommended to read the [quickstart guide](/README.md#quickstart-guide) and the [rectangles section of the shape guide](guide_shapes.md#rectangles)

```java
new Rect(
        new NDCoords(0, 0),
        PixelCoords.distXtoNDC(300),
        PixelCoords.distYtoNDC(300)
);
```

This code creates a new rectangle whose top left is at the center of the screen (coordinates (0, 0)), has a width of 300 pixels, and has a height of 300 pixels.

### Creating a texture

The `Texture` class's constructor is
```java
public Texture(TextureBuilder);
```

To assign image data to a `TextureBuilder`, you can do:
```java
new TextureBuilder().setImagePath("path/to/your/image/image.png");
```

You can also fill the texture with a solid color by doing:
```java
new TextureBuilder().fill(color, width, height);
```

Where `color` is a `jangl.color.Color` object, `width` is the width of the image in pixels, and `height` is the height of the image in pixels.

Another important method in the `TextureBuilder` class is the `setObeyCamera(bool)` method. When the argument is true, the object will move according to the camera. By default, this is off.

Putting this together, we can create our texture by doing:
```java
new Texture(new TextureBuilder().setImagePath("/path/to/your/image/image.png").setObeyCamera(true));
```

### Creating an image using the Rect and Texture

Now that we have a rectangle and texture, we can create an image:
```java
new Image (
        new Rect(
            new NDCoords(0, 0),
            PixelCoords.distXtoNDC(300),
            PixelCoords.distYtoNDC(300)
        ),

        new Texture(
                new TextureBuilder().setImagePath("/path/to/your/image/image.png").setObeyCamera(true)
        )
);
```

Incorporating this into the code into the constructor of the [base program](#creating-a-base-program), we get:

```java
import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.coords.PixelCoords;
import jangl.graphics.Image;
import jangl.graphics.Texture;
import jangl.graphics.TextureBuilder;
import jangl.io.Window;
import jangl.shapes.Rect;
import jangl.time.Clock;

public class ImageGuide {
    Image image;

    public ImageGuide() {
        this.image = new Image(
                new Rect(
                        new NDCoords(0, 0),
                        PixelCoords.distXtoNDC(300),
                        PixelCoords.distYtoNDC(300)
                ),

                new Texture(
                        new TextureBuilder().setImagePath("/path/to/your/image/image.png").setObeyCamera(true)
                )
        );
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
        new ImageGuide().run();
        Window.close();
    }
}
```

Then, we need to draw the image within the `draw()` method by calling `this.image.draw()`:

```java
import jangl.JANGL;
import jangl.coords.PixelCoords;
import jangl.coords.NDCoords;
import jangl.graphics.Image;
import jangl.graphics.Texture;
import jangl.graphics.TextureBuilder;
import jangl.io.Window;
import jangl.shapes.Rect;
import jangl.time.Clock;

public class ImageGuide {
    Image image;

    public ImageGuide() {
        this.image = new Image(
                new Rect(
                        new NDCoords(0, 0),
                        PixelCoords.distXtoNDC(300),
                        PixelCoords.distYtoNDC(300)
                ),

                new Texture(
                        new TextureBuilder().setImagePath("/path/to/your/image/image.png").setObeyCamera(true)
                )
        );
    }

    public void draw() {
        Window.clear();

        // Draw the image
        this.image.draw();
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
        new ImageGuide().run();
        Window.close();
    }
}
```

Congratulations! Now, we have an image rendered to our screen.

![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/daeea9fd-9247-4de4-91b7-67297de0840a)

### Closing the image

Finally, we need to close the image properly in order to avoid a memory leak. To do so, we need to close the `Rect` and `Texture` classes within.

This can be done by doing:
```java
image.rect().close();
image.texture().close();
```

We can incorporate these two lines at the end of the `run` method:

```java
import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.coords.PixelCoords;
import jangl.graphics.Image;
import jangl.graphics.Texture;
import jangl.graphics.TextureBuilder;
import jangl.io.Window;
import jangl.shapes.Rect;
import jangl.time.Clock;

public class ImageGuide {
    Image image;

    public ImageGuide() {
        this.image = new Image(
                new Rect(
                        new NDCoords(0, 0),
                        PixelCoords.distXtoNDC(300),
                        PixelCoords.distYtoNDC(300)
                ),

                new Texture(
                        new TextureBuilder().setImagePath("/path/to/your/image/image.png").setObeyCamera(true)
                )
        );
    }

    public void draw() {
        Window.clear();

        // Draw the image
        this.image.draw();
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

        this.image.rect().close();
        this.image.texture().close();
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        JANGL.init(1600, 900);
        new ImageGuide().run();
        Window.close();
    }
}
```

That's it! An image is being displayed to the screen and the resources are being closed properly. Thank you for following this guide.