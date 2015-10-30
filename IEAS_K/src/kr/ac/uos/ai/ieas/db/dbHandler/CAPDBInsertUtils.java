package kr.ac.uos.ai.ieas.db.dbHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import kr.ac.uos.ai.ieas.db.dbModel.CAPAlert;

public class CAPDBInsertUtils {

	private static String INSERT_ALERT = "INSERT INTO `alert`\n"
			+ "(`identifier`, `sender`, `sent`, `status`, `msgType`, `source`, `scope`, "
			+ "`restriction`, `addresses`, `code`, `note`, `references`, `incidents`"
			+ ") VALUES \n(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	public void insertCAP(CAPAlert alert) {
		try {
			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;

			this.insertAlert(conn, alert);
			this.insertInfo(conn, alert);
			this.insertResource(conn, alert);
			this.insertArea(conn, alert);

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void insertAlert(Connection conn, CAPAlert alert) {
		try {
			PreparedStatement pstmtAlert = conn
					.prepareStatement(CAPDBInsertUtils.INSERT_ALERT);
			pstmtAlert.setString(1, alert.getIdentifier());
			pstmtAlert.setString(2, alert.getSender());
			pstmtAlert.setDate(3, convertDateObjectType(alert.getSent()));
			
			if (null == alert.getStatus())
				{ pstmtAlert.setNull(4, java.sql.Types.VARCHAR); }
			else
				{ pstmtAlert.setString(4, alert.getStatus().name()); }
			
			pstmtAlert.setString(5, alert.getMsgType().name());
			
			if (null == alert.getSource())
				{ pstmtAlert.setNull(6, java.sql.Types.VARCHAR); }
			else
				{ pstmtAlert.setString(6, alert.getSource()); }
			
			
			pstmtAlert.setString(7, alert.getScope().name());
			
			if (null == alert.getRestriction())
				{ pstmtAlert.setNull(8, java.sql.Types.VARCHAR); }
			else
				{ pstmtAlert.setString(8, alert.getRestriction()); }
			
			if (null == alert.getAddresses())
				{ pstmtAlert.setNull(9, java.sql.Types.VARCHAR); }
			else 
				{ pstmtAlert.setString(9, alert.getRestriction()); }
			
			if (null == alert.getCode())
				{ pstmtAlert.setNull(10, java.sql.Types.VARCHAR); }
			else
				{ pstmtAlert.setString(10, alert.getCode()); }
			
			if (null == alert.getNote())
				{ pstmtAlert.setNull(11, java.sql.Types.VARCHAR); }
			else
				{ pstmtAlert.setString(11, alert.getNote()); }
			
			if (null == alert.getReferences())
				{ pstmtAlert.setNull(12, java.sql.Types.VARCHAR); }
			else
				{ pstmtAlert.setString(12, alert.getReferences()); }
			
			if (null == alert.getIncidents())
				{ pstmtAlert.setNull(13, java.sql.Types.VARCHAR); }
			else
				{ pstmtAlert.setString(13, alert.getIncidents()); }

			System.out.println(pstmtAlert.toString());
			pstmtAlert.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private java.sql.Date convertDateObjectType(java.util.Date sent) {
		java.util.Calendar cal = Calendar.getInstance();
		java.util.Date utilDate = new java.util.Date(); // your util date
		cal.setTime(utilDate);
		java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
		return sqlDate;
	}

	private void insertInfo(Connection conn, CAPAlert alert) {
	}

	private void insertArea(Connection conn, CAPAlert alert) {

	}

	private void insertResource(Connection conn, CAPAlert alert) {

	}

//	public static void main(String[] args) {
//		CAPAlert alert = new CAPAlert();
//		alert.setIdentifier("국민안전처0000000001");
//		alert.setSender("김철수@mpss.go.kr");
//		alert.setStatus(CAPAlert.Status.System);
//		alert.setMsgType(CAPAlert.MsgType.Alert);
//		alert.setScope(CAPAlert.Scope.Private);
//		alert.setSent(new Date());
//		CAPDBInsertUtils util = new CAPDBInsertUtils();
//		util.insertCAP(alert);
//	}

}
