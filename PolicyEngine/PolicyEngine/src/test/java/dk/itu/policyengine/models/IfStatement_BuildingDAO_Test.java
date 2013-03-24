package dk.itu.policyengine.models;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class IfStatement_BuildingDAO_Test 
{
	@Before
	public void setupDatabase() 
	{
		BuildingDAO.fillTable();
	}

	@Test
	public void PrintHashtableFromDB() 
	{
		Assert.assertNotNull(BuildingDAO.hashtable);
	}
}