package dk.itu.kben.gsd.sanitychecks;

import static org.junit.Assert.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dk.itu.kben.gsd.domain.Policy;
import dk.itu.kben.gsd.domain.PolicyEntities;
import dk.itu.kben.gsd.domain.PolicyEntity;
import dk.itu.kben.gsd.persistence.BuildingDAL;

public class PolicyUpdateTest {
	List<PolicyEntity> policyEntity;
	
	@Before
	public void set(){
		policyEntity = new ArrayList<PolicyEntity>();
		policyEntity = BuildingDAL.getActivePolicies().getPolicyEntities();
	}
	@Test
	public void test() {
		String name = "sasa";
		PolicyEntity policy = policyEntity.get(0);
		long id = policy.getId();
		policy.setName(name);
		BuildingDAL.persist(policy);
		
	}

}
