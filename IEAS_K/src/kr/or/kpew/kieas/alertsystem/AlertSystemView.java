package kr.or.kpew.kieas.alertsystem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.Profile.AlertSystemType;

public class AlertSystemView implements Observer {
	private AlertSystemController controller;

	private JFrame frame;
	private Container alertPane;
	private GridBagConstraints gbc;
	private JTextArea alertArea;
	private JScrollPane alertAreaPane;
	private JPanel buttonPane;
	private JTabbedPane mainTabbedPane;

	private JTextField systemType;
	private AlertSystemProfile profile;

	private JTextArea target;


	public AlertSystemView(AlertSystemProfile profile) {
		this.profile = profile;
	}
	
	public void show() {
		frame.setVisible(true);
	}

	public void init() {
		initLookAndFeel();
		initFrame();
		
	}

	public void setController(AlertSystemController controller) {
		this.controller = controller;
	}

	private void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void initFrame() {
		this.frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(controller);
		

		this.gbc = new GridBagConstraints();
		initAlertPane();
		initButtonPane();
		
		this.mainTabbedPane = new JTabbedPane();
		mainTabbedPane.addTab("경보메시지", alertPane);
		
		JPanel projectPanel = createProjectPanel();
		mainTabbedPane.addTab("프로젝트", projectPanel);

		
		Container container = frame.getContentPane();
		container.add(mainTabbedPane);

		frame.setSize(400, 400);
//		frame.setLocation(IntegratedAlertSystemMain.xLocation, IntegratedAlertSystemMain.yLocation);
//		IntegratedAlertSystemMain.xLocation += IntegratedAlertSystemMain.xIncrement;
//		IntegratedAlertSystemMain.yLocation += IntegratedAlertSystemMain.yIncrement;
		frame.setPreferredSize(new Dimension(512, 256));
	}

	private JPanel createProjectPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		StringBuffer sb = new StringBuffer();
		sb.append("1.다중언어").append("(쉬움)").append("\n").
		append("2.지역/수신기유형 맞춤").append("(쉬움)").append("\n").
		append("3.이미지/오디오 처리").append("(중간)").append("\n").
		append("4.파일/DB 저장").append("(어려움)").append("\n").
		append("5.비디오 처리").append("(어려움)").append("\n").
		append("6.자막 처리").append("(어려움)").append("\n");
		
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		ta.setText(sb.toString());
		panel.add(new JLabel("다음 중 하나 해보자?"));
		panel.add(ta);
		
		target = new JTextArea();
		ta.setEditable(false);
		panel.add(new JLabel("표출"));
		panel.add(target);
		
