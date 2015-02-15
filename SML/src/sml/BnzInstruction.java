package sml;

import java.util.List;

/**
 * This class ....
 * 
 * @author someone
 */

public class BnzInstruction extends Instruction {
	private int register;
	private String jumpToLabel;

//	public BnzInstruction(String label, String opcode) {
//		super(label, opcode);
//	}
//
//	public BnzInstruction(String label, int register, String jumpToLabel) {
//		super(label, "Bnz");
//		this.register = register;
//		this.jumpToLabel = jumpToLabel;
//
//	}

	public BnzInstruction(String label, String op, List<String> operands) throws Exception {
		super(label, "bnz", operands);
		if(operands == null || operands.size() != 2){
			throw new Exception("Invalid number of operands for instruction with label: " + label);
		} else if(Integer.valueOf(operands.get(0)) < 0 || Integer.valueOf(operands.get(0)) > 31){
			throw new Exception("Invalid register number for instruction with label: " + label);
		}
		this.register = Integer.valueOf(operands.get(0));
		this.jumpToLabel = operands.get(1);
	}
	
	
	@Override
	public void execute(Machine m) {
		if(m.getRegisters().getRegister(register) != 0){ // get the registers value/contents
			if(m.getLabels().indexOf(jumpToLabel) != -1) // label is found in the list of labels
				m.setPc(m.getLabels().indexOf(jumpToLabel)); // jump to the instruction with the label
		}
	}

	@Override
	public String toString() {
		return super.toString() + " if " + register + " is non zero, jump to label " + jumpToLabel;
	}
}
