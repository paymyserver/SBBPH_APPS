package bph.laporan.eis;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanTempahanRppKelompok extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanTempahanRppKelompok.class);
	public LaporanTempahanRppKelompok() {
		
		
		super.setFolderName("eis");
		super.setReportName("LAPORANTEMPAHANRPP_KELOMPOK");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
