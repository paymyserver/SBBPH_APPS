/**
 * 
 */
package bph.modules.eis.rpp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.rpp.RppPeranginan;
import bph.modules.eis.EisLaporan;
import bph.utils.DataUtil;
import bph.utils.Util;

/**
 * @author rzai
 * 
 */
public class SenaraiTempahanRPPMengikutWaktuPuncak extends LebahModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	
	@Override
	public String start() {
		// TODO Auto-generated method stub
		DbPersistence db = new DbPersistence();
		dataUtil = DataUtil.getInstance(db);
		context.put("xml", generateXML());
		context.put("util", new Util());
		context.put("selectedTab", 0);
		context.put("path", getPath());
		context.put("laporan", "waktu_puncak");
		List<RppPeranginan> listJenisPeranginan = dataUtil.getListPeranginanRpp();
		context.put("listJenisPeranginan", listJenisPeranginan);
		return getPath() + "/start.vm";
	}

	private String getTajukLaporan() {
		String tajukLaporan = "Bilangan Permohonan Rumah Peranginan Mengikut Waktu Puncak / Luar Puncak";
		context.put("tajukLaporan", tajukLaporan);
		return tajukLaporan;
	}

	private String getXAxisName() {
		return "Bulan";
	}

	private String getYAxisName() {
		return "Bilangan";
	}

	private String getSQL() {
		String tahun = getParam("yearStart");
		String statusPuncak = getParam("status_puncak");
		String idRPP = getParam("idPeranginan");
		StringBuffer sb = new StringBuffer();
		sb.append("select month(rpp_permohonan.tarikh_masuk_rpp) as x, count(rpp_peranginan.id) as y ");
		sb.append("from rpp_akaun join rpp_permohonan on rpp_akaun.id_permohonan = rpp_permohonan.id join rpp_peranginan on rpp_permohonan.id_peranginan = rpp_peranginan.id ");
		sb.append("where year(rpp_permohonan.tarikh_masuk_rpp) is not null ");
		if (!"".equalsIgnoreCase(tahun)){
			sb.append("and year(rpp_permohonan.tarikh_masuk_rpp) = '"+tahun+"' ");
		}else{
			sb.append("and year(rpp_permohonan.tarikh_masuk_rpp) = '2017' ");	
		}
		if (!"0".equalsIgnoreCase(idRPP)){
			sb.append("and rpp_permohonan.id_peranginan = '"+idRPP+"' ");
		}else{
	
		}
		if (!"".equalsIgnoreCase(statusPuncak)){
			if(statusPuncak.equalsIgnoreCase("Waktu Puncak")){
				sb.append("and rpp_permohonan.flag_waktu_puncak = '"+"Y"+"' ");
			}else if(statusPuncak.equalsIgnoreCase("Luar Puncak")){
				sb.append("and rpp_permohonan.flag_waktu_puncak = '"+"T"+"' ");
			}			
		}else{
	
		}
		sb.append("group by month(rpp_permohonan.tarikh_masuk_rpp)");
		return sb.toString();
	}

	private String getPath() {
		return "bph/modules/eis/rpp/laporanKutipanRPPMengikutCawangan";
	}

	public String generateXML() {
		context.put("laporan", "waktu_puncak");
		String xml = "<chart caption='" + getTajukLaporan()
				+ "' subcaption='' xAxisName='" + getXAxisName()
				+ "' yAxisName='" + getYAxisName()
				+ "' numberPrefix='' showLegend='1'>";

		Db db = null;
		try {
			db = new Db();
			Connection conn = db.getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(getSQL());
			List<Object> listLaporan = new ArrayList<Object>();
			while (rs.next()) {
				xml = xml + "<set label = '" + rs.getString("x")
						+ "' value = '" + rs.getString("y") + "' />";
				

				//LAPORAN STATISTIK
				EisLaporan laporan = new EisLaporan();
				laporan.setX(rs.getString("x"));
				laporan.setY(rs.getString("y"));
				listLaporan.add(laporan);


			}
			xml = xml + "</chart>";
			context.put("listLaporan", listLaporan);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null)
				db.close();
		}
		return xml;
	}

	/** START SENARAI TAB **/
	@Command("getStatistik")
	public String getStatistik() 
	{
		context.put("xml", generateXML());
		context.put("util", new Util());
		context.put("selectedTab", 0);
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}
	@Command("janaLaporan")
	public String janaLaporan() {
		context.put("xml", generateXML());
		context.put("selectedTab", getParam("selectedTab"));
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}

	@Command("getBar")
	public String getBar() {
		context.put("xml", generateXML());
		context.put("selectedTab", 1);
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}

	@Command("getDoughnut")
	public String getDoughnut() {
		context.put("xml", generateXML());
		context.put("selectedTab", 2);
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}

	@Command("getLine")
	public String getLine() {
		context.put("xml", generateXML());
		context.put("selectedTab", 3);
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}

	@Command("getPie")
	public String getPie() {
		context.put("xml", generateXML());
		context.put("selectedTab", 4);
		context.put("path", getPath());
		return getPath() + "/senaraiTab.vm";
	}
	/** END SENARAI TAB **/
}
