package node;

import java.util.ArrayList;

public class YolHesap {
	//gidilen sehirler
	public double maliyetD[] = new double[81];
	public static int cizimYolu[];

	/*
	 * gidimeyeceði yollarý false yapmak için ikinci bir komsuluk matrisi olusturuyoruz
	 * public boolean komsuluk[][] = DosyaIslem.komsuluk;
	 */
	
	public double minMaliyet(Sehir kalkis, Sehir varis, int yolcu) {
		//999999 = sonsuzluk
		for(int i=0; i<81; i++)	maliyetD[i] = 999999;
		maliyetD[kalkis.getPlaka()-1] = 0;
		
		//cýkan yollarý bul
		ArrayList<Sehir> sira = new ArrayList<Sehir>();
		sira.clear();
		int siraIndex = 0;
		System.out.println("Kalkis sehri " + kalkis.getIsim());

		for(int i=0; i<DosyaIslem.komsuluk[kalkis.getPlaka()-1].length; i++) {
			Sehir araSehir = DosyaIslem.plakadanSehirBul(DosyaIslem.komsuluk[kalkis.getPlaka()-1][i]);
			double maliyet = kalkis.maliyetHesap(araSehir, 0);
			if(kalkis.aciHesap(araSehir, 0) <= 80-yolcu && maliyet+maliyetD[kalkis.getPlaka()-1] < maliyetD[araSehir.getPlaka()-1]) {
				//System.out.println(araSehir.getIsim() + " Maliyet: " + maliyet);
				//geldiði yol
				araSehir.gidilenYol = new int[1];
				araSehir.gidilenYol[0] = kalkis.getPlaka();
				
				maliyetD[araSehir.getPlaka()-1] = maliyet;
				//kalkis ile varis komsu sehir olma durumu
				if(araSehir == varis) {
					System.out.println("Son Maliyet: " + maliyetD[varis.getPlaka()-1]);
					return maliyetD[varis.getPlaka()-1];
				}
				sira.add(araSehir);
			}
		}
		
		for(siraIndex=0; siraIndex<sira.size(); siraIndex++) {
			Sehir araSehir = sira.get(siraIndex);
			double maliyet;
			for(int i =0; i<DosyaIslem.komsuluk[araSehir.getPlaka()-1].length; i++) {
				Sehir sonrakiSehir = DosyaIslem.plakadanSehirBul(DosyaIslem.komsuluk[araSehir.getPlaka()-1][i]);
				if(sonrakiSehir == varis) maliyet = araSehir.maliyetHesap(sonrakiSehir, 2);
				else					  maliyet = araSehir.maliyetHesap(sonrakiSehir, 1);
				if(araSehir.aciHesap(sonrakiSehir, 0) <= 80-yolcu && maliyet+maliyetD[araSehir.getPlaka()-1] < maliyetD[sonrakiSehir.getPlaka()-1] && maliyet+maliyetD[araSehir.getPlaka()-1] < maliyetD[varis.getPlaka()-1]) {
					System.out.println(sonrakiSehir.getIsim() + " Maliyet: " + (maliyet+maliyetD[araSehir.getPlaka()-1]));
					sonrakiSehir.gidilenYol = new int[araSehir.gidilenYol.length+1];
					//geldiði yol
					for(int j=0; j<araSehir.gidilenYol.length; j++) sonrakiSehir.gidilenYol[j] = araSehir.gidilenYol[j];
					sonrakiSehir.gidilenYol[araSehir.gidilenYol.length] = araSehir.getPlaka();
					
					maliyetD[sonrakiSehir.getPlaka()-1] = maliyet+maliyetD[araSehir.getPlaka()-1];
					//varýþa geldiyse
					if(sonrakiSehir== varis) continue;
					sira.add(sonrakiSehir);
				}
			}
		}
		
		if(maliyetD[varis.getPlaka()-1] == 999999) {
			System.out.println("Sehre gidilemiyor");
			return 999999;
		}
		System.out.println("Son Maliyet: " + maliyetD[varis.getPlaka()-1]);
		System.out.println("Geldiði yol");
		for(int i=0; i<varis.gidilenYol.length; i++)	System.out.print(varis.gidilenYol[i]+ ", ");
		System.out.println(varis.getPlaka());
		
		cizimYolu = new int[varis.gidilenYol.length+1];
		for(int i=0; i<varis.gidilenYol.length; i++)	cizimYolu[i] = varis.gidilenYol[i];
		cizimYolu[cizimYolu.length-1] = varis.getPlaka();
		
		return maliyetD[varis.getPlaka()-1];
	}
	
	public void maksimumKar(Sehir kalkis, Sehir varis, double masraf, double bilet) {
		int yolcuSayisi = 5;
		int kar_yolcuSayisi=5;
		double min = (bilet*yolcuSayisi) - (minMaliyet(kalkis, varis, yolcuSayisi)*masraf);
		if(min == 999999) {
			System.out.println("Sehre ulaþýlamýyor");
			return;
		}// yolcu sayisi x20 max kar ?
		for(yolcuSayisi=6; yolcuSayisi<=50; yolcuSayisi++) {
			System.out.println(yolcuSayisi);
			double temp = (bilet*yolcuSayisi) - (minMaliyet(kalkis, varis, yolcuSayisi)*masraf);
			if( min >  temp) {
				min = temp;
				kar_yolcuSayisi = yolcuSayisi;
			}
		}
		System.out.println("\nMaksimum kar: " + min);
		System.out.println("Maksimum kar için yolcu sayisi: " + kar_yolcuSayisi);
		DosyaIslem.maksimumKarYaz(kalkis, varis, kar_yolcuSayisi);
	}
	
	public void elliKar(Sehir kalkis, Sehir varis, double masraf) {
		double fiyat[] = new double[5];
		int j=0;
		for(int i=10; i<=50; i+=10) {
			fiyat[j] = masraf* minMaliyet(kalkis, varis, i)/i;
					j++;
		}
		j=0;
		for(int i=10; i<=50; i+=10) {
			System.out.println(i + " kiþi için " + fiyat[j] + " alýnmalý");
					j++;
		}
		DosyaIslem.elliKarYaz(kalkis, varis);
	}
}
