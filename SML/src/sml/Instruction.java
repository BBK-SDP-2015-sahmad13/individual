package sml;

import java.util.List;

/**
 * This class is the superclass of the classes for machine instructions
 * 
 * @author someone
 */


public abstract class Instruction {
	protected String label;
	protected String opcode;
	protected List<String> operands;

	// Constructor: an instruction with label l and opcode op
	// (op must be an operation of the language)
	// operands is the list of parameters passed with the instruction e.g. the register name(s) or label name to jump to
	public Instruction(String l, String op, List<String> operands) {
		this.label = l;
		this.opcode = op;
		this.operands = operands;
	}
	
	// Constructor: an instruction with label l and opcode op
	// (op must be an operation of the language)
//	public Instruction(String l, String op) {
//		this.label = l;
//		this.opcode = op;
//	}
	
	

	// = the representation "label: opcode" of this Instruction

	@Override
	public String toString() {
		return label + ": " + opcode;
	}

	// Execute this instruction on machine m.

	public abstract void execute(Machine m);
}