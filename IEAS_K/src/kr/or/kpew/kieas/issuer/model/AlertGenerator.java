package kr.or.kpew.kieas.issuer.model;

import kr.or.kpew.kieas.common.KieasMessageBuilder;


public class AlertGenerator
{	
	private KieasMessageBuilder kieasMessageBuilder;

	/**
	 * CAP 메시지를 다루기 위해 사용되는 KieasMessageBuilder 객체 생성.
	 * @param _AlerterModelManager Model들을 관리하는 ModelManager. 
	 */
	public AlertGenerator()
	{
		this.kieasMessageBuilder = new KieasMessageBuilder();
		
		System.out.println("AlertGenerator instantiated");

		init();
	}

	private void init()
	{
	
	}
	
	public String getMessage()
	{
		return kieasMessageBuilder.getMessage();
	}

	public void setMessage(String message)
	{
		this.kieasMessageBuilder.setMessage(message);
	}

//	public void setModelProperty(String target, String value)
//	{
//		String memberName = transformToMemberName(target);
//
//		try
//		{			
//			this.getClass().getDeclaredField(memberName).set(this, value);
//
//			for (Object component : mViewComponentProperties)
//			{
//				if (component instanceof HashMap<?, ?>)
//				{
//					
////					if(memberName.equals("mCategory"))
////					{
////						((HashMap<String, String>) component).replace(target, value);
////						alerterModelManager.updateView(mViewName, target, value);
////						return;
////					}
//					((Map<String, String>) component).replace(target, value);
//					alerterModelManager.updateView(mViewName, target, value);
//					return;
//				}
//				if (component instanceof Vector<?>)
//				{
//					
//					for (Map<String, String> hashMap : (Vector<Map<String, String>>) component)
//					{
//						if(hashMap.containsKey(target))
//						{
//							hashMap.replace(target, value);
//							alerterModelManager.updateView(mViewName, target, value);
//							return;
//						}
//					}
//				}
//			}
//		}
//		catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e) 
//		{
//			System.out.println("there is no such a memberName " + memberName);
//			e.printStackTrace();
//			return;
//		}
//		System.out.println("there is no such a ModelPropertyName " + target);
//	}
//
//	private String transformToMemberName(String target)
//	{
//		target = "m" + target.replaceAll("[0-9]+", "").trim();
//		return target;
//	}
}
