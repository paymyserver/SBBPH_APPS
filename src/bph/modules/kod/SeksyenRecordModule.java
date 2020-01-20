package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.Seksyen;

public class SeksyenRecordModule extends LebahRecordTemplateModule<Seksyen> {

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
	public void afterSave(Seksyen r) {
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
	public boolean delete(Seksyen r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/seksyen";
	}

	@Override
	public Class<Seksyen> getPersistenceClass() {
		// TODO Auto-generated method stub
		return Seksyen.class;
	}

	@Override
	public void getRelatedData(Seksyen r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(Seksyen r) throws Exception {
		r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
		r.setFlagAktif(get("flagAktif"));
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