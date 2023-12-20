package com.socialnetwork.map_toysocialnetwork;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private static final int ITEMS_PER_PAGE = 10;

    @Override
    public void start(Stage primaryStage) {
        TableView<Person> tableView = createTableView();
        Pagination pagination = createPagination(tableView);
        VBox root = new VBox(tableView, pagination);
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Paginated Table Example");
        primaryStage.show();
    }

    private TableView<Person> createTableView() {
        TableView<Person> tableView = new TableView<>();
        TableColumn<Person, String> firstNameCol = new TableColumn<>("First Name");
        TableColumn<Person, String> lastNameCol = new TableColumn<>("Last Name");

        tableView.getColumns().addAll(firstNameCol, lastNameCol);
        tableView.setItems(generateDummyData());

        return tableView;
    }

    private Pagination createPagination(TableView<Person> tableView) {
        int pageCount = (int) Math.ceil((double) tableView.getItems().size() / ITEMS_PER_PAGE);

        Pagination pagination = new Pagination(pageCount, 0);
        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * ITEMS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, tableView.getItems().size());
            tableView.setItems(FXCollections.observableArrayList(tableView.getItems().subList(fromIndex, toIndex)));
            return tableView;
        });

        return pagination;
    }

    private ObservableList<Person> generateDummyData() {
        ObservableList<Person> data = FXCollections.observableArrayList();
        for (int i = 1; i <= 100; i++) {
            data.add(new Person("First" + i, "Last" + i));
        }
        return data;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class Person {
        private final String firstName;
        private final String lastName;

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }
}
