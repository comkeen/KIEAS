package kr.ac.uos.ai.ieas.db.dbHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import kr.ac.uos.ai.ieas.db.dbModel.CAPAlert;
import kr.ac.uos.ai.ieas.db.dbModel.CAPArea;
import kr.ac.uos.ai.ieas.db.dbModel.CAPInfo;
import kr.ac.uos.ai.ieas.db.dbModel.CAPResource;

public class CAPDBInsertUtils {

	private static String INSERT_ALERT = "INSERT INTO `alert`\n"
			+ "(`identifier`, `sender`, `sent`, `status`, `msgType`, `source`, `scope`, "
			+ "`restriction`, `addresses`, `code`, `note`, `references`, `incidents`"
			+ ") VALUES \n(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private static String INSERT_INFO = "INSERT INTO `info`\n"
			+ "(`language`, `cagetory`, `event`, `responseType`, `urgency`, `severity`, `certainty`, "
			+ "`audience`, `eventCode`, `effective`, `onset`, `expires`, `senderName`, `headline`, "
			+ "`description`, `instruction`, `web`, `contact`, `parameter`, `alert_eid`"
			+ ") VALUES \n(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	public void insertCAP(CAPAlert alert)
	{
		try {
			DataTransaction transaction = new DataTransaction(true);
			Connection conn = transaction.connection;

			this.insertAlert(conn, alert);
			int alert_eid = new CAPDBUtils().getAlertEidByObject(alert);

			if (null != alert.getInfoList()) {
				for (CAPInfo info : alert.getInfoList()) {
					info.setAlert_eid(alert_eid);
					System.out.println(alert_eid);
					this.insertInfo(conn, info);
					if (null != info.getAreaList()) {
						for (CAPArea area : info.getAreaList()) {
							this.insertArea(conn, area);
						}
					}
					if (null != info.getResList()) {
						for (CAPResource res : info.getResList()) {
							this.insertResource(conn, res);
						}
					}
				}
			}

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// For methods below, it automatically checks if elements are null for
	// fields
	// whose values can be null.

	private void insertAlert(Connection conn, CAPAlert alert) {
		try {
			PreparedStatement pstmtAlert = conn.prepareStatement(CAPDBInsertUtils.INSERT_ALERT);

			// #1: identifier
			pstmtAlert.setString(1, alert.getIdentifier());

			// #2: sender
			pstmtAlert.setString(2, alert.getSender());

			// #3: sent
			pstmtAlert.setString(3, DataFormatUtils.convertDateObjectType(alert.getSent()));

			// #4: status
			if (null == alert.getStatus()) {
				pstmtAlert.setNull(4, java.sql.Types.VARCHAR);
			} else {
				pstmtAlert.setString(4, alert.getStatus().name());
			}

			// #5: msgType
			pstmtAlert.setString(5, alert.getMsgType().name());

			// #6: source
			if (null == alert.getSource()) {
				pstmtAlert.setNull(6, java.sql.Types.VARCHAR);
			} else {
				pstmtAlert.setString(6, alert.getSource());
			}

			// #7: scope
			pstmtAlert.setString(7, alert.getScope().name());

			// #8: restriction
			if (null == alert.getRestriction()) {
				pstmtAlert.setNull(8, java.sql.Types.VARCHAR);
			} else {
				pstmtAlert.setString(8, alert.getRestriction());
			}

			// #9: addresses
			if (null == alert.getAddresses()) {
				pstmtAlert.setNull(9, java.sql.Types.VARCHAR);
			} else {
				pstmtAlert.setString(9, alert.getRestriction());
			}

			// #10: code
			if (null == alert.getCode()) {
				pstmtAlert.setNull(10, java.sql.Types.VARCHAR);
			} else {
				pstmtAlert.setString(10, alert.getCode());
			}

			// #11: note
			if (null == alert.getNote()) {
				pstmtAlert.setNull(11, java.sql.Types.VARCHAR);
			} else {
				pstmtAlert.setString(11, alert.getNote());
			}

			// #12: references
			if (null == alert.getReferences()) {
				pstmtAlert.setNull(12, java.sql.Types.VARCHAR);
			} else {
				pstmtAlert.setString(12, alert.getReferences());
			}

			// #13: incidents
			if (null == alert.getIncidents()) {
				pstmtAlert.setNull(13, java.sql.Types.VARCHAR);
			} else {
				pstmtAlert.setString(13, alert.getIncidents());
			}

			System.out.println(pstmtAlert.toString());
			pstmtAlert.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void insertInfo(Connection conn, CAPInfo info) {
		try {
			PreparedStatement pstmtInfo = conn.prepareStatement(CAPDBInsertUtils.INSERT_INFO);

			// #1: language
			if (null == info.getLanguage()) {
				pstmtInfo.setNull(1, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(1, info.getLanguage().name());
			}

			// #2: category
			pstmtInfo.setString(2, info.getCategory().name());

			// #3: event
			pstmtInfo.setString(3, info.getEvent());

			// #4: responseType
			if (null == info.getResponseType()) {
				pstmtInfo.setNull(4, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(4, info.getResponseType().name());
			}

			// #5: urgency
			pstmtInfo.setString(5, info.getUrgency().name());

			// #6 : severity
			pstmtInfo.setString(6, info.getSeverity().name());

			// #7: certainty
			pstmtInfo.setString(7, info.getCertainty().name());

			// #8: audience
			if (null == info.getAudience()) {
				pstmtInfo.setNull(8, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(8, info.getAudience());
			}

			// #9: eventCode
			if (null == info.getEventCode()) {
				pstmtInfo.setNull(9, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(9, info.getEventCode());
			}

			// #10: effective
			if (null == info.getEffective()) {
				pstmtInfo.setNull(10, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(10, DataFormatUtils.convertDateObjectType(info.getEffective()));
			}

			// #11: onset
			if (null == info.getOnset()) {
				pstmtInfo.setNull(11, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(11, DataFormatUtils.convertDateObjectType(info.getOnset()));
			}

			// #12: expires
			if (null == info.getExpires()) {
				pstmtInfo.setNull(12, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(12, DataFormatUtils.convertDateObjectType(info.getOnset()));
			}

			// #13: senderName
			if (null == info.getSenderName()) {
				pstmtInfo.setNull(13, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(13, info.getSenderName());
			}

			// #14: headline
			if (null == info.getHeadline()) {
				pstmtInfo.setNull(14, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(14, info.getHeadline());
			}

			// #15: description
			if (null == info.getDescription()) {
				pstmtInfo.setNull(15, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(15, info.getDescription());
			}

			// #16: instruction
			if (null == info.getInstruction()) {
				pstmtInfo.setNull(16, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(16, info.getInstruction());
			}

			// #17: Web
			if (null == info.getWeb()) {
				pstmtInfo.setNull(17, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(17, info.getWeb());
			}

			// #18: contact
			if (null == info.getContact()) {
				pstmtInfo.setNull(18, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(18, info.getContact());
			}

			// #19: parameter
			if (null == info.getParameter()) {
				pstmtInfo.setNull(19, java.sql.Types.VARCHAR);
			} else {
				pstmtInfo.setString(19, info.getParameter());
			}

			// #20: alert_eid
			pstmtInfo.setInt(20, 1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void insertArea(Connection conn, CAPArea area) {

	}

	private void insertResource(Connection conn, CAPResource res) {

	}

	// test code
	public static void main(String[] args) {
		CAPAlert alert = new CAPAlert();
		alert.setIdentifier("국민안전처1200000011");
		alert.setSender("김철수@mpss.go.kr");
		alert.setStatus(CAPAlert.Status.System);
		alert.setMsgType(CAPAlert.MsgType.Alert);
		alert.setScope(CAPAlert.Scope.Private);
		alert.setSent(new Date());
		CAPDBInsertUtils util = new CAPDBInsertUtils();
		util.insertCAP(alert);
	}

}
