package sml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/*
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 */
public class Translator {

	// word + line is the part of the current line that's not yet processed
	// word has no whitespace
	// If word and line are not empty, line begins with whitespace
	private String line = "";
	private Labels labels; // The labels of the program being translated
	private ArrayList<Instruction> program; // The program to be created
	private String fileName; // source file of SML code

	private static final String SRC = "src";

	public Translator(String fileName) {
		this.fileName = SRC + "/" + fileName;
	}

	// translate the small program in the file into lab (the labels) and
	// prog (the program)
	// return "no errors were detected"
	public boolean readAndTranslate(Labels lab, ArrayList<Instruction> prog) {

		try (Scanner sc = new Scanner(new File(fileName))) {
			// Scanner attached to the file chosen by the user
			labels = lab;
			labels.reset();
			program = prog;
			program.clear();

			try {
				line = sc.nextLine();
			} catch (NoSuchElementException ioE) {
				return false;
			}

			// Each iteration processes line and reads the next line into line
			while (line != null) {
				// Store the label in label
				String label = scan();

				if (label.length() > 0) {
					Instruction ins = getInstruction(label);
					if (ins != null) {
						labels.addLabel(label);
						program.add(ins);
					}
				}

				try {
					line = sc.nextLine();
				} catch (NoSuchElementException ioE) {
					return false;
				}
			}
		} catch (IOException ioE) {
			System.out.println("File: IO error " + ioE.getMessage());
			return false;
		}
		return true;
	}

	// line should consist of an MML instruction, with its label already
	// removed. Translate line into an instruction with label label
	// and return the instruction
	public Instruction getInstruction(String label) {		
//		int s1; // Possible operands of the instruction
//		int s2;
//		int r;
//		int x;

		if (line.equals(""))
			return null;

		String ins = scan();
		
		// check for duplicate label, if found, ignore the repeated instruction;
		// or we can exit the program by calling system.exit after displaying an error message
		for(Instruction instr: program){ 
			if(instr.label.equalsIgnoreCase(label)){
				System.out.println("Label repeated. Correct the instruction with label: " + label);
				System.out.println("Ignoring the instruction.");
				System.exit(0);
			}
		}
			
		String operand = scan(); // scan the first parameter/operand from the instruction
		List<String> opList = new ArrayList<String>();
		while(!operand.equals("")){	// accumulate the operands in a list
			opList.add(operand);
			operand = scan();	// scan the next operand from the instruction
		}
		
		// if instruction is bnz, check if the label points to an existing instruction label in the program instructions
		if(ins.equals("bnz")){
			String jumpToLbl; // label of instruction to jump to if the condition in bnz instruction is true
			boolean found = false; // if instruction, to jump to, is found
			if(opList.get(1) != null && opList.get(1).length() > 0){
				jumpToLbl = opList.get(1);
				for(Instruction instr: program){ 
					if(instr.label.equalsIgnoreCase(jumpToLbl)){
						found = true;
						break;
					}
				}
				if(!found){
					System.out.println("Bnz instruction should point to an existing instruction's label. Correct instruction with label: " + label);
					System.out.println("Terminating program execution. Correct the program and run again.");
					System.exit(0);
				}
			}
		}
		
		try {
			// supports sml.***Instruction format for instruction subclass name
			Class<?> instClass = Class.forName("sml."+ ins.substring(0, 1).toUpperCase() + ins.substring(1)	+ "Instruction");
			Constructor<?> instConstr = instClass.getConstructor(new Class[]{String.class, String.class, List.class});
			// create a new instance of instruction and pass it label , opcode and operands list
			Object instrObj =  instConstr.newInstance(new Object[]{label, ins, opList});  // calls the constructor with the given params	
			if(instrObj instanceof Instruction)
				return (Instruction) instrObj;
			return null;
		} catch(ClassNotFoundException cnfEx){
			System.out.println("Invalid Instruction Type. Correct the instruction with label: " + label);
			System.out.println("Terminating program execution. Correct the program and run again.");
			System.exit(0);
		} catch(InvocationTargetException iTE){
			if(iTE.getCause() instanceof NumberFormatException){
				System.out.println("Invalid Operand value. Correct the instruction with label: " + label);
				System.out.println("Terminating program execution. Correct the program and run again.");
				System.exit(0);
			} else{
				System.out.println(iTE.getTargetException().getMessage());
				System.out.println("Terminating program execution. Correct the program and run again.");
				System.exit(0);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

//		switch (ins) {
//		case "add":
//			r = scanInt();
//			s1 = scanInt();
//			s2 = scanInt();
//			return new AddInstruction(label, r, s1, s2);
//		case "lin":
//			r = scanInt();
//			s1 = scanInt();
//			return new LinInstruction(label, r, s1);
//		case "sub":
//			r = scanInt();
//			s1 = scanInt();
//			s2 = scanInt();
//			return new SubInstruction(label, r, s1, s2);
//		case "mul":
//			r = scanInt();
//			s1 = scanInt();
//			s2 = scanInt();
//			return new MulInstruction(label, r, s1, s2);
//		case "div":
//			r = scanInt();
//			s1 = scanInt();
//			s2 = scanInt();
//			return new DivInstruction(label, r, s1, s2);
//		case "out":
//			r = scanInt();
//			return new OutInstruction(label, r);
//		case "bnz":
//			r = scanInt();
//			jumpToLabel = scan();
//			return new BnzInstruction(label, r, jumpToLabel);
//		}

		return null;
	}

	/*
	 * Return the first word of line and remove it from line. If there is no
	 * word, return ""
	 */
	private String scan() {
		line = line.trim();
		if (line.length() == 0)
			return "";

		int i = 0;
		while (i < line.length() && line.charAt(i) != ' '
				&& line.charAt(i) != '\t') {
			i = i + 1;
		}
		String word = line.substring(0, i);
		line = line.substring(i);
		return word;
	}

	// Return the first word of line as an integer. If there is
	// any error, return the maximum int
	private int scanInt() {
		String word = scan();
		if (word.length() == 0) {
			return Integer.MAX_VALUE;
		}

		try {
			return Integer.parseInt(word);
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
	}
}