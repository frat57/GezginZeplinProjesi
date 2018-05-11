package gui;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import node.DosyaIslem;
import node.Sehir;
import node.YolHesap;

public class MainObject extends Application{

	private ObservableList<String> options = FXCollections.observableArrayList("Maksimum Kâr", "%50 kâr");
	private static ObservableList<String> city = FXCollections.observableArrayList();
	final private Image mapImage = new Image("file:..\\..\\ek\\Harita.png");
	
	public static void main(String[] args) {
		System.out.println("Basladý");
		DosyaIslem.sehirler();
		System.err.println("minHesap debug layer");
		
		DosyaIslem.sehirAdi(city);
		DosyaIslem.sehirKomsuluk();
		
		YolHesap y1 = new YolHesap();
		double zeplinMasraf=100;//1000km de
		double biletFiyat = 20;
		y1.minMaliyet(DosyaIslem.plakadanSehirBul(34), DosyaIslem.plakadanSehirBul(73), 5);
		//y1.maksimumKar(DosyaIslem.plakadanSehirBul(34), DosyaIslem.plakadanSehirBul(6), zeplinMasraf, biletFiyat);
		//y1.elliKar(DosyaIslem.plakadanSehirBul(34), DosyaIslem.plakadanSehirBul(6), zeplinMasraf);
		launch(args);	
	}

	@Override
	public void start(Stage stage) throws Exception {
		double zeplinMasraf=10;//100km de 1000 se kilometrede 1 masraf olur km/lira
		double biletFiyat = 20;
		//Read from file
		DosyaIslem.sehirler();
		DosyaIslem.sehirAdi(city);
		DosyaIslem.sehirKomsuluk();
		DosyaIslem.sehirResim();
		//main frame
		HBox mainLayout = new HBox();//left and right frames will go in there
		//left frame
		HBox leftFrame = new HBox();
		ImageView mapView = new ImageView();
		mapView.setImage(mapImage);
		leftFrame.getChildren().add(mapView);
		//right frame
		VBox rightFrame = new VBox();
		ComboBox optionBox = new ComboBox(options);
		
		//bottom layer has two options
		GridPane bottomFrame = new GridPane();
		optionBox.getSelectionModel().selectedItemProperty().addListener(
		(ObservableValue observable, Object oldValue, Object newValue) -> {
			//First clear right side of the window
			bottomFrame.getChildren().clear();
			
			bottomFrame.setVgap(10);
			bottomFrame.setHgap(40);
			ComboBox primCity = new ComboBox(city);
			ComboBox destCity = new ComboBox(city);
			Button opt1Button = new Button("Gönder");
			bottomFrame.add(new Text("Kalkýþ Noktasý"), 0, 0);
			bottomFrame.add(primCity, 0, 1);
			bottomFrame.add(new Text("Varýþ Noktasý"), 1, 0);
			bottomFrame.add(destCity, 1, 1);
			String number[] = new String[46];
			for(int i=5, j=0;i<51;i++, j++) {number[j] = Integer.toString(i);}
			
			if(newValue.equals(options.get(0))) {
				bottomFrame.add(opt1Button, 0, 3);
				bottomFrame.add(new Text("Sabit Bilet Fiyatý: " + biletFiyat), 0, 4);
				
				opt1Button.setOnAction(event -> {
					if( primCity.getSelectionModel().getSelectedItem() == null ||
						destCity.getSelectionModel().getSelectedItem() == null ) {
						mesajPenceresi("kalkýþ ve varýþ sehirleri secmeyi unutmayýn");
					}
					else if( primCity.getSelectionModel().getSelectedItem() == destCity.getSelectionModel().getSelectedItem() ) {
						mesajPenceresi("kalkýþ ve varýþ noktalarý ayný olamaz");
					}
					else {	
						bottomFrame.add(new Text("Kâr için yolcu sayýsý: "), 0, 5);
						Sehir ist = DosyaIslem.plakadanSehirBul(primCity.getSelectionModel().getSelectedIndex()+1);
						Sehir koc = DosyaIslem.plakadanSehirBul(destCity.getSelectionModel().getSelectedIndex()+1);
						System.out.println("ilk sehrimiz " + ist.getIsim() + "\nikinci sehrimiz "  +koc.getIsim() + "\nAralarýndaki yatay mesafe "
								+ ist.mesafeHesap(koc) + "\nRakým farký " + (ist.getRakim()-koc.getRakim()) + "\nRakým hesabý(ara adým) " +
								ist.rakimHesap(koc, 1) + "\nMaliyeti " + ist.maliyetHesap(koc, 2)
								+ "\naradaki min gereken açý " + Math.toDegrees(Math.atan((Math.abs(koc.getRakim()-ist.getRakim())+50)/ ist.mesafeHesap(koc))) );
						System.out.println("prim city komsularý" );
						for(int i=0; i<DosyaIslem.komsuluk[primCity.getSelectionModel().getSelectedIndex()+1].length; i++) {
							//System.out.println(DosyaIslem.komsuluk[primCity.getSelectionModel().getSelectedIndex()+1][i]);
						}
						//cizimPenceresi(YolHesap.cizimYolu);
						YolHesap y1 = new YolHesap();
						y1.maksimumKar(DosyaIslem.plakadanSehirBul(primCity.getSelectionModel().getSelectedIndex()+1), DosyaIslem.plakadanSehirBul(destCity.getSelectionModel().getSelectedIndex()+1), zeplinMasraf, biletFiyat);
					}
						
				});
			}
			else {
				bottomFrame.add(new Text("Yolcu sayýsý:"), 0, 3);
				bottomFrame.add(opt1Button, 0, 4);
				opt1Button.setOnAction(event -> {
					
					if( primCity.getSelectionModel().getSelectedItem() == null ||
						destCity.getSelectionModel().getSelectedItem() == null ) {
						mesajPenceresi("kalkýþ ve varýþ sehirleri secmeyi unutmayýn");
					}
					else if( primCity.getSelectionModel().getSelectedItem() == destCity.getSelectionModel().getSelectedItem() ) {
						mesajPenceresi("kalkýþ ve varýþ noktalarý ayný olamaz");
					}
					else {	
						bottomFrame.add(new Text("Kâr için yolcu sayýsý: "), 0, 5);
						//cizimPenceresi(YolHesap.cizimYolu);
					}
				});
			}
		});
		
		rightFrame.getChildren().addAll(optionBox, bottomFrame);
		mainLayout.getChildren().addAll(leftFrame, rightFrame);
		
		//Scene 
		Scene scene = new Scene(mainLayout, 800, 300);
		stage.setScene(scene);
		stage.setTitle("Zeplin");
		stage.setResizable(false);
		stage.show();
	}
	
