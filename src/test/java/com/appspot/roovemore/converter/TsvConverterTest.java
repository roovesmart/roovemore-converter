package com.appspot.roovemore.converter;

import junit.framework.TestCase;

public class TsvConverterTest extends TestCase {

	public void test_normal_convertTsvArray_1()
			throws  NullPointerException, InstantiationException, IllegalArgumentException, IllegalAccessException {

		String expected_email_1 = "email1";
		String expected_authKbn_1 = "authKbn1";
		String expected_authKey_1 = "authKey1";
		String expected_password_1 = "password1";
		String expected_nickname_1 = "nickname1";

		Member entity = new Member();
		entity.email = expected_email_1;
		entity.authKbn = expected_authKbn_1;
		entity.authKey = expected_authKey_1;
		entity.password = expected_password_1;
		entity.nickname = expected_nickname_1;

		TsvConverter<Member> tsvConv = new TsvConverter<Member>();
		String[] array = tsvConv.convertTsvArray(entity);

		assertEquals(expected_authKey_1, array[0]);
		assertEquals(expected_nickname_1, array[1]);
		assertEquals(expected_email_1, array[2]);
		assertEquals(expected_password_1, array[3]);
		assertEquals(expected_authKbn_1, array[4]);

		Member ret = tsvConv.convertEntity(array, Member.class);
		log(ret.authKbn);
		log(ret.authKey);
		log(ret.email);
		log(ret.nickname);
		log(ret.password);

		assertEquals(expected_authKey_1, ret.authKey);
		assertEquals(expected_nickname_1, ret.nickname);
		assertEquals(expected_email_1, ret.email);
		assertEquals(expected_password_1, ret.password);
		assertEquals(expected_authKbn_1, ret.authKbn);
	}

	public void test_normal_convertTsvArray_2()
			throws  NullPointerException, InstantiationException, IllegalArgumentException, IllegalAccessException {

		String expected_email_1 = "email1";
		String expected_authKbn_1 = "authKbn1";
		String expected_authKey_1 = null; // Test null
		String expected_password_1 = ""; // Test empty
		String expected_nickname_1 = "nickname1";

		Member entity = new Member();
		entity.email = expected_email_1;
		entity.authKbn = expected_authKbn_1;
		entity.authKey = expected_authKey_1;
		entity.password = expected_password_1;
		entity.nickname = expected_nickname_1;

		TsvConverter<Member> tsvConv = new TsvConverter<Member>();
		String[] array = tsvConv.convertTsvArray(entity);

		log("convertTsvArray result : array =");
		log(array[0]);
		log(array[1]);
		log(array[2]);
		log(array[3]);
		log(array[4]);

		assertEquals(tsvConv.tsvEmptyStr, array[0]);
		assertEquals(expected_nickname_1, array[1]);
		assertEquals(expected_email_1, array[2]);
		assertEquals(expected_password_1, array[3]);
		assertEquals(expected_authKbn_1, array[4]);

		Member ret = tsvConv.convertEntity(array, Member.class);

		log("convertEntity result : array =");
		log(ret.authKbn);
		log(ret.authKey);
		log(ret.email);
		log(ret.nickname);
		log(ret.password);

		assertEquals(expected_authKey_1, ret.authKey);
		assertEquals(expected_nickname_1, ret.nickname);
		assertEquals(expected_email_1, ret.email);
		assertEquals(expected_password_1, ret.password);
		assertEquals(expected_authKbn_1, ret.authKbn);
	}

	public void test_normal_convertTsvArray_3()
			throws  NullPointerException, InstantiationException, IllegalArgumentException, IllegalAccessException {

		Integer expected_Integer1 = 1;
		int expected_int2 = 2;
		Long expected_Long3 = 3L;
		String expected_String4 = "String4";

		Member2 entity = new Member2();

		entity.Integer1 = expected_Integer1;
		entity.int2 = expected_int2;
		entity.Long3 = expected_Long3;
		entity.String4 = expected_String4;

		TsvConverter<Member2> tsvConv = new TsvConverter<Member2>();
		String[] array = tsvConv.convertTsvArray(entity);

		log(array[0]);
		log(array[1]);
		log(array[2]);
		log(array[3]);

		assertEquals( String.valueOf( expected_Integer1 ), array[0]);
		assertEquals( String.valueOf( expected_int2 ), array[1]);
		assertEquals( String.valueOf( expected_Long3 ), array[2]);
		assertEquals( String.valueOf( expected_String4 ), array[3]);

		Member2 ret = tsvConv.convertEntity(array, Member2.class);

		assertEquals(expected_Integer1, ret.Integer1);
		assertEquals(expected_int2, ret.int2);
		assertEquals(expected_Long3, ret.Long3);
		assertEquals(expected_String4, ret.String4);

	}


	public static class Member {

		@Tsv(no = 1)
		public String authKey;

		@Tsv(no = 2)
		public String nickname;

		@Tsv(no = 3)
		public String email;

		@Tsv(no = 4)
		public String password;

		@Tsv(no = 5)
		public String authKbn;

	}

	public static class Member2 {

		@Tsv(no = 1)
		public Integer Integer1;

		@Tsv(no = 2)
		public int int2;

		@Tsv(no = 3)
		public Long Long3;

		@Tsv(no = 4)
		public String String4;

	}


	private void log(Object msg){
		System.out.println(msg);
	}

}
