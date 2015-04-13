package cn.imqiba.account;

public class AccountEight implements IAccount{
	
	public AccountEight()
	{
		
	}
	
	public String MatchPrettyMode(long account)
	{
		if(IsMatch_ABCDABCX(account))
		{
			//System.out.println("ABCDABCX : " + account);
			return "ABCDABCX";
		}
		
		if(IsMatch_AABBCCDD(account))
		{
			//System.out.println("AABBCCDD : " + account);
			return "AABBCCDD";
		}
		
		if(IsMatch_AAAAAAXX(account))
		{
			//System.out.println("AAAAAAXX : " + account);
			return "AAAAAAXX";
		}
		
		if(IsMatch_AABBBBAX(account))
		{
			//System.out.println("AABBBBAX : " + account);
			return "AABBBBAX";
		}
		
		if(IsMatch_ABABCDCD(account))
		{
			//System.out.println("ABABCDCD : " + account);
			return "ABABCDCD";
		}
		
		if(IsMatch_ABCDEFXX(account))
		{
			//System.out.println("ABCDEFXX : " + account);
			return "ABCDEFXX";
		}
		
		if(IsMatch_ABCDDCBA(account))
		{
			//System.out.println("ABCDDCBA : " + account);
			return "ABCDDCBA";
		}
		
		if(IsMatch_ABCABCXX(account))
		{
			//System.out.println("ABCABCXX : " + account);
			return "ABCABCXX";
		}
		
		//12346789
		if(IsMatch_ABCDFJHI(account))
		{
			//System.out.println("ABCDFJHI : " + account);
			return "ABCDFJHI";
		}
		
		if(IsMatch_OnlyAB(account))
		{
			//System.out.println("OnlyAB : " + account);
			return "OnlyAB";
		}
		
		return null;
	}
	
	public void MakeAccount(long start, long end, long[] arrNormalAccount, long[] arrPrettyAccount, String[] arrMatchMode)
	{
		long count = end - start;
		int normalCount = 0;
		int prettyCount = 0;
		for(int i = 0; i < count; ++i)
		{
			long account = start + i;
			String matchMode = MatchPrettyMode(account);
			if(matchMode != null)
			{
				arrPrettyAccount[prettyCount] = account;
				arrMatchMode[prettyCount] = matchMode;
				prettyCount++;
			}
			else
			{
				arrNormalAccount[normalCount++] = account;
			}
		}
	}
	
	protected boolean IsMatch_ABCDABCX(long account)
	{
		long high = account / 10000;
		long low = account % 10000;
		if(Math.abs(high - low) <= 10)
		{
			return true;
		}
		return false;
	}
	
	
	protected boolean IsMatch_AABBCCDD(long account)
	{
		for(int i = 0; i < 4; ++i)
		{
			int num = (int)(account / (long)Math.pow(100, i));
			num = num % 100;
			
			int high = num / 10;
			int low = num % 10;
			if(high != low)
			{
				return false;
			}
		}
		return true;
	}
	
	protected boolean IsMatch_AAAAAAXX(long account)
	{
		long num = account;
		//XXAAAAAA
		num = account % 1000000;
		if(num % 111111 == 0)
		{
			return true;
		}
		
		//XAAAAAAX
		num = account % 10000000;
		num = num / 10;
		if(num % 111111 == 0)
		{
			return true;
		}
		
		//AAAAAAXX
		num = account / 100;
		if(num % 111111 == 0)
		{
			return true;
		}
		
		return false;
	}
	
	protected boolean IsMatch_AABBBBAX(long account)
	{
		long high = account / 10000;
		long low = account % 10000;
		
		long highHigh = high / 100;
		long highLow = high % 100;
		
		long lowHigh = low / 100;
		long lowLow = low % 100;
		
		if(highLow == lowHigh)
		{
			if(highLow % 11 != 0)
			{
				return false;
			}
			
			if(Math.abs(highHigh - lowLow) <= 10)
			{
				return true;
			}
		}

		return false;
	}

