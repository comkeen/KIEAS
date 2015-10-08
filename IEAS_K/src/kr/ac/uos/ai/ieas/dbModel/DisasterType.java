package kr.ac.uos.ai.ieas.dbModel;

public class DisasterType {

	private DisasterType() {
	};

	public class NaturalDisaster {

		private NaturalDisaster() {
		};

		public static final String FLOOD = "ȫ��";
		public static final String RAINFALL = "����";
		public static final String DROUGHT = "����";
		public static final String THAW = "�غ�";
		public static final String TYPHOON = "��ǳ";
		public static final String WINDWAVE = "��ǳ";
		public static final String SNOWFALL = "����";
		public static final String YELLOW_SAND = "Ȳ��";
		public static final String EARTHQUAKE = "����";
		public static final String TSUNAMI = "������";
		public static final String REDTIDE = "����";

	}

	public class SocialDisaster {

		private SocialDisaster() {
		};

		public static final String FIRE = "ȭ��";
		public static final String EXPLOSION = "����";
		public static final String CAVEIN = "�ر�";
		public static final String TRAFFIC_ACCIDENT = "������";
		public static final String BLACKOUT = "����";
		public static final String TERRORISM = "�׷�";

	}

}
