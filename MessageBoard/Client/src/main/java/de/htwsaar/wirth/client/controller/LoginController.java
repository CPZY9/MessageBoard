package de.htwsaar.wirth.client.controller;

import java.net.URL;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.NotBoundException;
import java.rmi.UnknownHostException;
import java.rmi.server.ExportException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.htwsaar.wirth.client.ClientImpl;
import de.htwsaar.wirth.client.gui.ApplicationDelegate;
import de.htwsaar.wirth.client.util.PreferenceService;
import de.htwsaar.wirth.remote.exceptions.AuthenticationException;
import de.htwsaar.wirth.client.util.ExceptionUtil;
import de.htwsaar.wirth.remote.exceptions.UserAlreadyLoggedInException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtHostname;
    @FXML
    private TextField txtPort;
    @FXML
    private TextField txtGroupName;
    @FXML
    private Button btnConnect;

    private ClientImpl client;

    private ExecutorService exec;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = ClientImpl.getInstance();
        exec = Executors.newCachedThreadPool();
        initTextFields();

    }

    private void initTextFields()
    {
        txtUsername.setText(PreferenceService.getInstance().getUsername());
        txtHostname.setText(PreferenceService.getInstance().getHostName());
        txtPort.setText(PreferenceService.getInstance().getPort());
        txtGroupName.setText(PreferenceService.getInstance().getGroupeName());
    }

    public void initManager(final ApplicationDelegate delegate) {
        btnConnect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                delegate.showLoadingHUD();
                login(delegate);
            }
        });
    }

	private void login(ApplicationDelegate delegate) {
        int port;
		try {
		    port = Integer.parseInt(txtPort.getText());
		} catch(NumberFormatException e){
		    onError(e);
		    return;
        }
        PreferenceService.getInstance().setPreference(txtUsername.getText(),txtHostname.getText(),txtPort.getText(),txtGroupName.getText());
		Task<Void> task = client.login(	txtUsername.getText(), 
										txtPassword.getText(),
										txtHostname.getText(), 
										port,
										txtGroupName.getText());
		task.setOnSucceeded(e -> {
			delegate.showMainScreen();
		});
		task.setOnFailed(e -> {
			onError(e.getSource().getException());
		});
		exec.submit(task);
	}
	
	private void onError(Throwable e) {
        ApplicationDelegate.getInstance().showLoginScreen();
        try{
            throw e;
        } catch(NumberFormatException numberFormat){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ExceptionUtil.PORT_FORMAT.getLocation());
            alert.setHeaderText(ExceptionUtil.PORT_FORMAT.getDefaultText());
            alert.setContentText(ExceptionUtil.PORT_FORMAT.toString());
            alert.showAndWait();

        } catch(AuthenticationException authEx){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ExceptionUtil.WRONG_USER_OR_PSW.getLocation());
            alert.setHeaderText(ExceptionUtil.WRONG_USER_OR_PSW.getDefaultText());
            alert.setContentText(ExceptionUtil.WRONG_USER_OR_PSW.toString());
            alert.showAndWait();

        } catch(NotBoundException groupNameException){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ExceptionUtil.UNKNOWN_GROUPNAME.getLocation());
            alert.setHeaderText(ExceptionUtil.UNKNOWN_GROUPNAME.getDefaultText());
            alert.setContentText(ExceptionUtil.UNKNOWN_GROUPNAME.toString());
            alert.showAndWait();

        }catch(UnknownHostException hostException){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ExceptionUtil.UNKNOWN_HOST.getLocation());
            alert.setHeaderText(ExceptionUtil.UNKNOWN_HOST.getDefaultText());
            alert.setContentText(ExceptionUtil.UNKNOWN_HOST.toString());
            alert.showAndWait();

        }catch(ConnectException | ConnectIOException  connectionException){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ExceptionUtil.CONNECTION_ERROR.getLocation());
            alert.setHeaderText(ExceptionUtil.CONNECTION_ERROR.getDefaultText());
            alert.setContentText(ExceptionUtil.CONNECTION_ERROR.toString());
            alert.showAndWait();

        } catch(ExportException portAlreadyInUse){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ExceptionUtil.PORT_IN_USE.getLocation());
            alert.setHeaderText(ExceptionUtil.PORT_IN_USE.getDefaultText());
            alert.setContentText(ExceptionUtil.PORT_IN_USE.toString());
            alert.showAndWait();

        }catch(UserAlreadyLoggedInException userAlredyLoggedIn){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ExceptionUtil.ALLREADY_LOGGED_IN.getLocation());
            alert.setHeaderText(ExceptionUtil.ALLREADY_LOGGED_IN.getDefaultText());
            alert.setContentText(ExceptionUtil.ALLREADY_LOGGED_IN.toString());
            alert.showAndWait();

        }catch (Throwable ex){
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(ExceptionUtil.UNKNOWN_ERROR_LOGIN.getLocation());
            alert.setHeaderText(ExceptionUtil.UNKNOWN_ERROR_LOGIN.getDefaultText());
            alert.setContentText(ExceptionUtil.UNKNOWN_ERROR_LOGIN.toString());
            alert.showAndWait();
        }


    }
}
