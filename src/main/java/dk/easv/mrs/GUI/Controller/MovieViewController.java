// Project imports
package dk.easv.mrs.GUI.Controller;
import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.GUI.Model.MovieModel;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MovieViewController implements Initializable {


    public TextField txtMovieSearch;
    //public ListView<Movie> lstMovies;
    private MovieModel movieModel;

    @FXML
    private TextField txtTitle, txtYear;
    @FXML
    private TableColumn<Movie, String> colTitle;
    @FXML
    private TableColumn<Movie, Integer> colYear;
    @FXML
    private TableView<Movie> tblMovies;

    public MovieViewController()  {

        try {
            movieModel = new MovieModel();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        tblMovies.setItems(movieModel.getObservableMovies());

        tblMovies.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, selectedMovie) ->
        {
            if(selectedMovie != null) {
                txtTitle.setText(selectedMovie.getTitle());
                txtYear.setText(selectedMovie.getYear() + "");
            }
        });

        txtMovieSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                movieModel.searchMovie(newValue);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });

    }

    private void displayError(Throwable t)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    /**
     * Event handler to handle creating a movie
     * @param actionEvent
     * @throws Exception
     */
    @FXML
    private void btnHandleCreateClick(ActionEvent actionEvent) throws Exception{
        String title = txtTitle.getText();
        int year = Integer.parseInt(txtYear.getText());

        Movie newMovie = new Movie(-1, year, title);

        movieModel.createMovie(newMovie);
    }

    /**
     * Event handler to handle updating a movie
     * @param actionevent
     * @throws Exception
     */
    @FXML
    private void btnHandleUpdateClick(ActionEvent actionevent){

        Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
        if(selectedMovie != null) {
            selectedMovie.setTitle(txtTitle.getText());
            selectedMovie.setYear(Integer.parseInt(txtYear.getText()));

            try{
                movieModel.updateMovie(selectedMovie);
            }
            catch (Exception err){
                displayError(err);
            }

            tblMovies.refresh();
            tblMovies.scrollTo(selectedMovie);
            tblMovies.getSelectionModel().select(tblMovies.getItems().indexOf(selectedMovie));
        }
    }

    @FXML
    private void btnHandleDeleteClick(ActionEvent actionEvent) {
        Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();

        if(selectedMovie != null){
            try {
                movieModel.deleteMovie(selectedMovie);
            }
            catch (Exception err){
                displayError(err);
            }
        }
    }
}