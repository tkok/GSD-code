package dk.itu.kben.gsd;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class IfStatement_BuildingDAO_Test 
{
	@Before
	public void setupDatabase() 
	{
		BuildingDAO.FillTable();
	}

	@Test
	public void PrintHashtableFromDB() 
	{
		Assert.assertNotNull(BuildingDAO.hashtable);
	}
}