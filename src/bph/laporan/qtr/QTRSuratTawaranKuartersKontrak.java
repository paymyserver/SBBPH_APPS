package bph.laporan.qtr;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class QTRSuratTawaranKuartersKontrak extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(QTRSuratTawaranKuartersKontrak.class);
	public QTRSuratTawaranKuartersKontrak() {
		
		
		super.setFolderName("kuarters");
		super.setReportName("QTRSuratTawaranKuartersKontrak");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