	protected boolean IsMatch_ABABCDCD(long account)
	{
		long high = account / 10000;
		long low = account % 10000;
		
		long highHigh = high / 100;
		long highLow = high % 100;
		if(highHigh != highLow)
		{
			return false;
		}
		
		long lowHigh = low / 100;
		long lowLow = low % 100;
		if(lowHigh != lowLow)
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean IsMatch_ABCDEFXX(long account)
	{
		long num;
		//XXABCDEF
		long arrSufMode[] = {12345, 123456, 234567, 345678, 456789};
		num = account % 1000000;
		for(int i = 0; i < arrSufMode.length; ++i)
		{
			if(num == arrSufMode[i])
			{
				return true;
			}
		}
		
		//XABCDEFX
		long arrMidMode[] = {12345, 123456, 234567, 345678, 456789};
		num = account % 10000000;
		num = num / 10;
		for(int i = 0; i < arrMidMode.length; ++i)
		{
			if(num == arrMidMode[i])
			{
				return true;
			}
		}
		
		//ABCDEFXX
		long arrPreMode[] = {123456, 234567, 345678, 456789};
		num = account / 100;
		for(int i = 0; i < arrPreMode.length; ++i)
		{
			if(num == arrPreMode[i])
			{
				return true;
			}
		}
		
		return false;
	}
	
	protected boolean IsMatch_ABCDDCBA(long account)
	{
		long high = account / 10000;
		long low = account % 10000;
		
		//ABCD
		long arrMode[] = {1234, 2345, 3456, 5678, 6789};
		for(int i = 0; i < arrMode.length; ++i)
		{
			if(high == arrMode[i])
			{
				low = (low / 1000) + (((low / 100) % 10) * 10) + (((low / 10) % 10) * 100) + ((low % 10) * 1000);
				if(high == low)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	//12346789
	protected boolean IsMatch_ABCDFJHI(long account)
	{
		long high = account / 10000;
		long low = account % 10000;
		
		//ABCD
		long arrMode[] = {1234, 2345, 3456, 5678, 6789};
		for(int i = 0; i < arrMode.length; ++i)
		{
			if(high == arrMode[i])
			{
				for(int j = 0; j < arrMode.length; ++j)
				{
					if(low == arrMode[j])
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	protected boolean IsMatch_OnlyAB(long account)
	{
		int a = -1;
		int b = -1;
		for(int i = 0; i < 8; ++i)
		{
			int num = (int)(account / (long)Math.pow(10, i));
			num = num % 10;
			if(a == -1)
			{
				a = num;
			}
			else
			{
				if(b == -1)
				{
					b = num;
				}
				else if((a != num) && (b != num))
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	protected boolean IsMatch_ABCABCXX(long account)
	{
		long num;
		long high;
		long low;
		//XXABCABC
		long arrSufMode[] = {12, 123, 234, 345, 456, 567, 678, 789};
		num = account % 1000000;
		high = num / 1000;
		low = num % 1000;
		if(high != low)
		{
			return false;
		}
		for(int i = 0; i < arrSufMode.length; ++i)
		{
			if(high == arrSufMode[i])
			{
				return true;
			}
		}
		
		//XABCABCX
		long arrMidMode[] = {12, 123, 234, 345, 456, 567, 678, 789};
		num = account % 10000000;
		num = num / 10;
		high = num / 1000;
		low = num % 1000;
		if(high != low)
		{
			return false;
		}
		for(int i = 0; i < arrMidMode.length; ++i)
		{
			if(high == arrMidMode[i])
			{
				return true;
			}
		}
		
		//ABCABCXX
		long arrPreMode[] = {123, 234, 345, 456, 567, 678, 789};
		num = account / 100;
		high = num / 1000;
		low = num % 1000;
		if(high != low)
		{
			return false;
		}
		for(int i = 0; i < arrPreMode.length; ++i)
		{
			if(high == arrPreMode[i])
			{
				return true;
			}
		}
		
		return false;
	}
}
