package model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This class implements all the operations/commands that can applied on image.
 */
public class ImageProcessingModelImpl implements ImageProcessingModel {
  protected final Map<String, Image> LIST_OF_IMAGES;

  public ImageProcessingModelImpl() {
    LIST_OF_IMAGES = new HashMap<>();
  }

  @Override
  public Image loadImage(Image image, String imageName) {
    LIST_OF_IMAGES.put(imageName, image);
    return image;
  }

  @Override
  public Image saveImage(String imageName) {
    Image image = LIST_OF_IMAGES.getOrDefault(imageName, null);
    return image;
  }

  @Override
  public Image greyscale(String component, String sourceImageName, String destImageName) {
    Image sourceImage = LIST_OF_IMAGES.getOrDefault(sourceImageName, null);
    if (sourceImage == null) {
      return null;
    }
    Image resultImage;
    switch (component) {
      case "red-component":
        resultImage = greyscaleColorHelper(sourceImage, Color::getRedComponent);//greyscaleRedComponent(sourceImage);
        break;
      case "green-component":
        resultImage = greyscaleColorHelper(sourceImage, Color::getGreenComponent);//greyscaleGreenComponent(sourceImage);
        break;
      case "blue-component":
        resultImage = greyscaleColorHelper(sourceImage, Color::getBlueComponent);//greyscaleBlueComponent(sourceImage);
        break;
      case "value-component":
        resultImage = greyscalePixelHelper(sourceImage, Pixel::getValueComponent);//greyscaleValueComponent(sourceImage);
        break;
      case "intensity-component":
        resultImage = greyscalePixelHelper(sourceImage, Pixel::getIntensityComponent);//greyscaleIntensityComponent(sourceImage);
        break;
      case "luma-component":
        resultImage = greyscalePixelHelper(sourceImage, Pixel::getLumaComponent);//greyscaleLumaComponent(sourceImage);
        break;
      default:
        return null;
    }
    LIST_OF_IMAGES.put(destImageName, resultImage);
    return resultImage;
  }

  @Override
  public Image horizontalFlip(String sourceImageName, String destImageName) {
    Image image = LIST_OF_IMAGES.getOrDefault(sourceImageName, null);
    if (image == null) {
      return null;
    }
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j <= image.getWidth() / 2; j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsDestImage[i][j] = image.getPixels()[i][image.getWidth() - j - 1];
        listOfPixelsDestImage[i][image.getWidth() - j - 1] = image.getPixels()[i][j];
      }
    }
    Image destImage = new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
    LIST_OF_IMAGES.put(destImageName, destImage);
    return destImage;
  }

  @Override
  public Image verticalFlip(String sourceImageName, String destImageName) {
    Image image = LIST_OF_IMAGES.getOrDefault(sourceImageName, null);
    if (image == null) {
      return null;
    }
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i <= image.getHeight() / 2; i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsDestImage[i][j] = image.getPixels()[image.getHeight() - i - 1][j];
        listOfPixelsDestImage[image.getHeight() - i - 1][j] = image.getPixels()[i][j];
      }
    }
    Image destImage = new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
    LIST_OF_IMAGES.put(destImageName, destImage);
    return destImage;
  }

  @Override
  public Image brighten(int increment, String sourceImageName, String destImageName) {
    Image image = LIST_OF_IMAGES.getOrDefault(sourceImageName, null);
    if (image == null) {
      return null;
    }
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsDestImage[i][j].getColorComponent().setRedComponent(Math.max(0, (Math.min
                (image.getPixels()[i][j].getColorComponent().getRedComponent() + increment, 255))));
        listOfPixelsDestImage[i][j].getColorComponent().setGreenComponent(Math.max(0, (Math.min
                (image.getPixels()[i][j].getColorComponent().getGreenComponent() + increment,
                        255))));
        listOfPixelsDestImage[i][j].getColorComponent().setBlueComponent(Math.max(0, (Math.min
                (image.getPixels()[i][j].getColorComponent().getBlueComponent()
                        + increment, 255))));
      }
    }
    Image destImage = new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
    LIST_OF_IMAGES.put(destImageName, destImage);
    return destImage;
  }

  @Override
  public Image rgbSplit(String sourceImageName, String redImageName, String greenImageName,
                        String blueImageName) {
    Image image = LIST_OF_IMAGES.getOrDefault(sourceImageName, null);
    if (image == null) {
      return null;
    }
    Image redDestImage = greyscaleColorHelper(image, Color::getRedComponent);//greyscaleRedComponent(image);
    Image greenDestImage = greyscaleColorHelper(image, Color::getGreenComponent);//greyscaleGreenComponent(image);
    Image blueDestImage = greyscaleColorHelper(image, Color::getBlueComponent);//greyscaleBlueComponent(image);
    LIST_OF_IMAGES.put(redImageName, redDestImage);
    LIST_OF_IMAGES.put(greenImageName, greenDestImage);
    LIST_OF_IMAGES.put(blueImageName, blueDestImage);
    return redDestImage;
  }

  @Override
  public Image rgbCombine(String destImageName, String redImageName, String greenImageName,
                          String blueImageName) {
    Image redImage = LIST_OF_IMAGES.getOrDefault(redImageName, null);
    Image greenImage = LIST_OF_IMAGES.getOrDefault(greenImageName, null);
    Image blueImage = LIST_OF_IMAGES.getOrDefault(blueImageName, null);
    if (redImage == null || greenImage == null || blueImage == null) {
      return null;
    }
    Pixel[][] listOfPixelsDestImage = new Pixel[redImage.getHeight()][redImage.getWidth()];
    for (int i = 0; i < redImage.getHeight(); i++) {
      for (int j = 0; j < redImage.getWidth(); j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsDestImage[i][j].getColorComponent().setRedComponent(redImage.getPixels()[i][j]
                .getColorComponent().getRedComponent());
        listOfPixelsDestImage[i][j].getColorComponent().setGreenComponent(greenImage
                .getPixels()[i][j].getColorComponent().getGreenComponent());
        listOfPixelsDestImage[i][j].getColorComponent().setBlueComponent(blueImage
                .getPixels()[i][j].getColorComponent().getBlueComponent());
      }
    }
    Image destImage = new Image(redImage.getWidth(), redImage.getHeight(),
            redImage.getMaxValueOfColor(), listOfPixelsDestImage);
    LIST_OF_IMAGES.put(destImageName, destImage);
    return destImage;
  }

  private Image greyscalePixelHelper(Image image, Function<Pixel, Integer> getComponent) {
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsDestImage[i][j].getColorComponent().setRedComponent(
                getComponent.apply(image.getPixels()[i][j]));
        listOfPixelsDestImage[i][j].getColorComponent().setGreenComponent(
                getComponent.apply(image.getPixels()[i][j]));
        listOfPixelsDestImage[i][j].getColorComponent().setBlueComponent(
                getComponent.apply(image.getPixels()[i][j]));
      }
    }
    return new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
  }

  private Image greyscaleColorHelper(Image image, Function<Color, Integer> getComponent) {
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsDestImage[i][j].getColorComponent().setRedComponent(
                getComponent.apply(image.getPixels()[i][j].getColorComponent()));
        listOfPixelsDestImage[i][j].getColorComponent().setGreenComponent(
                getComponent.apply(image.getPixels()[i][j].getColorComponent()));
        listOfPixelsDestImage[i][j].getColorComponent().setBlueComponent(
                getComponent.apply(image.getPixels()[i][j].getColorComponent()));
      }
    }
    return new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
  }
}
