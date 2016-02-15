package kr.ac.uos.ai.ieas.TestCase;

import static org.junit.Assert.*;

import org.junit.Test;

import kr.ac.uos.ai.ieas.resource.IKieasMessageBuilder;
import kr.ac.uos.ai.ieas.resource.KieasMessageBuilder;

public class TestCase
{
	@Test
	public void KieasMessageBuilderTest()
	{
		IKieasMessageBuilder kieasMessageBuilder1 = new KieasMessageBuilder();
		IKieasMessageBuilder kieasMessageBuilder2 = new KieasMessageBuilder();
		String str = kieasMessageBuilder1.buildDefaultMessage();
		System.out.println("str = " + str);
		kieasMessageBuilder2.setMessage(str);
		String str2 = kieasMessageBuilder2.getMessage();
		System.out.println("str2 = " + str2);
		assertEquals(true, str == str2);
		
//		fail("Not yet implemented");
	}
}