		return panel;
	}

	private void initAlertPane() {
		alertPane = new JPanel();
		alertPane.setLayout(new GridBagLayout());

		alertArea = new JTextArea(5, 20);
		alertAreaPane = new JScrollPane(alertArea);

		alertArea.setText("");

		gbc.fill = GridBagConstraints.BOTH;
		setGbc(0, 0, 1, 1, 1, 8);
		alertPane.add(alertAreaPane, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		setGbc(0, 1, 1, 1, 1, 2);

	}

	private void initButtonPane() {
		this.buttonPane = new JPanel();
		
		systemType = new JTextField(15);
		systemType.setEnabled(false);
		systemType.setText("<경보시스템 종류>");

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(controller);
		buttonPane.add(clearButton, BorderLayout.WEST);

		buttonPane.add(systemType, BorderLayout.WEST);

		alertPane.add(buttonPane, gbc);
	}


	private void setGbc(int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
	}

	public void systemExit() {
		String question = "표준경보시스템 프로그램을 종료하시겠습니까?";
		String title = "프로그램 종료";

		if (JOptionPane.showConfirmDialog(frame, question, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			System.exit(0);
		} else {
			System.out.println("AS: cancel exit program");
		}
	}

	//model의 notifyObservers(Object obj)에 의해 호출되는 함수. obj를 처리하면 된다.
	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof String) {	
			//실제 view에서 하는 일
			alertArea.setText((String)arg);
			
			//CAP 메시지를 이용하는 기능들 구현해보자
			IKieasMessageBuilder alert = new KieasMessageBuilder();
			alert.parse((String)arg);
			
			StringBuffer sb = new StringBuffer();
			//1.다중언어
			String language = profile.getLanguage(); //수신기 언어
			for(int i = 0; i < alert.getInfoCount(); i++) {
				if(language.equals(alert.getLanguage(i))) {
					sb.setLength(0);
					sb.append("수신기 언어: ").append(language).append("\n").
					append("경보내용: ").append("\n").
					append(alert.getDescription(i));
					
					String text = sb.toString();
					target.setText(text);
					System.out.println(text);
				}
			}
			
			//2.지역/수신기맞춤
			String geo = profile.getAgency(); //수신기 지역명
//			String geo = profile.getGeoCode(); 지역코드로도 가능
			for(int i = 0; i < alert.getAreaCount(0); i++) {
				if(geo.equals(alert.getAreaDesc(0, i))) {
					sb.setLength(0);
					sb.append("수신기 지역: ").append(geo).append("\n").
					append("경보내용: ").append("\n").
					append(alert.getDescription(i));
					
					String text = sb.toString();
					target.setText(text);
					System.out.println(text);
				}
			}
			
			AlertSystemType alertSystemType = profile.getType(); //수신기 유형
			for (String type : alert.getAddresses()) {
				if(type.equals(alertSystemType.getDescription())) {
					sb.setLength(0);
					sb.append("수신기 유형: ").append(type).append("\n").
					append("경보내용: ").append("\n").
					append(alert.getDescription(0));
					
					String text = sb.toString();
					target.setText(text);
					System.out.println(text);

				}
			}
			
			//3.이미지/오디오 처리
			String capability = profile.getCapability();
			for(int i = 0; i < alert.getResourceCount(0); i++) {
				//alert.info.resource.mimeType 요소는 첨부된 리소스의 파일 형식을 정의한다.
				if(capability.equals(alert.getMimeType(0, i))) {
					sb.setLength(0);
					sb.append("수신기 표출기능: ").append(capability);
					
					String text = sb.toString();
					target.setText(text);
					System.out.println(text);

					//이미지 or 오디오 처리
				}
			}
			
			//4.파일/DB 저장
			//파일 저장은 쉬움. jFileChooser 혹은 내가 구현한 kr.or.kpew.kieas.issuer.model.XmlReaderAndWriter 를 써보자.
			//http://blog.naver.com/PostView.nhn?blogId=cracker542&logNo=40119977325 jFileChooser는 여기 참조.
			//DB는 알아서 해보자. 파일 저장처럼 cap.xml 통으로 저장할 수 있으면 된다. (cap 요소별 컬럼으로 나눠서 저장할 필요 없다) 시간 부족으로 안해봄.
			
			//5.비디오 처리
			//자바를 이용한 video play 기능 구현
			//java media framework; jmf 라이브러리를 쓰면 된다더라. 시간 부족으로 안해봄.
			//http://newstars.tistory.com/34 여기 참조
						
			//6.자막 처리
			//xuggler 는 영상 인코딩/디코딩 하는 라이브러리 라더라.
			//http://www.xuggle.com/downloads 여기서 다운받아서 해보자.
			//이걸로 영상 위에 자막 올리는게 될 것 같다고 하더라
			//https://stackoverflow.com/questions/17811068/xuggler-can-we-write-text-on-video 스택오버플로 질문 참조
			
		}
		else if(arg instanceof AlertSystemProfile) {
			AlertSystemProfile profile = (AlertSystemProfile)arg;
			frame.setTitle(profile.getSender());
			systemType.setText(profile.getType().getDescription());
		}
	}

	public void clear() {
		alertArea.setText("");
	}
}
