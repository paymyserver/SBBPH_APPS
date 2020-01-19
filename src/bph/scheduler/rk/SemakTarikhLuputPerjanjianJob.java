package bph.scheduler.rk;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import bph.entities.rk.RkFail;
import bph.modules.rk.UtilRk;
import db.persistence.MyPersistence;

public class SemakTarikhLuputPerjanjianJob implements Job {

	static Logger myLogger = Logger.getLogger("SemakTarikhLuputPerjanjianJob");
	private MyPersistence mp;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing SemakTarikhLuputPerjanjianJob on : " + new Date());
		System.out.println("Executing SemakTarikhLuputPerjanjianJob on : " + new Date());
		
		try {
			mp = new MyPersistence();
			List<RkFail> listFail = mp.list("select x from RkFail x where x.flagAktifPerjanjian = 'Y' order by x.id asc");
			for (int i = 0; i < listFail.size(); i++) {
				RkFail fail = listFail.get(i);
				if (fail != null) {
					UtilRk.semakTarikhLuputPerjanjian(fail, mp);
				}
			}	
			
			UtilRk.updateStatusSewaRuangKomersil(mp);			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		myLogger.info("Finish SemakTarikhLuputPerjanjianJob on : " + new Date());
		System.out.println("Finish SemakTarikhLuputPerjanjianJob on : " + new Date());
	}
}
