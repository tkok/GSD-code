package dk.itu.kben.gsd.sanitychecks;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.itu.kben.gsd.persistence.BuildingDAL;

public class PolicyDeleteAll {

	@Test
	public void delete(){
		BuildingDAL.deleteAll();
		assertEquals(0, BuildingDAL.getAllPolicies().getSize());
	}
}
