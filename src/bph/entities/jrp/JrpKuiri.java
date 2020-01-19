package bph.entities.jrp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;

@Entity
@Table(name = "jrp_kuiri")
public class JrpKuiri {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private JrpPermohonan permohonan;
	
	@Column(name = "ulasan")
	private String ulasan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_ulasan")
	private Date tarikhUlasan;
	
	@Column(name = "flag_hantar")
	private String flagHantar;
	
	@Column(name = "maklumbalas")
	private String maklumbalas;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_maklumbalas")
	private Date tarikhMaklumbalas;
	
	@Column(name = "flag_hantar_maklumbalas")
	private String flagHantarMaklumbalas;
	
	@Column(name = "flag_buka_ulasan")
	private String flagBukaUlasan;
	
	@Column(name = "flag_buka_maklumbalas")
	private String flagBukaMaklumbalas;
	
	@Column(name = "flag_internal")
	private String flagInternal;
	
	public JrpKuiri() {
		setId(UID.getUID());
	}

	public String getFlagInternal() {
		return flagInternal;
	}

	public void setFlagInternal(String flagInternal) {
		this.flagInternal = flagInternal;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JrpPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(JrpPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public String getUlasan() {
		return ulasan;
	}

	public void setUlasan(String ulasan) {
		this.ulasan = ulasan;
	}

	public Date getTarikhUlasan() {
		return tarikhUlasan;
	}

	public void setTarikhUlasan(Date tarikhUlasan) {
		this.tarikhUlasan = tarikhUlasan;
	}

	public String getFlagHantar() {
		return flagHantar;
	}

	public void setFlagHantar(String flagHantar) {
		this.flagHantar = flagHantar;
	}

	public String getMaklumbalas() {
		return maklumbalas;
	}

	public void setMaklumbalas(String maklumbalas) {
		this.maklumbalas = maklumbalas;
	}

	public Date getTarikhMaklumbalas() {
		return tarikhMaklumbalas;
	}

	public void setTarikhMaklumbalas(Date tarikhMaklumbalas) {
		this.tarikhMaklumbalas = tarikhMaklumbalas;
	}

	public String getFlagHantarMaklumbalas() {
		return flagHantarMaklumbalas;
	}

	public void setFlagHantarMaklumbalas(String flagHantarMaklumbalas) {
		this.flagHantarMaklumbalas = flagHantarMaklumbalas;
	}

	public String getFlagBukaUlasan() {
		return flagBukaUlasan;
	}

	public void setFlagBukaUlasan(String flagBukaUlasan) {
		this.flagBukaUlasan = flagBukaUlasan;
	}

	public String getFlagBukaMaklumbalas() {
		return flagBukaMaklumbalas;
	}

	public void setFlagBukaMaklumbalas(String flagBukaMaklumbalas) {
		this.flagBukaMaklumbalas = flagBukaMaklumbalas;
	}

}