package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * handles the GUI
 * 
 * @author Alex
 *
 */
public class GUI {
	/**
	 * creates and handles the main menu
	 * 
	 * @param primaryStage
	 *            stage
	 */
	public void menu(Stage primaryStage) {
		RedBlackTree<Integer> rb = new RedBlackTree<Integer>();
		RBView view = new RBView(rb);

		BorderPane bp = new BorderPane();
		Scene sc = new Scene(bp, 500, 500);
		sc.getStylesheets().add("application/application.css");

		VBox top = new VBox();
		top.getStyleClass().add("optionsBackground");
		Label title = new Label("Red Black Tree");
		title.getStyleClass().add("title");
		top.getChildren().add(title);

		VBox bottom = new VBox();
		bottom.getStyleClass().add("background");
		HBox options = new HBox(3);
		options.getStyleClass().add("optionsBackground");
		TextField tf = new TextField();
		tf.setPromptText("Enter a value (max 3 digits)...");
		tf.setPrefColumnCount(3);
		Button insert = new Button("Insert");
		Button delete = new Button("Delete");
		options.getChildren().add(insert);
		options.getChildren().add(delete);
		bottom.getChildren().add(tf);
		bottom.getChildren().add(options);
		
		ScrollPane sp = new ScrollPane();
		sp.getStyleClass().add("background");
		sp.setContent(view);

		bp.setTop(top);
		bp.setCenter(view);
		bp.setBottom(bottom);

		primaryStage.setScene(sc);
		primaryStage.show();

		insert.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				int value = Integer.parseInt(tf.getText());

				if (tf.getText().equals("")) {
					view.displayTree();
					view.setStatus("Invalid Input");
				} else {
					try {
						if (value < 0) {
							view.displayTree();
							view.setStatus("Invalid Input");
						} else {
							if (rb.find(value)) {
								view.displayTree();
								view.setStatus(value + " is already in the tree...");
							} else {
								rb.insert(value);
								view.displayTree();
								tf.clear();
								view.setStatus(value + " has been inserted in the tree...");
							}
						}
					} catch (NumberFormatException e1) {
						tf.clear();
					}
				}
			}
		});

		delete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				int value = Integer.parseInt(tf.getText());
				if (!rb.find(value)) {
					view.displayTree();
					view.setStatus(value + " is not in the tree...");
				} else {
					rb.delete(value);
					view.displayTree();
					tf.clear();
					view.setStatus(value + " has been deleted in the tree...");
				}
			}
		});
	}

	/**
	 * handles creation and refresh for the tree
	 * 
	 * @author Alex
	 *
	 */
	public class RBView extends Pane {
		private RedBlackTree<Integer> rb = new RedBlackTree<>();
		private double radius = 15;
		private double vGap = 50;

		/**
		 * default constructor
		 * 
		 * @param rb
		 *            red black tree
		 */
		RBView(RedBlackTree<Integer> rb) {
			this.rb = rb;
			setStatus("Tree is empty");
		}

		/**
		 * displays message
		 * 
		 * @param msg
		 *            message
		 */
		public void setStatus(String msg) {
			getChildren().add(new Text(20, 20, msg));
		}

		public void displayTree() {
			this.getChildren().clear();
			if (rb.root != rb.nil) {
				displayTree(rb.root, getWidth() / 2, vGap, getWidth() / 4);
			}
		}

		/**
		 * creates the tree
		 * 
		 * @param node
		 *            node
		 * @param x
		 *            x coordinate
		 * @param y
		 *            y coordinate
		 * @param hGap
		 *            horizontal gap
		 */
		public void displayTree(RBNode<Integer> node, double x, double y, double hGap) {
			if (node.left != rb.nil) {
				getChildren().add(new Line(x - hGap, y + vGap, x, y));
				displayTree(node.left, x - hGap, y + vGap, hGap / 2);
			}

			if (node.right != rb.nil) {
				getChildren().add(new Line(x + hGap, y + vGap, x, y));
				displayTree(node.right, x + hGap, y + vGap, hGap / 2);
			}

			if (node.color == "R") {
				Circle circle = new Circle(x, y, radius);
				circle.setFill(Color.RED);
				circle.setStroke(Color.BLACK);
				getChildren().addAll(circle, new Text(x - 4, y + 4, node.getData() + ""));
			} else {
				Circle circle = new Circle(x, y, radius);
				circle.setFill(Color.BLACK);
				circle.setStroke(Color.BLACK);
				Text value = new Text(x - 4, y + 4, node.getData() + "");
				value.setFill(Color.WHITE);
				getChildren().addAll(circle, value);
			}
		}
	}
}
