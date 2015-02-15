package sml;

import java.util.List;

/**
 * This class ....
 * 
 * @author someone
 */

public class LinInstruction extends Instruction {
	private int register;
	private int value;

//	public LinInstruction(String label, String opcode) {
//		super(label, opcode);
//	}
//
//	public LinInstruction(String label, int register, int value) {
//		super(label, "lin");
//		this.register = register;
//		this.value = value;
//	}
	
	public LinInstruction(String label, String op, List<String> operands) throws Exception {
		super(label, "lin", operands);
		if(Integer.valueOf(operands.get(0)) < 0 || Integer.valueOf(operands.get(0)) > 31){
			throw new Exception("Invalid register number for instruction with label: " + label);
		} else if(operands.size() > 2){
			throw new Exception("Invalid number of operands for instruction with label: " + label);
		}
		this.register = Integer.valueOf(operands.get(0));
		this.value = Integer.valueOf(operands.get(1));
	}
	
	

	@Override
	public void execute(Machine m) {
		m.getRegisters().setRegister(register, value);
	}

	@Override
	public String toString() {
		return super.toString() + " register " + register + " value is " + value;
	}
}
