package kr.ac.uos.ai.ieas.db.dbModel;

public enum CAPTable {
	
	ALERT("alert"),
	INFO("info"),
	RESOURCE("resource"),
	AREA("area");
	
	private String tableName;
	
	private CAPTable(String codename) {
		this.tableName = codename;
	}
	
	public String getTableName() {
		return tableName;
	}
}
