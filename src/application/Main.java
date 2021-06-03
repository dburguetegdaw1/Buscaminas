package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import buscaminas.tablero.Tablero;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Main extends Application {
	private Tablero t;
	private Scene scene;
	private Button tF;
	private Button retirarBanderas;
	private GridPane tableroOculto;
	private GridPane tableroVisible;
	private MenuItem nuevaPartida;
	private MenuItem cambiarConfig;
	private MenuItem creditos;
	private MenuItem comoJugar;
	private Stage stage;
	private Button[][] botones;
	private Button[][] botonesOcultos;
	private boolean[][] descubierto;
	private boolean perder;
	private int Ancho;
	private int Largo;
	private int bombas;
	private int bombasDescubiertas;
	private int btnClick;

	@Override
	public void start(Stage primaryStage) {
		try {
			Ancho = 10;
			Largo = 10;
			bombas = 20;
			btnClick = 0;
			perder = false;
			t = new Tablero(Ancho, Largo, bombas);
			stage = primaryStage;
			BorderPane root = crearGUI();
			root.setBackground(new Background(new BackgroundFill(Color.rgb(175, 175, 175), null, null)));
			scene = new Scene(root, 420, 480);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setResizable(false);
			primaryStage.setTitle(" - Buscaminas - ");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	private BorderPane crearGUI() {
		BorderPane panel = new BorderPane();
		panel.setTop(crearBarraMenu());
		GridPane g = new GridPane();
		tF = new Button();
		tF.setDisable(true);
		tF.setOpacity(1);
		tF.setText((bombas) + t.cantBombas());
		tF.setAlignment(Pos.CENTER);
		retirarBanderas = new Button();
		retirarBanderas.setOpacity(1);
		retirarBanderas.setText("Retirar Banderas");
		retirarBanderas.setAlignment(Pos.CENTER);
		retirarBanderas.setOnAction(e -> retirarBanderas());
		g.add(tF, 0, 0);
		g.add(retirarBanderas, 1, 0);
		g.setHgap(10);
		g.setAlignment(Pos.CENTER);
		tF.getStyleClass().add("botonBombas");
		retirarBanderas.getStyleClass().add("botonBombas");
		panel.setBottom(g);
		panel.setPadding(new Insets(0, 0, 10, 0));
		panel.setAlignment(tF, Pos.CENTER);
		panel.setCenter(crearPanelPrincipal());
		return panel;
	}

	private MenuBar crearBarraMenu() {
		MenuBar b = new MenuBar();

		// Menu juego
		Menu m1 = new Menu();
		m1.setText("Juego");
		m1.setMnemonicParsing(true);
		nuevaPartida = new MenuItem();
		nuevaPartida.setText("Nueva Partida");
		nuevaPartida.setOnAction(e -> nPartida());
		nuevaPartida.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		cambiarConfig = new MenuItem();
		cambiarConfig.setText("Cambiar Configuración");
		cambiarConfig.setOnAction(e -> cConfig());
		cambiarConfig.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
		m1.getItems().addAll(nuevaPartida, cambiarConfig);

		// Menu Ayuda
		Menu m2 = new Menu();
		m2.setText("Ayuda");
		m2.setMnemonicParsing(true);
		creditos = new MenuItem();
		creditos.setText("Créditos");
		creditos.setOnAction(e -> mostrarCreditos());
		creditos.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
		comoJugar = new MenuItem();
		comoJugar.setText("Como Jugar");
		comoJugar.setOnAction(e -> comoJugar());
		comoJugar.setAccelerator(new KeyCodeCombination(KeyCode.J, KeyCombination.CONTROL_DOWN));
		m2.getItems().addAll(creditos, comoJugar);

		b.getMenus().addAll(m1, m2);
		return b;
	}

	@SuppressWarnings("static-access")
	private StackPane crearPanelPrincipal() {
		StackPane s = new StackPane();
		BorderPane panel = new BorderPane();
		tableroVisible = new GridPane();
		tableroVisible.setPadding(new Insets(10, 10, 5, 10));
		tableroVisible.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		tableroOculto = new GridPane();
		tableroOculto.setPadding(new Insets(10, 10, 5, 10));
		tableroOculto.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		panel.setCenter(tableroOculto);
		panel.setCenter(tableroVisible);
		descubierto = new boolean[t.getTableroVisible().length][t.getTableroVisible()[0].length];
		botonesOcultos = new Button[t.getTableroVisible().length][t.getTableroVisible()[0].length];
		for (int i = 0; i < t.getTableroOculto().length; i++) {
			for (int j = 0; j < t.getTableroOculto()[i].length; j++) {
				Button btn = new Button(t.getTableroOculto()[i][j] + "");
				btn.setText(t.getTableroOculto()[i][j] + "");
				btn.setMaxSize(Ancho, Largo);
				btn.setAlignment(Pos.CENTER_LEFT);
				if(t.getTableroOculto()[i][j] == '9') {
					btn.getStyleClass().add("botonletraguion");
				}
				else if (t.getTableroOculto()[i][j] != 'X') {
					switch(Integer.parseInt(t.getTableroOculto()[i][j]+"")) {
					case 1:
						btn.getStyleClass().add("botonletra1");
						break;
					case 2:
						btn.getStyleClass().add("botonletra2");
						break;
					case 3:
						btn.getStyleClass().add("botonletra3");
						break;
					case 4:
						btn.getStyleClass().add("botonletra4");
						break;
					case 5:
						btn.getStyleClass().add("botonletra5");
						break;
					case 6:
						btn.getStyleClass().add("botonletra6");
						break;
					case 7:
						btn.getStyleClass().add("botonletra7");
						break;
					case 8:
						btn.getStyleClass().add("botonletra8");
						break;
					}
				}
				else {
					btn.getStyleClass().add("botonletraX");
				}
				final int k = i;
				final int l = j;
				btn.setOnAction(e -> desvelarCerca(btn, k, l));
				btn.setMaxWidth(Double.MAX_VALUE);
				botonesOcultos[i][j] = btn;
				tableroOculto.add(btn, l, k);
				tableroOculto.setHgrow(btn, Priority.ALWAYS);
				tableroOculto.setVgrow(btn, Priority.ALWAYS);
			}
		}
		int rowCount = t.getTableroOculto().length;
		int columnCount = t.getTableroOculto()[0].length;

		RowConstraints rc = new RowConstraints();
		rc.setPercentHeight(100d / rowCount);

		for (int i = 0; i < rowCount; i++) {
		    tableroVisible.getRowConstraints().add(rc);
		    tableroOculto.getRowConstraints().add(rc);
		}

		ColumnConstraints cc = new ColumnConstraints();
		cc.setPercentWidth(100d / columnCount);

		for (int i = 0; i < columnCount; i++) {
		    tableroVisible.getColumnConstraints().add(cc);
		    tableroOculto.getColumnConstraints().add(cc);
		}
		botones = new Button[t.getTableroVisible().length][t.getTableroVisible()[0].length];
		for (int i = 0; i < t.getTableroVisible().length; i++) {
			for (int j = 0; j < t.getTableroVisible()[i].length; j++) {
				Button btn = new Button(t.getTableroVisible()[i][j] + "");
				btn.setText(t.getTableroVisible()[i][j] + "");
				btn.setMaxSize(Ancho, Largo);
				btn.setAlignment(Pos.CENTER_LEFT);
				btn.setOpacity(1);
				btn.getStyleClass().add("botonletraV");
				final int k = i;
				final int l = j;
				btn.setMaxWidth(Double.MAX_VALUE);
				botones[i][j] = btn;
				tableroVisible.add(btn, l, k);
				tableroVisible.setHgrow(btn, Priority.ALWAYS);
				tableroVisible.setVgrow(btn, Priority.ALWAYS);
				s.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {

					if (e.isSecondaryButtonDown() && e.isPrimaryButtonDown()) {
						btnClick += e.getClickCount()-1;
						btn.setOnAction(f -> bandera(btn));
					}
					else if (e.isPrimaryButtonDown()) {
						btnClick += e.getClickCount();
						btn.setOnAction(f -> descubrir(k, l));
					}
				});
			}
		}
		s.getChildren().add(tableroOculto);
		s.getChildren().add(tableroVisible);

		return s;
	}

	private Object bandera(Button btn) {
		if (btn.getText().equals("~")) {
			btn.setText("B");
			btn.setDisable(true);
			btn.setOpacity(1);
		}
		int calc = 0;
		for (int j2 = 0; j2 < botones.length; j2++) {
			for (int k = 0; k < botones[j2].length; k++) {
				if(botones[j2][k].getText().equals("B")) {
					calc++;
				}
			}
		}
		tF.setText((bombas-calc) + t.cantBombas());
		return btn;
	}

	@SuppressWarnings("static-access")
	private Button[][] descubrir(int i, int j) {
		botones[i][j].setText("-");
		botones[i][j].setOpacity(0);
		botones[i][j].setDisable(true);
		descubierto[i][j] = true;
		if (botonesOcultos[i][j].getText().equals("X") && (btnClick/(Ancho*Largo)) != 1) {
			for (int a = 0; a < t.getTableroOculto().length; a++) {
				for (int b = 0; b < t.getTableroOculto()[0].length; b++) {
					if (botonesOcultos[a][b].getText().equals("X")) {
						botones[a][b].setOpacity(0);
					} else if (!descubierto[a][b] && !botones[a][b].getText().equals(" ")) {
						botones[a][b].setOpacity(1);
					}
					if (botones[a][b].getText().equals("B")) {
						botones[a][b].setText("X");
						botones[a][b].getStyleClass().setAll("exploto");
						botones[a][b].setAlignment(Pos.CENTER);
						botones[a][b].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
					}
					botones[a][b].setDisable(true);
				}
			}
			perder();
			retirarBanderas.setDisable(true);
		} else if (botonesOcultos[i][j].getText().equals("X") && (btnClick/(Ancho*Largo)) == 1) {
			t.setTableroOculto(i, j);
			t.vaciarNums();
			t.setCasillasConBombaCerca();
			botonesOcultos[i][j].setText(t.getTableroOculto()[i][j] + "");
			t.colocarBomba(i, j);
			t.colocarNumeros();
			botonesOcultos = new Button[t.getTableroVisible().length][t.getTableroVisible()[0].length];
			for (int a = 0; a < t.getTableroOculto().length; a++) {
				for (int b = 0; b < t.getTableroOculto()[a].length; b++) {
					Button btn = new Button(t.getTableroOculto()[a][b] + "");
					btn.setText(t.getTableroOculto()[a][b] + "");
					btn.setMaxSize(Ancho, Largo);
					btn.setAlignment(Pos.CENTER_LEFT);
					if(t.getTableroOculto()[a][b] == '9') {
						btn.getStyleClass().add("botonletraguion");
					}
					else if (t.getTableroOculto()[a][b] != 'X') {
						switch(Integer.parseInt(t.getTableroOculto()[a][b]+"")) {
						case 1:
							btn.getStyleClass().add("botonletra1");
							break;
						case 2:
							btn.getStyleClass().add("botonletra2");
							break;
						case 3:
							btn.getStyleClass().add("botonletra3");
							break;
						case 4:
							btn.getStyleClass().add("botonletra4");
							break;
						case 5:
							btn.getStyleClass().add("botonletra5");
							break;
						case 6:
							btn.getStyleClass().add("botonletra6");
							break;
						case 7:
							btn.getStyleClass().add("botonletra7");
							break;
						case 8:
							btn.getStyleClass().add("botonletra8");
							break;
						}
					}
					else {
						btn.getStyleClass().add("botonletraX");
					}
					final int k = a;
					final int l = b;
					btn.setOnAction(e -> desvelarCerca(btn, k, l));
					btn.setMaxWidth(Double.MAX_VALUE);
					botonesOcultos[a][b] = btn;
					tableroOculto.add(btn, l, k);
					tableroOculto.setHgrow(btn, Priority.ALWAYS);
					tableroOculto.setVgrow(btn, Priority.ALWAYS);
				}
			}
			desvelar(i, j);
		} else if (botonesOcultos[i][j].getText().charAt(0) == '9') {
			desvelar(i, j);
		}
		int calcu = 0;
		for (int j2 = 0; j2 < botones.length; j2++) {
			for (int k = 0; k < botones[j2].length; k++) {
				if(botones[j2][k].getText().equals("B")) {
					calcu++;
				}
			}
		}
		if(!retirarBanderas.isDisabled())
			bombasDescubiertas = calcu;
		tF.setText((bombas-bombasDescubiertas) + t.cantBombas());
		if(ganar() && !perder) {
			mensaje();
			retirarBanderas.setDisable(true);
		}
		return botonesOcultos;
	}

	private void desvelar(int i, int j) {
		t.descubrirCasilla(i, j);
		for (int j2 = 0; j2 < botones.length; j2++) {
			for (int k = 0; k < botones[j2].length; k++) {
				try {
					if (t.getCasillaDescubierta()[k][j2]) {
						botones[k][j2].setDisable(true);
						botones[k][j2].setOpacity(0);
						botones[k][j2].setText(" ");
					}
				} catch(ArrayIndexOutOfBoundsException a) {}
			}
		}
	}
	
	private void desvelarCerca(Button btn, int i, int j) {
		if(btn.getText().equals("9")) {
			btn.setDisable(true);
		}
		else {
			int numCas = Integer.parseInt(btn.getText());
			if (numCas == cantBan(i, j)) {
				descubrir(i, j);
			}
		}
	}
	
	private int cantBan(int i, int j) {
		int cantBan = 0;
		if (botones[i-1][j-1].getText().equals("B")) {
			cantBan++;
		}
		if (botones[i-1][j].getText().equals("B")) {
			cantBan++;
		}
		if (botones[i-1][j+1].getText().equals("B")) {
			cantBan++;
		}
		if (botones[i][j-1].getText().equals("B")) {
			cantBan++;
		}
		if (botones[i][j+1].getText().equals("B")) {
			cantBan++;
		}
		if (botones[i+1][j-1].getText().equals("B")) {
			cantBan++;
		}
		if (botones[i+1][j].getText().equals("B")) {
			cantBan++;
		}
		if (botones[i+1][j+1].getText().equals("B")) {
			cantBan++;
		}
		return cantBan;
	}
	
	private void retirarBanderas() {
		for (int i = 0; i < botones.length; i++) {
			for (int j = 0; j < botones.length; j++) {
				if (botones[i][j].getText().equals("B")) {
					botones[i][j].setText("~");
					botones[i][j].setDisable(false);
					botones[i][j].setOpacity(1);
				}
			}
		}
	}

	private boolean ganar() {
		boolean ganado = false;
		int calc = 0;
		for (int i = 0; i < botones.length; i++) {
			for (int j = 0; j < botones[i].length; j++) {
				if (botones[i][j].getOpacity() == 1) {
					calc++;
				}
			}
		}
		if(calc == bombas) {
			ganado = true;
			tF.setText("0"+t.cantBombas());
			for (int i = 0; i < botones.length; i++) {
				for (int j = 0; j < botones[i].length; j++) {
					botones[i][j].setDisable(true);
					botones[i][j].setText("B");
					if(botonesOcultos[i][j].getText().equals("X")) {
						botones[i][j].setOpacity(1);
					}
					else {
						botones[i][j].setOpacity(0);
					}
				}
			}
		}
		return ganado;
	}

	private Object nPartida() {
		btnClick = 0;
		try {
			t = new Tablero(Ancho, Largo, bombas);
			BorderPane root = crearGUI();
			root.setBackground(new Background(new BackgroundFill(Color.rgb(175, 175, 175), null, null)));
			scene = new Scene(root, 40 * Ancho+20, 40 * Largo + 80);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setResizable(false);
			stage.setTitle(" - Buscaminas - ");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		btnClick = 0;
		perder = false;
		return scene;
	}

	private Object cConfig() {
		try {
			List<Integer> op1 = Arrays.asList(3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
			ChoiceDialog<Integer> d = new ChoiceDialog<>(10, op1);
			d.setTitle("Configurar");
			d.setHeaderText(null);
			d.setContentText("Escoge tamaño del tablero");
			Optional<Integer> opt1 = d.showAndWait();
			List<Integer> op3 = new ArrayList<>();
			for (int i = 1; i <= (opt1.get().intValue() * opt1.get().intValue()) / 3; i++) {
				op3.add(i);
			}
			ChoiceDialog<Integer> d3 = new ChoiceDialog<>((opt1.get().intValue() * opt1.get().intValue()) * 2 / 10,
					op3);
			d3.setTitle("Configurar");
			d3.setHeaderText(null);
			d3.setContentText("Selecciona cantidad de bombas");
			Optional<Integer> opt3 = d3.showAndWait();
			Ancho = opt1.get().intValue();
			Largo = opt1.get().intValue();
			bombas = opt3.get().intValue();
			t = new Tablero(Ancho, Largo, bombas);
			BorderPane root = crearGUI();
			root.setBackground(new Background(new BackgroundFill(Color.rgb(175, 175, 175), null, null)));
			scene = new Scene(root, 40 * opt1.get().intValue()+20, 40 * opt1.get().intValue() + 80);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setResizable(false);
			stage.setTitle(" - Buscaminas - ");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scene;
	}

	private Object comoJugar() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("application1.css").toExternalForm());
		alert.setHeaderText(null);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.setTitle("Como Jugar");
		String cont = "Controles: Mover el ratón para hacer click en las diferentes casillas\n";
		cont += "\tLos números inidcan cúantas bombas hay a su alrededor\n\tTu eres el encargado de averiguar donde se encuentran\n";
		cont += "Para colocar una bandera, has de hacer click con los dos botones del ratón\n\n";
		alert.setContentText(cont);
		alert.showAndWait();
		return alert;
	}

	private Object mostrarCreditos() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		DialogPane dialogPane = alert.getDialogPane();
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		alert.setHeaderText(null);
		alert.setTitle("Créditos");
		alert.setContentText(t.creditosFinales());
		alert.showAndWait();
		return alert;
	}

	private void perder() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		DialogPane dialogPane = alert.getDialogPane();
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		dialogPane.getStylesheets().add(getClass().getResource("application2.css").toExternalForm());
		alert.setHeaderText(null);
		alert.setGraphic(null);
		alert.setTitle("Fin de partida");
		alert.setContentText("Es una lástima\n\tHas perdido\n\tHas hecho un total de " + (btnClick/(Ancho*Largo)) + " clicks");
		alert.showAndWait();
		btnClick = 0;
		perder = true;
	}
	
	private void mensaje(){
		Alert d = new Alert(Alert.AlertType.INFORMATION);
		DialogPane dialogPane = d.getDialogPane();
		d.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		dialogPane.getStylesheets().add(getClass().getResource("application3.css").toExternalForm());
		d.setGraphic(null);
		d.setHeaderText(null);
		d.setTitle("VICTORIA");
		d.setContentText("¡Enhorabuena!\n\t¡Has ganado!\n\tHas hecho un total de " + (btnClick/(Ancho*Largo)) + " clicks");
		d.showAndWait();
		btnClick = 0;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
