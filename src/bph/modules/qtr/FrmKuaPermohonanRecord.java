package bph.modules.qtr;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.ws.BindingProvider;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import portal.module.entity.UsersActivity;
import portal.module.entity.UsersJob;
import portal.module.entity.UsersSpouse;
import sbbph.ws.BPHServices;
import sbbph.ws.BPHServicesImplService;
import bph.entities.kod.Agensi;
import bph.entities.kod.BadanBerkanun;
import bph.entities.kod.Bandar;
import bph.entities.kod.Bangsa;
import bph.entities.kod.Etnik;
import bph.entities.kod.Gelaran;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.Jantina;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JawatanAPMM;
import bph.entities.kod.JawatanATM;
import bph.entities.kod.JawatanPDRM;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.JenisPerkhidmatan;
import bph.entities.kod.KelasKuarters;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Negeri;
import bph.entities.kod.Status;
import bph.entities.kod.StatusPekerjaan;
import bph.entities.kod.StatusPerkahwinan;
import bph.entities.kod.StatusPerkhidmatan;
import bph.entities.portal.Giliran;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaCatatanPermohonan;
import bph.entities.qtr.KuaKelainanUpaya;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.qtr.KuaPinjamanPemohon;
import bph.entities.qtr.KuaSequencePermohonan;
import bph.entities.qtr.VW_KuaAgihan;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class FrmKuaPermohonanRecord extends
		LebahRecordTemplateModule<KuaPermohonan> {

	private static final long serialVersionUID = 1816637883473727892L;
	static Logger myLogger = Logger
			.getLogger("bph/modules/qtr/FrmKuaPermohonanRecord");
	private DataUtil dataUtil;
	private KuartersUtils kuaUtil = new KuartersUtils();
	private String uploadDir = ResourceBundle.getBundle("dbconnection")
			.getString("folder");
	private MyPersistence mp;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<KuaPermohonan> getPersistenceClass() {
		return KuaPermohonan.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/permohonan";
	}

	@Override
	public void begin() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		context.put("userId", userId);
		dataUtil = DataUtil.getInstance(db);
		setHideDeleteButton(true);
		setDisableKosongkanUpperButton(true);
		setDisableDefaultButton(true);
		setDisableSaveAddNewButton(true);
		setDisableAddNewRecordButton(true);
		setDisableBackButton(true);
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);

		String[] role = userRole.split(" ");

		for (int i = 0; i < role.length; i++) {
			if ("Penyedia".equals(role[i])) {
				context.put("currentRoleQTR", role[i]);
			} else if ("Penyemak".equals(role[i])) {
				context.put("currentRoleQTR", role[i]);
			} else if ("Pelulus".equals(role[i])) {
				context.put("currentRoleQTR", role[i]);
			}
		}

		addFilter("status.id IN ('1419483289678')");
		setOrderBy("tarikhPermohonan DESC");
		context.put("newRecord", "false");
		context.put("findLokasiPermohonan", dataUtil.getListLokasiPermohonan());
		context.put("findStatus", dataUtil.getListStatusKuarters());
		context.put("selectNegeriPekerjaan", dataUtil.getListNegeri());
		context.put("selectNegeriPinjaman", dataUtil.getListNegeri());
		context.put("selectLokasiPermohonan",
				dataUtil.getListLokasiPermohonan());
		context.put("overall", "tidak");
		context.put("path", getPath());
	}

	@Override
	public void save(KuaPermohonan r) throws Exception {
		// r.setStatus(mp.find(Status.class, "1419601227590"));
		// r.setTarikhKemaskini(new Date());
	}

	@Override
	public boolean delete(KuaPermohonan r) throws Exception {
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("noPermohonan", getParam("findNoPermohonan"));
		r.put("pemohon.userName", getParam("findNamaPemohon"));
		r.put("pemohon.noKP", getParam("findNoKPPemohon"));
		r.put("lokasi.id", getParam("findLokasiPermohonan"));
		r.put("datePermohonan", new OperatorDateBetween(
				getDate("findTarikhPermohonan"),
				getDate("findTarikhPermohonan")));
		return r;
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void afterSave(KuaPermohonan r) {

	}

	@Override
	public void getRelatedData(KuaPermohonan r) {
		boolean kelulusan = false;
		userRole = (String) request.getSession().getAttribute("_portal_role");
		try {
			mp = new MyPersistence();
			if ("(QTR) Penyedia".equalsIgnoreCase(userRole)
					|| "(QTR) Pelulus".equalsIgnoreCase(userRole)) {
				context.put("qtrstaff", "true");
			} else {
				context.put("qtrstaff", "false");
			}
			UsersJob uj = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ r.getPemohon().getId() + "'");
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					r.getId());
			context.put("r", kp);

			if ("true".equals(kp.getKelulusan1())
					&& "true".equals(kp.getKelulusan3())) {
				context.put("disableAddNewRecordButton", true);
				kelulusan = true;
			}

			context.put("kelulusan", kelulusan);

			context.remove("kpp");
			KuaPinjamanPemohon kpp = (KuaPinjamanPemohon) mp
					.get("SELECT kpp FROM KuaPinjamanPemohon kpp WHERE kpp.users.id = '"
							+ r.getPemohon().getId() + "'");

			if (kpp != null) {
				context.put("kpp",
						mp.find(KuaPinjamanPemohon.class, kpp.getId()));
			}

			context.put("resultPermohonanCompletion",
					getCompletionPermohonan(mp, r.getId()));

			context.put("uj", uj);

		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
	}

	@Command("tarikhJangkaSiap")
	public String tarikhJangkaSiap() {
		return getPath() + "/sub_page/pinjaman/tarikhJangkaSiap.vm";
	}

	@Command("getPermohonan")
	public String getPermohonan() {
		String idPermohonan = getParam("idPermohonan");
		boolean kelulusan = false;
		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					idPermohonan);
			if ("true".equals(kp.getKelulusan1())
					&& "true".equals(kp.getKelulusan3())) {
				context.put("disableAddNewRecordButton", true);
				kelulusan = true;
			}
			context.put("kelulusan", kelulusan);
			context.put("r", kp);
			context.put(
					"kpp",
					mp.get("SELECT pp FROM KuaPinjamanPemohon pp WHERE pp.users.id = '"
							+ kp.getPemohon().getId() + "'"));
		} catch (Exception e) {
			System.out.println("Error getPermohonan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/entry_sub/entry_permohonan.vm";
	}

	// TODO
	@SuppressWarnings("unchecked")
	@Command("hantarSemakan")
	// butang hantar ke senarai menunggu
	public String hantarSemakan() {
		boolean success = false;
		boolean wujudKelasDowngrade = false;
		String idPermohonan = getParam("idPermohonan");
		String statusPermohonanAsal = "";
		KelasKuarters k1 = null;
		KelasKuarters k2 = null;
		KuaAgihan agihan = null;
		KuaPermohonan r = null;
		UsersJob uj = null;
		LokasiPermohonan lp = null;
		try {
			mp = new MyPersistence();
			mp.begin();
			r = (KuaPermohonan) mp.find(KuaPermohonan.class, idPermohonan);
			uj = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ r.getPemohon().getId() + "'");
			agihan = (KuaAgihan) mp
					.get("SELECT a FROM KuaAgihan a WHERE a.permohonan.id = '"
							+ idPermohonan + "'");

			// 17072018 - ADD BY PEJE - TO REMOVE CORRUPT DATA AGIHAN WHERE
			// KELAS KUARTERS IS NULL
			if (agihan != null) {
				if (agihan.getKelasKuarters() == null) {
					mp.remove(agihan);
					agihan = null;
				}
			}

			lp = (LokasiPermohonan) mp.find(LokasiPermohonan.class, r
					.getLokasi().getId());

			if (uj != null) {
				String gredKelasKuarters = "";
				String idPerkhidmatan = "";
				String gredJawatan = "";
				if (uj.getGredJawatan() != null) {
					gredKelasKuarters = uj.getGredJawatan().getKelasKuarters();

					/* UNTUK KES BADAN BERUNIFORM */
					idPerkhidmatan = uj.getKelasPerkhidmatan().getId();
					gredJawatan = uj.getGredJawatan().getKeterangan();
					if (idPerkhidmatan.equalsIgnoreCase("XA")
							|| idPerkhidmatan.equalsIgnoreCase("VUXA")
							||
							// idPerkhidmatan.equalsIgnoreCase("T") ||
							// idPerkhidmatan.equalsIgnoreCase("VUT") ||
							idPerkhidmatan.equalsIgnoreCase("YA")
							|| idPerkhidmatan.equalsIgnoreCase("VUYA")
							|| idPerkhidmatan.equalsIgnoreCase("ZA")
							|| idPerkhidmatan.equalsIgnoreCase("VUZ")) {
						String gredUniform = idPerkhidmatan + gredJawatan;
						if (idPerkhidmatan.equalsIgnoreCase("XA")
								|| idPerkhidmatan.equalsIgnoreCase("VUXA")) {
							JawatanAPMM jawatan = (JawatanAPMM) mp
									.get("SELECT j FROM JawatanAPMM j WHERE j.id = '"
											+ gredUniform + "'");
							if (jawatan != null) {
								gredKelasKuarters = jawatan.getKelasLayak();
							}
						} else if (idPerkhidmatan.equalsIgnoreCase("YA")
								|| idPerkhidmatan.equalsIgnoreCase("VUYA")) {
							JawatanPDRM jawatan = (JawatanPDRM) mp
									.get("SELECT j FROM JawatanPDRM j WHERE j.id = '"
											+ gredUniform + "'");
							if (jawatan != null) {
								gredKelasKuarters = jawatan.getKelasLayak();
							}
						} else if (idPerkhidmatan.equalsIgnoreCase("ZA")
								|| idPerkhidmatan.equalsIgnoreCase("VUZ")) {
							JawatanATM jawatan = (JawatanATM) mp
									.get("SELECT j FROM JawatanATM j WHERE j.id = '"
											+ gredUniform + "'");
							if (jawatan != null) {
								gredKelasKuarters = jawatan.getKelasLayak();
							}
						}
					}
					/* END UNTUK KES BADAN BERUNIFORM */
				}
				String gredKelasKuartersDowngrade = getKelasDowngrade(gredKelasKuarters);

				// KUARTERS PUTRAJAYA
				if (r.getLokasi().getId().equalsIgnoreCase("01")) {
					gredKelasKuartersDowngrade = getKelasDowngrade(gredKelasKuarters);
				}

				// KUARTERS KUALA LUMPUR/PETALING JAYA
				if (r.getLokasi().getId().equalsIgnoreCase("02")) {
					gredKelasKuartersDowngrade = "";
				}

				// KUARTERS KLIA
				if (r.getLokasi().getId().equalsIgnoreCase("06")) {
					if (gredKelasKuarters.equalsIgnoreCase("A")
							|| gredKelasKuarters.equalsIgnoreCase("B")
							|| gredKelasKuarters.equalsIgnoreCase("C")
							|| gredKelasKuarters.equalsIgnoreCase("D")
							|| gredKelasKuarters.equalsIgnoreCase("E")) {
						gredKelasKuarters = "F";
					}
					gredKelasKuartersDowngrade = "";
				}

				// KUARTERS TANJUNG KUPANG
				if (r.getLokasi().getId().equalsIgnoreCase("04")) {

					if (gredKelasKuarters.equalsIgnoreCase("A")
							|| gredKelasKuarters.equalsIgnoreCase("B")
							|| gredKelasKuarters.equalsIgnoreCase("C")) {
						gredKelasKuarters = "D";
					}
					if (gredKelasKuarters.equalsIgnoreCase("F")) {
						gredKelasKuarters = "G";
					}
					gredKelasKuartersDowngrade = "";
				}

				// KUARTERS LABUAN
				if (r.getLokasi().getId().equalsIgnoreCase("03")) {
					if (gredKelasKuarters.equalsIgnoreCase("A")
							|| gredKelasKuarters.equalsIgnoreCase("B")
							|| gredKelasKuarters.equalsIgnoreCase("C")
							|| gredKelasKuarters.equalsIgnoreCase("D")) {
						gredKelasKuarters = "E";
					}
					gredKelasKuartersDowngrade = "";
				}

				k1 = (KelasKuarters) mp.find(KelasKuarters.class,
						gredKelasKuarters);
				k2 = (KelasKuarters) mp.find(KelasKuarters.class,
						gredKelasKuartersDowngrade);
			}

			// if (uj != null) {
			// String gredKelasKuarters = "";
			// if (uj.getGredJawatan() != null) {
			// gredKelasKuarters = uj.getGredJawatan().getKelasKuarters();
			// }
			// String gredKelasKuartersDowngrade =
			// getKelasDowngrade(gredKelasKuarters);
			// k1 = (KelasKuarters) mp.find(KelasKuarters.class,
			// gredKelasKuarters);
			// k2 = (KelasKuarters) mp.find(KelasKuarters.class,
			// gredKelasKuartersDowngrade);
			// }

			statusPermohonanAsal = r.getStatus().getId();
			if (r.getStatus().getId().equalsIgnoreCase("1423101446117") == false) {
				r.setStatus((Status) mp.find(Status.class, "1419601227590"));
			} else {
				r.setStatus((Status) mp.find(Status.class, "1423101446117"));
			}
			mp.flush();
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error hantarSemakan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		@SuppressWarnings("rawtypes")
		List kelasKuarters = new ArrayList();
		if (k1 != null)
			kelasKuarters.add(k1.getId());

		if (agihan != null) {
			if (k2 != null) {
				// if (agihan.getKelasKuarters()!= k2.getId()) {
				// if (agihan.getKelasKuarters()!= null) {
				if (agihan.getKelasKuarters().equals(k2.getId())) {
					wujudKelasDowngrade = true;
				}
			}
		}

		if (wujudKelasDowngrade == false) {
			if ("1".equals(r.getFlagDowngrade())) {
				if (k2 != null)
					kelasKuarters.add(k2.getId());
			}
		}

		try {
			mp = new MyPersistence();
			mp.begin();
			for (int i = 0; i < kelasKuarters.size(); i++) {

				String jenisKelasKuarters = "";

				/* CHECK JIKA DAH ADA DALAM SENARAI MENUNGGU */
				KuaAgihan agihanLama = (KuaAgihan) mp
						.get("SELECT a FROM KuaAgihan a WHERE a.permohonan.id = '"
								+ idPermohonan
								+ "' and a.idLokasi.id='"
								+ lp.getId()
								+ "' and a.kelasKuarters ='"
								+ kelasKuarters.get(i).toString() + "'");

				if (agihanLama == null) {
					KuaAgihan ka = new KuaAgihan();
					if (k2 != null) {
						if (k2.getId().equals(kelasKuarters.get(i).toString())) {
							jenisKelasKuarters = "D";
						} else {
							jenisKelasKuarters = "L";
						}
					} else {
						jenisKelasKuarters = "L";
					}
					ka.setPermohonan(r);
					ka.setPemohon(r.getPemohon());
					ka.setPekerjaan(uj);
					if (statusPermohonanAsal.equalsIgnoreCase("1431327994524")) {
						ka.setNoGiliran(1);
					} else {
						ka.setNoGiliran(getNoGiliran(kelasKuarters.get(i)
								.toString(), r.getLokasi().getId(), mp));
					}
					ka.setStatus((Status) mp
							.find(Status.class, "1419601227590"));
					ka.setTarikhAgih(new Date());
					ka.setTarikhKemaskini(new Date());
					ka.setDateAgih(new Date());
					ka.setPengagih((Users) mp.find(Users.class, userId));
					ka.setKelasKuarters(kelasKuarters.get(i).toString());
					ka.setJenisKelasKuarters(jenisKelasKuarters);
					ka.setIdLokasi(lp);
					ka.setFlagAktif("Y");
					mp.persist(ka);

					// rozai tambah 24/8/2017 sebab nak guna menunggu manual
					// 17072018 - EDIT BY PEJE - IF RECORD EXIST THEN LEAVE
					Giliran giliran = (Giliran) mp
							.get("select x from Giliran x where x.lokasiPermohonan.id = '"
									+ lp.getId()
									+ "' and x.kelasKuarters = '"
									+ kelasKuarters.get(i).toString()
									+ "' and x.noKP = '"
									+ r.getPemohon().getId() + "'");
					if (giliran == null) {
						giliran = new Giliran();
						giliran.setNoKP(r.getPemohon().getId());
						giliran.setLokasiPermohonan(lp);
						// giliran.setNoDaftar(r.getNoPermohonan());
						Integer intGiliran = getNoGiliran(kelasKuarters.get(i)
								.toString(), r.getLokasi().getId(), mp);
						String noGiliran = intGiliran.toString();
						giliran.setNoGiliran(noGiliran);
						giliran.setKelasKuarters(kelasKuarters.get(i)
								.toString());
						giliran.setJenisKelasKuarters(jenisKelasKuarters);
						giliran.setFlagManual("T");
						mp.persist(giliran);
					}
				} else {
					Giliran giliran = (Giliran) mp
							.get("select x from Giliran x where x.lokasiPermohonan.id = '"
									+ lp.getId()
									+ "' and x.kelasKuarters = '"
									+ kelasKuarters.get(i).toString()
									+ "' and x.noKP = '"
									+ r.getPemohon().getId() + "'");
					if (giliran == null) {
						giliran = new Giliran();
						giliran.setNoKP(r.getPemohon().getId());
						giliran.setLokasiPermohonan(lp);
						giliran.setNoGiliran(String.valueOf(agihanLama
								.getNoGiliran()));
						giliran.setKelasKuarters(kelasKuarters.get(i)
								.toString());
						giliran.setJenisKelasKuarters(jenisKelasKuarters);
						giliran.setFlagManual("T");
						giliran.setTarikhKemaskini(new Date());
						mp.persist(giliran);
					}
				}
			}
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error hantarSemakan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		context.put("success", success);
		return getPath() + "/result/hantarSemakan.vm";
	}

	@Command("addCatatanPermohonan")
	public String addCatatanPermohonan() {
		context.remove("rekod");
		return getPath()
				+ "/sub_page/catatanPermohonan/popupCatatanPermohonan.vm";
	}

	@Command("doSaveCatatanPermohonan")
	public String doSaveCatatanPermohonan() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		try {
			mp = new MyPersistence();
			KuaPermohonan permohonan = (KuaPermohonan) mp.find(
					KuaPermohonan.class, getParam("idPermohonan"));
			if (permohonan != null) {
				mp.begin();
				KuaCatatanPermohonan catatanPermohonan = new KuaCatatanPermohonan();
				catatanPermohonan.setPermohonan(permohonan);
				catatanPermohonan.setCatatan(getParam("catatan"));
				catatanPermohonan.setDaftarOleh((Users) mp.find(Users.class,
						userId));
				catatanPermohonan.setTarikhDaftar(new Date());
				mp.persist(catatanPermohonan);
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		simpanAuditTrail(getParam("idPermohonan"), "simpanCatatan");
		return getPerakuan();
	}

	@Command("doDeleteCatatanPermohonan")
	public String doDeleteCatatanPermohonan() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		try {
			mp = new MyPersistence();
			KuaCatatanPermohonan catatanPermohonan = (KuaCatatanPermohonan) mp
					.find(KuaCatatanPermohonan.class, getParam("idCatatan"));
			if (catatanPermohonan != null) {
				mp.begin();
				mp.remove(catatanPermohonan);
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		simpanAuditTrail(getParam("idPermohonan"), "hapusCatatan");
		return getPerakuan();
	}

	// @Command("simpanCatatan")
	// // butang simpan catatan
	// public String simpanCatatan() {
	// boolean success = false;
	// String idPermohonan = getParam("idPermohonan");
	// KuaPermohonan r = null;
	// userId = (String) request.getSession().getAttribute("_portal_login");
	// try {
	// mp = new MyPersistence();
	// //Users penyedia=((Users) mp.find(Users.class, userId));
	// //String nama=penyedia.getUserName();
	// r = (KuaPermohonan) mp.find(KuaPermohonan.class, idPermohonan);
	// Date t = new Date();
	// //Format formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
	// Format formatter = new SimpleDateFormat("dd/MM/yyyy");
	// String s = formatter.format(t);
	// String catatanDulu=r.getCatatan();
	// if(catatanDulu!=null){
	// //r.setCatatan(catatanDulu+"&#13;&#10;"+"&#13;&#10;"+getParam("catatan")+"&#13;&#10;"+"Dikemaskini oleh "+WordUtils.capitalize(nama)+" pada "+s);
	// //r.setCatatan(catatanDulu+"&#13;&#10;"+"("+s+")"+getParam("catatan")+" ("+nama+")");
	// //r.setCatatan(catatanDulu+"&#13;&#10;"+"&#13;&#10;"+getParam("catatan")+"&#13;&#10;"+"("+s+") oleh "+nama);
	// r.setCatatan(catatanDulu+"&#13;&#10;"+"&#13;&#10;"+"("+s+") "+getParam("catatan"));
	// }
	// else
	// {
	// //r.setCatatan(getParam("catatan")+"&#13;&#10;"+"("+s+") oleh "+nama);
	// //r.setCatatan(getParam("catatan")+"&#13;&#10;"+"("+s+") oleh "+nama);
	// //r.setCatatan("("+s+") "+getParam("catatan")+" - "+nama);
	// r.setCatatan("("+s+") "+getParam("catatan"));
	// }
	// mp.begin();
	// mp.persist(r);
	// mp.commit();
	// success = true;
	// } catch (Exception e) {
	// System.out.println("Error simpanCatatan : " + e.getMessage());
	// } finally {
	// if (mp != null) { mp.close(); }
	// }
	// simpanAuditTrail(idPermohonan, "simpanCatatan");
	// context.put("success", success);
	// return getPath() + "/result/simpanCatatan.vm";
	// }

	@Command("tolakPermohonan")
	// butang simpan catatan
	public String tolakPermohonan() {
		boolean success = false;
		String idPermohonan = getParam("idPermohonan");
		KuaPermohonan r = null;
		userId = (String) request.getSession().getAttribute("_portal_login");
		try {
			mp = new MyPersistence();
			r = (KuaPermohonan) mp.find(KuaPermohonan.class, idPermohonan);
			if (r != null) {
				r.setStatus((Status) mp.find(Status.class, "1419601227598"));
			}
			mp.begin();
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error batalPermohonan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		simpanAuditTrail(idPermohonan, "tolakPermohonan");
		context.put("success", success);
		return getPath() + "/result/tolakPermohonan.vm";
	}

	public String getKelasDowngrade(String kelas) {
		String kelasDowngrade = "";
		if ("A".equals(kelas)) {
			kelasDowngrade = "";
		} else if ("B".equals(kelas)) {
			kelasDowngrade = "";
		} else if ("D".equals(kelas)) {
			kelasDowngrade = "F";
		} else if ("E".equals(kelas)) {
			kelasDowngrade = "F";
		} else {
			kelasDowngrade = "";
		}
		return kelasDowngrade;
	}

	/*--------------------------------------------------------------- LULUS PERMOHONAN ---------------------------------------------------------------*/

	@Command("lulusPermohonan")
	public String lulusPermohonan() {
		boolean success = false;
		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			kp.setStatus((Status) mp.find(Status.class, "1419601227595"));
			kp.setTarikhKemaskini(new Date());
			mp.begin();
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error begin : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		context.put("success", success);
		return getPath() + "/result/lulusPermohonan.vm";
	}

	/*--------------------------------------------------------------- SEMAK KELAYAKAN 1 ---------------------------------------------------------------*/
	@Command("kelulusan1")
	public String kelulusan1() {// fungsi untuk semak kelayakan pemohon
		Boolean kelulusan = false;
		boolean newRecord = false;
		boolean newRecordKerja = false;
		boolean newRecordPinjaman = false;
		boolean result = false;// tarikh jangka siap
		boolean result2 = false;// radius rumah dimiliki
		@SuppressWarnings("unused")
		boolean result3 = false;// prima/ppa1m mesti diduduki
		@SuppressWarnings("unused")
		int jenisAktiviti = 2;
		@SuppressWarnings("unused")
		int jenisAktivitiKerja = 2;
		@SuppressWarnings("unused")
		int jenisAktivitiPinjaman = 2;
		String idPermohonan = getParam("idPermohonan");

		try {
			mp = new MyPersistence();

			// dapatkan lokasi permohonan dan lokasi kuarters
			LokasiPermohonan lokasiPermohonan = (LokasiPermohonan) mp.find(
					LokasiPermohonan.class, getParam("idLokasiPermohonan"));
			Bandar bandarLokasi = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPekerjaan"));

			// ------------------start rozai upgrade
			// 18/11/2015--------------------------
			if ("14".equals(lokasiPermohonan.getNegeri().getId())
					|| "15".equals(lokasiPermohonan.getNegeri().getId())
					|| "16".equals(lokasiPermohonan.getNegeri().getId())
					|| "01".equals(lokasiPermohonan.getNegeri().getId())
					|| "10".equals(lokasiPermohonan.getNegeri().getId())) {

				if (bandarLokasi.getNegeri().getId()
						.equals(lokasiPermohonan.getNegeri().getId())) {
					result = true;
					kelulusan = true;
					context.put("kelulusan", kelulusan);
				} else {
					result = false;
					kelulusan = false;
					context.put("kelulusan", kelulusan);
				}

				if ("16".equals(lokasiPermohonan.getNegeri().getId())
						&& bandarLokasi.getId().equals("1052")) {
					// PUTRAJAYA--CYBERJAYA
					result = true;
					kelulusan = true;
					context.put("kelulusan", kelulusan);
				}

				if ("10".equals(lokasiPermohonan.getNegeri().getId())
						&& bandarLokasi.getId().equals("1401")) {
					// PETALING JAYA--KUALA LUMPUR
					result = true;
					kelulusan = true;
					context.put("kelulusan", kelulusan);
				}

			} else if ("05".equals(lokasiPermohonan.getNegeri().getId())) { // KLIA

				// NILAI //SEPANG //B.B.ENSTEK
				if (bandarLokasi.getId().equals("0517")
						|| bandarLokasi.getId().equals("1035")
						|| bandarLokasi.getId().equals("0543")) {
					result = true;
					kelulusan = true;
					context.put("kelulusan", kelulusan);
				} else {
					result = false;
					kelulusan = false;
					context.put("kelulusan", kelulusan);
				}
			}

			// kelulusan = true;
			// context.put("kelulusan", kelulusan);
			// ---------------------------end rozai upgrade
			// 18/11/2015----------------------------------------------------------------------
			/* Kiraan Radius dan Tarikh Jangka Siap Kuarters */
			int pinjamanPerumahan = getParamAsInteger("valuePinjamanPerumahan");
			String jenisPerumahan = getParam("jenisPerumahan");
			String pembiayaan = getParam("valuePembiayaan");
			String pembelian = getParam("valuePembelian");
			int statusRumah = getParamAsInteger("valueStatusRumah");
			Date t = null;

			if (pinjamanPerumahan == 1) // checkPinjaman(ada)
			{
				if ("swasta".equals(jenisPerumahan)) // checkPerumahan(swasta)
				{
					if ("bank".equals(pembiayaan)) // checkPembiayaan
					{
						if (statusRumah == 1) // checkPembinaan(siap)
						{
							String jarak = checkJarakLangitudeLotitude(mp);
							if (jarak == "true") // luar radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
								}
							} else {
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{

									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
								}
							}
							context.put("result2", result2);
						} else if (statusRumah == 2) // checkPembinaan(dalam
						// pembinaan)
						{
							String[] tarikhJangkaSiap = getParam(
									"tarikhJangkaSiap").split(",");
							String tarikhJangkaSiapBaru = "01-"
									+ getMonth(tarikhJangkaSiap[0].trim())
									+ "-" + tarikhJangkaSiap[1].trim();
							int days = 0;

							try {
								t = new SimpleDateFormat("dd-MM-yyyy")
										.parse(tarikhJangkaSiapBaru);
								days = Util.daysBetween(new Date(), t);
							} catch (ParseException e1) {
								e1.printStackTrace();
							}

							if (days < 365) // kurang dari setahun
							{
								String jarak = checkJarakLangitudeLotitude(mp);
								if (jarak == "true") // luar radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								} else // dalam radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								}
								context.put("result2", result2);
							} else // lebih dari setahun
							{
								String jarak = checkJarakLangitudeLotitude(mp);
								if (jarak == "true") // luar radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
									context.put("result2", result2);
								} else // dalam radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								}

							}
						}
					} else // if pinjaman kerajaan
					{
						if (statusRumah == 1) // checkPembinaan(siap)
						{
							String jarak = checkJarakLangitudeLotitude(mp);
							if (jarak == "true") // luar radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
								}
							} else {
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									// result2=false;
									result2 = true;// override untuk kes
									// tertentu
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									// result2=false;
									result2 = true;// override untuk kes
									// tertentu
								}
							}
							context.put("result2", result2);
						} else if (statusRumah == 2) // checkPembinaan(dalam
						// pembinaan)
						{
							String[] tarikhJangkaSiap = getParam(
									"tarikhJangkaSiap").split(",");
							String tarikhJangkaSiapBaru = "01-"
									+ getMonth(tarikhJangkaSiap[0].trim())
									+ "-" + tarikhJangkaSiap[1].trim();
							int days = 0;

							try {
								t = new SimpleDateFormat("dd-MM-yyyy")
										.parse(tarikhJangkaSiapBaru);
								days = Util.daysBetween(new Date(), t);
							} catch (ParseException e1) {
								e1.printStackTrace();
							}

							if (days < 365) // kurang dari setahun
							{
								String jarak = checkJarakLangitudeLotitude(mp);
								if (jarak == "true") // luar radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								} else // dalam radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										// result2=false;
										result2 = true;// override untuk kes
										// tertentu
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										// result2=false;
										result2 = true;// override untuk kes
										// tertentu
									}
								}
								context.put("result2", result2);
							} else // lebih dari setahun
							{
								String jarak = checkJarakLangitudeLotitude(mp);
								if (jarak == "true") // luar radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
									context.put("result2", result2);
								} else // dalam radius
								{
									if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
									{
										result2 = true;
									} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
									{
										result2 = true;
									} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
									{
										result2 = true;
									}
								}

							}
						}
					}
				} else // checkPerumahan(PRIMA/PPA1M)
				{
					if (statusRumah == 1) // checkPembinaan(siap)
					{
						String jarak = checkJarakLangitudeLotitude(mp);
						if (jarak == "true")// luar radius
						{
							if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
							{
								result2 = true;
							} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
							{
								result2 = true;
							} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
							{
								result2 = true;
							}
						} else {
							if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
							{
								// result2=false;
								result2 = true;// override untuk kes tertentu
							} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
							{
								// result2=false;
								result2 = true;// override untuk kes tertentu
							} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
							{
								// result2=false;
								result2 = true;// override untuk kes tertentu
							}
						}
						context.put("result2", result2);
					} else if (statusRumah == 2) // checkPembinaan(dalam
					// pembinaan)
					{
						String[] tarikhJangkaSiap = getParam("tarikhJangkaSiap")
								.split(",");
						String tarikhJangkaSiapBaru = "01-"
								+ getMonth(tarikhJangkaSiap[0].trim()) + "-"
								+ tarikhJangkaSiap[1].trim();
						int days = 0;

						try {
							t = new SimpleDateFormat("dd-MM-yyyy")
									.parse(tarikhJangkaSiapBaru);
							days = Util.daysBetween(new Date(), t);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}

						if (days < 365) // kurang dari setahun
						{
							String jarak = checkJarakLangitudeLotitude(mp);
							if (jarak == "true") // luar radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result = true;
								}
							} else // dalam radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									// result=false;
									result2 = true;// override untuk kes
									// tertentu
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									// result=false;
									result2 = true;// override untuk kes
									// tertentu
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									// result=false;
									result2 = true;// override untuk kes
									// tertentu
								}
							}
							context.put("result", result);
						} else // lebih dari setahun
						{
							String jarak = checkJarakLangitudeLotitude(mp);
							if (jarak == "true") // luar radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
								}
								context.put("result2", result2);
							} else // dalam radius
							{
								if ("sendiri".equals(pembelian)) // checkPembelian(sendiri)
								{
									result2 = true;
									context.put("result", result);
								} else if ("pasangan".equals(pembelian)) // checkPembelian(pasangan)
								{
									result2 = true;
									context.put("result", result);
								} else if ("bersama".equals(pembelian)) // checkPembelian(bersama)
								{
									result2 = true;
									context.put("result", result);
								}
							}

						}
					}
				}
			} else // checkPinjaman(tiada)
			{
				result = true;
				result2 = true;

				context.put("result", result);
				context.put("result2", result2);

			}

			KuaPermohonan kp = null;
			kp = (KuaPermohonan) mp.find(KuaPermohonan.class, idPermohonan);
			Users pemohon = (Users) mp.find(Users.class, kp.getPemohon()
					.getId());
			KuaPermohonan permohonan = (KuaPermohonan) mp
					.get("SELECT p FROM KuaPermohonan p WHERE p.pemohon.id = '"
							+ kp.getPemohon().getId()
							+ "' AND p.status.id = '1419483289675'");

			mp.begin();
			Status status = (Status) mp.find(Status.class, "1419483289675");

			if (!"".equals(idPermohonan)) {
				kp = (KuaPermohonan) mp.find(KuaPermohonan.class, idPermohonan);
			} else {
				if (permohonan != null) {
					kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
							permohonan.getId());
				}
			}

			if (kp == null) {
				jenisAktiviti = 1;
				newRecord = true;
				kp = new KuaPermohonan();
			} else {
				String statusTerkini = kp.getStatus().getId().toString();
				if ("1419483289S675".equals(statusTerkini))// deraf permohonan
				{
					status = (Status) mp.find(Status.class, "1419483289675");
				} else if ("1419483289678".equals(statusTerkini))// permohonan
				// baru
				{
					status = (Status) mp.find(Status.class, "1419483289678");
				} else if ("1431405647299".equals(statusTerkini))// permohonan
				// kemaskini
				{
					status = (Status) mp.find(Status.class, "1431405647299");
				} else {
					status = (Status) mp.find(Status.class, statusTerkini);
				}
			}

			kp.setPemohon(pemohon);
			kp.setLokasi(lokasiPermohonan);
			// kp.setTarikhMasuk(new Date());
			if (newRecord == false) {
				kp.setTarikhKemaskini(new Date());
			}
			kp.setStatus(status);
			kp.setKelulusan1(kelulusan.toString());

			if (result == true && result2 == true) {
				kp.setKelulusan3("true");
				kp.setPinjaman(1);
			} else {
				kp.setKelulusan3("false");
				kp.setPinjaman(0);
			}

			if (newRecord == true) {
				mp.persist(kp);
			}

			idPermohonan = kp.getId();

			// db.commit(request, "PROCESSING FILE (Permohonan Kuarters) : "+
			// idPermohonan, jenisAktiviti);

			context.put("idPermohonan", idPermohonan);

			UsersJob uj = null;
			UsersJob usersJob = (UsersJob) mp
					.get("SELECT usersJob FROM UsersJob usersJob WHERE usersJob.users.id = '"
							+ kp.getPemohon().getId() + "'");
			if (usersJob != null) {
				uj = (UsersJob) mp.find(UsersJob.class,
						usersJob.getId() != null ? usersJob.getId() : "");
			}

			Bandar bandarPekerjaan = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPekerjaan"));

			if (uj == null) {
				jenisAktivitiKerja = 1;
				newRecordKerja = true;
				uj = new UsersJob();
			}

			uj.setUsers(pemohon);
			// uj.setAlamat1(getParam("alamatPekerjaan1"));
			// uj.setAlamat2(getParam("alamatPekerjaan2"));
			// uj.setAlamat3(getParam("alamatPekerjaan3"));
			uj.setPoskod(getParam("poskodPekerjaan"));
			uj.setBandar(bandarPekerjaan);

			context.put("uj", uj);

			if (newRecordKerja == true) {
				mp.persist(uj);
			}

			// db.commit(request, "PROCESSING FILE (Pekerjaan Pemohon) : "+
			// uj.getId(), jenisAktivitiKerja);

			KuaPinjamanPemohon kpp = null;

			KuaPinjamanPemohon pinjaman = (KuaPinjamanPemohon) mp
					.get("SELECT p FROM KuaPinjamanPemohon p WHERE p.users.id = '"
							+ kp.getPemohon().getId() + "'");
			if (pinjaman != null)
				kpp = (KuaPinjamanPemohon) mp.find(KuaPinjamanPemohon.class,
						pinjaman.getId() != null ? pinjaman.getId() : "");

			Bandar bandarPinjaman = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPinjaman"));

			if (kpp == null) {
				jenisAktivitiPinjaman = 1;
				newRecordPinjaman = true;
				kpp = new KuaPinjamanPemohon();
			}

			kpp.setUsers(pemohon);
			kpp.setPinjamanPerumahan(pinjamanPerumahan);
			kpp.setStatusPembinaan(statusRumah);
			kpp.setTarikhJangkaSiap(t);
			kpp.setPoskodPinjaman(getParam("poskodPinjaman"));
			kpp.setBandar(bandarPinjaman);
			kpp.setJenisPerumahan(jenisPerumahan);
			kpp.setPembiayaan(pembiayaan);
			kpp.setPembelian(getParam("valuePembelian"));
			kpp.setAlamat1(getParam("alamatPinjaman1"));
			kpp.setAlamat2(getParam("alamatPinjaman2"));
			kpp.setAlamat3(getParam("alamatPinjaman3"));

			if (newRecordPinjaman == true) {
				mp.persist(kpp);
			}

			// db.commit(request, "PROCESSING FILE (Pinjaman Pemohon) : "+
			// uj.getId(), jenisAktivitiPinjaman);

			mp.commit();
			context.remove("resultPermohonanCompletion");
			// context.put("resultPermohonanCompletion",
			// getCompletionPermohonan(kp.getId()));
			// context.put("resultPermohonanCompletion", kelulusan.toString());
		} catch (Exception e) {
			System.out.println("Error kelulusan1 : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		// rozai add 29/3/2016
		// context.remove("resultPermohonanCompletion");
		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(mp, getParam("idPermohonan")));
		return getPath() + "/result/kelulusan1.vm";
	}

	@Command("kelulusan1Retrieve")
	public String kelulusan1Retrieve() {
		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			context.put("kelulusan", Boolean.parseBoolean(kp.getKelulusan1()));
			context.put("result", true);
			context.put("result2", true);
			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion",
					getCompletionPermohonan(mp, getParam("idPermohonan")));
			Users u = (Users) mp.find(Users.class, kp.getPemohon().getId());
			context.put("users", u);

		} catch (Exception e) {
			System.out.println("Error kelulusan1Retrieve : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		getPeribadi();
		return getPath() + "/result/kelulusan1.vm";

	}

	@Command("getPerakuan")
	public String getPerakuan() {
		try {
			mp = new MyPersistence();

			String kelasLayak = "";
			String kelasDowngrade = "";
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			UsersJob pekerjaan = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ kp.getPemohon().getId() + "'");

			if (pekerjaan != null) {
				String gredKelasKuarters = "";
				String idPerkhidmatan = "";
				String gredJawatan = "";
				if (pekerjaan.getGredJawatan() != null) {
					gredKelasKuarters = pekerjaan.getGredJawatan()
							.getKelasKuarters();

					/* UNTUK KES BADAN BERUNIFORM */
					idPerkhidmatan = pekerjaan.getKelasPerkhidmatan().getId();
					gredJawatan = pekerjaan.getGredJawatan().getKeterangan();
					if (idPerkhidmatan.equalsIgnoreCase("XA")
							|| idPerkhidmatan.equalsIgnoreCase("VUXA")
							||
							// idPerkhidmatan.equalsIgnoreCase("T") ||
							// idPerkhidmatan.equalsIgnoreCase("VUT") ||
							idPerkhidmatan.equalsIgnoreCase("YA")
							|| idPerkhidmatan.equalsIgnoreCase("VUYA")
							|| idPerkhidmatan.equalsIgnoreCase("ZA")
							|| idPerkhidmatan.equalsIgnoreCase("VUZ")) {
						String gredUniform = idPerkhidmatan + gredJawatan;
						if (idPerkhidmatan.equalsIgnoreCase("XA")
								|| idPerkhidmatan.equalsIgnoreCase("VUXA")) {
							JawatanAPMM jawatan = (JawatanAPMM) mp
									.get("SELECT j FROM JawatanAPMM j WHERE j.id = '"
											+ gredUniform + "'");
							if (jawatan != null) {
								gredKelasKuarters = jawatan.getKelasLayak();
							}
						} else if (idPerkhidmatan.equalsIgnoreCase("YA")
								|| idPerkhidmatan.equalsIgnoreCase("VUYA")) {
							JawatanPDRM jawatan = (JawatanPDRM) mp
									.get("SELECT j FROM JawatanPDRM j WHERE j.id = '"
											+ gredUniform + "'");
							if (jawatan != null) {
								gredKelasKuarters = jawatan.getKelasLayak();
							}
						} else if (idPerkhidmatan.equalsIgnoreCase("ZA")
								|| idPerkhidmatan.equalsIgnoreCase("VUZ")) {
							JawatanATM jawatan = (JawatanATM) mp
									.get("SELECT j FROM JawatanATM j WHERE j.id = '"
											+ gredUniform + "'");
							if (jawatan != null) {
								gredKelasKuarters = jawatan.getKelasLayak();
							}
						}
					}
					/* END UNTUK KES BADAN BERUNIFORM */
				}
				kelasLayak = gredKelasKuarters;

				// KUARTERS KUALA LUMPUR/PETALING JAYA
				if (kp.getLokasi().getId().equalsIgnoreCase("02")) {
					kelasDowngrade = "";
				} else {
					kelasDowngrade = getKelasDowngrade(kelasLayak);
				}

				// KUARTERS KLIA
				if (kp.getLokasi().getId().equalsIgnoreCase("06")) {
					if (kelasLayak.equalsIgnoreCase("A")
							|| kelasLayak.equalsIgnoreCase("B")
							|| kelasLayak.equalsIgnoreCase("C")
							|| kelasLayak.equalsIgnoreCase("D")
							|| kelasLayak.equalsIgnoreCase("E")) {
						kelasLayak = "F";
					}
					kelasDowngrade = "";
				}

				// KUARTERS TANJUNG KUPANG
				if (kp.getLokasi().getId().equalsIgnoreCase("04")) {

					if (kelasLayak.equalsIgnoreCase("A")
							|| kelasLayak.equalsIgnoreCase("B")
							|| kelasLayak.equalsIgnoreCase("C")) {
						kelasLayak = "D";
					}
					if (kelasLayak.equalsIgnoreCase("F")) {
						kelasLayak = "G";
					}
					kelasDowngrade = "";
				}

				// KUARTERS LABUAN
				if (kp.getLokasi().getId().equalsIgnoreCase("03")) {
					if (kelasLayak.equalsIgnoreCase("A")
							|| kelasLayak.equalsIgnoreCase("B")
							|| kelasLayak.equalsIgnoreCase("C")
							|| kelasLayak.equalsIgnoreCase("D")
							|| kelasLayak.equalsIgnoreCase("E")) {
						kelasLayak = "E";
					} else {
						kelasLayak = "F";
					}
					kelasDowngrade = "";
				}
			}

			String lokasi = "";
			if (pekerjaan.getBandar() != null) {
				if (pekerjaan.getBandar().getNegeri() != null) {
					lokasi = pekerjaan.getBandar().getNegeri().getKeterangan();
				}
			}
			context.put("kelasLayak", kelasLayak);
			context.put("kelasDowngrade", kelasDowngrade);
			context.put("lokasi", lokasi);

			//
			kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			kp.setKelasLayak(kelasLayak);
			if (kelasLayak.equalsIgnoreCase("C")) {
				kp.setFlagDowngrade("0");
			}
			kp.setKelasDowngrade(kelasDowngrade);
			mp.begin();
			mp.commit();
			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion",
					getCompletionPermohonan(mp, getParam("idPermohonan")));

			// dapatkan KL area
			String nogori = "";
			kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			if (kp.getLokasi().getBandar().getNegeri().getId().toString() != null) {
				nogori = kp.getLokasi().getId().toString();
			}

			if (nogori.equalsIgnoreCase("02")) {
				context.put("areaKL", true);
			} else {
				context.put("areaKL", false);
			}
		} catch (Exception e) {
			System.out.println("Error getPerakuan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/entry_sub/entry_page_sub_bottom.vm";
	}

	/*--------------------------------------------------------------- DETAIL PERIBADI ---------------------------------------------------------------*/

	@Command("getPeribadi")
	public String getPeribadi() {

		try {
			mp = new MyPersistence();

			context.put("selectGelaran", dataUtil.getListGelaranPeribadi());
			context.put("selectJenisPengenalan",
					dataUtil.getListFewJenisPengenalan());
			context.put("selectAgama", dataUtil.getListAgama());
			context.put("selectJantina", dataUtil.getListJantina());
			context.put("selectBangsa", dataUtil.getListBangsa());
			context.put("selectEtnik", dataUtil.getListEtnik());
			context.put("selectStatusPerkahwinan",
					dataUtil.getListStatusPerkahwinan());
			context.put("selectNegeri", dataUtil.getListNegeri());
			context.put("selectNegeriSemasa", dataUtil.getListNegeri());

			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			Users u = (Users) mp.find(Users.class, kp.getPemohon().getId());
			context.put("users", u);

			// myLogger.debug("IMG NAME ::: " + getParam("imgName"));
			context.put("imgName", getParam("imgName"));

			Date tarikhLahir = kuaUtil.getTarikhLahir(u.getNoKP());
			context.put("tarikhLahir", tarikhLahir);

			context.put("activity", "peribadi");

			context.put("dirUpload", uploadDir + "qtr/permohonan/"
					+ getParam("idPermohonan") + "/");

		} catch (Exception e) {
			System.out.println("Error getPeribadi : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/sub_page/peribadi.vm";
	}

	@Command("selectBandar")
	public String selectBandar() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";

		if (get("idNegeri").trim().length() > 0) {
			idNegeri = get("idNegeri");
		}

		context.put("selectBandar", dataUtil.getListBandar(idNegeri));
		return getPath() + "/sub_page/peribadi/selectBandar.vm";
	}

	@Command("selectBandarSemasa")
	public String selectBandarSemasa() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeriSemasa = "0";

		if (get("idNegeriSemasa").trim().length() > 0)
			idNegeriSemasa = get("idNegeriSemasa");

		context.put("selectBandarSemasa",
				dataUtil.getListBandar(idNegeriSemasa));
		return getPath() + "/sub_page/peribadi/selectBandarSemasa.vm";
	}

	@Command("simpanPeribadi")
	public String simpanPeribadi() {
		boolean success = false;
		// String idPermohonan = getParam("idPermohonan");
		try {
			mp = new MyPersistence();

			String idBilAnak = getParam("idBilAnak");
			int bilAnak = 0;
			if (!"".equals(idBilAnak)) {
				if ("99".equals(idBilAnak)) {
					bilAnak = getParamAsInteger("userBilAnak");
				} else if ("TB".equals(idBilAnak)) {
					bilAnak = 0;
				} else {
					bilAnak = Integer.parseInt(idBilAnak);
				}
			}

			Gelaran gelaran = (Gelaran) mp.find(Gelaran.class,
					getParam("idGelaran"));
			JenisPengenalan jenisPengenalan = (JenisPengenalan) mp.find(
					JenisPengenalan.class, getParam("idJenisPengenalan"));
			Jantina jantina = (Jantina) mp.find(Jantina.class,
					getParam("idJantina"));
			Bangsa bangsa = (Bangsa) mp
					.find(Bangsa.class, getParam("idBangsa"));
			Etnik etnik = (Etnik) mp.find(Etnik.class, getParam("idEtnik"));
			StatusPerkahwinan statusPerkahwinan = (StatusPerkahwinan) mp.find(
					StatusPerkahwinan.class, getParam("idStatusPerkahwinan"));
			Bandar bandar = (Bandar) mp
					.find(Bandar.class, getParam("idBandar"));
			Bandar bandarSemasa = (Bandar) mp.find(Bandar.class,
					getParam("idBandarSemasa"));
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));

			Users users = (Users) mp.find(Users.class, kp.getPemohon().getId());

			users.setGelaran(gelaran);
			users.setUserName(getParam("userName"));
			// users.setAvatar(getParam("gambarPersonalImage"));
			users.setJenisPengenalan(jenisPengenalan);
			users.setNoKP(getParam("userNoKP"));
			users.setNoKP2(getParam("userNoKP2"));
			users.setTarikhLahir(getDate("userTarikhLahir"));
			users.setJantina(jantina);
			users.setBangsa(bangsa);
			users.setStatusPerkahwinan(statusPerkahwinan);
			users.setBilAnak(bilAnak);
			users.setFlagAnak(idBilAnak);
			users.setNoTelefon(getParam("userNoTel"));
			users.setNoTelefonBimbit(getParam("userNoTelBimbit"));
			users.setEmel(getParam("email"));
			users.setAlamat1(getParam("userAlamat1"));
			users.setAlamat2(getParam("userAlamat2"));
			users.setAlamat3(getParam("userAlamat3"));
			users.setPoskod(getParam("userPoskod"));
			users.setBandar(bandar);
			users.setUserAddress(getParam("userAlamatSemasa1"));
			users.setUserAddress2(getParam("userAlamatSemasa2"));
			users.setUserAddress3(getParam("userAlamatSemasa3"));
			users.setUserPostcode(getParam("userPoskodSemasa"));
			users.setUserBandar(bandarSemasa);
			users.setEtnik(etnik);

			mp.begin();

			kp.setPeribadi(1);
			kp.setTarikhKemaskini(new Date());
			kuaUtil.kuartersLog(2, "Users",
					(Users) mp.find(Users.class, userId), users.getId());

			// db.commit(request, "PROCESSING FILE (Penghuni - Peribadi) : "+
			// users.getId(), 2);
			mp.commit();

			success = true;

		} catch (Exception e) {
			System.out.println("Error simpanPeribadi : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		context.put("success", success);

		context.remove("resultPermohonanCompletion");
		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(mp, getParam("idPermohonan")));
		return getPath() + "/sub_page/result/simpanPeribadi.vm";
	}

	@Command("uploadGambar")
	public String uploadGambar() throws Exception {
		context.put("imgName", kuaUtil.uploadFile(request, "permohonan",
				getParam("idPermohonan")));
		return getPath() + "/sub_page/peribadi/uploaddoc.vm";
	}

	@Command("checkEmail")
	public String checkEmail() {
		boolean result = false;

		boolean firstValidate = Util.isValidEmailAddress(getParam("email"));
		boolean secondValidate = Util.validateEmail(getParam("email"));

		if (firstValidate == true && secondValidate == true) {
			result = true;
		}

		context.put("result", result);

		return getPath() + "/sub_page/result/resultCheckEmail.vm";
	}

	@Command("refreshListPeribadi")
	public String refreshListPeribadi() throws Exception {
		String imgName = getParam("imgName");

		context.put("imgName", imgName);

		return getPath() + "/sub_page/peribadi/listDokumen.vm";
	}

	/*--------------------------------------------------------------- DETAIL PEKERJAAN ---------------------------------------------------------------*/

	@Command("getPekerjaan")
	public String getPekerjaan() throws Exception {

		context.put("selectJawatan", dataUtil.getListJawatan());
		context.put("selectKelasPerkhidmatan",
				dataUtil.getListKelasPerkhidmatan());
		context.put("selectGredJawatan", dataUtil.getListGredPerkhidmatan());
		context.put("selectJenisPerkhidmatan",
				dataUtil.getListJenisPerkhidmatan());
		context.put("selectStatusPerkhidmatan",
				dataUtil.getListStatusPerkhidmatan());
		context.put("selectKementerian", dataUtil.getListKementerian());

		context.put("activity", "pekerjaan");

		context.remove("uj");

		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			UsersJob usersJob = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ kp.getPemohon().getId() + "'");

			if (usersJob != null) {
				context.put("uj", mp.find(UsersJob.class, usersJob.getId()));
			}

			context.put("users", mp.find(Users.class, kp.getPemohon().getId()));
			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion",
					getCompletionPermohonan(mp, getParam("idPermohonan")));

		} catch (Exception e) {
			System.out.println("Error getPekerjaan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/sub_page/pekerjaan.vm";
	}

	@Command("selectBandarPekerjaan")
	public String selectBandarPekerjaan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";

		if (get("idNegeriPekerjaan").trim().length() > 0) {
			idNegeri = get("idNegeriPekerjaan");
		}

		context.put("selectBandarPekerjaan", dataUtil.getListBandar(idNegeri));
		return getPath() + "/select/selectBandarPekerjaan.vm";
	}

	@Command("selectJabatan")
	public String selectJabatan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idKementerian = "0";

		if (get("idKementerian").trim().length() > 0) {
			idKementerian = get("idKementerian");
		}

		context.put("selectJabatan", dataUtil.getListAgensi(idKementerian));
		return getPath() + "/sub_page/pekerjaan/selectJabatan.vm";
	}

	@Command("selectJenisPerkhidmatan")
	public String selectJenisPerkhidmatan() {
		dataUtil = DataUtil.getInstance(db);
		String idJenisPerkhidmatan = getParam("idJenisPerkhidmatan");
		String subPath = "jenisPerkhidmatan1";

		if ("01".equals(idJenisPerkhidmatan)) {
			context.put("selectKementerian", dataUtil.getListKementerian());

			subPath = "jenisPerkhidmatan1";
		} else if ("03".equals(idJenisPerkhidmatan)) {
			context.put("selectBadanBerkanun", dataUtil.getListBadanBerkanun());

			subPath = "jenisPerkhidmatan2";
		}

		return getPath() + "/sub_page/pekerjaan/" + subPath + ".vm";
	}

	@Command("selectJawatan")
	public String selectJawatan() {
		dataUtil = DataUtil.getInstance(db);
		String idKelasPerkhidmatan = "";
		if (get("idKelasPerkhidmatan").trim().length() > 0) {
			idKelasPerkhidmatan = get("idKelasPerkhidmatan");
			idKelasPerkhidmatan = idKelasPerkhidmatan.substring(0, 1);
		}
		context.put("selectJawatan",
				dataUtil.getListJawatan(idKelasPerkhidmatan));
		return getPath() + "/sub_page/pekerjaan/selectJawatan.vm";
	}

	@Command("simpanPekerjaan")
	public String simpanPekerjaan() {
		// userId = (String) request.getSession().getAttribute("_portal_login");
		Boolean result = true;
		Boolean result2 = true;
		boolean success = false;
		boolean newRecord = false;
		int jenisAktiviti = 2;
		// String idPermohonan = getParam("idPermohonan");
		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			Users users = (Users) mp.find(Users.class, kp.getPemohon().getId());
			KelasPerkhidmatan kelasPerkhidmatan = (KelasPerkhidmatan) mp.find(
					KelasPerkhidmatan.class, getParam("idKelasPerkhidmatan"));
			GredPerkhidmatan gredJawatan = (GredPerkhidmatan) mp.find(
					GredPerkhidmatan.class, getParam("idGredJawatan"));
			Jawatan jawatan = (Jawatan) mp.find(Jawatan.class,
					getParam("idJawatan"));
			JenisPerkhidmatan jenisPerkhidmatan = (JenisPerkhidmatan) mp.find(
					JenisPerkhidmatan.class, getParam("idJenisPerkhidmatan"));
			StatusPerkhidmatan statusPerkhidmatan = (StatusPerkhidmatan) mp
					.find(StatusPerkhidmatan.class,
							getParam("idStatusPerkhidmatan"));
			Agensi agensi = (Agensi) mp.find(Agensi.class,
					getParam("idJabatan"));
			BadanBerkanun badanBerkanun = (BadanBerkanun) mp.find(
					BadanBerkanun.class, getParam("idBadanBerkanun"));
			// Bandar bandar = (Bandar) mp.find(Bandar.class,
			// getParam("idBandarPekerjaan"));

			if ("02".equals(statusPerkhidmatan.getId())) {
				int days = Util.daysBetween(new Date(), getDate("tarikhTamat"));

				if (days < 365) {
					result = false;
				}

				if (getParamAsInteger("valueFlagITP") == 0
						&& getParamAsInteger("valueFlagEPW") == 0) {
					result2 = false;
				}
			}

			UsersJob uj = null;

			UsersJob usersJob = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ kp.getPemohon().getId() + "'");
			if (usersJob != null) {
				uj = (UsersJob) mp.find(UsersJob.class, usersJob.getId());
			}

			if (uj == null) {
				newRecord = true;
				jenisAktiviti = 1;
				uj = new UsersJob();
			}

			uj.setUsers(users);
			uj.setKelasPerkhidmatan(kelasPerkhidmatan);
			uj.setGredJawatan(gredJawatan);
			uj.setJawatan(jawatan);
			uj.setTarikhLantikan(getDate("tarikhLantikan"));
			uj.setNoGaji(getParam("noGaji"));
			uj.setGajiPokok(Double.parseDouble(getParam("gajiPokok")
					.replaceAll("RM", "").replaceAll(",", "")));
			uj.setJenisPerkhidmatan(jenisPerkhidmatan);
			uj.setStatusPerkhidmatan(statusPerkhidmatan);
			uj.setTarikhTamat(getDate("tarikhTamat"));
			uj.setFlagITP(getParamAsInteger("valueFlagITP"));
			uj.setFlagEPW(getParamAsInteger("valueFlagEPW"));
			uj.setAgensi(agensi);
			uj.setBadanBerkanun(badanBerkanun);
			uj.setBahagian(getParam("bahagian"));
			uj.setAlamat1(getParam("alamatPekerjaan1"));
			uj.setAlamat2(getParam("alamatPekerjaan2"));
			uj.setAlamat3(getParam("alamatPekerjaan3"));
			// uj.setPoskod(getParam("poskodPekerjaan"));
			// uj.setBandar(bandar);
			uj.setNoTelPejabat(getParam("noTelPejabat"));
			uj.setNoFaks(getParam("noFaks"));
			uj.setEmel(getParam("email"));
			uj.setFlagCola(getParamAsInteger("valueFlagCola"));

			mp.begin();

			if (newRecord == true) {
				mp.persist(uj);
			}

			// if (!"03".equals(statusPerkhidmatan.getId())) {
			if (statusPerkhidmatan.getId() != null) {
				if (result == true && result2 == true) {
					kp.setKelulusan2("true");
				} else {
					kp.setKelulusan2("false");
				}
			} else {
				kp.setKelulusan2("false");
			}

			kp.setPekerjaan(1);
			kp.setTarikhKemaskini(new Date());
			kuaUtil.kuartersLog(jenisAktiviti, "UsersJob",
					(Users) mp.find(Users.class, userId), uj.getId());

			// db.commit(request, "PROCESSING FILE (Pekerjaan Penghuni) : "+
			// uj.getId(), jenisAktiviti);
			mp.commit();

			success = true;

			context.put("success", success);
			context.put("result", result);
			context.put("result2", result2);

			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion",
					getCompletionPermohonan(mp, getParam("idPermohonan")));

		} catch (Exception e) {
			System.out.println("Error simpanPekerjaan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/sub_page/result/simpanPekerjaan.vm";
	}

	/*--------------------------------------------------------------- DETAIL PASANGAN ---------------------------------------------------------------*/

	@Command("getPasangan")
	public String getPasangan() throws Exception {

		context.put("selectJenisPengenalanPasangan",
				dataUtil.getListJenisPengenalan());
		context.put("selectStatusPekerjaanPasangan",
				dataUtil.getListStatusPekerjaan());
		context.put("selectNegeriPasangan", dataUtil.getListNegeri());
		context.put("selectKelasPerkhidmatanPasangan",
				dataUtil.getListKelasPerkhidmatan());
		context.put("selectGredJawatanPasangan",
				dataUtil.getListGredPerkhidmatan());
		context.put("selectKementerianPasangan", dataUtil.getListKementerian());
		context.put("selectJawatanPasangan", dataUtil.getListJawatan());
		context.put("selectBadanBerkanunPasangan",
				dataUtil.getListBadanBerkanun());

		context.put("activity", "pasangan");

		context.remove("us");

		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			UsersSpouse usersSpouse = (UsersSpouse) mp
					.get("SELECT us FROM UsersSpouse us WHERE us.users.id = '"
							+ kp.getPemohon().getId() + "'");

			if (usersSpouse != null) {
				context.put("us",
						mp.find(UsersSpouse.class, usersSpouse.getId()));
			}

			context.put("users", mp.find(Users.class, kp.getPemohon().getId()));

			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion",
					getCompletionPermohonan(mp, getParam("idPermohonan")));

		} catch (Exception e) {
			System.out.println("Error getPasangan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/sub_page/pasangan.vm";
	}

	@Command("selectBandarPasangan")
	public String selectBandarPasangan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";

		if (get("idNegeriPasangan").trim().length() > 0) {
			idNegeri = get("idNegeriPasangan");
		}

		context.put("selectBandarPasangan", dataUtil.getListBandar(idNegeri));
		return getPath() + "/sub_page/pasangan/selectBandarPasangan.vm";
	}

	@Command("selectJabatanPasangan")
	public String selectJabatanPasangan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idKementerian = "0";

		if (get("idKementerianPasangan").trim().length() > 0) {
			idKementerian = get("idKementerianPasangan");
		}

		context.put("selectJabatanPasangan",
				dataUtil.getListAgensi(idKementerian));

		return getPath() + "/sub_page/pasangan/selectJabatanPasangan.vm";
	}

	@Command("simpanPasangan")
	public String simpanPasangan() {

		boolean success = false;
		boolean newRecord = false;
		int jenisAktiviti = 2;
		// String idPermohonan = getParam("idPermohonan");

		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			Users users = (Users) mp.find(Users.class, kp.getPemohon().getId());
			JenisPengenalan jenisPengenalan = (JenisPengenalan) mp.find(
					JenisPengenalan.class,
					getParam("idJenisPengenalanPasangan"));
			StatusPekerjaan statusPekerjaan = (StatusPekerjaan) mp.find(
					StatusPekerjaan.class,
					getParam("idStatusPekerjaanPasangan"));
			KelasPerkhidmatan kelasPerkhidmatan = (KelasPerkhidmatan) mp.find(
					KelasPerkhidmatan.class,
					getParam("idKelasPerkhidmatanPasangan"));
			GredPerkhidmatan gredJawatan = (GredPerkhidmatan) mp.find(
					GredPerkhidmatan.class, getParam("idGredJawatanPasangan"));
			Jawatan jawatan = (Jawatan) mp.find(Jawatan.class,
					getParam("idJawatanPasangan"));
			Agensi agensi = (Agensi) mp.find(Agensi.class,
					getParam("idJabatanPasangan"));
			BadanBerkanun badanBerkanun = (BadanBerkanun) mp.find(
					BadanBerkanun.class, getParam("idBadanBerkanunPasangan"));
			Bandar bandar = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPasangan"));

			UsersSpouse us = null;

			UsersSpouse usersSpouse = (UsersSpouse) mp
					.get("SELECT us FROM UsersSpouse us WHERE us.users.id = '"
							+ kp.getPemohon().getId() + "'");

			if (usersSpouse != null) {
				us = (UsersSpouse) mp.find(UsersSpouse.class,
						usersSpouse.getId());
			}

			if (us == null) {
				newRecord = true;
				jenisAktiviti = 1;
				us = new UsersSpouse();
			}

			us.setUsers(users);
			us.setNamaPasangan(getParam("namaPasangan"));
			us.setJenisPengenalan(jenisPengenalan);
			us.setNoKPPasangan(getParam("pasanganNoKP"));
			us.setStatusPekerjaanPasangan(statusPekerjaan);
			us.setJenisPekerjaan(getParam("pasanganJenisPekerjaan"));
			if (!"".equals(getParam("pasanganGaji"))) {
				us.setGajiPasangan(Double.parseDouble(getParam("pasanganGaji")
						.replaceAll("RM", "").replaceAll(",", "")));
			}
			us.setNamaSyarikat(getParam("pasanganSyarikat"));
			us.setAlamatPejabat1(getParam("pasanganAlamatKerja1"));
			us.setAlamatPejabat2(getParam("pasanganAlamatKerja2"));
			us.setAlamatPejabat3(getParam("pasanganAlamatKerja3"));
			us.setPoskodPejabat(getParam("pasanganPoskodKerja"));
			us.setBandarPejabat(bandar);
			us.setNoTelPejabat(getParam("pasanganNoTelKerja"));
			us.setNoFaksPejabat(getParam("pasanganNoFaksKerja"));
			us.setNoTelBimbit(getParam("pasanganNoTelBimbit"));
			us.setBadanBerkanun(badanBerkanun);
			us.setGredJawatan(gredJawatan);
			us.setJawatan(jawatan);
			us.setAgensi(agensi);
			us.setKelasPerkhidmatan(kelasPerkhidmatan);

			mp.begin();

			if (newRecord == true) {
				mp.persist(us);
			}

			kp.setPasangan(1);
			kp.setTarikhKemaskini(new Date());
			kuaUtil.kuartersLog(jenisAktiviti, "UsersSpouse",
					(Users) mp.find(Users.class, userId), us.getId());

			// db.commit(request, "PROCESSING FILE (Pasangan Penghuni) : "+
			// us.getId(), jenisAktiviti);
			mp.commit();

			success = true;

		} catch (Exception e) {
			System.out.println("Error simpanPasangan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		context.put("success", success);

		context.remove("resultPermohonanCompletion");
		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(mp, getParam("idPermohonan")));
		return getPath() + "/sub_page/result/simpanPasangan.vm";
	}

	/*--------------------------------------------------------------- DETAIL PINJAMAN ---------------------------------------------------------------*/

	@Command("getPinjaman")
	public String getPinjaman() throws Exception {

		context.put("selectNegeriPinjaman", dataUtil.getListNegeri());

		context.put("activity", "pinjaman");

		context.remove("kpp");

		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			KuaPinjamanPemohon kpp = (KuaPinjamanPemohon) mp
					.get("SELECT kpp FROM KuaPinjamanPemohon kpp WHERE kpp.users.id = '"
							+ kp.getPemohon().getId() + "'");

			if (kpp != null) {
				context.put("kpp",
						mp.find(KuaPinjamanPemohon.class, kpp.getId()));
			}

			context.put("users", mp.find(Users.class, kp.getPemohon().getId()));

			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion",
					getCompletionPermohonan(mp, getParam("idPermohonan")));

		} catch (Exception e) {
			System.out.println("Error getPinjaman : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/sub_page/pinjaman.vm";
	}

	@Command("selectBandarPinjaman")
	public String selectBandarPinjaman() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";

		if (get("idNegeriPinjaman").trim().length() > 0) {
			idNegeri = get("idNegeriPinjaman");
		}

		context.put("selectBandarPinjaman", dataUtil.getListBandar(idNegeri));
		return getPath() + "/sub_page/pinjaman/selectBandarPinjaman.vm";
	}

	@Command("simpanPinjaman")
	public String simpanPinjaman() {

		boolean success = false;
		boolean newRecord = false;
		int jenisAktiviti = 2;
		String idPermohonan = getParam("idPermohonan");

		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					idPermohonan);
			Users users = (Users) mp.find(Users.class, kp.getPemohon().getId());
			Bandar bandar = (Bandar) mp.find(Bandar.class,
					getParam("idBandarPinjaman"));

			KuaPinjamanPemohon kpp = null;

			KuaPinjamanPemohon pinjamanPemohon = (KuaPinjamanPemohon) mp
					.get("SELECT p FROM KuaPinjamanPemohon p WHERE p.users.id = '"
							+ kp.getPemohon().getId() + "'");

			if (pinjamanPemohon != null) {
				kpp = (KuaPinjamanPemohon) mp.find(KuaPinjamanPemohon.class,
						pinjamanPemohon.getId());
			}

			if (kpp == null) {
				newRecord = true;
				jenisAktiviti = 1;
				kpp = new KuaPinjamanPemohon();
			}

			Date t = null;
			String[] tarikhJangkaSiap = getParam("tarikhJangkaSiap").split(",");
			String tarikhJangkaSiapBaru = "01-"
					+ getMonth(tarikhJangkaSiap[0].trim()) + "-"
					+ tarikhJangkaSiap[1].trim();

			t = new SimpleDateFormat("dd-MM-yyyy").parse(tarikhJangkaSiapBaru);

			kpp.setUsers(users);
			kpp.setPinjamanPerumahan(getParamAsInteger("valuePinjamanPerumahan"));
			kpp.setStatusPembinaan(getParamAsInteger("valueStatusRumah"));
			kpp.setTarikhJangkaSiap(t);
			kpp.setAlamat1(getParam("alamatPinjaman1"));
			kpp.setAlamat2(getParam("alamatPinjaman2"));
			kpp.setAlamat3(getParam("alamatPinjaman3"));
			kpp.setPoskodPinjaman(getParam("poskodPinjaman"));
			kpp.setBandar(bandar);
			kpp.setJenisPerumahan(getParam("jenisPerumahan"));
			kpp.setPembiayaan(getParam("valuePembiayaan"));
			kpp.setPembelian(getParam("valuePembelian"));

			mp.begin();

			if (newRecord == true) {
				mp.persist(kpp);
			}

			if (getParamAsInteger("valuePinjamanPerumahan") == 0) {
				kp.setKelulusan3("true");
			}

			kp.setPinjaman(1);
			kp.setTarikhKemaskini(new Date());
			kuaUtil.kuartersLog(jenisAktiviti, "KuaPinjamanPemohon",
					(Users) mp.find(Users.class, kp.getPemohon().getId()),
					kpp.getId());

			// db.commit(request, "PROCESSING FILE (Pinjaman Penghuni) : "+
			// kpp.getId(), jenisAktiviti);
			mp.commit();

			success = true;

		} catch (Exception e) {
			System.out.println("Error simpanPinjaman : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		context.put("success", success);

		context.remove("resultPermohonanCompletion");
		context.put("resultPermohonanCompletion",
				getCompletionPermohonan(mp, getParam("idPermohonan")));
		return getPath() + "/sub_page/result/simpanPinjaman.vm";
	}

	@Command("getKelainanUpaya")
	public String getKelainanUpaya() {

		context.put("activity", "kelainanUpaya");

		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					getParam("idPermohonan"));
			context.put("users", mp.find(Users.class, kp.getPemohon().getId()));
			context.remove("resultPermohonanCompletion");
			context.put("resultPermohonanCompletion",
					getCompletionPermohonan(mp, getParam("idPermohonan")));

		} catch (Exception e) {
			System.out.println("Error getKelainanUpaya : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/sub_page/kelainanUpaya.vm";
	}

	@SuppressWarnings("unchecked")
	@Command("getRecordKelainanUpaya")
	public String getRecordKelainanUpaya() {
		try {
			mp = new MyPersistence();

			List<KuaKelainanUpaya> kku = mp
					.list("SELECT ku FROM KuaKelainanUpaya ku WHERE ku.permohonan.id = '"
							+ getParam("idPermohonan") + "'");
			context.put("kku", kku);

		} catch (Exception e) {
			System.out.println("Error getRecordKelainanUpaya : "
					+ e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/sub_page/kelainanUpaya/record.vm";
	}

	@Command("tambahKelainanUpaya")
	public String tambahKelainanUpaya() {
		return getPath() + "/sub_page/kelainanUpaya/start.vm";
	}

	@Command("deleteKelainanUpaya")
	public String deleteKelainanUpaya() {
		boolean success = false;

		try {
			mp = new MyPersistence();

			KuaKelainanUpaya kku = (KuaKelainanUpaya) mp.find(
					KuaKelainanUpaya.class, getParam("idKelainanUpaya"));

			mp.begin();
			if (kku != null) {
				mp.remove(kku);
			}

			kuaUtil.kuartersLog(3, "KuaKelainanUpaya",
					(Users) mp.find(Users.class, userId), kku.getId());

			// db.commit(request,
			// "PROCESSING FILE (Kelainan Upaya Penghuni) : "+ kku.getId(), 3);
			mp.commit();

			success = true;

		} catch (Exception e) {
			System.out.println("Error deleteKelainanUpaya : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		context.put("success", success);

		return getPath() + "/sub_page/kelainanUpaya/resultDelete.vm";
	}

	@Command("simpanKelainanUpaya")
	public String simpanKelainanUpaya() {
		boolean success = false;

		try {
			mp = new MyPersistence();

			KuaPermohonan permohonan = (KuaPermohonan) mp.find(
					KuaPermohonan.class, getParam("idPermohonan"));
			KuaKelainanUpaya kku = new KuaKelainanUpaya();

			kku.setNoKad(getParam("kelainanUpayaNoKad"));
			kku.setNoMyKad(getParam("kelainanUpayaNoKP"));
			kku.setHubungan(getParam("idHubunganKelainanUpaya"));
			kku.setImgName(getParam("imgNameKelainanUpaya"));
			kku.setAvatarName(getParam("avatarNameKelainanUpaya"));
			kku.setPermohonan(permohonan);

			mp.begin();
			mp.persist(kku);

			kuaUtil.kuartersLog(1, "KuaKelainanUpaya",
					(Users) mp.find(Users.class, userId), kku.getId());

			// db.commit(request,
			// "PROCESSING FILE (Kelainan Upaya Penghuni) : "+ kku.getId(), 1);
			mp.commit();

			success = true;

		} catch (Exception e) {
			System.out.println("Error simpanKelainanUpaya : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		context.put("success", success);

		return getPath() + "/sub_page/kelainanUpaya/result.vm";
	}

	@Command("uploadDoc")
	public String uploadDoc() throws Exception {
		String imgName = kuaUtil.uploadFile(request, "kelainanUpaya",
				getParam("idPermohonan"));
		context.put("imgName", imgName);
		context.put("avatarName",
				imgName.substring(0, imgName.lastIndexOf(".")) + "_thumbnail"
						+ imgName.substring(imgName.lastIndexOf(".")));

		return getPath() + "/sub_page/kelainanUpaya/uploaddoc.vm";
	}

	@Command("refreshList")
	public String refreshList() throws Exception {
		String imgName = getParam("imgName");
		String avatarName = getParam("avatarName");

		context.put("imgName", imgName);
		context.put("avatarName", avatarName);

		return getPath() + "/sub_page/kelainanUpaya/listDokumen.vm";
	}

	public int getNoGiliran(String kelasKuarters, String lokasi,
			MyPersistence mp) {
		// buat semula
		int i = 1;
		VW_KuaAgihan ka = (VW_KuaAgihan) mp
				.get("SELECT ka FROM VW_KuaAgihan ka WHERE ka.kelasKuarters = '"
						+ kelasKuarters
						+ "' AND ka.idLokasi = '"
						+ lokasi
						+ "'");
		if (ka != null) {
			i = ka.getMaxNoGiliran() + 1;
		}
		return i;
	}

	/*--------------------------------------------------------------- STATUS COMPLETION PERMOHONAN ---------------------------------------------------------------*/

	public boolean getCompletionPermohonan(MyPersistence mp, String idPermohonan) {
		boolean result = false;
		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					idPermohonan);
			if (kp != null) {
				if ("true".equals(kp.getKelulusan1())) {
					if ("true".equals(kp.getKelulusan2())) {
						if ("true".equals(kp.getKelulusan3())) {
							if (kp.getPekerjaan() == 1) {
								if (kp.getPeribadi() == 1) {
									if (kp.getPinjaman() == 1) {
										result = true;
										if (kp.getPemohon()
												.getStatusPerkahwinan() != null) {
											if ("02".equals(kp.getPemohon()
													.getStatusPerkahwinan()
													.getId())) {
												if (kp.getPasangan() == 1) {
													result = true;
												} else {
													result = false;
												}
											}
										}
									} else {
										result = false;
									}
								} else {
									result = false;
								}
							} else {
								result = false;
							}
						} else {
							result = false;
						}
					} else {
						result = false;
					}
				} else {
					result = false;
				}
			} else {
				result = false;
			}
		} catch (Exception e) {
			System.out.println("Error begin : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return result;
	}

	public boolean getStatusPerkahwinanPemohon(MyPersistence mp,
			String idPermohonan) {
		boolean result = false;
		try {
			mp = new MyPersistence();
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
					idPermohonan);
			if (kp != null && kp.getPemohon() != null
					&& kp.getPemohon().getStatusPerkahwinan() != null) {
				if ("02".equals(kp.getPemohon().getStatusPerkahwinan().getId())) {
					if (kp.getPasangan() == 1) {
						result = true;
					}
				}
			}
		} catch (Exception e) {
			System.out
					.println("Error getStatusPerkahwinan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return result;
	}

	public String getMonth(String month) {
		String result = "";
		if ("Jan".equals(month))
			result = "01";
		else if ("Feb".equals(month))
			result = "02";
		else if ("Mar".equals(month) || "Mac".equals(month))
			result = "03";
		else if ("Apr".equals(month))
			result = "04";
		else if ("May".equals(month) || "Mei".equals(month))
			result = "05";
		else if ("Jun".equals(month))
			result = "06";
		else if ("Jul".equals(month))
			result = "07";
		else if ("Aug".equals(month) || "Ogo".equals(month))
			result = "08";
		else if ("Sep".equals(month))
			result = "09";
		else if ("Oct".equals(month) || "Okt".equals(month))
			result = "10";
		else if ("Nov".equals(month))
			result = "11";
		else if ("Dec".equals(month) || "Dis".equals(month))
			result = "12";
		return result;
	}

	@SuppressWarnings("rawtypes")
	public String checkJarakLangitudeLotitude(MyPersistence mp) {
		String resultRadius = "";
		List array = new ArrayList();
		String alamat1 = getParam("alamatPinjaman1");
		String alamat2 = getParam("alamatPinjaman2");
		String alamat3 = getParam("alamatPinjaman3");
		String poskod = getParam("poskodPinjaman");
		String alamatRumah = "";
		// get point lokasi tempat kerja
		String mercuTanda = "";

		alamat1 = alamat1.trim().toLowerCase().replaceAll(" ", "+");
		alamat2 = alamat2.trim().toLowerCase().replaceAll(" ", "+");
		alamat3 = alamat3.trim().toLowerCase().replaceAll(" ", "+");
		alamatRumah = alamat1 + "+" + alamat2 + "+" + alamat3 + "+" + poskod;

		Bandar bandarPinjaman = (Bandar) mp.find(Bandar.class,
				getParam("idBandarPinjaman"));
		if (bandarPinjaman != null) {
			alamatRumah = alamatRumah
					+ "+"
					+ bandarPinjaman.getKeterangan().trim().toLowerCase()
							.replaceAll(" ", "+");
		}

		Negeri negeri = (Negeri) mp.find(Negeri.class,
				getParam("idNegeriPinjaman"));
		if (negeri != null) {
			alamatRumah = alamatRumah
					+ "+"
					+ negeri.getKeterangan().trim().toLowerCase()
							.replaceAll(" ", "+");
		}

		try {
			/* START SETTING FOR DEVELOPMENT/LOCAL */
			// array=KuartersUtils.getDistanceBetweenLocation(alamatRumah,mercuTanda);
			/* END SETTING FOR DEVELOPMENT/LOCAL */

			/* START SETTING FOR LIVE/STAGING/PAT */
			BPHServicesImplService service = new BPHServicesImplService();
			BPHServices bphService = service.getPort(BPHServices.class);

			String WS_URL = ResourceBundle.getBundle("dbconnection").getString(
					"WS_URL");
			// int connectionTimeOutInMs = 10000;// 10 Second
			BindingProvider p = (BindingProvider) bphService;
			p.getRequestContext().put(
					BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WS_URL);

			sbbph.ws.GoogleManager googleManagerService = null;
			googleManagerService = bphService.getDistanceBetweenLocation(
					alamatRumah, mercuTanda);
			array = googleManagerService.getArray();
			/* END SETTING FOR LIVE/STAGING/PAT */

		} catch (Exception ex) {
			myLogger.debug("ERROR (getListRangeMap) :::: " + ex.getMessage());
		}

		if (array != null) {
			try {
				Double lat1 = Double.parseDouble(array.get(0).toString());
				Double lon1 = Double.parseDouble(array.get(1).toString());
				// mercutanda
				LokasiPermohonan lokasiPermohonan = (LokasiPermohonan) mp.find(
						LokasiPermohonan.class, getParam("idLokasiPermohonan"));
				Double lat2 = lokasiPermohonan.getLat();
				Double lon2 = lokasiPermohonan.getLon();
				// Double
				// minRange=getDistanceFromLatLonInKm(lat1,lon1,lat2,lon2);
				Double minRange = getDistanceFromLatLonInKm(lat1, lon1, lat2,
						lon2);
				// Double minRange =
				// Double.parseDouble(Collections.min(array).toString());
				if (minRange > 25) {
					resultRadius = "true"; // checkRadius(Luar layak)
				} else {
					resultRadius = "false"; // checkRadius(Tak layak)
				}
			} catch (Exception e) {
				System.out.println("Error Google Map API : " + e.getMessage());
				resultRadius = "true";
			}
		}
		// context.put("result2", result2);
		// resultRadius = "true";
		return resultRadius;
	}

	public Double getDistanceFromLatLonInKm(Double lat1, Double lon1,
			Double lat2, Double lon2) {
		int R = 6371; // Radius of the earth in km
		double dLat = (lat2 - lat1) * (Math.PI / 180); // deg2rad below
		double dLon = (lon2 - lon1) * (Math.PI / 180);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(lat1 * (Math.PI / 180))
				* Math.cos(lat2 * (Math.PI / 180)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c; // Distance in km
		return d;
	}

	public void simpanAuditTrail(String idPermohonan, String flagProses) {
		mp = new MyPersistence();
		UsersActivity activity = new UsersActivity();
		KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class,
				idPermohonan);
		Users u = null;
		UsersJob uj = null;
		UsersSpouse us = null;

		if (kp != null) {
			u = (Users) mp.find(Users.class, kp.getPemohon().getId());
			uj = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ kp.getPemohon().getId() + "'");
			us = (UsersSpouse) mp
					.get("SELECT us FROM UsersSpouse us WHERE us.users.id = '"
							+ kp.getPemohon().getId() + "'");

			if (u != null) {
				activity.setUserLogin(u.getId());
				activity.setGelaran(u.getGelaran());
				activity.setUserName(u.getUserName());
				activity.setUserAddress(u.getUserAddress());
				activity.setUserAddress2(u.getUserAddress2());
				activity.setUserAddress3(u.getUserAddress3());
				activity.setUserPostcode(u.getUserPostcode());
				activity.setUserBandar(u.getUserBandar());
				activity.setNoKP(u.getNoKP());
				activity.setNoKP2(u.getNoKP2());
				activity.setNoTelefon(u.getNoTelefon());
				activity.setNoTelefonBimbit(u.getNoTelefonBimbit());
				activity.setNoTelefonPejabat(u.getNoTelefonPejabat());
				activity.setNoFaks(u.getNoFaks());
				activity.setEmel(u.getEmel());
				activity.setSeksyen(u.getSeksyen());
				activity.setKeteranganJawatan(u.getKeteranganJawatan());
				activity.setKelasPerkhidmatan(u.getKelasPerkhidmatan());
				activity.setGredPerkhidmatan(u.getGredPerkhidmatan());
				activity.setJenisPerkhidmatan(u.getJenisPerkhidmatan());
				activity.setAgensi(u.getAgensi());
				activity.setBahagian(u.getBahagian());
				activity.setBadanBerkanun(u.getBadanBerkanun());
				activity.setJantina(u.getJantina());
				activity.setBangsa(u.getBangsa());
				activity.setEtnik(u.getEtnik());
				activity.setAgama(u.getAgama());
				activity.setTarikhLahir(u.getTarikhLahir());
				activity.setStatusPerkahwinan(u.getStatusPerkahwinan());
				activity.setAlamat1(u.getAlamat1());
				activity.setAlamat2(u.getAlamat2());
				activity.setAlamat3(u.getAlamat3());
				activity.setPoskod(u.getPoskod());
				activity.setBandar(u.getBandar());
				activity.setJenisPengguna(u.getJenisPengguna());
				activity.setFlagAktif(u.getFlagAktif());
				activity.setFlagAnak(u.getFlagAnak());
				activity.setBilAnak(u.getBilAnak());
			}

			if (uj != null) {
				activity.setUjJawatan(uj.getJawatan());
				activity.setUjGredJawatan(uj.getGredJawatan());
				activity.setUjJenisPerkhidmatan(uj.getJenisPerkhidmatan());
				activity.setUjKelasPerkhidmatan(uj.getKelasPerkhidmatan());
				activity.setUjStatusPerkhidmatan(uj.getStatusPerkhidmatan());
				activity.setUjAgensi(uj.getAgensi());
				activity.setUjBahagian(uj.getBahagian());
				activity.setUjBadanBerkanun(uj.getBadanBerkanun());
				activity.setUjTarikhLantikan(uj.getTarikhLantikan());
				activity.setUjTarikhTamat(uj.getTarikhTamat());
				activity.setUjNoGaji(uj.getNoGaji());
				activity.setUjGajiPokok(uj.getGajiPokok());
				activity.setUjAlamat1(uj.getAlamat1());
				activity.setUjAlamat2(uj.getAlamat2());
				activity.setUjAlamat3(uj.getAlamat3());
				activity.setUjPoskod(uj.getPoskod());
				activity.setUjBandar(uj.getBandar());
				activity.setUjNoTelPejabat(uj.getNoTelPejabat());
				activity.setUjNoFaks(uj.getNoFaks());
				activity.setUjEmel(uj.getEmel());
				activity.setUjFlagITP(uj.getFlagITP());
				activity.setUjFlagEPW(uj.getFlagEPW());
				activity.setUjFlagCola(uj.getFlagCola());
				activity.setUjTarikhBersara(uj.getTarikhBersara());
			}

			if (us != null) {
				activity.setUsNamaPasangan(us.getNamaPasangan());
				activity.setUsJenisPengenalan(us.getJenisPengenalan());
				activity.setUsNoKPPasangan(us.getNoKPPasangan());
				activity.setUsStatusPekerjaanPasangan(us
						.getStatusPekerjaanPasangan());
				activity.setUsJenisPekerjaan(us.getJenisPekerjaan());
				activity.setUsGajiPasangan(us.getGajiPasangan());
				activity.setUsNamaSyarikat(us.getNamaSyarikat());
				activity.setUsAlamatPejabat1(us.getAlamatPejabat1());
				activity.setUsAlamatPejabat2(us.getAlamatPejabat2());
				activity.setUsAlamatPejabat3(us.getAlamatPejabat3());
				activity.setUsPoskodPejabat(us.getPoskodPejabat());
				activity.setUsBandarPejabat(us.getBandarPejabat());
				activity.setUsNoTelPejabat(us.getNoTelPejabat());
				activity.setUsNoFaksPejabat(us.getNoFaksPejabat());
				activity.setUsNoTelBimbit(us.getNoTelBimbit());
				activity.setUsJawatan(us.getJawatan());
				activity.setUsGredJawatan(us.getGredJawatan());
				activity.setUsKelasPerkhidmatan(us.getKelasPerkhidmatan());
				activity.setUsAgensi(us.getAgensi());
				activity.setUsBadanBerkanun(us.getBadanBerkanun());
			}

			if (flagProses == "hantarPermohonan") {
				activity.setReason("Hantar");
				activity.setMessage("Hantar permohonan kuarters");
			}

			if (flagProses == "kemaskiniPermohonan") {
				activity.setReason("Kemaskini");
				activity.setMessage("Kemaskini rekod permohonan kuarters");
			}

			if (flagProses == "simpanPeribadi") {
				activity.setReason("simpanPeribadi");
				activity.setMessage("Mengemaskini maklumat peribadi pemohon");
			}

			if (flagProses == "simpanPekerjaan") {
				activity.setReason("simpanPekerjaan");
				activity.setMessage("Mengemaskini maklumat pekerjaan pemohon");
			}

			if (flagProses == "simpanPasangan") {
				activity.setReason("simpanPasangan");
				activity.setMessage("Mengemaskini rekod pasangan pemohon");
			}

			if (flagProses == "simpanPinjaman") {
				activity.setReason("simpanPinjaman");
				activity.setMessage("Mengemaskini rekod pinjaman pemohon");
			}

			if (flagProses == "kemaskiniPermohonanDalaman") {
				activity.setReason("kemaskiniPermohonanDalaman");
				activity.setMessage("(INTERNAL)Kemaskini rekod permohonan kuarters");
			}

			if (flagProses == "simpanPeribadiDalaman") {
				activity.setReason("simpanPeribadiDalaman");
				activity.setMessage("(INTERNAL)Mengemaskini maklumat peribadi pemohon");
			}

			if (flagProses == "simpanPekerjaanDalaman") {
				activity.setReason("simpanPekerjaanDalaman");
				activity.setMessage("(INTERNAL)Mengemaskini maklumat pekerjaan pemohon");
			}

			if (flagProses == "simpanPasanganDalaman") {
				activity.setReason("simpanPasanganDalaman");
				activity.setMessage("(INTERNAL)Mengemaskini rekod pasangan pemohon");
			}

			if (flagProses == "simpanPinjamanDalaman") {
				activity.setReason("simpanPinjamanDalaman");
				activity.setMessage("(INTERNAL)Mengemaskini rekod pinjaman pemohon");
			}

			if (flagProses == "tolakPermohonan") {
				activity.setReason("tolakPermohonan");
				activity.setMessage("Permohonan kuarters ditolak");
			}

			if (flagProses == "simpanCatatan") {
				activity.setReason("simpanCatatan");
				activity.setMessage("simpanCatatan");
			}

			if (flagProses == "hapusCatatan") {
				activity.setReason("hapusCatatan");
				activity.setMessage("hapusCatatan");
			}

			activity.setTarikhKemaskini(new Date());
			UsersActivity ua = (UsersActivity) mp
					.get("SELECT ua FROM UsersActivity ua WHERE ua.userLogin = '"
							+ kp.getPemohon().getId()
							+ "' ORDER BY ua.turutan DESC");
			if (ua != null) {
				int turutan = ua.getTurutan() + 1;
				activity.setTurutan(turutan);
			} else {
				int turutan = 0;
				activity.setTurutan(turutan);
			}
			activity.setFlagDowngrade(kp.getFlagDowngrade());
			activity.setFlagTuntutan(kp.getFlagTuntutan());
			activity.setTarikhPermohonan(kp.getTarikhPermohonan());
			activity.setLokasiPermohonan(kp.getLokasi());
			activity.setStatus(kp.getStatus());
			mp.begin();
			mp.persist(activity);
			try {
				mp.commit();
			} catch (Exception e) {
				System.out.println("Error kemaskiniPermohonan : "
						+ e.getMessage());
			} finally {
				if (mp != null) {
					mp.close();
				}
			}
		}
	}

	public synchronized String generateNoPermohonan(MyPersistence mp) {
		Calendar calCurrent = new GregorianCalendar();
		calCurrent.setTime(new Date());
		int bulan = calCurrent.get(Calendar.MONTH) + 1;
		int tahun = calCurrent.get(Calendar.YEAR);
		int counter = 0;

		String month = new DecimalFormat("00").format(bulan);
		String year = new DecimalFormat("0000").format(tahun);
		String key = year + month;

		KuaSequencePermohonan seq = (KuaSequencePermohonan) mp
				.get("select x from KuaSequencePermohonan x where x.id = '"
						+ key + "'");

		// mp.begin();
		if (seq != null) {
			mp.pesismisticLock(seq);
			counter = seq.getBilangan() + 1;
			seq.setBilangan(counter);
			seq = (KuaSequencePermohonan) mp.merge(seq);
		} else {
			counter = 1;
			seq = new KuaSequencePermohonan();
			seq.setId(key);
			seq.setBilangan(counter);
			mp.persist(seq);
			mp.flush();
		}
		// mp.commit();

		String formatserial = new DecimalFormat("0000").format(counter);
		String noPermohonan = key + formatserial;
		return noPermohonan;
	}

	@Command("getMaklumatKuarters")
	public String getMaklumatKuarters() throws Exception {
		String idPenghuni = get("idPenghuni").trim();
		try {
			mp = new MyPersistence();
			KuaPenghuni penghuni = (KuaPenghuni) mp
					.get("SELECT ua FROM KuaPenghuni ua WHERE ua.pemohon.id = '"
							+ idPenghuni + "' and ua.tarikhKeluar is null");
			if (penghuni != null) {
				KuaKuarters kuarters = (KuaKuarters) mp.find(KuaKuarters.class,
						penghuni.getKuarters().getId());
				context.put("kuarters", kuarters);
			}
		} catch (Exception e) {
			System.out.println("Error getMaklumatKuarters : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/entry_page.vm";
	}
}
