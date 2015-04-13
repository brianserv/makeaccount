package cn.imqiba.account;

public interface IAccount {
	
	public String MatchPrettyMode(long account);
	
	public void MakeAccount(long start, long end, long[] arrNormalAccount, long[] arrPrettyAccount, String[] arrMatchMode);

}
