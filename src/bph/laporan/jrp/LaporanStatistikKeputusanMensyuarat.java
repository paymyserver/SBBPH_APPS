package bph.laporan.jrp;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanStatistikKeputusanMensyuarat extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanStatistikKeputusanMensyuarat.class);
	public LaporanStatistikKeputusanMensyuarat() {
		
		
		super.setFolderName("jrp");
		super.setReportName("LaporanStatistikKeputusanMensyuarat");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
