package dk.itu.kben.gsd.domain;

public class FloatValue implements Value{

	Float floatValue;
	
	public FloatValue(){
		
	}
	
	public FloatValue(float value){
		floatValue = new Float(value);
	}
	@Override
	public int compareTo(Value aValue) {
		float iAValue = ((FloatValue) aValue).getValue().floatValue();
		float iTheValue = floatValue.floatValue();
		
		if (iAValue == iTheValue) return 0;
		if (iAValue < iTheValue) return 1;
		if (iAValue < iTheValue) return -1;
		
		return -1;
	}
	public void setValue(Float value){
		this.floatValue = value;
	}

	@Override
	public Float getValue() {
		// TODO Auto-generated method stub
		return floatValue;
	}
	
	
}
