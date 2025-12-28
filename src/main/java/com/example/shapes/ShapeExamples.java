package com.example.shapes;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShapeExamples extends Application {
    private List<Shape> shapes = new ArrayList<>();
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private Scene scene;
    EventHandler<MouseEvent> groupmovement = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            mouseEvent.consume();
        }
    };
    EventHandler<ScrollEvent> allScrolls = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent scrollEvent) {
            for (Shape shape : shapes) {
                randomColor(shape);
            }
        }
    };

    EventHandler<MouseEvent> shapedragged = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent mouseEvent) {
            Node node = (Node) mouseEvent.getSource();
            if (MouseEvent.MOUSE_PRESSED.equals(mouseEvent.getEventType())) {
                orgSceneX = mouseEvent.getSceneX();
                orgSceneY = mouseEvent.getSceneY();
                orgTranslateX = node.getTranslateX();
                orgTranslateY = node.getTranslateY();
                System.out.println(orgSceneX);

            } else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEvent.getEventType())) {
                double offsetX = mouseEvent.getSceneX() - orgSceneX;
                double offsetY = mouseEvent.getSceneY() - orgSceneY;
                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;

                node.setTranslateX(newTranslateX);
                node.setTranslateY(newTranslateY);
                Bounds b = node.localToScene(node.getBoundsInLocal());
                if (b.getMinX() < 0) {
                    node.setTranslateX(node.getTranslateX() - b.getMinX());
                }
                if (b.getMinY() < 0) {
                    node.setTranslateY(node.getTranslateY() - b.getMinY());
                }
                double width = node.getScene().getWidth();
                double height = node.getScene().getHeight();
                if (b.getMaxX() > width) {
                    node.setTranslateX(node.getTranslateX() - (b.getMaxX() - width));

                }
                if (b.getMaxY() > height) {
                    node.setTranslateY(node.getTranslateY() - (b.getMaxY() - height));
                }

            }
        }
    };


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Group group = new Group();
        CheckBox cb = new CheckBox("Gruppenbewegung");
        Rectangle rec = new Rectangle(0, 0, 150, 70);
        rec.setFill(Color.AQUA);
        Circle circ = new Circle(220, 50, 50, Color.GREEN);
        Polygon pol = new Polygon(new double[]{
                300.0, 0.0,
                400.0, 0.0,
                300.0, 100.0
        });
        pol.setFill(Color.CORAL);
        shapes.add(rec);
        shapes.add(circ);
        shapes.add(pol);

        scene = new Scene(root, 600, 600);
        group.getChildren().addAll(shapes);


        cb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (cb.isSelected()) {
                    group.addEventFilter(MouseEvent.ANY, groupmovement);
                    group.addEventFilter(MouseEvent.ANY, shapedragged);
                    group.addEventFilter(ScrollEvent.ANY, allScrolls);
                } else {
                    group.removeEventFilter(MouseEvent.ANY, shapedragged);
                    group.removeEventFilter(MouseEvent.ANY, groupmovement);
                    group.removeEventFilter(ScrollEvent.ANY, allScrolls);
                }
            }
        });

        for (Shape shape : shapes) {
            changeColor(shape);
            resetColor(shape, Color.BLACK);
            shape.addEventHandler(MouseEvent.MOUSE_PRESSED, shapedragged);
            shape.addEventHandler(MouseEvent.MOUSE_DRAGGED, shapedragged);

        }
        root.getChildren().addAll(group, cb);
        primaryStage.setTitle("Shapes Zeichnen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void randomColor(Shape shape) {
        Random random = new Random();
        Color c = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        shape.setFill(c);
    }

    private void changeColor(Shape shape) {

        shape.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                randomColor(shape);
            }
        });
    }

    private void resetColor(Shape shape, Color color) {
        shape.setOnMouseClicked(new EventHandler<MouseEvent>() {
            Color reset = color;

            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        shape.setFill(reset);
                    }
                }
            }
        });
    }

}


