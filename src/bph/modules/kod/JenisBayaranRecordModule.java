package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.JenisBayaran;

public class JenisBayaranRecordModule extends
		LebahRecordTemplateModule<JenisBayaran> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(JenisBayaran r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean delete(JenisBayaran r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/jenisBayaran";
	}

	@Override
	public Class<JenisBayaran> getPersistenceClass() {
		// TODO Auto-generated method stub
		return JenisBayaran.class;
	}

	@Override
	public void getRelatedData(JenisBayaran r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(JenisBayaran r) throws Exception {
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
		r.setAmaun(getParamAsDouble("amaun"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String find_id = get("find_id");
		String find_keterangan = get("find_keterangan");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);
		map.put("keterangan", find_keterangan);

		return map;
	}
}
