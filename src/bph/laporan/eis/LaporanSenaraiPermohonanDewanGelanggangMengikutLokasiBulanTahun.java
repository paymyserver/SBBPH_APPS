package bph.laporan.eis;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;

public class LaporanSenaraiPermohonanDewanGelanggangMengikutLokasiBulanTahun extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(LaporanSenaraiPermohonanDewanGelanggangMengikutLokasiBulanTahun.class);
	public LaporanSenaraiPermohonanDewanGelanggangMengikutLokasiBulanTahun() {
		
		
		super.setFolderName("eis");
		super.setReportName("LaporanSenaraiPermohonanDewanGelanggangMengikutLokasiBulanTahun");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
	
}
