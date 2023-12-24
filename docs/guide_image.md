# Image Guide

This guide will show you how to display an image to the screen. Before following this guide, it is recommended to view the [quickstart guide](/README.md#quickstart-guide) and the [rectangles section of the shape guide](guide_shapes.md#rectangles).

## Preparing an image

The first step is to prepare an image to use for this guide. Common file formats that are supported are `.png` and `.jpg`, among others. For the purposes of this guide, it is recommended that your image has a square aspect ratio.

If you do not want to create an image, you can use the one below:

![image](https://github.com/AlexanderJCS/Jangl/assets/98898166/c3ea4ebc-8fb9-4974-b033-de762ea1b337)

## Creating a base program

Before we start, we need to create a base program that will contain our shapes. This code is largely explained within the [quickstart guide](/README.md#quickstart-guide), and therefore will not be explained in great detail. This code will be used later.

```java
import jangl.Jangl;
import jangl.io.Window;

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
            Jangl.update();
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        Jangl.init(1600, 900);
        Window.setVsync(true);

        new ImageGuide().run();

        Window.close();
    }
}
```

## Creating an image

The `Image` record provides a wrapper for the combination of a shape and up two classes that implement the Bindable interface. The bindables modify how the shape is displayed, by using shaders, displaying an image, etc.

Below are the constructors for the Image class:
```java
public Image(Shape shape, Bindable bindable1)
public Image(Shape shape, Bindable bindable1, Bindable bindable2)
```

In this guide, we will only be using the first constructor.

### Creating a rectangle

The first item the `Image` record needs is a rectangle for the image to be displayed on to. Images can be scaled and stretched to any size, but to maintain image quality, we will make the rectangle's aspect ratio equal to the image's aspect ratio (square in the case of the example image).

If you do not understand the code below, it is recommended to read the [quickstart guide](/README.md#quickstart-guide) and the [rectangles section of the shape guide](guide_shapes.md#rectangles)

Below we define our rectangle. This code creates a new rectangle whose top left coordinate is at (0.5, 0.5) and has a width of (0.3, 0.3). Since its width and height are the same, it is a perfect square.

```java
new Rect(
        new WorldCoords(0.5f, 0.5f), 0.3f, 0.3f
);
```

### Creating a texture

The `Texture` class's constructor is
```java
public Texture(TextureBuilder);
```

To assign image data to a `TextureBuilder`, you can do:
```java
new TextureBuilder().setImagePath("path/to/your/image/image.png");
```

Alternatively, you can use a shortcut for this by writing:
```java
new TextureBuilder("/path/to/your/image/image.png");
```

You can also fill the texture with a solid color by writing:
```java
new TextureBuilder().fill(color, width, height);
```

Where `color` is a `jangl.color.Color` object, `width` is the width of the image in pixels, and `height` is the height of the image in pixels.

Another important method in the `TextureBuilder` class is the `setObeyCamera(bool)` method. When the argument is true, the object will move according to the camera. By default, the object obeys the camera. Sometimes it is useful for the object to not obey the camera when you want to display a UI element that should always remain on screen.

Sometimes, with small textures (such as pixel art), the image can appear blurry. This is because the image has linear filter mode enabled. To disable linear filtering, you can write:
```java
new TextureBuilder("path/to/your/image/image.png")
        .setPixelatedScaling(false)
```

Putting this together, we can create our texture by writing:
```java
new TextureBuilder("path/to/your/image/image.png")
        .setObeyCamera(false)
        .toTexture();
```

### Creating an image using the Rect and Texture

Now that we have a rectangle and texture, we can create an image:
```java
new Image(
    new Rect(
        new WorldCoords(0.5f, 0.5f), 0.3f, 0.3f
    ),
        
    new Texture(
        new TextureBuilder("path/to/your/image/image.png")
        .setObeyCamera(false)
        .toTexture()
    )
);
```

Incorporating this into the code into the constructor of the [base program](#creating-a-base-program), we get:

```java
import jangl.Jangl;
import jangl.coords.WorldCoords;
import jangl.graphics.textures.Image;
import jangl.graphics.textures.Texture;
import jangl.graphics.textures.TextureBuilder;
import jangl.io.Window;
import jangl.shapes.Rect;

public class ImageGuide {
    Image image;

    public ImageGuide() {
        this.image = new Image(
                new Rect(
                        new WorldCoords(0.5f, 0.5f), 0.3f, 0.3f
                ),

                new Texture(
                        new TextureBuilder("path/to/your/image/image.png")
                                .setObeyCamera(false)
                                .toTexture()
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
            Jangl.update();
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        Jangl.init(1600, 900);
        Window.setVsync(true);

        new ImageGuide().run();

        Window.close();
    }
}
```

Then, we need to draw the image within the `draw()` method by calling `this.image.draw()`:

```java
import jangl.Jangl;
import jangl.coords.WorldCoords;
import jangl.graphics.textures.Image;
import jangl.graphics.textures.Texture;
import jangl.graphics.textures.TextureBuilder;
import jangl.io.Window;
import jangl.shapes.Rect;

public class ImageGuide {
    private final Image image;

    public ImageGuide() {
        this.image = new Image(
                new Rect(
                        new WorldCoords(0.5f, 0.5f), 0.3f, 0.3f
                ),

                new Texture(
                        new TextureBuilder("path/to/your/image/image.png")
                                .setObeyCamera(false)
                                .toTexture()
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
            Jangl.update();
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        Jangl.init(1600, 900);
        Window.setVsync(true);

        new ImageGuide().run();

        Window.close();
    }
}
```

Congratulations! Now, we have an image rendered to our screen.

![image](https://github.com/AlexanderJCS/Jangl/assets/98898166/daeea9fd-9247-4de4-91b7-67297de0840a)

## Repeating the Texture

If you want to repeat the image on the rectangle, we first need to modify the wrap mode on the texture. We can achieve this by writing:
```java
import jangl.graphics.textures.TextureBuilder;
import jangl.graphics.textures.WrapMode;

new TextureBuilder("path/to/your/image/image.png")
        .setWrapMode(WrapMode.REPEAT)
        .toTexture()
```

We then need to modify the repeat values on the shape.
```java
shape.setTexRepeatX(2);
shape.setTexRepeatY(2);
```

```
NOTICE: setTexRepeatX() and setTexRepeatY() do not work for the TileSheetRect class.
```

Incorporating this into the code, we get:

```java
import jangl.Jangl;
import jangl.coords.WorldCoords;
import jangl.graphics.textures.Image;
import jangl.graphics.textures.TextureBuilder;
import jangl.graphics.textures.enums.WrapMode;
import jangl.io.Window;
import jangl.shapes.Rect;

public class ImageGuide {
    private final Image image;

    public ImageGuide() {
        this.image = new Image(
                new Rect(
                        new WorldCoords(0.5f, 0.5f), 0.3f, 0.3f
                ),

                new TextureBuilder("/path/to/your/image/image.png")
                        .setImagePath("/path/to/your/image/image.png")
                        .setWrapMode(WrapMode.REPEAT)
                        .toTexture()
        );

        this.image.shape().setTexRepeatX(2);
        this.image.shape().setTexRepeatY(2);
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
            Jangl.update();
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        Jangl.init(1600, 900);
        Window.setVsync(true);

        new ImageGuide().run();

        Window.close();
    }
}
```

Thank you for following this guide.

