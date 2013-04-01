package dk.itu.kben.gsd;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import dk.itu.kben.gsd.persistence.BuildingDAO;

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
		Assert.assertNotNull(BuildingDAO.isEmpty());
	}
}