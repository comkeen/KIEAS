package kr.ac.uos.ai.ieas.db.dbModel;

public class DisasterType
{
	private DisasterType() 
	{
	};

	public class NaturalDisaster {

		private NaturalDisaster()
		{
			
		};

		public static final String FLOOD = "홍수";
		public static final String RAINFALL = "폭우";
		public static final String DROUGHT = "가뭄";
		public static final String THAW = "해빙";
		public static final String TYPHOON = "태풍";
		public static final String WINDWAVE = "강풍";
		public static final String SNOWFALL = "대설";
		public static final String YELLOW_SAND = "황사";
		public static final String EARTHQUAKE = "지진";
		public static final String TSUNAMI = "지진해일";
		public static final String REDTIDE = "적조";

	}

	public class SocialDisaster {

		private SocialDisaster()
		{
			
		};

		public static final String FIRE = "화재";
		public static final String EXPLOSION = "폭발";
		public static final String COLLAPSE = "붕괴";
		public static final String TRAFFIC_ACCIDENT = "교통사고";
		public static final String BLACKOUT = "정전";
		public static final String TERRORISM = "테러";
	}
}
