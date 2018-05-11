package node;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.collections.ObservableList;

public class DosyaIslem {

	private static Sehir[] sehir = new Sehir[81];
	public static int[][] komsuluk = new int[81][];
	
	public static int getKomsuluk(int i, int j) {return komsuluk[i-1][j-1];}
	public static Sehir plakadanSehirBul(int plaka) {return sehir[plaka-1];}
	
	//ilk constructorla oluþturduðumuz sehirleri maine atýcaðýz
	public static void sehirler() {	
		try {
			FileReader fr = new FileReader("file:..\\..\\ek\\lat-long.txt");
			BufferedReader br = new BufferedReader(fr);
			
			String satir;
			//ilk satirý es geç
			br.readLine();
			//Dosyanýn sonuna kadar oku ve sehir objelerini oluþtur
			for(int i=0; (satir = br.readLine()) != null; i++) {
				String[] satirEleman = satir.split(",");
				sehir[i] = new Sehir(Double.parseDouble(satirEleman[0]), Double.parseDouble(satirEleman[1]), Integer.parseInt(satirEleman[2]), Integer.parseInt(satirEleman[3]));
			}
		} catch(IOException io) {
			System.out.println("lat-long.txt okunurken hata");
		}
	}
	
	//sehir adlarýný ekle
	public static void sehirAdi(ObservableList<String> city) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("file:..\\..\\ek\\Sehir.txt"), "UTF-8") );
			
			String satir;
			while((satir = br.readLine()) != null) {
				String[] satirEleman = satir.split(" ");
				int plaka = Integer.parseInt(satirEleman[0]);
				sehir[plaka-1].setIsim(satirEleman[1]);
				city.add(satirEleman[1]);
			}
		} catch(IOException io) {
			System.out.println("Sehir.txt okunurken hata");
		}
	}
	
	public static void sehirKomsuluk() {
		FileReader fr;
		try {
			fr = new FileReader("file:..\\..\\ek\\komsuluklar.txt");
			BufferedReader br = new BufferedReader(fr);
			String satir;
			//ilk satirý atla
			br.readLine();
			//Komsuluk matrisi olusturma
			while((satir=br.readLine()) != null) {
				String[] satirEleman = satir.split(",");
				int sehir_index = Integer.parseInt(satirEleman[0]);
				komsuluk[sehir_index-1] = new int[satirEleman.length-1];
				for(int i=1; i<satirEleman.length; i++) {
					int eleman = Integer.parseInt(satirEleman[i]);
					komsuluk[sehir_index-1][i-1] = eleman;
				}
			}
			
		} catch (IOException e) {
			System.out.println("komsuluklar.txt okunurken hata");
		}
		
	}
	
	public static void maksimumKarYaz(Sehir kalkis, Sehir varis, int yolcuSayisi) {
		YolHesap y1 = new YolHesap();
		y1.minMaliyet(kalkis, varis, yolcuSayisi);
		File maksimum = new File("file:..\\..\\ek\\maksimumKar.txt");
		try {
			if (!maksimum.exists()) maksimum.createNewFile();
				FileWriter fileWriter = new FileWriter(maksimum, false);
		        BufferedWriter bWriter = new BufferedWriter(fileWriter);
		        for(int i=0; i<YolHesap.cizimYolu.length-1; i++) {
		        	bWriter.write(plakadanSehirBul(YolHesap.cizimYolu[i]).getIsim() +"\n");
		        	bWriter.write("Mesafe: " + plakadanSehirBul(YolHesap.cizimYolu[i]).mesafeHesap(plakadanSehirBul(YolHesap.cizimYolu[i+1])));
		        	bWriter.write("\n" +plakadanSehirBul(YolHesap.cizimYolu[i+1]).getIsim()+"\n");
		        }
		        bWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void elliKarYaz(Sehir kalkis, Sehir varis) {
		YolHesap y1 = new YolHesap();
		
		File maksimum = new File("file:..\\..\\ek\\elliKar.txt");
		try {
			if (!maksimum.exists()) maksimum.createNewFile();
				FileWriter fileWriter = new FileWriter(maksimum, false);
		        BufferedWriter bWriter = new BufferedWriter(fileWriter);
		        for(int yolcu=10; yolcu<=50; yolcu+=10) {
		        	y1.minMaliyet(kalkis, varis, yolcu);
		        	bWriter.write(yolcu + " kiþi için\n");
		        	for(int i=0; i<YolHesap.cizimYolu.length-1; i++) {
			        	bWriter.write(plakadanSehirBul(YolHesap.cizimYolu[i]).getIsim() +"\n");
			        	bWriter.write("Mesafe: " + plakadanSehirBul(YolHesap.cizimYolu[i]).mesafeHesap(plakadanSehirBul(YolHesap.cizimYolu[i+1])));
			        	bWriter.write("\n" +plakadanSehirBul(YolHesap.cizimYolu[i+1]).getIsim()+"\n");
			        }
		        }
		        bWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	//sehirleri harita resminde göstermek için koordinatlar
	public static void sehirResim() {
		FileReader fr;
		try {
			fr = new FileReader("file:..\\..\\ek\\koordinat.txt");
			BufferedReader br = new BufferedReader(fr);
			String satir;
			int i=1;
			while((satir=br.readLine()) != null) {
				String[] satirEleman = satir.split(",");
				double x = Double.parseDouble(satirEleman[0]);
				double y = Double.parseDouble(satirEleman[1]);
				plakadanSehirBul(i).image_x=x;
				plakadanSehirBul(i).image_y=y;
				i++;
			}
			
		} catch (IOException e) {
			System.out.println("koordinatlar.txt okunurken hata");
		}
	}

}
