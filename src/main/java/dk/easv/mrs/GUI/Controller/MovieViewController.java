// Project imports
package dk.easv.mrs.GUI.Controller;
import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.GUI.Model.MovieModel;

// Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class MovieViewController implements Initializable {


    public TextField txtMovieSearch;
    public ListView<Movie> lstMovies;
    private MovieModel movieModel;

    @FXML
    private TextField txtTitle, txtYear;

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
        lstMovies.setItems(movieModel.getObservableMovies());

        lstMovies.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, selectedMovie) ->
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

        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();
        if(selectedMovie != null) {
            selectedMovie.setTitle(txtTitle.getText());
            selectedMovie.setYear(Integer.parseInt(txtYear.getText()));

            try{
                movieModel.updateMovie(selectedMovie);
            }
            catch (Exception err){
                displayError(err);
            }

            lstMovies.refresh();
            lstMovies.scrollTo(selectedMovie);
            lstMovies.getSelectionModel().select(lstMovies.getItems().indexOf(selectedMovie));
        }
    }

    @FXML
    private void btnHandleDeleteClick(ActionEvent actionEvent) {
        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();

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
