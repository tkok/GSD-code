package dk.itu.policyengine.expressionlanguage;

import static org.junit.Assert.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dk.itu.policyengine.domain.Policy;
import dk.itu.policyengine.domain.PolicyEntities;
import dk.itu.policyengine.domain.PolicyEntity;
import dk.itu.policyengine.persistence.DataAccessLayer;

public class PolicyUpdateTest {
	List<PolicyEntity> policyEntity;
	
	@Before
	public void set(){
		policyEntity = new ArrayList<PolicyEntity>();
		policyEntity = DataAccessLayer.getActivePolicies().getPolicyEntities();
	}
	@Test
	public void test() {
		String name = "sasa";
		PolicyEntity policy = policyEntity.get(0);
		long id = policy.getId();
		policy.setName(name);
		DataAccessLayer.persist(policy);
		
	}

}
