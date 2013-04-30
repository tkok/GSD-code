package dk.itu.policyengine.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class PolicyEntities {
	
	List<PolicyEntity> policyEntities;
	
	public PolicyEntities() {
		policyEntities = new ArrayList<PolicyEntity>();
	}
	
	public void add(PolicyEntity policyEntity) {
		policyEntities.add(policyEntity);
	}
	
	public int getSize() {
		return policyEntities.size();
	}
	
	public List<PolicyEntity> getPolicyEntities() {
		return policyEntities;
	}
	
	public String toJSON() {
		Gson gson = GsonFactory.getInstance();
		
		return gson.toJson(policyEntities);
	}
}