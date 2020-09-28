import java.security.SecureRandom;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author 000359041
 * @web http://java-buddy.blogspot.com/2015/07/apply-animaton-in-javafx-charts-with.html
 */
public class MonteCarloDemonstration2 extends Application {
    final int iterations = 100000;
    
    SecureRandom random = new SecureRandom();

    int[] group = new int[ 1576 ]; // Scores in Yahtzee range from 0 to 1575
    int sum = 0;
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;
    int over150=0;
    int over200=0;
    int count = 0;

    // initialize the data set - makes more sense if your data is objects
    private void prepareData() {
        for (int i = 0; i < group.length; i++) group[i] = 0;
    }

    public static int doExperiment() {
        return new YahtzeeStrategy().play();
    }
    
    @Override
    public void start(Stage primaryStage) {
 
        prepareData();
 
        Label labelInfo = new Label();
        labelInfo.setText(
                "java.version: " + System.getProperty("java.version") + "\n"
                + "javafx.runtime.version: " + System.getProperty("javafx.runtime.version")
        );
 
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> barChart
                = new BarChart<>(xAxis, yAxis);
        barChart.setCategoryGap(0);
        barChart.setBarGap(0);
 
        xAxis.setLabel("Game Scores");
        yAxis.setLabel("Samples");
 
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Yahtzee Score Histogram");
        
        // initialize the bars
        for ( int i = 0; i < group.length; i++ )
            series1.getData().add(new XYChart.Data(Integer.toString(i), group[i]));
 
        barChart.getData().addAll(series1);
         
        Label labelCnt = new Label();
        Label labelAnimated = new Label();
 
        VBox vBox = new VBox();
        vBox.getChildren().addAll(labelInfo, barChart, labelCnt, labelAnimated);
 
        StackPane root = new StackPane();
        root.getChildren().add(vBox);
 
        Scene scene = new Scene(root, 800, 500);
 
        primaryStage.setTitle("Yahtzee Monte Carlo Demonstration");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //Apply Animating Data in Charts
        //ref: http://docs.oracle.com/javafx/2/charts/bar-chart.htm
        //"Animating Data in Charts" section

        
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(1), (ActionEvent actionEvent) -> {

            int data = doExperiment();

            sum += data;
            min = min < data ? min : data;
            max = max > data ? max : data;

            if (data>200) over200++;
            if ( data > 150) over150++;

            count++;
            
            // tuned for the idea of throwing and summing die
            // to use for other types of data this will need
            // to be altered
            group[data]++;
            
            if ( count % 500 == 0 ) {
                for ( int y = 0; y < 1576; y++ )
                    series1.getData().set(y, new XYChart.Data(Integer.toString(y), ((double)group[y]/count) ));
                
                String s;
                s = String.format( "Iterations: %d\t\t\tMin Score: %d\t\tMax Score: %d\t\tAverage Score: %.2f\nGames>150: %.2f%%\tGames>200: %.2f%%", count, min, max, ((double)sum/count), 100*((double)over150/count),100*((double)over200/count) );
                labelCnt.setText(s);
                //labelAnimated.setText("barChart.getAnimated() = " + barChart.getAnimated());
            }             
        }));
 
        timeline.setCycleCount(iterations);  // number of times to play the animation - 1 frame per cycle in this setup
        timeline.play();
 
        barChart.setAnimated(false);
         
    }
 
    public static void main(String[] args) {
        launch(args);
    }    
    
}
