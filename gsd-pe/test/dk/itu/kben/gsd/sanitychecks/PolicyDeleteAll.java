package dk.itu.kben.gsd.sanitychecks;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.itu.policyengine.persistence.DataAccessLayer;

public class PolicyDeleteAll {

	@Test
	public void delete(){
		DataAccessLayer.deleteAll();
		assertEquals(0, DataAccessLayer.getAllPolicies().getSize());
	}
}
