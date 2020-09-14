package application;
	
import java.time.ZoneId;
import java.util.Date;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Caso;
import service.BaseService;
import service.JsonService;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {						
			primaryStage.setTitle("Gestión COVID");
			
			//----    ----    ----    --------    --------    ----
			//panel de gestión ----    ----    ----    ----    ----
	        GridPane grdPanelGestion = new GridPane();
	        grdPanelGestion.setVgap(10);
	        grdPanelGestion.setHgap(20);
			Label lbSelectCom=new Label("Seleccione la comunidad");
			BaseService service=new JsonService("c:\\temp\\datos_ccaas.json");			
			ObservableList<String> listaComunidades = FXCollections.observableList(service.comunidades()); 
			ListView<String> lstComunidades = new ListView<>(listaComunidades);
			lstComunidades.setMinHeight(250);
			lstComunidades.setMinWidth(150);
			
		    ToggleGroup group = new ToggleGroup();
		    RadioButton btnTodasFechas = new RadioButton("Todas las fechas");
		    btnTodasFechas.setMinWidth(125);
		    btnTodasFechas.setToggleGroup(group);
		    btnTodasFechas.setSelected(true);
		    RadioButton btnEntreFechas = new RadioButton("Entre fechas");
		    btnEntreFechas.setMinWidth(125);
		    btnEntreFechas.setToggleGroup(group);
		    
		    DatePicker dpFecha1 = new DatePicker();
		    DatePicker dpFecha2 = new DatePicker();
		    dpFecha1.setMinWidth(100);
		    dpFecha2.setMinWidth(100);
		    
		    TableView<Caso> table = new TableView<Caso>();
		    table.setVisible(false);
		    		    
		    Button btnVerCasos=new Button("Ver casos");
		    btnVerCasos.setMinWidth(120);
		    btnVerCasos.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					table.getColumns().clear();
					table.setVisible(true);
					ObservableList<Caso> lista;
					if (btnTodasFechas.isSelected() ) {
						lista=FXCollections.observableList(service.casosDeComunidad( lstComunidades.getSelectionModel().getSelectedItem() ));
					} else {
						 ZoneId defaultZoneId = ZoneId.systemDefault();
						 Date date1 = Date.from(dpFecha1.getValue().atStartOfDay(defaultZoneId).toInstant());
						 Date date2 = Date.from(dpFecha2.getValue().atStartOfDay(defaultZoneId).toInstant());
						 
						 //ChronoLocalDate chronoDate =
						 //    ((isoDate != null) ? datePicker.getChronology().date(isoDate) : null);
						lista=FXCollections.observableList(service.casosEntreFechasComunidad(date1 ,date2, lstComunidades.getSelectionModel().getSelectedItem() ));
					}
					//Creamos y configuramos la tabla
			        TableColumn<Caso,String> colComunidad = new TableColumn<>("Nombre");
			        colComunidad.setMinWidth(150);
			        //Informamos los nombres de las propiedades del JavaBean "Contacto"
			        //que el programa deberá llamar al hacer un set/get
			        
			        colComunidad.setCellValueFactory(
			                new PropertyValueFactory<Caso, String>("comunidad"));
			        
			        TableColumn<Caso,Date> colFecha = new TableColumn<>("Fecha");
			        colFecha.setMinWidth(200);
			        colFecha.setCellValueFactory(
			                new PropertyValueFactory<Caso, Date>("fecha"));
			        
			        TableColumn<Caso,Integer> colPositivos = new TableColumn<>("Positivos");
			        colPositivos.setMinWidth(100);
			        colPositivos.setCellValueFactory(
			                new PropertyValueFactory<Caso, Integer>("positivos"));
			                
			        //Asignamos los datos
			        table.setItems(lista);
			        //Añadimos las columnas
			        //table.getColumns().addAll(firstNameCol, ageCol, emailCol);
			        table.getColumns().addAll(colComunidad,colFecha,colPositivos);
				}
		    	
		    }
			);
		    
		    grdPanelGestion.add(lbSelectCom, 0, 0, 2, 1);
		    grdPanelGestion.add(btnTodasFechas, 1, 1);
		    grdPanelGestion.add(btnEntreFechas, 1, 2);
		    grdPanelGestion.add(lstComunidades, 0, 1, 1, 3);
		    grdPanelGestion.add(dpFecha1, 2, 2);			
		    grdPanelGestion.add(dpFecha2, 3, 2);
		    grdPanelGestion.add(btnVerCasos, 1, 3);
		    grdPanelGestion.add(table, 0, 5, 4, 1);
			//panel de gestión ----    ----    ----    ----    ----
			
			
			
			//----    ----    ----    --------    --------    ----
	   
			//----    ----    ----    --------    --------    ----
			//MENUS ----    ----    ----    ----    ----
			//cabecera de menu 1
			Menu m = new Menu("Menu"); 	  
	        //elementos de menu 
	        MenuItem m1 = new MenuItem("Consultas"); 
	        //MenuItem m2 = new MenuItem("Estadísticas"); 
	        m1.setOnAction( new EventHandler<ActionEvent>() {				
				@Override
				public void handle(ActionEvent event) {				
					grdPanelGestion.setVisible(true);
				}
	        });	               
	        m.getItems().add(m1);  
	        
	        //cabecera de menu 2
	        Menu n = new Menu("Estadísticas");
	        //elementos de menu 
	        MenuItem m2 = new MenuItem("Informe"); 
	        n.getItems().add(m2);
			//MENUS ----    ----    ----    ----    ----
			//----    ----    ----    --------    --------    ----
	  
	        //barra principal de menú 
	        MenuBar mb = new MenuBar(); 	  
	        //se añaden los menus 
	        mb.getMenus().addAll(m,n);
	  
	        //se crea caja vertical
	        VBox vb = new VBox();
	        	        	       
			vb.getChildren().add(mb);
	        vb.getChildren().add(grdPanelGestion);
	        
	        grdPanelGestion.setVisible(false);
	        	  
	        // create a scene 
	        Scene sc = new Scene(vb, 700, 900);	     	        	  
	        // set the scene 
	        primaryStage.setScene(sc);	        	  
	        primaryStage.show(); 
	        
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
