import java.io.IOException;

import controller.ImageProcessingController;
import controller.ImageProcessingControllerImpl;
import model.ImageProcessingModel;
import model.ImageProcessingModelImpl;

/**
 * The entry point of the program where we can create Image Processing Model and
 * run some operations on it.
 */

public class ImageManipulator {
  /**
   * Main method that starts the application.
   *
   * @param args accepts a single argument of type array.
   */
  public static void main(String[] args) {
    ImageProcessingModel model = new ImageProcessingModelImpl();
    ImageProcessingController controller = new ImageProcessingControllerImpl(model, System.in
            , System.out);
    controller.execute();

  }
}
