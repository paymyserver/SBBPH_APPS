package bph.modules.rk;

import portal.module.entity.Users;

public class SenaraiRuangKomersilUKKRecordModule extends SenaraiRuangKomersilRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doOverideFilterRecord() {
		clearFilter();
		Users users = db.find(Users.class, userId);
		
		this.addFilter("seksyen.id = '07'");
		
		this.setOrderBy("idRuang");
		this.setOrderType("asc");		
		
		boolean showSeksyen = false;
		context.put("showSeksyen", showSeksyen);
		context.put("idSeksyen", "07");
	}
}
