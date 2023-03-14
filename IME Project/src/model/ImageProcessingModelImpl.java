package model;

import java.util.HashMap;
import java.util.Map;

public class ImageProcessingModelImpl implements ImageProcessingModel {
  public static Map<String, Image> listOfImages = new HashMap<>();

  @Override
  public Image loadImage(String imagePath, String imageName) {
    Image image = ReadPPMUtil.readPPM(imagePath);
    if(image == null){
      return null;
    }
    listOfImages.put(imageName, image);
    return image;
  }

  @Override
  public Image saveImage(String imagePath, String imageName) {
    Image image = listOfImages.getOrDefault(imageName, null);
    if(image == null){
      return null;
    }
    CreatePPMUtil.createPPM(imagePath, image);
    return image;
  }

  @Override
  public Image greyscale(String component, String sourceImageName, String destImageName) {
    Image sourceImage = listOfImages.getOrDefault(sourceImageName, null);
    if(sourceImage==null){
      return null;
    }
    Image resultImage = null;
    switch (component) {
      case "red-component":
        resultImage = greyscaleRedComponent(sourceImage);
        break;
      case "green-component":
        resultImage = greyscaleGreenComponent(sourceImage);
        break;
      case "blue-component":
        resultImage = greyscaleBlueComponent(sourceImage);
        break;
      case "value-component":
        resultImage = greyscaleValueComponent(sourceImage);
        break;
      case "intensity-component":
        resultImage = greyscaleIntensityComponent(sourceImage);
        break;
      case "luma-component":
        resultImage = greyscaleLumaComponent(sourceImage);
        break;
    }
    listOfImages.put(destImageName, resultImage);
    return resultImage;
  }

