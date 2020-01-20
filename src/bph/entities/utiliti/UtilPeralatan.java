package bph.entities.utiliti;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "util_peralatan")
public class UtilPeralatan {

	@Id
	@Column(name = "id")
	private String id;
	
	@JoinColumn(name = "id_dewan")
	private UtilDewan dewan;
//	@Column(name = "id_dewan")
//	private String idDewan;
	
	@Column(name = "nama")
	private String nama;
	
	@Column(name = "kuantiti")
	private String kuantiti;
	
	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "kadar_sewa")
	private double kadarSewa;

	public UtilPeralatan() {
		setId(UID.getUID());
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNama() {
		return nama;
	}

	public String getKuantiti() {
		return kuantiti;
	}

	public void setKuantiti(String kuantiti) {
		this.kuantiti = kuantiti;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setKadarSewa(double kadarSewa) {
		this.kadarSewa = kadarSewa;
	}

	public double getKadarSewa() {
		return kadarSewa;
	}

	public void setDewan(UtilDewan dewan) {
		this.dewan = dewan;
	}

	public UtilDewan getDewan() {
		return dewan;
	}
	
}



