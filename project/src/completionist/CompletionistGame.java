package completionist;

public class CompletionistGame 
{
	
	private static LevelManager levelManager;
	
	public static void main(String[] args)
	{
		levelManager = new LevelManager("The Completionist", 1600, 800);
		levelManager.start();
	}

}
