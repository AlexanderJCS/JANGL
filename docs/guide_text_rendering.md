# Text Rendering

This guide will show how to render text to the screen using JANGL. This guide assumes you have already read the [quickstart guide](/README.md#quickstart-guide).

## Hiero
The first step is to [download the Hiero software](https://libgdx.com/wiki/tools/hiero). This application will be used to create font files. If you do not want to use Hiero, you can also download the Arial font files [here](/src/test/resources/demo/font).

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

import jangl.Jangl;

public class TextGuide {
    public TextGuide() {

    }

    public static void main(String[] args) {
        Jangl.init(1600, 900);  // screen width in pixels, screen height in pixels
        Window.setVsync(true);
        
        Window.close();
    }
}
```

Then, create a `Font` variable inside the constructor. The `Font` constructor takes in two arguments: the path to the `.fnt` file and the path to the `.png` file associated with the font file.

```java
import jangl.Jangl;
import jangl.graphics.font.Font;

public class TextGuide {
    public TextGuide() {
        Font myFont = new Font(
                "path/to/fnt/file/fontName.fnt",
                "path/to/png/file/fontName.png"
        );
    }

    public static void main(String[] args) {
        Jangl.init(1600, 900);  // screen width in pixels, screen height in pixels
        Window.setVsync(true);

        Window.close();
    }
}
```

Now, you can create a `TextBuilder` class that takes in the following constructor arguments:
- The `Font` class
- The string of text to display
- The x and y coordinates of the top left corner of the text

Below, we create a new Text class in the constructor by calling the `textBuilder.toText()` method.

```java
import jangl.Jangl;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Text;
import jangl.graphics.font.Font;
import jangl.graphics.font.TextBuilder;
import jangl.io.Window;

public class TextGuide {
    private final Text text;

    public TextGuide() {
        Font myFont = new Font(
                "path/to/fnt/file/fontName.fnt",
                "path/to/png/file/fontName.png"
        );

        this.text = new TextBuilder(myFont, "Hello World", new WorldCoords(0.1f, 0.9f)).toText();
    }

    public static void main(String[] args) {
        Jangl.init(1600, 900);  // screen width in pixels, screen height in pixels
        Window.setVsync(true);

        Window.close();
    }
}
```

Now we create a `run` method to draw the text to the screen, using the `text.draw()` method. We also create a new `TextGuide` object in the `main` method and call the `run` method.

Now, we have text rendering to the screen!

```java
import jangl.Jangl;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Text;
import jangl.graphics.font.Font;
import jangl.graphics.font.TextBuilder;
import jangl.io.Window;

public class TextGuide {
    private final Text text;

    public TextGuide() {
        Font myFont = new Font(
                "path/to/fnt/file/fontName.fnt",
                "path/to/png/file/fontName.png"
        );

        this.text = new TextBuilder(myFont, "Hello World", new WorldCoords(0.1f, 0.9f)).toText();
    }

    public void run() {
        while (Window.shouldRun()) {
            Window.clear();
            this.text.draw();

            Jangl.update();
        }
    }

    public static void main(String[] args) {
        Jangl.init(1600, 900);  // screen width in pixels, screen height in pixels
        Window.setVsync(true);

        TextGuide textGuide = new TextGuide();
        textGuide.run();

        Window.close();
    }
}
```

We can also customize the text. Here, we change the text in the following ways:
- We make the text red by calling `text.setColor(Color.RED)`
- We make the text center-justified by calling `builder.setJustification(Justify.CENTER)` (now the text is centered around the x coordinate we passed into the `TextBuilder` constructor)
- We make the text 0.1 world coords tall by calling `builder.setHeight(0.1f)`
- We make the text wrap at 0.4 world coords wide by calling `builder.setWrapWidth(0.4f)`
- We make the text cut off at 0.2 world coords tall by calling `builder.setYCutoff(0.2f)`

```java
import jangl.Jangl;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Justify;
import jangl.graphics.font.Text;
import jangl.graphics.font.Font;
import jangl.graphics.font.TextBuilder;
import jangl.io.Window;

public class TextGuide {
    private final Text text;

    public TextGuide() {
        Font myFont = new Font(
                "path/to/fnt/file/fontName.fnt",
                "path/to/png/file/fontName.png"
        );

        myFont.setFontColor(ColorFactory.RED);  // sets the color of the font to red

        this.text = new TextBuilder(myFont, "Hello World", WorldCoords.getMiddle())
                .setJustification(Justify.CENTER)  // sets the justification of the text
                .setHeight(0.1f)  // sets the height of the text in world coords, essentially the font size
                .setWrapWidth(0.4f)  // sets the width at which the text will wrap (this text cannot be more than 0.4 world coords wide)
                .setYCutoff(0.2f)  // sets the height at which the text will be cut off (this text cannot be more than 0.2 world coords tall)
                .toText();  // builds the text object
    }

    public void run() {
        while (Window.shouldRun()) {
            Window.clear();
            this.text.draw();

            Jangl.update();
        }
    }

    public static void main(String[] args) {
        Jangl.init(1600, 900);  // screen width in pixels, screen height in pixels
        Window.setVsync(true);

        TextGuide textGuide = new TextGuide();
        textGuide.run();

        Window.close();
    }
}
```

That's it! We covered all the capabilities of JANGL text.