	public void mesajPenceresi(String msg) {
		Stage window = new Stage();
		VBox layout = new VBox();
		Label text = new Label(msg);
		
		layout.getChildren().add(text);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout, 300, 50);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setScene(scene);
		window.setTitle("Hata");
		window.show();
	}
	
	public void cizimPenceresi(int[] yol) {
		Stage window = new Stage();
		Group layout = new Group();
		AnchorPane anchor = new AnchorPane();
		Image image = new Image("file:..\\..\\ek\\harita-buyuk.png");
		ImageView mapView = new ImageView();
		mapView.setImage(image);
		anchor.getChildren().add(mapView);
		int window_w = 1000;
		int window_h = 450;
		
		Line line = new Line(0, 0, 0, 0);
		line.setStrokeWidth(2);
		line.setFill(null);
		line.setStroke(Color.BLACK);
		Circle circle = new Circle(0, 0, 0);
		circle.setStroke(Color.BLACK);
		//circle.setFill(null);
		circle.setStrokeWidth(2);
		int genislik = 9;
		for(int i=0; i<yol.length-1; i++) {
			Sehir s = DosyaIslem.plakadanSehirBul(yol[i]);
			Sehir ite = DosyaIslem.plakadanSehirBul(yol[i+1]);
			
			circle = new Circle(s.image_x, s.image_y, genislik);
			line= new Line(s.image_x, s.image_y, ite.image_x, ite.image_y);
			anchor.getChildren().addAll(circle, line);
		}
		Sehir s = DosyaIslem.plakadanSehirBul(yol[yol.length-1]);	
		circle = new Circle(s.image_x, s.image_y, genislik);
		
		Scene scene = new Scene(anchor, window_w, window_h);
		window.setScene(scene);
		window.setTitle("Gidilen Yol");
		window.show();
	}
}
