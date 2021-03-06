package bph.entities.kewangan;

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
import portal.module.entity.Users;

@Entity
@Table(name = "kew_subsidiari_rpp_migrate")
public class KewSubsidiariRPPMigrate {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "nama_pemohon")
	private String namaPemohon;

	@Column(name = "no_kp")
	private String noKP;

	@Column(name = "tarikh_terima_permohonan")
	private String tarikhTerimaPermohonan;

	@Column(name = "tarikh_tindakan")
	private String tarikhTindakan;

	@Column(name = "tarikh_selesai")
	private String tarikhSelesai;

	@Column(name = "id_penyedia")
	private String idPenyedia;

	@Column(name = "jumlah_bayaran")
	private String jumlahBayaran;

	@Column(name = "no_resit")
	private String noResit;

	@Column(name = "no_eft")
	private String noEft;

	@Column(name = "tarikh_eft")
	private String tarikhEft;

	@Column(name = "no_baucer")
	private String noBaucer;

	@Column(name = "tarikh_baucer")
	private String tarikhBaucer;

	@Column(name = "flag_migrate")
	private String flagMigrate;

	@Column(name = "msg")
	private String msg;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public KewSubsidiariRPPMigrate() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNamaPemohon() {
		return namaPemohon;
	}

	public void setNamaPemohon(String namaPemohon) {
		this.namaPemohon = namaPemohon;
	}

	public String getNoKP() {
		return noKP;
	}

	public void setNoKP(String noKP) {
		this.noKP = noKP;
	}

	public String getTarikhTerimaPermohonan() {
		return tarikhTerimaPermohonan;
	}

	public void setTarikhTerimaPermohonan(String tarikhTerimaPermohonan) {
		this.tarikhTerimaPermohonan = tarikhTerimaPermohonan;
	}

	public String getTarikhTindakan() {
		return tarikhTindakan;
	}

	public void setTarikhTindakan(String tarikhTindakan) {
		this.tarikhTindakan = tarikhTindakan;
	}

	public String getTarikhSelesai() {
		return tarikhSelesai;
	}

	public void setTarikhSelesai(String tarikhSelesai) {
		this.tarikhSelesai = tarikhSelesai;
	}

	public String getIdPenyedia() {
		return idPenyedia;
	}

	public void setIdPenyedia(String idPenyedia) {
		this.idPenyedia = idPenyedia;
	}

	public String getJumlahBayaran() {
		return jumlahBayaran;
	}

	public void setJumlahBayaran(String jumlahBayaran) {
		this.jumlahBayaran = jumlahBayaran;
	}

	public String getNoResit() {
		return noResit;
	}

	public void setNoResit(String noResit) {
		this.noResit = noResit;
	}

	public String getNoEft() {
		return noEft;
	}

	public void setNoEft(String noEft) {
		this.noEft = noEft;
	}

	public String getTarikhEft() {
		return tarikhEft;
	}

	public void setTarikhEft(String tarikhEft) {
		this.tarikhEft = tarikhEft;
	}

	public String getNoBaucer() {
		return noBaucer;
	}

	public void setNoBaucer(String noBaucer) {
		this.noBaucer = noBaucer;
	}

	public String getTarikhBaucer() {
		return tarikhBaucer;
	}

	public void setTarikhBaucer(String tarikhBaucer) {
		this.tarikhBaucer = tarikhBaucer;
	}

	public String getFlagMigrate() {
		return flagMigrate;
	}

	public void setFlagMigrate(String flagMigrate) {
		this.flagMigrate = flagMigrate;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}
}
