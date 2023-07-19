# Text Rendering

Creating and displaying text to the screen is easy.

## Hiero
The first step is to [download the Hiero software](https://libgdx.com/wiki/tools/hiero). This application will be used to create font files. If you do not want to use Hiero, you can also download the Arial font files [here](/src/demo/demoResources/font).

1. Select which font to use on the top left. Either select a file or use a system font.
2. On the left, select if you want your text to be bold or italic.
3. On the right, select the color of the text by clicking on the Color box.
4. In the "Sample Text" area, select which charset you want. The options are: extended, NEHE, and ASCII. The default is ASCII. **If you choose the extended charset, make sure to manually add a space character since Hiero does not include it automatically.**
5. On the left, below the font selection area, set the font size. Larger font sizes consumes more memory, but it is also less pixelated. It is recommended to choose a font size that suits the text in your application.
6. Click on "Glyph cache" above the window displaying the sample text. This reveals the "pages" value and page width and height settings. Lower the width and height to the smallest possible image while the pages value is still 1. JANGL does not support multiple pages of text.
7. Select File -> `Save BMFont files (text)` and select a location to save your `.fnt` and `.png` files that will be accessible to your Java program.

Now, you can close Hiero.

## Loading the font & displaying text

Moving to Java, the next step is to create a class that initializes JANGL as well as  an empty constructor:

```java

import jangl.JANGL;
import jangl.Jangl;

public class FontDemo {
    public FontDemo() {

    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);  // screen width in pixels, screen height in pixels
    }
}
```

Then, create a `Font` variable inside the constructor. The `Font` constructor takes in two arguments: the path to the `.fnt` file and the path to the `.png` file associated with the font file.

```java
import jangl.JANGL;
import jangl.Jangl;
import jangl.graphics.font.Font;

public class FontDemo {
    public FontDemo() {
        Font myFont = new Font(
                "path/to/fnt/file/fontName.fnt",
                "path/to/png/file/fontName.png"
        );
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);  // screen width in pixels, screen height in pixels
    }
}
```

Now, you can create a `Text` class that takes in the following constructor arguments:
- The top left position of the text
- The `Font` class
- The y height of the text, in `NDCoords` (normalized device coordinates)
- The text you want to display (string)

For this example, the text will be a member variable of `FontDemo` so it can be accessed through a `run` method that will be created later.

```java
import jangl.JANGL;
import jangl.JaNGL;
import jangl.Jangl;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Text;
import jangl.graphics.font.Font;

public class FontDemo {
    Text text;

    public FontDemo() {
        Font myFont = new Font(
                "path/to/fnt/file/fontName.fnt",
                "path/to/png/file/fontName.png"
        );

        this.text = new Text(
                new WorldCoords(-0.5f, 0),  // the top left coordinate of the text
                myFont,  // the font object
                0.1f,  // the y height, in normalized device coords, of the text
                "Hello World!"  // the text to display
        );
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);  // screen width in pixels, screen height in pixels
    }
}
```

Now, this text needs to be drawn to the screen. To do so, a new method called `run` is created. Inside the method, the text is drawn at 60 FPS. If you are unclear about anything else in the run method, the [quickstart guide](/README.md#quickstart-guide) explains that more clearly.

```java
import jangl.JANGL;
import jangl.JaNGL;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Text;
import jangl.graphics.font.Font;
import jangl.io.Window;
import jangl.time.Clock;
import jangl.time.GameClock;

public class FontDemo {
    Text text;

    public FontDemo() {
        Font myFont = new Font(
                "path/to/fnt/file/fontName.fnt",
                "path/to/png/file/fontName.png"
        );

        this.text = new Text(
                new WorldCoords(-0.5f, 0),  // the top left coordinate of the text
                myFont,  // the font object
                0.1f,  // the y height, in normalized device coords, of the text
                "Hello World!"  // the text to display
        );
    }

    public void run() {
        while (Window.shouldRun()) {
            this.text.draw();

            JANGL.update();
            Clock.busyTick(60);
        }

        Window.close();
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);  // screen width in pixels, screen height in pixels
    }
}
```

Finally, we need to add a few finishing touches:
- Create a new FontDemo class and call the run method
- Close the font and text after the `run()` method

```java
import jangl.JANGL;
import jangl.JaNGL;
import jangl.Jangl;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Font;
import jangl.graphics.font.Text;
import jangl.io.Window;
import jangl.time.Clock;
import jangl.time.GameClock;

public class FontDemo {
    Text text;

    public FontDemo() {
        Font myFont = new Font(
                "path/to/fnt/file/fontName.fnt",
                "path/to/png/file/fontName.png"
        );

        this.text = new Text(
                new WorldCoords(-0.5f, 0),  // the top left coordinate of the text
                myFont,  // the font object
                0.1f,  // the y height, in normalized device coords, of the text
                "Hello World!"  // the text to display
        );
    }

    public void run() {
        while (Window.shouldRun()) {
            this.text.draw();

            JANGL.update();
            Clock.busyTick(60);
        }

        Window.close();

        // It is important to close the Font object in addition to the text object
        // to avoid a memory leak. To do so, we can call the close() method.
        // It is important to note that text.close() does not close the font.
        this.text.getFont().close();
        this.text.close();
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);  // screen width in pixels, screen height in pixels
        new FontDemo().run();  // run a new FontDemo
    }
}
```

And that's it! Text is now rendering to the screen.

![image](https://github.com/AlexanderJCS/JANGL/assets/98898166/59203b17-3219-4e25-915e-9285f4410bda)