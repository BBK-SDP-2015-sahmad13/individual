package sml;

import java.util.List;

/**
 * This class ....
 * 
 * @author someone
 */

public class MulInstruction extends Instruction {

	private int result;
	private int op1;
	private int op2;

//	public MulInstruction(String label, String op) {
//		super(label, op);
//	}
//
//	public MulInstruction(String label, int result, int op1, int op2) {
//		this(label, "mul");
//		this.result = result;
//		this.op1 = op1;
//		this.op2 = op2;
//	}
	
	public MulInstruction(String label, String op, List<String> operands) throws Exception {
		super(label, "mul", operands);
		if(operands == null || operands.size() != 3){
			throw new Exception("Invalid number of operands for instruction with label: " + label);
		} else if(Integer.valueOf(operands.get(0)) < 0 || Integer.valueOf(operands.get(0)) > 31
				|| Integer.valueOf(operands.get(1)) < 0 || Integer.valueOf(operands.get(1)) > 31
				|| Integer.valueOf(operands.get(2)) < 0 || Integer.valueOf(operands.get(2)) > 31){
			throw new Exception("Invalid register number for instruction with label: " + label);
		}
		this.result = Integer.valueOf(operands.get(0));
		this.op1 = Integer.valueOf(operands.get(1));
		this.op2 = Integer.valueOf(operands.get(2));
	}
	
	
	@Override
	public void execute(Machine m) {
		int value1 = m.getRegisters().getRegister(op1);
		int value2 = m.getRegisters().getRegister(op2);
		m.getRegisters().setRegister(result, value1 * value2);
	}

	@Override
	public String toString() {
		return super.toString() + " " + op1 + " * " + op2 + " to " + result;
	}
}
