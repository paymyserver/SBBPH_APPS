package bph.laporan.rpp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanDaftarPemohonDanPermohonan extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanDaftarPemohonDanPermohonan.class);
	public LaporanDaftarPemohonDanPermohonan() {
		
		
		super.setFolderName("rpp");
		super.setReportName("LaporanDaftarPemohonDanPermohonan");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}