package node;

public class Sehir {

	private String isim; 
	private int plaka;
	private double lat, log, rakim;
	public int gidilenYol[];
	
	public double image_x, image_y;
	
	//ilk olarak lat-long.txt okunacak
	public Sehir(double lat, double log, int plaka, int rakim) {
		this.plaka = plaka;
		this.lat = lat;
		this.log = log;
		this.rakim = rakim;
	}

	public void setIsim(String isim) {
		this.isim = isim;
	}
	
	public String getIsim() {
		return isim;
	}

	public int getPlaka() {
		return plaka;
	}

	public double getLat() {
		return lat;
	}

	public double getLog() {
		return log;
	}

	public double getRakim() {
		return rakim;
	}
	
	//diğer fonksiyonlar
	public double mesafeHesap(Sehir iki) {
		double a, c, latFark, logFark;
		double R = 6371;
		latFark = Math.toRadians(iki.lat-this.lat);
		logFark = Math.toRadians(iki.log-this.log);
		
		a = Math.pow(Math.sin(latFark/2), 2)
			+ Math.pow(Math.sin(logFark/2), 2) * Math.cos(Math.toRadians(this.lat)) * Math.cos(Math.toRadians(iki.lat));
		c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return R * c;
	}
	
	/* kalkıs	a-(b+50)
	 * ara adım	(a+50) - (b+50) ->> a -b
	 * varıs	(a+50)-b
	 */
	public double rakimHesap(Sehir iki, int durum) {
		if(durum == 0)
			return this.rakim - iki.rakim +50;
		else if(durum == 1) 
			return this.rakim - iki.rakim;
		//else
		return this.rakim - iki.rakim -50;
	}

	public double maliyetHesap(Sehir iki, int durum) {
		double mesafe = this.mesafeHesap(iki);
		double yukseklik = this.rakimHesap(iki, durum)/1000;//km
		//System.out.println(yol + "      +" + Math.pow(yukseklik, 2));
		return Math.sqrt( Math.pow(mesafe, 2) + Math.pow(yukseklik, 2) );
	}
	
	public double aciHesap(Sehir iki, int durum) {
		double mesafe = this.mesafeHesap(iki);
		double rakim = this.rakimHesap(iki, durum);
		return Math.toDegrees(Math.atan(rakim/mesafe));
	}
	
	/* public boolean komsulukKontrol(Sehir iki) {
		if(DosyaIslem.getKomsuluk(this.plaka, iki.plaka) == true)	return true;
		//else
		return false;
	} */
	
}