  @Override
  public Image horizontalFlip(String sourceImageName, String destImageName) {
    Image image = listOfImages.getOrDefault(sourceImageName, null);
    if(image == null){
      return null;
    }
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    Pixel t;
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth() / 2; j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0
                , 0);
        t = listOfPixelsDestImage[i][j];
        listOfPixelsDestImage[i][j] = listOfPixelsDestImage[i][image.getWidth() - j - 1];
        listOfPixelsDestImage[i][image.getWidth() - j - 1] = t;
      }
    }
    Image destImage = new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
    listOfImages.put(destImageName, destImage);
    return destImage;
  }

  @Override
  public Image verticalFlip(String sourceImageName, String destImageName) {
    Image image = listOfImages.getOrDefault(sourceImageName, null);
    if(image == null){
      return null;
    }
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    Pixel t;
    for (int i = 0; i < image.getHeight() / 2; i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0
                , 0);
        t = listOfPixelsDestImage[i][j];
        listOfPixelsDestImage[i][j] = listOfPixelsDestImage[image.getHeight() - i - 1][j];
        listOfPixelsDestImage[image.getHeight() - i - 1][j] = t;
      }
    }
    Image destImage = new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
    listOfImages.put(destImageName, destImage);
    return destImage;
  }

  @Override
  public Image brighten(int increment, String sourceImageName, String destImageName) {
    Image image = listOfImages.getOrDefault(sourceImageName, null);
    if(image == null){
      return null;
    }
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0
                , 0);
        listOfPixelsDestImage[i][j].colorComponent.redComponent = Math.min(image.getPixels()[i][j]
                .colorComponent.redComponent + increment, image.getMaxValueOfColor());
        listOfPixelsDestImage[i][j].colorComponent.greenComponent = Math.min(image.getPixels()[i][j]
                .colorComponent.greenComponent + increment, image.getMaxValueOfColor());
        listOfPixelsDestImage[i][j].colorComponent.blueComponent = Math.min(image.getPixels()[i][j]
                .colorComponent.blueComponent + increment, image.getMaxValueOfColor());
      }
    }
    Image destImage = new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
    listOfImages.put(destImageName, destImage);
    return destImage;
  }

  @Override
  public Image rgbSplit(String sourceImageName, String redImageName, String greenImageName,
                          String blueImageName) {
    Image image = listOfImages.getOrDefault(sourceImageName, null);
    if(image == null){
      return null;
    }
    Pixel[][] listOfPixelsRedDestImage = new Pixel[image.getHeight()][image.getWidth()];
    Pixel[][] listOfPixelsGreenDestImage = new Pixel[image.getHeight()][image.getWidth()];
    Pixel[][] listOfPixelsBlueDestImage = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        listOfPixelsRedDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsGreenDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsBlueDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsRedDestImage[i][j].colorComponent.greenComponent =
                listOfPixelsRedDestImage[i][j].colorComponent.redComponent;
        listOfPixelsRedDestImage[i][j].colorComponent.blueComponent
                = listOfPixelsRedDestImage[i][j].colorComponent.redComponent;
        listOfPixelsGreenDestImage[i][j].colorComponent.redComponent =
                listOfPixelsGreenDestImage[i][j].colorComponent.greenComponent;
        listOfPixelsGreenDestImage[i][j].colorComponent.blueComponent
                = listOfPixelsGreenDestImage[i][j].colorComponent.greenComponent;
        listOfPixelsBlueDestImage[i][j].colorComponent.greenComponent =
                listOfPixelsBlueDestImage[i][j].colorComponent.blueComponent;
        listOfPixelsBlueDestImage[i][j].colorComponent.redComponent
                = listOfPixelsBlueDestImage[i][j].colorComponent.blueComponent;
      }
    }
    Image redDestImage = new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsRedDestImage);
    Image greenDestImage = new Image(image.getWidth(), image.getHeight(),
            image.getMaxValueOfColor(), listOfPixelsGreenDestImage);
    Image blueDestImage = new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsBlueDestImage);
    listOfImages.put(redImageName, redDestImage);
    listOfImages.put(greenImageName, greenDestImage);
    listOfImages.put(blueImageName, blueDestImage);
    return redDestImage;
  }

  @Override
  public Image rgbCombine(String destImageName, String redImageName, String greenImageName,
                          String blueImageName) {
    Image redImage = listOfImages.getOrDefault(redImageName, null);
    Image greenImage = listOfImages.getOrDefault(greenImageName, null);
    Image blueImage = listOfImages.getOrDefault(blueImageName, null);
    if(redImage==null || greenImage==null || blueImage==null){
      return null;
    }
    Pixel[][] listOfPixelsDestImage = new Pixel[redImage.getHeight()][redImage.getWidth()];
    for (int i = 0; i < redImage.getHeight(); i++) {
      for (int j = 0; j < redImage.getWidth(); j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsDestImage[i][j].colorComponent.redComponent
                = redImage.getPixels()[i][j].colorComponent.redComponent;
        listOfPixelsDestImage[i][j].colorComponent.greenComponent
                = greenImage.getPixels()[i][j].colorComponent.greenComponent;
        listOfPixelsDestImage[i][j].colorComponent.blueComponent
                = blueImage.getPixels()[i][j].colorComponent.blueComponent;
      }
    }
    Image destImage = new Image(redImage.getWidth(), redImage.getHeight(), redImage.getMaxValueOfColor(),
            listOfPixelsDestImage);
    listOfImages.put(destImageName, destImage);
    return destImage;
  }

  private Image greyscaleRedComponent(Image image) {
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsDestImage[i][j].colorComponent.greenComponent
                = image.getPixels()[i][j].colorComponent.redComponent;
        listOfPixelsDestImage[i][j].colorComponent.blueComponent
                = image.getPixels()[i][j].colorComponent.redComponent;
      }
    }
    return new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
  }

  private Image greyscaleGreenComponent(Image image) {
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsDestImage[i][j].colorComponent.redComponent
                = image.getPixels()[i][j].colorComponent.greenComponent;
        listOfPixelsDestImage[i][j].colorComponent.blueComponent
                = image.getPixels()[i][j].colorComponent.greenComponent;
      }
    }
    return new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
  }

  private Image greyscaleBlueComponent(Image image) {
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsDestImage[i][j].colorComponent.redComponent
                = image.getPixels()[i][j].colorComponent.blueComponent;
        listOfPixelsDestImage[i][j].colorComponent.greenComponent
                = image.getPixels()[i][j].colorComponent.blueComponent;
      }
    }
    return new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
  }

  private Image greyscaleValueComponent(Image image) {
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        listOfPixelsDestImage[i][j]
                = new Pixel(i, j, 0, 0, 0);
        listOfPixelsDestImage[i][j].colorComponent.redComponent
                = image.getPixels()[i][j].getValueComponent();
        listOfPixelsDestImage[i][j].colorComponent.greenComponent
                = image.getPixels()[i][j].getValueComponent();
        listOfPixelsDestImage[i][j].colorComponent.blueComponent
                = image.getPixels()[i][j].getValueComponent();
      }
    }
    return new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
  }

  private Image greyscaleIntensityComponent(Image image) {
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0,
                0);
        listOfPixelsDestImage[i][j].colorComponent.redComponent
                = image.getPixels()[i][j].getIntensityComponent();
        listOfPixelsDestImage[i][j].colorComponent.greenComponent
                = image.getPixels()[i][j].getIntensityComponent();
        listOfPixelsDestImage[i][j].colorComponent.blueComponent
                = image.getPixels()[i][j].getIntensityComponent();
      }
    }
    return new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
  }

  private Image greyscaleLumaComponent(Image image) {
    Pixel[][] listOfPixelsDestImage = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        listOfPixelsDestImage[i][j] = new Pixel(i, j, 0, 0
                , 0);
        listOfPixelsDestImage[i][j].colorComponent.redComponent
                = image.getPixels()[i][j].getLumaComponent();
        listOfPixelsDestImage[i][j].colorComponent.greenComponent
                = image.getPixels()[i][j].getLumaComponent();
        listOfPixelsDestImage[i][j].colorComponent.blueComponent
                = image.getPixels()[i][j].getLumaComponent();
      }
    }
    return new Image(image.getWidth(), image.getHeight(), image.getMaxValueOfColor(),
            listOfPixelsDestImage);
  }
}