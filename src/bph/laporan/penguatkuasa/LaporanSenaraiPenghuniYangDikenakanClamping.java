package bph.laporan.penguatkuasa;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiPenghuniYangDikenakanClamping extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiPenghuniYangDikenakanClamping.class);
	public LaporanSenaraiPenghuniYangDikenakanClamping() {
		
		
		super.setFolderName("penguatkuasa");
		super.setReportName("LaporanSenaraiPenghuniYangDikenakanClamping");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
