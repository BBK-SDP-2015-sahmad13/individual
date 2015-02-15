package sml;

import java.util.List;

/**
 * This class ....
 * 
 * @author someone
 */

public class OutInstruction extends Instruction {

	private int register;
	
//	public OutInstruction(String label, String op) {
//		super(label, op);
//	}
//
//	public OutInstruction(String label, int register) {
//		this(label, "out");
//		this.register = register;
//	}
	
	public OutInstruction(String label, String op, List<String> operands) throws Exception {
		super(label, "out", operands);
		if(Integer.valueOf(operands.get(0)) < 0 || Integer.valueOf(operands.get(0)) > 31){
			throw new Exception("Invalid register number for instruction with label: " + label);
		}
		this.register = Integer.valueOf(operands.get(0));
	}

	@Override
	public void execute(Machine m) {
		int value = m.getRegisters().getRegister(register);
		System.out.println(value);
	}

	@Override
	public String toString() {
		return super.toString() + " " + register + " on Java Console";
	}
}
